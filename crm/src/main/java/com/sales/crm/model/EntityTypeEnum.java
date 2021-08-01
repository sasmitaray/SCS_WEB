package com.sales.crm.model;

public enum EntityTypeEnum {
	
	ALL(0),
	TENANT(1),
	CUSTOMER(2),
	MANUFACTURER(3),
	USER(4),
	ORDER(5),
	BEAT(6),
	AREA(7),
	OTP(8);
	
	
	private int entityType;
	
	EntityTypeEnum(int entityType){
		this.entityType = entityType;
	}

	public int getEntityType() {
		return entityType;
	}

	public void setEntityType(int entityType) {
		this.entityType = entityType;
	}
	
	

}
