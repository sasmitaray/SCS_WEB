package com.sales.crm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sales.crm.model.Beat;
import com.sales.crm.model.SalesExecutive;
import com.sales.crm.model.TrimmedCustomer;

public interface SalesExecDAO {
	
	SalesExecutive get(int salesExecID, int tenantID);
	
	List<SalesExecutive> getSalesExecutives(int tenantID);
	
	void assignBeats(int tenantID, int manufacturerID, int salesExecID, List<Integer> beatIDs) throws Exception;
	
	void updateAssignedBeats(int tenantID, int manufacturerID, int salesExecID, List<Integer> beatIDs) throws Exception;
	
	List<SalesExecutive> getSalesExecMapsBeatsCustomers(int tenantID);
	
	List<Beat> getAssignedBeats(int salesExecID, int tenantID);
	
	List<Beat> getScheduledVisitSalesExecBeats(int salesExecID, Date visitDate, int tenantID);
	
	List<SalesExecutive> getScheduledVisitSalesExecs(Date visitDate, int tenantID);
	
	List<TrimmedCustomer> getScheduledVisitBeatCustomers(int salesExecID, Date visitDate, int beatID, int tenantID);
	
	void deleteBeatAssignment(int manufacturerID, int salesExecID, int tenantID) throws Exception;
	
	List<SalesExecutive> getSalesExecutivesHavingBeatsAssigned(int tenantID);
	
	int getSalesExecutiveCount(int tenantID);
	
	List<SalesExecutive> getSalesExecsNotMappedToManufacturer(int tenantID, int manufacturerID);
	
	public List<SalesExecutive> getActiveSalesExecutives(int tenantID);
	
	public Map<Integer, String> getManufacturerParams(int salesExecID, int tenantID);
}
