package com.sales.crm.model;

import java.util.List;

public class OrderBookingStats extends BusinessEntity{
	
	private int totalNoOfVisits;
	
	private int noOfVisitsCompleted;
	
	private int noOfVisitsPending;
	
	private List<String> allCustomersForVisit;
	
	private List<String> completedCustomers;
	
	private List<String> pendingCustomers;

	public int getTotalNoOfVisits() {
		return totalNoOfVisits;
	}

	public void setTotalNoOfVisits(int totalNoOfVisits) {
		this.totalNoOfVisits = totalNoOfVisits;
	}

	public int getNoOfVisitsCompleted() {
		return noOfVisitsCompleted;
	}

	public void setNoOfVisitsCompleted(int noOfVisitsCompleted) {
		this.noOfVisitsCompleted = noOfVisitsCompleted;
	}

	public int getNoOfVisitsPending() {
		return noOfVisitsPending;
	}

	public void setNoOfVisitsPending(int noOfVisitsPending) {
		this.noOfVisitsPending = noOfVisitsPending;
	}

	public List<String> getAllCustomersForVisit() {
		return allCustomersForVisit;
	}

	public void setAllCustomersForVisit(List<String> allCustomersForVisit) {
		this.allCustomersForVisit = allCustomersForVisit;
	}

	public List<String> getCompletedCustomers() {
		return completedCustomers;
	}

	public void setCompletedCustomers(List<String> completedCustomers) {
		this.completedCustomers = completedCustomers;
	}

	public List<String> getPendingCustomers() {
		return pendingCustomers;
	}

	public void setPendingCustomers(List<String> pendingCustomers) {
		this.pendingCustomers = pendingCustomers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((allCustomersForVisit == null) ? 0 : allCustomersForVisit.hashCode());
		result = prime * result + ((completedCustomers == null) ? 0 : completedCustomers.hashCode());
		result = prime * result + noOfVisitsCompleted;
		result = prime * result + noOfVisitsPending;
		result = prime * result + ((pendingCustomers == null) ? 0 : pendingCustomers.hashCode());
		result = prime * result + totalNoOfVisits;
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
		OrderBookingStats other = (OrderBookingStats) obj;
		if (allCustomersForVisit == null) {
			if (other.allCustomersForVisit != null)
				return false;
		} else if (!allCustomersForVisit.equals(other.allCustomersForVisit))
			return false;
		if (completedCustomers == null) {
			if (other.completedCustomers != null)
				return false;
		} else if (!completedCustomers.equals(other.completedCustomers))
			return false;
		if (noOfVisitsCompleted != other.noOfVisitsCompleted)
			return false;
		if (noOfVisitsPending != other.noOfVisitsPending)
			return false;
		if (pendingCustomers == null) {
			if (other.pendingCustomers != null)
				return false;
		} else if (!pendingCustomers.equals(other.pendingCustomers))
			return false;
		if (totalNoOfVisits != other.totalNoOfVisits)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OrderBookingStats [totalNoOfVisits=" + totalNoOfVisits + ", noOfVisitsCompleted=" + noOfVisitsCompleted
				+ ", noOfVisitsPending=" + noOfVisitsPending + ", allCustomersForVisit=" + allCustomersForVisit
				+ ", completedCustomers=" + completedCustomers + ", pendingCustomers=" + pendingCustomers
				+ ", tenantID=" + tenantID + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + "]";
	}

	
	

}
