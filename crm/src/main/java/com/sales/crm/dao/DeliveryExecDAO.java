package com.sales.crm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sales.crm.model.Beat;
import com.sales.crm.model.CustomerOrder;
import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.DeliveryExecutive;

public interface DeliveryExecDAO {
	
	List<DeliveryExecutive> getDelivExecutivesHavingBeatsAssigned(int tenantID);
	
	List<DeliveryExecutive> getDeliveryExecutives(int tenantID);
	
	void assignBeats(int tenantID, int salesExecID, List<Integer> beatIDs) throws Exception;
	
	DeliveryExecutive getDelivExecutive(int delivExecID, int tenantID);
	
	void updateAssignedBeats(int tenantID, int salesExecID,  List<Integer> beatIDs) throws Exception;
	
	void deleteBeatAssignment(int delivExecID, int tenantID) throws Exception;
	
	List<DeliveryExecutive> getDeliveryExecutivesScheduled(int tenantID);
	
	List<Beat> getAssignedBeats(int delivExecID, int tenantID);
	
	List<Beat> getScheduledVisitDelivExecBeats(int delivExecID, Date visitDate, int tenantID);
	
	List<DeliveryExecutive> getScheduledVisitDelivExecs(Date visitDate, int tenantID);
	
	List<CustomerOrder> getScheduledCustomersOrdersForDelivery(int delivExecID, Date visitDate, int beatID, int tenantID);
	
	List<String> alreadyDeliveryBookingScheduledCustomer(DeliveryBookingSchedule deliveryBookingSchedule) throws Exception;
	
	void scheduleDeliveryBooking(DeliveryBookingSchedule deliveryBookingSchedule) throws Exception;
	
	void unscheduleDeliveryBooking(List<Integer> customerIDs, Date visitDate, int tenantID) throws Exception;
	
	int getDeliveryExecutiveCount(int tenantID);
	
	public List<DeliveryExecutive> getActiveDeliveryExecutives(int tenantID);
	
	public List<DeliveryExecutive> getDelivExecsNotMappedToManufacturer(int tenantID, int manufacturerID);
	
	public Map<Integer, String> getManufacturerParams(int delivExecID, int tenantID);
	
	public DeliveryExecutive getDelivExecutiveByCode(String delivExecCode, int tenantID) ;
	
}
