package com.sales.crm.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.exception.CRMException;
import com.sales.crm.model.Beat;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.SalesExecutive;
import com.sales.crm.model.TrimmedCustomer;

@Repository("salesExecDAO")
public class SalesExecDAOImpl implements SalesExecDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(SalesExecDAOImpl.class);
	
	private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public SalesExecutive get(int salesExecID, int tenantID) {
		Session session = null;
		SalesExecutive salesExec = null;
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT ID, USER_NAME, DESCRIPTION, EMAIL_ID, MOBILE_NO, FIRST_NAME, LAST_NAME, STATUS_ID, DATE_CREATED, DATE_MODIFIED FROM USERS WHERE ID=? AND TENANT_ID = ?");
			query.setParameter(0, salesExecID);
			query.setParameter(1, tenantID);
			List results = query.list();
			for(Object obj : results){
				salesExec = new SalesExecutive();
				Object[] objs = (Object[])obj;
				salesExec.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				salesExec.setUserName(String.valueOf(objs[1]));
				salesExec.setDescription(String.valueOf(objs[2]));
				salesExec.setEmailID(String.valueOf(objs[3]));
				salesExec.setMobileNo(String.valueOf(objs[4]));
				salesExec.setFirstName(String.valueOf(objs[5]));
				salesExec.setLastName(String.valueOf(objs[6]));
				salesExec.setStatusID(Integer.valueOf(String.valueOf(objs[7])));
				if(objs[8] != null){
					salesExec.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
				}
				
				if(objs[9] != null){
					salesExec.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
				}
				salesExec.setTenantID(tenantID);
			}
			
			//Get Beats
			SQLQuery getBeats = session.createSQLQuery("SELECT a.* FROM BEATS a, MANUFACTURER_SALES_EXEC_BEATS b where a.ID=b.BEAT_ID AND b.SALES_EXEC_ID=? ");
			getBeats.setParameter(0, salesExecID);
			List beats = getBeats.list();
			List<Beat> beatList = new ArrayList<Beat>();
			List<Integer> beatIDsList = new ArrayList<Integer>();
			for(Object obj : beats){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setCode(String.valueOf(objs[1]));
				beat.setName(String.valueOf(objs[2]));
				beat.setDescription(String.valueOf(objs[3]));
				beat.setCoverageSchedule(String.valueOf(objs[4]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[5])));
				beat.setStatusID(Integer.valueOf(String.valueOf(objs[6])));
				beat.setTenantID(Integer.valueOf(String.valueOf(objs[7])));
				if(objs[8] != null){
					beat.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
				}
				if(objs[9] != null){
					beat.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
				}
				beatList.add(beat);
				beatIDsList.add(beat.getBeatID());
			}
			salesExec.setBeats(beatList);
			salesExec.setBeatIDLists(beatIDsList);
			
			//Get Customer IDs
			SQLQuery custQuery = session.createSQLQuery("SELECT CUSTOMER_ID FROM CUSTOMER_SALES_EXEC WHERE SALES_EXEC_ID= ? ");
			custQuery.setParameter(0, salesExecID);
			List<Integer> custIDs = custQuery.list();
			if(custIDs != null && custIDs.size() > 0){
				salesExec.setCustomerIDs(custIDs);
			}
		}catch(Exception exception){
			logger.error("Error while fetching sales executive details.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return salesExec;
	}
	
	@Override
	public List<SalesExecutive> getActiveSalesExecutives(int tenantID){
		Session session = null;
		List<SalesExecutive> salesExecs = new ArrayList<SalesExecutive>(); 
		try{
			Set<Integer> userIDs = new HashSet<Integer>();
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.ID, a.USER_NAME, a.DESCRIPTION, a.EMAIL_ID, a.MOBILE_NO, a.FIRST_NAME, a.LAST_NAME, a.STATUS_ID, a.DATE_CREATED, a.DATE_MODIFIED, a.TENANT_ID FROM USERS a, USER_ROLE b, TENANT_USER c WHERE a.ID=b.USER_ID AND a.ID=c.USER_ID AND b.ROLE_ID= ? AND c.TENANT_ID=? AND a.STATUS_ID = ?");
			query.setParameter(0, 2);
			query.setParameter(1, tenantID);
			query.setParameter(2, EntityStatusEnum.ACTIVE.getEntityStatus());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				SalesExecutive salesExec = new SalesExecutive();
				salesExec.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				salesExec.setUserName(String.valueOf(objs[1]));
				salesExec.setDescription(String.valueOf(objs[2]));
				salesExec.setEmailID(String.valueOf(objs[3]));
				salesExec.setMobileNo(String.valueOf(objs[4]));
				salesExec.setFirstName(String.valueOf(objs[5]));
				salesExec.setLastName(String.valueOf(objs[6]));
				salesExec.setStatusID(Integer.valueOf(String.valueOf(objs[7])));
				if(objs[8] != null){
					salesExec.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
				}
				
				if(objs[9] != null){
					salesExec.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
				}
				salesExec.setTenantID(Integer.valueOf(String.valueOf(objs[10])));
				salesExec.setTenantID(tenantID);
				salesExecs.add(salesExec);
			}
			
			
		}catch(Exception exception){
			logger.error("Error while fetching list of sales executives", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return salesExecs;
	}

	@Override
	public List<SalesExecutive> getSalesExecutives(int tenantID){
		Session session = null;
		Map<Integer, SalesExecutive> salesExecs = new HashMap<Integer, SalesExecutive>(); 
		try{
			Set<Integer> userIDs = new HashSet<Integer>();
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.ID, a.USER_NAME, a.DESCRIPTION, a.EMAIL_ID, a.MOBILE_NO, a.FIRST_NAME, a.LAST_NAME, a.STATUS_ID, a.DATE_CREATED, a.DATE_MODIFIED, a.TENANT_ID FROM USERS a, USER_ROLE b, TENANT_USER c WHERE a.ID=b.USER_ID AND a.ID=c.USER_ID AND b.ROLE_ID= ? AND c.TENANT_ID=?");
			query.setParameter(0, 2);
			query.setParameter(1, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				SalesExecutive salesExec = new SalesExecutive();
				salesExec.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				salesExec.setUserName(String.valueOf(objs[1]));
				salesExec.setDescription(String.valueOf(objs[2]));
				salesExec.setEmailID(String.valueOf(objs[3]));
				salesExec.setMobileNo(String.valueOf(objs[4]));
				salesExec.setFirstName(String.valueOf(objs[5]));
				salesExec.setLastName(String.valueOf(objs[6]));
				salesExec.setStatusID(Integer.valueOf(String.valueOf(objs[7])));
				if(objs[8] != null){
					salesExec.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
				}
				
				if(objs[9] != null){
					salesExec.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
				}
				salesExec.setTenantID(Integer.valueOf(String.valueOf(objs[10])));
				salesExec.setTenantID(tenantID);
				salesExecs.put(salesExec.getUserID(), salesExec);
				//add user id to set
				userIDs.add(salesExec.getUserID());
			}
			
			//Get Beats
			if(userIDs.size() > 0){
				SQLQuery getBeats = session.createSQLQuery("SELECT b.SALES_EXEC_ID, a.* FROM BEATS a, MANUFACTURER_SALES_EXEC_BEATS b where a.ID=b.BEAT_ID AND b.SALES_EXEC_ID in (" +StringUtils.join(userIDs, ",") +")");
				//getRoles.setParameter(0, userIDs);
				List beats = getBeats.list();
				for(Object obj : beats){
					Object[] objs = (Object[])obj;
					int salesExeID = Integer.valueOf(String.valueOf(objs[0]));
					Beat beat = new Beat();
					beat.setBeatID(Integer.valueOf(String.valueOf(objs[1])));
					beat.setCode(String.valueOf(objs[2]));
					beat.setName(String.valueOf(objs[3]));
					beat.setDescription(String.valueOf(objs[4]));
					beat.setCoverageSchedule(String.valueOf(objs[5]));
					beat.setDistance(Integer.valueOf(String.valueOf(objs[6])));
					beat.setStatusID(Integer.valueOf(String.valueOf(objs[7])));
					beat.setTenantID(Integer.valueOf(String.valueOf(objs[8])));
					if(objs[9] != null){
						beat.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
					}
					if(objs[10] != null){
						beat.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[10])).getTime()));
					}
					//Sets Beats list
					if(salesExecs.get(salesExeID).getBeats() == null){
						salesExecs.get(salesExeID).setBeats( new ArrayList<Beat>());
					}
					salesExecs.get(salesExeID).getBeats().add(beat);
					
					//Set Beat Ids
					if(salesExecs.get(salesExeID).getBeatIDLists() == null){
						salesExecs.get(salesExeID).setBeatIDLists(new ArrayList<Integer>());
					}
					salesExecs.get(salesExeID).getBeatIDLists().add(beat.getBeatID());
				}
				
				//Get Manufacturers
				SQLQuery manufQry = session.createSQLQuery("SELECT b.SALES_EXEC_ID, a.ID, a.NAME FROM MANUFACTURERS a, MANUFACTURER_SALES_EXECUTIVES b WHERE a.ID = b.MANUFACTURER_ID AND a.TENANT_ID = b.TENANT_ID AND b.SALES_EXEC_ID IN (" +StringUtils.join(userIDs, ",") +") AND b.TENANT_ID = ? order by b.SALES_EXEC_ID");
				manufQry.setParameter(0, tenantID);
				List manufs = manufQry.list();
				Map<Integer, Map<Integer, String>> salesExecManufMap = new HashMap<Integer, Map<Integer,String>>();
				for(Object obj : manufs){
					Object[] objs = (Object[])obj;
					int salesExId = Integer.valueOf(String.valueOf(objs[0]));
					int manufId = Integer.valueOf(String.valueOf(objs[1]));
					String manufName = String.valueOf(objs[2]);
					
					//Update Manufacturer
					if (salesExecs.get(salesExId).getManufacturers() == null) {
						salesExecs.get(salesExId).setManufacturers(new HashMap<Integer, String>());
					}
					salesExecs.get(salesExId).getManufacturers().put(manufId, manufName);
				}
			}
			
		}catch(Exception exception){
			logger.error("Error while fetching list of sales executives", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return new ArrayList<SalesExecutive>(salesExecs.values());
	}
	
	
	@Override
	public List<SalesExecutive> getSalesExecutivesHavingBeatsAssigned(int tenantID){
		Session session = null;
		Map<Integer, SalesExecutive> salesExecsMap = new HashMap<Integer, SalesExecutive>(); 
		try{
			Set<Integer> userIDs = new HashSet<Integer>();
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.*, b.ID BEAT_ID, b.NAME BEAT_NAME FROM USERS a, BEATS b, MANUFACTURER_SALES_EXEC_BEATS c, TENANT_USER d WHERE a.ID=c.SALES_EXEC_ID AND b.ID=c.BEAT_ID AND a.ID=D.USER_ID AND d.TENANT_ID= ?;");
			query.setParameter(0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
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
				if(objs[12] != null){
					salesExec.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[12])).getTime()));
				}
				
				if(objs[13] != null){
					salesExec.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[13])).getTime()));
				}
				
				if(!salesExecsMap.containsKey(salesExec.getUserID())){
					salesExecsMap.put(salesExec.getUserID(), salesExec);
				}
				
				//Add Beats
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[14])));
				beat.setName(String.valueOf(objs[15]));
				if(salesExecsMap.get(salesExec.getUserID()).getBeats() == null){
					salesExecsMap.get(salesExec.getUserID()).setBeats(new ArrayList<Beat>());
				}
				salesExecsMap.get(salesExec.getUserID()).getBeats().add(beat);
			}
		}catch(Exception exception){
			logger.error("Error while fetching list of sales executives", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return new ArrayList<SalesExecutive>(salesExecsMap.values());
	}

	@Override
	public void assignBeats(final int tenantID, final int manufacturerID, final int salesExecID, final List<Integer> beatIDs) throws Exception{
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
						String sqlInsert = "INSERT INTO MANUFACTURER_SALES_EXEC_BEATS (MANUFACTURER_ID, SALES_EXEC_ID, BEAT_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, ?, ?)";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int i = 0; i < beatIDs.size(); i++) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, salesExecID);
							pstmt.setInt(3, beatIDs.get(i));
							pstmt.setInt(4, 0);
							pstmt.setInt(5, tenantID);
							pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
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
			logger.error("Error while assigning beats.", exception);
			if(transaction != null){
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
	public void updateAssignedBeats(final int tenantID, final int manufacturerID, final int salesExecID, final List<Integer> beatIDs) throws Exception{
		Session session = null;
		Transaction transaction = null;
		Map<Integer, Integer> beatTranxCntMap = new HashMap<Integer, Integer>();
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Gets beats and their transaction count
			SQLQuery tranxCounterQry = session.createSQLQuery("SELECT BEAT_ID, TRANX_COUNTER FROM MANUFACTURER_SALES_EXEC_BEATS where SALES_EXEC_ID = ? AND TRANX_COUNTER > 0");
			tranxCounterQry.setInteger(0, salesExecID);
			List results = tranxCounterQry.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				beatTranxCntMap.put(Integer.valueOf(String.valueOf(objs[0])), Integer.valueOf(String.valueOf(objs[1])));
			}
			if (!beatIDs.containsAll(beatTranxCntMap.keySet())) {
				throw new CRMException("0000",
						"Assigned beats to Sales Executive could not be updated successfully because some of the beats for which transactions "
						+ "exists in system are not allowed to be dissociated from the Sales Executive. Please verify the list of beats before updating"
						+ " the association witn Sales Executive.");
			}
			//Delete existing sales Exec Beats
			SQLQuery deleteSalesExecBeats = session.createSQLQuery("DELETE FROM MANUFACTURER_SALES_EXEC_BEATS WHERE SALES_EXEC_ID =? AND MANUFACTURER_ID = ? AND TENANT_ID = ?");
			deleteSalesExecBeats.setParameter(0, salesExecID);
			deleteSalesExecBeats.setParameter(1, manufacturerID);
			deleteSalesExecBeats.setParameter(2, tenantID);
			deleteSalesExecBeats.executeUpdate();
			if(beatIDs.size() != 1 || beatIDs.get(0) != -1) {
				session.doWork(new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						PreparedStatement pstmt = null;
						try {
							String sqlInsert = "INSERT INTO MANUFACTURER_SALES_EXEC_BEATS (MANUFACTURER_ID, SALES_EXEC_ID, BEAT_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, ?, ?)";
							pstmt = connection.prepareStatement(sqlInsert);
							for (int i = 0; i < beatIDs.size(); i++) {
								pstmt.setInt(1, manufacturerID);
								pstmt.setInt(2, salesExecID);
								pstmt.setInt(3, beatIDs.get(i));
								pstmt.setInt(4, beatTranxCntMap.get(beatIDs.get(i)) == null ? 0 : beatTranxCntMap.get(beatIDs.get(i)));
								pstmt.setInt(5, tenantID);
								pstmt.setDate(6, new java.sql.Date(new Date().getTime()));
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
			logger.error("Error while updating assigned beats", exception);
			if(transaction != null){
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
	public List<SalesExecutive> getSalesExecMapsBeatsCustomers(int tenantID) {
		Session session = null;
		List<SalesExecutive> salesExecList = new ArrayList<SalesExecutive>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.ID, a.FIRST_NAME, a.LAST_NAME FROM USERS a, BEATS b, CUSTOMERS c, ORDER_BOOKING_SCHEDULE d, TENANT_USER e, ORDER_BOOKING_SCHEDULE_CUSTOMERS f  WHERE a.ID=d.SALES_EXEC_ID AND b.ID=d.BEAT_ID AND c.ID=f.CUSTOMER_ID AND d.ID=f.ORDER_BOOKING_SCHEDULE_ID AND e.USER_ID=a.ID AND e.TENANT_ID=? group by a.ID");
			query.setParameter(0, tenantID);
			List salesExecs = query.list();
			for(Object obj : salesExecs){
				Object[] objs = (Object[])obj;
				SalesExecutive salesExecutive = new SalesExecutive();
				salesExecutive.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				salesExecutive.setFirstName(String.valueOf(objs[1]));
				salesExecutive.setLastName(String.valueOf(objs[2]));
				salesExecList.add(salesExecutive);
			}
			return salesExecList;
		} catch (Exception exception) {
			logger.error("Error fetching sales executives mapped to beat and customer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public List<Beat> getAssignedBeats(int salesExecID, int tenantID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			SQLQuery beatsQuery = session.createSQLQuery("SELECT a.ID, a.NAME, a.DESCRIPTION, a.COVERAGE_SCHEDULE, a.DISTANCE, a.DATE_CREATED, a.DATE_MODIFIED FROM BEATS a, MANUFACTURER_SALES_EXEC_BEATS b WHERE a.ID=b.BEAT_ID AND a.TENANT_ID=b.TENANT_ID AND SALES_EXEC_ID=? AND a.TENANT_ID = ?");
			beatsQuery.setParameter(0, salesExecID);
			beatsQuery.setParameter(1, tenantID);
			List beatsList = beatsQuery.list();
			for(Object obj : beatsList){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setName(String.valueOf(objs[1]));
				beat.setDescription(String.valueOf(objs[2]));
				beat.setCoverageSchedule(String.valueOf(objs[3]));
				beat.setDistance(Integer.valueOf(String.valueOf(objs[4])));
				if(objs[5] != null){
					beat.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[5])).getTime()));
				}
				if(objs[6] != null){
					beat.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[6])).getTime()));
				}
				beats.add(beat);
			}
			return beats;
		} catch (Exception exception) {
			logger.error("Error while fetching assigned beats.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	

	@Override
	public List<Beat> getScheduledVisitSalesExecBeats(int salesExecID, Date visitDate, int tenantID) {
		Session session = null;
		List<Beat> beats = new ArrayList<Beat>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT b.ID, b.NAME FROM USERS a, BEATS b, ORDER_BOOKING_SCHEDULE c  WHERE a.ID=c.SALES_EXEC_ID AND b.ID=c.BEAT_ID AND a.TENANT_ID=b.TENANT_ID AND b.TENANT_ID=c.TENANT_ID AND a.ID=? AND c.VISIT_DATE = ? AND a.TENANT_ID= ? group by b.ID");
			query.setParameter(0, salesExecID);
			query.setDate(1, visitDate);
			query.setParameter(2, tenantID);
			List lists = query.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(objs[0])));
				beat.setName(String.valueOf(objs[1]));
				beats.add(beat);
			}
			return beats;
		} catch (Exception exception) {
			logger.error("Error fetching sales executives mapped to beat and customer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}

	@Override
	public List<SalesExecutive> getScheduledVisitSalesExecs(Date visitDate, int tenantID) {
		Session session = null;
		List<SalesExecutive> salesExecs = new ArrayList<SalesExecutive>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.ID, a.FIRST_NAME, a.LAST_NAME FROM USERS a, ORDER_BOOKING_SCHEDULE b  WHERE a.ID=b.SALES_EXEC_ID AND b.VISIT_DATE = ? AND b.TENANT_ID = ? group by a.ID");
			query.setDate(0, visitDate);
			query.setParameter(1, tenantID);
			List lists = query.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				SalesExecutive salesExecutive = new SalesExecutive();
				salesExecutive.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				salesExecutive.setFirstName(String.valueOf(objs[1]));
				salesExecutive.setLastName(String.valueOf(objs[2]));
				salesExecutive.setName(String.valueOf(objs[1]) +" "+ String.valueOf(objs[2]));
				salesExecs.add(salesExecutive);
			}
			return salesExecs;
		} catch (Exception exception) {
			logger.error("Error fetching sales executives mapped to beat and customer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}
	
	@Override
	public List<TrimmedCustomer> getScheduledVisitBeatCustomers(int salesExecID, Date visitDate, int beatID, int tenantID){
		Session session = null;
		List<TrimmedCustomer> trimmedCustomers = new ArrayList<TrimmedCustomer>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.ID, a.NAME FROM CUSTOMERS a, ORDER_BOOKING_SCHEDULE b, ORDER_BOOKING_SCHEDULE_CUSTOMERS c WHERE a.ID=c.CUSTOMER_ID AND c.ORDER_BOOKING_SCHEDULE_ID=b.ID AND a.TENANT_ID=b.TENANT_ID AND b.TENANT_ID=c.TENANT_ID AND b.SALES_EXEC_ID= ? AND b.VISIT_DATE= ? AND b.BEAT_ID= ? AND a.TENANT_ID = ? group by a.ID");
			query.setParameter(0, salesExecID);
			query.setDate(1, visitDate);
			query.setParameter(2, beatID);
			query.setParameter(3, tenantID);
			List lists = query.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				trimmedCustomers.add(trimmedCustomer);
			}
			return trimmedCustomers;
		} catch (Exception exception) {
			logger.error("Error fetching sales executives mapped to beat and customer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}
	
	@Override
	public void deleteBeatAssignment(int manufacturerID, int salesExecID, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM MANUFACTURER_SALES_EXEC_BEATS WHERE SALES_EXEC_ID= ? AND MANUFACTURER_ID = ? AND TENANT_ID = ?" );
			query.setParameter(0, salesExecID);
			query.setParameter(1, manufacturerID);
			query.setParameter(2, tenantID);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Sales Executive beats could not be successfully removed", exception);
			if(transaction != null){
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
	public int getSalesExecutiveCount(int tenantID){
		Session session = null;
		int counts = 0;
		try{
			session = sessionFactory.openSession();
			SQLQuery count = session.createSQLQuery("SELECT COUNT(*) FROM USERS a, USER_ROLE b, TENANT_USER c WHERE a.ID=b.USER_ID AND a.ID=c.USER_ID AND b.ROLE_ID= 2 AND c.TENANT_ID=?");
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
	public List<SalesExecutive> getSalesExecsNotMappedToManufacturer(int tenantID, int manufacturerID) {
		Session session = null;
		List<SalesExecutive> salesExecs = new ArrayList<SalesExecutive>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.ID, a.FIRST_NAME, a.LAST_NAME FROM USERS a, USER_ROLE b WHERE a.ID = b.USER_ID AND b.ROLE_ID = 2 AND a.STATUS_ID = ? AND a.ID NOT IN (SELECT SALES_EXEC_ID FROM MANUFACTURER_SALES_EXECUTIVES WHERE TENANT_ID= ? AND MANUFACTURER_ID = ?)");
			query.setParameter(0, EntityStatusEnum.ACTIVE.getEntityStatus());
			query.setParameter(1, tenantID);
			query.setParameter(2, manufacturerID);
			List lists = query.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				SalesExecutive salesExecutive = new SalesExecutive();
				salesExecutive.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				salesExecutive.setFirstName(String.valueOf(objs[1]));
				salesExecutive.setLastName(String.valueOf(objs[2]));
				salesExecs.add(salesExecutive);
			}
			return salesExecs;
		} catch (Exception exception) {
			logger.error("Error fetching sales executives not mapped to manufacturer.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return salesExecs;
	}
	
	@Override
	public Map<Integer, String> getManufacturerParams(int salesExecID, int tenantID){
		Session session = null;
		Map<Integer, String> manufacturerMap = new HashMap<Integer, String>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.ID, a.NAME FROM MANUFACTURERS a, MANUFACTURER_SALES_EXECUTIVES b WHERE a.ID = b.MANUFACTURER_ID AND a.TENANT_ID = b.TENANT_ID AND b.SALES_EXEC_ID = ? AND b.TENANT_ID = ?");
			query.setParameter(0, salesExecID);
			query.setParameter(1, tenantID);
			List lists = query.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				manufacturerMap.put(Integer.valueOf(String.valueOf(objs[0])), String.valueOf(objs[1]));
			}
		} catch (Exception exception) {
			logger.error("Error fetching manufacturer attributes for sales executive.", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return manufacturerMap;
		
	}
}
