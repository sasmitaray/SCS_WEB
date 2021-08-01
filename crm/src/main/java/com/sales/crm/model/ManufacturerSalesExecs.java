package com.sales.crm.model;

import java.util.ArrayList;
import java.util.List;

public class ManufacturerSalesExecs {
	
	private Manufacturer manufacturer;
	
	private List<SalesExecutive> salesExecs = new ArrayList<SalesExecutive>();
	
	//if any of the sales exec has active transaction
	private boolean hasActiveTransaction = false;
		
	private int tenantID;

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<SalesExecutive> getSalesExecs() {
		return salesExecs;
	}

	public void setSalesExecs(List<SalesExecutive> salesExecs) {
		this.salesExecs = salesExecs;
	}

	public boolean getHasActiveTransaction() {
		return hasActiveTransaction;
	}

	public void setHasActiveTransaction(boolean hasActiveTransaction) {
		this.hasActiveTransaction = hasActiveTransaction;
	}

	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}

	@Override
	public String toString() {
		return "ManufacturerSalesExecs [manufacturer=" + manufacturer + ", salesExecs=" + salesExecs
				+ ", hasActiveTransaction=" + hasActiveTransaction + ", tenantID=" + tenantID + "]";
	}

	
	
	

}
