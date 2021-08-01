package com.sales.crm.model;

public enum TenantType {
	
	SYSTEM(1),
	RESELLER(2);
	
	private int tenantType;
	
	
	TenantType(int tenantType){
		this.tenantType = tenantType;
	}


	public int getTenantType() {
		return tenantType;
	}


	public void setTenantType(int tenantType) {
		this.tenantType = tenantType;
	}
	
	

}
