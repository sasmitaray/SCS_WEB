package com.sales.crm.dao;

import java.util.List;

import com.sales.crm.model.ResourcePermission;
import com.sales.crm.model.Role;

public interface RoleDAO {
	
	void create(Role role);
	
	Role get(int roleID, int tenantID);
	
	void update(Role role);
	
	void delete(int roleID, int tenantID);
	
	List<Role> getRoles(List<Integer> roleIDs, int tenantID);
	
	List<ResourcePermission> getResourcePermissions(int tenantID) throws Exception;
	
	List<ResourcePermission> getRoleResourcePermissions(List<Integer> roleIDs, int tenantID) throws Exception;
	
	void saveRoleResourcePermission(List<ResourcePermission> resourcePermissions, int roleID, int tenantID) throws Exception;

	public List<Integer> getRoleResourcePermissionIDs(List<Integer> roleIDs, int tenantID) throws Exception;
}
