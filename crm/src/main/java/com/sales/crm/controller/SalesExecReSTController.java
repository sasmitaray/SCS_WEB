package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.crm.model.Beat;
import com.sales.crm.model.SalesExecutive;
import com.sales.crm.model.TrimmedCustomer;
import com.sales.crm.service.SalesExecService;

@RestController
@RequestMapping("/rest/salesExecReST")
public class SalesExecReSTController {
	
	@Autowired
	SalesExecService salesExecService;
	
	@Autowired
	HttpSession httpSession;
	
	
	@GetMapping(value="/beats/{salesExecID}/{tenantID}")
	public List<Beat> getSalesExecsBeats(@PathVariable("salesExecID") int salesExecID, @PathVariable("tenantID") int tenantID){
		return salesExecService.getAssignedBeats(salesExecID, tenantID);
	}
	
	@GetMapping(value="/scheduledVisit/{salesExecID}/{visitDate}/{tenantID}")
	public List<Beat> getSalesExecsBeatsScheduledVisit(@PathVariable("salesExecID") int salesExecID, @PathVariable("visitDate") String visitDate, @PathVariable("tenantID") int tenantID){
		Date date = new Date();
		try{
			date = new SimpleDateFormat("dd-MM-yyyy").parse(visitDate);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		
		return salesExecService.getScheduledVisitSalesExecBeats(salesExecID, date, tenantID);
	}
	
	
	@GetMapping(value="/list/{visitDate}/{tenantID}")
	public List<SalesExecutive> getScheduledVisitSalesExecs(@PathVariable("visitDate") String visitDate, @PathVariable("tenantID") int tenantID){
		Date date = new Date();
		try{
			date = new SimpleDateFormat("dd-MM-yyyy").parse(visitDate);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		return salesExecService.getScheduledVisitSalesExecs(date, tenantID);
	}

	
	@GetMapping(value="/scheduledVisit/{salesExecID}/{visitDate}/{beatID}/{tenantID}")
	public List<TrimmedCustomer> getScheduledVisitBeatCustomers(@PathVariable("salesExecID") int salesExecID,
			@PathVariable("visitDate") String visitDate, @PathVariable("beatID") int beatID,
			@PathVariable("tenantID") int tenantID) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("dd-MM-yyyy").parse(visitDate);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return salesExecService.getScheduledVisitBeatCustomers(salesExecID, date, beatID, tenantID);
	}
	
	@GetMapping(value="/salesExecsNotMappedToManufacturer/{manufacturerID}")
	public List<SalesExecutive> getSalesExecsNotMappedToManufacturer(@PathVariable("manufacturerID") int manufacturerID){
		return salesExecService.getSalesExecsNotMappedToManufacturer(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))), manufacturerID);
	}

	
	@GetMapping(value="/manufacturerParams/{salesExecID}/{tenantID}")
	public String getManufacturerParams(@PathVariable("salesExecID") int salesExecID, @PathVariable("tenantID") int tenantID){
		Map<Integer, String> attributeMap = salesExecService.getManufacturerParams(salesExecID, tenantID);
		JSONArray array = new JSONArray();
		for(Map.Entry<Integer, String> entry: attributeMap.entrySet()) {
			JSONObject obj = new JSONObject();
			obj.put("id",entry.getKey());
			obj.put("name", entry.getValue());
			array.put(obj);
		}
		return array.toString();
	}
}
