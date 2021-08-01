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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.model.Address;
import com.sales.crm.model.BusinessEntity;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Tenant;
import com.sales.crm.service.TenantService;
import com.sales.crm.service.UserService;

@Controller
@RequestMapping("/web/tenantWeb")
public class TenantWebController {
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpSession httpSession;
	
	
	@RequestMapping(value="/selfRegisterTenantForm", method = RequestMethod.GET)  
	public ModelAndView selfRegisterTenantForm(){
		return new ModelAndView("/tenant_self_registration", "tenant", new Tenant());
	}
	
	@RequestMapping(value="/selfRegisterTenant",method = RequestMethod.POST)  
	public ModelAndView selfRegisterTenant(@ModelAttribute("tenant") Tenant tenant){
		String errMsg = "";
		String emailID = "";
		String succMsg = "";
		try{
			List<Address> addresses = tenant.getAddress();
			for(Address address : addresses){
				if(address.getAddrressType() == 1){
					emailID = address.getEmailID();
				}
			}
			if(tenantService.isEmailIDAlreadyUsed(emailID)){
				errMsg = "E-Mail address "+ emailID + " is already in use, please provide another valid email id.";
			}else{
				tenant.setStatusID(EntityStatusEnum.SELF_REGISTERED.getEntityStatus());
				tenantService.createTenant(tenant);
				String entity = "";
				if(tenant.getTenantType() == 2) {
					entity = "Reseller";
				}
				succMsg = "<br> Dear "+ entity +", <br> Your registration request has been accepted. You will be communicated for the further process through your email address.<br> Thank You.";
			}
		}catch(Exception exception){
			errMsg = "Self registration is not successfull. Please try after sometime and if the error persists, contact System Administrator.";
		}
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("errMsg", errMsg);
		modelMap.put("succMsg", succMsg);
		return new ModelAndView("/tenant_self_reg_conf", "map", modelMap);
	}
	
	@RequestMapping(value="/createTenantForm", method = RequestMethod.GET)  
	public ModelAndView createTenantForm(){
		return new ModelAndView("/create_tenant", "tenant", new Tenant());
	}
	
	@GetMapping(value="/view")
	public ModelAndView get(){
		Tenant tenant = tenantService.getTenant(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/tenant_details", "tenant", tenant);
		
	}
	
	
	@GetMapping(value="/{tenantID}")
	public ModelAndView getDetails(@PathVariable int tenantID){
		Tenant tenant = tenantService.getTenant(tenantID);
		return new ModelAndView("/tenant_details", "tenant", tenant);
		
	}
	
	@RequestMapping(value="/editTenantForm/{tenantID}", method = RequestMethod.GET)  
	public ModelAndView editCustomerForm(@PathVariable int tenantID){
		Tenant tenant = tenantService.getTenant(tenantID);
		return new ModelAndView("/edit_tenant", "tenant", tenant);
	}
	
	@RequestMapping(value="/update",method = RequestMethod.POST) 
	public ModelAndView update(@ModelAttribute("tenant") Tenant tenant){
		tenantService.updateTenant(tenant);
		return get();
	}	
	
	@GetMapping(value="/list")
	public ModelAndView list(){
		List<Tenant> tenants = new ArrayList<Tenant>();
		try{
			tenants = tenantService.getTenants();
		}catch(Exception exception){
			
		}
		return new ModelAndView("/tenants_list","tenants", tenants);  
	}
	
	@GetMapping(value="/delete/{tenantID}")
	public void delete(@PathVariable int tenantID){
		tenantService.deleteTenant(tenantID);
	}
	
	@GetMapping(value="/activate/{tenantID}")
	public ModelAndView activateTenant(@PathVariable int tenantID){
		tenantService.activateTenant(tenantID);
		return getDetails(tenantID);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
