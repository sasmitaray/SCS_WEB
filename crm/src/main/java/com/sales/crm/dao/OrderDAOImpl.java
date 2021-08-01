package com.sales.crm.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Order;
import com.sales.crm.model.OrderBookingSchedule;
import com.sales.crm.model.OrderBookingStats;
import com.sales.crm.model.ScheduledOrderSummary;

@Repository("orderDAO")
public class OrderDAOImpl implements OrderDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(OrderDAOImpl.class);
	
	private static List<Integer> statusList;
	
	private static Map<String, Object> filterMap = new HashMap<String, Object>();
	
	static {
		statusList = new ArrayList<Integer>();
		statusList.add(EntityStatusEnum.ORDER_CREATED.getEntityStatus());
		statusList.add(EntityStatusEnum.DELIVERY_SCHEDULED.getEntityStatus());
		statusList.add(EntityStatusEnum.DELIVERY_COMPLETED.getEntityStatus());
		statusList.add(EntityStatusEnum.PARTIALLY_DELIVERED.getEntityStatus());
		statusList.add(EntityStatusEnum.PAYMENT_SCHEDULED.getEntityStatus());
		statusList.add(EntityStatusEnum.PARTIALLY_PAID.getEntityStatus());
		
		filterMap.put("date", "VISIT_DATE");
		filterMap.put("salesExecID", "SALES_EXEC_ID");
		filterMap.put("beatID", "BEAT_ID");
		filterMap.put("customerID", "CUST_ID");
		filterMap.put("delivExecID", "DELIV_EXEC_ID");
	}
	
	@Override
	public int create(Order order) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			order.setDateCreated(new Date());
			order.setStatusID(EntityStatusEnum.ORDER_CREATED.getEntityStatus());
			order.setCode(UUID.randomUUID().toString());
			logger.debug(" Order :: "+ order);
			session.save(order);
			//Set status in ORDER_BOOKING_SCHEDULE_CUSTOMERS
			SQLQuery updateStatus = session.createSQLQuery("UPDATE ORDER_BOOKING_SCHEDULE_CUSTOMERS SET STATUS_ID = ? WHERE ORDER_BOOKING_SCHEDULE_ID = ? AND CUSTOMER_ID = ?");
			updateStatus.setParameter(0, EntityStatusEnum.ORDER_CREATED.getEntityStatus());
			updateStatus.setParameter(1, order.getOrderBookingID());
			updateStatus.setParameter(2, order.getCustomerID());
			updateStatus.executeUpdate();
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating order", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return order.getOrderID();
	}

	@Override
	public int scheduleOrderBooking(final OrderBookingSchedule orderBookingSchedule) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			final List<Integer> custIDList = orderBookingSchedule.getCustomerIDs();
			final List<Integer> manufacturerIDList = orderBookingSchedule.getManufacturerIDs();
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("INSERT INTO ORDER_BOOKING_SCHEDULE (SALES_EXEC_ID, BEAT_ID, VISIT_DATE, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())");
			query.setInteger(0, orderBookingSchedule.getSalesExecutiveID());
			query.setInteger(1, orderBookingSchedule.getBeatID());
			query.setDate(2, orderBookingSchedule.getVisitDate());
			query.setInteger(3, orderBookingSchedule.getTenantID());
			query.executeUpdate();
			orderBookingSchedule.setBookingScheduleID(((BigInteger) session.createSQLQuery("SELECT LAST_INSERT_ID()").uniqueResult()).intValue());
			
			//ORDER_BOOKING_SCHEDULE_CUSTOMERS
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO ORDER_BOOKING_SCHEDULE_CUSTOMERS (ORDER_BOOKING_SCHEDULE_ID, CUSTOMER_ID, STATUS_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int i = 0; i < custIDList.size(); i++) {
							pstmt.setInt(1, orderBookingSchedule.getBookingScheduleID());
							pstmt.setInt(2, custIDList.get(i));
							pstmt.setInt(3, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
							pstmt.setInt(4, orderBookingSchedule.getTenantID());
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//ORDER_BOOKING_SCHEDULE_MANUFACTURERS
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO ORDER_BOOKING_SCHEDULE_MANUFACTURERS (ORDER_BOOKING_SCHEDULE_ID, MANUFACTURER_ID, STATUS_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int i = 0; i < manufacturerIDList.size(); i++) {
							pstmt.setInt(1, orderBookingSchedule.getBookingScheduleID());
							pstmt.setInt(2, manufacturerIDList.get(i));
							pstmt.setInt(3, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
							pstmt.setInt(4, orderBookingSchedule.getTenantID());
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//Increase counter
			//MANUFACTURER_SALES_EXEC_BEATS
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE manufacturer_sales_exec_beats " + 
								"            SET tranx_counter = " + 
								" (select  tranx_counter from (SELECT tranx_counter " + 
								"              FROM   manufacturer_sales_exec_beats " + 
								"              WHERE  sales_exec_id = ? " + 
								"                     AND beat_id = ? "   +
								"                     AND MANUFACTURER_ID = ? ) TABLE1) + ? WHERE  sales_exec_id = ? " + 
								"                     AND beat_id = ?  " +
								"                     AND MANUFACTURER_ID = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int manufID : manufacturerIDList) {
							pstmt.setInt(1, orderBookingSchedule.getSalesExecutiveID());
							pstmt.setInt(2, orderBookingSchedule.getBeatID());
							pstmt.setInt(3, manufID);
							pstmt.setInt(4, custIDList.size());
							pstmt.setInt(5, orderBookingSchedule.getSalesExecutiveID());
							pstmt.setInt(6, orderBookingSchedule.getBeatID());
							pstmt.setInt(7, manufID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//increase counter
			//MANUFACTURER_BEAT
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE manufacturer_beat " + 
								"SET    tranx_counter = (SELECT tranx_counter " + 
								"                        FROM   (SELECT tranx_counter " + 
								"                                FROM   manufacturer_beat " + 
								"                                WHERE  manufacturer_id = ? " + 
								"                                       AND beat_id = ?) TABLE1) " + 
								"                       + ? " + 
								"WHERE  manufacturer_id = ? " + 
								"       AND beat_id = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int manufID : manufacturerIDList) {
							pstmt.setInt(1, manufID);
							pstmt.setInt(2, orderBookingSchedule.getBeatID());
							pstmt.setInt(3, custIDList.size());
							pstmt.setInt(4, manufID);
							pstmt.setInt(5, orderBookingSchedule.getBeatID());
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//increase counter
			//MANUFACTURER_SALES_EXECUTIVES
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE MANUFACTURER_SALES_EXECUTIVES " + 
								"SET    tranx_counter = (SELECT tranx_counter " + 
								"                        FROM   (SELECT tranx_counter " + 
								"                                FROM   MANUFACTURER_SALES_EXECUTIVES " + 
								"                                WHERE  manufacturer_id = ? " + 
								"                                       AND sales_exec_id = ?) TABLE1) " + 
								"                       + ? " + 
								"WHERE  manufacturer_id = ? " + 
								"       AND sales_exec_id = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int manufID : manufacturerIDList) {
							pstmt.setInt(1, manufID);
							pstmt.setInt(2, orderBookingSchedule.getSalesExecutiveID());
							pstmt.setInt(3, custIDList.size());
							pstmt.setInt(4, manufID);
							pstmt.setInt(5, orderBookingSchedule.getSalesExecutiveID());
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//increase counter
			//BEAT_CUSTOMER
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE beat_customer " + 
								"SET    tranx_counter = (SELECT tranx_counter " + 
								"                        FROM   (SELECT tranx_counter " + 
								"                                FROM   beat_customer " + 
								"                                WHERE  customer_id = ? " + 
								"                                       AND beat_id = ?) TABLE1) " + 
								"                       + ? " + 
								"WHERE  customer_id = ? " + 
								"       AND beat_id = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int customerID : custIDList) {
							pstmt.setInt(1, customerID);
							pstmt.setInt(2, orderBookingSchedule.getBeatID());
							pstmt.setInt(3, 1);
							pstmt.setInt(4, customerID);
							pstmt.setInt(5, orderBookingSchedule.getBeatID());
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//Increase Customer Tranx counter
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE customers " + 
								"SET    tranx_counter = (SELECT tranx_counter " + 
								"                        FROM   (SELECT tranx_counter " + 
								"                                FROM   customers " + 
								"                                WHERE  id = ?) TABLE1) " + 
								"                       + 1 " + 
								"WHERE  id = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int customerID : custIDList) {
							pstmt.setInt(1, customerID);
							pstmt.setInt(2, customerID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//Increase Manufacturer Tranx counter
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE MANUFACTURERS " + 
								"SET    tranx_counter = (SELECT tranx_counter " + 
								"                        FROM   (SELECT tranx_counter " + 
								"                                FROM   MANUFACTURERS " + 
								"                                WHERE  id = ?) TABLE1) " + 
								"                       + ? " + 
								"WHERE  id = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int manufacturerID : manufacturerIDList) {
							pstmt.setInt(1, manufacturerID);
							pstmt.setInt(2, custIDList.size());
							pstmt.setInt(3, manufacturerID);
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			
			//Increase Beat Tranx counter
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "UPDATE BEATS " + 
								"SET    tranx_counter = (SELECT tranx_counter " + 
								"                        FROM   (SELECT tranx_counter " + 
								"                                FROM   BEATS " + 
								"                                WHERE  id = ?) TABLE1) " + 
								"                       + 1 " + 
								"WHERE  id = ? ";
						pstmt = connection.prepareStatement(sqlInsert);
						for (int customerID : custIDList) {
							pstmt.setInt(1, orderBookingSchedule.getBeatID());
							pstmt.setInt(2, orderBookingSchedule.getBeatID());
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
			logger.error("Error while scheduling sales executives visti.", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return orderBookingSchedule.getBookingScheduleID();
	}
	
	@Override
	public List<String> alreadyOrderBookingScheduledCustomer(OrderBookingSchedule orderBookingSchedule) throws Exception{
		Session session = null;
		List<String> customerNames = new ArrayList<String>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.NAME FROM CUSTOMERS a, ORDER_BOOKING_SCHEDULE b, ORDER_BOOKING_SCHEDULE_CUSTOMERS c WHERE a.ID=c.CUSTOMER_ID AND b.ID=c.ORDER_BOOKING_SCHEDULE_ID AND b.SALES_EXEC_ID=? AND b.BEAT_ID=? AND b.VISIT_DATE = ? AND b.TENANT_ID= ? AND c.STATUS_ID = ? AND c.CUSTOMER_ID IN ("+ StringUtils.join(orderBookingSchedule.getCustomerIDs(), ",")+") group by a.NAME");
			query.setParameter(0, orderBookingSchedule.getSalesExecutiveID());
			query.setParameter(1, orderBookingSchedule.getBeatID());
			query.setDate(2, orderBookingSchedule.getVisitDate());
			query.setParameter(3, orderBookingSchedule.getTenantID());
			query.setInteger(4, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			List lists = query.list();
			for(Object obj : lists){
				customerNames.add(String.valueOf(obj));
			}
		} catch (Exception exception) {
			logger.error("Error fetching sales executives mapped to beat and customer.", exception);
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return customerNames;
	}
	

	@Override
	public void unScheduleOrderBooking(int orderScheduleID, int customerID, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			//Decrease counter
			//MANUFACTURER_SALES_EXEC_BEATS
			SQLQuery idQry = session.createSQLQuery("SELECT manufacturer_sales_exec_beats.id, " + 
					"       manufacturer_sales_exec_beats.tranx_counter " + 
					"FROM   manufacturer_sales_exec_beats " + 
					"       INNER JOIN order_booking_schedule " + 
					"               ON manufacturer_sales_exec_beats.sales_exec_id = " + 
					"                             order_booking_schedule.sales_exec_id " + 
					"                  AND manufacturer_sales_exec_beats.beat_id = " + 
					"                      order_booking_schedule.beat_id " + 
					"                  AND order_booking_schedule.id = ? ");
			idQry.setInteger(0, orderScheduleID);
			List lists = idQry.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE MANUFACTURER_SALES_EXEC_BEATS SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Decrease counter
			//MANUFACTURER_BEAT
			SQLQuery idQry1 = session.createSQLQuery("select " + 
					"MANUFACTURER_BEAT.ID, " + 
					"MANUFACTURER_BEAT.TRANX_COUNTER " + 
					"from " + 
					"MANUFACTURER_BEAT " + 
					"INNER JOIN " + 
					"(select a.beat_id, b.MANUFACTURER_ID from  " + 
					"ORDER_BOOKING_SCHEDULE a, ORDER_BOOKING_SCHEDULE_MANUFACTURERS b " + 
					"WHERE  " + 
					"a.ID = b.ORDER_BOOKING_SCHEDULE_ID AND " + 
					"a.ID = ?) AS TABLE1 " + 
					"ON " + 
					"MANUFACTURER_BEAT.MANUFACTURER_ID = TABLE1.MANUFACTURER_ID " + 
					"AND MANUFACTURER_BEAT.BEAT_ID = TABLE1.BEAT_ID");
			idQry1.setInteger(0, orderScheduleID);
			List lists1 = idQry1.list();
			for(Object obj : lists1){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE MANUFACTURER_BEAT SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Decrease counter
			//MANUFACTURER_SALES_EXECUTIVES
			SQLQuery idQry2 = session.createSQLQuery("SELECT MANUFACTURER_SALES_EXECUTIVES.id, " + 
					"       MANUFACTURER_SALES_EXECUTIVES.tranx_counter " + 
					"FROM   MANUFACTURER_SALES_EXECUTIVES " + 
					"       INNER JOIN (SELECT a.SALES_EXEC_ID, " + 
					"                          b.manufacturer_id " + 
					"                   FROM   order_booking_schedule a, " + 
					"                          order_booking_schedule_manufacturers b " + 
					"                   WHERE  a.id = b.order_booking_schedule_id " + 
					"                          AND a.id = ?) AS TABLE1 " + 
					"               ON MANUFACTURER_SALES_EXECUTIVES.manufacturer_id = TABLE1.manufacturer_id " + 
					"                  AND MANUFACTURER_SALES_EXECUTIVES.SALES_EXEC_ID = TABLE1.SALES_EXEC_ID ");
			idQry2.setInteger(0, orderScheduleID);
			List lists2 = idQry2.list();
			for(Object obj : lists2){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE MANUFACTURER_SALES_EXECUTIVES SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Decrease counter
			//BEAT_CUSTOMER
			SQLQuery idQry3 = session.createSQLQuery("SELECT id, " + 
					"       tranx_counter " + 
					"FROM   beat_customer " + 
					"WHERE  beat_id = (SELECT a.beat_id " + 
					"                  FROM   order_booking_schedule a, " + 
					"                         order_booking_schedule_customers b " + 
					"                  WHERE  a.id = b.order_booking_schedule_id " + 
					"                         AND  a.tenant_id = b.tenant_id " + 
					"                         AND b.customer_id = ? " + 
					"                         AND a.id = ? AND a.tenant_id = ?) " + 
					"       AND customer_id = ? AND tenant_id = ? ");
			idQry3.setInteger(0, customerID);
			idQry3.setInteger(1, orderScheduleID);
			idQry3.setInteger(2, tenantID);
			idQry3.setInteger(3, customerID);
			idQry3.setInteger(4, tenantID);
			List lists3 = idQry3.list();
			for(Object obj : lists3){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE BEAT_CUSTOMER SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Decrease customer TRANX_COUNTER 
			SQLQuery idQry4 = session.createSQLQuery("UPDATE customers  " + 
					"SET    tranx_counter = (SELECT tranx_counter   " + 
					"                        FROM   (SELECT tranx_counter   " + 
					"                                FROM   customers   " + 
					"                                WHERE  ID = ? ) TABLE1) - 1   " + 
					" WHERE  ID = ?  ");
			idQry4.setInteger(0, customerID);
			idQry4.setInteger(1, customerID);
			idQry4.executeUpdate();
			
			//decrease tranx counter for Manufacturer
			SQLQuery idQry5 = session.createSQLQuery("SELECT id, " + 
					"       tranx_counter " + 
					"FROM   manufacturers " + 
					"WHERE  id IN (SELECT manufacturer_id " + 
					"              FROM   order_booking_schedule_manufacturers " + 
					"              WHERE  order_booking_schedule_id = ?) ");
			idQry5.setInteger(0, orderScheduleID);
			List lists5 = idQry5.list();
			for(Object obj : lists5){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE MANUFACTURERS SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Decrease BEAT TRANX_COUNTER 
			SQLQuery idQry6 = session.createSQLQuery("UPDATE beats " + 
					"SET    tranx_counter = (SELECT tranx_counter " + 
					"                        FROM   (SELECT tranx_counter " + 
					"                                FROM   beats " + 
					"                                WHERE  id = (SELECT beat_id " + 
					"                                             FROM   order_booking_schedule " + 
					"                                             WHERE  id = ?)) TABLE1) - 1 " + 
					"WHERE  id = (SELECT beat_id " + 
					"             FROM   order_booking_schedule " + 
					"             WHERE  id = ?) ");
			idQry6.setInteger(0, orderScheduleID);
			idQry6.setInteger(1, orderScheduleID);
			idQry6.executeUpdate();
			
			
			//Delete from ORDER_BOOKING_SCHEDULE_CUSTOMERS
			SQLQuery deleteScheduledCust = session.createSQLQuery("DELETE FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS WHERE ORDER_BOOKING_SCHEDULE_ID = ? AND CUSTOMER_ID = ? AND STATUS_ID = ? AND TENANT_ID = ?");
			deleteScheduledCust.setParameter(0, orderScheduleID);
			deleteScheduledCust.setParameter(1, customerID);
			deleteScheduledCust.setParameter(2, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			deleteScheduledCust.setParameter(3, tenantID);
			deleteScheduledCust.executeUpdate();
			
			//Delete from ORDER_BOOKING_SCHEDULE if no reference in ORDER_BOOKING_SCHEDULE_CUSTOMERS
			SQLQuery getSchedulesForCust = session.createSQLQuery("SELECT COUNT(*) FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS WHERE ORDER_BOOKING_SCHEDULE_ID = ? AND TENANT_ID = ?");
			getSchedulesForCust.setParameter(0, orderScheduleID);
			getSchedulesForCust.setParameter(1, tenantID);
			List counts = getSchedulesForCust.list();
			if(counts != null && counts.size() == 1 && ((BigInteger)counts.get(0)).intValue() == 0){
				//Delete from ORDER_BOOKING_SCHEDULE
				SQLQuery query = session.createSQLQuery(
						"DELETE FROM ORDER_BOOKING_SCHEDULE WHERE ID = ? AND TENANT_ID = ?");
				query.setParameter(0, orderScheduleID);
				query.setParameter(1, tenantID);
				query.executeUpdate();
			}
			
			transaction.commit();
		} catch (Exception exception) {
			if(transaction != null){
				transaction.rollback();
			}
			logger.error("Error unscheduling order booking.", exception);
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public List<Order> getOrders(int tenantID, int orderID) throws Exception {
		Session session = null;
		List<Order> orders = new ArrayList<Order>();
		SQLQuery orderQuery = null;
		try{
			session = sessionFactory.openSession();
			if(orderID != -1){
				orderQuery = session.createSQLQuery("SELECT ORDERS.*, " + 
						"ORDER_REMOVAL_DETAILS.REMOVAL_REASON  " + 
						"FROM " + 
						"(SELECT a.*, " + 
						"       c.NAME, " + 
						"       concat (d.FIRST_NAME,\" \", d.LAST_NAME) SALES_EXEC " + 
						"FROM   order_details a, " + 
						"       order_booking_schedule b, " + 
						"       customers c, " + 
						"       USERS d " + 
						"WHERE  a.order_booking_id = b.id " + 
						"       AND a.customer_id = c.id " + 
						"       AND a.tenant_id = b.tenant_id " + 
						"       AND b.tenant_id = c.tenant_id " + 
						"       AND c.tenant_id = d.tenant_id " + 
						"       AND d.ID = b.SALES_EXEC_ID " + 
						"       AND a.tenant_id = ? " + 
						"       AND a.id = ? " + 
						") ORDERS " + 
						"LEFT JOIN ORDER_REMOVAL_DETAILS " + 
						"ON ORDERS.ID = ORDER_REMOVAL_DETAILS.ORDER_ID " + 
						"ORDER BY DATE_CREATED DESC");
				orderQuery.setParameter(0, tenantID);
				orderQuery.setParameter(1, orderID);
			}else{
				orderQuery = session.createSQLQuery("SELECT ORDERS.*, " + 
						"ORDER_REMOVAL_DETAILS.REMOVAL_REASON  " + 
						"FROM " + 
						"(SELECT a.*, " + 
						"       c.NAME, " + 
						"       concat (d.FIRST_NAME,\" \", d.LAST_NAME) SALES_EXEC " + 
						"FROM   order_details a, " + 
						"       order_booking_schedule b, " + 
						"       customers c, " + 
						"       USERS d " + 
						"WHERE  a.order_booking_id = b.id " + 
						"       AND a.customer_id = c.id " + 
						"       AND a.tenant_id = b.tenant_id " + 
						"       AND b.tenant_id = c.tenant_id " + 
						"       AND c.tenant_id = d.tenant_id " + 
						"       AND d.ID = b.SALES_EXEC_ID " + 
						"       AND a.tenant_id = ? " + 
						") ORDERS " + 
						"LEFT JOIN ORDER_REMOVAL_DETAILS " + 
						"ON ORDERS.ID = ORDER_REMOVAL_DETAILS.ORDER_ID " + 
						"ORDER BY DATE_CREATED DESC");
				orderQuery.setParameter(0, tenantID);
			}
			List results = orderQuery.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				Order order = new Order();
				order.setOrderID(Integer.valueOf(String.valueOf(objs[0])));
				order.setCode(String.valueOf(objs[1]));
				order.setOrderBookingID(Integer.valueOf(String.valueOf(objs[2])));
				order.setNoOfLineItems(Integer.valueOf(String.valueOf(objs[3])));
				order.setBookValue(Double.valueOf(String.valueOf(objs[4])));
				order.setRemark(String.valueOf(objs[5]));
				order.setStatusID(Integer.valueOf(String.valueOf(objs[6])));
				switch(order.getStatusID()){
					case 50:
						order.setStatusAsString("Order Booking Scheduled");
						break;
					case 51:
						order.setStatusAsString("Order Created");
						break;
					case 52:
						order.setStatusAsString("Delivery Scheduled");
						break;
					case 53:
						order.setStatusAsString("Delivery Completed");
						break;
					case 54:
						order.setStatusAsString("Delivery Partial");
						break;
					case 55:
						order.setStatusAsString("Payment Scheduled");
						break;	
					case 56:
						order.setStatusAsString("Payment Completed");
						break;	
					case 57:
						order.setStatusAsString("Payment Partial");
						break;	
					case 58:
						order.setStatusAsString("Order Deleted");
						break;		
				}
				order.setCustomerID(Integer.valueOf(String.valueOf(objs[7])));
				order.setTenantID(tenantID);
				if(objs[9] != null){
					order.setDateCreated(new Date(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[9])).getTime()));
				}
				order.setCustomerName(String.valueOf(objs[11]));
				order.setSalesExecName(String.valueOf(objs[12]));
				order.setRemovalReason(String.valueOf(objs[13]).equals("null") ? "": String.valueOf(objs[13]));
				orders.add(order);
			}
		}catch(Exception e){
			logger.error("Error while fetching order list", e);
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return orders;
	}

	@Override
	public OrderBookingStats getOrderBookingStats(int salesExecID, Date date, int tenantID) throws Exception{
		Session session = null;
		OrderBookingStats orderBookingStats = new OrderBookingStats();
		try{
			session = sessionFactory.openSession();
			//all orders booked
			SQLQuery allOrderBooking = session.createSQLQuery("SELECT b.NAME FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS a, CUSTOMERS b WHERE a.CUSTOMER_ID = b.ID AND a.TENANT_ID=b.TENANT_ID AND ORDER_BOOKING_SCHEDULE_ID IN (SELECT ID FROM ORDER_BOOKING_SCHEDULE WHERE SALES_EXEC_ID = ? AND VISIT_DATE = ?) AND a.TENANT_ID = ?");
			allOrderBooking.setParameter(0, salesExecID);
			allOrderBooking.setParameter(1, new java.sql.Date(date.getTime()));
			allOrderBooking.setParameter(2, tenantID);
			List<String> allOrderBookedCustomers = allOrderBooking.list();
			orderBookingStats.setTotalNoOfVisits(allOrderBookedCustomers.size());
			orderBookingStats.setAllCustomersForVisit(allOrderBookedCustomers);
			
			//Order Booking completed
			SQLQuery completedOrderBookingQuery = session.createSQLQuery("SELECT b.NAME FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS a, CUSTOMERS b WHERE a.CUSTOMER_ID = b.ID AND a.TENANT_ID=b.TENANT_ID AND ORDER_BOOKING_SCHEDULE_ID IN (SELECT ID FROM ORDER_BOOKING_SCHEDULE WHERE SALES_EXEC_ID = ? AND VISIT_DATE = ? ) AND a.STATUS_ID = ? AND a.TENANT_ID = ?");
			completedOrderBookingQuery.setParameter(0, salesExecID);
			completedOrderBookingQuery.setParameter(1, new java.sql.Date(date.getTime()));
			completedOrderBookingQuery.setParameter(2, EntityStatusEnum.ORDER_CREATED.getEntityStatus());
			completedOrderBookingQuery.setParameter(3, tenantID);
			List<String> allOrderCompletedCustomers = completedOrderBookingQuery.list();
			orderBookingStats.setNoOfVisitsCompleted(allOrderCompletedCustomers.size());
			orderBookingStats.setCompletedCustomers(allOrderCompletedCustomers);
			
			//Order Booking Pending
			SQLQuery pendingOrderBookingQuery = session.createSQLQuery("SELECT b.NAME FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS a, CUSTOMERS b WHERE a.CUSTOMER_ID = b.ID AND a.TENANT_ID=b.TENANT_ID AND ORDER_BOOKING_SCHEDULE_ID IN (SELECT ID FROM ORDER_BOOKING_SCHEDULE WHERE SALES_EXEC_ID = ? AND VISIT_DATE = ? ) AND a.STATUS_ID= ? AND a.TENANT_ID = ?");
			pendingOrderBookingQuery.setParameter(0, salesExecID);
			pendingOrderBookingQuery.setParameter(1, new java.sql.Date(date.getTime()));
			pendingOrderBookingQuery.setParameter(2, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			pendingOrderBookingQuery.setParameter(3, tenantID);
			List<String> pendingOrderCustomers = pendingOrderBookingQuery.list();
			orderBookingStats.setNoOfVisitsPending(pendingOrderCustomers.size());
			orderBookingStats.setPendingCustomers(pendingOrderCustomers);
					
		}catch(Exception exception){
			logger.error("Error while fetching order booking stats.", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return orderBookingStats;
	}

	@Override
	public List<OrderBookingSchedule> getAllOrderBookedForDate(int tenantID, Date date) throws Exception{
		Session session = null;
		List<OrderBookingSchedule> orderSchedules = new ArrayList<OrderBookingSchedule>();
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(
					"SELECT a.ID, b.ID CUST_ID, b.NAME CUST_NAME, d.ID BEAT_ID, d.NAME BEAT_NAME, c.ID SALES_EXEC_ID, c.FIRST_NAME, c.LAST_NAME FROM ORDER_BOOKING_SCHEDULE a, CUSTOMERS b, USERS c, BEATS d, ORDER_BOOKING_SCHEDULE_CUSTOMERS e WHERE e.CUSTOMER_ID=b.ID AND e.ORDER_BOOKING_SCHEDULE_ID=a.ID AND a.BEAT_ID = d.ID AND a.SALES_EXEC_ID = c.ID AND a.TENANT_ID=b.TENANT_ID AND a.TENANT_ID=d.TENANT_ID AND a.TENANT_ID= ? AND a.VISIT_DATE = ? AND e.STATUS_ID= ?");
			query.setParameter(0, tenantID);
			query.setParameter(1, new java.sql.Date(date.getTime()));
			query.setParameter(2, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				OrderBookingSchedule orderBookingSchedule = new OrderBookingSchedule();
				orderBookingSchedule.setBookingScheduleID(Integer.parseInt(String.valueOf(objs[0])));
				orderBookingSchedule.setCustomerID(Integer.parseInt(String.valueOf(objs[1])));
				orderBookingSchedule.setCustomerName(String.valueOf(objs[2]));
				orderBookingSchedule.setBeatID(Integer.parseInt(String.valueOf(objs[3])));
				orderBookingSchedule.setBeatName(String.valueOf(objs[4]));
				orderBookingSchedule.setSalesExecutiveID(Integer.parseInt(String.valueOf(objs[5])));
				orderBookingSchedule.setSalesExecName(String.valueOf(objs[6]) +" "+ String.valueOf(objs[7]));
				orderSchedules.add(orderBookingSchedule);
			}

		}catch(Exception exception){
			logger.error("Error while fetching scheduled order bookings for " + new SimpleDateFormat("dd-MM-yyyy").format(date) +".");
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return orderSchedules;
	}
	
	/**
	 * 
	 * Fetch schedules with status 1
	 * 
	 */
	@Override
	public List<OrderBookingSchedule> getOrdersBookingSchedules(int tenantID, int salesExecID, int beatID, Date date) throws Exception{
		Session session = null;
		List<OrderBookingSchedule> orderSchedules = new ArrayList<OrderBookingSchedule>();
		try{
			session = sessionFactory.openSession();
			StringBuilder sqlBuilder = new StringBuilder(
					"SELECT a.ID, b.ID CUST_ID, b.NAME CUST_NAME, d.ID BEAT_ID, d.NAME BEAT_NAME, c.ID SALES_EXEC_ID, c.FIRST_NAME, c.LAST_NAME FROM ORDER_BOOKING_SCHEDULE a, CUSTOMERS b, USERS c, BEATS d, ORDER_BOOKING_SCHEDULE_CUSTOMERS e WHERE e.CUSTOMER_ID=b.ID AND e.ORDER_BOOKING_SCHEDULE_ID=a.ID AND a.BEAT_ID = d.ID AND a.SALES_EXEC_ID = c.ID AND a.TENANT_ID=b.TENANT_ID AND a.TENANT_ID=d.TENANT_ID AND e.STATUS_ID = "+ EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			//Add Reseller ID
			sqlBuilder.append(" AND a.TENANT_ID ="+ tenantID);
			//Sales Exec Id
			if(salesExecID > 0){
				sqlBuilder.append(" AND a.SALES_EXEC_ID ="+ salesExecID);
			}
			//Beat Id
			if(beatID > 0){
				sqlBuilder.append(" AND a.BEAT_ID ="+ beatID);
			}
			//Visit Date
			if(date != null){
				sqlBuilder.append(" AND a.VISIT_DATE ='"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"'");
			}
			
			logger.debug(" getOrdersBookingSchedule sql "+ sqlBuilder.toString());
			
			SQLQuery query = session.createSQLQuery(sqlBuilder.toString());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				OrderBookingSchedule orderBookingSchedule = new OrderBookingSchedule();
				orderBookingSchedule.setBookingScheduleID(Integer.parseInt(String.valueOf(objs[0])));
				orderBookingSchedule.setCustomerID(Integer.parseInt(String.valueOf(objs[1])));
				orderBookingSchedule.setCustomerName(String.valueOf(objs[2]));
				orderBookingSchedule.setBeatID(Integer.parseInt(String.valueOf(objs[3])));
				orderBookingSchedule.setBeatName(String.valueOf(objs[4]));
				orderBookingSchedule.setSalesExecutiveID(Integer.parseInt(String.valueOf(objs[5])));
				orderBookingSchedule.setSalesExecName(String.valueOf(objs[6]) +" "+ String.valueOf(objs[7]));
				orderSchedules.add(orderBookingSchedule);
			}

		}catch(Exception exception){
			logger.error("Error while fetching scheduled order bookings for " + new SimpleDateFormat("dd-MM-yyyy").format(date) +".");
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return orderSchedules;
	}
	
	
	@Override
	public List<OrderBookingSchedule> getOrderScheduleReport(int tenantID, int salesExecID, int beatID, int customerID, int orderScheduleID, int status, Date date) throws Exception{
		Session session = null;
		List<OrderBookingSchedule> orderSchedules = new ArrayList<OrderBookingSchedule>();
		boolean whereClauseAdded = false;
		try{
			StringBuilder sqlBuilder = new StringBuilder(
					"SELECT * FROM (SELECT ORDER_SCHEDULE_ID, ORDER_DETAILS.ID ORDER_ID, CUST_ID, BEAT_ID, SALES_EXEC_ID, VISIT_DATE SALES_VISIT_DATE, BEAT_NAME, CUST_NAME, FIRST_NAME, LAST_NAME, ORDER_SCHEDULE.STATUS_ID STATUS FROM (SELECT a.ID ORDER_SCHEDULE_ID, a.VISIT_DATE, b.ID CUST_ID, b.NAME CUST_NAME, d.ID BEAT_ID, d.NAME BEAT_NAME, c.ID SALES_EXEC_ID, c.FIRST_NAME, c.LAST_NAME, e.STATUS_ID FROM ORDER_BOOKING_SCHEDULE a, CUSTOMERS b, USERS c, BEATS d, ORDER_BOOKING_SCHEDULE_CUSTOMERS e WHERE e.CUSTOMER_ID=b.ID AND e.ORDER_BOOKING_SCHEDULE_ID=a.ID AND a.BEAT_ID = d.ID AND a.SALES_EXEC_ID = c.ID AND a.TENANT_ID=b.TENANT_ID AND a.TENANT_ID=d.TENANT_ID AND a.TENANT_ID = "+ tenantID +") ORDER_SCHEDULE LEFT JOIN ORDER_DETAILS ON ORDER_SCHEDULE.ORDER_SCHEDULE_ID = ORDER_DETAILS.ORDER_BOOKING_ID AND ORDER_SCHEDULE.CUST_ID = ORDER_DETAILS.CUSTOMER_ID) FINAL");
			
			if (salesExecID > 0 || beatID > 0 || customerID > 0 || date != null || orderScheduleID > 0) {
				sqlBuilder.append(" WHERE ");
			}
			
			//Sales Exec Id
			if(salesExecID > 0){
				if(whereClauseAdded){
					sqlBuilder.append(" AND ");
				}
				sqlBuilder.append(" SALES_EXEC_ID ="+ salesExecID);
				whereClauseAdded = true;
			}
			
			//Beat Id
			if(beatID > 0){
				if(whereClauseAdded){
					sqlBuilder.append(" AND ");
				}
				sqlBuilder.append(" BEAT_ID ="+ beatID);
				whereClauseAdded = true;
			}
			
			//Customer ID
			if(customerID > 0){
				if(whereClauseAdded){
					sqlBuilder.append(" AND ");
				}
				sqlBuilder.append(" CUST_ID ="+ customerID);
				whereClauseAdded = true;
			}
			
			//Visit Date
			if(date != null){
				if(whereClauseAdded){
					sqlBuilder.append(" AND ");
				}
				sqlBuilder.append(" SALES_VISIT_DATE ='"+new SimpleDateFormat("yyyy-MM-dd").format(date)+"'");
				whereClauseAdded = true;
			}
			
			//Status
			if(status > 0){
				if(whereClauseAdded){
					sqlBuilder.append(" AND ");
				}
				sqlBuilder.append(" STATUS_ID ="+ status);
				whereClauseAdded = true;
			}
			
			if(orderScheduleID > 0){
				if(whereClauseAdded){
					sqlBuilder.append(" AND ");
				}
				sqlBuilder.append(" ORDER_SCHEDULE_ID ="+ orderScheduleID);
				whereClauseAdded = true;
			}
			
			logger.debug(" getOrderScheduleReport sql "+ sqlBuilder.toString());
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(sqlBuilder.toString());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				OrderBookingSchedule orderBookingSchedule = new OrderBookingSchedule();
				orderBookingSchedule.setBookingScheduleID(Integer.parseInt(String.valueOf(objs[0])));
				if(String.valueOf(objs[1]) != null && !String.valueOf(objs[1]).equals("null")){
					orderBookingSchedule.setOrderID(Integer.parseInt(String.valueOf(objs[1])));
				}
				orderBookingSchedule.setCustomerID(Integer.parseInt(String.valueOf(objs[2])));
				orderBookingSchedule.setVisitDateAsString(new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[5]))));
				orderBookingSchedule.setBeatName(String.valueOf(objs[6]));
				orderBookingSchedule.setCustomerName(String.valueOf(objs[7]));
				orderBookingSchedule.setSalesExecName(String.valueOf(objs[8]) +" "+ (String.valueOf(objs[9]) != null ? String.valueOf(objs[9]) : ""));
				if(String.valueOf(objs[10]) != null && !String.valueOf(objs[10]).equals("null")){
					orderBookingSchedule.setStatus(Integer.valueOf(String.valueOf(objs[10])));
					switch(orderBookingSchedule.getStatus()){
					case 50:
						orderBookingSchedule.setStatusAsString("Order Booking Scheduled");
						break;
					case 51:
						orderBookingSchedule.setStatusAsString("Order Created");
						break;
					case 52:
						orderBookingSchedule.setStatusAsString("Delivery Scheduled");
						break;
					case 53:
						orderBookingSchedule.setStatusAsString("Delivery Completed");
						break;
					case 54:
						orderBookingSchedule.setStatusAsString("Delivery Partial");
						break;
					case 55:
						orderBookingSchedule.setStatusAsString("Payment Scheduled");
						break;	
					case 56:
						orderBookingSchedule.setStatusAsString("Payment Completed");
						break;	
					case 57:
						orderBookingSchedule.setStatusAsString("Payment Partial");
						break;	
					case 60:
						orderBookingSchedule.setStatusAsString("OTP Verified (Order Creation Failed)");
						break;		
					}
				}else{
					orderBookingSchedule.setStatusAsString("Order Booking Scheduled");
				}
				orderSchedules.add(orderBookingSchedule);
			}

		}catch(Exception exception){
			logger.error("Error while fetching order schedule report", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return orderSchedules;
	}
	
	@Override
	public List<ScheduledOrderSummary> getScheduledOrderSummary(int tenantID, int salesExecID, Date visitDate){
		
		Session session = null;
		List<ScheduledOrderSummary> orderSchedules = new ArrayList<ScheduledOrderSummary>();
		try{
			String visitDateStr = new SimpleDateFormat("yyyy-MM-dd").format(visitDate);
			StringBuilder queryBuilder = new StringBuilder(
					"SELECT a.ORDER_SCHEDULE_ID, a.VISIT_DATE, a.SALES_EXEC_ID, a.FIRST_NAME, a.LAST_NAME, b. TOTAL_SCHEDULED, IFNULL(a.ORDER_COUNT, 0) 'ORDER_COUNT', IFNULL(a.LINES, 0) 'LINES', IFNULL(a.VALUE, 0) 'VALUE' "
					+ "FROM (SELECT * FROM (SELECT a.ID ORDER_SCHEDULE_ID, a.VISIT_DATE, c.ID SALES_EXEC_ID, c.FIRST_NAME, c.LAST_NAME FROM ORDER_BOOKING_SCHEDULE a, USERS c WHERE a.SALES_EXEC_ID = c.ID AND a.TENANT_ID ="+ tenantID +" ) A "
					+ "LEFT JOIN (SELECT DISTINCT ORDER_BOOKING_ID, COUNT(ID) 'ORDER_COUNT', SUM(NO_OF_LINE_ITEMS) 'LINES', SUM(BOOK_VALUE) 'VALUE' FROM ORDER_DETAILS WHERE TENANT_ID= "+ tenantID +" GROUP BY ORDER_BOOKING_ID) B ON A.ORDER_SCHEDULE_ID = B.ORDER_BOOKING_ID) a, "
					+ "(SELECT ORDER_BOOKING_SCHEDULE_ID, COUNT(CUSTOMER_ID) 'TOTAL_SCHEDULED' FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS GROUP BY ORDER_BOOKING_SCHEDULE_ID) b WHERE a.ORDER_SCHEDULE_ID=b.ORDER_BOOKING_SCHEDULE_ID AND VISIT_DATE= '"+ visitDateStr + "'");
			
			if(salesExecID != -1){
				queryBuilder.append(" AND SALES_EXEC_ID = "+ salesExecID);
			}
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(queryBuilder.toString());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				ScheduledOrderSummary scheduledOrderSummary = new ScheduledOrderSummary();
				scheduledOrderSummary.setScheduleID(Integer.parseInt(String.valueOf(objs[0])));
				scheduledOrderSummary.setVisitDateStr(new SimpleDateFormat("dd-MM-yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[1]))));
				scheduledOrderSummary.setSalesExecName(String.valueOf(objs[3]) + " "+ (String.valueOf(objs[4]) != null ? String.valueOf(objs[4]) : ""));
				scheduledOrderSummary.setNumberOfSchedules(Integer.parseInt(String.valueOf(objs[5])));
				scheduledOrderSummary.setNumberOfOrders(Integer.parseInt(String.valueOf(objs[6])));
				scheduledOrderSummary.setNumberOfOrdersPending(scheduledOrderSummary.getNumberOfSchedules() - scheduledOrderSummary.getNumberOfOrders());
				scheduledOrderSummary.setNumberOfLines(Integer.parseInt(String.valueOf(objs[7])));
				scheduledOrderSummary.setTotalBookValue(Double.parseDouble(String.valueOf(objs[8])));
				orderSchedules.add(scheduledOrderSummary);
			}
			
		}catch(Exception exception){
			logger.error("Error while fetching schedule order summary.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return orderSchedules;
	}

	@Override
	public void editOrder(Order order) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			order.setDateModified(new Date());
			logger.debug(" Order :: "+ order);
			session.update(order);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating order", e);
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
	public int createWithOTP(Order order, String otp) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			//Update OTP
			SQLQuery query = session.createSQLQuery(" SELECT ID FROM CUSTOMER_OTP WHERE CUSTOMER_ID= ? AND OTP_TYPE= ? AND GENERTAED_OTP= ? AND DATE(GENERATED_DATE_TIME)= CURDATE() AND SUBMITTED_DATE_TIME IS NULL AND SUBMITTED_OTP IS NULL");
			query.setParameter(0, order.getCustomerID());
			query.setParameter(1, 1);
			query.setParameter(2, otp);
			List results = query.list();
			if(results != null && results.size() == 1){
				for(Object obj : results){
					int otpID = Integer.valueOf(String.valueOf(obj));
					SQLQuery updateQuery = session.createSQLQuery("UPDATE CUSTOMER_OTP SET SUBMITTED_OTP=?, STATUS_ID= ?, SUBMITTED_DATE_TIME=NOW(), DATE_MODIFIED=CURDATE() WHERE ID=? AND TENANT_ID = ? ");
					updateQuery.setParameter(0, otp);
					updateQuery.setParameter(1, EntityStatusEnum.OTP_VERIFIED.getEntityStatus());
					updateQuery.setParameter(2, otpID);
					updateQuery.setParameter(3, order.getTenantID());
					updateQuery.executeUpdate();
				}
			}
			
			//Create Order
			order.setDateCreated(new Date());
			order.setCode(UUID.randomUUID().toString());
			order.setStatusID(EntityStatusEnum.ORDER_CREATED.getEntityStatus());
			logger.debug(" Order :: "+ order);
			session.save(order);
			//Set status in ORDER_BOOKING_SCHEDULE_CUSTOMERS
			SQLQuery updateStatus = session.createSQLQuery("UPDATE ORDER_BOOKING_SCHEDULE_CUSTOMERS SET STATUS_ID = "+EntityStatusEnum.ORDER_CREATED.getEntityStatus() + " WHERE ORDER_BOOKING_SCHEDULE_ID = ? AND CUSTOMER_ID = ?");
			updateStatus.setParameter(0, order.getOrderBookingID());
			updateStatus.setParameter(1, order.getCustomerID());
			updateStatus.executeUpdate();
			
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating order", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return order.getOrderID();
	}

	//Check if status is ORDER Scheduled
	public boolean isOrderBookingScheduledForCustomer(int customerID, int tenantID) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT ID FROM ORDER_BOOKING_SCHEDULE_CUSTOMERS WHERE CUSTOMER_ID = ? AND TENANT_ID = ? AND STATUS_ID ="+ EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			query.setInteger(0, customerID);
			query.setInteger(1, tenantID);
			List count = query.list();
			if(count != null && count.size() > 0 ){
				return true;
			}
		}catch(Exception e) {
			logger.error("Error while fetching customer order schedule status", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}
	
	//Check if status is ORDER Scheduled
	//Order Created Or Delivery Scheduled or Partially Delivered
	//Order Delivered Or Payment Scheduled OR Partially Paid
	public boolean isOrderingProcessInProgressForCustomer(int customerID, int tenantID) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT order_booking_schedule_id " + 
					" FROM   order_booking_schedule_customers " + 
					" WHERE  customer_id =  ? " + 
					"       AND tenant_id = ? " + 
					"       AND status_id = "+ EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus()+ 
					" UNION " + 
					" SELECT id " + 
					" FROM   order_details " + 
					" WHERE  customer_id = ? " + 
					"       AND tenant_id = ? " + 
					"       AND status_id IN ( "+ StringUtils.join(statusList, ",")+")");
			query.setInteger(0, customerID);
			query.setInteger(1, tenantID);
			query.setInteger(2, customerID);
			query.setInteger(3, tenantID);
			List count = query.list();
			if(count != null && count.size() > 0){
				return true;
			}
		}catch(Exception e) {
			logger.error("Error while fetching customer order schedule status", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}
	
	@Override
	public List<OrderBookingSchedule> searchScheduledOrders(Map<String, Object> filterCriteria, int tenantID){
		logger.debug("searchScheduledOrders Enters");
		Session session = null;
		MultiKeyMap<Object, OrderBookingSchedule> multiKeyMap=new MultiKeyMap<Object, OrderBookingSchedule>();
		try {
			session = sessionFactory.openSession();
			StringBuilder queryBuilder = new StringBuilder("SELECT * FROM (SELECT d. ID ORDER_BOOKING_ID,\n" + 
					" a.ID SALES_EXEC_ID,\n" + 
					" CONCAT (a.FIRST_NAME, \" \", a.LAST_NAME) AS SALES_EXEC_NAME,\n" + 
					" b.ID CUST_ID,\n" + 
					" b.NAME CUST_NAME,\n" + 
					" c.ID MANUF_ID,\n" + 
					" c.NAME NANUF_NAME,\n" + 
					" d.VISIT_DATE VISIT_DATE,\n" + 
					" g.ID BEAT_ID,\n" + 
					" g.NAME BEAT_NAME\n" + 
					" FROM USERS a, \n" + 
					" CUSTOMERS b, \n" + 
					" MANUFACTURERS c, \n" + 
					" ORDER_BOOKING_SCHEDULE d, \n" + 
					" ORDER_BOOKING_SCHEDULE_CUSTOMERS e, \n" + 
					" ORDER_BOOKING_SCHEDULE_MANUFACTURERS f, \n" + 
					" BEATS g \n" + 
					" WHERE a.ID = d.SALES_EXEC_ID AND\n" + 
					" d.BEAT_ID = g.ID AND\n" + 
					" d.ID = e.ORDER_BOOKING_SCHEDULE_ID AND\n" + 
					" d.ID = f.ORDER_BOOKING_SCHEDULE_ID AND\n" + 
					" b.ID = e.CUSTOMER_ID AND\n" + 
					" c.ID = f.MANUFACTURER_ID AND\n" + 
					" a.TENANT_ID = b.TENANT_ID AND\n" + 
					" b.TENANT_ID = c.TENANT_ID AND\n" + 
					" c.TENANT_ID = d.TENANT_ID AND\n" + 
					" d.TENANT_ID = e.TENANT_ID AND\n" + 
					" e.TENANT_ID = f.TENANT_ID AND\n" + 
					" f.TENANT_ID = g.TENANT_ID AND\n" + 
					" e.STATUS_ID = ? AND\n" + 
					" a.TENANT_ID = ? ) ORDER_SCHEDULE \n" + 
					"\n" + 
					"");
			if(filterCriteria != null && filterCriteria.size() > 0){
				queryBuilder.append(" WHERE ");
				int index = 0;
				for(Map.Entry<String, Object> entry : filterCriteria.entrySet()){
					if(index != 0){
						queryBuilder.append(" AND ");
					}
					String searchParam = entry.getKey();
					Object searchVal = entry.getValue();
					if(searchParam.equals("date")){
						queryBuilder.append(" VISIT_DATE ='"+new SimpleDateFormat("yyyy-MM-dd").format(searchVal)+"'");
					}else {
						queryBuilder.append(filterMap.get(searchParam)+ "=" +searchVal );
					}
					++index;
				}
			}
			
			logger.debug(" search sql "+ queryBuilder.toString());
			SQLQuery query = session.createSQLQuery(queryBuilder.toString());
			query.setInteger(0, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			query.setInteger(1, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				//OrderBookingSchedule schedule = new OrderBookingSchedule();
				int customerID = Integer.parseInt(String.valueOf(objs[3]));
				Date visitDate = new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[7]));
				int bookingScheduleID = Integer.parseInt(String.valueOf(objs[0]));
				if(!multiKeyMap.containsKey(customerID, bookingScheduleID)) {
					multiKeyMap.put(customerID, bookingScheduleID, new OrderBookingSchedule());
				}
				OrderBookingSchedule schedule = multiKeyMap.get(customerID, bookingScheduleID);
				
				schedule.setBookingScheduleID(Integer.parseInt(String.valueOf(objs[0])));
				schedule.setSalesExecutiveID(Integer.parseInt(String.valueOf(objs[1])));
				schedule.setSalesExecName(String.valueOf(objs[2]));
				schedule.setCustomerID(Integer.parseInt(String.valueOf(objs[3])));
				schedule.setCustomerName(String.valueOf(objs[4]));
				//schedule.setManufacturerID(Integer.parseInt(String.valueOf(objs[5])));
				schedule.getManufacturerNames().add(String.valueOf(objs[6]));
				schedule.setVisitDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[7])));
				schedule.setVisitDateAsString(new SimpleDateFormat("dd-MM-yyyy").format(schedule.getVisitDate()));
				schedule.setBeatID(Integer.parseInt(String.valueOf(objs[8])));
				schedule.setBeatName(String.valueOf(objs[9]));
				//schedules.add(schedule);
			}
			
		}catch(Exception e) {
			logger.error("Error while searching scheduled order visits.", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("searchScheduledOrders Exits");
		return new ArrayList(multiKeyMap.values());
	}

	@Override
	public void deleteOrder(int orderID, String remark, int tenantID) throws Exception{

		logger.debug("deleteOrder Enters");
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Status
			SQLQuery updateCustomer = session.createSQLQuery("UPDATE ORDER_DETAILS SET STATUS_ID = ? WHERE ID = ? AND TENANT_ID= ?");
			updateCustomer.setInteger(0, EntityStatusEnum.ORDER_DELETED.getEntityStatus());
			updateCustomer.setInteger(1, orderID);
			updateCustomer.setInteger(2, tenantID);
			updateCustomer.executeUpdate();
			//Deactivate Reason
			SQLQuery deactivateQry = session.createSQLQuery("INSERT INTO ORDER_REMOVAL_DETAILS (ORDER_ID, REMOVAL_REASON, REMOVAL_DATE, TENANT_ID, DATE_CREATED) VALUES (?, ?, CURDATE(), ?, CURDATE())");
			deactivateQry.setInteger(0, orderID);
			deactivateQry.setString(1, remark);
			deactivateQry.setInteger(2, tenantID);
			deactivateQry.executeUpdate();
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deleting order "+orderID+".", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("deleteOrder Exits");
	
	}
	
	
	@Override
	public List<DeliveryBookingSchedule> getAllOrderDeliveryBookedForDate(int tenantID, Date date){
		Session session = null;
		List<DeliveryBookingSchedule> delivereySchedules = new ArrayList<DeliveryBookingSchedule>();
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("\n" + 
					"SELECT a.ID ID,   " + 
					"       e.id ORDER_ID,   " + 
					"       e.ORDER_BOOKING_ID, " + 
					"       e.NO_OF_LINE_ITEMS, " + 
					"       e.BOOK_VALUE, " + 
					"       e.REMARK, " + 
					"       e.DATE_CREATED, " + 
					"       b.id   CUST_ID,   " + 
					"       b.NAME CUST_NAME,   " + 
					"       d.id   BEAT_ID,    " + 
					"       d.NAME BEAT_NAME,    " + 
					"       c.id   DELIV_EXEC_ID,    " + 
					"       concat(c.first_name, \" \", c.last_name) AS DELIV_EXEC_NAME,    " + 
					"       a.VISIT_DATE   " + 
					"FROM   DELIVERY_SCHEDULE a,    " + 
					"       customers b,    " + 
					"       USERS c,    " + 
					"       beats d,    " + 
					"       ORDER_DETAILS e    " + 
					"WHERE  a.customer_id = b.id    " + 
					"       AND a.beat_id = d.id    " + 
					"       AND a.ORDER_ID = e.ID    " + 
					"       AND a.DELIVERY_EXEC_ID = c.id    " + 
					"       AND a.tenant_id = b.tenant_id    " + 
					"       AND a.tenant_id = c.tenant_id    " + 
					"       AND a.tenant_id = ?   " + 
					"       AND a.visit_date = ?    " + 
					"       AND e.status_id = ?");
			query.setParameter(0, tenantID);
			query.setDate(1, new java.sql.Date(date.getTime()));
			query.setParameter(2, EntityStatusEnum.DELIVERY_SCHEDULED.getEntityStatus());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				DeliveryBookingSchedule deliveryBookingSchedule = new DeliveryBookingSchedule();
				deliveryBookingSchedule.setID(Integer.parseInt(String.valueOf(objs[0])));
				deliveryBookingSchedule.setOrderIDs(Arrays.asList(new Integer[]{Integer.parseInt(String.valueOf(objs[1]))}));
				//Orders
				Order order = new Order();
				order.setOrderID(Integer.parseInt(String.valueOf(objs[1])));
				order.setOrderBookingID(Integer.parseInt(String.valueOf(objs[2])));
				order.setNoOfLineItems(Integer.parseInt(String.valueOf(objs[3])));
				order.setBookValue(Double.parseDouble(String.valueOf(objs[4])));
				order.setRemark(String.valueOf(objs[5]));
				order.setDateCreated(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[6])));
				deliveryBookingSchedule.setOrders(Arrays.asList(new Order[]{order}));
				
				deliveryBookingSchedule.setCustomerID(Integer.parseInt(String.valueOf(objs[7])));
				deliveryBookingSchedule.setCustomerName(String.valueOf(objs[8]));
				deliveryBookingSchedule.setBeatID(Integer.parseInt(String.valueOf(objs[9])));
				deliveryBookingSchedule.setBeatName(String.valueOf(objs[10]));
				deliveryBookingSchedule.setDelivExecutiveID(Integer.parseInt(String.valueOf(objs[11])));
				deliveryBookingSchedule.setDelivExecutiveName(String.valueOf(objs[12]));
				deliveryBookingSchedule.setVisitDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[13])));
				delivereySchedules.add(deliveryBookingSchedule);
			}

		}catch(Exception exception){
			logger.error("Error while fetching delivery bookings for " + new SimpleDateFormat("dd-MM-yyyy").format(date) +".", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return delivereySchedules;
	}
	
	
	@Override
	public void unScheduleOrderDelivery(int deliverySchedID, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
		
			//Decrease counter
			//MANUFACTURER_DELIV_EXECUTIVES
			SQLQuery idQry = session.createSQLQuery("SELECT manufacturer_deliv_executives.id, " + 
					"       manufacturer_deliv_executives.tranx_counter " + 
					"FROM   manufacturer_deliv_executives " + 
					"       INNER JOIN (SELECT a.delivery_exec_id, " + 
					"                          b.manufacturer_id " + 
					"                   FROM   delivery_schedule a, " + 
					"                          order_booking_schedule_manufacturers b, " + 
					"                          order_details c, " + 
					"                          order_booking_schedule d " + 
					"                   WHERE  a.order_id = c.id " + 
					"                          AND c.order_booking_id = d.id " + 
					"                          AND b.order_booking_schedule_id = d.id " + 
					"                          AND a.id = ?) AS TABLE1 " + 
					"               ON manufacturer_deliv_executives.manufacturer_id = " + 
					"                  TABLE1.manufacturer_id " + 
					"                  AND manufacturer_deliv_executives.deliv_exec_id = " + 
					"                      TABLE1.delivery_exec_id ");
			idQry.setInteger(0, deliverySchedID);
			List lists = idQry.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE MANUFACTURER_DELIV_EXECUTIVES SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Decrease counter
			//DELIVERY_EXEC_BEATS
			SQLQuery idQry1 = session.createSQLQuery("SELECT a.id, " + 
					"       a.tranx_counter " + 
					"FROM   delivery_exec_beats a, " + 
					"       delivery_schedule b " + 
					"WHERE  a.deliv_exec_id = b.delivery_exec_id " + 
					"       AND a.beat_id = b.beat_id " + 
					"       AND a.tenant_id = b.tenant_id " + 
					"       AND a.tenant_id = ? " + 
					"       AND b.id = ? ");
			idQry1.setInteger(0, tenantID);
			idQry1.setInteger(1, deliverySchedID);
			List lists1 = idQry1.list();
			for(Object obj : lists1){
				Object[] objs = (Object[])obj;
				int id = Integer.valueOf(String.valueOf(objs[0]));
				int counter = Integer.valueOf(String.valueOf(objs[1]));
				if (counter > 0) {
					SQLQuery decreaseCnt = session.createSQLQuery("UPDATE DELIVERY_EXEC_BEATS SET TRANX_COUNTER = "+ (counter-1) +" WHERE ID = "+ id);
					decreaseCnt.executeUpdate();
				}
			}
			
			//Update Orders table
			SQLQuery updateOrderQry = session.createSQLQuery("UPDATE ORDER_DETAILS SET STATUS_ID = ? WHERE ID = (SELECT ORDER_ID FROM DELIVERY_SCHEDULE WHERE ID = ? AND TENANT_ID = ?)");
			updateOrderQry.setInteger(0, EntityStatusEnum.ORDER_CREATED.getEntityStatus());
			updateOrderQry.setInteger(1, deliverySchedID);
			updateOrderQry.setInteger(2, tenantID);
			updateOrderQry.executeUpdate();
			
			//Delete from DELIVERY_SCHEDULE
			SQLQuery deleteScheduledCust = session.createSQLQuery("DELETE FROM DELIVERY_SCHEDULE WHERE ID = ? AND TENANT_ID = ?");
			deleteScheduledCust.setInteger(0, deliverySchedID);
			deleteScheduledCust.setInteger(1, tenantID);
			deleteScheduledCust.executeUpdate();
			
			transaction.commit();
		} catch (Exception exception) {
			if(transaction != null){
				transaction.rollback();
			}
			logger.error("Error unscheduling order delivery.", exception);
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
	@Override
	public List<DeliveryBookingSchedule> searchScheduledOrderDeliveries(Map<String, Object> filterCriteria, int tenantID){
		logger.debug("searchScheduledOrderDeliveries Enters");
		Session session = null;
		List<DeliveryBookingSchedule> delivereySchedules = new ArrayList<DeliveryBookingSchedule>();
		try {
			session = sessionFactory.openSession();
			StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ( " + 
					"SELECT a.ID ID,   " + 
					"       e.id ORDER_ID,   " + 
					"       e.ORDER_BOOKING_ID, " + 
					"       e.NO_OF_LINE_ITEMS, " + 
					"       e.BOOK_VALUE, " + 
					"       e.REMARK, " + 
					"       e.DATE_CREATED, " + 
					"       b.id cust_id, " + 
					"       b.NAME cust_name, " + 
					"       d.id beat_id, " + 
					"       d.NAME beat_name, " + 
					"       c.id deliv_exec_id, " + 
					"       Concat(c.first_name, \" \", c.last_name) AS deliv_exec_name, " + 
					"       a.visit_date " + 
					"FROM   delivery_schedule a, " + 
					"       customers b, " + 
					"       USERS c, " + 
					"       beats d, " + 
					"       order_details e " + 
					"WHERE  a.customer_id = b.id " + 
					"AND    a.beat_id = d.id " + 
					"AND    a.order_id = e.id " + 
					"AND    a.delivery_exec_id = c.id " + 
					"AND    a.tenant_id = b.tenant_id " + 
					"AND    a.tenant_id = c.tenant_id " + 
					"AND    a.TENANT_ID = ? ) DELIVERY " + 
					"");
			if(filterCriteria != null && filterCriteria.size() > 0){
				queryBuilder.append(" WHERE ");
				int index = 0;
				for(Map.Entry<String, Object> entry : filterCriteria.entrySet()){
					if(index != 0){
						queryBuilder.append(" AND ");
					}
					String searchParam = entry.getKey();
					Object searchVal = entry.getValue();
					if(searchParam.equals("date")){
						queryBuilder.append(" VISIT_DATE ='"+new SimpleDateFormat("yyyy-MM-dd").format(searchVal)+"'");
					}else {
						queryBuilder.append(filterMap.get(searchParam)+ "=" +searchVal );
					}
					++index;
				}
			}
			
			logger.debug(" search sql "+ queryBuilder.toString());
			SQLQuery query = session.createSQLQuery(queryBuilder.toString());
			query.setInteger(0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				DeliveryBookingSchedule deliveryBookingSchedule = new DeliveryBookingSchedule();
				deliveryBookingSchedule.setID(Integer.parseInt(String.valueOf(objs[0])));
				deliveryBookingSchedule.setOrderIDs(Arrays.asList(new Integer[]{Integer.parseInt(String.valueOf(objs[1]))}));
				//Orders
				Order order = new Order();
				order.setOrderID(Integer.parseInt(String.valueOf(objs[1])));
				order.setOrderBookingID(Integer.parseInt(String.valueOf(objs[2])));
				order.setNoOfLineItems(Integer.parseInt(String.valueOf(objs[3])));
				order.setBookValue(Double.parseDouble(String.valueOf(objs[4])));
				order.setRemark(String.valueOf(objs[5]));
				order.setDateCreated(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[6])));
				deliveryBookingSchedule.setOrders(Arrays.asList(new Order[]{order}));
				
				deliveryBookingSchedule.setCustomerID(Integer.parseInt(String.valueOf(objs[7])));
				deliveryBookingSchedule.setCustomerName(String.valueOf(objs[8]));
				deliveryBookingSchedule.setBeatID(Integer.parseInt(String.valueOf(objs[9])));
				deliveryBookingSchedule.setBeatName(String.valueOf(objs[10]));
				deliveryBookingSchedule.setDelivExecutiveID(Integer.parseInt(String.valueOf(objs[11])));
				deliveryBookingSchedule.setDelivExecutiveName(String.valueOf(objs[12]));
				deliveryBookingSchedule.setVisitDate(new SimpleDateFormat("yyyy-MM-dd").parse(String.valueOf(objs[13])));
				delivereySchedules.add(deliveryBookingSchedule);
			}
			
		}catch(Exception e) {
			logger.error("Error while searching scheduled delivery visits.", e);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("searchScheduledOrderDeliveries Exits");
		return delivereySchedules;
	}
	
}
