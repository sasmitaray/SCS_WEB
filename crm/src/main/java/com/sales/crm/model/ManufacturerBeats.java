package com.sales.crm.model;

import java.util.ArrayList;
import java.util.List;

public class ManufacturerBeats {
	
	private Manufacturer manufacturer;
	
	private List<Beat> beats = new ArrayList<Beat>();
	
	//if any of the beat has active transaction
	private boolean hasActiveTransaction = false;
	
	private int tenantID;

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public List<Beat> getBeats() {
		return beats;
	}

	public void setBeats(List<Beat> beats) {
		this.beats = beats;
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
