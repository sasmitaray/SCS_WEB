package com.sales.crm.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sales.crm.model.CustomerOTP;
import com.sales.crm.service.OTPService;

@Controller
@RequestMapping("/web/otpWeb")
public class OTPWebController {

	@Autowired
	OTPService otpService;
	
	@Autowired
	HttpSession httpSession;
	
	@GetMapping(value="/report")
	public ModelAndView getOTPReport(){
		List<CustomerOTP> customerOTPs = otpService.getOTPReport(Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		return new ModelAndView("/otp_report", "customerOTPs", customerOTPs);
		
	}
	
	@GetMapping(value="/delete/{otpID}")
	public ModelAndView deleteOTP(@PathVariable int otpID){
		try{
			otpService.deleteOTP(otpID, Integer.parseInt(String.valueOf(httpSession.getAttribute("tenantID"))));
		}catch(Exception exception){
			
		}
		return new ModelAndView("redirect:/web/otpWeb/report");
		
	}
	
	
}
