package com.sales.crm.dao;

import java.util.List;

import com.sales.crm.model.Reseller;
import com.sales.crm.model.Tenant;

public interface TenantDAO {
	
	void create(Tenant tenant) throws Exception;
	
	Tenant get(int tenantID);
	
	void update(Tenant tenant);
	
	void delete(int tenantID);
	
	boolean isEmailIDAlreadyUsed(String emailID) throws Exception;
	
	List<Tenant> getTenants() throws Exception;

}
