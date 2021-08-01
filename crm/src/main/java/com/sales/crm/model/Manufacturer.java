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
@Table(name = "MANUFACTURERS")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class Manufacturer extends BusinessEntity{
	
	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	int manufacturerID;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "NAME")
	String name;
	
	@Column(name = "DESCRIPTION")
	String description;
	
	@Column(name = "TRANX_COUNTER")
	private int transCounter;
	
	//@OneToMany(orphanRemoval=true, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Transient
	List<Address> address;
	
	@Transient
	private ManufacturerSalesOfficer salesOfficer;
	
	@Transient
	private ManufacturerAreaManager areaManager;
	
	@Transient
	private List<Manufacturer> manufacturers;
	
	@Transient
	private List<Integer> manufacturerIDs;
	
	@Transient
	private List<Beat> beats;
	
	@Transient
	private List<SalesExecutive> salesExecs;
	
	@Transient
	private List<Integer> salesExecsIDs;
	
	@Transient
	private List<DeliveryExecutive> delivExecs;
	
	@Transient
	private List<Integer> delivExecsIDs;
	
	@Transient
	private List<Integer> beatIDs;
	
	@Transient
	private boolean hasTransactions;
	
	
	public int getManufacturerID() {
		return manufacturerID;
	}
	public void setManufacturerID(int manufacturerID) {
		this.manufacturerID = manufacturerID;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public ManufacturerSalesOfficer getSalesOfficer() {
		return salesOfficer;
	}
	public void setSalesOfficer(ManufacturerSalesOfficer salesOfficer) {
		this.salesOfficer = salesOfficer;
	}
	public ManufacturerAreaManager getAreaManager() {
		return areaManager;
	}
	public void setAreaManager(ManufacturerAreaManager areaManager) {
		this.areaManager = areaManager;
	}
	
	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}
	public void setManufacturers(List<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}
	
	public List<Integer> getManufacturerIDs() {
		return manufacturerIDs;
	}
	public void setManufacturerIDs(List<Integer> manufacturerIDs) {
		this.manufacturerIDs = manufacturerIDs;
	}
	
	public List<Beat> getBeats() {
		return beats;
	}
	public void setBeats(List<Beat> beats) {
		this.beats = beats;
	}
	
	public List<SalesExecutive> getSalesExecs() {
		return salesExecs;
	}
	public void setSalesExecs(List<SalesExecutive> salesExecs) {
		this.salesExecs = salesExecs;
	}
	public List<Integer> getSalesExecsIDs() {
		return salesExecsIDs;
	}
	public void setSalesExecsIDs(List<Integer> salesExecsIDs) {
		this.salesExecsIDs = salesExecsIDs;
	}
	public List<DeliveryExecutive> getDelivExecs() {
		return delivExecs;
	}
	public void setDelivExecs(List<DeliveryExecutive> delivExecs) {
		this.delivExecs = delivExecs;
	}
	
	public List<Integer> getDelivExecsIDs() {
		return delivExecsIDs;
	}
	public void setDelivExecsIDs(List<Integer> delivExecsIDs) {
		this.delivExecsIDs = delivExecsIDs;
	}
	
	public List<Integer> getBeatIDs() {
		return beatIDs;
	}
	public void setBeatIDs(List<Integer> beatIDs) {
		this.beatIDs = beatIDs;
	}
	public int getTransCounter() {
		return transCounter;
	}
	public void setTransCounter(int transCounter) {
		this.transCounter = transCounter;
	}
	public boolean getHasTransactions() {
		return transCounter > 0 ? true : false;
	}
	public void setHasTransactions(boolean hasTransactions) {
		this.hasTransactions = hasTransactions;
	}
	
	
	
	
}
