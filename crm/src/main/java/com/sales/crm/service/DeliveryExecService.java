package com.sales.crm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.DeliveryExecDAO;
import com.sales.crm.model.Beat;
import com.sales.crm.model.CustomerOrder;
import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.DeliveryExecutive;

@Service("deliveryService")
public class DeliveryExecService {
	
	@Autowired
	DeliveryExecDAO deliveryDAO;
	
	public List<DeliveryExecutive> getDelivExecutivesHavingBeatsAssigned(int tenantID){
		return deliveryDAO.getDelivExecutivesHavingBeatsAssigned(tenantID);
	}

	public List<DeliveryExecutive> getDeliveryExecutives(int tenantID){
		return deliveryDAO.getDeliveryExecutives(tenantID);
	}
	
	public void assignBeats(int tenantID, final int salesExecID, final List<Integer> beatIDs) throws Exception{
		deliveryDAO.assignBeats(tenantID, salesExecID, beatIDs);
	}
	
	public DeliveryExecutive getDelivExecutive(int delivExecID, int tenantID){
		return deliveryDAO.getDelivExecutive(delivExecID, tenantID);
	}
	
	public void updateAssignedBeats(int tenantID, int delivExecID, List<Integer> beatIDs) throws Exception{
		deliveryDAO.updateAssignedBeats(tenantID, delivExecID, beatIDs);
	}
	
	public void deleteBeatAssignment(int delivExecID, int tenantID) throws Exception{
		deliveryDAO.deleteBeatAssignment(delivExecID, tenantID);
	}
	
	public List<DeliveryExecutive> getDeliveryExecutivesScheduled(int tenantID){
		return deliveryDAO.getDeliveryExecutivesScheduled(tenantID);
	}
	
	public List<Beat> getAssignedBeats(int delivExecID, int tenantID){
		return deliveryDAO.getAssignedBeats(delivExecID, tenantID);
	}
	
	public List<Beat> getScheduledVisitDelivExecBeats(int delivExecID, Date visitDate, int tenantID){
		return deliveryDAO.getScheduledVisitDelivExecBeats(delivExecID, visitDate, tenantID);
	}
	
	public List<DeliveryExecutive> getScheduledVisitDelivExecs(Date visitDate, int tenantID){
		return deliveryDAO.getScheduledVisitDelivExecs(visitDate, tenantID);
	}
	
	public List<CustomerOrder> getScheduledCustomersOrdersForDelivery(int delivExecID, Date visitDate, int beatID, int tenantID){
		return deliveryDAO.getScheduledCustomersOrdersForDelivery(delivExecID, visitDate, beatID, tenantID);
	}
	
	public List<String> alreadyDeliveryBookingScheduledCustomer(DeliveryBookingSchedule deliveryBookingSchedule) throws Exception{
		return deliveryDAO.alreadyDeliveryBookingScheduledCustomer(deliveryBookingSchedule);
	}
	
	public void scheduleDeliveryBooking(DeliveryBookingSchedule deliveryBookingSchedule) throws Exception{
		deliveryDAO.scheduleDeliveryBooking(deliveryBookingSchedule);
	}
	
	public void unscheduleDeliveryBooking(List<Integer> customerIDs, Date visitDate, int tenantID) throws Exception{
		deliveryDAO.unscheduleDeliveryBooking(customerIDs, visitDate, tenantID);
	}
	
	public int getDeliveryExecutiveCount(int tenantID){
		return deliveryDAO.getDeliveryExecutiveCount(tenantID);
	}
	
	public List<DeliveryExecutive> getActiveDeliveryExecutives(int tenantID){
		return deliveryDAO.getActiveDeliveryExecutives(tenantID);
	}
	
	public List<DeliveryExecutive> getDelivExecsNotMappedToManufacturer(int tenantID, int manufacturerID){
		return deliveryDAO.getDelivExecsNotMappedToManufacturer(tenantID, manufacturerID);
	}
	
	public Map<Integer, String> getManufacturerParams(int delivExecID, int tenantID){
		return deliveryDAO.getManufacturerParams(delivExecID, tenantID);
	}
	
	public DeliveryExecutive getDelivExecutiveByCode(String delivExecCode, int tenantID) {
		return deliveryDAO.getDelivExecutiveByCode(delivExecCode, tenantID);
	}
}
