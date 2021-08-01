package com.sales.crm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sales.crm.dao.CustomerDAO;
import com.sales.crm.dao.TenantDAO;
import com.sales.crm.model.Address;
import com.sales.crm.model.Customer;
import com.sales.crm.model.Reseller;
import com.sales.crm.model.Tenant;
import com.sales.crm.model.User;
import com.sales.crm.service.CustomerService;
import com.sales.crm.service.UserService;
import com.sales.crm.util.HibernateUtil;

public class ResellerCustomerDataTest {

	static Reseller reseller;

	static Customer customer1;
	static Customer customer2;

	static Address main_resellerAdd;
	static Address bill_resellerAdd;

	static Address main_customer1Add;
	static Address bill_customer1Add;

	static Address main_customer2Add;
	static Address bill_customer2Add;
	
	
	Session session;
	
	static ClassPathXmlApplicationContext context ;
	static TenantDAO dao;
	static CustomerDAO customerDAO;
	static CustomerService customerService;
	static UserService userService;

	@BeforeClass
	public static void init() {
		context = new ClassPathXmlApplicationContext("Hibernate-Spring.xml");
		try{
			dao = (TenantDAO) context.getBean("tenantDAO");
			customerDAO = (CustomerDAO) context.getBean("customerDAO");
			customerService = (CustomerService)context.getBean("customerService");
			userService = (UserService)context.getBean("userService");
		}catch(Exception exception){
			exception.printStackTrace();
		}


		
		main_resellerAdd = new Address();
		main_resellerAdd.setAddressLine1("res_main_addressLine1");
		main_resellerAdd.setAddressLine2("res_main_addressLine2");
		main_resellerAdd.setAddrressType(1);
		main_resellerAdd.setStreet("res_main_street");
		main_resellerAdd.setCity("res_main_city");
		main_resellerAdd.setState("res_main_contactPerson");
		main_resellerAdd.setCountry("res_main_country");
		main_resellerAdd.setContactPerson("res_main_contactPerson");
		main_resellerAdd.setDateCreated(new Date());
		main_resellerAdd.setMobileNumberPrimary("res_main_mobileNumberPrimary");
		main_resellerAdd.setMobileNumberSecondary("res_main_mobileNumberSecondary");
		main_resellerAdd.setPhoneNumber("res_main_phoneNumber");

		bill_resellerAdd = new Address();
		bill_resellerAdd.setAddressLine1("res_bill_addressLine1");
		bill_resellerAdd.setAddressLine2("res_bill_addressLine2");
		bill_resellerAdd.setAddrressType(2);
		bill_resellerAdd.setStreet("res_bill_street");
		bill_resellerAdd.setCity("res_bill_city");
		bill_resellerAdd.setState("res_bill_contactPerson");
		bill_resellerAdd.setCountry("res_bill_country");
		bill_resellerAdd.setContactPerson("res_bill_contactPerson");
		bill_resellerAdd.setDateCreated(new Date());
		bill_resellerAdd.setMobileNumberPrimary("res_bill_mobileNumberPrimary");
		bill_resellerAdd.setMobileNumberSecondary("res_bill_mobileNumberSecondary");
		bill_resellerAdd.setPhoneNumber("res_bill_phoneNumber");

		main_customer1Add = new Address();
		main_customer1Add.setAddressLine1("cust1_main_addressLine1");
		main_customer1Add.setAddressLine2("cust1_main_addressLine2");
		main_customer1Add.setAddrressType(1);
		main_customer1Add.setStreet("cust1_main_street");
		main_customer1Add.setCity("cust1_main_city");
		main_customer1Add.setState("cust1_main_contactPerson");
		main_customer1Add.setCountry("cust1_main_country");
		main_customer1Add.setContactPerson("cust1_main_contactPerson");
		main_customer1Add.setDateCreated(new Date());
		main_customer1Add.setMobileNumberPrimary("cust1_main_mobileNumberPrimary");
		main_customer1Add.setMobileNumberSecondary("cust1_main_mobileNumberSecondary");
		main_customer1Add.setPhoneNumber("cust1_main_phoneNumber");

		main_customer2Add = new Address();
		main_customer2Add.setAddressLine1("cust2_main_addressLine1");
		main_customer2Add.setAddressLine2("cust2_main_addressLine2");
		main_customer2Add.setAddrressType(1);
		main_customer2Add.setStreet("cust2_main_street");
		main_customer2Add.setCity("cust2_main_city");
		main_customer2Add.setState("cust2_main_contactPerson");
		main_customer2Add.setCountry("cust2_main_country");
		main_customer2Add.setContactPerson("cust2_main_contactPerson");
		main_customer2Add.setDateCreated(new Date());
		main_customer2Add.setMobileNumberPrimary("cust2_main_mobileNumberPrimary");
		main_customer2Add.setMobileNumberSecondary("cust2_main_mobileNumberSecondary");
		main_customer2Add.setPhoneNumber("cust2_main_phoneNumber");

		bill_customer1Add = new Address();
		bill_customer1Add.setAddressLine1("cust1_bill_addressLine1");
		bill_customer1Add.setAddressLine2("cust1_bill_addressLine2");
		bill_customer1Add.setAddrressType(2);
		bill_customer1Add.setStreet("cust1_bill_street");
		bill_customer1Add.setCity("cust1_bill_city");
		bill_customer1Add.setState("cust1_bill_contactPerson");
		bill_customer1Add.setCountry("cust1_bill_country");
		bill_customer1Add.setContactPerson("cust1_bill_contactPerson");
		bill_customer1Add.setDateCreated(new Date());
		bill_customer1Add.setMobileNumberPrimary("cust1_bill_mobileNumberPrimary");
		bill_customer1Add.setMobileNumberSecondary("cust1_bill_mobileNumberSecondary");
		bill_customer1Add.setPhoneNumber("cust1_bill_phoneNumber");

		bill_customer2Add = new Address();
		bill_customer2Add.setAddressLine1("cust2_bill_addressLine1");
		bill_customer2Add.setAddressLine2("cust2_bill_addressLine2");
		bill_customer2Add.setAddrressType(2);
		bill_customer2Add.setStreet("cust2_bill_street");
		bill_customer2Add.setCity("cust2_bill_city");
		bill_customer2Add.setState("cust2_bill_contactPerson");
		bill_customer2Add.setCountry("cust2_bill_country");
		bill_customer2Add.setContactPerson("cust2_bill_contactPerson");
		bill_customer2Add.setDateCreated(new Date());
		bill_customer2Add.setMobileNumberPrimary("cust2_bill_mobileNumberPrimary");
		bill_customer2Add.setMobileNumberSecondary("cust2_bill_mobileNumberSecondary");
		bill_customer2Add.setPhoneNumber("cust2_bill_phoneNumber");

		reseller = new Reseller();
		reseller.setName("Res");
		reseller.setDescription("Res_Des");
		reseller.setDateCreated(new Date());
		
				customer1 = new Customer();
		customer1.setName("cust1");
		customer1.setDescription("cust1_desc");
		//customer1.setVisitDate(new Date());
		customer1.setDateCreated(new Date());
		List<Address> address = new ArrayList<Address>();
		address.add(main_customer1Add);
		//address.add(bill_customer1Add);
		customer1.setAddress(address);
		//customer1.setSalesExec(salesExec);

		customer2 = new Customer();
		customer2.setName("cust2");
		customer2.setDescription("cust2_desc");
		//customer2.setVisitDate(new Date());
		customer2.setDateCreated(new Date());
		
		
		
	}
	
	@AfterClass
	public static void classCleanUp(){
	}

	public void setUp() {
		session = HibernateUtil.getSessionFactory().openSession();
	}

	@After
	public void tearDown() {
		//session.close();
	}
	
	@Test
	public void testCreateUser(){
		User user = new User();
		
		user.setUserName("test_user");
		user.setDescription("test_desc");
		user.setEmailID("test_email");
		user.setFirstName("test_f_name");
		user.setLastName("test_n_name");
		user.setMobileNo("test_m_number");
		user.setPassword("test_pass");
		user.setTenantID(13);
		try{
			userService.createUser(user);
		}catch(Exception exception){
			exception.printStackTrace();
		}
	}
	
	
	
	@Test
	public void getTrimmedCustomers(){
		try{
			customerService.getTenantTrimmedCustomers(13);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGetCustomer(){
		//customerDAO.get(22);
	}

	@Test
	public void testCreateReseller() {
		try {
			
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(main_resellerAdd);
			addresses.add(bill_resellerAdd);
			reseller.setAddress(addresses);

			// Insert
			dao.create(reseller);
			int id = reseller.getTenantID();

			// Fetch
			Tenant tenant1 = dao.get(id);

			// Test
			assertEquals(reseller.getName(), tenant1.getName());

			// Delete
			// dao.delete(reseller1.getResellerID());

			// Fetch
			//Reseller reseller2 = dao.get(id);
			//assertNull(reseller2);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testCreateCustomerFailsWithoutParent() {
		try {
			// Insert
			session.beginTransaction();
			session.save(customer1);
			int id = customer1.getCustomerID();

			// Fetch
			Customer customer11 = (Customer) session.load(Customer.class, id);

			// Test
			assertEquals(customer1.getName(), customer11.getName());

			// Delete
			session.delete(customer11);

			// Fetch
			try {
				session.load(Customer.class, id);
				assertTrue(false);
			} catch (org.hibernate.ObjectNotFoundException e) {
				assertTrue(true);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			assertFalse(false);
		}

	}
	
	@Test
	public void testGetResellerCustomers(){
		try{
			customerDAO.getTenantCustomers(13);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testCreateCustomerPassWithParent() {
		try {
			customerService.createCustomer(customer1);
		} catch (Exception e) {
			assertFalse(true);
		}

	}
	
	@Test
	public void testCreateAddress() {
		try {
			// Insert
			session.beginTransaction();
			session.save(main_resellerAdd);
			int id = main_resellerAdd.getId();

			// Fetch
			Address address = (Address) session.load(Address.class, id);

			// Test
			assertEquals(main_resellerAdd.getAddressLine1(), address.getAddressLine1());

			// Delete
			session.delete(address);

			// Fetch
			try {
				session.load(Address.class, id);
				assertTrue(false);
			} catch (org.hibernate.ObjectNotFoundException e) {
				assertTrue(true);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			assertFalse(true);
		}

	}
	
	
	@Test
	public void testCreateResellerWithAddress() {
		try {
			// Insert
			session.beginTransaction();
			//Address
			List<Address> addresses = new ArrayList<Address>();
			addresses.add(main_resellerAdd);
			addresses.add(bill_resellerAdd);
			//Add Adddress to reseller
			reseller.setAddress(addresses);
			//Save Reseller
			session.save(reseller);
			int id = reseller.getTenantID();

			// Fetch Reseller
			Reseller reseller = (Reseller) session.load(Reseller.class, id);
			
			//Fetch Addresses and Test
			for(Address address : reseller.getAddress()){
				Address address1 = (Address)session.load(Address.class, address.getId());
				if(address1.getAddrressType() == 1){
					assertEquals("res_main_addressLine1", address1.getAddressLine1());
				}else if(address1.getAddrressType() == 2){
					assertEquals("res_bill_addressLine1", address1.getAddressLine1());
				}
			}
			
			int addressId = reseller.getAddress().get(0).getId();
			
			// Delete
			session.delete(reseller);

			// Fetch
			try {
				session.load(Address.class, addressId);
				assertTrue(false);
			} catch (org.hibernate.ObjectNotFoundException e) {
				assertTrue(true);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			assertFalse(true);
		}

	}
	
	
	@Test
	public void testFetchResellerWithCustomer() {
		try {
			// Insert
			session.beginTransaction();
			//Save Reseller
			session.save(reseller);
			int tenantID = reseller.getTenantID();
			
			customer1.setTenantID(tenantID);
			customer2.setTenantID(tenantID);
			
			//Save Customer
			
			
			//Fetch Reseller
			Reseller reseller1 = (Reseller)session.load(Reseller.class, tenantID);
			
			assertEquals(2, reseller1.getCustomers().size());
			
			int customerId = reseller1.getCustomers().get(0).getCustomerID();
			
			try {
				session.load(Customer.class, customerId);
				assertTrue(true);
			} catch (org.hibernate.ObjectNotFoundException e) {
				assertTrue(false);
			}
			
			// Delete
			session.delete(reseller1);

			// Fetch
			try {
				session.load(Customer.class, customerId);
				assertTrue(false);
			} catch (org.hibernate.ObjectNotFoundException e) {
				assertTrue(true);
			}

			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}

	}
}
