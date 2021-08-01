package com.sales.crm.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.crm.model.SalesExecutive;
import com.sales.crm.service.ManufacturerService;

@RestController
@RequestMapping("/rest/manufacturerReST")
public class ManufacturerReSTController {
	
	@Autowired
	ManufacturerService manufacturerService;
	
	
	@Autowired
	HttpSession httpSession;
	
	@GetMapping(value="/salesExecs/{manufacturerID}/{tenantID}")
	public List<SalesExecutive> getManufacturerSalesExecs(@PathVariable("manufacturerID") int manufacturerID, @PathVariable("tenantID") int tenantID){
		return manufacturerService.getManufacturerById(manufacturerID, tenantID).getSalesExecs();
	}
	

}
