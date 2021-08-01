package com.sales.crm.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.SalesExecDAO;
import com.sales.crm.model.Beat;
import com.sales.crm.model.SalesExecutive;
import com.sales.crm.model.TrimmedCustomer;

@Service("salesExecService")
public class SalesExecService {
	
	@Autowired
	SalesExecDAO salesExecDAO;
	
	public SalesExecutive getSalesExecutive(int salesExecID, int tenantID){
		return salesExecDAO.get(salesExecID, tenantID);
	}

	public List<SalesExecutive> getSalesExecutives(int tenantID){
		return salesExecDAO.getSalesExecutives(tenantID);
	}
	
	public void assignBeats(int tenantID, int manufacturerID, int salesExecID, List<Integer> beatIDs) throws Exception{
		salesExecDAO.assignBeats(tenantID, manufacturerID, salesExecID, beatIDs);
	}

	public void updateAssignedBeats(int tenantID, int manufacturerID, int salesExecID, List<Integer> beatIDs) throws Exception{
		salesExecDAO.updateAssignedBeats(tenantID, manufacturerID, salesExecID, beatIDs);
	}
	
	public List<SalesExecutive> getSalesExecMapsBeatsCustomers(int tenantID){
		return salesExecDAO.getSalesExecMapsBeatsCustomers(tenantID);
	}
	
	public List<Beat> getAssignedBeats(int salesExecID, int tenantID){
		return salesExecDAO.getAssignedBeats(salesExecID, tenantID);
	}
	
	
	
	public List<Beat> getScheduledVisitSalesExecBeats(int salesExecID, Date visitDate, int tenantID){
		return salesExecDAO.getScheduledVisitSalesExecBeats(salesExecID, visitDate, tenantID);
	}
	
	public List<SalesExecutive> getScheduledVisitSalesExecs(Date visitDate, int tenantID){
		return salesExecDAO.getScheduledVisitSalesExecs(visitDate, tenantID);
	}
	
	public List<TrimmedCustomer> getScheduledVisitBeatCustomers(int salesExecID, Date visitDate, int beatID, int tenantID){
		return salesExecDAO.getScheduledVisitBeatCustomers(salesExecID, visitDate, beatID, tenantID);
	}
	
	public void deleteBeatAssignment(int manufacturerID, int salesExecID, int tenantID) throws Exception{
		salesExecDAO.deleteBeatAssignment(manufacturerID, salesExecID, tenantID);
	}
	
	public List<SalesExecutive> getSalesExecutivesHavingBeatsAssigned(int tenantID){
		return salesExecDAO.getSalesExecutivesHavingBeatsAssigned(tenantID);
	}
	
	public int getSalesExecutiveCount(int tenantID){
		return salesExecDAO.getSalesExecutiveCount(tenantID);
	}
	
	public List<SalesExecutive> getSalesExecsNotMappedToManufacturer(int tenantID, int manufacturerID){
		return salesExecDAO.getSalesExecsNotMappedToManufacturer(tenantID, manufacturerID);
	}
	
	public List<SalesExecutive> getActiveSalesExecutives(int tenantID){
		return salesExecDAO.getActiveSalesExecutives(tenantID);
	}
	
	public Map<Integer, String> getManufacturerParams(int salesExecID, int tenantID){
		return salesExecDAO.getManufacturerParams(salesExecID, tenantID);
	}
}
