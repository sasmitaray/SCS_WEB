package com.sales.crm.dao;

import java.util.List;

import com.sales.crm.model.Beat;
import com.sales.crm.model.TrimmedCustomer;

public interface BeatDAO {
	
	void create(Beat beat) throws Exception;
	
	Beat get(String beatCode, int tenantID);
	
	void update(Beat beat) throws Exception;
	
	void delete(int beatID, int tenantID) throws Exception;
	
	List<Beat> getTenantBeats(int tenantID);
	
	void assignCustomersToBeat(int beatID, List<Integer> customerIDs, int tenantID) throws Exception;
	
	void updateAssignedCustomersToBeat(int beatID, List<Integer> customerIDs, int tenantID) throws Exception;
	
	List<TrimmedCustomer> getBeatCustomers( int beatID, int tenantID);
	
	void deleteAssignedBeatCustomerLink(String beatCode, int tenantID) throws Exception;
	
	int getBeatsCount(int tenantID);
	
	List<Beat> getBeatsNotMappedToCustomer(int customerID, int tenantID);
	
	List<Beat> getBeatsNotMappedToSalesExec(int tenantID, int manufacturerID, int salesExecID);
	
	public List<String> getTenantBeatNames(int tenantID);
	
	public List<Beat> getBeatsNotMappedToManufacturer(int manufacturerID, int tenantID);
	
	public List<Beat> getBeatsNotMappedToDelivExec(int delivExecID, int tenantID);
	
}
