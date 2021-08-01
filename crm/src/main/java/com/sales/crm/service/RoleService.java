package com.sales.crm.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.RoleDAO;
import com.sales.crm.model.ResourcePermission;
import com.sales.crm.model.Role;
import com.sales.crm.model.User;

@Service
public class RoleService {
	
	@Autowired
	private RoleDAO roleDAO;
	
	public Role getRole(int roleID, int tenantID){
		return roleDAO.get(roleID, tenantID);
	}
	
	public void createRole(Role role){
		roleDAO.create(role);
	}
	
	public void updateRole(Role role){
		roleDAO.update(role);
	}
	
	public void deleteRole(int roleID, int tenantID){
		roleDAO.delete(roleID, tenantID);
	}
	
	public List<Role> getRoles(User user){
		List<Role> roles = user.getRoles();
		List<Integer> roleIDs = new ArrayList<Integer>();
		for(Role role : roles){
			roleIDs.add(role.getRoleID());
		}
		return roleDAO.getRoles(roleIDs, user.getTenantID());
	}
	
	public List<Role> getRoles(int tenantID){
		return roleDAO.getRoles(null, tenantID);
	}
	
	public List<ResourcePermission> getResourcePermissions(int tenantID) throws Exception {
		return roleDAO.getResourcePermissions(tenantID);
	}

	public List<ResourcePermission> getRolesResourcePermissions(List<Integer> roleIDs, int tenantID) throws Exception {
		return roleDAO.getRoleResourcePermissions(roleIDs, tenantID);
	}
	
	public void saveRoleResourcePermission(List<ResourcePermission> resourcePermissions, int roleID, int tenantID) throws Exception{
		roleDAO.saveRoleResourcePermission(resourcePermissions, roleID, tenantID);
	}
	
	public List<Integer> getRoleResourcePermissionIDs(List<Integer> roleIDs, int tenantID) throws Exception {
		return roleDAO.getRoleResourcePermissionIDs(roleIDs, tenantID);
	}
	
	public List<Integer> getRoleResourcePermissionIDs(User user) throws Exception {
		List<Role> roles = user.getRoles();
		List<Integer> roleIDs = new ArrayList<Integer>();
		for(Role role : roles){
			roleIDs.add(role.getRoleID());
		}
		return roleDAO.getRoleResourcePermissionIDs(roleIDs, user.getTenantID());
	}
}
