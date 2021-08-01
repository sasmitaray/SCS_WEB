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

import com.sales.crm.model.Reseller;
import com.sales.crm.model.Tenant;
import com.sales.crm.service.ResellerService;
import com.sales.crm.service.TenantService;
import com.sales.crm.service.UserService;

@Controller
@RequestMapping("/web/resellerWeb")
public class ResellerWebController {
	
	@Autowired
	ResellerService resellerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	TenantService tenantService;
	
	
	/**
	@RequestMapping(value="/selfRegisterResellerForm", method = RequestMethod.GET)  
	public ModelAndView selfRegisterResellerForm(){
		return new ModelAndView("/reseller_self_registration", "reseller", new Reseller());
	}
	
	@RequestMapping(value="/selfRegisterReseller",method = RequestMethod.POST)  
	public ModelAndView selfRegisterReseller(@ModelAttribute("reseller") Reseller reseller){
		String msg = "";
		String emailID = "";
		try{
			List<Address> addresses = reseller.getAddress();
			for(Address address : addresses){
				if(address.getAddrressType() == 1){
					emailID = address.getEmailID();
				}
			}
			if(resellerService.isEmailIDAlreadyUsed(emailID)){
				msg = "E-Mail address "+ emailID + " is already in use, please provide another valid email id.";
			}else{
				reseller.setStatus(BusinessEntity.STATUS_SELF_REGISTERED);
				resellerService.createReseller(reseller);
			}
		}catch(Exception exception){
			msg = "Self registration is not successfull. Please try after sometime and if the error persists, contact System Administrator.";
		}
		return new ModelAndView("/reseller_self_reg_conf", "msg", msg);
	}
	*/
	
	/**
	 * Used without login
	 * 
	 * @param reseller
	 * @return
	 */
	/**
	@RequestMapping(value="/saveReseller",method = RequestMethod.POST)  
	public ModelAndView createReseller(@ModelAttribute("reseller") Reseller reseller){
		resellerService.createReseller(reseller);
		User user = new User();
		user.setUserName("user"+reseller.getResellerID());
		user.setPassword("pass"+reseller.getResellerID());
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setResellerID(reseller.getResellerID());
		Set<Integer> roleIDList = new HashSet<Integer>();
		roleIDList.add(1);
		user.setRoleIDs(roleIDList);
		try{
			userService.createUser(user);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return new ModelAndView("/message", "message", "Admin User is successfully created. <br><b>User Name</b> - "+ user.getUserName() +"<br><b>password</b> - "+ user.getPassword());
	
	}
	**/
	
	@RequestMapping(value="/createResellerForm", method = RequestMethod.GET)  
	public ModelAndView createResellerForm(){
		return new ModelAndView("/create_reseller", "reseller", new Reseller());
	}
	
	@GetMapping(value="/view")
	public ModelAndView get(){
		Reseller reseller = resellerService.getReseller(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/reseller_details", "reseller", reseller);
		
	}
	
	
	@GetMapping(value="/{resellerID}")
	public ModelAndView getDetails(@PathVariable int resellerID){
		Reseller reseller = resellerService.getReseller(resellerID);
		return new ModelAndView("/reseller_details", "reseller", reseller);
		
	}
	
	@RequestMapping(value="/editResellerForm/{resellerID}", method = RequestMethod.GET)  
	public ModelAndView editCustomerForm(@PathVariable int resellerID){
		Reseller reseller = resellerService.getReseller(resellerID);
		return new ModelAndView("/edit_reseller", "reseller", reseller);
	}
	
	@RequestMapping(value="/update",method = RequestMethod.POST) 
	public ModelAndView update(@ModelAttribute("reseller") Reseller reseller){
		String msg = "";
		try {
			resellerService.updateReseller(reseller);
		}catch(Exception exception){
			msg = "Reseller details could not be updated successfully, please contact System Administrator. ";
		}
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("msg", msg);
		return new ModelAndView("/edit_reseller_conf", "map", modelMap);
	}	
	
	@GetMapping(value="/list")
	public ModelAndView list(){
		List<Reseller> resellers = new ArrayList<Reseller>();
		try{
			resellers = resellerService.getResellers();
		}catch(Exception exception){
			
		}
		return new ModelAndView("/resellers_list","resellers", resellers);  
	}
	
	/**
	@GetMapping(value="/delete/{resellerID}")
	public void delete(@PathVariable int resellerID){
		resellerService.deleteReseller(resellerID);
	}
	*/
	
	@GetMapping(value="/activate/{resellerID}")
	public ModelAndView activateReseller(@PathVariable int resellerID){
		resellerService.activateReseller(resellerID);
		return getDetails(resellerID);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
