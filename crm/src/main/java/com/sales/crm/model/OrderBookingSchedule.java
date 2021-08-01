package com.sales.crm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Entity
@Table(name = "ORDER_BOOKING_SCHEDULE")
@AttributeOverrides({
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class OrderBookingSchedule extends BusinessEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int bookingScheduleID;
	
	@Column(name = "SALES_EXEC_ID")
	private int salesExecutiveID;
	
	@Column(name = "BEAT_ID")
	private int beatID;
	
	@Column(name = "VISIT_DATE")
	private Date visitDate;
	
	@Transient
	private String salesExecName;
	
	@Transient
	private String beatName;
	
	@Transient
	private String customerName;
	
	@Transient
	private List<Integer> customerIDs;
	
	//looks odd but used for table display
	@Transient
	private int customerID;
	
	@Transient
	private int orderID;
	
	@Transient
	private String statusAsString;
	
	@Transient
	private String visitDateAsString;
	
	@Transient
	private int status;
	
	@Transient
	private int statusID;
	
	@Transient
	private List<Integer> manufacturerIDs;
	
	@Transient
	private int manufacturerID;
	
	@Transient
	private String manufacturerName;
	
	@Transient
	private Set<String> customerNames = new HashSet<String>();
	
	@Transient
	private Set<String> manufacturerNames = new HashSet<String>();
	
	@Transient
	private String customerNamesString;
	
	@Transient
	private String manufacturerNamesString;

	public int getSalesExecutiveID() {
		return salesExecutiveID;
	}

	public void setSalesExecutiveID(int salesExecutiveID) {
		this.salesExecutiveID = salesExecutiveID;
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

	public String getSalesExecName() {
		return salesExecName;
	}

	public void setSalesExecName(String salesExecName) {
		this.salesExecName = salesExecName;
	}

	public String getBeatName() {
		return beatName;
	}

	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	public int getBookingScheduleID() {
		return bookingScheduleID;
	}

	public void setBookingScheduleID(int bookingScheduleID) {
		this.bookingScheduleID = bookingScheduleID;
	}

	public int getOrderID() {
		return orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public String getStatusAsString() {
		return statusAsString;
	}

	public void setStatusAsString(String statusAsString) {
		this.statusAsString = statusAsString;
	}

	public String getVisitDateAsString() {
		return visitDateAsString;
	}

	public void setVisitDateAsString(String visitDateAsString) {
		this.visitDateAsString = visitDateAsString;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<Integer> getManufacturerIDs() {
		return manufacturerIDs;
	}

	public void setManufacturerIDs(List<Integer> manufacturerIDs) {
		this.manufacturerIDs = manufacturerIDs;
	}

	public int getManufacturerID() {
		return manufacturerID;
	}

	public void setManufacturerID(int manufacturerID) {
		this.manufacturerID = manufacturerID;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	public Set<String> getCustomerNames() {
		return customerNames;
	}

	public void setCustomerNames(Set<String> customerNames) {
		this.customerNames = customerNames;
	}

	public Set<String> getManufacturerNames() {
		return manufacturerNames;
	}

	public void setManufacturerNames(Set<String> manufacturerNames) {
		this.manufacturerNames = manufacturerNames;
	}

	public String getCustomerNamesString() {
		return StringUtils.join(getCustomerNames(), ",");
	}

	public void setCustomerNamesString(String customerNamesString) {
		this.customerNamesString = customerNamesString;
	}

	public String getManufacturerNamesString() {
		return StringUtils.join(getManufacturerNames(), ",");
	}

	public void setManufacturerNamesString(String manufacturerNamesString) {
		this.manufacturerNamesString = manufacturerNamesString;
	}

	
}
