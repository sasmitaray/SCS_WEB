package com.sales.crm.model;

import java.util.Date;

public class ScheduledOrderSummary extends BusinessEntity{

	private int scheduleID;
	
	private Date visitDate;
	
	private String visitDateStr;
	
	private int salesExecID;
	
	private String salesExecName;
	
	private int numberOfOrders;
	
	private int numberOfLines;
	
	private double totalBookValue;
	
	private int numberOfSchedules;
	
	private int numberOfOrdersPending;

	public int getScheduleID() {
		return scheduleID;
	}

	public void setScheduleID(int scheduleID) {
		this.scheduleID = scheduleID;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitDateStr() {
		return visitDateStr;
	}

	public void setVisitDateStr(String visitDateStr) {
		this.visitDateStr = visitDateStr;
	}

	public int getSalesExecID() {
		return salesExecID;
	}

	public void setSalesExecID(int salesExecID) {
		this.salesExecID = salesExecID;
	}

	public String getSalesExecName() {
		return salesExecName;
	}

	public void setSalesExecName(String salesExecName) {
		this.salesExecName = salesExecName;
	}

	public int getNumberOfOrders() {
		return numberOfOrders;
	}

	public void setNumberOfOrders(int numberOfOrders) {
		this.numberOfOrders = numberOfOrders;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	public double getTotalBookValue() {
		return totalBookValue;
	}

	public void setTotalBookValue(double totalBookValue) {
		this.totalBookValue = totalBookValue;
	}

	public int getNumberOfSchedules() {
		return numberOfSchedules;
	}

	public void setNumberOfSchedules(int numberOfSchedules) {
		this.numberOfSchedules = numberOfSchedules;
	}

	public int getNumberOfOrdersPending() {
		return numberOfOrdersPending;
	}

	public void setNumberOfOrdersPending(int numberOfOrdersPending) {
		this.numberOfOrdersPending = numberOfOrdersPending;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + numberOfLines;
		result = prime * result + numberOfOrders;
		result = prime * result + numberOfOrdersPending;
		result = prime * result + numberOfSchedules;
		result = prime * result + salesExecID;
		result = prime * result + ((salesExecName == null) ? 0 : salesExecName.hashCode());
		result = prime * result + scheduleID;
		long temp;
		temp = Double.doubleToLongBits(totalBookValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((visitDate == null) ? 0 : visitDate.hashCode());
		result = prime * result + ((visitDateStr == null) ? 0 : visitDateStr.hashCode());
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
		ScheduledOrderSummary other = (ScheduledOrderSummary) obj;
		if (numberOfLines != other.numberOfLines)
			return false;
		if (numberOfOrders != other.numberOfOrders)
			return false;
		if (numberOfOrdersPending != other.numberOfOrdersPending)
			return false;
		if (numberOfSchedules != other.numberOfSchedules)
			return false;
		if (salesExecID != other.salesExecID)
			return false;
		if (salesExecName == null) {
			if (other.salesExecName != null)
				return false;
		} else if (!salesExecName.equals(other.salesExecName))
			return false;
		if (scheduleID != other.scheduleID)
			return false;
		if (Double.doubleToLongBits(totalBookValue) != Double.doubleToLongBits(other.totalBookValue))
			return false;
		if (visitDate == null) {
			if (other.visitDate != null)
				return false;
		} else if (!visitDate.equals(other.visitDate))
			return false;
		if (visitDateStr == null) {
			if (other.visitDateStr != null)
				return false;
		} else if (!visitDateStr.equals(other.visitDateStr))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ScheduledOrderSummary [scheduleID=" + scheduleID + ", visitDate=" + visitDate + ", visitDateStr="
				+ visitDateStr + ", salesExecID=" + salesExecID + ", salesExecName=" + salesExecName
				+ ", numberOfOrders=" + numberOfOrders + ", numberOfLines=" + numberOfLines + ", totalBookValue="
				+ totalBookValue + ", numberOfSchedules=" + numberOfSchedules + ", numberOfOrdersPending="
				+ numberOfOrdersPending + ", tenantID=" + tenantID + ", dateCreated=" + dateCreated + ", dateModified="
				+ dateModified + "]";
	}

	
	
}
