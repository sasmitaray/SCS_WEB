package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.exception.CRMException;
import com.sales.crm.model.Beat;
import com.sales.crm.model.Manufacturer;
import com.sales.crm.model.ManufacturerSalesExecBeats;
import com.sales.crm.service.BeatService;
import com.sales.crm.service.ManufacturerService;
import com.sales.crm.service.SalesExecService;

@Controller
@RequestMapping("/web/salesExecWeb")
public class SalesExecWebController {
	
	@Autowired
	SalesExecService salesExecService;
	
	@Autowired
	BeatService beatService;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	ManufacturerService manufacturerService;
	
	
	@GetMapping(value="/beatlist")
	public ModelAndView salesExecBeatsList(){
		List<ManufacturerSalesExecBeats> manufSalesExecBeats = manufacturerService.getManufSalesExecBeatsList(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/salesexec_beats_list","manufSalesExecBeats", manufSalesExecBeats);  
	}
	
	@GetMapping(value="/deleteBeatsAssignment/{manufacturerID}/{salesExecID}")
	public ModelAndView deleteBeatAssignment(@PathVariable("manufacturerID") int manufacturerID, @PathVariable("salesExecID") int salesExecID){
		String msg = "";
		try{
			salesExecService.deleteBeatAssignment(manufacturerID, salesExecID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Beats associated to Sales Executive could not be removed successfully. Please contact System Administrator.";
		}
		return new ModelAndView("/remove_assign_beats_conf","msg", msg); 
	}
	
	@GetMapping(value="/assignBeatForm") 
	public ModelAndView assignBeatToSalesExecForm(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<Manufacturer> manufacturers = manufacturerService.getTenantManufacturers(tenantID);
		ManufacturerSalesExecBeats manufSalesExecBeats = new ManufacturerSalesExecBeats();
		modelMap.put("manufacturers", manufacturers);
		modelMap.put("manufSalesExecBeats", manufSalesExecBeats);
		modelMap.put("tenantID", tenantID);
		return new ModelAndView("/assign_beats", modelMap);
	}
	
	@PostMapping(value="/assignBeat") 
	public ModelAndView assignBeatToSalesExec(@ModelAttribute("manufSalesExecBeats") ManufacturerSalesExecBeats manufSalesExecBeats){
		String msg = "";
		try{
			int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
			salesExecService.assignBeats(tenantID, manufSalesExecBeats.getManufacturer().getManufacturerID(), manufSalesExecBeats.getSalesExecutive().getUserID(),manufSalesExecBeats.getBeatIDLists());
		}catch(Exception exception){
			msg = "Sales Executive to Beats assignment could not be processed successfully. Please contact System Administrator.";
		}
		return new ModelAndView("/assign_beats_conf", "msg", msg);
	}
	
	
	@GetMapping(value="/assignBeatEditForm/{manufCode}/{salesExecCode}") 
	public ModelAndView assignBeatToSalesExecEditForm(@PathVariable("manufCode") String manufCode, @PathVariable("salesExecCode") String salesExecCode){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//SalesExecutive salesExec = salesExecService.getSalesExecutive(salesExecID);
		List<Beat> beats = beatService.getTenantBeats(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		ManufacturerSalesExecBeats manufSalesExecBeats = manufacturerService.getManufSalesExecBeat(manufCode, salesExecCode, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		modelMap.put("beats", beats);
		modelMap.put("manufSalesExecBeats", manufSalesExecBeats);
		return new ModelAndView("/edit_assigned_beats", modelMap);
	}
	
	@PostMapping(value="/updateAssignedBeats") 
	public ModelAndView updateAssignedBeat(@ModelAttribute("manufSalesExecBeats") ManufacturerSalesExecBeats manufSalesExecBeats){
		String msg = "";
		try{
			int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
			salesExecService.updateAssignedBeats(tenantID, manufSalesExecBeats.getManufacturer().getManufacturerID(), manufSalesExecBeats.getSalesExecutive().getUserID(), manufSalesExecBeats.getBeatIDLists());
		}catch(Exception exception){
			msg = "Beats assigned to Sales Executive could not be updated successfully. Please contact System Administrator.";
			if(exception instanceof CRMException) {
				msg = exception.getMessage();
			}
		}
		return new ModelAndView("/update_assign_beats_conf", "msg", msg);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
		binder.registerCustomEditor(List.class, "beats", new CustomCollectionEditor(List.class) {
			@Override
			protected Object convertElement(Object element) {
				Beat beat = new Beat();
				beat.setBeatID(Integer.valueOf(String.valueOf(element)));

				return beat;
			}

		});
	}
}
