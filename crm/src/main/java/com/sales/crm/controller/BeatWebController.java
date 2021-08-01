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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.exception.CRMException;
import com.sales.crm.model.Area;
import com.sales.crm.model.Beat;
import com.sales.crm.model.Customer;
import com.sales.crm.model.TrimmedCustomer;
import com.sales.crm.service.AreaService;
import com.sales.crm.service.BeatService;
import com.sales.crm.service.CustomerService;
import com.sales.crm.service.ManufacturerService;
import com.sales.crm.service.UserService;

@Controller
@RequestMapping("/web/beatWeb")
public class BeatWebController {

	@Autowired
	BeatService beatService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	UserService userService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ManufacturerService manufacturerService;
	
	@GetMapping(value="/{beatCode}")
	public ModelAndView get(@PathVariable String beatCode){
		Beat beat = beatService.getBeat(beatCode, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/beat_details", "beat", beat);
		
	}
	
	@RequestMapping(value="/createBeatForm", method = RequestMethod.GET)  
	public ModelAndView createBeatForm(Model model){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<Area> areas = areaService.getTenantAreasNotMappedToBeat(tenantID);
		//List<Manufacturer> manufacturer = manufacturerService.getTenantManufacturers(tenantID);
		List<String> beatNames = beatService.getTenantBeatNames(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("areas", areas);
		modelMap.put("beat", new Beat());
		//modelMap.put("manufacturers", manufacturer);
		modelMap.put("beatNames", beatNames);
		return new ModelAndView("/create_beat", modelMap);
	}
	
	@RequestMapping(value="/editBeatForm/{beatCode}", method = RequestMethod.GET)  
	public ModelAndView editBeatForm(@PathVariable String beatCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		Beat beat = beatService.getBeat(beatCode, tenantID);
		List<Area> areas = areaService.getTenantAreasNotMappedToBeatForEdit(tenantID, beat.getBeatID());
		List<Area> beatAreas = areaService.getBeatAreas(beat.getBeatID(), tenantID);
		List<Integer> areaIDs = new ArrayList<Integer>();
		for(Area area : beatAreas){
			areaIDs.add(area.getAreaID());
		}
		beat.setAreaIDs(areaIDs);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("areas", areas);
		modelMap.put("beat", beat);
		return new ModelAndView("/edit_beat", modelMap);
	}
	
	@RequestMapping(value="/save",method = RequestMethod.POST)  
	public ModelAndView create(@ModelAttribute("beat") Beat beat){
		if(beat.getCoverageSchedule().equals("-1")){
			beat.setCoverageSchedule("");
		}
		//beat.setManufacturerID(Integer.parseInt(beat.getManufacturerIDStr()));
		beat.setTenantID(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		String msg = "";
		try{
			beatService.createBeat(beat);
		}catch(Exception exception){
			msg = "New Beat could not be created successfully. Please try after sometine, if error persists contact System Administrator.";
		}
		return new ModelAndView("/create_beat_conf", "msg", msg);
	}
	
	@RequestMapping(value="/update",method = RequestMethod.POST) 
	public ModelAndView update(@ModelAttribute("beat") Beat beat) {
		if (beat.getCoverageSchedule().equals("-1")) {
			beat.setCoverageSchedule("");
		}
		String msg = "";
		try {
			beatService.updateBeat(beat);
		} catch (Exception exception) {
			msg = "Beat details could not be updated successfully. Please try after sometine, if error persists contact System Administrator. ";
		}
		Map<String, String> modelMap = new HashMap<String, String>();
		modelMap.put("msg", msg);
		modelMap.put("beatCode", String.valueOf(beat.getCode()));
		return new ModelAndView("/edit_beat_conf", "map", modelMap);
	}
	
	@GetMapping(value="/delete/{beatID}")
	public ModelAndView delete(@PathVariable int beatID){
		String msg = "";
		try{
			beatService.deleteBeat(beatID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Beat could not be successfully removed. Please try after sometine, if error persists contact System Administrator.";
		}
		return new ModelAndView("/delete_beat_conf","msg", msg);  
	}
	
	@GetMapping(value="/list")
	public ModelAndView list() {
		List<Beat> beats = beatService
				.getTenantBeats(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/beat_list", "beats", beats);
	}
	
	@GetMapping(value="/beat-customers/list")
	public ModelAndView getBeatCustomers(){
		List<Beat> beats = beatService.getTenantBeats(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/beat_customers_list","beats", beats);  
	}
	
	@GetMapping(value="/assignBeatsForm") 
	public ModelAndView getAssignBeatsFormForm(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<Beat> beats = beatService.getTenantBeats(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("beats", beats);
		modelMap.put("beat", new Beat());
		modelMap.put("tenantID", tenantID);
		return new ModelAndView("/assign_beats_to_customer", modelMap);
	}
	
	@PostMapping(value="/assignBeatsToCustomer")
	public ModelAndView assignBeatsToCustomer(@ModelAttribute("beat") Beat beat){
		String msg = "";
		try{
			beatService.assignCustomersToBeat(beat.getBeatID(), beat.getCustomerIDs(), beat.getTenantID());
		}catch(Exception exception){
			msg = "Customers could not be successfully assigned to beat. Please try after sometine, if error persists contact System Administrator.";
		}
		return new ModelAndView("/assign_beat_customers_conf", "msg", msg);
	}
	
	@GetMapping(value="/assignedBeatCustomerEditForm/{beatCode}") 
	public ModelAndView editAssignedBeatToCustomerForm(@PathVariable("beatCode") String beatCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		Map<String, Object> modelMap = new HashMap<String, Object>();
		Beat beat = beatService.getBeat(beatCode, tenantID);
		List<TrimmedCustomer> customers = customerService.getTenantTrimmedCustomers(tenantID);
		modelMap.put("beat", beat);
		modelMap.put("customers", customers);
		//modelMap.put("customer", new Customer());
		return new ModelAndView("/edit_assigned_beats_to_customer", modelMap);
	}
	
	@PostMapping(value="/updateAssignedCustomersToBeat")
	public ModelAndView updateAssignedBeatToCustomers(@ModelAttribute("beat") Beat beat){
		String msg = "";
		try{
			beatService.updateAssignedCustomersToBeat(beat.getBeatID(), beat.getCustomerIDs(), Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Assigned customers to beat could not be updated successfully. Please try after sometine, if error persists contact System Administrator.";
			if(exception instanceof CRMException) {
				msg = exception.getMessage();
			}
		}
		return new ModelAndView("/update_beat_customers_conf", "msg", msg);
	}
	
	@GetMapping(value="/deleteAssignedCustomersBeatLink/{beatCode}")
	public ModelAndView deleteAssignedBeatCustomerLink(@PathVariable("beatCode") String beatCode){
		String msg = "";
		try{
			beatService.deleteAssignedBeatCustomerLink(beatCode,Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Beats associated to customers could not be removed successfully. Please try after sometine, if error persists contact System Administrator.";
		}
		return new ModelAndView("/remove_beat_customers_conf","msg", msg); 
	}
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));

		/**
		binder.registerCustomEditor(Integer.class, "areaIDs", new CustomCollectionEditor(List.class) {
			@Override
			protected Object convertElement(Object element) {
				Area area = new Area();
				area.setAreaID(Integer.valueOf(String.valueOf(element)));

				return area;
			}
		});
		**/
	}
}
