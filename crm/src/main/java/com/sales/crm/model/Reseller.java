package com.sales.crm.model;

import java.util.List;

import javax.persistence.Transient;

public class Reseller extends Tenant{
	
	private static final long serialVersionUID = 0l;
	
	@Transient
	private int resellerID;
	
	@Transient
	private List<Customer> customers;
	
	@Transient
	private List<Beat> beats;
	
	@Transient
	private List<Area> areas;
	
	public List<Customer> getCustomers() {
		return customers;
	}
	public void setCustomers(List<Customer> customers) {
		this.customers = customers;
	}
	public List<Beat> getBeats() {
		return beats;
	}
	public void setBeats(List<Beat> beats) {
		this.beats = beats;
	}
	public List<Area> getAreas() {
		return areas;
	}
	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}
	
	public int getResellerID() {
		return getTenantID();
	}
	public void setResellerID(int resellerID) {
		setTenantID(resellerID);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((areas == null) ? 0 : areas.hashCode());
		result = prime * result + ((beats == null) ? 0 : beats.hashCode());
		result = prime * result + ((customers == null) ? 0 : customers.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reseller other = (Reseller) obj;
		if (areas == null) {
			if (other.areas != null)
				return false;
		} else if (!areas.equals(other.areas))
			return false;
		if (beats == null) {
			if (other.beats != null)
				return false;
		} else if (!beats.equals(other.beats))
			return false;
		if (customers == null) {
			if (other.customers != null)
				return false;
		} else if (!customers.equals(other.customers))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Reseller [customers=" + customers + ", beats=" + beats + ", areas=" + areas + "]";
	}
	

	
	
}
