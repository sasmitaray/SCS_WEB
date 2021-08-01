package com.sales.crm.model;

import java.util.List;

public class ManufacturerSalesExecBeats {
	
	private Manufacturer manufacturer;
	
	private SalesExecutive salesExecutive;
	
	private List<Beat> beats;
	
	private List<Integer> beatIDLists;
	
	private int tenantID;
	
	private boolean hasTransactions;

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public SalesExecutive getSalesExecutive() {
		return salesExecutive;
	}

	public void setSalesExecutive(SalesExecutive salesExecutive) {
		this.salesExecutive = salesExecutive;
	}

	public List<Beat> getBeats() {
		return beats;
	}

	public void setBeats(List<Beat> beats) {
		this.beats = beats;
	}

	public List<Integer> getBeatIDLists() {
		return beatIDLists;
	}

	public void setBeatIDLists(List<Integer> beatIDLists) {
		this.beatIDLists = beatIDLists;
	}

	
	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}
	
	

	public boolean getHasTransactions() {
		return hasTransactions;
	}

	public void setHasTransactions(boolean hasTransactions) {
		this.hasTransactions = hasTransactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beatIDLists == null) ? 0 : beatIDLists.hashCode());
		result = prime * result + ((beats == null) ? 0 : beats.hashCode());
		result = prime * result + ((manufacturer == null) ? 0 : manufacturer.hashCode());
		result = prime * result + ((salesExecutive == null) ? 0 : salesExecutive.hashCode());
		result = prime * result + tenantID;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ManufacturerSalesExecBeats other = (ManufacturerSalesExecBeats) obj;
		if (beatIDLists == null) {
			if (other.beatIDLists != null)
				return false;
		} else if (!beatIDLists.equals(other.beatIDLists))
			return false;
		if (beats == null) {
			if (other.beats != null)
				return false;
		} else if (!beats.equals(other.beats))
			return false;
		if (manufacturer == null) {
			if (other.manufacturer != null)
				return false;
		} else if (!manufacturer.equals(other.manufacturer))
			return false;
		if (salesExecutive == null) {
			if (other.salesExecutive != null)
				return false;
		} else if (!salesExecutive.equals(other.salesExecutive))
			return false;
		if (tenantID != other.tenantID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ManufacturerSalesExecBeats [manufacturer=" + manufacturer + ", salesExecutive=" + salesExecutive
				+ ", beats=" + beats + ", beatIDLists=" + beatIDLists + ", tenantID=" + tenantID + "]";
	}

	
}
