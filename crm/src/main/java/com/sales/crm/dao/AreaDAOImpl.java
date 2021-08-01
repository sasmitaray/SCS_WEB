package com.sales.crm.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.model.Area;
import com.sales.crm.model.Beat;
import com.sales.crm.model.EntityStatusEnum;

@Repository("areaDAO")
public class AreaDAOImpl implements AreaDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(AreaDAOImpl.class);
	
	
	@Override
	public void create(Area area) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			area.setDateCreated(new Date());
			area.setCode(UUID.randomUUID().toString());
			area.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
			session.save(area);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating area", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}

	
	private Beat getBeatForArea(Session session, int areaID, int tenantID) {
		SQLQuery  beatQuery = session.createSQLQuery("SELECT a.* FROM BEATS a, BEAT_AREA b WHERE a.ID = b.BEAT_ID and a.TENANT_ID = b.TENANT_ID and b.AREA_ID= ? and a.TENANT_ID = ?");
		beatQuery.setParameter(0, areaID);
		beatQuery.setParameter(1, tenantID);
		List beatList = beatQuery.list();
		Beat beat = null;
		if(beatList != null && beatList.size() > 0){
			for (Object obj : beatList) {
				Object[] objs = (Object[]) obj;
				beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setCode(String.valueOf(objs[1]));
				beat.setName(String.valueOf(objs[2]));
				beat.setDescription(String.valueOf(objs[3]));
				beat.setCoverageSchedule(String.valueOf(objs[4]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
				beat.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
			}
		}
		return beat;
	}
	@Override
	public Area get(String areaCode, int tenantID) {
		Session session = null;
		Area area = null;
		Beat beat = null;
		try{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Area where code = :areaCode AND tenantID = :tenantID");
			query.setParameter("areaCode", areaCode);
			query.setParameter("tenantID", tenantID);
			if (query.list() != null) {
				area = (Area)query.list().get(0);
			}
			
			if(area != null) {
				beat = getBeatForArea(session, area.getAreaID(), tenantID);
				if(beat != null) {
					area.setBeat(beat);
				}
			}
			
		}catch(Exception exception){
			logger.error("Error while fetching area details.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return area;
	
	}

	@Override
	public void update(Area area) throws Exception{

		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			area.setDateModified(new Date());
			session.update(area);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while updating area", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	
		
	}

	@Override
	public void delete(String areaCode, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			Area area = get(areaCode, tenantID);
			transaction = session.beginTransaction();
			//Remove from BEAT_AREA
			SQLQuery beatAreaQuery = session.createSQLQuery("DELETE FROM BEAT_AREA WHERE AREA_ID= ? and TENANT_ID = ?");
			beatAreaQuery.setParameter(0, area.getAreaID());
			beatAreaQuery.setParameter(1, tenantID);
			beatAreaQuery.executeUpdate();
			//Delete Area
			session.delete(area);
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deleting area.", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	}

	@Override
	public List<Area> getTenantAreas(int tenantID) {
		Session session = null;
		List<Area> areas = null; 
		try{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Area where tenantID = :tenantID order by DATE_CREATED ASC");
			query.setParameter("tenantID", tenantID);
			areas = query.list();
			for(Area area : areas) {
				area.setBeat(getBeatForArea(session, area.getAreaID(), tenantID));
			}
		}catch(Exception exception){
			logger.error("Error while getting tenant Areas", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return areas;
	}

	@Override
	public List<Area> getBeatAreas(int beatID, int tenantID) {

		Session session = null;
		List<Area> areaList = new ArrayList<Area>();
		try {
			session = sessionFactory.openSession();
			//Get Areas
			SQLQuery areasQuery = session.createSQLQuery("SELECT b.BEAT_ID, a.* FROM AREAS a, BEAT_AREA b WHERE a.ID = b.AREA_ID AND a.TENANT_ID = b.TENANT_ID AND b.BEAT_ID= ? AND a.TENANT_ID = ?");
			areasQuery.setParameter(0, beatID);
			areasQuery.setParameter(1, tenantID);
			List areas = areasQuery.list();
			for (Object obj : areas) {
				Object[] objs = (Object[]) obj;
				Area area = new Area();
				area.setAreaID(Integer.valueOf(String.valueOf(objs[1])));
				area.setCode(String.valueOf(objs[2]));
				area.setName(String.valueOf(objs[3]));
				area.setDescription(String.valueOf(objs[4]));
				area.setWordNo(String.valueOf(objs[5]));
				area.setPinCode(String.valueOf(objs[6]));
				area.setTenantID(Integer.valueOf(String.valueOf(objs[7])));
				areaList.add(area);
			}
		} catch (Exception exception) {
			logger.error("Error while getting areas for beat.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return areaList;

	}
	
	@Override
	public List<Area> getTenantAreasNotMappedToBeat(int tenantID) {

		Session session = null;
		List<Area> areaList = new ArrayList<Area>();
		try {
			session = sessionFactory.openSession();
			//Get Areas
			SQLQuery areasQuery = session.createSQLQuery("SELECT * FROM AREAS WHERE ID NOT IN (SELECT AREA_ID FROM BEAT_AREA) AND TENANT_ID= ? ");
			areasQuery.setParameter(0, tenantID);
			List areas = areasQuery.list();
			for (Object obj : areas) {
				Object[] objs = (Object[]) obj;
				Area area = new Area();
				area.setAreaID(Integer.valueOf(String.valueOf(objs[0])));
				area.setCode(String.valueOf(objs[1]));
				area.setName(String.valueOf(objs[2]));
				area.setDescription(String.valueOf(objs[3]));
				area.setWordNo(String.valueOf(objs[4]));
				area.setPinCode(String.valueOf(objs[5]));
				area.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
				areaList.add(area);
			}
		} catch (Exception exception) {
			logger.error("Error while getting areas for beat.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return areaList;

	}
	
	@Override
	public List<Area> getTenantAreasNotMappedToBeatForEdit(int tenantID, int beatID) {

		Session session = null;
		List<Area> areaList = new ArrayList<Area>();
		try {
			session = sessionFactory.openSession();
			//Get Areas
			SQLQuery areasQuery = session.createSQLQuery("SELECT * FROM AREAS WHERE ID NOT IN (SELECT AREA_ID FROM BEAT_AREA) AND TENANT_ID= ? UNION SELECT * FROM AREAS WHERE ID IN (SELECT AREA_ID FROM BEAT_AREA WHERE BEAT_ID= ?)");
			areasQuery.setParameter(0, tenantID);
			areasQuery.setParameter(1, beatID);
			List areas = areasQuery.list();
			for (Object obj : areas) {
				Object[] objs = (Object[]) obj;
				Area area = new Area();
				area.setAreaID(Integer.valueOf(String.valueOf(objs[0])));
				area.setCode(String.valueOf(objs[1]));
				area.setName(String.valueOf(objs[2]));
				area.setDescription(String.valueOf(objs[3]));
				area.setWordNo(String.valueOf(objs[4]));
				area.setPinCode(String.valueOf(objs[5]));
				area.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
				areaList.add(area);
			}
		} catch (Exception exception) {
			logger.error("Error while getting areas for beat.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return areaList;

	}

}
