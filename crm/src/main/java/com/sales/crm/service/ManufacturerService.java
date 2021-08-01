package com.sales.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.ManufacturerDAO;
import com.sales.crm.model.Manufacturer;
import com.sales.crm.model.ManufacturerBeats;
import com.sales.crm.model.ManufacturerDelivExecs;
import com.sales.crm.model.ManufacturerSalesExecBeats;
import com.sales.crm.model.ManufacturerSalesExecs;

@Service("manufacturerService")
public class ManufacturerService {
	
	@Autowired
	private ManufacturerDAO manufacturerDAO;
	
	public Manufacturer getManufacturer(String manufacturerCode, int tenantID){
		return manufacturerDAO.get(manufacturerCode, tenantID);
	}
	
	public Manufacturer getManufacturerById(int manufacturerID, int tenantID){
		return manufacturerDAO.getById(manufacturerID, tenantID);
	}
	
	public void createManufacturer(Manufacturer manufacturer) throws Exception{
		manufacturerDAO.create(manufacturer);
	}
	
	public void updateManufacturer(Manufacturer manufacturer) throws Exception{
		manufacturerDAO.update(manufacturer);
	}
	
	public void deleteManufacturer(int manufacturerID, int tenantID) throws Exception{
		manufacturerDAO.delete(manufacturerID, tenantID);
	}
	
	public List<Manufacturer> getTenantManufacturers(int tenantID){
		return manufacturerDAO.getTenantManufacturers(tenantID);
	}
	
	public int getManufacturersCount(int tenantID){
		return manufacturerDAO.getManufacturersCount(tenantID);
	}
	
	//public List<Manufacturer> getManufacturerSalesExecsList(int tenantID){
	//	return manufacturerDAO.getManufacturerSalesExecsList(tenantID);
	//}
	
	public void assignSalesExecutivesToManufacturer(int tenantID, Manufacturer manufacturer) throws Exception{
		manufacturerDAO.assignSalesExecutivesToManufacturer(tenantID, manufacturer);
	}
	
	public void updateAssignedSalesExecs(int manufacturerID, List<Integer> salesExecIDs, int tenantID) throws Exception{
		manufacturerDAO.updateAssignedSalesExecs(manufacturerID, salesExecIDs, tenantID);
	}
	
	public void deleteAassignedSalesExec(String  manufacturerCode, int tenantID) throws Exception{
		manufacturerDAO.deleteAassignedSalesExec(manufacturerDAO.get(manufacturerCode, tenantID).getManufacturerID(), tenantID);
	}
	
	public List<ManufacturerSalesExecBeats> getManufSalesExecBeatsList(int tenantID){
		return manufacturerDAO.getManufSalesExecBeatsList(tenantID);
	}
	
	public ManufacturerSalesExecBeats getManufSalesExecBeat(String manufacturerCode, String salesExecCode, int tenantID){
		return manufacturerDAO.getManufSalesExecBeat(manufacturerCode, salesExecCode, tenantID);
	}
	
	public void updateAssignedDelivExecs(int manufacturerID, List<Integer> delivExecIDs, int tenantID) throws Exception{
		manufacturerDAO.updateAssignedDelivExecs(manufacturerID, delivExecIDs, tenantID);
	}
	
	public void deleteAassignedDelivExec(String  manufacturerCode, int tenantID) throws Exception{
		manufacturerDAO.deleteAassignedDelivExec(manufacturerDAO.get(manufacturerCode, tenantID).getManufacturerID(), tenantID);
	}
	
	public List<ManufacturerDelivExecs> getManufacturerDelivExecsList(int tenantID){
		return manufacturerDAO.getManufacturerDelivExecsList(tenantID);
	}
	
	public void assignDelivExecutivesToManufacturer(int tenantID, Manufacturer manufacturer) throws Exception{
		manufacturerDAO.assignDelivExecutivesToManufacturer(tenantID, manufacturer);
	}
	
	public List<Manufacturer> getTenantTrimmedManufacturers(int tenantID){
		return manufacturerDAO.getTenantTrimmedManufacturers(tenantID);
	}
	
	public void assignBeatsToManufacturer(int manufacturerID, List<Integer> beatIDs, int tenantID) throws Exception{
		manufacturerDAO.assignBeatsToManufacturer(manufacturerID, beatIDs, tenantID);
	}
	
	public void updateAssignedBeatToManufacturer(int manufacturerID, List<Integer> beatIDs, int tenantID) throws Exception{
		manufacturerDAO.updateAssignedBeatToManufacturer(manufacturerID, beatIDs, tenantID);
	}
	
	public void deleteAssignedBeatManufacturerLink( String manufacturerCode, int tenantID) throws Exception{
		manufacturerDAO.deleteAssignedBeatManufacturerLink( getManufacturer(manufacturerCode, tenantID).getManufacturerID(), tenantID);
	}
	
	public List<ManufacturerBeats> getManufacturerBeats(int tenantID){
		return manufacturerDAO.getManufacturerBeats(tenantID);
	}
	
	public List<ManufacturerSalesExecs> getManufacturerSalesExecs(int tenantID){
		return manufacturerDAO.getManufacturerSalesExecs(tenantID);
	}
}
