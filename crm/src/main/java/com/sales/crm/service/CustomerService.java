package com.sales.crm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.CustomerDAO;
import com.sales.crm.model.Customer;
import com.sales.crm.model.CustomerOrder;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.TrimmedCustomer;

@Service("customerService")
public class CustomerService {
	
	@Autowired
	private CustomerDAO customerDAO;
	
	@Autowired
	private OrderService orderService;
	
	public Customer getCustomer(String customerCode, int tenantID){
		Customer customer = customerDAO.get(customerCode, tenantID);
		customer.setOrderingProcessInProgress(orderService.isOrderingProcessInProgressForCustomer(customer.getCustomerID(), tenantID));
		return customer;
	}
	
	public void createCustomer(Customer customer) throws Exception{
		customerDAO.create(customer);
	}
	
	public void updateCustomer(Customer customer) throws Exception{
		customerDAO.update(customer);
	}
	
	public void deleteCustomer(int customerID, int tenantID) throws Exception{
		customerDAO.delete(customerID, tenantID);
	}
	
	public List<Customer> getTenantCustomers(int tenantID){
		return customerDAO.getTenantCustomers(tenantID);
	}
	
	public List<TrimmedCustomer> scheduledTrimmedCustomerslist(int salesExecID, Date visitDate, int tenantID) throws Exception{
		return customerDAO.scheduledTrimmedCustomerslist(salesExecID, visitDate, tenantID);
	}
	
	public List<TrimmedCustomer> getTenantTrimmedCustomers(int tenantID) {
		return customerDAO.getTenantTrimmedCustomers(tenantID);
	}
	
	public String getCustomerPrimaryMobileNo(int customerID, int tenantID) throws Exception{
		return customerDAO.getCustomerPrimaryMobileNo(customerID, tenantID);
	}
	
	public List<TrimmedCustomer> getCustomersToSchedule(int salesExecID, int beatID, Date visitDate, List<Integer> manufacturerIDs,int tenantID){
		return customerDAO.getCustomersToSchedule(salesExecID, beatID, visitDate, manufacturerIDs, tenantID);
	}
	
	public List<CustomerOrder> getCustomersToScheduleDelivery(int beatID, Date visitDate, List<Integer> manufacturerIDs, int tenantID){
		return customerDAO.getCustomersToScheduleDelivery(beatID, visitDate,manufacturerIDs, tenantID);
	}
	
	public List<TrimmedCustomer> scheduledTrimmedCustomerslistForDeliveryToday(int delivExecID, Date visitDate, int tenantID){
		return customerDAO.scheduledTrimmedCustomerslistForDeliveryToday(delivExecID, visitDate, tenantID);
	}
	
	public List<TrimmedCustomer> getCustomerForOTPVerification(int userID, int otpType, int tenantID){
		return customerDAO.getCustomerForOTPVerification(userID, otpType, tenantID);
	}
	
	public void createCustomers(List<Customer> customers) throws Exception{
		customerDAO.createCustomers(customers);
	}
	
	public List<Customer> search(int tenantID, Map<String, Object> filterCriteria)throws Exception{
		return customerDAO.search(tenantID, filterCriteria);
	}
	
	public int getCustomersCount(int tenantID){
		return customerDAO.getCustomersCount(tenantID);
	}
	
	public void deactivateCustomer(int customerID, String remark, int tenantID) throws Exception{
		customerDAO.deactivateCustomer(customerID, remark, tenantID);
	}
	
	public void activateCustomer(int customerID, String remark, int tenantID) throws Exception{
		customerDAO.activateCustomer(customerID, remark, tenantID);
	}
	
	public List<TrimmedCustomer> getCustomersNotMappedToBeat(int tenantID){
		return customerDAO.getCustomersNotMappedToBeat(tenantID);
	}
	
}
