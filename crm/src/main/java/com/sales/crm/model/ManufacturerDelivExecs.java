package com.sales.crm.model;

import java.util.ArrayList;
import java.util.List;

public class ManufacturerDelivExecs {
	
	private Manufacturer manufacturer;
	
	private List<DeliveryExecutive> delivExecs = new ArrayList<DeliveryExecutive>();
	
	//if any of the delivery exec has active transaction
	private boolean hasActiveTransaction = false;
		
	private int tenantID;

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<DeliveryExecutive> getDelivExecs() {
		return delivExecs;
	}

	public void setDelivExecs(List<DeliveryExecutive> delivExecs) {
		this.delivExecs = delivExecs;
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

	

}
