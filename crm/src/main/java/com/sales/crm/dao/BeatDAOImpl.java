package com.sales.crm.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import com.sales.crm.model.Area;
import com.sales.crm.model.Beat;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Manufacturer;
import com.sales.crm.model.TrimmedCustomer;

@Repository("beatDAO")
public class BeatDAOImpl implements BeatDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	private static Logger logger = Logger.getLogger(BeatDAOImpl.class);
	
	@Override
	public void create(Beat beat) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			beat.setDateCreated(new Date());
			beat.setCode(UUID.randomUUID().toString());
			beat.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
			session.save(beat);
			// create beat_area
			if (beat.getAreaIDs() != null && beat.getAreaIDs().size() > 0) {
				for (int areaID : beat.getAreaIDs()) {
					SQLQuery createBeatArea = session.createSQLQuery("INSERT INTO BEAT_AREA (BEAT_ID, AREA_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())");
					createBeatArea.setParameter(0, beat.getBeatID());
					createBeatArea.setParameter(1, areaID);
					createBeatArea.setParameter(2, beat.getTenantID());
					createBeatArea.executeUpdate();
				}
			}
			transaction.commit();
		} catch (Exception e) {
			logger.error("Error while creating beat.", e);
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
	public Beat get(String beatCode, int tenantID) {

		Session session = null;
		Beat beat = null;
		try {
			session = sessionFactory.openSession();
			
			Query query = session.createQuery("from Beat where code = :beatCode AND tenantID = :tenantID");
			query.setParameter("beatCode", beatCode);
			query.setParameter("tenantID", tenantID);
			if (query.list() != null) {
				beat = (Beat)query.list().get(0);
			}
			//Get Areas
			SQLQuery areasQuery = session.createSQLQuery("SELECT b.BEAT_ID, a.* FROM AREAS a, BEAT_AREA b WHERE a.ID = b.AREA_ID AND a.TENANT_ID=b.TENANT_ID AND b.BEAT_ID= ? AND a.TENANT_ID = ?");
			areasQuery.setParameter(0, beat.getBeatID());
			areasQuery.setParameter(1, tenantID);
			List areas = areasQuery.list();
			List<Area> areaList = new ArrayList<Area>();
			List<Integer> areaIDs = new ArrayList<Integer>();
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
				areaIDs.add(area.getAreaID());
			}
			
			//GET Beat Customers
			List<Integer> customerIDs = new ArrayList<Integer>();
			SQLQuery beatCustomersQuery = session.createSQLQuery("SELECT a.ID FROM CUSTOMERS a, BEAT_CUSTOMER b WHERE a.ID = b.CUSTOMER_ID AND a.TENANT_ID=b.TENANT_ID AND b.BEAT_ID=? AND a.TENANT_ID = ?");
			beatCustomersQuery.setParameter(0, beat.getBeatID());
			beatCustomersQuery.setParameter(1, tenantID);
			List beatCustIDList = beatCustomersQuery.list();
			for (Object obj : beatCustIDList) {
				customerIDs.add(Integer.valueOf(String.valueOf(obj)));
			}
			
			//Get BEATS Manufacturer
			List<Manufacturer> manufacturers = new ArrayList<Manufacturer>();
			SQLQuery manufacturerQry = session.createSQLQuery("SELECT * FROM MANUFACTURERS WHERE ID IN (SELECT MANUFACTURER_ID FROM MANUFACTURER_BEAT WHERE BEAT_ID = ? AND TENANT_ID = ?)");
			manufacturerQry.setParameter(0, beat.getBeatID());
			manufacturerQry.setParameter(1, tenantID);
			List resutls = manufacturerQry.list();
			for (Object obj : resutls) {
				Object[] objs = (Object[]) obj;
				Manufacturer manufacturer = new Manufacturer();
				manufacturer.setManufacturerID(Integer.valueOf(String.valueOf(objs[0])));
				manufacturer.setCode(String.valueOf(objs[1]));
				manufacturer.setName(String.valueOf(objs[2]));
				manufacturers.add(manufacturer);
			}
			
			//Add Areas
			if(areaList.size() > 0){
				beat.setAreas(areaList);
				beat.setAreaIDs(areaIDs);
			}
			
			//Add Customer IDS
			if(customerIDs.size() > 0){
				beat.setCustomerIDs(customerIDs);
			}
			
			//Add manufacturer
			if(manufacturers.size() > 0){
				beat.setManufacturers(manufacturers);
			}
		} catch (Exception exception) {
			logger.error("Error while fetching beat details.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return beat;

	}

	@Override
	public void update(Beat beat) throws Exception{

		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			beat.setDateModified(new Date());
			session.update(beat);
			//update Beat_Area
			//Delete mapping first and then re-create the mapping if areas are available in the request
			SQLQuery deleteUserRole = session.createSQLQuery("DELETE FROM BEAT_AREA WHERE BEAT_ID= ?");
			deleteUserRole.setParameter(0, beat.getBeatID());
			deleteUserRole.executeUpdate();
			if(beat.getAreaIDs() != null && beat.getAreaIDs().size() > 0){
				for(int areaID : beat.getAreaIDs()){
					if(areaID == -1) {
						continue;
					}
					SQLQuery createBeatArea = session.createSQLQuery("INSERT INTO BEAT_AREA (BEAT_ID, AREA_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())");
					createBeatArea.setParameter(0, beat.getBeatID());
					createBeatArea.setParameter(1, areaID);
					createBeatArea.setParameter(2, beat.getTenantID());
					createBeatArea.executeUpdate();
				}
			}
			transaction.commit();
			
		} catch (Exception e) {
			logger.error("Error while updating beat.", e);
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
	public void delete(int beatID, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			Beat beat = (Beat) session.get(Beat.class, beatID);
			transaction = session.beginTransaction();
			//Delete BEAT_AREA
			SQLQuery beatAreaQuery = session.createSQLQuery("DELETE FROM BEAT_AREA WHERE BEAT_ID=? AND TENANT_ID = ?");
			beatAreaQuery.setParameter(0, beatID);
			beatAreaQuery.setParameter(1, tenantID);
			beatAreaQuery.executeUpdate();
			//Delete Beat
			session.delete(beat);
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while deleting beat.", exception);
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
	public List<Beat> getTenantBeats(int tenantID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Beat where tenantID = :tenantID order by DATE_CREATED ASC");
			query.setParameter("tenantID", tenantID);
			beats = query.list();

			// get beat ids
			SQLQuery beatsIDQuery = session.createSQLQuery("SELECT ID FROM BEATS WHERE TENANT_ID= ?");
			beatsIDQuery.setParameter(0, tenantID);
			List beatIDs = beatsIDQuery.list();

			//Beat-Area Map
			Map<Integer, List<Area>> beatAreaMap = new HashMap<Integer, List<Area>>();
			//Beat-Customers Map
			Map<Integer, List<TrimmedCustomer>> beatCustomersMap = new HashMap<Integer, List<TrimmedCustomer>>();
			//beat-customer id map
			Map<Integer, List<Integer>> beatCustomerIDsMap = new HashMap<Integer, List<Integer>>();
			
			//Beat-customer tranx map
			Map<Integer, Boolean> beatTranxMap = new HashMap<Integer, Boolean>();
			
			if (beatIDs != null && beatIDs.size() > 0) {
				// Get Areas
				SQLQuery areasQuery = session.createSQLQuery(
						"SELECT b.BEAT_ID, a.* FROM AREAS a, BEAT_AREA b WHERE a.ID = b.AREA_ID AND a.TENANT_ID=b.TENANT_ID AND a.TENANT_ID= ? AND b.BEAT_ID IN ("
								+ StringUtils.join(beatIDs, ",") + ")");
				areasQuery.setParameter(0, tenantID);
				List areas = areasQuery.list();
				for (Object obj : areas) {
					Object[] objs = (Object[]) obj;
					int beatID = Integer.valueOf(String.valueOf(objs[0]));
					Area area = new Area();
					area.setAreaID(Integer.valueOf(String.valueOf(objs[1])));
					area.setCode(String.valueOf(objs[2]));
					area.setName(String.valueOf(objs[3]));
					area.setDescription(String.valueOf(objs[4]));
					area.setWordNo(String.valueOf(objs[5]));
					area.setPinCode(String.valueOf(objs[6]));
					area.setTenantID(Integer.valueOf(String.valueOf(objs[7])));

					if (!beatAreaMap.containsKey(beatID)) {
						beatAreaMap.put(beatID, new ArrayList<Area>());
					}
					beatAreaMap.get(beatID).add(area);
				}
				
				//GET Beat Customers
				SQLQuery beatCustomersQuery = session.createSQLQuery("SELECT b.BEAT_ID, a.ID, a.NAME, a.CODE, b.TRANX_COUNTER FROM CUSTOMERS a, BEAT_CUSTOMER b WHERE a.ID = b.CUSTOMER_ID AND a.TENANT_ID=b.TENANT_ID AND a.TENANT_ID = ?");
				beatCustomersQuery.setParameter(0, tenantID);
				List beatCustList = beatCustomersQuery.list();
				for (Object obj : beatCustList) {
					Object[] objs = (Object[]) obj;
					int beatID = Integer.valueOf(String.valueOf(objs[0]));
					TrimmedCustomer customer = new TrimmedCustomer();
					customer.setCustomerID(Integer.valueOf(String.valueOf(objs[1])));
					customer.setCustomerName(String.valueOf(objs[2]));
					customer.setCustomerCode(String.valueOf(objs[3]));
					
					if(!beatCustomersMap.containsKey(beatID)){
						beatCustomersMap.put(beatID, new ArrayList<TrimmedCustomer>());
						beatCustomerIDsMap.put(beatID, new ArrayList<Integer>());
					}
					if(Integer.valueOf(String.valueOf(objs[4])) > 0) {
						if(!beatTranxMap.containsKey(beatID)) {
							beatTranxMap.put(beatID, true);
						}
					}
					beatCustomersMap.get(beatID).add(customer);
					beatCustomerIDsMap.get(beatID).add(customer.getCustomerID());
				}
				
			}
			
			
			// Add area and Customers to Beat
			for (Beat beat : beats) {
				beat.setAreas(beatAreaMap.get(beat.getBeatID()));
				beat.setCustomers(beatCustomersMap.get(beat.getBeatID()));
				beat.setCustomerIDs(beatCustomerIDsMap.get(beat.getBeatID()));
				if(beatTranxMap.containsKey(beat.getBeatID())) {
					beat.setHasBeatCustomersTransaction(true);
				}
			}

		} catch (Exception exception) {
			logger.error("Error while fetching tenant beats.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return beats;
	}

	@Override
	public void assignCustomersToBeat(final int beatID, final List<Integer> customerIDs, int tenantID) throws Exception{
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
						String sqlInsert = "INSERT INTO BEAT_CUSTOMER (BEAT_ID, CUSTOMER_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, 0, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int customerID : customerIDs) {
							pstmt.setInt(1, beatID);
							pstmt.setInt(2, customerID);
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
			logger.error("Error while assigning beats to customer.", exception);
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
	public void updateAssignedCustomersToBeat(int beatID, List<Integer> customerIDs, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		Map<Integer, Integer> beatCustTranxCntMap = new HashMap<Integer, Integer>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Gets Beat Customer transaction count
			SQLQuery tranxCounterQry = session.createSQLQuery("SELECT CUSTOMER_ID, TRANX_COUNTER FROM BEAT_CUSTOMER where BEAT_ID = ? AND TRANX_COUNTER > 0");
			tranxCounterQry.setInteger(0, beatID);
			List results = tranxCounterQry.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				beatCustTranxCntMap.put(Integer.valueOf(String.valueOf(objs[0])), Integer.valueOf(String.valueOf(objs[1])));
			}
			if (!customerIDs.containsAll(beatCustTranxCntMap.keySet())) {
				throw new CRMException("000",
						"Assigned Customers to Beat could not be updated successfully because some of the customers for which transactions exists in system are not allowed to be dissociate from the beat. Please verify the list of customers before updating the association with the beat.");
			}
			//Delete existing Beat-Customer
			SQLQuery deleteSalesExecBeats = session.createSQLQuery("DELETE FROM BEAT_CUSTOMER WHERE BEAT_ID =? AND TENANT_ID = ?");
			deleteSalesExecBeats.setParameter(0, beatID);
			deleteSalesExecBeats.setParameter(1, tenantID);
			deleteSalesExecBeats.executeUpdate();
			// get Connction from Session
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO BEAT_CUSTOMER (BEAT_ID, CUSTOMER_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int customerID : customerIDs) {
							pstmt.setInt(1, beatID);
							pstmt.setInt(2, customerID);
							pstmt.setInt(3, beatCustTranxCntMap.get(customerID) == null ? 0 : beatCustTranxCntMap.get(customerID));
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
			logger.error("Error while updating assigned beats to customer", e);
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
	public List<TrimmedCustomer> getBeatCustomers(int beatID, int tenantID) {
		Session session = null;
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>();
		try {
			session = sessionFactory.openSession();
			SQLQuery beatsQuery = session.createSQLQuery("SELECT a.ID, a.NAME FROM CUSTOMERS a, BEAT_CUSTOMER b WHERE a.ID = b.CUSTOMER_ID AND a.TENANT_ID=b.TENANT_ID AND b.BEAT_ID = ? AND a.TENANT_ID = ?");
			beatsQuery.setParameter(0, beatID);
			beatsQuery.setParameter(1, tenantID);
			List customerList = beatsQuery.list();
			for(Object obj : customerList){
				Object[] objs = (Object[])obj;
				TrimmedCustomer customer = new TrimmedCustomer();
				customer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				customer.setCustomerName(String.valueOf(objs[2]));
				customers.add(customer)	;		
			}
			return customers;
		} catch (Exception exception) {
			logger.error("Error while getting customers for beat.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public void deleteAssignedBeatCustomerLink(String beatCode, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM BEAT_CUSTOMER WHERE BEAT_ID= (SELECT ID FROM BEATS WHERE CODE = ?) AND TENANT_ID = ?");
			query.setParameter(0, beatCode);
			query.setParameter(1, tenantID);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Customer beats could not be successfully removed", exception);
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
	public int getBeatsCount(int tenantID){
		Session session = null;
		int counts = 0;
		try{
			session = sessionFactory.openSession();
			SQLQuery count = session.createSQLQuery("SELECT COUNT(*) FROM BEATS WHERE TENANT_ID= ?");
			count.setParameter(0, tenantID);
			List results = count.list();
			if(results != null && results.size() == 1 ){
				counts = ((BigInteger)results.get(0)).intValue();
			}
		}catch(Exception exception){
			logger.error("Error while fetching number of customers.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return counts;
	}

	@Override
	public List<Beat> getBeatsNotMappedToCustomer(int customerID, int tenantID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			SQLQuery beatsQuery = session.createSQLQuery("SELECT * FROM BEATS WHERE ID NOT IN (SELECT BEAT_ID FROM BEAT_CUSTOMER WHERE CUSTOMER_ID= ? AND TENANT_ID = ?)");
			beatsQuery.setParameter(0, customerID);
			beatsQuery.setParameter(1, tenantID);
			List beatList = beatsQuery.list();
			for(Object obj : beatList){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setCode(String.valueOf(objs[1]));
				beat.setName(String.valueOf(objs[2]));
				beat.setDescription(String.valueOf(objs[3]));
				beat.setCoverageSchedule(String.valueOf(objs[4]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
				beat.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
				beats.add(beat);
			}
			return beats;
		} catch (Exception exception) {
			logger.error("Error while getting beats for customer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public List<Beat> getBeatsNotMappedToSalesExec(int tenantID, int manufacturerID, int salesExecID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			SQLQuery beatsQuery = session.createSQLQuery("SELECT * FROM BEATS WHERE ID NOT IN (SELECT BEAT_ID FROM MANUFACTURER_SALES_EXEC_BEATS WHERE MANUFACTURER_ID= ? AND SALES_EXEC_ID = ?) AND TENANT_ID= ?");
			beatsQuery.setParameter(0, manufacturerID);
			beatsQuery.setParameter(1, salesExecID);
			beatsQuery.setParameter(2, tenantID);
			List beatList = beatsQuery.list();
			for(Object obj : beatList){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setCode(String.valueOf(objs[1]));
				beat.setName(String.valueOf(objs[2]));
				beat.setDescription(String.valueOf(objs[3]));
				beat.setCoverageSchedule(String.valueOf(objs[4]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
				beat.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
				beats.add(beat);
			}
			return beats;
		} catch (Exception exception) {
			logger.error("Error while getting beats not mapped to sales executive.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}
	
	@Override
	public List<String> getTenantBeatNames(int tenantID){

		Session session = null;
		List<Beat> beats = null;
		List<String> beatNames = new ArrayList<String>();
		try {
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Beat where tenantID = :tenantID order by DATE_CREATED ASC");
			query.setParameter("tenantID", tenantID);
			beats = query.list();
			for(Beat beat : beats) {
				beatNames.add(beat.getName());
			}
		} catch (Exception exception) {
			logger.error("Error while fetching tenant beats.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return beatNames;
	
	}
	
	@Override
	public List<Beat> getBeatsNotMappedToManufacturer(int manufacturerID, int tenantID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			SQLQuery beatsQuery = session.createSQLQuery("SELECT * FROM BEATS WHERE ID NOT IN (SELECT BEAT_ID FROM MANUFACTURER_BEAT WHERE MANUFACTURER_ID= ? AND TENANT_ID = ?)");
			beatsQuery.setParameter(0, manufacturerID);
			beatsQuery.setParameter(1, tenantID);
			List beatList = beatsQuery.list();
			for(Object obj : beatList){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setCode(String.valueOf(objs[1]));
				beat.setName(String.valueOf(objs[2]));
				beat.setDescription(String.valueOf(objs[3]));
				beat.setCoverageSchedule(String.valueOf(objs[4]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
				beat.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
				beats.add(beat);
			}
			return beats;
		} catch (Exception exception) {
			logger.error("Error while getting beats not assigned to the manufacturer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public List<Beat> getBeatsNotMappedToDelivExec(int delivExecID, int tenantID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			SQLQuery beatsQuery = session.createSQLQuery("SELECT * " + 
					"FROM   beats " + 
					"WHERE  id NOT IN (SELECT beat_id " + 
					"                  FROM   delivery_exec_beats " + 
					"                  WHERE  deliv_exec_id = ? " + 
					"                         AND tenant_id = ?) " + 
					"       AND tenant_id = ? ");
			beatsQuery.setParameter(0, delivExecID);
			beatsQuery.setParameter(1, tenantID);
			beatsQuery.setParameter(2, tenantID);
			List beatList = beatsQuery.list();
			for(Object obj : beatList){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setCode(String.valueOf(objs[1]));
				beat.setName(String.valueOf(objs[2]));
				beat.setDescription(String.valueOf(objs[3]));
				beat.setCoverageSchedule(String.valueOf(objs[4]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
				beat.setTenantID(Integer.valueOf(String.valueOf(objs[6])));
				beats.add(beat);
			}
		} catch (Exception exception) {
			logger.error("Error while getting beats not mapped to sales executive.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return beats;
	}
}
