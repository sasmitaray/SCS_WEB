package com.sales.crm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@MappedSuperclass
public class BusinessEntity implements Serializable{
	
	private static final long serialVersionUID = 0l;
	
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_SELF_REGISTERED = 1;
	public static final int STATUS_ACTIVE = 2;
	public static final int STATUS_CANCELLED = 3;
	
	protected int tenantID;
	
	protected Date dateCreated;
	
	protected Date dateModified;
	
	protected int statusID;
	
	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}
	
	public int getStatusID() {
		return statusID;
	}

	public void setStatusID(int statusID) {
		this.statusID = statusID;
	}

	@Override
	public String toString() {
		return "BusinessEntity [tenantID=" + tenantID + ", dateCreated=" + dateCreated + ", dateModified="
				+ dateModified + "]";
	}

	
		
}
