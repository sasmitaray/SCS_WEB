package com.sales.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.crm.model.Reseller;
import com.sales.crm.service.ResellerService;

@RestController
@RequestMapping("/rest/reseller")
public class ResellerReSTController {
	
	@Autowired
	ResellerService resellerService;
	
	/**
	@PutMapping
	public ResponseEntity<Reseller> create(@RequestBody Reseller reseller){
		try {
			resellerService.createReseller(reseller);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<Reseller>(reseller, HttpStatus.CREATED);
	}
	*/
	
	@GetMapping(value="/{resellerID}")
	public Reseller get(@PathVariable int resellerID){
		return resellerService.getReseller(resellerID);
	}
	
	/**
	@PostMapping
	public ResponseEntity<Reseller> update(@RequestBody Reseller reseller){
		resellerService.updateReseller(reseller);
		return new ResponseEntity<Reseller>(reseller, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/{resellerID}")
	public void delete(@PathVariable int resellerID){
		resellerService.deleteReseller(resellerID);
	}
	*/
	
}
