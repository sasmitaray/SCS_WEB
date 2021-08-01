package com.sales.crm.model;

import java.util.List;
import java.util.Set;

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
@Table(name = "USERS")
@AttributeOverrides({
	@AttributeOverride(name = "statusID", column = @Column(name = "STATUS_ID")),
	@AttributeOverride(name = "tenantID", column = @Column(name = "TENANT_ID")),
	@AttributeOverride(name = "dateCreated", column = @Column(name = "DATE_CREATED")),
	@AttributeOverride(name = "dateModified", column = @Column(name = "DATE_MODIFIED"))})
public class User extends BusinessEntity{

	private static final long serialVersionUID = 0l;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	private int userID;
	
	@Column(name = "CODE")
	private String code;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "MOBILE_NO")
	private String mobileNo;
	
	@Column(name = "EMAIL_ID")
	private String emailID;
	
	@Column(name= "LOGGED_IN")
	private int loggedIn;
	
	@Transient
	private List<Role> roles;
	
	@Transient
	private Set<Integer> roleIDs;
	
	@Transient
	private String name;
	
	@Transient
	private String newPassword;
	
	@Transient
	private int passwordMedium;
	
	@Transient
	private List<SecurityQuestion> securityQuestions;
	
	@Transient
	private List<String> secQuestionAnsws;
	
	@Transient
	private int tenantID;

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Set<Integer> getRoleIDs() {
		return roleIDs;
	}

	public void setRoleIDs(Set<Integer> roleIDs) {
		this.roleIDs = roleIDs;
	}

	public String getName() {
		return firstName +" "+lastName;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	public int getPasswordMedium() {
		return passwordMedium;
	}

	public void setPasswordMedium(int passwordMedium) {
		this.passwordMedium = passwordMedium;
	}
	
	public List<SecurityQuestion> getSecurityQuestions() {
		return securityQuestions;
	}

	public void setSecurityQuestions(List<SecurityQuestion> securityQuestions) {
		this.securityQuestions = securityQuestions;
	}
	
	public int getLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(int loggedIn) {
		this.loggedIn = loggedIn;
	}

	
	public List<String> getSecQuestionAnsws() {
		return secQuestionAnsws;
	}

	public void setSecQuestionAnsws(List<String> secQuestionAnsws) {
		this.secQuestionAnsws = secQuestionAnsws;
	}
	

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((emailID == null) ? 0 : emailID.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + loggedIn;
		result = prime * result + ((mobileNo == null) ? 0 : mobileNo.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((newPassword == null) ? 0 : newPassword.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + passwordMedium;
		result = prime * result + ((roleIDs == null) ? 0 : roleIDs.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((secQuestionAnsws == null) ? 0 : secQuestionAnsws.hashCode());
		result = prime * result + ((securityQuestions == null) ? 0 : securityQuestions.hashCode());
		result = prime * result + tenantID;
		result = prime * result + userID;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
		User other = (User) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (emailID == null) {
			if (other.emailID != null)
				return false;
		} else if (!emailID.equals(other.emailID))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (loggedIn != other.loggedIn)
			return false;
		if (mobileNo == null) {
			if (other.mobileNo != null)
				return false;
		} else if (!mobileNo.equals(other.mobileNo))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (newPassword == null) {
			if (other.newPassword != null)
				return false;
		} else if (!newPassword.equals(other.newPassword))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (passwordMedium != other.passwordMedium)
			return false;
		if (roleIDs == null) {
			if (other.roleIDs != null)
				return false;
		} else if (!roleIDs.equals(other.roleIDs))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (secQuestionAnsws == null) {
			if (other.secQuestionAnsws != null)
				return false;
		} else if (!secQuestionAnsws.equals(other.secQuestionAnsws))
			return false;
		if (securityQuestions == null) {
			if (other.securityQuestions != null)
				return false;
		} else if (!securityQuestions.equals(other.securityQuestions))
			return false;
		if (tenantID != other.tenantID)
			return false;
		if (userID != other.userID)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [userID=" + userID + ", code=" + code + ", userName=" + userName + ", password=" + password
				+ ", description=" + description + ", firstName=" + firstName + ", lastName=" + lastName + ", mobileNo="
				+ mobileNo + ", emailID=" + emailID + ", loggedIn=" + loggedIn + ", roles=" + roles + ", roleIDs="
				+ roleIDs + ", name=" + name + ", newPassword=" + newPassword + ", passwordMedium=" + passwordMedium
				+ ", securityQuestions=" + securityQuestions + ", secQuestionAnsws=" + secQuestionAnsws + ", tenantID="
				+ tenantID + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", statusID="
				+ statusID + "]";
	}

	
	
}
