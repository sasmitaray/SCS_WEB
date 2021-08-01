package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.model.ResPermWebModel;
import com.sales.crm.model.Resource;
import com.sales.crm.model.ResourcePermission;
import com.sales.crm.model.Role;
import com.sales.crm.model.User;
import com.sales.crm.service.RoleService;

@Controller
@RequestMapping("/web/role")
public class RoleWebController {

	@Autowired
	RoleService roleService;
	
	@Autowired
	HttpSession httpSession;
	
	
	
	@GetMapping(value="/{roleID}")
	public ModelAndView get(@PathVariable int roleID){
		Role role = roleService.getRole(roleID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/role_details", "role", role);
		
	}
	
	@RequestMapping(value="/createRoleForm", method = RequestMethod.GET)  
	public ModelAndView createRoleForm(Model model){
		return new ModelAndView("/create_role", "role", new Role());
	}
	
	@RequestMapping(value="/editRoleForm/{userID}", method = RequestMethod.GET)  
	public ModelAndView editRoleForm(@PathVariable int roleID){
		Role role = roleService.getRole(roleID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/edit_role", "role", role);
	}
	
	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView create(@ModelAttribute("role") Role role){
		roleService.createRole(role);
		return list();
	}
	
	@RequestMapping(value="/update",method = RequestMethod.POST) 
	public ModelAndView update(@ModelAttribute("role") Role role){
		roleService.updateRole(role);
		return get(role.getRoleID());
	}
	
	@DeleteMapping(value="/{roleID}")
	public void delete(@PathVariable int roleID){
		roleService.deleteRole(roleID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
	}
	
	@GetMapping(value="/list")
	public ModelAndView list(){
		List<Role> roles = roleService.getRoles((User)httpSession.getAttribute("user"));
		return new ModelAndView("/roles_list","roles", roles);  
	}
	
	@GetMapping(value="/resource_permission/{roleID}/view")
	public ModelAndView getRoleResourcePermissions(@PathVariable int roleID){
		Map<Resource, List<ResourcePermission>> resPermMap = new HashMap<Resource, List<ResourcePermission>>();
		try{
			List<Integer> roleIDs = new ArrayList<Integer>();
			roleIDs.add(roleID);
			List<ResourcePermission> resourcePermissions = roleService.getRolesResourcePermissions(roleIDs,
					Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
			for(ResourcePermission resourcePermission : resourcePermissions){
				Resource resource = resourcePermission.getResource();
				if(!resPermMap.containsKey(resource)){
					resPermMap.put(resource, new ArrayList<ResourcePermission>());
				}
				resPermMap.get(resource).add(resourcePermission);
			}
	
		}catch(Exception exception){
			exception.printStackTrace();
		}
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("resPermMap", resPermMap);
		modelMap.put("resPermWebModel", new ResPermWebModel());
		modelMap.put("roleID", roleID);
		return new ModelAndView("/resource_permission_view", modelMap);  
	}
	
	@GetMapping(value="/resource_permission/{roleID}/editform")
	public ModelAndView getRoleResourcePermissionsEditForm(@PathVariable int roleID){
		Map<Resource, List<ResourcePermission>> resPermMap = new HashMap<Resource, List<ResourcePermission>>();
		int tenantID = -1;
		try{
			List<Integer> roleIDs = new ArrayList<Integer>();
			roleIDs.add(roleID);
			tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
			List<ResourcePermission> resourcePermissions = roleService.getRolesResourcePermissions(roleIDs,
					tenantID);
			for(ResourcePermission resourcePermission : resourcePermissions){
				Resource resource = resourcePermission.getResource();
				if(!resPermMap.containsKey(resource)){
					resPermMap.put(resource, new ArrayList<ResourcePermission>());
				}
				resPermMap.get(resource).add(resourcePermission);
			}
	
		}catch(Exception exception){
			exception.printStackTrace();
		}
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("resPermMap", resPermMap);
		modelMap.put("resPermWebModel", new ResPermWebModel());
		modelMap.put("roleID", roleID);
		modelMap.put("tenantID", tenantID);
		return new ModelAndView("/edit_resource_permission", modelMap);  
	}
	
	@RequestMapping(value="/resource_permission/save",method = RequestMethod.POST) 
	public ModelAndView saveRoleResourcePermission(@ModelAttribute("resPermWebModel") ResPermWebModel resPermWebModel){
	    List<ResourcePermission> resourcePermissions = new ArrayList<ResourcePermission>();
	    String msg = "";
	    for(Integer resourcePermId : resPermWebModel.getResourcePermIDList()){
	    	ResourcePermission resourcePermission = new ResourcePermission();
	    	resourcePermission.setId(resourcePermId);
	    	resourcePermission.setTenantID(resPermWebModel.getTenantID());
	    	resourcePermission.setRoleID(resPermWebModel.getRoleID());
	    	resourcePermissions.add(resourcePermission);
	    }
	    try{
	    	roleService.saveRoleResourcePermission(resourcePermissions, resPermWebModel.getRoleID(), resPermWebModel.getTenantID());
	    }catch(Exception exception){
	    	msg = "Previleges could not be updated successfully. Please re-try after sometime and if error persists, contact system administrator. ";
	    }
	    Map<String, Object> modelMap = new HashMap<String, Object>();
	    modelMap.put("msg", msg);
	    modelMap.put("roleID", resPermWebModel.getRoleID());
		return new ModelAndView("/resource_permission_edit_conf", modelMap);
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
