package com.sales.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.BeatDAO;
import com.sales.crm.model.Beat;
import com.sales.crm.model.Customer;
import com.sales.crm.model.TrimmedCustomer;

@Service("beatService")
public class BeatService {
	
	@Autowired
	private BeatDAO beatDAO;
	
	@Autowired
	private CustomerService customerService;
	
	public Beat getBeat(String beatCode, int tenantID){
		return beatDAO.get(beatCode, tenantID);
	}
	
	public void createBeat(Beat beat) throws Exception{
		beatDAO.create(beat);
	}
	
	public void updateBeat(Beat beat) throws Exception{
		beatDAO.update(beat);
	}
	
	public void deleteBeat(int beatID, int tenantID) throws Exception{
		beatDAO.delete(beatID, tenantID);
	}
	
	public List<Beat> getTenantBeats(int tenantID){
		return beatDAO.getTenantBeats(tenantID);
	}
	
	public void assignCustomersToBeat(int beatID, List<Integer> customerIDs, int tenantID) throws Exception{
		beatDAO.assignCustomersToBeat(beatID, customerIDs, tenantID);	
	}
	
	public void updateAssignedCustomersToBeat(int beatID, List<Integer> customerIDs, int tenantID) throws Exception{
		beatDAO.updateAssignedCustomersToBeat(beatID, customerIDs, tenantID);
	}
	
	public List<TrimmedCustomer> getBeatCustomers(int beatID, int tenantID){
		return beatDAO.getBeatCustomers( beatID, tenantID);
	}
	
	public void deleteAssignedBeatCustomerLink(String beatCode, int tenantID) throws Exception{
		beatDAO.deleteAssignedBeatCustomerLink(beatCode, tenantID);
	}
	
	public int getBeatsCount(int tenantID){
		return beatDAO.getBeatsCount(tenantID);
	}
	
	public List<Beat> getBeatsNotMappedToCustomer(int customerID, int tenantID){
		return beatDAO.getBeatsNotMappedToCustomer(customerID, tenantID);
	}
	
	public List<Beat> getBeatsNotMappedToSalesExec(int tenantID, int manufacturerID, int salesExecID){
		return beatDAO.getBeatsNotMappedToSalesExec(tenantID, manufacturerID, salesExecID);
	}
	
	public List<String> getTenantBeatNames(int tenantID){
		return beatDAO.getTenantBeatNames(tenantID);
	}
	
	public List<Beat> getBeatsNotMappedToManufacturer(int customerID, int tenantID){
		return beatDAO.getBeatsNotMappedToManufacturer(customerID, tenantID);
	}
	
	public List<Beat> getBeatsNotMappedToDelivExec(int delivExecID, int tenantID){
		return beatDAO.getBeatsNotMappedToDelivExec(delivExecID, tenantID);
	}
}
