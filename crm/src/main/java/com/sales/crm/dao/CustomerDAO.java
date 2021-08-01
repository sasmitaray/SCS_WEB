package com.sales.crm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sales.crm.model.Customer;
import com.sales.crm.model.CustomerOrder;
import com.sales.crm.model.TrimmedCustomer;

public interface CustomerDAO {
	
	void create(Customer customer) throws Exception;
	
	Customer get(String customerCode, int tenantID);
	
	void update(Customer customer) throws Exception;
	
	void delete(int customerID, int tenantID) throws Exception;
	
	List<Customer> getTenantCustomers(int tenantID);
	
	List<TrimmedCustomer> scheduledTrimmedCustomerslist(int salesExecID, Date visitDate, int tenantID) throws Exception;
	
	List<TrimmedCustomer> getTenantTrimmedCustomers(int tenantID);
	
	String getCustomerPrimaryMobileNo(int customerID, int tenantID) throws Exception;
	
	List<TrimmedCustomer> getCustomersToSchedule(int salesExecID, int beatID, Date visitDate, List<Integer> manufacturerIDs, int tenantID);
	
	List<CustomerOrder> getCustomersToScheduleDelivery(int beatID, Date visitDate, List<Integer> manufacturerIDs, int tenantID);
	
	List<TrimmedCustomer> scheduledTrimmedCustomerslistForDeliveryToday(int delivExecID, Date visitDate, int tenantID);
	
	List<TrimmedCustomer> getCustomerForOTPVerification(int userID, int otpType, int tenantID);
	
	void createCustomers(List<Customer> customers) throws Exception;
	
	List<Customer> search(int tenantID, Map<String, Object> filterCriteria)throws Exception;
	
	int getCustomersCount(int tenantID);
	
	void deactivateCustomer(int customerID, String remark, int tenantID) throws Exception;
	
	void activateCustomer(int customerID, String remark, int tenantID) throws Exception;
	
	public List<TrimmedCustomer> getCustomersNotMappedToBeat(int tenantID);
}
