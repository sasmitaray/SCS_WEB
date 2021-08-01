package com.sales.crm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Transient;

public class DeliveryExecutive extends User {
	
	@Transient
	private List<Beat> beats;
	
	@Transient
	private List<Integer> beatIDLists;
	
	@Transient
	private Date visitDate;
	
	@Transient
	private List<Integer> customerIDs;
	
	@Transient
	private boolean hasTransaction;

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

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	
	public List<Integer> getCustomerIDs() {
		return customerIDs;
	}

	public void setCustomerIDs(List<Integer> customerIDs) {
		this.customerIDs = customerIDs;
	}

	
	public boolean getHasTransaction() {
		return hasTransaction;
	}

	public void setHasTransaction(boolean hasTransaction) {
		this.hasTransaction = hasTransaction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((beatIDLists == null) ? 0 : beatIDLists.hashCode());
		result = prime * result + ((beats == null) ? 0 : beats.hashCode());
		result = prime * result + ((customerIDs == null) ? 0 : customerIDs.hashCode());
		result = prime * result + ((visitDate == null) ? 0 : visitDate.hashCode());
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
		DeliveryExecutive other = (DeliveryExecutive) obj;
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
		if (customerIDs == null) {
			if (other.customerIDs != null)
				return false;
		} else if (!customerIDs.equals(other.customerIDs))
			return false;
		if (visitDate == null) {
			if (other.visitDate != null)
				return false;
		} else if (!visitDate.equals(other.visitDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DeliveryExecutive [beats=" + beats + ", beatIDLists=" + beatIDLists + ", visitDate=" + visitDate
				+ ", customerIDs=" + customerIDs + ", tenantID=" + getTenantID() + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + "]";
	}

	
	
}
