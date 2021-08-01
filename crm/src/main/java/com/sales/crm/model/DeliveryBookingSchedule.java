package com.sales.crm.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class DeliveryBookingSchedule extends BusinessEntity{
	
	private int ID;
	
	private int delivExecutiveID;
	
	private int beatID;
	
	private List<Integer> customerIDs;
	
	private Date visitDate;
	
	private List<Integer> orderBookingIDs;
	
	//Not the best design :(
	private Map<Integer, List<String>> customerOrderMap;
	
	private int tenantID;
	
	private List<Integer> orderIDs;
	
	private int customerID;
	
	private String visitDateAsString;
	
	private String customerName;
	
	private String delivExecutiveName;
	
	private String beatName;
	
	private List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
	
	private List<Order> orders = new ArrayList<Order>();
	
	private List<Integer> manufacturerIDs;
	
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getDelivExecutiveID() {
		return delivExecutiveID;
	}

	public void setDelivExecutiveID(int delivExecutiveID) {
		this.delivExecutiveID = delivExecutiveID;
	}

	public int getBeatID() {
		return beatID;
	}

	public void setBeatID(int beatID) {
		this.beatID = beatID;
	}

	public List<Integer> getCustomerIDs() {
		return customerIDs;
	}

	public void setCustomerIDs(List<Integer> customerIDs) {
		this.customerIDs = customerIDs;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public List<Integer> getOrderBookingIDs() {
		return orderBookingIDs;
	}

	public void setOrderBookingIDs(List<Integer> orderBookingIDs) {
		this.orderBookingIDs = orderBookingIDs;
	}

	public Map<Integer, List<String>> getCustomerOrderMap() {
		return customerOrderMap;
	}

	public void setCustomerOrderMap(Map<Integer, List<String>> customerOrderMap) {
		this.customerOrderMap = customerOrderMap;
	}

	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}
	
	public List<Integer> getOrderIDs() {
		return orderIDs;
	}

	public void setOrderIDs(List<Integer> orderIDs) {
		this.orderIDs = orderIDs;
	}
	
	public List<CustomerOrder> getCustomerOrders() {
		return customerOrders;
	}

	public void setCustomerOrders(List<CustomerOrder> customerOrders) {
		this.customerOrders = customerOrders;
	}
	
	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	

	public String getVisitDateAsString() {
		return new SimpleDateFormat("dd-MM-yyyy").format(getVisitDate());
	}

	public void setVisitDateAsString(String visitDateAsString) {
		this.visitDateAsString = visitDateAsString;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getDelivExecutiveName() {
		return delivExecutiveName;
	}

	public void setDelivExecutiveName(String delivExecutiveName) {
		this.delivExecutiveName = delivExecutiveName;
	}

	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public List<Integer> getManufacturerIDs() {
		return manufacturerIDs;
	}

	public void setManufacturerIDs(List<Integer> manufacturerIDs) {
		this.manufacturerIDs = manufacturerIDs;
	}

	
}
