package com.sales.crm.model;

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
@Table(name = "CUSTOMER_OTP")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class CustomerOTP extends BusinessEntity{
	
	@Transient
	public static final int OTP_ORDER_BOOKING = 1;
	@Transient
	public static final int OTP_DELIVERY_CONF = 2;
	@Transient
	public static final int OTP_PAYMENT_CONF = 3;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int otpID;
	
	@Column(name = "CUSTOMER_ID")
	private int customerID;
	
	@Column(name = "FIELD_EXEC_ID")
	private int fieldExecID;
	
	@Column(name = "GENERTAED_OTP")
	private String genaratedOTP;
	
	@Column(name = "SUBMITTED_OTP")
	private String submiitedOTP;
	
	@Column(name = "OTP_TYPE")
	private int otpType;
	
	@Column(name = "GENERATED_DATE_TIME")
	private Date otpGeneratedDateTime;
	
	@Column(name = "SUBMITTED_DATE_TIME")
	private Date otpSubmitedDateTime;
	
	@Column(name = "RETRY_COUNT")
	private int retryCount;
	
	@Transient
	private String customerName;
	
	@Transient
	private String salesExecName;
	
	@Transient
	private String otpStringType;
	
	@Transient
	private String otpStatus;
	
	@Transient
	private String stringDateGenerated;
	
	@Transient
	private String stringDateUsed;
	
	//Order Booking ID/Order ID
	@Transient
	private int entityID;

	public int getOtpID() {
		return otpID;
	}

	public void setOtpID(int otpID) {
		this.otpID = otpID;
	}

	public int getCustomerID() {
		return customerID;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	
	public String getGenaratedOTP() {
		return genaratedOTP;
	}

	public void setGenaratedOTP(String genaratedOTP) {
		this.genaratedOTP = genaratedOTP;
	}

	public String getSubmiitedOTP() {
		return submiitedOTP;
	}

	public void setSubmiitedOTP(String submiitedOTP) {
		this.submiitedOTP = submiitedOTP;
	}

	public int getOtpType() {
		return otpType;
	}

	public void setOtpType(int otpType) {
		this.otpType = otpType;
	}

	public Date getOtpGeneratedDateTime() {
		return otpGeneratedDateTime;
	}

	public void setOtpGeneratedDateTime(Date otpGeneratedDateTime) {
		this.otpGeneratedDateTime = otpGeneratedDateTime;
	}

	public Date getOtpSubmitedDateTime() {
		return otpSubmitedDateTime;
	}

	public void setOtpSubmitedDateTime(Date otpSubmitedDateTime) {
		this.otpSubmitedDateTime = otpSubmitedDateTime;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSalesExecName() {
		return salesExecName;
	}

	public void setSalesExecName(String salesExecName) {
		this.salesExecName = salesExecName;
	}

	public String getOtpStringType() {
		return otpStringType;
	}

	public void setOtpStringType(String otpStringType) {
		this.otpStringType = otpStringType;
	}

	public String getOtpStatus() {
		return otpStatus;
	}

	public void setOtpStatus(String otpStatus) {
		this.otpStatus = otpStatus;
	}

	public String getStringDateGenerated() {
		return stringDateGenerated;
	}

	public void setStringDateGenerated(String stringDateGenerated) {
		this.stringDateGenerated = stringDateGenerated;
	}

	public String getStringDateUsed() {
		return stringDateUsed;
	}

	public void setStringDateUsed(String stringDateUsed) {
		this.stringDateUsed = stringDateUsed;
	}

	public int getFieldExecID() {
		return fieldExecID;
	}

	public void setFieldExecID(int fieldExecID) {
		this.fieldExecID = fieldExecID;
	}

	public int getEntityID() {
		return entityID;
	}

	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + customerID;
		result = prime * result + ((customerName == null) ? 0 : customerName.hashCode());
		result = prime * result + fieldExecID;
		result = prime * result + ((genaratedOTP == null) ? 0 : genaratedOTP.hashCode());
		result = prime * result + ((otpGeneratedDateTime == null) ? 0 : otpGeneratedDateTime.hashCode());
		result = prime * result + otpID;
		result = prime * result + ((otpStatus == null) ? 0 : otpStatus.hashCode());
		result = prime * result + ((otpStringType == null) ? 0 : otpStringType.hashCode());
		result = prime * result + ((otpSubmitedDateTime == null) ? 0 : otpSubmitedDateTime.hashCode());
		result = prime * result + otpType;
		result = prime * result + retryCount;
		result = prime * result + ((salesExecName == null) ? 0 : salesExecName.hashCode());
		result = prime * result + ((stringDateGenerated == null) ? 0 : stringDateGenerated.hashCode());
		result = prime * result + ((stringDateUsed == null) ? 0 : stringDateUsed.hashCode());
		result = prime * result + ((submiitedOTP == null) ? 0 : submiitedOTP.hashCode());
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
		CustomerOTP other = (CustomerOTP) obj;
		if (customerID != other.customerID)
			return false;
		if (customerName == null) {
			if (other.customerName != null)
				return false;
		} else if (!customerName.equals(other.customerName))
			return false;
		if (fieldExecID != other.fieldExecID)
			return false;
		if (genaratedOTP == null) {
			if (other.genaratedOTP != null)
				return false;
		} else if (!genaratedOTP.equals(other.genaratedOTP))
			return false;
		if (otpGeneratedDateTime == null) {
			if (other.otpGeneratedDateTime != null)
				return false;
		} else if (!otpGeneratedDateTime.equals(other.otpGeneratedDateTime))
			return false;
		if (otpID != other.otpID)
			return false;
		if (otpStatus == null) {
			if (other.otpStatus != null)
				return false;
		} else if (!otpStatus.equals(other.otpStatus))
			return false;
		if (otpStringType == null) {
			if (other.otpStringType != null)
				return false;
		} else if (!otpStringType.equals(other.otpStringType))
			return false;
		if (otpSubmitedDateTime == null) {
			if (other.otpSubmitedDateTime != null)
				return false;
		} else if (!otpSubmitedDateTime.equals(other.otpSubmitedDateTime))
			return false;
		if (otpType != other.otpType)
			return false;
		if (retryCount != other.retryCount)
			return false;
		if (salesExecName == null) {
			if (other.salesExecName != null)
				return false;
		} else if (!salesExecName.equals(other.salesExecName))
			return false;
		if (stringDateGenerated == null) {
			if (other.stringDateGenerated != null)
				return false;
		} else if (!stringDateGenerated.equals(other.stringDateGenerated))
			return false;
		if (stringDateUsed == null) {
			if (other.stringDateUsed != null)
				return false;
		} else if (!stringDateUsed.equals(other.stringDateUsed))
			return false;
		if (submiitedOTP == null) {
			if (other.submiitedOTP != null)
				return false;
		} else if (!submiitedOTP.equals(other.submiitedOTP))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomerOTP [otpID=" + otpID + ", customerID=" + customerID + ", fieldExecID=" + fieldExecID
				+ ", genaratedOTP=" + genaratedOTP + ", submiitedOTP=" + submiitedOTP + ", otpType=" + otpType
				+ ", otpGeneratedDateTime=" + otpGeneratedDateTime + ", otpSubmitedDateTime=" + otpSubmitedDateTime
				+ ", retryCount=" + retryCount + ", customerName=" + customerName + ", salesExecName=" + salesExecName
				+ ", otpStringType=" + otpStringType + ", otpStatus=" + otpStatus + ", stringDateGenerated="
				+ stringDateGenerated + ", stringDateUsed=" + stringDateUsed + ", tenantID=" + tenantID
				+ ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", statusID=" + statusID + "]";
	}

	
	

}
