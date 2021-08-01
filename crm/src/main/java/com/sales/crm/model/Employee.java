package com.sales.crm.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

public class Employee extends BusinessEntity {

	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int empID;
	
	@Column(name = "CODE")
	private String code;
	
	
	@Column(name = "EMP_TYPE")
	private int type;
	
	@Column(name = "CUSTOMER_ID")
	private int customerID;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employeeID", cascade = CascadeType.PERSIST)
	private List<Address> addresses;
	
	
	public int getEmpID() {
		return empID;
	}
	public void setEmpID(int empID) {
		this.empID = empID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
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
		result = prime * result + ((addresses == null) ? 0 : addresses.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + customerID;
		result = prime * result + empID;
		result = prime * result + type;
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
		Employee other = (Employee) obj;
		if (addresses == null) {
			if (other.addresses != null)
				return false;
		} else if (!addresses.equals(other.addresses))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (customerID != other.customerID)
			return false;
		if (empID != other.empID)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Employee [empID=" + empID + ", code=" + code + ", type=" + type + ", customerID=" + customerID
				+ ", addresses=" + addresses + ", tenantID=" + tenantID + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + "]";
	}
	
	
	
}
