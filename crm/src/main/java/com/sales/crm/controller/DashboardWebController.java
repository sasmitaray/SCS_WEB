package com.sales.crm.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.model.SalesExecutive;
import com.sales.crm.model.ScheduledOrderSummary;
import com.sales.crm.service.BeatService;
import com.sales.crm.service.CustomerService;
import com.sales.crm.service.DeliveryExecService;
import com.sales.crm.service.OrderService;
import com.sales.crm.service.SalesExecService;
import com.sales.crm.service.ManufacturerService;

@Controller
@RequestMapping("/web/dashboardWeb")
public class DashboardWebController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	CustomerService customerService;
	
	
	@Autowired
	SalesExecService salesExecService;
	
	@Autowired
	BeatService beatService;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	ManufacturerService manufacturerService;
	
	@Autowired
	DeliveryExecService deliveryService;
	
	@GetMapping(value="/dashboard")
	public ModelAndView getDashboard(){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<ScheduledOrderSummary> scheduledOrderSummaries = new ArrayList<ScheduledOrderSummary>();
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		//Number Of Customers
		int numberOfCustomers = customerService.getCustomersCount(tenantID);
		//Number Of Manufacturer
		int numberOfManufacturers = manufacturerService.getManufacturersCount(tenantID);
		//Number of Beats
		int numberOfBeats = beatService.getBeatsCount(tenantID);
		//Number of SalesExecuties
		int numberOfSalesExecs = salesExecService.getSalesExecutiveCount(tenantID);
		//Number of Delivery Execs
		int numberOfDeliveryExecs = deliveryService.getDeliveryExecutiveCount(tenantID);
		scheduledOrderSummaries = orderService.getScheduledOrderSummary(tenantID, -1, new Date());
		List<SalesExecutive> salesExecs = salesExecService.getSalesExecutives(tenantID);
		modelMap.put("numberOfCustomers", numberOfCustomers);
		modelMap.put("numberOfManufacturers", numberOfManufacturers);
		modelMap.put("numberOfBeats", numberOfBeats);
		modelMap.put("numberOfSalesExecs", numberOfSalesExecs);
		modelMap.put("numberOfDeliveryExecs", numberOfDeliveryExecs);
		modelMap.put("scheduledOrderSummaries", scheduledOrderSummaries);
		modelMap.put("salesExecs", salesExecs);
		return new ModelAndView("/dashboard", modelMap);
		
	}

}
