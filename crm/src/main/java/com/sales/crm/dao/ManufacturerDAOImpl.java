package com.sales.crm.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.exception.CRMException;
import com.sales.crm.model.Address;
import com.sales.crm.model.Beat;
import com.sales.crm.model.DeliveryExecutive;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Manufacturer;
import com.sales.crm.model.ManufacturerAreaManager;
import com.sales.crm.model.ManufacturerBeats;
import com.sales.crm.model.ManufacturerDelivExecs;
import com.sales.crm.model.ManufacturerSalesExecBeats;
import com.sales.crm.model.ManufacturerSalesExecs;
import com.sales.crm.model.ManufacturerSalesOfficer;
import com.sales.crm.model.SalesExecutive;


@Repository("manufacturerDAO")
public class ManufacturerDAOImpl implements ManufacturerDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(ManufacturerDAOImpl.class);
	
	private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

	
	@Override
	public void create(Manufacturer manufacturer) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			manufacturer.setDateCreated(new Date());
			manufacturer.setCode(UUID.randomUUID().toString());
			manufacturer.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
			session.save(manufacturer);
			for(Address address : manufacturer.getAddress()){
				address.setDateCreated(new Date());
				address.setCode(UUID.randomUUID().toString());
				address.setTenantID(manufacturer.getTenantID());
				address.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
				//Save Adddress
				session.save(address);
				//Insert into Manufacturer Address
				SQLQuery query = session.createSQLQuery("INSERT INTO MANUFACTURER_ADDRESS (MANUFACTURER_ID, ADDRESS_ID, TENANT_ID, DATE_CREATED) VALUES (?, ? , ?, CURDATE())");
				query.setInteger(0, manufacturer.getManufacturerID());
				query.setInteger(1, address.getId());
				query.setInteger(2, manufacturer.getTenantID());
				query.executeUpdate();
			}
			
			//Save Sales Officer
			if(manufacturer.getSalesOfficer() != null){
				ManufacturerSalesOfficer salesOfficer = manufacturer.getSalesOfficer();
				SQLQuery insertSalesOff = session.createSQLQuery("INSERT INTO MANUFACTURER_SALES_OFFICER (CODE, NAME, CONTACT_NO, EFFECTIVE_FROM, MANUFACTURER_ID, STATUS_ID, TENANT_ID, DATE_CREATED ) VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE())");
				insertSalesOff.setParameter(0, UUID.randomUUID().toString());
				insertSalesOff.setParameter(1, salesOfficer.getName());
				insertSalesOff.setParameter(2, salesOfficer.getContactNo());
				insertSalesOff.setDate(3, salesOfficer.getEffectiveFrom());
				insertSalesOff.setParameter(4, manufacturer.getManufacturerID());
				insertSalesOff.setParameter(5, EntityStatusEnum.ACTIVE.getEntityStatus());
				insertSalesOff.setParameter(6, manufacturer.getTenantID());
				insertSalesOff.executeUpdate();
			}
			//Save Area Manager
			if(manufacturer.getAreaManager() != null){
				ManufacturerAreaManager areaMgr = manufacturer.getAreaManager();
				SQLQuery insertAreaMgr = session.createSQLQuery("INSERT INTO MANUFACTURER_AREA_MANAGER (CODE, NAME, CONTACT_NO, EFFECTIVE_FROM, MANUFACTURER_ID, STATUS_ID, TENANT_ID, DATE_CREATED ) VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE())");
				insertAreaMgr.setParameter(0, UUID.randomUUID().toString());
				insertAreaMgr.setParameter(1, areaMgr.getName());
				insertAreaMgr.setParameter(2, areaMgr.getContactNo());
				insertAreaMgr.setDate(3, areaMgr.getEffectiveFrom());
				insertAreaMgr.setParameter(4, manufacturer.getManufacturerID());
				insertAreaMgr.setParameter(5, EntityStatusEnum.ACTIVE.getEntityStatus());
				insertAreaMgr.setParameter(6, manufacturer.getTenantID());
				insertAreaMgr.executeUpdate();
			}
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating manufacturer.", e);
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
	
	private List<Address> getManufacturerAddresses(Session session, int manufacturerID, int tenantID) throws Exception {
		// Get Manufacturer Address
		SQLQuery addressQuery = session.createSQLQuery(
				"SELECT a.* FROM Address a, MANUFACTURER_ADDRESS b WHERE a.ID = b.ADDRESS_ID AND b.MANUFACTURER_ID = ? AND b.TENANT_ID=?");
		addressQuery.setParameter(0, manufacturerID);
		addressQuery.setParameter(1, tenantID);
		List addresses = addressQuery.list();
		List<Address> addressList = new ArrayList<Address>(2);
		for (Object obj : addresses) {
			Object[] objs = (Object[]) obj;
			Address address = new Address();
			address.setId(Integer.valueOf(String.valueOf(objs[0])));
			address.setCode(String.valueOf(objs[1]));
			address.setAddressLine1(String.valueOf(objs[2]));
			address.setAddressLine2(String.valueOf(objs[3]));
			address.setStreet(String.valueOf(objs[4]));
			address.setCity(String.valueOf(objs[5]));
			address.setState(String.valueOf(objs[6]));
			address.setCountry(String.valueOf(objs[7]));
			address.setPostalCode(String.valueOf(objs[8]));
			address.setContactPerson(String.valueOf(objs[9]));
			address.setPhoneNumber(String.valueOf(objs[10]));
			address.setMobileNumberPrimary(String.valueOf(objs[11]));
			address.setMobileNumberSecondary(String.valueOf(objs[12]));
			address.setEmailID(String.valueOf(objs[13]));
			address.setAddrressType(Integer.valueOf(String.valueOf(objs[14])));
			address.setStatusID(Integer.valueOf(String.valueOf(objs[15])));
			address.setTenantID(Integer.valueOf(String.valueOf(objs[16])));
			address.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[17])).getTime()));
			if (objs[18] != null) {
				address.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[18])).getTime()));
			}
			addressList.add(address);
		}
		return addressList;
	}

	private List<ManufacturerSalesOfficer> getManufacturerSalesOfficers(Session session, int manufacturerID, int tenantID) {
		// get Sales Officer
		Query soQuery = session.createQuery("FROM ManufacturerSalesOfficer WHERE manufacturerID =:id AND tenantID = :tenantID");
		soQuery.setParameter("id", manufacturerID);
		soQuery.setParameter("tenantID", tenantID);
		List<ManufacturerSalesOfficer> sos = soQuery.list();
		return sos;
	}

	private List<ManufacturerAreaManager> getManufacturerAreaManagers(Session session, int manufacturerID, int tenantID) {
		// get Area Manager
		Query amQuery = session.createQuery("FROM ManufacturerAreaManager WHERE manufacturerID =:id AND tenantID = :tenantID");
		amQuery.setParameter("id", manufacturerID);
		amQuery.setParameter("tenantID", tenantID);
		List<ManufacturerAreaManager> ams = amQuery.list();
		return ams;
	}

	/**
	private List<Integer> getManufacturerManufacturerIDS(Session session, int manufacturerID, int tenantID) {
		// Get Mapped Manufacturers
		SQLQuery manufQuery = session.createSQLQuery(
				"SELECT MANUFACTURER_ID FROM SUPPLIER_MANUFACTURER WHERE SUPPLIER_ID = ? AND TENANT_ID= ? ");
		manufQuery.setParameter(0, manufacturerID);
		manufQuery.setParameter(1, tenantID);
		List<Integer> manufacturerIDs = manufQuery.list();
		return manufacturerIDs;
	}
	**/

	private Map<Integer, Beat> getManufacturerBeats(Session session, int manufacturerID, int tenantID) throws Exception{
		// Beat Id, Beat map
		Map<Integer, Beat> beatsMap = new HashMap<Integer, Beat>();
		SQLQuery beatQuery = session.createSQLQuery(
				"SELECT * FROM BEATS WHERE ID IN (SELECT BEAT_ID FROM MANUFACTURER_BEAT WHERE MANUFACTURER_ID = ?) AND TENANT_ID= ?");
		beatQuery.setParameter(0, manufacturerID);
		beatQuery.setParameter(1, tenantID);
		List resutls = beatQuery.list();
		for (Object obj : resutls) {
			Object[] objs = (Object[]) obj;
			Beat beat = new Beat();
			beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
			beat.setCode(String.valueOf(objs[1]));
			beat.setName(String.valueOf(objs[2]));
			beat.setDescription(String.valueOf(objs[3]));
			beat.setCoverageSchedule(String.valueOf(objs[4]));
			beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
			beat.setStatusID(Integer.valueOf(String.valueOf(objs[6])));
			beat.setTenantID(Integer.valueOf(String.valueOf(objs[7])));
			if (objs[8] != null) {
				beat.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
			}
			if (objs[9] != null) {
				beat.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
			}
			beatsMap.put(beat.getBeatID(), beat);
		}
		return beatsMap;
	}
	
	private List<SalesExecutive> getManufacturerSalexExecs(Session session, int manufacturerID, int tenantID) throws Exception {
		// Get Sales Executives
		List<SalesExecutive> salesExecs = new ArrayList<SalesExecutive>();
		List<Integer> salesExecIDs = new ArrayList<Integer>();
		SQLQuery salesExecsQry = session.createSQLQuery(
				"SELECT a.* FROM USERS a, MANUFACTURER_SALES_EXECUTIVES b WHERE  a.ID = b.SALES_EXEC_ID AND a.TENANT_ID=b.TENANT_ID AND b.MANUFACTURER_ID = ? AND a.TENANT_ID=? ");
		salesExecsQry.setInteger(0, manufacturerID);
		salesExecsQry.setInteger(1, tenantID);
		List lists = salesExecsQry.list();
		for (Object obj : lists) {
			Object[] objs = (Object[]) obj;
			SalesExecutive salesExec = new SalesExecutive();
			salesExec.setUserID(Integer.valueOf(String.valueOf(objs[0])));
			salesExec.setCode(String.valueOf(objs[1]));
			salesExec.setUserName(String.valueOf(objs[2]));
			salesExec.setDescription(String.valueOf(objs[4]));
			salesExec.setEmailID(String.valueOf(objs[5]));
			salesExec.setMobileNo(String.valueOf(objs[6]));
			salesExec.setFirstName(String.valueOf(objs[7]));
			salesExec.setLastName(String.valueOf(objs[8]));
			salesExec.setStatusID(Integer.valueOf(String.valueOf(objs[9])));
			salesExec.setLoggedIn(Integer.valueOf(String.valueOf(objs[10])));
			salesExec.setTenantID(Integer.valueOf(String.valueOf(objs[11])));
			if (objs[12] != null) {
				salesExec.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[12])).getTime()));
			}

			if (objs[13] != null) {
				salesExec.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[13])).getTime()));
			}
			salesExecs.add(salesExec);
			// salesExecIDs.add(salesExec.getUserID());

		}
		return salesExecs;
	}
	
	private List<DeliveryExecutive> getManufacturerDelivExecs(Session session, int manufacturerID, int tenantID) throws Exception {
		// Get Sales Executives
		List<DeliveryExecutive> delivExecs = new ArrayList<DeliveryExecutive>();
		SQLQuery delivExecsQry = session.createSQLQuery(
				"SELECT a.* FROM USERS a, MANUFACTURER_DELIV_EXECUTIVES b WHERE  a.ID = b.DELIV_EXEC_ID AND a.TENANT_ID=b.TENANT_ID AND b.MANUFACTURER_ID = ? AND a.TENANT_ID=? ");
		delivExecsQry.setInteger(0, manufacturerID);
		delivExecsQry.setInteger(1, tenantID);
		List lists = delivExecsQry.list();
		for (Object obj : lists) {
			Object[] objs = (Object[]) obj;
			DeliveryExecutive delivExec = new DeliveryExecutive();
			delivExec.setUserID(Integer.valueOf(String.valueOf(objs[0])));
			delivExec.setCode(String.valueOf(objs[1]));
			delivExec.setUserName(String.valueOf(objs[2]));
			delivExec.setDescription(String.valueOf(objs[4]));
			delivExec.setEmailID(String.valueOf(objs[5]));
			delivExec.setMobileNo(String.valueOf(objs[6]));
			delivExec.setFirstName(String.valueOf(objs[7]));
			delivExec.setLastName(String.valueOf(objs[8]));
			delivExec.setStatusID(Integer.valueOf(String.valueOf(objs[9])));
			delivExec.setLoggedIn(Integer.valueOf(String.valueOf(objs[10])));
			delivExec.setTenantID(Integer.valueOf(String.valueOf(objs[11])));
			if (objs[12] != null) {
				delivExec.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[12])).getTime()));
			}

			if (objs[13] != null) {
				delivExec.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[13])).getTime()));
			}
			delivExecs.add(delivExec);
		
		}
		return delivExecs;
	}

	@Override
	public Manufacturer get(String manufacturerCode, int tenantID) {
		Session session = null;
		Manufacturer manufacturer = null;
		try {
			session = sessionFactory.openSession();
			// manufacturer = (Manufacturer)session.get(Manufacturer.class, manufacturerID);
			Query query = session.createQuery("from Manufacturer where code = :manufacturerCode AND tenantID = :tenantID");
			query.setParameter("manufacturerCode", manufacturerCode);
			query.setParameter("tenantID", tenantID);
			if (query.list() != null && query.list().size() == 1) {
				manufacturer = (Manufacturer) query.list().get(0);

				// Add address to manufacturer
				manufacturer.setAddress(getManufacturerAddresses(session, manufacturer.getManufacturerID(), tenantID));

				// get Sales Officer
				List<ManufacturerSalesOfficer> sos = getManufacturerSalesOfficers(session, manufacturer.getManufacturerID(), tenantID);
				if (sos != null && sos.size() == 1) {
					manufacturer.setSalesOfficer(sos.get(0));

				}
				
				// get Area Manager
				List<ManufacturerAreaManager> ams = getManufacturerAreaManagers(session, manufacturer.getManufacturerID(), tenantID);
				if (ams != null && ams.size() == 1) {
					manufacturer.setAreaManager(ams.get(0));
				}
				
				// Get beats
				List<Beat> beats = new ArrayList<Beat>(getManufacturerBeats(session, manufacturer.getManufacturerID(), tenantID).values());
				if (beats.size() > 0) {
					manufacturer.setBeats(beats);
					manufacturer.setBeatIDs(new ArrayList<Integer>(getManufacturerBeats(session, manufacturer.getManufacturerID(), tenantID).keySet()));
				}
				
		
				// Get Sales Executives
				List<SalesExecutive> salesExecs = getManufacturerSalexExecs(session, manufacturer.getManufacturerID(), tenantID);
				if(salesExecs.size() > 0) {
					List<Integer> salesExecIDs = new ArrayList();
					for(SalesExecutive salesExec : salesExecs) {
						salesExecIDs.add(salesExec.getUserID());
					}
					manufacturer.setSalesExecs(salesExecs);
					manufacturer.setSalesExecsIDs(salesExecIDs);
				}
				
				// Get Delivery Executives
				List<DeliveryExecutive> delivExecs = getManufacturerDelivExecs(session, manufacturer.getManufacturerID(), tenantID);
				if(delivExecs.size() > 0) {
					List<Integer> delivExecIDs = new ArrayList();
					for(DeliveryExecutive delivExec : delivExecs) {
						delivExecIDs.add(delivExec.getUserID());
					}
					manufacturer.setDelivExecs(delivExecs);
					manufacturer.setDelivExecsIDs(delivExecIDs);
				}
			}

		} catch (Exception exception) {
			logger.error("Error while fetching manufacturer details", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return manufacturer;
	}
	
	@Override
	public Manufacturer getById(int manufacturerID, int tenantID) {
		Session session = null;
		Manufacturer manufacturer = null;
		try {
			session = sessionFactory.openSession();
			// manufacturer = (Manufacturer)session.get(Manufacturer.class, manufacturerID);
			Query query = session.createQuery("from Manufacturer where manufacturerID = :manufacturerID AND tenantID = :tenantID");
			query.setParameter("manufacturerID", manufacturerID);
			query.setParameter("tenantID", tenantID);
			if (query.list() != null && query.list().size() == 1) {
				manufacturer = (Manufacturer) query.list().get(0);

				// Add address to customer
				manufacturer.setAddress(getManufacturerAddresses(session, manufacturerID, tenantID));

				// get Sales Officer
				List<ManufacturerSalesOfficer> sos = getManufacturerSalesOfficers(session, manufacturerID, tenantID);
				if (sos != null && sos.size() == 1) {
					manufacturer.setSalesOfficer(sos.get(0));

				}
				
				// get Area Manager
				List<ManufacturerAreaManager> ams = getManufacturerAreaManagers(session, manufacturerID, tenantID);
				if (ams != null && ams.size() == 1) {
					manufacturer.setAreaManager(ams.get(0));
				}
				
				// Get beats
				List<Beat> beats = new ArrayList<Beat>(getManufacturerBeats(session, manufacturerID, tenantID).values());
				if (beats.size() > 0) {
					manufacturer.setBeats(beats);
					manufacturer.setBeatIDs(new ArrayList<Integer>(getManufacturerBeats(session, manufacturerID, tenantID).keySet()));
				}

				// Get Sales Executives
				List<SalesExecutive> salesExecs = getManufacturerSalexExecs(session, manufacturerID, tenantID);
				if(salesExecs.size() > 0) {
					List<Integer> salesExecIDs = new ArrayList();
					for(SalesExecutive salesExec : salesExecs) {
						salesExecIDs.add(salesExec.getUserID());
					}
					manufacturer.setSalesExecs(salesExecs);
					manufacturer.setSalesExecsIDs(salesExecIDs);
				}
			}

		} catch (Exception exception) {
			logger.error("Error while fetching manufacturer details", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return manufacturer;
	}

	@Override
	public void update(Manufacturer manufacturer) throws Exception{

		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			manufacturer.setDateModified(new Date());
			for(Address address : manufacturer.getAddress()){
				address.setDateModified(new Date());
			}
			session.update(manufacturer);
			//Save Sales Officer
			if(manufacturer.getSalesOfficer() != null){
				ManufacturerSalesOfficer salesOfficer = manufacturer.getSalesOfficer();
				SQLQuery updateSalesOff = session.createSQLQuery("UPDATE MANUFACTURER_SALES_OFFICER SET NAME= ?, CONTACT_NO = ?, DATE_MODIFIED = ? WHERE ID = ? AND MANUFACTURER_ID = ? AND TENANT_ID = ?");
				updateSalesOff.setParameter(0, salesOfficer.getName());
				updateSalesOff.setParameter(1, salesOfficer.getContactNo());
				updateSalesOff.setDate(2, new Date());
				updateSalesOff.setParameter(3, salesOfficer.getID());
				updateSalesOff.setParameter(4, manufacturer.getManufacturerID());
				updateSalesOff.setParameter(5, manufacturer.getTenantID());
				int result = updateSalesOff.executeUpdate();
				if(result == 0){
					logger.debug("Nothing has been modified :)");
				}
			}
			//Save Area Manager
			if(manufacturer.getAreaManager() != null){
				ManufacturerAreaManager areaMgr = manufacturer.getAreaManager();
				SQLQuery updateAreaMgr = session.createSQLQuery("UPDATE MANUFACTURER_AREA_MANAGER SET NAME= ?, CONTACT_NO = ?, DATE_MODIFIED = ? WHERE ID = ? AND MANUFACTURER_ID = ? AND TENANT_ID = ?");
				updateAreaMgr.setParameter(0, areaMgr.getName());
				updateAreaMgr.setParameter(1, areaMgr.getContactNo());
				updateAreaMgr.setDate(2, new Date());
				updateAreaMgr.setParameter(3, areaMgr.getID());
				updateAreaMgr.setParameter(4, manufacturer.getManufacturerID());
				updateAreaMgr.setParameter(5, manufacturer.getTenantID());
				int result = updateAreaMgr.executeUpdate();
				if(result == 0){
					logger.debug("Nothing has been modified :)");
				}
			}
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while updating manufacturer", e);
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
	public void delete(int manufacturerID, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Remove Manufacturer Address
			Query delAddressQry = session.createSQLQuery(
					"DELETE " + 
					"  d.* " + 
					"FROM address d " + 
					"WHERE  d.id IN (SELECT ID FROM (SELECT a.id " + 
					"                FROM   ADDRESS a, " + 
					"                       MANUFACTURERS b, " + 
					"                       MANUFACTURER_ADDRESS c " + 
					"                WHERE  a.id = c.address_id " + 
					"                       AND b.id = c.MANUFACTURER_ID " + 
					"                       AND b.id = ? " + 
					"                       AND a.tenant_id = ? " + 
					"                       AND a.tenant_id = b.tenant_id " + 
					"                       AND b.tenant_id = c.tenant_id) X)");
			delAddressQry.setInteger(0, manufacturerID);
			delAddressQry.setInteger(1, tenantID);
			delAddressQry.executeUpdate();
			//Remove Manufacturer
			Query query = session.createQuery("delete from Manufacturer where manufacturerID = :manufacturerID AND tenantID = :tenantID");
			query.setParameter("manufacturerID", manufacturerID);
			query.setParameter("tenantID", tenantID);
			query.executeUpdate();
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deleting manufacturer", exception);
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
	public List<Manufacturer> getTenantManufacturers(int tenantID) {
		Session session = null;
		List<Manufacturer> manufacturers = null;
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Manufacturer where tenantID = :tenantID order by DATE_CREATED ASC");
			query.setParameter("tenantID", tenantID);
			manufacturers = query.list();
			for (Manufacturer manufacturer : manufacturers) {
				int manufacturerID = manufacturer.getManufacturerID();
				// Add address to customer
				manufacturer.setAddress(getManufacturerAddresses(session, manufacturerID, tenantID));

				// get Sales Officer
				List<ManufacturerSalesOfficer> sos = getManufacturerSalesOfficers(session, manufacturerID, tenantID);
				if (sos != null && sos.size() == 1) {
					manufacturer.setSalesOfficer(sos.get(0));

				}
				
				// get Area Manager
				List<ManufacturerAreaManager> ams = getManufacturerAreaManagers(session, manufacturerID, tenantID);
				if (ams != null && ams.size() == 1) {
					manufacturer.setAreaManager(ams.get(0));
				}
				
				// Get beats
				List<Beat> beats = new ArrayList<Beat>(getManufacturerBeats(session, manufacturerID, tenantID).values());
				if (beats.size() > 0) {
					manufacturer.setBeats(beats);
					manufacturer.setBeatIDs(new ArrayList<Integer>(getManufacturerBeats(session, manufacturerID, tenantID).keySet()));
				}
				
				
				// Get Sales Executives
				List<SalesExecutive> salesExecs = getManufacturerSalexExecs(session, manufacturerID, tenantID);
				if(salesExecs.size() > 0) {
					List<Integer> salesExecIDs = new ArrayList();
					for(SalesExecutive salesExec : salesExecs) {
						salesExecIDs.add(salesExec.getUserID());
					}
					manufacturer.setSalesExecs(salesExecs);
					manufacturer.setSalesExecsIDs(salesExecIDs);
					
				}
			}
		} catch (Exception exception) {
			logger.error("Error while fetching tenant manufacturers", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return manufacturers;
	}
	
	@Override
	public List<Manufacturer> getTenantTrimmedManufacturers(int tenantID) {
		Session session = null;
		List<Manufacturer> manufacturers = new ArrayList<Manufacturer>();
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Manufacturer where tenantID = :tenantID order by DATE_CREATED ASC");
			query.setParameter("tenantID", tenantID);
			manufacturers = query.list();
		} catch (Exception exception) {
			logger.error("Error while fetching tenant trimmed manufacturers", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return manufacturers;
	}

	@Override
	public int getManufacturersCount(int tenantID){
		Session session = null;
		int counts = 0;
		try{
			session = sessionFactory.openSession();
			SQLQuery count = session.createSQLQuery("SELECT COUNT(*) FROM MANUFACTURERS WHERE TENANT_ID= ?");
			count.setParameter(0, tenantID);
			List results = count.list();
			if(results != null && results.size() == 1 ){
				counts = ((BigInteger)results.get(0)).intValue();
			}
		}catch(Exception exception){
			logger.error("Error while fetching number of manufacturers.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return counts;
	}
	
	/**
	@Override
	public List<Manufacturer> getSuppManufacturerList(int tenantID) {
		Session session = null;
		Map<Integer, Manufacturer> suppMap = new HashMap<Integer, Manufacturer>();
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.ID SUPP_ID, a.NAME SUPP_NAME, b.ID MANF_ID, b.FULL_NAME MANF_NAME FROM MANUFACTURERS a, MANUFACTURERS b, SUPPLIER_MANUFACTURER c "
					+ "WHERE a.ID = c.SUPPLIER_ID AND b.ID = c.MANUFACTURER_ID AND a.TENANT_ID = ? AND b.TENANT_ID = ?");
			query.setParameter(0, tenantID);
			query.setParameter(1, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				//Manufacturer
				Manufacturer manufacturer = null;
				if(suppMap.get(Integer.valueOf(String.valueOf(objs[0]))) != null){
					manufacturer = suppMap.get(Integer.valueOf(String.valueOf(objs[0])));
				}else{
					manufacturer = new Manufacturer();
					manufacturer.setManufacturerID(Integer.valueOf(String.valueOf(objs[0])));
					manufacturer.setName(String.valueOf(objs[1]));
					suppMap.put(manufacturer.getManufacturerID(), manufacturer);
				}
				//Manufaucturer
				Manufacturer manufacturer = new Manufacturer();
				manufacturer.setManufacturerID(Integer.valueOf(String.valueOf(objs[2])));
				manufacturer.setFullName(String.valueOf(objs[3]));
				//Set in manufacturer
				if(manufacturer.getManufacturers() == null){
					manufacturer.setManufacturers(new ArrayList<Manufacturer>());
				}
				manufacturer.getManufacturers().add(manufacturer);
			}
		}catch(Exception exception){
			logger.error("Error while fetching manufacturer-manufacturer mapping.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return new ArrayList<Manufacturer>(suppMap.values());
	}
	
	
	@Override
	public List<Manufacturer> getManufacturerSalesExecsList(int tenantID) {
		Session session = null;
		Map<Integer, Manufacturer> manufMap = new HashMap<Integer, Manufacturer>();
		MultiKeyMap<Object, String> tranMap = new MultiKeyMap<Object, String>();
		try{
			session = sessionFactory.openSession();
			SQLQuery tranQuery = session.createSQLQuery("select " + 
					"b.MANUFACTURER_ID, " + 
					"a.SALES_EXEC_ID " + 
					"from " + 
					"ORDER_BOOKING_SCHEDULE a, " + 
					"ORDER_BOOKING_SCHEDULE_MANUFACTURERS b " + 
					"where a.ID = b.ORDER_BOOKING_SCHEDULE_ID");
			List results1 = tranQuery.list();
			for(Object obj : results1){
				Object[] objs = (Object[])obj;
				tranMap.put(Integer.valueOf(String.valueOf(objs[0])), Integer.valueOf(String.valueOf(objs[1])), "");
			}
			
			
			SQLQuery query = session.createSQLQuery("SELECT a.ID, a.CODE, a.NAME, b.ID SALES_EXEC_ID, b.FIRST_NAME, b.LAST_NAME FROM  a, USERS b, "
					+ "MANUFACTURER_SALES_EXECUTIVES c WHERE a.ID=c.MANUFACTURER_ID AND b.ID=c.SALES_EXEC_ID AND c.TENANT_ID= ?");
			query.setParameter(0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				//Manufacturer
				Manufacturer manufacturer = null;
				if(manufMap.get(Integer.valueOf(String.valueOf(objs[0]))) != null){
					manufacturer = manufMap.get(Integer.valueOf(String.valueOf(objs[0])));
				}else{
					manufacturer = new Manufacturer();
					manufacturer.setManufacturerID(Integer.valueOf(String.valueOf(objs[0])));
					manufacturer.setCode(String.valueOf(objs[1]));
					manufacturer.setName(String.valueOf(objs[2]));
					manufMap.put(manufacturer.getManufacturerID(), manufacturer);
				}
				manufacturer.setTenantID(tenantID);
				//Sales Executive
				SalesExecutive salesExecutive = new SalesExecutive();
				salesExecutive.setUserID(Integer.valueOf(String.valueOf(objs[3])));
				salesExecutive.setFirstName(String.valueOf(objs[4]));
				salesExecutive.setLastName(String.valueOf(objs[5]));
				//Set in manufacturer
				if(manufacturer.getSalesExecs() == null){
					manufacturer.setSalesExecs(new ArrayList<SalesExecutive>());
					manufacturer.setSalesExecsIDs(new ArrayList<Integer>());
				}
				manufacturer.getSalesExecs().add(salesExecutive);
				manufacturer.getSalesExecsIDs().add(salesExecutive.getUserID());
				
				//CHeck if the manufacturer and Sales Executive has any transaction
				if(tranMap.containsKey(manufacturer.getManufacturerID(), salesExecutive.getUserID())) {
					if(!manufacturer.getIsManufSalesExecHasOrdersBooked()) {
						manufacturer.setIsManufSalesExecHasOrdersBooked(true);
					}
				}
			}
		}catch(Exception exception){
			logger.error("Error while fetching manufacturer-salesexecutive mapping.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return new ArrayList<Manufacturer>(manufMap.values());
	}
	
	
	@Override
	public void assignManufacturer(int manufacturerID, List<Integer> manufacturerIDs, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			// get Connction from Session
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO SUPPLIER_MANUFACTURER (SUPPLIER_ID, MANUFACTURER_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int i = 0; i < manufacturerIDs.size(); i++) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, manufacturerIDs.get(i));
							pstmt.setInt(3, tenantID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning manufacturer to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public void updateAssignedManufacturer(int manufacturerID, List<Integer> manufacturerIDs, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Remove existing mapping
			SQLQuery removeMapping = session.createSQLQuery("DELETE FROM SUPPLIER_MANUFACTURER WHERE SUPPLIER_ID = ?");
			removeMapping.setParameter(0, manufacturerID);
			removeMapping.executeUpdate();
			// get Connction from Session
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO SUPPLIER_MANUFACTURER (SUPPLIER_ID, MANUFACTURER_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int i = 0; i < manufacturerIDs.size(); i++) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, manufacturerIDs.get(i));
							pstmt.setInt(3, tenantID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning manufacturer to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
		
	}

	@Override
	public void deleteAassignedManufacturer(int manufacturerID, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Remove existing mapping
			SQLQuery removeMapping = session.createSQLQuery("DELETE FROM SUPPLIER_MANUFACTURER WHERE SUPPLIER_ID = ? AND TENANT_ID = ?");
			removeMapping.setParameter(0, manufacturerID);
			removeMapping.setParameter(1, tenantID);
			removeMapping.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while removing assigned manufacturer to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
		
	}
**/
	@Override
	public void assignSalesExecutivesToManufacturer(int tenantID, Manufacturer manufacturer) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			final List<Integer> salesExecIDs = manufacturer.getSalesExecsIDs();
			final int manufacturerID = manufacturer.getManufacturerID();
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO MANUFACTURER_SALES_EXECUTIVES (MANUFACTURER_ID, SALES_EXEC_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, 0, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int salesExecID : salesExecIDs) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, salesExecID);
							pstmt.setInt(3, tenantID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning sales executives to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public void updateAssignedSalesExecs(int manufacturerID, List<Integer> salesExecIDs, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		Map<Integer, Integer> salesExTranxCntMap = new HashMap<Integer, Integer>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Gets manufacturer sales executive transaction count
			SQLQuery tranxCounterQry = session.createSQLQuery("SELECT SALES_EXEC_ID, TRANX_COUNTER FROM MANUFACTURER_SALES_EXECUTIVES where MANUFACTURER_ID = ? AND TRANX_COUNTER > 0");
			tranxCounterQry.setInteger(0, manufacturerID);
			List results = tranxCounterQry.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				salesExTranxCntMap.put(Integer.valueOf(String.valueOf(objs[0])), Integer.valueOf(String.valueOf(objs[1])));
			}
			if (!salesExecIDs.containsAll(salesExTranxCntMap.keySet())) {
				throw new CRMException("000",
						"Assigned Sales Executives to manufacturer could not be updated successfully because some of the Sales Executives for which transactions exists in system are not allowed to be dissociate from the Manufacturer. Please verify the list of Sales Executives before updating the association witn manufacturer.");
			}
			//Remove existing mapping
			SQLQuery removeMapping = session.createSQLQuery("DELETE FROM MANUFACTURER_SALES_EXECUTIVES WHERE MANUFACTURER_ID = ?");
			removeMapping.setParameter(0, manufacturerID);
			removeMapping.executeUpdate();
			//If User deselects all the existing mapped sales executives
			if(salesExecIDs.size() != 1 || salesExecIDs.get(0) != -1) {
				// get Connction from Session
				session.doWork(new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						PreparedStatement pstmt = null;
						try {
							String sqlInsert = "INSERT INTO MANUFACTURER_SALES_EXECUTIVES (MANUFACTURER_ID, SALES_EXEC_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())";
							pstmt = connection.prepareStatement(sqlInsert);
							for (int salesExecID : salesExecIDs) {
								pstmt.setInt(1, manufacturerID);
								pstmt.setInt(2, salesExecID);
								pstmt.setInt(3, salesExTranxCntMap.get(salesExecID) == null ? 0 : salesExTranxCntMap.get(salesExecID));
								pstmt.setInt(4, tenantID);
								pstmt.addBatch();
							}
							pstmt.executeBatch();
						} finally {
							pstmt.close();
						}
					}
				});
			}
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning sales executive to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
		
	}

	
	@Override
	public void deleteAassignedSalesExec(int manufacturerID, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Remove existing mapping
			SQLQuery removeMapping = session.createSQLQuery("DELETE FROM MANUFACTURER_SALES_EXECUTIVES WHERE MANUFACTURER_ID = ? AND TENANT_ID = ?");
			removeMapping.setParameter(0, manufacturerID);
			removeMapping.setParameter(1, tenantID);
			removeMapping.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while removing assigned manufacturer to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
		
	}

	@Override
	public List<ManufacturerSalesExecBeats> getManufSalesExecBeatsList(int tenantID) {
		Session session = null;
		Map<String, ManufacturerSalesExecBeats> manufMap = new HashMap<String, ManufacturerSalesExecBeats>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.id, " + 
					"       a.NAME, " + 
					"       a.code MANUFACTURER_CODE, " + 
					"       c.id            SALES_EXEC_ID, " + 
					"       c.first_name    FIRST_NAME, " + 
					"       c.last_name     LAST_NAME, " + 
					"       c.code SALES_EXEC_code, " + 
					"       b.id            BEAT_ID, " + 
					"       b.NAME          BEAT_NAME, " + 
					"       b.code          BEAT_CODE, " + 
					"       d.tranx_counter TRANX_COUNTER " + 
					"FROM   manufacturers a, " + 
					"       beats b, " + 
					"       USERS c, " + 
					"       manufacturer_sales_exec_beats d " + 
					"WHERE  a.id = d.manufacturer_id " + 
					"       AND b.id = d.beat_id " + 
					"       AND c.id = d.sales_exec_id " + 
					"       AND d.tenant_id = ?  " + 
					"       ");
			query.setParameter(0, tenantID);
			List lists = query.list();
			for (Object obj : lists) {
				Object[] objs = (Object[]) obj;
				
				Manufacturer manufacturer = new Manufacturer();
				manufacturer.setManufacturerID(Integer.valueOf(String.valueOf(objs[0])));
				manufacturer.setName(String.valueOf(objs[1]));
				manufacturer.setCode(String.valueOf(objs[2]));
				
				SalesExecutive salesExecutive = new SalesExecutive();
				salesExecutive.setUserID(Integer.valueOf(String.valueOf(objs[3])));
				salesExecutive.setFirstName(String.valueOf(objs[4]));
				salesExecutive.setLastName(String.valueOf(objs[5]));
				salesExecutive.setCode(String.valueOf(objs[6]));
				
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[7])));
				beat.setName(String.valueOf(objs[8]));
				beat.setCode(String.valueOf(objs[9]));
				
				if(!manufMap.containsKey(manufacturer.getManufacturerID()+"-"+salesExecutive.getUserID())){
					ManufacturerSalesExecBeats manufSalesExecBeats = new ManufacturerSalesExecBeats();
					manufSalesExecBeats.setManufacturer(manufacturer);
					manufSalesExecBeats.setSalesExecutive(salesExecutive);
					List<Beat> beats = new ArrayList<>();
					beats.add(beat);
					manufSalesExecBeats.setBeats(beats);
					if(!manufSalesExecBeats.getHasTransactions()) {
						manufSalesExecBeats.setHasTransactions(Integer.valueOf(String.valueOf(objs[10])) > 0 ? true : false);
					}
					manufMap.put(manufacturer.getManufacturerID()+"-"+salesExecutive.getUserID(), manufSalesExecBeats);
				}else{
					ManufacturerSalesExecBeats manufSalesExecBeats = manufMap.get(manufacturer.getManufacturerID()+"-"+salesExecutive.getUserID());
					if(!manufSalesExecBeats.getHasTransactions()) {
						manufSalesExecBeats.setHasTransactions(Integer.valueOf(String.valueOf(objs[10])) > 0 ? true : false);
					}
					manufSalesExecBeats.getBeats().add(beat);
					manufMap.put(manufacturer.getManufacturerID()+"-"+salesExecutive.getUserID(), manufSalesExecBeats);
				}
			}
		} catch (Exception exception) {
			logger.error("Error while removing assigned manufacturer to Manufacturer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return new ArrayList<ManufacturerSalesExecBeats>(manufMap.values());
	}
	
	
	@Override
	public ManufacturerSalesExecBeats getManufSalesExecBeat(String manufacturerCode, String salesExecCode, int tenantID) {
		Session session = null;
		ManufacturerSalesExecBeats manufSalesExecBeat = null;
		List<Integer> beatIDsList = new ArrayList<Integer>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.id, " + 
					"       a.NAME, " + 
					"       c.id         SALES_EXEC_ID, " + 
					"       c.first_name FIRST_NAME, " + 
					"       c.last_name  LAST_NAME, " + 
					"       b.id         BEAT_ID, " + 
					"       b.NAME       BEAT_NAME " + 
					"FROM   manufacturers a, " + 
					"       beats b, " + 
					"       USERS c, " + 
					"       manufacturer_sales_exec_beats d " + 
					"WHERE  a.id = d.manufacturer_id " + 
					"       AND b.id = d.beat_id " + 
					"       AND c.id = d.sales_exec_id " + 
					"       AND a.tenant_id = b.tenant_id " + 
					"       AND b.tenant_id = c.tenant_id " + 
					"       AND c.tenant_id = d.tenant_id " + 
					"       AND a.code = ? " + 
					"       AND c.code = ?  " + 
					"       AND d.tenant_id = ?");
			query.setString(0, manufacturerCode);
			query.setString(1, salesExecCode);
			query.setInteger(2, tenantID);
			List lists = query.list();
			boolean added = false;
			for (Object obj : lists) {
				Object[] objs = (Object[]) obj;
				if(!added){
					manufSalesExecBeat = new ManufacturerSalesExecBeats();
					Manufacturer manufacturer = new Manufacturer();
					manufacturer.setManufacturerID(Integer.valueOf(String.valueOf(objs[0])));
					manufacturer.setName(String.valueOf(objs[1]));
					
					SalesExecutive salesExecutive = new SalesExecutive();
					salesExecutive.setUserID(Integer.valueOf(String.valueOf(objs[2])));
					salesExecutive.setFirstName(String.valueOf(objs[3]));
					salesExecutive.setLastName(String.valueOf(objs[4]));
					manufSalesExecBeat.setManufacturer(manufacturer);
					manufSalesExecBeat.setSalesExecutive(salesExecutive);
					Beat beat = new Beat();
					beat.setBeatID(Integer.valueOf(String.valueOf(objs[5])));
					beat.setName(String.valueOf(objs[6]));
					List<Beat> beats = new ArrayList<>();
					beats.add(beat);
					beatIDsList.add(beat.getBeatID());
					manufSalesExecBeat.setBeatIDLists(beatIDsList);
					manufSalesExecBeat.setBeats(beats);
					added = true;
				}else{
					Beat beat = new Beat();
					beat.setBeatID(Integer.valueOf(String.valueOf(objs[5])));
					beat.setName(String.valueOf(objs[6]));
					manufSalesExecBeat.getBeatIDLists().add(beat.getBeatID());
					manufSalesExecBeat.getBeats().add(beat);
				}
			}
		} catch (Exception exception) {
			logger.error("Error while removing assigned manufacturer to Manufacturer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return manufSalesExecBeat;
	}
	
	@Override
	public void assignDelivExecutivesToManufacturer(int tenantID, Manufacturer manufacturer) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			final List<Integer> delivExecIDs = manufacturer.getDelivExecsIDs();
			final int manufacturerID = manufacturer.getManufacturerID();
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO MANUFACTURER_DELIV_EXECUTIVES (MANUFACTURER_ID, DELIV_EXEC_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, 0, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int delivExecID : delivExecIDs) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, delivExecID);
							pstmt.setInt(3, tenantID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning delivery executives to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public void updateAssignedDelivExecs(int manufacturerID, List<Integer> delivExecIDs, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		Map<Integer, Integer> delivExTranxCntMap = new HashMap<Integer, Integer>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Gets manufacturer delivery executive transaction count
			SQLQuery tranxCounterQry = session.createSQLQuery("SELECT DELIV_EXEC_ID, TRANX_COUNTER FROM MANUFACTURER_DELIV_EXECUTIVES where MANUFACTURER_ID = ? AND TRANX_COUNTER > 0");
			tranxCounterQry.setInteger(0, manufacturerID);
			List results = tranxCounterQry.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				delivExTranxCntMap.put(Integer.valueOf(String.valueOf(objs[0])), Integer.valueOf(String.valueOf(objs[1])));
			}
			if (!delivExecIDs.containsAll(delivExTranxCntMap.keySet())) {
				throw new CRMException("000",
						"Assigned Delivery Executives to manufacturer could not be updated successfully because some of the Delivery Executives for which transactions exists in system are not allowed to be dissociate from the Manufacturer. Please verify the list of Delivery Executives before updating the association witn manufacturer.");
			}
			
			//Remove existing mapping
			SQLQuery removeMapping = session.createSQLQuery("DELETE FROM MANUFACTURER_DELIV_EXECUTIVES WHERE MANUFACTURER_ID = ?");
			removeMapping.setParameter(0, manufacturerID);
			removeMapping.executeUpdate();
			
			//If User deselects all the existing mapped sales executives
			if(delivExecIDs.size() != 1 || delivExecIDs.get(0) != -1) {
				// get Connction from Session
				session.doWork(new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						PreparedStatement pstmt = null;
						try {
							String sqlInsert = "INSERT INTO MANUFACTURER_DELIV_EXECUTIVES (MANUFACTURER_ID, DELIV_EXEC_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())";
							pstmt = connection.prepareStatement(sqlInsert);
							for (int delivExecID : delivExecIDs) {
								pstmt.setInt(1, manufacturerID);
								pstmt.setInt(2, delivExecID);
								pstmt.setInt(3, delivExTranxCntMap.get(delivExecID) == null ? 0 : delivExTranxCntMap.get(delivExecID));
								pstmt.setInt(4, tenantID);
								pstmt.addBatch();
							}
							pstmt.executeBatch();
						} finally {
							pstmt.close();
						}
					}
				});
			}
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning delivery executive to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
		
	}
	
	
	@Override
	public void deleteAassignedDelivExec(int manufacturerID, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Remove existing mapping
			SQLQuery removeMapping = session.createSQLQuery("DELETE FROM MANUFACTURER_DELIV_EXECUTIVES WHERE MANUFACTURER_ID = ? AND TENANT_ID = ?");
			removeMapping.setParameter(0, manufacturerID);
			removeMapping.setParameter(1, tenantID);
			removeMapping.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while removing assigned delivery executive to Manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
		
	}
	
	@Override
	public List<ManufacturerDelivExecs> getManufacturerDelivExecsList(int tenantID) {
		Session session = null;
		Transaction transaction = null;
		Map<Integer, ManufacturerDelivExecs> manufacturerDelivExecsMap = new HashMap<Integer, ManufacturerDelivExecs>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT a.id   manuf_id, " + 
					"       a.NAME manuf_name, " + 
					"       a.code manuf_code, " + 
					"       b.id   sales_exec_id, " + 
					"       b.first_name first_name, " +
					"       b.last_name last_name, " + 
					"       b.code sales_exec_code, " + 
					"       c.tranx_counter " + 
					"FROM   manufacturers a, " + 
					"       users b, " + 
					"       manufacturer_deliv_executives c " + 
					"WHERE  a.id = c.manufacturer_id " + 
					"       AND b.id = c.deliv_exec_id " + 
					"       AND a.tenant_id = b.tenant_id " + 
					"       AND b.tenant_id = c.tenant_id " + 
					"       AND a.tenant_id = ?");
			query.setParameter(0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				int manufID = Integer.valueOf(String.valueOf(objs[0]));
				if(manufacturerDelivExecsMap.get(manufID) == null) {
					ManufacturerDelivExecs manufacturerDelivExec = new ManufacturerDelivExecs();
					//Manufacturer
					Manufacturer manufacturer = new Manufacturer();
					manufacturer.setManufacturerID(manufID);
					manufacturer.setName(String.valueOf(objs[1]));
					manufacturer.setCode(String.valueOf(objs[2]));
					manufacturerDelivExec.setManufacturer(manufacturer);
					//Add Manufacturer to map
					manufacturerDelivExecsMap.put(manufID, manufacturerDelivExec);
					
				}
				
				//Beat
				DeliveryExecutive delivExec = new DeliveryExecutive();
				delivExec.setUserID(Integer.valueOf(String.valueOf(objs[3])));
				delivExec.setFirstName(String.valueOf(objs[4]));
				delivExec.setLastName(String.valueOf(objs[5]));
				delivExec.setCode(String.valueOf(objs[6]));
				manufacturerDelivExecsMap.get(manufID).getDelivExecs().add(delivExec);
				//active transaction
				boolean hasActiveTransaction = Integer.valueOf(String.valueOf(objs[7])) > 0 ? true : false;
				if(!manufacturerDelivExecsMap.get(manufID).getHasActiveTransaction()) {
					manufacturerDelivExecsMap.get(manufID).setHasActiveTransaction(hasActiveTransaction);
				}
				//tenant id
				manufacturerDelivExecsMap.get(manufID).setTenantID(tenantID);
			}
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while fetching manufacturer sales executives", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return new ArrayList<ManufacturerDelivExecs>(manufacturerDelivExecsMap.values());
	}

	@Override
	public void assignBeatsToManufacturer(int manufacturerID, List<Integer> beatIDs, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			// get Connction from Session
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO MANUFACTURER_BEAT(MANUFACTURER_ID, BEAT_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, 0, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int beatID : beatIDs) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, beatID);
							pstmt.setInt(3, tenantID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while assigning beats to manufacturer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public void updateAssignedBeatToManufacturer(int manufacturerID, List<Integer> beatIDs, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		Map<Integer, Integer> beatTranxCntMap = new HashMap<Integer, Integer>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			//Gets manufacturer beats transaction count
			SQLQuery tranxCounterQry = session.createSQLQuery("SELECT BEAT_ID, TRANX_COUNTER FROM MANUFACTURER_BEAT where MANUFACTURER_ID = ? AND TRANX_COUNTER > 0");
			tranxCounterQry.setInteger(0, manufacturerID);
			List results = tranxCounterQry.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				beatTranxCntMap.put(Integer.valueOf(String.valueOf(objs[0])), Integer.valueOf(String.valueOf(objs[1])));
			}
			if (!beatIDs.containsAll(beatTranxCntMap.keySet())) {
				throw new CRMException("000",
						"Assigned beats to manufacturer could not be updated successfully because some of the beats for which transactions exists in system are not allowed to be dissociate from the Manufacturer. Please verify the list of beats before updating the association witn manufacturer.");
			}
			//Delete existing Beat-Customer
			SQLQuery deleteSalesExecBeats = session.createSQLQuery("DELETE FROM MANUFACTURER_BEAT WHERE MANUFACTURER_ID =? AND TENANT_ID = ?");
			deleteSalesExecBeats.setParameter(0, manufacturerID);
			deleteSalesExecBeats.setParameter(1, tenantID);
			deleteSalesExecBeats.executeUpdate();
			// get Connction from Session
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO MANUFACTURER_BEAT (MANUFACTURER_ID, BEAT_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int beatID : beatIDs) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, beatID);
							pstmt.setInt(3, beatTranxCntMap.get(beatID) == null ? 0 : beatTranxCntMap.get(beatID));
							pstmt.setInt(4, tenantID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error while updating assigned beats to manufacturer", e);
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	
	}
	
	@Override
	public void deleteAssignedBeatManufacturerLink(int manufacturerID, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM MANUFACTURER_BEAT WHERE MANUFACTURER_ID= ? AND TENANT_ID = ?");
			query.setParameter(0, manufacturerID);
			query.setParameter(1, tenantID);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Manufacture beats could not be successfully removed", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public List<ManufacturerBeats> getManufacturerBeats(int tenantID){
		Session session = null;
		Transaction transaction = null;
		Map<Integer, ManufacturerBeats> manufactureBeatsrMap = new HashMap<Integer, ManufacturerBeats>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT a.id manuf_id, " + 
					"       a.NAME manuf_name," + 
					"       a.code manuf_code," + 
					"       b.id beat_id, " + 
					"       b.NAME beat_name, " + 
					"       b.NAME beat_code, " + 
					"       c.tranx_counter " + 
					"FROM   manufacturers a, " + 
					"       beats b, " + 
					"       manufacturer_beat c " + 
					"WHERE  a.id = c.manufacturer_id " + 
					"       AND b.id = c.beat_id " + 
					"       AND a.tenant_id = b.tenant_id " + 
					"       AND b.tenant_id = c.tenant_id " + 
					"       AND a.tenant_id = ? ");
			query.setParameter(0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				int manufID = Integer.valueOf(String.valueOf(objs[0]));
				if(manufactureBeatsrMap.get(manufID) == null) {
					ManufacturerBeats manufacturerBeats = new ManufacturerBeats();
					//Manufacturer
					Manufacturer manufacturer = new Manufacturer();
					manufacturer.setManufacturerID(manufID);
					manufacturer.setName(String.valueOf(objs[1]));
					manufacturer.setCode(String.valueOf(objs[2]));
					manufacturerBeats.setManufacturer(manufacturer);
					//Add Manufacturer to map
					manufactureBeatsrMap.put(manufID, manufacturerBeats);
					
				}
				
				//Beat
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[3])));
				beat.setName(String.valueOf(objs[4]));
				beat.setCode(String.valueOf(objs[5]));
				manufactureBeatsrMap.get(manufID).getBeats().add(beat);
				//active transaction
				boolean hasActiveTransaction = Integer.valueOf(String.valueOf(objs[6])) > 0 ? true : false;
				if(!manufactureBeatsrMap.get(manufID).getHasActiveTransaction()) {
					manufactureBeatsrMap.get(manufID).setHasActiveTransaction(hasActiveTransaction);
				}
				//tenant id
				manufactureBeatsrMap.get(manufID).setTenantID(tenantID);
			}
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while fetching manufacturer beats", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return new ArrayList<ManufacturerBeats>(manufactureBeatsrMap.values());
	}
	
	
	@Override
	public List<ManufacturerSalesExecs> getManufacturerSalesExecs(int tenantID){
		Session session = null;
		Transaction transaction = null;
		Map<Integer, ManufacturerSalesExecs> manufactureSalesExecsMap = new HashMap<Integer, ManufacturerSalesExecs>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT a.id   manuf_id, " + 
					"       a.NAME manuf_name, " + 
					"       a.code manuf_code, " + 
					"       b.id   sales_exec_id, " + 
					"       b.first_name first_name, " +
					"       b.last_name last_name, " + 
					"       b.code sales_exec_code, " + 
					"       c.tranx_counter " + 
					"FROM   manufacturers a, " + 
					"       users b, " + 
					"       manufacturer_sales_executives c " + 
					"WHERE  a.id = c.manufacturer_id " + 
					"       AND b.id = c.sales_exec_id " + 
					"       AND a.tenant_id = b.tenant_id " + 
					"       AND b.tenant_id = c.tenant_id " + 
					"       AND a.tenant_id = ?");
			query.setParameter(0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				int manufID = Integer.valueOf(String.valueOf(objs[0]));
				if(manufactureSalesExecsMap.get(manufID) == null) {
					ManufacturerSalesExecs manufacturersalesExec = new ManufacturerSalesExecs();
					//Manufacturer
					Manufacturer manufacturer = new Manufacturer();
					manufacturer.setManufacturerID(manufID);
					manufacturer.setName(String.valueOf(objs[1]));
					manufacturer.setCode(String.valueOf(objs[2]));
					manufacturersalesExec.setManufacturer(manufacturer);
					//Add Manufacturer to map
					manufactureSalesExecsMap.put(manufID, manufacturersalesExec);
					
				}
				
				//Beat
				SalesExecutive salesExec = new SalesExecutive();
				salesExec.setUserID(Integer.valueOf(String.valueOf(objs[3])));
				salesExec.setFirstName(String.valueOf(objs[4]));
				salesExec.setLastName(String.valueOf(objs[5]));
				salesExec.setCode(String.valueOf(objs[6]));
				manufactureSalesExecsMap.get(manufID).getSalesExecs().add(salesExec);
				//active transaction
				boolean hasActiveTransaction = Integer.valueOf(String.valueOf(objs[7])) > 0 ? true : false;
				if(!manufactureSalesExecsMap.get(manufID).getHasActiveTransaction()) {
					manufactureSalesExecsMap.get(manufID).setHasActiveTransaction(hasActiveTransaction);
				}
				//tenant id
				manufactureSalesExecsMap.get(manufID).setTenantID(tenantID);
			}
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while fetching manufacturer sales executives", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return new ArrayList<ManufacturerSalesExecs>(manufactureSalesExecsMap.values());
	}
}
