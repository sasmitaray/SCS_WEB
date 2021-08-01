package com.sales.crm.model;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class ReSTResponse {
	
	private int status;
	
	private String errorMsg;
	
	private String errorCode;
	
	private int businessEntityID;
	
	List<? extends BusinessEntity> businessEntities;
	
	List<ObjectNode> jsonObjects;
	
	public static final int STATUS_SUCCESS = 1;
	
	public static final int STATUS_FAILURE = 0;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public List<? extends BusinessEntity> getBusinessEntities() {
		return businessEntities;
	}
	public void setBusinessEntities(List<? extends BusinessEntity> businessEntities) {
		this.businessEntities = businessEntities;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public int getBusinessEntityID() {
		return businessEntityID;
	}
	public void setBusinessEntityID(int businessEntityID) {
		this.businessEntityID = businessEntityID;
	}
	public List<ObjectNode> getJsonObjects() {
		return jsonObjects;
	}
	public void setJsonObjects(List<ObjectNode> jsonObjects) {
		this.jsonObjects = jsonObjects;
	}
	
	
}
