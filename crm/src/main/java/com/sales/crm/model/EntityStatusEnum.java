package com.sales.crm.model;

public enum EntityStatusEnum {
	
NOT_APPLICABLE(EntityTypeEnum.ALL, 1, "Not Applicable"),
ACTIVE(EntityTypeEnum.ALL, 2, "Active"),
INACTIVE(EntityTypeEnum.ALL, 3, "Inactive"),
CANCELLED(EntityTypeEnum.ALL, 4, "Cancelled"),
SELF_REGISTERED(EntityTypeEnum.TENANT, 10, "Self Registered"),
ORDER_BOOKING_SCHEDULED(EntityTypeEnum.ORDER, 50, "Order Booking Scheduled"),
ORDER_CREATED(EntityTypeEnum.ORDER, 51, "Order Created"),
DELIVERY_SCHEDULED(EntityTypeEnum.ORDER, 52, "Delivery Scheduled"),
DELIVERY_COMPLETED(EntityTypeEnum.ORDER, 53, "Delivery Completed"),
PARTIALLY_DELIVERED(EntityTypeEnum.ORDER, 54, "Partially Delivered"),
PAYMENT_SCHEDULED(EntityTypeEnum.ORDER, 55, "Payment Scheduled"),
PAYMENT_COMPLETED(EntityTypeEnum.ORDER, 56, "Payment Completed"),
PARTIALLY_PAID(EntityTypeEnum.ORDER, 57, "Partially Paid"),
ORDER_DELETED(EntityTypeEnum.ORDER, 58, "Order Deleted"),
OTP_GENERATED(EntityTypeEnum.OTP, 60, "OTP Generated"),
OTP_VERIFIED(EntityTypeEnum.OTP, 61, "OTP Verified");	
	
	
private EntityTypeEnum entityType;
private int entityStatus;
private String statusText;



EntityStatusEnum(EntityTypeEnum entityType, int entityStatus, String statusText) {
	
	this.entityType = entityType;
	this.entityStatus = entityStatus;
	this.statusText = statusText;
}



public int getEntityStatus() {
	return entityStatus;
}

public void setEntityStatus(int entityStatus) {
	this.entityStatus = entityStatus;
}

public String getStatusText() {
	return statusText;
}


}
