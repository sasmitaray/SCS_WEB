package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.exception.CRMException;
import com.sales.crm.model.Beat;
import com.sales.crm.model.DeliveryExecutive;
import com.sales.crm.model.Manufacturer;
import com.sales.crm.model.ManufacturerBeats;
import com.sales.crm.model.ManufacturerDelivExecs;
import com.sales.crm.model.ManufacturerSalesExecs;
import com.sales.crm.model.SalesExecutive;
import com.sales.crm.service.BeatService;
import com.sales.crm.service.DeliveryExecService;
import com.sales.crm.service.ManufacturerService;
import com.sales.crm.service.SalesExecService;

@Controller
@RequestMapping("/web/manufacturerWeb")
public class ManufacturerWebController {
	
	private static Logger logger = Logger.getLogger(ManufacturerWebController.class);
	

	@Autowired
	ManufacturerService manufacturerService;
	
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	SalesExecService salesExecService;
	
	@Autowired
	DeliveryExecService delivExecService;
	
	@Autowired
	BeatService beatService;
	
	@GetMapping(value="/{manufacturerCode}")
	public ModelAndView get(@PathVariable String manufacturerCode){
		Manufacturer manufacturer = manufacturerService.getManufacturer(manufacturerCode, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/manufacturer_details", "manufacturer", manufacturer);
		
	}
	
	@RequestMapping(value="/createManufacturerForm", method = RequestMethod.GET)  
	public ModelAndView createManufacturerForm(Model model){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("manufacturer", new Manufacturer());
		return new ModelAndView("/create_manufacturer", modelMap);
	}
	
	@RequestMapping(value="/editManufacturerForm/{manufacturerCode}", method = RequestMethod.GET)  
	public ModelAndView editManufacturerForm(@PathVariable String manufacturerCode){
		Manufacturer manufacturer = manufacturerService.getManufacturer(manufacturerCode, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("manufacturer", manufacturer);
		return new ModelAndView("/edit_manufacturer", modelMap);
	}
	
	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView create(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		manufacturer.setTenantID(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		String msg = "";
		try{
			manufacturerService.createManufacturer(manufacturer);
		}catch(Exception exception){
			msg = "Creation of new manufacturer is not successfull, please contact System Administrator";
		}
		return new ModelAndView("/create_manufacturer_conf","msg", msg); 
	}
	
	
	@RequestMapping(value="/update",method = RequestMethod.POST) 
	public ModelAndView update(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		String msg = "";
		try{
			manufacturerService.updateManufacturer(manufacturer);
		}catch(Exception exception){
			msg = "Manufacturer details could not be updated successfully, please contact System Administrator. ";
		}
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("msg", msg);
		modelMap.put("manufacturerID", String.valueOf(manufacturer.getManufacturerID()));
		return new ModelAndView("/edit_manufacturer_conf", "map", modelMap);
	}
	
	@GetMapping(value="/delete/{manufacturerID}")
	public ModelAndView delete(@PathVariable int manufacturerID){
		String msg = "";
		try{
			manufacturerService.deleteManufacturer(manufacturerID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Manufacturer could not be successfully removed, please contact System Administrator";
		}
		return new ModelAndView("/delete_manufacturer_conf","msg", msg); 
	}
	
	@GetMapping(value="/list")
	public ModelAndView list(){
		List<Manufacturer> manufacturers = manufacturerService.getTenantManufacturers(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/manufacturer_list","manufacturers", manufacturers);  
	}
	
	@GetMapping(value="/assignSalesExecutiveForm") 
	public ModelAndView getAssignSalesExecutivesToManufacturerForm(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<Manufacturer> manufacturers = manufacturerService.getTenantManufacturers(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("manufacturers", manufacturers);
		modelMap.put("manufacturer", new Manufacturer());
		return new ModelAndView("/assign_salesexecs_to_manufacturer", modelMap);
	}
	
	@GetMapping(value="/assignDelivExecutiveForm") 
	public ModelAndView getAssignDelivExecutivesToManufacturerForm(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<Manufacturer> manufacturers = manufacturerService.getTenantManufacturers(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("manufacturers", manufacturers);
		modelMap.put("manufacturer", new Manufacturer());
		return new ModelAndView("/assign_delivexecs_to_manufacturer", modelMap);
	}
	
	@PostMapping(value="/assignSalesExecutive") 
	public ModelAndView assignSalesExecutivesToManufacturer(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		String msg = "";
		try{
			manufacturerService.assignSalesExecutivesToManufacturer(tenantID, manufacturer);
		}catch(Exception exception){
			msg = "Sales Executives could not be successfully mapped to manufacturer. Please try after sometime and if error persists contact System Administrator";
		}
		return new ModelAndView("/assign_salesexec_to_manufacturer_conf", "msg", msg);
	}
	
	@PostMapping(value="/assignDelivExecutive") 
	public ModelAndView assignDelivExecutivesToManufacturer(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		String msg = "";
		try{
			manufacturerService.assignDelivExecutivesToManufacturer(tenantID, manufacturer);
		}catch(Exception exception){
			msg = "Delivery Executives could not be successfully mapped to manufacturer. Please try after sometime and if error persists contact System Administrator";
		}
		return new ModelAndView("/assign_delivexec_to_manufacturer_conf", "msg", msg);
	}
	
	
	/**
	@GetMapping(value="/supp-manufacturer/list")
	public ModelAndView suppManufacturerList(){
		List<Manufacturer> manufacturers = manufacturerService.getSuppManufacturerList(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/supp_manufacturer_list","manufacturers", manufacturers);  
	}
**/
	@GetMapping(value="/manufacturer-salesexecs/list")
	public ModelAndView manufacturerSalesExecList(){
		List<ManufacturerSalesExecs> manufacturerSalesExecs = manufacturerService.getManufacturerSalesExecs(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/manufacturer_sales_execs_list","manufacturerSalesExecs", manufacturerSalesExecs);  
	}
	
	@GetMapping(value="/manufacturer-delivexecs/list")
	public ModelAndView manufacturerDelivExecList(){
		List<ManufacturerDelivExecs> manufacturerDelivExecs = manufacturerService.getManufacturerDelivExecsList(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/manufacturer_deliv_execs_list","manufacturerDelivExecs", manufacturerDelivExecs);  
	}
	
	@GetMapping(value="/assignSalesExecEditForm/{manufacturerCode}") 
	public ModelAndView editAssignSalesExecForm(@PathVariable String manufacturerCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<SalesExecutive> salesExecs = salesExecService.getActiveSalesExecutives(tenantID);
		Manufacturer manufacturer = manufacturerService.getManufacturer(manufacturerCode, tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("salesExecs", salesExecs);
		modelMap.put("manufacturer", manufacturer);
		return new ModelAndView("/edit_assign_salesexec_to_manufacturer", modelMap);
	}
	
	@GetMapping(value="/assignDelivExecEditForm/{manufacturerCode}") 
	public ModelAndView editAssignDelivExecForm(@PathVariable String manufacturerCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<DeliveryExecutive> delivExecs = delivExecService.getActiveDeliveryExecutives(tenantID);
		Manufacturer manufacturer = manufacturerService.getManufacturer(manufacturerCode, tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("delivExecs", delivExecs);
		modelMap.put("manufacturer", manufacturer);
		return new ModelAndView("/edit_assign_delivexec_to_manufacturer", modelMap);
	}
	
	@PostMapping(value="/updateAassignedSalesexecs")
	public ModelAndView updateAssignedSalesExecutives(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		String msg = "";
		try{
			int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
			manufacturerService.updateAssignedSalesExecs(manufacturer.getManufacturerID(), manufacturer.getSalesExecsIDs(), tenantID);
		}catch(Exception exception){
			msg = "Sales Executives mapped to manufacturer could not be successfully updated. Please try after sometime and if error persists contact System Administrator";
			if(exception instanceof CRMException) {
				msg = exception.getMessage();
			}else {
				logger.error("Error while updating sales executives manufacturer mapping", exception);
			}
		}
		return new ModelAndView("/edit_salesexec_to_manufacturer_conf", "msg", msg);
	}
	
	@PostMapping(value="/updateAassignedDelivexecs")
	public ModelAndView updateAssignedDelivExecutives(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		String msg = "";
		try{
			int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
			manufacturerService.updateAssignedDelivExecs(manufacturer.getManufacturerID(), manufacturer.getDelivExecsIDs(), tenantID);
		}catch(Exception exception){
			msg = "Delivery Executives mapped to manufacturer could not be successfully updated. Please try after sometime and if error persists contact System Administrator";
			if(exception instanceof CRMException) {
				msg = exception.getMessage();
			}else {
				logger.error("Error while updating delivery executives manufacturer mapping", exception);
			}
		}
		return new ModelAndView("/edit_delivexec_to_manufacturer_conf", "msg", msg);
	}
	
	@GetMapping(value="/deleteAassignedSalesexec/{manufacturerCode}/{tenantID}")
	public ModelAndView deleteAassignedSalesexec(@PathVariable("manufacturerCode") String manufacturerCode, @PathVariable("tenantID") int tenantID){
		String msg = "";
		try{
			manufacturerService.deleteAassignedSalesExec(manufacturerCode, tenantID);
		}catch(Exception exception){
			logger.error("Error while removing manufacturer sales executive mapping", exception);
			msg = "Sales Executives mapped to manufacturer could not be removed successfully. Please try after sometime and if error persists contact System Administrator";
		}
		return new ModelAndView("/remove_manufacturer_salesexec_conf","msg", msg); 
	}
	
	@GetMapping(value="/deleteAassignedDelivExec/{manufacturerCode}")
	public ModelAndView deleteAassignedDelivExec(@PathVariable String manufacturerCode){
		String msg = "";
		try{
			manufacturerService.deleteAassignedDelivExec(manufacturerCode, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Delivery Executives mapped to manufacturer could not be removed successfully. Please try after sometime and if error persists contact System Administrator";
		}
		return new ModelAndView("/remove_manufacturer_delivexec_conf","msg", msg); 
	}
	
	@GetMapping(value="/beats/list")
	public ModelAndView getManufacturerBeats(){
		List<ManufacturerBeats> manufacturerBeats = manufacturerService.getManufacturerBeats(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/manufacturer_beats_list","manufacturerBeats", manufacturerBeats);  
	}
	
	@GetMapping(value="/assignBeatsForm") 
	public ModelAndView getAssignBeatsFormForm(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<Manufacturer> manufacturers = manufacturerService.getTenantTrimmedManufacturers(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("manufacturers", manufacturers);
		modelMap.put("manufacturer", new Manufacturer());
		modelMap.put("tenantID", tenantID);
		return new ModelAndView("/assign_beats_to_manufacturer", modelMap);
	}
	
	@PostMapping(value="/assignBeatsToManufacturer")
	public ModelAndView assignBeatsToCustomer(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		String msg = "";
		try{
			manufacturerService.assignBeatsToManufacturer(manufacturer.getManufacturerID(), manufacturer.getBeatIDs(), manufacturer.getTenantID());
		}catch(Exception exception){
			msg = "Manufacturers could not be successfully assigned to beat. Please try after sometine, if error persists contact System Administrator.";
		}
		return new ModelAndView("/assign_manufacturer_beats_conf", "msg", msg);
	}
	
	@GetMapping(value="/assignedBeatManufacturerEditForm/{manufacturerCode}") 
	public ModelAndView editAssignedBeatToCustomerForm(@PathVariable("manufacturerCode") String manufacturerCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		Manufacturer manufacturer = manufacturerService.getManufacturer(manufacturerCode, tenantID);
		List<Beat> beats = beatService.getTenantBeats(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("manufacturer", manufacturer);
		modelMap.put("beats", beats);
		return new ModelAndView("/edit_assigned_beats_to_manufacturer", modelMap);
	}
	
	@PostMapping(value="/updateAssignedBeatToManufacturer")
	public ModelAndView updateAssignedBeatToManufacturer(@ModelAttribute("manufacturer") Manufacturer manufacturer){
		String msg = "";
		try{
			manufacturerService.updateAssignedBeatToManufacturer(manufacturer.getManufacturerID(), manufacturer.getBeatIDs(), Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Assigned beats to manufacturer could not be updated successfully. Please try after sometine, if error persists contact System Administrator.";
			if(exception instanceof CRMException) {
				msg = exception.getMessage();
			}
		}
		return new ModelAndView("/update_manufacturer_beat_conf", "msg", msg);
	}
	
	@GetMapping(value="/deleteAssignedBeatManufacturerLink/{manufacturerCode}/{tenantID}")
	public ModelAndView deleteAssignedBeatManufacturerLink(@PathVariable("manufacturerCode") String manufacturerCode, @PathVariable("tenantID") int tenantID){
		String msg = "";
		try{
			manufacturerService.deleteAssignedBeatManufacturerLink(manufacturerCode, tenantID);
		}catch(Exception exception){
			msg = "Beats associated to manufacturer could not be removed successfully. Please try after sometine, if error persists contact System Administrator.";
		}
		return new ModelAndView("/remove_manufacturer_beat_conf","msg", msg); 
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		webDataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
