package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sales.crm.exception.ErrorCodes;
import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.Order;
import com.sales.crm.model.OrderBookingSchedule;
import com.sales.crm.model.OrderBookingStats;
import com.sales.crm.model.ReSTResponse;
import com.sales.crm.model.ScheduledOrderSummary;
import com.sales.crm.service.OrderService;

@RestController
@RequestMapping("/rest/orderReST")
public class OrderReSTController {

	private static Logger logger = Logger.getLogger(OrderReSTController.class);

	@Autowired
	HttpSession session;

	@Autowired
	OrderService orderService;

	@PostMapping(value = "/create")
	public ResponseEntity<ReSTResponse> createOrder(@RequestBody Order order) {
		ReSTResponse response = new ReSTResponse();
		int tenantID = (Integer) session.getAttribute("tenantID");
		int orderID = -1;
		try {
			order.setTenantID(tenantID);
			orderID = orderService.create(order);
			response.setBusinessEntityID(orderID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/createWithOTP/{otp}")
	public ResponseEntity<ReSTResponse> createOrderWithOTP(@RequestBody Order order, @PathVariable("otp") String otp) {
		ReSTResponse response = new ReSTResponse();
		int orderID = -1;
		try {
			orderID = orderService.createWithOTP(order, otp);
			response.setBusinessEntityID(orderID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/list")
	public ResponseEntity<ReSTResponse> getOrderList() {
		ReSTResponse response = new ReSTResponse();
		int tenantID = (Integer) session.getAttribute("tenantID");
		try {
			List<Order> orders = orderService.getOrders(tenantID, -1);
			response.setBusinessEntities(orders);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/orderBookingStatsToday/{salesExecID}/{tenantID}")
	public ResponseEntity<ReSTResponse> getOrderBookingStatsForToday(@PathVariable("salesExecID") int salesExecID,
			@PathVariable("tenantID") int tenantID) {
		ReSTResponse response = new ReSTResponse();
		List<OrderBookingStats> orderBookingStats = new ArrayList<OrderBookingStats>();
		try {
			orderBookingStats.add(orderService.getOrderBookingStats(salesExecID, new Date(), tenantID));
			response.setBusinessEntities(orderBookingStats);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
		}

		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);

	}

	@GetMapping(value = "/unscheduleOrderBooking/{orderScheduleID}/{customerID}/{tenantID}")
	public ResponseEntity<ReSTResponse> unscheduleOrderBooking(@PathVariable("orderScheduleID") int orderScheduleID,
			@PathVariable("customerID") int customerID, @PathVariable("tenantID") int tenantID) {
		ReSTResponse response = new ReSTResponse();
		try {
			orderService.unScheduleOrderBooking(orderScheduleID, customerID, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Error while removed order booked schedule", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled order booking could not be cancelled successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/scheduledVisit/{salesExecID}/{visitDate}/{beatID}/{tenantID}")
	public ResponseEntity<ReSTResponse> getOrdersBookingSchedules(@PathVariable("salesExecID") int salesExecID,
			@PathVariable("visitDate") String visitDateStr, @PathVariable("beatID") int beatID,
			@PathVariable("tenantID") int tenantID) {
		List<OrderBookingSchedule> orderBookingSchedules;
		ReSTResponse response = new ReSTResponse();
		try {
			Date visitDate = null;
			// Hack by passing -
			if (visitDateStr != null && !visitDateStr.equals("-")) {
				visitDate = new SimpleDateFormat("dd-MM-yyyy").parse(visitDateStr);
			} else {
				visitDate = new Date();
			}
			orderBookingSchedules = orderService.getOrdersBookingSchedules(tenantID, salesExecID, beatID, visitDate);
			response.setBusinessEntities(orderBookingSchedules);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Not able to fetch scheduled order booking list.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled order booking could not be fetched successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	
	@PostMapping("/scheduledorders/search")
	public ResponseEntity<ReSTResponse> searchScheduledOrders(@RequestBody ObjectNode objectNode) {
		Map<String, Object> filters = new HashMap<String, Object>();
		ReSTResponse response = new ReSTResponse();
		try {
			String date = objectNode.get("date").asText();
			if (!(date == null || date.equals(""))) {
				filters.put("date", new SimpleDateFormat("dd-MM-yyyy").parse(date));
			}
			int salesExecID = objectNode.get("salesExecID").asInt();
			if (salesExecID != -1) {
				filters.put("salesExecID", salesExecID);
			}
			int beatID = objectNode.get("beatID").asInt();
			if (beatID != -1) {
				filters.put("beatID", beatID);
			}
			int customerID = objectNode.get("customerID").asInt();
			if (customerID != -1) {
				filters.put("customerID", customerID);
			}
			int tenantID = objectNode.get("tenantID").asInt();
			List<OrderBookingSchedule> orderBookingSchedules = orderService.searchScheduledOrders(filters, tenantID);
			response.setBusinessEntities(orderBookingSchedules);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Not able to fetch scheduled order booking list.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled order booking could not be fetched successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/orderScheduleReport/{salesExecID}/{beatID}/{visitDate}/{custID}/{status}/{tenantID}")
	public ResponseEntity<ReSTResponse> getOrderScheduleReport(@PathVariable("salesExecID") int salesExecID,
			@PathVariable("beatID") int beatID, @PathVariable("visitDate") String visitDateStr,
			@PathVariable("custID") int custID, @PathVariable("status") int status, @PathVariable("tenantID") int tenantID) {
		List<OrderBookingSchedule> orderBookingSchedules = new ArrayList<OrderBookingSchedule>();
		ReSTResponse response = new ReSTResponse();
		try {
			Date visitDate = null;
			// Hack by passing -
			if (visitDateStr != null && !visitDateStr.equals("-")) {
				visitDate = new SimpleDateFormat("dd-MM-yyyy").parse(visitDateStr);
			} else {
				visitDate = new Date();
			}
			orderBookingSchedules = orderService.getOrderScheduleReport(tenantID, salesExecID, beatID, custID, -1,
					status, visitDate);
			response.setBusinessEntities(orderBookingSchedules);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Not able to fetch order schedule report data.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled order booking could not be fetched successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);

	}

	@GetMapping(value = "/dashboardOrderScheduleSummary/{visitDate}/{salesExecID}")
	public ResponseEntity<ReSTResponse> getDashboardOrderScheduleSummary(@PathVariable("salesExecID") int salesExecID,
			@PathVariable("visitDate") String visitDateStr) {
		List<ScheduledOrderSummary> scheduledOrderSummaries = new ArrayList<ScheduledOrderSummary>();
		ReSTResponse response = new ReSTResponse();
		try {
			Date visitDate = null;
			// Hack by passing -
			if (visitDateStr != null && !visitDateStr.equals("-")) {
				visitDate = new SimpleDateFormat("dd-MM-yyyy").parse(visitDateStr);
			} else {
				visitDate = new Date();
			}
			int tenantID = (Integer) session.getAttribute("tenantID");
			scheduledOrderSummaries = orderService.getScheduledOrderSummary(tenantID, salesExecID, visitDate);
			response.setBusinessEntities(scheduledOrderSummaries);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Not able to fetch order schedule report data.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled order booking could not be fetched successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);

	}
	
	@PostMapping(value="/delete")
	public ResponseEntity<ReSTResponse>  deleteOrder(@RequestBody ObjectNode objectNode){
		ReSTResponse response = new ReSTResponse();
		int orderID = -1;
		try{
			orderID = objectNode.get("orderID").asInt();
			String remark = objectNode.get("remark").asText();
			int tenantID = objectNode.get("tenantID").asInt();
			orderService.deleteOrder(orderID, remark, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		}catch(Exception exception){
			logger.error("Error while deleting order  "+ orderID, exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Something is not right ! Please contact System Administrator");
			return new ResponseEntity<ReSTResponse>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/unscheduleOrderDelivery/{deliverySchedID}/{tenantID}")
	public ResponseEntity<ReSTResponse> unscheduleOrderDelivery(@PathVariable("deliverySchedID") int deliverySchedID,
			@PathVariable("tenantID") int tenantID) {
		ReSTResponse response = new ReSTResponse();
		try {
			orderService.unScheduleOrderDelivery(deliverySchedID, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Error while removed order delivery schedule", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled order delivery could not be cancelled successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}
	
	@PostMapping("/scheduledOrderDeliveries/search")
	public ResponseEntity<ReSTResponse> searchScheduledOrderDeliveries(@RequestBody ObjectNode objectNode) {
		Map<String, Object> filters = new HashMap<String, Object>();
		ReSTResponse response = new ReSTResponse();
		try {
			String date = objectNode.get("date").asText();
			if (!(date == null || date.equals(""))) {
				filters.put("date", new SimpleDateFormat("dd-MM-yyyy").parse(date));
			}
			int delivExecID = objectNode.get("delivExecID").asInt();
			if (delivExecID != -1) {
				filters.put("delivExecID", delivExecID);
			}
			int beatID = objectNode.get("beatID").asInt();
			if (beatID != -1) {
				filters.put("beatID", beatID);
			}
			int customerID = objectNode.get("customerID").asInt();
			if (customerID != -1) {
				filters.put("customerID", customerID);
			}
			int tenantID = objectNode.get("tenantID").asInt();
			List<DeliveryBookingSchedule> deliveryBookingSchedules = orderService.searchScheduledOrderDeliveries(filters, tenantID);
			response.setBusinessEntities(deliveryBookingSchedules);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			logger.error("Not able to fetch scheduled delivery booking list.", exception);
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg(
					"Scheduled delivery booking could not be fetched successfully. Please try after sometime and if error persists, contact System Administrator");
			return new ResponseEntity<ReSTResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

}
