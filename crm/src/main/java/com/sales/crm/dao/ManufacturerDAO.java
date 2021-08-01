package com.sales.crm.dao;

import java.util.List;

import com.sales.crm.model.Manufacturer;
import com.sales.crm.model.ManufacturerBeats;
import com.sales.crm.model.ManufacturerDelivExecs;
import com.sales.crm.model.ManufacturerSalesExecBeats;
import com.sales.crm.model.ManufacturerSalesExecs;

public interface ManufacturerDAO {
	
	void create(Manufacturer manufacturer) throws Exception;
	
	Manufacturer get(String manufacturerCode, int tenantID );
	
	Manufacturer getById(int manufacturerID, int tenantID );
	
	void update(Manufacturer manufacturer) throws Exception;
	
	void delete(int manufacturerID, int tenantID) throws Exception;
	
	List<Manufacturer> getTenantManufacturers(int tenantID);
	
	int getManufacturersCount(int tenantID);
	
	//List<Manufacturer> getSuppManufacturerList(int tenantID);
	
	//void assignManufacturer(int manufacturerID, List<Integer> manufacturerIDs, int tenantID) throws Exception;
	
	//void updateAssignedManufacturer(int manufacturerID, List<Integer> manufacturerIDs, int tenantID) throws Exception;
	
	//void deleteAassignedManufacturer(int manufacturerID, int tenantID) throws Exception;
	
	//public List<Manufacturer> getManufacturerSalesExecsList(int tenantID) ;
	
	public void assignSalesExecutivesToManufacturer(int tenantID, Manufacturer manufacturer) throws Exception;

	void updateAssignedSalesExecs(int manufacturerID, List<Integer> salesExecIDs, int tenantID) throws Exception;
	
	void deleteAassignedSalesExec(int manufacturerID, int tenantID) throws Exception;
	
	List<ManufacturerSalesExecBeats> getManufSalesExecBeatsList(int tenantID);
	
	ManufacturerSalesExecBeats getManufSalesExecBeat(String manufacturerCode, String salesExecCode, int tenantID);
	
	void updateAssignedDelivExecs(int manufacturerID, List<Integer> delivExecIDs, int tenantID) throws Exception;
	
	void deleteAassignedDelivExec(int manufacturerID, int tenantID) throws Exception;
	
	public List<ManufacturerDelivExecs> getManufacturerDelivExecsList(int tenantID) ;
	
	public void assignDelivExecutivesToManufacturer(int tenantID, Manufacturer manufacturer) throws Exception;
	
	public List<Manufacturer> getTenantTrimmedManufacturers(int tenantID);
	
	public void assignBeatsToManufacturer(int manufacturerID, List<Integer> beatIDs, int tenantID) throws Exception;
	
	public void updateAssignedBeatToManufacturer(int manufacturerID, List<Integer> beatIDs, int tenantID) throws Exception;
	
	public void deleteAssignedBeatManufacturerLink(int manufacturerID, int tenantID) throws Exception;
	
	public List<ManufacturerBeats> getManufacturerBeats(int tenantID);
	
	public List<ManufacturerSalesExecs> getManufacturerSalesExecs(int tenantID);
	
}
