package com.sales.crm.model;

import java.util.List;

public class ResPermWebModel {
	
	private List<Integer> resourcePermIDList;
	
	private int tenantID;
	
	private int roleID;

	public List<Integer> getResourcePermIDList() {
		return resourcePermIDList;
	}

	public void setResourcePermIDList(List<Integer> resourcePermIDList) {
		this.resourcePermIDList = resourcePermIDList;
	}


	public int getTenantID() {
		return tenantID;
	}

	public void setTenantID(int tenantID) {
		this.tenantID = tenantID;
	}

	public int getRoleID() {
		return roleID;
	}

	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	
	
}
