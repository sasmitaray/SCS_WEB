package com.sales.crm.model;

public class Permission {
	
	private int permissionID;
	
	private String permissionKey;
	
	private String name;
	
	private String description;
	
	public int getPermissionID() {
		return permissionID;
	}
	public void setPermissionID(int permissionID) {
		this.permissionID = permissionID;
	}
	public String getPermissionKey() {
		return permissionKey;
	}
	public void setPermissionKey(String permissionKey) {
		this.permissionKey = permissionKey;
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
	
	@Override
	public String toString() {
		return "Permission [permissionID=" + permissionID + ", permissionKey=" + permissionKey + ", name=" + name
				+ ", description=" + description + "]";
	}
	
	
	
}
