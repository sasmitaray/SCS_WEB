package com.sales.crm.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.crm.model.Beat;
import com.sales.crm.model.TrimmedCustomer;
import com.sales.crm.service.BeatService;

@RestController
@RequestMapping("/rest/beatReST")
public class BeatRESTController {

	@Autowired
	BeatService beatService;

	@Autowired
	HttpSession httpSession;

	@GetMapping(value = "/{beatID}/{tenantID}")
	public List<TrimmedCustomer> getBeatCustomers(@PathVariable("beatID") int beatID,
			@PathVariable("tenantID") int tenantID) {
		return beatService.getBeatCustomers(beatID, tenantID);
	}

	/**
	@GetMapping(value = "/beatsNotMappedToCustomer/{customerID}/{tenantID}")
	public List<Beat> getBeatsNotMappedToCustomer(@PathVariable("customerID") int customerID,
			@PathVariable("tenantID") int tenantID) {
		return beatService.getBeatsNotMappedToCustomer(customerID, tenantID);
	}
	*/

	@GetMapping(value = "/beatsNotMappedToSalesExec/{manufacturerID}/{salesExecID}/{tenantID}")
	public List<Beat> getBeatsNotMappedToSalesExec(@PathVariable("manufacturerID") int manufacturerID,
			@PathVariable("salesExecID") int salesExecID, @PathVariable("tenantID") int tenantID) {
		return beatService.getBeatsNotMappedToSalesExec(tenantID, manufacturerID, salesExecID);
	}
	
	@GetMapping(value = "/beatsNotMappedToManufacturer/{manufacturerID}/{tenantID}")
	public List<Beat> getBeatsNotMappedToManufacturer(@PathVariable("manufacturerID") int manufacturerID,
			@PathVariable("tenantID") int tenantID) {
		return beatService.getBeatsNotMappedToManufacturer(manufacturerID, tenantID);
	}
	
	@GetMapping(value = "/beatsNotMappedToDelivExec/{delivExecID}/{tenantID}")
	public List<Beat> getBeatsNotMappedToDelivExec(
			@PathVariable("delivExecID") int delivExecID, @PathVariable("tenantID") int tenantID) {
		return beatService.getBeatsNotMappedToDelivExec(delivExecID, tenantID);
	}

}
