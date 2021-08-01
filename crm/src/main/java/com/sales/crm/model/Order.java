package com.sales.crm.model;

import java.text.SimpleDateFormat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ORDER_DETAILS")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class Order extends BusinessEntity{
	
	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int orderID;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "ORDER_BOOKING_ID")
	private int orderBookingID;
	
	@Column(name = "NO_OF_LINE_ITEMS")
	private int noOfLineItems;
	
	@Column(name = "BOOK_VALUE")
	private double bookValue;
	
	@Column(name = "REMARK")
	private String remark;
	
	@Column(name = "CUSTOMER_ID")
	private int customerID;
	
	@Transient
	private String dateCreatedString;
	
	@Transient
	private String customerName;
	
	@Transient
	private String statusAsString;
	
	@Transient
	private String removalReason;
	
	@Transient
	private String salesExecName;

	public String getDateCreatedString() {
		if(dateCreatedString == null && dateCreated != null ){
			dateCreatedString = new SimpleDateFormat("dd-MM-yyyy").format(dateCreated);
		}
		return dateCreatedString;
	}

	public void setDateCreatedString(String dateCreatedString) {
		this.dateCreatedString = dateCreatedString;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public int getOrderBookingID() {
		return orderBookingID;
	}

	public void setOrderBookingID(int orderBookingID) {
		this.orderBookingID = orderBookingID;
	}

	public int getNoOfLineItems() {
		return noOfLineItems;
	}

	public void setNoOfLineItems(int noOfLineItems) {
		this.noOfLineItems = noOfLineItems;
	}

	public double getBookValue() {
		return bookValue;
	}

	public void setBookValue(double bookValue) {
		this.bookValue = bookValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getStatusAsString() {
		return statusAsString;
	}

	public void setStatusAsString(String statusAsString) {
		this.statusAsString = statusAsString;
	}

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getRemovalReason() {
		return removalReason;
	}

	public void setRemovalReason(String removalReason) {
		this.removalReason = removalReason;
	}

	public String getSalesExecName() {
		return salesExecName;
	}

	public void setSalesExecName(String salesExecName) {
		this.salesExecName = salesExecName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(bookValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + customerID;
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + ((dateCreatedString == null) ? 0 : dateCreatedString.hashCode());
		result = prime * result + noOfLineItems;
		result = prime * result + orderBookingID;
		result = prime * result + orderID;
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((statusAsString == null) ? 0 : statusAsString.hashCode());
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
		Order other = (Order) obj;
		if (Double.doubleToLongBits(bookValue) != Double.doubleToLongBits(other.bookValue))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (customerID != other.customerID)
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (dateCreatedString == null) {
			if (other.dateCreatedString != null)
				return false;
		} else if (!dateCreatedString.equals(other.dateCreatedString))
			return false;
		if (noOfLineItems != other.noOfLineItems)
			return false;
		if (orderBookingID != other.orderBookingID)
			return false;
		if (orderID != other.orderID)
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (statusAsString == null) {
			if (other.statusAsString != null)
				return false;
		} else if (!statusAsString.equals(other.statusAsString))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Order [orderID=" + orderID + ", code=" + code + ", orderBookingID=" + orderBookingID
				+ ", noOfLineItems=" + noOfLineItems + ", bookValue=" + bookValue + ", remark=" + remark
				+ ", customerID=" + customerID + ", dateCreatedString=" + dateCreatedString + ", customerName="
				+ customerName + ", statusAsString=" + statusAsString + ", tenantID=" + tenantID + ", dateCreated="
				+ dateCreated + ", dateModified=" + dateModified + ", statusID=" + statusID + "]";
	}

	
}
