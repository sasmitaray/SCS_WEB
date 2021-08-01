package com.sales.crm.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ADDRESS")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class Address extends BusinessEntity{
	
	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int id;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "ADDRESS_LINE1")
	private String addressLine1;
	
	@Column(name = "ADDRESS_LINE2")
	private String addressLine2;
	
	@Column(name = "STREET")
	private String street;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "POSTAL_CODE")
	private String postalCode;
	
	@Column(name = "CONTACT_PERSON")
	private String contactPerson;
	
	@Column(name = "PHONE_NO")
	private String phoneNumber;
	
	@Column(name = "MOBILE_NUMBER_PRIMARY")
	private String mobileNumberPrimary;
	
	@Column(name = "MOBILE_NUMBER_SECONDARY")
	private String mobileNumberSecondary;
	
	@Column(name = "ADDRESS_TYPE")
	private int addrressType;
	
	@Column(name = "EMAIL_ID")
	private String emailID;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public String getEmailID() {
		return emailID;
	}
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public int getAddrressType() {
		return addrressType;
	}
	public void setAddrressType(int addrressType) {
		this.addrressType = addrressType;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobileNumberPrimary() {
		return mobileNumberPrimary;
	}
	public void setMobileNumberPrimary(String mobileNumberPrimary) {
		this.mobileNumberPrimary = mobileNumberPrimary;
	}
	public String getMobileNumberSecondary() {
		return mobileNumberSecondary;
	}
	public void setMobileNumberSecondary(String mobileNumberSecondary) {
		this.mobileNumberSecondary = mobileNumberSecondary;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
		result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
		result = prime * result + addrressType;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((contactPerson == null) ? 0 : contactPerson.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((emailID == null) ? 0 : emailID.hashCode());
		result = prime * result + id;
		result = prime * result + ((mobileNumberPrimary == null) ? 0 : mobileNumberPrimary.hashCode());
		result = prime * result + ((mobileNumberSecondary == null) ? 0 : mobileNumberSecondary.hashCode());
		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
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
		Address other = (Address) obj;
		if (addressLine1 == null) {
			if (other.addressLine1 != null)
				return false;
		} else if (!addressLine1.equals(other.addressLine1))
			return false;
		if (addressLine2 == null) {
			if (other.addressLine2 != null)
				return false;
		} else if (!addressLine2.equals(other.addressLine2))
			return false;
		if (addrressType != other.addrressType)
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (contactPerson == null) {
			if (other.contactPerson != null)
				return false;
		} else if (!contactPerson.equals(other.contactPerson))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (emailID == null) {
			if (other.emailID != null)
				return false;
		} else if (!emailID.equals(other.emailID))
			return false;
		if (id != other.id)
			return false;
		if (mobileNumberPrimary == null) {
			if (other.mobileNumberPrimary != null)
				return false;
		} else if (!mobileNumberPrimary.equals(other.mobileNumberPrimary))
			return false;
		if (mobileNumberSecondary == null) {
			if (other.mobileNumberSecondary != null)
				return false;
		} else if (!mobileNumberSecondary.equals(other.mobileNumberSecondary))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Address [id=" + id + ", code=" + code + ", addressLine1=" + addressLine1 + ", addressLine2="
				+ addressLine2 + ", street=" + street + ", city=" + city + ", state=" + state + ", country=" + country
				+ ", postalCode=" + postalCode + ", contactPerson=" + contactPerson + ", phoneNumber=" + phoneNumber
				+ ", mobileNumberPrimary=" + mobileNumberPrimary + ", mobileNumberSecondary=" + mobileNumberSecondary
				+ ", addrressType=" + addrressType + ", emailID=" + emailID + ", tenantID=" + tenantID
				+ ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + "]";
	}
	
	
}
