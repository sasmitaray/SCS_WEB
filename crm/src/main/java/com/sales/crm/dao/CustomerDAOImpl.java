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

import com.sales.crm.model.Address;
import com.sales.crm.model.Beat;
import com.sales.crm.model.Customer;
import com.sales.crm.model.CustomerOrder;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Order;
import com.sales.crm.model.TrimmedCustomer;
import com.sales.crm.model.User;


@Repository("customerDAO")
public class CustomerDAOImpl implements CustomerDAO{

	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(CustomerDAOImpl.class);
	
	private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	
	static {
		statusMap.put(EntityStatusEnum.ACTIVE.getEntityStatus(), EntityStatusEnum.ACTIVE.getStatusText());
		statusMap.put(EntityStatusEnum.INACTIVE.getEntityStatus(), EntityStatusEnum.INACTIVE.getStatusText());
	}

	
	@Override
	public void create(Customer customer) throws Exception{
		logger.debug("create Enters");
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			customer.setDateCreated(new Date());
			customer.setCode(UUID.randomUUID().toString());
			customer.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
			session.save(customer);
			//Save Address
			for(Address address : customer.getAddress()){
				address.setDateCreated(new Date());
				address.setTenantID(customer.getTenantID());
				address.setCode(UUID.randomUUID().toString());
				address.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
				//Save Adddress
				session.save(address);
				//Insert into Tenant Address
				SQLQuery query = session.createSQLQuery("INSERT INTO CUSTOMER_ADDRESS (CUSTOMER_ID, ADDRESS_ID, TENANT_ID, DATE_CREATED) VALUES (?, ? , ?, CURDATE())");
				query.setInteger(0, customer.getCustomerID());
				query.setInteger(1, address.getId());
				query.setInteger(2, customer.getTenantID());
				query.executeUpdate();
			}
			//Customer-Beat Assignment
			final List<Integer> beatIDs = customer.getBeatIDs();
			final int customerID = customer.getCustomerID();
			if(beatIDs != null && beatIDs.size() > 0) {
				session.doWork(new Work() {
					@Override
					public void execute(Connection connection) throws SQLException {
						PreparedStatement pstmt = null;
						try {
							String sqlInsert = "INSERT INTO BEAT_CUSTOMER (BEAT_ID, CUSTOMER_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())";
							pstmt = connection.prepareStatement(sqlInsert);
							for (int beatID : beatIDs) {
								pstmt.setInt(0, beatID);
								pstmt.setInt(1, customerID);
								pstmt.setInt(2, customer.getTenantID());
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
		}catch(Exception e){
			logger.error("Error while creating customer", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("create Exits");
	}

	@Override
	public Customer get(String customerCode, int tenantID) {
		logger.debug("get Enters");
		Session session = null;
		Customer customer = null;
		try {
			session = sessionFactory.openSession();
			// customer = (Customer)session.get(Customer.class, customerID);
			Query query = session.createQuery("from Customer where code = :customerCode AND tenantID = :tenantID");
			query.setParameter("customerCode", customerCode);
			query.setParameter("tenantID", tenantID);
			if (query.list() != null && query.list().size() == 1) {
				customer = (Customer) query.list().get(0);

				// Get Address
				SQLQuery addressQuery = session.createSQLQuery(
						"SELECT a.* FROM Address a, CUSTOMER_ADDRESS b WHERE a.ID = b.ADDRESS_ID AND b.CUSTOMER_ID = ? AND b.TENANT_ID=?");
				addressQuery.setParameter(0, customer.getCustomerID());
				addressQuery.setParameter(1, customer.getTenantID());
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
				// Add address to customer
				customer.setAddress(addressList);

				// Get Sales exec details
				SQLQuery salesExecQuery = session.createSQLQuery(
						"SELECT a.ID, a.FIRST_NAME, a.LAST_NAME FROM USERS a, CUSTOMER_SALES_EXEC b WHERE b.SALES_EXEC_ID=a.ID AND a.TENANT_ID=b.TENANT_ID AND b.CUSTOMER_ID= ? AND a.TENANT_ID = ?");
				salesExecQuery.setParameter(0, customer.getCustomerID());
				salesExecQuery.setParameter(1, tenantID);
				List salesExecs = salesExecQuery.list();
				if (salesExecs != null && salesExecs.size() == 1) {
					Object[] objs = (Object[]) salesExecs.get(0);
					customer.setSalesExecID(Integer.valueOf(String.valueOf(objs[0])));
					customer.setSalesExecName(String.valueOf(objs[1]) + " " + String.valueOf(objs[2]));
				}
				// GET Beats
				List<Beat> beatList = new ArrayList<Beat>();
				List<Integer> beatIDs = new ArrayList<Integer>();
				SQLQuery beatsQry = session.createSQLQuery(
						"SELECT * FROM BEATS WHERE ID IN (SELECT BEAT_ID FROM BEAT_CUSTOMER WHERE CUSTOMER_ID= ? AND TENANT_ID = ?) AND TENANT_ID = ?");
				beatsQry.setParameter(0, customer.getCustomerID());
				beatsQry.setParameter(1, tenantID);
				beatsQry.setParameter(2, tenantID);
				List beats = beatsQry.list();
				for (Object obj : beats) {
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
					if(objs[8] != null){
						beat.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
					}
					if(objs[9] != null){
						beat.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
					}
					beatIDs.add(beat.getBeatID());
					beatList.add(beat);
				}
				customer.setBeatIDs(beatIDs);
				customer.setBeats(beatList);
				
				//Get Deactivation Details for deactivated customer
				if(customer.getStatusID() == EntityStatusEnum.INACTIVE.getEntityStatus()) {
					SQLQuery deactQry = session.createSQLQuery("SELECT DEACT_REASON, DEACT_DATE FROM CUSTOMER_DEACTIVATION_DETAILS WHERE CUSTOMER_ID= ? ORDER BY ID DESC LIMIT 1");
					deactQry.setInteger(0, customer.getCustomerID());
					List list = deactQry.list();
					for (Object obj : list) {
						Object[] objs = (Object[]) obj;
						customer.setDeactivationReason((String.valueOf(objs[0]) == null || String.valueOf(objs[0]).equals("")) ? "": String.valueOf(objs[0]));
						if (objs[1] != null) {
							customer.setDeactivationDateStr(new SimpleDateFormat("dd-MM-yyyy").format((Date)objs[1]));
						}
					}
				}
			}
		} catch (Exception exception) {
			logger.error("Error while fetching customer details", exception);
		} finally {
			if (session != null) {
				session.close();
			}
		}
		logger.debug("get Exits");
		return customer;
	}

	@Override
	public void update(Customer customer) throws Exception{
		logger.debug("update Enters");
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			customer.setDateModified(new Date());
			for(Address address : customer.getAddress()){
				address.setDateModified(new Date());
				session.update(address);
			}
			session.update(customer);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while updating customer", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("update Exits");
	}

	@Override
	public void delete(int customerID, int tenantID) throws Exception {
		logger.debug("delete Enters");
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			
			//Remove customer Address
			Query delAddressQry = session.createSQLQuery(
					"DELETE " + 
					"  d.* " + 
					"FROM address d " + 
					"WHERE  d.id IN (SELECT ID FROM (SELECT a.id " + 
					"                FROM   address a, " + 
					"                       customers b, " + 
					"                       customer_address c " + 
					"                WHERE  a.id = c.address_id " + 
					"                       AND b.id = c.customer_id " + 
					"                       AND b.id = ? " + 
					"                       AND a.tenant_id = ? " + 
					"                       AND a.tenant_id = b.tenant_id " + 
					"                       AND b.tenant_id = c.tenant_id) X)");
			delAddressQry.setInteger(0, customerID);
			delAddressQry.setInteger(1, tenantID);
			delAddressQry.executeUpdate();
			
			//Remove Customer
			Query query = session
					.createQuery("delete from Customer where customerID = :customerID AND tenantID = :tenantID");
			query.setParameter("customerID", customerID);
			query.setParameter("tenantID", tenantID);
			query.executeUpdate();
			
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while deleting customer.", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
		logger.debug("delete Exits");
	}

	@Override
	public List<Customer> getTenantCustomers(int tenantID) {
		logger.debug("getTenantCustomers Enters");
		Session session = null;
		List<Customer> customers = null; 
		try{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Customer where tenantID = :tenantID order by DATE_CREATED DESC");
			query.setParameter("tenantID", tenantID);
			customers = query.list();
			
			//Fetch Sales Execs
			Map<Integer, User> salesExecMap = new HashMap<Integer, User>();
			SQLQuery salesExecQry = session.createSQLQuery("SELECT d.CUSTOMER_ID, a.ID, a.USER_NAME, a.FIRST_NAME, a.LAST_NAME FROM USERS a, USER_ROLE b, TENANT_USER c, CUSTOMER_SALES_EXEC d  WHERE a.ID=b.USER_ID AND a.ID=c.USER_ID AND d.SALES_EXEC_ID=a.ID AND b.ROLE_ID= 2 AND c.TENANT_ID= ?");
			salesExecQry.setParameter(0, tenantID);
			List results = salesExecQry.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				int customerId = Integer.valueOf(String.valueOf(objs[0]));
				User user = new User();
				user.setUserID(Integer.valueOf(String.valueOf(objs[1])));
				user.setUserName(String.valueOf(objs[2]));
				user.setFirstName(String.valueOf(objs[3]));
				user.setLastName(String.valueOf(objs[4]));
				salesExecMap.put(customerId, user);
			}
			
			//Fetch Beats
			Map<Integer, List<Beat>> customerBeatMap = new HashMap<Integer, List<Beat>>();
			SQLQuery customerBeatQry = session.createSQLQuery("SELECT  a.CUSTOMER_ID, b.* FROM BEAT_CUSTOMER a, BEATS b WHERE a.BEAT_ID = b.ID AND a.TENANT_ID=b.TENANT_ID AND a.TENANT_ID= ?");
			customerBeatQry.setParameter(0, tenantID);
			List custBeatsresults = customerBeatQry.list();
			for(Object obj : custBeatsresults){
				Object[] objs = (Object[])obj;
				int customerId = Integer.valueOf(String.valueOf(objs[0]));
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
				if(!customerBeatMap.containsKey(customerId)){
					customerBeatMap.put(customerId, new ArrayList<Beat>());
				}
				customerBeatMap.get(customerId).add(beat);
			}
			
			//Set Sales Execs in customer
			for(Customer customer : customers){
				if(salesExecMap.containsKey(customer.getCustomerID())){
					customer.setSalesExecID(salesExecMap.get(customer.getCustomerID()).getUserID());
					customer.setSalesExecName(salesExecMap.get(customer.getCustomerID()).getFirstName() +" "+salesExecMap.get(customer.getCustomerID()).getLastName());
				}
				if(customerBeatMap.containsKey(customer.getCustomerID())){
					customer.setBeats(customerBeatMap.get(customer.getCustomerID()));
				}
				
				//Set Customer Status String
				customer.setStatusAsString(statusMap.get(customer.getStatusID()));
			}
		}catch(Exception exception){
			logger.error("Error while fetching customer List.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getTenantCustomers Exits");
		return customers;
	}

	@Override
	public List<TrimmedCustomer> scheduledTrimmedCustomerslist(int salesExecID, Date visitDate, int tenantID) throws Exception{
		logger.debug("scheduledTrimmedCustomerslist Enters");
		Session session = null;
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>(); 
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.ID, a.NAME, b.ID order_booking_id FROM CUSTOMERS a, ORDER_BOOKING_SCHEDULE b, ORDER_BOOKING_SCHEDULE_CUSTOMERS c  WHERE a.ID = c.CUSTOMER_ID AND b.ID = c.ORDER_BOOKING_SCHEDULE_ID AND a.TENANT_ID=b.TENANT_ID AND  c.STATUS_ID = ? AND b.SALES_EXEC_ID= ? AND b.VISIT_DATE = ? AND a.TENANT_ID = ?");
			query.setParameter( 0, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			query.setParameter( 1, salesExecID);
			query.setDate(2, new java.sql.Date(visitDate.getTime()));
			query.setParameter( 3, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				trimmedCustomer.setOrderRefID(Integer.valueOf(String.valueOf(objs[2])));
				customers.add(trimmedCustomer);
			}
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("scheduledTrimmedCustomerslist Exits");
		return customers;
	}
	
	@Override
	public List<TrimmedCustomer> getTenantTrimmedCustomers(int tenantID){
		logger.debug("getTenantTrimmedCustomers Enters");
		Session session = null;
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>(); 
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT ID, NAME FROM CUSTOMERS WHERE TENANT_ID=?");
			query.setParameter( 0, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				customers.add(trimmedCustomer);
			}
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getTenantTrimmedCustomers Exits");
		return customers;
	}
	
	@Override
	public String getCustomerPrimaryMobileNo(int customerID, int tenantID) throws Exception{
		logger.debug("getCustomerPrimaryMobileNo Enters");
		Session session = null;
		String mobileNo = "";
		try{
			session = sessionFactory.openSession();
			SQLQuery mobileNoQuery = session.createSQLQuery("SELECT a.MOBILE_NUMBER_PRIMARY FROM ADDRESS a, CUSTOMER_ADDRESS b WHERE a.ID=b.ADDRESS_ID AND a.TENANT_ID=b.TENANT_ID AND b.CUSTOMER_ID=? AND a.TENANT_ID= ? AND a.ADDRESS_TYPE=1");
			mobileNoQuery.setParameter(0, customerID);
			mobileNoQuery.setParameter(1, tenantID);
			List numbers = mobileNoQuery.list();
			if(numbers != null && numbers.size() == 1){
				mobileNo =  "+91"+String.valueOf(numbers.get(0));
			}
		}catch(Exception exception){
			logger.error("Error while getting customer mobile number.", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getCustomerPrimaryMobileNo Exits");
		return mobileNo;
	}
	
	@Override
	public List<TrimmedCustomer> getCustomersToSchedule(int salesExecID, int beatID, Date visitDate, List<Integer> manufacturerIDs, int tenantID) {
		logger.debug("getCustomersToSchedule Enters");
		Session session = null;
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>(); 
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT ID, NAME FROM CUSTOMERS WHERE ID IN    " + 
					"(SELECT     " + 
					"CUSTOMER_ID     " + 
					"FROM     " + 
					"BEAT_CUSTOMER     " + 
					"WHERE     " + 
					"BEAT_ID= ? AND    " + 
					"CUSTOMER_ID NOT IN    " + 
					"(SELECT b.CUSTOMER_ID     " + 
					"FROM     " + 
					"ORDER_BOOKING_SCHEDULE a,    " + 
					"ORDER_BOOKING_SCHEDULE_CUSTOMERS b,    " + 
					"ORDER_BOOKING_SCHEDULE_MANUFACTURERS c    " + 
					"WHERE     " + 
					"a.ID = b.ORDER_BOOKING_SCHEDULE_ID AND    " + 
					"a.ID = c.ORDER_BOOKING_SCHEDULE_ID AND    " + 
					"a.TENANT_ID = b.TENANT_ID AND    " + 
					"a.VISIT_DATE = ? AND    " + 
					"b.STATUS_ID = ? AND " + 
					"a.SALES_EXEC_ID = ? AND " + 
					"a.BEAT_ID = ? AND    " + 
					"c.MANUFACTURER_ID in (" + StringUtils.join(manufacturerIDs, ",") + ") AND    " + 
					"a.TENANT_ID = ?))");
			query.setParameter( 0, beatID);
			query.setDate(1, visitDate);
			query.setInteger(2, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			query.setInteger( 3, salesExecID);
			query.setInteger( 4, beatID);
			query.setInteger( 5, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				customers.add(trimmedCustomer);
			}
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer for beat-customer edit.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getCustomersToSchedule Exits");
		return customers;
	}
	
	@Override
	public List<CustomerOrder> getCustomersToScheduleDelivery(int beatID, Date visitDate, List<Integer> manufacturerIDs, int tenantID) {
		logger.debug("getCustomersToScheduleDelivery Enters");
		Session session = null;
		List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>(); 
		Map<TrimmedCustomer, List<Order>> custOrdersMap = new HashMap<TrimmedCustomer, List<Order>>();
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT c.id   CUST_ID, " + 
					"       c.NAME CUST_NAME, " + 
					"       b.* " + 
					"FROM   order_booking_schedule a, " + 
					"       order_details b, " + 
					"       customers c, " + 
					"       order_booking_schedule_customers d, " + 
					"       order_booking_schedule_manufacturers e " + 
					"WHERE  a.id = b.order_booking_id " + 
					"       AND a.id = d.order_booking_schedule_id " + 
					"       AND d.customer_id = c.id " + 
					"       AND b.customer_id = c.id " + 
					"       AND a.id = e.order_booking_schedule_id " + 
					"       AND a.visit_date <= ? " + 
					"       AND a.beat_id = ? " + 
					"       AND b.status_id IN ( ?, ? ) " + 
					"       AND b.tenant_id = ? " + 
					"       AND e.manufacturer_id IN ("+ StringUtils.join(manufacturerIDs, ",") +") ");
			query.setDate( 0, visitDate);
			query.setParameter(1, beatID);
			query.setParameter(2, EntityStatusEnum.ORDER_CREATED.getEntityStatus());
			query.setParameter(3, EntityStatusEnum.PARTIALLY_DELIVERED.getEntityStatus());
			query.setParameter(4, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				
				Order order = new Order();
				order.setCustomerID(trimmedCustomer.getCustomerID());
				order.setOrderID(Integer.valueOf(String.valueOf(objs[2])));
				order.setCode(String.valueOf(objs[3]));
				order.setOrderBookingID(Integer.valueOf(String.valueOf(objs[4])));
				order.setNoOfLineItems(Integer.valueOf(String.valueOf(objs[5])));
				order.setBookValue(Double.valueOf(String.valueOf(objs[6])));
				order.setRemark(String.valueOf(objs[7]));
				order.setStatusID(Integer.valueOf(String.valueOf(objs[8])));
				order.setTenantID(Integer.valueOf(String.valueOf(objs[10])));
				if(objs[11] != null){
					order.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[11])).getTime()));
				}
				
				if(!custOrdersMap.containsKey(trimmedCustomer)){
					custOrdersMap.put(trimmedCustomer, new ArrayList<Order>());
				}
				custOrdersMap.get(trimmedCustomer).add(order);
			}
			
			for(Map.Entry<TrimmedCustomer, List<Order>> entry : custOrdersMap.entrySet()){
				CustomerOrder customerOrder = new CustomerOrder();
				customerOrder.setCustomer(entry.getKey());
				customerOrder.setOrders(entry.getValue());
				customerOrders.add(customerOrder);
			}
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer for delivery schedule.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getCustomersToScheduleDelivery Exits");
		return customerOrders;
	}

	@Override
	public List<TrimmedCustomer> scheduledTrimmedCustomerslistForDeliveryToday(int delivExecID, Date visitDate, int tenantID) {
		logger.debug("getCustomersToScheduleDelivery Enters");
		Session session = null;
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>(); 
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.ID cust_id, a.NAME, c.ID order_id FROM CUSTOMERS a, DELIVERY_SCHEDULE b, ORDER_DETAILS c WHERE a.ID=b.CUSTOMER_ID AND a.TENANT_ID=b.TENANT_ID AND b.ORDER_ID = c.ID AND b.DELIVERY_EXEC_ID= ? AND b.VISIT_DATE= ? AND c.STATUS_ID = ? AND a.TENANT_ID = ?");
			query.setParameter( 0, delivExecID);
			query.setDate(1, visitDate);
			query.setParameter(2, EntityStatusEnum.DELIVERY_SCHEDULED.getEntityStatus());
			query.setParameter(3, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				trimmedCustomer.setOrderRefID(Integer.valueOf(String.valueOf(objs[2])));
				customers.add(trimmedCustomer);
			}
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer for beat-customer edit.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getCustomersToScheduleDelivery Exits");
		return customers;
	}

	@Override
	public List<TrimmedCustomer> getCustomerForOTPVerification(int userID, int otpType, int tenantID){
		logger.debug("getCustomerForOTPVerification Enters");
		Session session = null;
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>(); 
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT c.id, " + 
					"       c.NAME, " + 
					"       a.id order_booking_id " + 
					"FROM   order_booking_schedule a, " + 
					"       order_booking_schedule_customers b, " + 
					"       customers c " + 
					"WHERE  a.id = b.order_booking_schedule_id " + 
					"       AND b.customer_id = c.id " + 
					"       AND a.visit_date = Curdate() " + 
					"       AND c.id IN (SELECT customer_id " + 
					"                    FROM   customer_otp " + 
					"                    WHERE  field_exec_id = ? " + 
					"                           AND otp_type = ? " + 
					"                           AND tenant_id = ? " + 
					"                           AND status_id = ? " +
					"                           AND submitted_otp IS NULL " + 
					"                           AND Date(generated_date_time) = Curdate()) " + 
					"       AND b.status_id = ? " +
					"       AND a.SALES_EXEC_ID = ? " +
					"       AND Date(a.VISIT_DATE) = Curdate()");
			query.setInteger(0, userID);
			query.setInteger(1, otpType);
			query.setInteger(2, tenantID);
			query.setInteger(3, EntityStatusEnum.OTP_GENERATED.getEntityStatus());
			query.setInteger(4, EntityStatusEnum.ORDER_BOOKING_SCHEDULED.getEntityStatus());
			query.setInteger(5, userID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerName(String.valueOf(objs[1]));
				trimmedCustomer.setOrderRefID(Integer.valueOf(String.valueOf(objs[2])));
				customers.add(trimmedCustomer);
			}
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer for otp registration.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("getCustomerForOTPVerification Exits");
		return customers;
	}

	@Override
	public void createCustomers(List<Customer> customers) throws Exception {
		logger.debug("createCustomers Enters");
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			int beatID = -1;
			for(Customer customer : customers){
				customer.setDateCreated(new Date());
				customer.setCode(UUID.randomUUID().toString());
				customer.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
				session.save(customer);
				//Save Address
				for(Address address : customer.getAddress()){
					address.setDateCreated(new Date());
					address.setTenantID(customer.getTenantID());
					address.setCode(UUID.randomUUID().toString());
					address.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
					//Save Adddress
					session.save(address);
					//Insert into Tenant Address
					SQLQuery query = session.createSQLQuery("INSERT INTO CUSTOMER_ADDRESS (CUSTOMER_ID, ADDRESS_ID, TENANT_ID, DATE_CREATED) VALUES (?, ? , ?, CURDATE())");
					query.setInteger(0, customer.getCustomerID());
					query.setInteger(1, address.getId());
					query.setInteger(2, customer.getTenantID());
					query.executeUpdate();
				}
				//check for beat
				if(customer.getBeatName() != null && !customer.getBeatName().isEmpty()){
					SQLQuery getBeatQry = session.createSQLQuery(" SELECT ID FROM BEATS WHERE LOWER(NAME) = ? AND TENANT_ID = ?");
					getBeatQry.setParameter( 0, customer.getBeatName().toLowerCase() );
					getBeatQry.setParameter(1, customer.getTenantID());
					List results = getBeatQry.list();
					if(results.isEmpty()){
						//create beat
						Beat beat = new Beat();
						beat.setCode(UUID.randomUUID().toString());
						beat.setTenantID(customer.getTenantID());
						beat.setName(customer.getBeatName());
						beat.setDescription(customer.getBeatName());
						beat.setDateCreated(new Date());
						beat.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
						session.save(beat);
						beatID = beat.getBeatID();
					}else{
						for(Object obj : results){
							beatID = Integer.valueOf(String.valueOf(obj));
						}
					}
				}
				//Save Customer
				//session.save(customer);
				//Create customer-Beat link
				SQLQuery beatCustInsert = session.createSQLQuery("INSERT INTO BEAT_CUSTOMER (BEAT_ID, CUSTOMER_ID, TRANX_COUNTER, TENANT_ID, DATE_CREATED) VALUES (?, ?, 0, ?, CURDATE())");
				beatCustInsert.setParameter(0, beatID);
				beatCustInsert.setParameter(1, customer.getCustomerID());
				beatCustInsert.setParameter(2, customer.getTenantID());
				beatCustInsert.executeUpdate();
			}
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while bulk load of customers.", exception);
			transaction.rollback();
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("createCustomers Exits");
	}
	
	@Override
	public List<Customer> search(int tenantID, Map<String, Object> filterCriteria)throws Exception{
		logger.debug("search Enters");
		Session session = null;
		List<Customer> customers = new ArrayList<Customer>();
		try{
			session = sessionFactory.openSession();
			StringBuilder queryBuilder = new StringBuilder("SELECT * " + 
					"FROM   (SELECT a.id, " + 
					"               a.code, " + 
					"               a.NAME, " + 
					"               a.description, " + 
					"               a.status_id, " + 
					"               X.NAME BEAT_NAME, " + 
					"               b.city, " + 
					"               b.contact_person, " + 
					"               b.phone_no " + 
					"        FROM   address b, " + 
					"               customer_address c, " + 
					"               customers a " + 
					"               LEFT JOIN (SELECT e.beat_id, " + 
					"                                 e.customer_id, " + 
					"                                 d.NAME " + 
					"                          FROM   beat_customer e, " + 
					"                                 beats d " + 
					"                          WHERE  d.id = e.beat_id "+ 
					"                                 AND d.tenant_id = e.tenant_id " + 
					"                                 AND d.tenant_id = " + tenantID +" ) X " + 
					"                      ON a.id = X.customer_id " + 
					"        WHERE  c.address_id = b.id " + 
					"               AND b.address_type = 1 " + 
					"               AND a.id = c.customer_id " + 
					"               AND a.tenant_id =  " + tenantID + ") XYZ");
			if(filterCriteria != null && filterCriteria.size() > 0){
				queryBuilder.append(" WHERE ");
				int index = 0;
				for(Map.Entry<String, Object> entry : filterCriteria.entrySet()){
					if(index != 0){
						queryBuilder.append(" AND ");
					}
					String searchParam = entry.getKey();
					Object searchVal = entry.getValue();
					if(!searchParam.equals("ID")){
						queryBuilder.append(searchParam+" LIKE '%"+searchVal+"%'");
					}else {
						queryBuilder.append(searchParam+" = "+searchVal);
					}
					++index;
				}
			}
			logger.debug(" search sql "+ queryBuilder.toString());
			SQLQuery query = session.createSQLQuery(queryBuilder.toString());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				Customer customer = new Customer();
				customer.setCustomerID(Integer.parseInt(String.valueOf(objs[0])));
				customer.setCode(String.valueOf(objs[1]));
				customer.setName(String.valueOf(objs[2]) != null ? String.valueOf(objs[2]) : "");
				customer.setDescription(String.valueOf(objs[3]) != null ? String.valueOf(objs[3]) : "");
				customer.setStatusAsString(statusMap.get(Integer.parseInt(String.valueOf(objs[4]))));
				customer.setBeatName((String.valueOf(objs[5]) == null || String.valueOf(objs[5]).equals("null")) ? "" : String.valueOf(objs[5]) );
				customer.setTenantID(tenantID);
				Address mainAdd = new Address();
				mainAdd.setCity(String.valueOf(objs[6]) != null ? String.valueOf(objs[6]) : "");
				mainAdd.setContactPerson(String.valueOf(objs[7]) != null ? String.valueOf(objs[7]) : "");
				mainAdd.setPhoneNumber(String.valueOf(objs[8]) != null ? String.valueOf(objs[8]) : "");
				mainAdd.setTenantID(tenantID);
				List<Address> addressList = new ArrayList<Address>();
				addressList.add(mainAdd);
				customer.setAddress(addressList);
				customers.add(customer);
			}
		}catch(Exception exception){
			logger.error("Error while searching customers.", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("search Exits");
		return customers;
	}
	
	@Override
	public int getCustomersCount(int tenantID){
		logger.debug("getCustomersCount Enters");
		Session session = null;
		int counts = 0;
		try{
			session = sessionFactory.openSession();
			SQLQuery count = session.createSQLQuery("SELECT COUNT(*) FROM CUSTOMERS WHERE TENANT_ID= ?");
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
		logger.debug("getCustomersCount Exits");
		return counts;
	}

	@Override
	public void deactivateCustomer(int customerID, String remark, int tenantID) throws Exception{
		logger.debug("deactivateCustomer Enters");
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Status
			SQLQuery updateCustomer = session.createSQLQuery("UPDATE CUSTOMERS SET STATUS_ID = ? WHERE ID = ? AND TENANT_ID= ?");
			updateCustomer.setInteger(0, EntityStatusEnum.INACTIVE.getEntityStatus());
			updateCustomer.setInteger(1, customerID);
			updateCustomer.setInteger(2, tenantID);
			updateCustomer.executeUpdate();
			//Deactivate Reason
			SQLQuery deactivateQry = session.createSQLQuery("INSERT INTO CUSTOMER_DEACTIVATION_DETAILS (CUSTOMER_ID, DEACT_REASON,DEACT_DATE,TENANT_ID,DATE_CREATED) VALUES (?, ?, CURDATE(), ?, CURDATE())");
			deactivateQry.setInteger(0, customerID);
			deactivateQry.setString(1, remark);
			deactivateQry.setInteger(2, tenantID);
			deactivateQry.executeUpdate();
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deactivating customer "+customerID+".", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("deactivateCustomer Exits");
	}
	
	@Override
	public void activateCustomer(int customerID, String remark, int tenantID) throws Exception{
		logger.debug("activateCustomer Enters");
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Status
			SQLQuery updateCustomer = session.createSQLQuery("UPDATE CUSTOMERS SET STATUS_ID = ? WHERE ID = ? AND TENANT_ID= ?");
			updateCustomer.setInteger(0, EntityStatusEnum.ACTIVE.getEntityStatus());
			updateCustomer.setInteger(1, customerID);
			updateCustomer.setInteger(2, tenantID);
			updateCustomer.executeUpdate();
			//activate Reason
			SQLQuery deactivateQry = session.createSQLQuery("UPDATE CUSTOMER_DEACTIVATION_DETAILS SET ACTIVATION_REASON = ?, ACTIVATION_DATE =CURDATE(), DATE_MODIFIED=CURDATE() WHERE CUSTOMER_ID = ? AND TENANT_ID = ? AND ACTIVATION_DATE IS NULL");
			deactivateQry.setString(0, remark);
			deactivateQry.setInteger(1, customerID);
			deactivateQry.setInteger(2, tenantID);
			deactivateQry.executeUpdate();
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while activating customer "+customerID+".", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		logger.debug("activateCustomer Exits");
	}
	
	@Override
	public List<TrimmedCustomer> getCustomersNotMappedToBeat(int tenantID){
		logger.debug("getCustomersNotMappedToBeat Enters");
		Session session = null;
		List<TrimmedCustomer> trimmedCustomers = new ArrayList<TrimmedCustomer>();
		try {
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT id, " + 
					"       code, " + 
					"       NAME " + 
					"FROM   customers " + 
					"WHERE  id NOT IN (SELECT customer_id " + 
					"                  FROM   beat_customer " + 
					"                  WHERE  tenant_id = ?) " + 
					"       AND tenant_id = ?; ");
			query.setInteger(0, tenantID);
			query.setInteger(1, tenantID);
			List customerList = query.list();
			for (Object obj : customerList) {
				Object[] objs = (Object[]) obj;
				TrimmedCustomer trimmedCustomer = new TrimmedCustomer();
				trimmedCustomer.setCustomerID(Integer.valueOf(String.valueOf(objs[0])));
				trimmedCustomer.setCustomerCode(String.valueOf(objs[1]));
				trimmedCustomer.setCustomerName(String.valueOf(objs[2]));
				trimmedCustomers.add(trimmedCustomer);
			}
		}catch(Exception exception) {
			logger.error("Error wghile fetching customer not mapped to beat.");
		}finally {
			if(session != null){
				session.close();
			}
		}
		logger.debug("getCustomersNotMappedToBeat Exits");
		return trimmedCustomers;
	}
}
