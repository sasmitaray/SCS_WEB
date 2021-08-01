package com.sales.crm.model;

import java.util.List;

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
@Table(name = "BEATS")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class Beat extends BusinessEntity {

	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int beatID;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "COVERAGE_SCHEDULE")
	private String coverageSchedule;
	
	@Column(name = "DISTANCE")
	private int distance;

	@Column(name = "TRANX_COUNTER")
	private int transCounter;
	
	@Transient
	private List<Area> areas;
	
	@Transient
	private List<TrimmedCustomer> customers;
	
	@Transient
	private List<Integer> customerIDs;
	
	@Transient
	private List<Integer> areaIDs;
	
	@Transient
	private int manufacturerID;
	
	@Transient
	private List<Manufacturer> manufacturers;
	
	//HACK FOR UI, PLEASE DON'T USE
	@Transient
	private String manufacturerIDStr;
	
	@Transient
	private boolean hasTransaction;
	
	@Transient
	private boolean hasBeatCustomersTransaction;
	
	public int getBeatID() {
		return beatID;
	}

	public void setBeatID(int beatID) {
		this.beatID = beatID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getCoverageSchedule() {
		return coverageSchedule;
	}

	public void setCoverageSchedule(String coverageSchedule) {
		this.coverageSchedule = coverageSchedule;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	
	public List<Area> getAreas() {
		return areas;
	}

	public void setAreas(List<Area> areas) {
		this.areas = areas;
	}

	public List<TrimmedCustomer> getCustomers() {
		return customers;
	}

	public void setCustomers(List<TrimmedCustomer> customers) {
		this.customers = customers;
	}

	public List<Integer> getCustomerIDs() {
		return customerIDs;
	}

	public void setCustomerIDs(List<Integer> customerIDs) {
		this.customerIDs = customerIDs;
	}

	public List<Integer> getAreaIDs() {
		return areaIDs;
	}

	public void setAreaIDs(List<Integer> areaIDs) {
		this.areaIDs = areaIDs;
	}
	
	public int getManufacturerID() {
		return manufacturerID;
	}

	public void setManufacturerID(int manufacturerID) {
		this.manufacturerID = manufacturerID;
	}

	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(List<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}

	public String getManufacturerIDStr() {
		return manufacturerIDStr;
	}

	public void setManufacturerIDStr(String manufacturerIDStr) {
		this.manufacturerIDStr = manufacturerIDStr;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean getHasBeatCustomersTransaction() {
		return hasBeatCustomersTransaction;
	}

	public void setHasBeatCustomersTransaction(boolean hasBeatCustomersTransaction) {
		this.hasBeatCustomersTransaction = hasBeatCustomersTransaction;
	}

	public int getTransCounter() {
		return transCounter;
	}

	public void setTransCounter(int transCounter) {
		this.transCounter = transCounter;
	}

	public boolean getHasTransaction() {
		return transCounter > 0 ? true : false;
	}

	public void setHasTransaction(boolean hasTransaction) {
		this.hasTransaction = hasTransaction;
	}

	
}
