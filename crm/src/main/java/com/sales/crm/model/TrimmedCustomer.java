package com.sales.crm.model;

public class TrimmedCustomer extends BusinessEntity{

	private int customerID;
	private String customerCode;
	private String customerName;
	
	//Order Booking ID or Order ID
	private int orderRefID;
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
	
	public int getOrderRefID() {
		return orderRefID;
	}
	public void setOrderRefID(int orderRefID) {
		this.orderRefID = orderRefID;
	}
	public String getCustomerCode() {
		return customerCode;
	}
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}
	
	
}
