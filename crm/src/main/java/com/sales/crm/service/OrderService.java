package com.sales.crm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.OrderDAO;
import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.Order;
import com.sales.crm.model.OrderBookingSchedule;
import com.sales.crm.model.OrderBookingStats;
import com.sales.crm.model.ScheduledOrderSummary;

@Service("orderService")
public class OrderService {
	
	@Autowired
	private OrderDAO orderDAO;
	
	public int create(Order order) throws Exception{
		return orderDAO.create(order);
	}
	
	public int createWithOTP(Order order, String otp) throws Exception{
		return orderDAO.createWithOTP(order, otp);
	}
	
	
	public int scheduleOrderBooking(OrderBookingSchedule orderBookingSchedule) throws Exception{
		if(orderBookingSchedule.getVisitDate() == null){
			orderBookingSchedule.setVisitDate(new Date());
		}
		return orderDAO.scheduleOrderBooking( orderBookingSchedule);
	}
	
	public List<String> alreadyOrderBookingScheduledCustomer(OrderBookingSchedule orderBookingSchedule) throws Exception{
		if(orderBookingSchedule.getVisitDate() == null){
			orderBookingSchedule.setVisitDate(new Date());
		}
		return orderDAO.alreadyOrderBookingScheduledCustomer( orderBookingSchedule);
	}
	
	public void unScheduleOrderBooking(int orderScheduleID, int customerID, int tenantID) throws Exception{
		orderDAO.unScheduleOrderBooking(orderScheduleID, customerID, tenantID);
	}
	
	public List<Order> getOrders(int tenantID, int orderID) throws Exception{
		return orderDAO.getOrders(tenantID, orderID);
	}
	
	public OrderBookingStats getOrderBookingStats(int salesExecID, Date date, int tenantID) throws Exception{
		return orderDAO.getOrderBookingStats(salesExecID, date, tenantID);
	}
	
	public List<OrderBookingSchedule> getAllOrderBookedForToday(int tenantID) throws Exception{
		Map<String, Object> filterCriteria = new HashMap<String, Object>();
		filterCriteria.put("date", new Date());
		return orderDAO.searchScheduledOrders(filterCriteria, tenantID);
	}
	
	public List<OrderBookingSchedule> getOrdersBookingSchedules(int tenantID, int salesExecID, int beatID, Date date) throws Exception{
		return orderDAO.getOrdersBookingSchedules(tenantID, salesExecID, beatID, date);
	}
	
	public List<OrderBookingSchedule> getOrderScheduleReport(int tenantID, int salesExecID, int beatID, int customerID, int orderScheduleID, int status, Date date) throws Exception{
		return orderDAO.getOrderScheduleReport(tenantID, salesExecID, beatID, customerID, orderScheduleID, status, date);
	}
	
	public List<ScheduledOrderSummary> getScheduledOrderSummary(int tenantID, int salesExecID, Date visitDate){
		return orderDAO.getScheduledOrderSummary(tenantID, salesExecID, visitDate);
	}
	
	public List<Order> getOrderDetails(int tenantID, int orderID) throws Exception{
		return orderDAO.getOrders(tenantID, orderID);
	}
	
	public void editOrder(Order order) throws Exception{
		orderDAO.editOrder(order);
	}
	
	public boolean isOrderingProcessInProgressForCustomer(int customerID, int tenantID) {
		return orderDAO.isOrderingProcessInProgressForCustomer(customerID, tenantID);
	}
	
	public List<OrderBookingSchedule> searchScheduledOrders(Map<String, Object> filterCriteria, int tenantID){
		return orderDAO.searchScheduledOrders(filterCriteria, tenantID);
	}
	
	public void deleteOrder(int orderID, String remark, int tenantID) throws Exception{
		orderDAO.deleteOrder(orderID, remark, tenantID);
	}

	public List<DeliveryBookingSchedule> getAllOrderDeliveryBookedForDate(int tenantID, Date date){
		return orderDAO.getAllOrderDeliveryBookedForDate(tenantID, date);
	}
	
	public List<DeliveryBookingSchedule> getAllOrderDeliveryBookedForToday(int tenantID){
		return orderDAO.getAllOrderDeliveryBookedForDate(tenantID, new Date());
	}
	
	public void unScheduleOrderDelivery(int deliverySchedID, int tenantID) throws Exception{
		orderDAO.unScheduleOrderDelivery(deliverySchedID, tenantID);
	}
	
	public List<DeliveryBookingSchedule> searchScheduledOrderDeliveries(Map<String, Object> filterCriteria, int tenantID){
		return orderDAO.searchScheduledOrderDeliveries(filterCriteria, tenantID);
	}
}
