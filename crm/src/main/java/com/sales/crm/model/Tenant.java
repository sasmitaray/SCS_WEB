package com.sales.crm.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "TENANTS")
public class Tenant{
	
private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int tenantID;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "TYPE")
	private int tenantType;
	
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	
	@Column(name = "STATUS_ID")
	private int statusID;
	
	@Column(name = "DATE_CREATED")
	private Date dateCreated;
	
	@Column(name = "DATE_MODIFIED")
	private Date dateModified;	
	
	@Transient
	private String statusText;
	
	@Transient
	private List<Address> address;
	
	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getTenantType() {
		return tenantType;
	}
	public void setTenantType(int tenantType) {
		this.tenantType = tenantType;
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

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + statusID;
		result = prime * result + ((statusText == null) ? 0 : statusText.hashCode());
		result = prime * result + tenantID;
		result = prime * result + tenantType;
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
		Tenant other = (Tenant) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateModified == null) {
			if (other.dateModified != null)
				return false;
		} else if (!dateModified.equals(other.dateModified))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (statusID != other.statusID)
			return false;
		if (statusText == null) {
			if (other.statusText != null)
				return false;
		} else if (!statusText.equals(other.statusText))
			return false;
		if (tenantID != other.tenantID)
			return false;
		if (tenantType != other.tenantType)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tenant [tenantID=" + tenantID + ", code=" + code + ", tenantType=" + tenantType + ", name=" + name
				+ ", description=" + description + ", statusID=" + statusID + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + ", statusText=" + statusText + ", address=" + address
				+ ", getTenantID()=" + getTenantID() + ", getCode()=" + getCode() + ", getTenantType()="
				+ getTenantType() + ", getName()=" + getName() + ", getDescription()=" + getDescription()
				+ ", getAddress()=" + getAddress() + ", getStatusText()=" + getStatusText() + ", getDateCreated()="
				+ getDateCreated() + ", getDateModified()=" + getDateModified() + ", hashCode()=" + hashCode() + "]";
	}


	
	
	
}
