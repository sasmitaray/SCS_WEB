package com.sales.crm.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

public class SalesExecutive extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	private List<Beat> beats;
	
	@Transient
	private List<Integer> beatIDLists;
	
	@Transient
	private Date visitDate;
	
	@Transient
	private List<Integer> customerIDs;
	
	@Transient
	private int tenantID;
	
	private Map<Integer, String> manufacturers = new HashMap<Integer, String>();

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
	
	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}

	public Map<Integer, String> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(Map<Integer, String> manufacturers) {
		this.manufacturers = manufacturers;
	}

	@Override
	public String toString() {
		return "SalesExecutive [beats=" + beats + ", beatIDLists=" + beatIDLists + ", visitDate=" + visitDate
				+ ", customerIDs=" + customerIDs + ", tenantID=" + tenantID + ", manufacturers=" + manufacturers
				+ ", getUserID()=" + getUserID() + ", getUserName()=" + getUserName() + ", getPassword()="
				+ getPassword() + ", getDescription()=" + getDescription() + ", getFirstName()=" + getFirstName()
				+ ", getLastName()=" + getLastName() + ", getMobileNo()=" + getMobileNo() + ", getEmailID()="
				+ getEmailID() + ", getRoles()=" + getRoles() + ", getRoleIDs()=" + getRoleIDs() + ", getName()="
				+ getName() + ", getNewPassword()=" + getNewPassword() + ", getPasswordMedium()=" + getPasswordMedium()
				+ ", getSecurityQuestions()=" + getSecurityQuestions() + ", getLoggedIn()=" + getLoggedIn()
				+ ", getSecQuestionAnsws()=" + getSecQuestionAnsws() + ", getCode()=" + getCode() + ", hashCode()="
				+ hashCode() + ", toString()=" + super.toString() + ", getDateCreated()=" + getDateCreated()
				+ ", getDateModified()=" + getDateModified() + ", getStatusID()=" + getStatusID() + ", getClass()="
				+ getClass() + "]";
	}

	
	
	
}
