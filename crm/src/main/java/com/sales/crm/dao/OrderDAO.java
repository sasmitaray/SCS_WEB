package com.sales.crm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.Order;
import com.sales.crm.model.OrderBookingSchedule;
import com.sales.crm.model.OrderBookingStats;
import com.sales.crm.model.ScheduledOrderSummary;

public interface OrderDAO {
	
	int create(Order order) throws Exception;
	
	int scheduleOrderBooking(OrderBookingSchedule orderBookingSchedule) throws Exception;
	
	List<String> alreadyOrderBookingScheduledCustomer(OrderBookingSchedule orderBookingSchedule) throws Exception;
	
	void unScheduleOrderBooking(int orderScheduleID, int customerID, int tenantID) throws Exception;	
	
	List<Order> getOrders(int tenantID, int orderID) throws Exception;
	
	OrderBookingStats getOrderBookingStats(int salesExecID, Date date, int tenantID) throws Exception;
	
	List<OrderBookingSchedule> getAllOrderBookedForDate(int tenantID, Date date) throws Exception;
	
	List<OrderBookingSchedule> getOrdersBookingSchedules(int tenantID, int salesExecID, int beatID, Date date) throws Exception;
	
	List<OrderBookingSchedule> getOrderScheduleReport(int tenantID, int salesExecID, int beatID, int customerID, int orderScheduleID, int status, Date date) throws Exception;
	
	List<ScheduledOrderSummary> getScheduledOrderSummary(int tenantID, int salesExecID, Date visitDate);
	
	void editOrder(Order order) throws Exception;
	
	int createWithOTP(Order order, String otp) throws Exception;
	
	boolean isOrderingProcessInProgressForCustomer(int customerID, int tenantID);
	
	public List<OrderBookingSchedule> searchScheduledOrders(Map<String, Object> filterCriteria, int tenantID);

	public void deleteOrder(int orderID, String remark, int tenantID) throws Exception;
	
	public List<DeliveryBookingSchedule> getAllOrderDeliveryBookedForDate(int tenantID, Date date);
	
	public void unScheduleOrderDelivery(int deliverySchedID, int tenantID) throws Exception;
	
	public List<DeliveryBookingSchedule> searchScheduledOrderDeliveries(Map<String, Object> filterCriteria, int tenantID);
}
