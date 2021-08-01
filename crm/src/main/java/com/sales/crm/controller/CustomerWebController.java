package com.sales.crm.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.model.Beat;
import com.sales.crm.model.Customer;
import com.sales.crm.model.SalesExecutive;
import com.sales.crm.model.User;
import com.sales.crm.service.BeatService;
import com.sales.crm.service.CustomerService;
import com.sales.crm.service.SalesExecService;
import com.sales.crm.service.UserService;
import com.sales.crm.util.CustomerXLSProcessor;
import com.sales.crm.util.EncodeDecodeUtil;

@Controller
@RequestMapping("/web/customerWeb")
public class CustomerWebController {

	@Autowired
	CustomerService customerService;
	
	
	@Autowired
	UserService userService;
	
	@Autowired
	SalesExecService salesExecService;
	
	@Autowired
	BeatService beatService;
	
	@Autowired
	HttpSession httpSession;
	
	private static Logger logger = Logger.getLogger(CustomerWebController.class);
	
	
	@GetMapping(value="/{customerCode}")
	public ModelAndView get(@PathVariable String customerCode){
		//int decodedCustomerID = EncodeDecodeUtil.decodeStringToNumber(customerID);
		Customer customer = customerService.getCustomer(customerCode, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/customer_details", "customer", customer);
		
	}
	
	@RequestMapping(value="/createCustomerForm", method = RequestMethod.GET)  
	public ModelAndView createCustomerForm(Model model){
		List<Beat> beats = beatService.getTenantBeats(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("customer", new Customer());
		modelMap.put("beats", new ArrayList<Beat>());
		return new ModelAndView("/create_customer", modelMap);
	}
	
	@RequestMapping(value="/editCustomerForm/{customerCode}", method = RequestMethod.GET)  
	public ModelAndView editCustomerForm(@PathVariable String customerCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		Customer customer = customerService.getCustomer(customerCode, tenantID);
		//List<SalesExecutive> salesExecs = salesExecService.getSalesExecutives(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("customer", customer);
		//modelMap.put("salesExecs", salesExecs);
		return new ModelAndView("/edit_customer", modelMap);
	}
	
	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView create(@ModelAttribute("customer") Customer customer){
		customer.setTenantID(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		String msg="";
		try{
			customerService.createCustomer(customer);
		}catch(Exception exception){
			msg = "Creation of new customer is not successfull, please contact System Administrator";
		}
		return new ModelAndView("/create_customer_conf","msg", msg); 
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST) 
	public ModelAndView update(@ModelAttribute("customer") Customer customer){
		String msg = "";
		try{
			customerService.updateCustomer(customer);
		}catch(Exception exception){
			msg = "Customer details could not be updated successfully, please contact System Administrator. ";
		}
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("msg", msg);
		modelMap.put("code", String.valueOf(customer.getCode()));
		return new ModelAndView("/edit_customer_conf", "map", modelMap);
	}
	
	@GetMapping(value="/delete/result/{status}")
	public ModelAndView deleteConfirm(@PathVariable("status")  int status){
		String msg = "";
		if(status != 0) {
			msg = "Customer could not be deleted successfully, please contact System Administrator. ";
		}
		return new ModelAndView("/delete_customer_conf","msg", msg); 
	}
	
	@GetMapping(value="/list")
	public ModelAndView list(){
		List<Customer> customers = new ArrayList<Customer>();
		try{
			customers = customerService.search(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))), null);
		}catch(Exception exception){
			logger.error("Error while fetching customer list.");
		}
		return new ModelAndView("/customer_list","customers", customers);  
	}
	
	@PostMapping("/fileUpload")
    public ModelAndView handleFileUpload(@RequestParam MultipartFile file) {
		String msg = "";
		Map<Integer, List<String>> errors = new HashMap<Integer, List<String>>();
		try {
			List<Customer> customers = CustomerXLSProcessor.processCustomerXLS(file.getInputStream(), errors, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
			customerService.createCustomers(customers);
		} catch (Exception e) {
			msg = "Customers could not be successfully imported from excel file. Please try again after sometime and if error persists, contact System Administrator";
		}
		if(errors.size() > 0){
			msg = constructHTMLMsg(errors);
		}
		return new ModelAndView("/customer_upload_conf","msg", msg);  
    }
	
	@GetMapping("/downloadAddCustomerTemplate")
	public void downloadCreateCustomerTemplate(HttpServletRequest request, HttpServletResponse response) {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("/template/customers_template.xlsx").getFile());
			Path path = file.toPath();
			if (Files.exists(path)) {
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
				Files.copy(path, response.getOutputStream());
				response.getOutputStream().flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}
	
	private String constructHTMLMsg(Map<Integer, List<String>> errors){
		
		StringBuilder builder = new StringBuilder();
		
		builder.append("<br>The records listed below are having errors and not processed. All other records which are not shown here are processed successfully."
				+ " Please correct errorouns records and upload the excell again.<br><br>");
		builder.append("<UL>");
		for(Map.Entry<Integer, List<String>> entry : errors.entrySet()){
			builder.append("<LI>Row No : "+entry.getKey()+"</LI><UL>");
			for(String msg : entry.getValue()){
				builder.append("<LI>"+msg+"</LI>");
			}
			builder.append("</UL>");
		}
		builder.append("</UL>");
		
		return builder.toString();
	}

	@GetMapping(value="/deactivate/result/{customerCode}/{status}")
	public ModelAndView deactivateSuccess(@PathVariable("customerCode") String customerCode, @PathVariable("status")  int status){
		String msg = "";
		Map<String, String> modelMap = new HashMap<String, String>();
		if(status != 0) {
			msg = "Customer could not be deactivated successfully, please contact System Administrator. ";
		}
		modelMap.put("msg", msg);
		modelMap.put("action", "Deactivated");
		modelMap.put("customerCode", customerCode);
		return new ModelAndView("/update_customer_status_conf","map", modelMap);  
	}
	
	@GetMapping(value="/activate/result/{customerCode}/{status}")
	public ModelAndView activateCustomer(@PathVariable("customerCode") String customerCode, @PathVariable("status")  int status){
		String msg = "";
		Map<String, String> modelMap = new HashMap<String, String>();
		if(status != 0) {
			msg = "Customer could not be deactivated successfully, please contact System Administrator. ";
		}
		modelMap.put("msg", msg);
		modelMap.put("action", "Activated");
		modelMap.put("customerCode", customerCode);
		return new ModelAndView("/update_customer_status_conf","map", modelMap);  
	}
	
	@InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
     SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
     dateFormat.setLenient(false);
     webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
     
     webDataBinder.registerCustomEditor(User.class, "salesExec", new CustomNumberEditor(Integer.class, true)
	    {
	      @Override
	      public void setValue(Object value)
	      {
	    	 if(value instanceof Integer) {
	    		 User user = new User();
	    		 if(value != null){
	    			 user.setUserID(Integer.parseInt(String.valueOf(value)));
	    		 }
	    		 super.setValue(user);
	    	 }else{
	    		 super.setValue(value);
	    	 }
	      }


	    });  
     }
	
}
