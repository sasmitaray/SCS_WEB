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
@Table(name = "CUSTOMERS")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class Customer extends BusinessEntity{
	
	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int customerID;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "TRANX_COUNTER")
	private int transCounter;
	
	
	//@OneToMany(orphanRemoval=true, cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@Transient
	private List<Address> address;
	
	@Transient
	private int salesExecID;
	
	@Transient
	private String salesExecName;
	
	@Transient
	private String beatName;
	
	@Transient
	private List<Integer> beatIDs;
	
	@Transient
	private List<Beat> beats;
	
	@Transient
	private String customerIDStr;
	
	@Transient
	private boolean isOrderingProcessInProgress;
	
	@Transient
	private String statusAsString;
	
	@Transient
	private boolean hasTransactions;
	
	@Transient
	private String deactivationReason;
	
	@Transient
	private String activationReason;
	
	@Transient
	private String deactivationDateStr;
	
	@Transient
	private String activationDateStr;
	
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
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
	public int getSalesExecID() {
		return salesExecID;
	}
	public void setSalesExecID(int salesExecID) {
		this.salesExecID = salesExecID;
	}
	public String getSalesExecName() {
		return salesExecName;
	}
	public void setSalesExecName(String salesExecName) {
		this.salesExecName = salesExecName;
	}
	
	public List<Integer> getBeatIDs() {
		return beatIDs;
	}
	public void setBeatIDs(List<Integer> beatIDs) {
		this.beatIDs = beatIDs;
	}
	public List<Beat> getBeats() {
		return beats;
	}
	public void setBeats(List<Beat> beats) {
		this.beats = beats;
	}
	
	public String getBeatName() {
		return beatName;
	}
	public void setBeatName(String beatName) {
		this.beatName = beatName;
	}
	
	public String getCustomerIDStr() {
		return customerIDStr;
	}
	public void setCustomerIDStr(String customerIDStr) {
		this.customerIDStr = customerIDStr;
	}
	
	public boolean getIsOrderingProcessInProgress() {
		return isOrderingProcessInProgress;
	}
	public void setOrderingProcessInProgress(boolean isOrderingProcessInProgress) {
		this.isOrderingProcessInProgress = isOrderingProcessInProgress;
	}
	
	public String getStatusAsString() {
		return statusAsString;
	}
	public void setStatusAsString(String statusAsString) {
		this.statusAsString = statusAsString;
	}
	
	public boolean getHasTransactions() {
		return transCounter > 0 ? true : false;
	}
	public void setHasTransactions(boolean hasTransactions) {
		this.hasTransactions = hasTransactions;
	}
	public int getTransCounter() {
		return transCounter;
	}
	public void setTransCounter(int transCounter) {
		this.transCounter = transCounter;
	}
	
	public String getDeactivationReason() {
		return deactivationReason;
	}
	public void setDeactivationReason(String deactivationReason) {
		this.deactivationReason = deactivationReason;
	}
	public String getActivationReason() {
		return activationReason;
	}
	public void setActivationReason(String activationReason) {
		this.activationReason = activationReason;
	}
	public String getDeactivationDateStr() {
		return deactivationDateStr;
	}
	public void setDeactivationDateStr(String deactivationDateStr) {
		this.deactivationDateStr = deactivationDateStr;
	}
	public String getActivationDateStr() {
		return activationDateStr;
	}
	public void setActivationDateStr(String activationDateStr) {
		this.activationDateStr = activationDateStr;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((beatIDs == null) ? 0 : beatIDs.hashCode());
		result = prime * result + ((beatName == null) ? 0 : beatName.hashCode());
		result = prime * result + ((beats == null) ? 0 : beats.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + customerID;
		result = prime * result + ((customerIDStr == null) ? 0 : customerIDStr.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (hasTransactions ? 1231 : 1237);
		result = prime * result + (isOrderingProcessInProgress ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + salesExecID;
		result = prime * result + ((salesExecName == null) ? 0 : salesExecName.hashCode());
		result = prime * result + ((statusAsString == null) ? 0 : statusAsString.hashCode());
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
		Customer other = (Customer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (beatIDs == null) {
			if (other.beatIDs != null)
				return false;
		} else if (!beatIDs.equals(other.beatIDs))
			return false;
		if (beatName == null) {
			if (other.beatName != null)
				return false;
		} else if (!beatName.equals(other.beatName))
			return false;
		if (beats == null) {
			if (other.beats != null)
				return false;
		} else if (!beats.equals(other.beats))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (customerID != other.customerID)
			return false;
		if (customerIDStr == null) {
			if (other.customerIDStr != null)
				return false;
		} else if (!customerIDStr.equals(other.customerIDStr))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (hasTransactions != other.hasTransactions)
			return false;
		if (isOrderingProcessInProgress != other.isOrderingProcessInProgress)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (salesExecID != other.salesExecID)
			return false;
		if (salesExecName == null) {
			if (other.salesExecName != null)
				return false;
		} else if (!salesExecName.equals(other.salesExecName))
			return false;
		if (statusAsString == null) {
			if (other.statusAsString != null)
				return false;
		} else if (!statusAsString.equals(other.statusAsString))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", code=" + code + ", name=" + name + ", description="
				+ description + ", address=" + address + ", salesExecID=" + salesExecID + ", salesExecName="
				+ salesExecName + ", beatName=" + beatName + ", beatIDs=" + beatIDs + ", beats=" + beats
				+ ", customerIDStr=" + customerIDStr + ", isOrderingProcessInProgress=" + isOrderingProcessInProgress
				+ ", statusAsString=" + statusAsString + ", isCustHavingAnyTransaction=" + hasTransactions
				+ ", tenantID=" + tenantID + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified
				+ ", statusID=" + statusID + "]";
	}
	
	
	
	
}
