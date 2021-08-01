package com.sales.crm.model;

import java.util.Date;

public class ResourcePermission {
	
	private int id;
	
	private Resource resource;
	
	private Permission permission;
	
	private int roleID;
	
	private Date dateCreated;
	
	private Date dateModified;
	
	private boolean isPresent;
	
	private int tenantID;
	
	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}

	public boolean isPresent() {
		return isPresent;
	}

	public void setPresent(boolean isPresent) {
		this.isPresent = isPresent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + id;
		result = prime * result + (isPresent ? 1231 : 1237);
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((resource == null) ? 0 : resource.hashCode());
		result = prime * result + roleID;
		result = prime * result + tenantID;
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
		ResourcePermission other = (ResourcePermission) obj;
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
		if (id != other.id)
			return false;
		if (isPresent != other.isPresent)
			return false;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (roleID != other.roleID)
			return false;
		if (tenantID != other.tenantID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResourcePermission [id=" + id + ", resource=" + resource + ", permission=" + permission + ", roleID="
				+ roleID + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", isPresent="
				+ isPresent + ", tenantID=" + tenantID + "]";
	}

		
}
