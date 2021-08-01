package com.sales.crm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

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
@Table(name = "MANUFACTURER_SALES_OFFICER")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class ManufacturerSalesOfficer extends BusinessEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int ID;
	
	@Column(name = "CODE")
	private String code;

	@Column(name = "NAME")
	private String name;
	
	@Column(name = "EFFECTIVE_FROM")
	private Date effectiveFrom;
	
	@Column(name = "CONTACT_NO")
	private String contactNo;
	
	@Column(name = "MANUFACTURER_ID")
	private int manufacturerID;
	
	@Transient
	private String effectiveFromStr;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getEffectiveFrom() {
		return effectiveFrom;
	}

	public void setEffectiveFrom(Date effectiveFrom) {
		this.effectiveFrom = effectiveFrom;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public int getManufacturerID() {
		return manufacturerID;
	}

	public void setManufacturerID(int manufacturerID) {
		this.manufacturerID = manufacturerID;
	}

	
	public String getEffectiveFromStr() {
		if(effectiveFrom != null){
			return new SimpleDateFormat("dd-MM-yyyy").format(effectiveFrom);
		}
		return effectiveFromStr;
	}

	public void setEffectiveFromStr(String effectiveFromStr) {
		this.effectiveFromStr = effectiveFromStr;
	}
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((contactNo == null) ? 0 : contactNo.hashCode());
		result = prime * result + ((effectiveFrom == null) ? 0 : effectiveFrom.hashCode());
		result = prime * result + ((effectiveFromStr == null) ? 0 : effectiveFromStr.hashCode());
		result = prime * result + manufacturerID;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ManufacturerSalesOfficer other = (ManufacturerSalesOfficer) obj;
		if (ID != other.ID)
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (contactNo == null) {
			if (other.contactNo != null)
				return false;
		} else if (!contactNo.equals(other.contactNo))
			return false;
		if (effectiveFrom == null) {
			if (other.effectiveFrom != null)
				return false;
		} else if (!effectiveFrom.equals(other.effectiveFrom))
			return false;
		if (effectiveFromStr == null) {
			if (other.effectiveFromStr != null)
				return false;
		} else if (!effectiveFromStr.equals(other.effectiveFromStr))
			return false;
		if (manufacturerID != other.manufacturerID)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ManufacturerSalesOfficer [ID=" + ID + ", code=" + code + ", name=" + name + ", effectiveFrom="
				+ effectiveFrom + ", contactNo=" + contactNo + ", manufacturerID=" + manufacturerID
				+ ", effectiveFromStr=" + effectiveFromStr + ", tenantID=" + tenantID + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + ", statusID=" + statusID + "]";
	}

	

	
}
