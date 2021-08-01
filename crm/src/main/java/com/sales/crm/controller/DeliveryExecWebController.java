package com.sales.crm.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.sales.crm.model.DeliveryBookingSchedule;
import com.sales.crm.model.DeliveryExecutive;
import com.sales.crm.model.TrimmedCustomer;
import com.sales.crm.service.BeatService;
import com.sales.crm.service.CustomerService;
import com.sales.crm.service.DeliveryExecService;
import com.sales.crm.service.OrderService;

@Controller
@RequestMapping("/web/deliveryExecWeb")
public class DeliveryExecWebController {
	
	@Autowired
	private DeliveryExecService deliveryExecService;
	
	@Autowired
	HttpSession httpSession;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	private BeatService beatService;
	
	@Autowired
	private OrderService orderService;

	@GetMapping(value="/scheduledDeliveryBookings")
	public ModelAndView getScheduledDeliveryBookingsList(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<DeliveryExecutive> delivExecs = deliveryExecService.getActiveDeliveryExecutives(tenantID);
		List<Beat> beats = beatService.getTenantBeats(tenantID);
		List<TrimmedCustomer> customers = customerService.getTenantTrimmedCustomers(tenantID);
		List<DeliveryBookingSchedule> deliveriesBookedForToday = orderService.getAllOrderDeliveryBookedForToday(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("delivExecs", delivExecs);
		modelMap.put("deliveryBookingSchedule", new DeliveryBookingSchedule());
		modelMap.put("beats", beats);
		modelMap.put("customers", customers);
		modelMap.put("deliveriesBookedForToday", deliveriesBookedForToday);
		modelMap.put("tenantID", tenantID);
		return new ModelAndView("/delivery_booking_list", modelMap);
	}
	
	@GetMapping(value="/beatlist")
	public ModelAndView salesExecBeatsList(){
		List<DeliveryExecutive> delivExecs = deliveryExecService.getDelivExecutivesHavingBeatsAssigned(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/delivexec_beats_list","delivExecs", delivExecs);  
	}
	
	@GetMapping(value="/assignBeatForm") 
	public ModelAndView assignBeatToSalesExecForm(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		Map<String, Object> modelMap = new HashMap<String, Object>();
		List<DeliveryExecutive> delivExecs = deliveryExecService.getActiveDeliveryExecutives(tenantID);
		//List<Beat> beats = beatService.getTenantBeats(tenantID) ;
		modelMap.put("delivExecs", delivExecs);
		modelMap.put("tenantID", tenantID);
		modelMap.put("delivExec", new DeliveryExecutive());
		return new ModelAndView("/delivExec_assign_beats", modelMap);
	}
	
	@PostMapping(value="/assignBeat") 
	public ModelAndView assignBeatToSalesExec(@ModelAttribute("delivExec") DeliveryExecutive delivExec) {
		String msg = "";
		try {
			deliveryExecService.assignBeats(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))),
					delivExec.getUserID(), delivExec.getBeatIDLists());
		} catch (Exception exception) {
			msg = "Delivery Executive to Beats assignment could not be processed successfully. Please contact System Administrator.";
		}
		return new ModelAndView("/delivery_exec_assign_beats_conf", "msg", msg);
	}
	
	@GetMapping(value="/assignBeatEditForm/{delivExecCode}") 
	public ModelAndView assignBeatToSalesExecEditForm(@PathVariable String delivExecCode){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		Map<String, Object> modelMap = new HashMap<String, Object>();
		DeliveryExecutive delivExec = deliveryExecService.getDelivExecutiveByCode(delivExecCode, tenantID);
		List<Beat> beats = beatService.getTenantBeats(tenantID);
		modelMap.put("delivExec", delivExec);
		modelMap.put("beats", beats);
		return new ModelAndView("/edit_assigned_beats_to_delivery_exec", modelMap);
	}
	
	@PostMapping(value="/updateAssignedBeats") 
	public ModelAndView updateAssignedBeat(@ModelAttribute("delivExec") DeliveryExecutive delivExec){
		String msg = "";
		try{
			deliveryExecService.updateAssignedBeats(delivExec.getTenantID(), delivExec.getUserID(), delivExec.getBeatIDLists());
		}catch(Exception exception){
			msg = "Beats assigned to Delivery Executive could not be updated successfully. Please contact System Administrator.";
			if(exception instanceof CRMException) {
				msg = exception.getMessage();
			}
		}
		return new ModelAndView("/update_delivery_exec_beats_conf", "msg", msg);
	}
	
	@GetMapping(value="/deleteBeatsAssignment/{delivExecID}")
	public ModelAndView deleteBeatAssignment(@PathVariable int delivExecID){
		String msg = "";
		try{
			deliveryExecService.deleteBeatAssignment(delivExecID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Beats associated to Delivery Executive could not be removed successfully. Please contact System Administrator.";
		}
		return new ModelAndView("/remove_deliv_exec_assign_beats_conf","msg", msg); 
	}
	
	@GetMapping(value="/scheduleDeliveryBookingForm")
	public ModelAndView getScheduleOrderBookingForm(){
		int tenantID = Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID")));
		List<DeliveryExecutive> delivExecs = deliveryExecService.getActiveDeliveryExecutives(tenantID);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("delivExecs", delivExecs);
		modelMap.put("deliveryBookingSchedule", new DeliveryBookingSchedule());
		modelMap.put("tenantID", tenantID);
		return new ModelAndView("/schedule_delivery_booking", modelMap);
	}
	
	@PostMapping(value="/scheduleDeliveryBooking") 
	public ModelAndView scheduleOrderBooking(HttpServletRequest request, @ModelAttribute("deliveryBookingSchedule") DeliveryBookingSchedule deliveryBookingSchedule){
		String msg = "";
		try{
			Map<Integer, List<String>> customerOrderMap = new HashMap<Integer, List<String>>();
			for(int customerID : deliveryBookingSchedule.getCustomerIDs()){
				if(request.getParameter(String.valueOf(customerID)) != null
						&& !(request.getParameter(String.valueOf(customerID)).trim().isEmpty())){
					String[] orderIDs = ((String)request.getParameter(String.valueOf(customerID))).split("-");
					ArrayList<String> orderIDsList = new ArrayList<String>();
					for(String orderID : orderIDs){
						orderIDsList.add(orderID);
					}
					customerOrderMap.put(customerID, orderIDsList);
				}
			}
			deliveryBookingSchedule.setCustomerOrderMap(customerOrderMap);
			deliveryBookingSchedule.setTenantID(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Scheduling of order delivery could not be processed successfully, please contact the System Administrator.";
		}
		
		if(msg.equals("")){
			try{
				deliveryExecService.scheduleDeliveryBooking(deliveryBookingSchedule);
			}catch(Exception exception){
				msg = "Scheduling of order delivery could not be processed successfully, please contact the System Administrator.";
			}
		}
		return new ModelAndView("/delivery_booking_schedule_conf", "msg", msg);
	}
	
	/**
	@PostMapping(value="/unscheduleDeliveryBooking") 
	public ModelAndView unscheduleDeliveryBooking(@ModelAttribute("deliveryBookingSchedule") DeliveryBookingSchedule deliveryBookingSchedule){
		String msg = "";
		try{
			deliveryExecService.unscheduleDeliveryBooking(deliveryBookingSchedule.getCustomerIDs(), deliveryBookingSchedule.getVisitDate(), Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			msg = "Customer visits could not be cancelled successfully. Please try after sometine and if error persists contact System Administrator.";
		}
		return new ModelAndView("/scheduled_delivery_cancel_conf", "msg", msg);
	}
	**/
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
		
	}
	
}
