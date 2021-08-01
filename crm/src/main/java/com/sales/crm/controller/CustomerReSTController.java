package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sales.crm.exception.ErrorCodes;
import com.sales.crm.model.Customer;
import com.sales.crm.model.CustomerOrder;
import com.sales.crm.model.ReSTResponse;
import com.sales.crm.model.TrimmedCustomer;
import com.sales.crm.service.CustomerService;

@RestController
@RequestMapping("/rest/customer")
public class CustomerReSTController {

	private static Logger logger = Logger.getLogger(CustomerReSTController.class);

	@Autowired
	CustomerService customerService;

	@Autowired
	HttpSession httpSession;

	@GetMapping(value = "/scheduledCustomers/{salesExecID}/{tenantID}")
	public ResponseEntity<ReSTResponse> scheduledTrimmedCustomerslist(@PathVariable("salesExecID") int salesExecID,
			@PathVariable("tenantID") int tenantID) {
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>();
		ReSTResponse response = new ReSTResponse();
		try {
			customers = customerService.scheduledTrimmedCustomerslist(salesExecID, new Date(), tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
			response.setBusinessEntities(customers);
		} catch (Exception exception) {
			logger.error("Fetching sheduled customers failed.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	// Called from mobile APP to list customers for delivery
	@GetMapping(value = "/scheduledCustomersForDelivery/{delivExecID}/{tenantID}")
	public ResponseEntity<ReSTResponse> scheduledTrimmedCustomerslistForDeliveryToday(
			@PathVariable("delivExecID") int delivExecID, @PathVariable("tenantID") int tenantID) {
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>();
		ReSTResponse response = new ReSTResponse();
		try {
			customers = customerService.scheduledTrimmedCustomerslistForDeliveryToday(delivExecID, new Date(),
					tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
			response.setBusinessEntities(customers);
		} catch (Exception exception) {
			logger.error("Fetching cstomers for delivery has failed.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/toSchedule")
	public List<TrimmedCustomer> getCustomersToSchedule(@RequestBody ObjectNode objectNode) {
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>();
		try {
			List<Integer> manufacturerIDs = new ArrayList<Integer>();
			int salesExecID = objectNode.get("salesExecID").asInt();
			int beatID = objectNode.get("beatID").asInt();
			int tenantID = objectNode.get("tenantID").asInt();
			String visitDate = objectNode.get("date").asText();
			Date date = new SimpleDateFormat("dd-MM-yyyy").parse(visitDate);
			if(objectNode.get("manufIDs") instanceof ArrayNode) {
				Iterator<JsonNode> itr = objectNode.get("manufIDs").elements();
				while(itr.hasNext()) {
					manufacturerIDs.add(itr.next().asInt());
				}
			}else {
				manufacturerIDs.add(objectNode.get("manufIDs").asInt());
			}
					
			customers = customerService.getCustomersToSchedule(salesExecID, beatID, date, manufacturerIDs, tenantID);
		} catch (Exception exception) {
			logger.error("Error while fetching customers to schedule visit.", exception);
		}
		return customers;
	}

	@PostMapping(value = "/customersToScheduleDelivery")
	public List<CustomerOrder> getCustomersToScheduleDelivery(@RequestBody ObjectNode objectNode) {
		List<CustomerOrder> customerOrders = new ArrayList<CustomerOrder>();
		try {
			List<Integer> manufacturerIDs = new ArrayList<Integer>();
			int beatID = objectNode.get("beatID").asInt();
			int tenantID = objectNode.get("tenantID").asInt();
			String visitDate = objectNode.get("date").asText();
			Date date = new SimpleDateFormat("dd-MM-yyyy").parse(visitDate);
			if(objectNode.get("manufIDs") instanceof ArrayNode) {
				Iterator<JsonNode> itr = objectNode.get("manufIDs").elements();
				while(itr.hasNext()) {
					manufacturerIDs.add(itr.next().asInt());
				}
			}else {
				manufacturerIDs.add(objectNode.get("manufIDs").asInt());
			}
			
			customerOrders = customerService.getCustomersToScheduleDelivery(beatID, date, manufacturerIDs, tenantID);
		} catch (Exception exception) {
			logger.error("Error while fetching customers to schedule delivery.", exception);
		}
		return customerOrders;
	}

	@GetMapping(value = "/customersForOTPVerification/{fieldExecID}/{otpType}/{tenantID}")
	public ResponseEntity<ReSTResponse> getCustomerForOTPVerification(@PathVariable("fieldExecID") int fieldExecID,
			@PathVariable("otpType") int otpType, @PathVariable("tenantID") int tenantID) {
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>();
		ReSTResponse response = new ReSTResponse();
		try {
			customers = customerService.getCustomerForOTPVerification(fieldExecID, otpType, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
			response.setBusinessEntities(customers);
		} catch (Exception exception) {
			logger.error("Fetchingh customers for OTP verification has failed.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/search/{filterParam}/{filterValue}")
	public ResponseEntity<ReSTResponse> search(@PathVariable("filterParam") String filterParam,
			@PathVariable("filterValue") Object filterValue) {
		List<Customer> customers = new ArrayList<Customer>();
		ReSTResponse response = new ReSTResponse();
		try {
			Map<String, Object> filterMap = new HashMap<String, Object>();
			filterMap.put(filterParam, filterValue);
			customers = customerService.search(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))),
					filterMap);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
			response.setBusinessEntities(customers);
		} catch (Exception exception) {
			logger.error("Customer search failed with error.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
			return new ResponseEntity<ReSTResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping(value="/activate")
	public ResponseEntity<ReSTResponse> activateCustomer(@RequestBody ObjectNode objectNode){
		ReSTResponse response = new ReSTResponse();
		int customerID = -1;
		try{
			customerID = objectNode.get("customerID").asInt();
			String remark = objectNode.get("remark").asText();
			int tenantID = objectNode.get("tenantID").asInt();
			customerService.activateCustomer(customerID, remark, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		}catch(Exception exception){
			logger.error("Error while activating customer "+ customerID, exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
			return new ResponseEntity<ReSTResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@PostMapping(value="/deactivate")
	public ResponseEntity<ReSTResponse>  deactivateCustomer(@RequestBody ObjectNode objectNode){
		ReSTResponse response = new ReSTResponse();
		int customerID = -1;
		try{
			customerID = objectNode.get("customerID").asInt();
			String remark = objectNode.get("remark").asText();
			int tenantID = objectNode.get("tenantID").asInt();
			customerService.deactivateCustomer(customerID, remark, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		}catch(Exception exception){
			logger.error("Error while deactivating customer "+ customerID, exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
			return new ResponseEntity<ReSTResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}
	
	@DeleteMapping(value="/delete")
	public ResponseEntity<ReSTResponse> delete(@RequestBody ObjectNode objectNode){
		ReSTResponse response = new ReSTResponse();
		int customerID = -1;
		int tenantID = -1;
		try{
			customerID = objectNode.get("customerID").asInt();
			tenantID = objectNode.get("tenantID").asInt();
			customerService.deleteCustomer(customerID, tenantID);
		}catch(Exception exception){
			logger.error("Error while deleting customer " +customerID, exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
			return new ResponseEntity<ReSTResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	
	@GetMapping(value = "/customersNotMappedToBeat/{tenantID}")
	public List<TrimmedCustomer> getCustomersNotMappedToBeat(
			@PathVariable("tenantID") int tenantID) {
		List<TrimmedCustomer> customers = new ArrayList<TrimmedCustomer>();
		try {
			customers = customerService.getCustomersNotMappedToBeat(tenantID);
		} catch (Exception exception) {
			logger.error("Fetching sheduled customers failed.", exception);
		}
		return customers;
	}
}
