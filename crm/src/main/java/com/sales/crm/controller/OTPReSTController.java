package com.sales.crm.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.crm.exception.CRMException;
import com.sales.crm.exception.ErrorCodes;
import com.sales.crm.model.CustomerOTP;
import com.sales.crm.model.ReSTResponse;
import com.sales.crm.model.User;
import com.sales.crm.service.OTPService;

@RestController
@RequestMapping("/rest/otpReST")
public class OTPReSTController {

	@Autowired
	OTPService otpService;

	@Autowired
	HttpSession session;

	@PutMapping(value = "/generate/{customerID}/{otpType}/{orderRefID}/{tenantID}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ReSTResponse> generateOTP(@PathVariable("customerID") int customerID,
			@PathVariable("otpType") int otpType, @PathVariable("orderRefID") int orderRefID, @PathVariable("tenantID") int tenantID) {
		ReSTResponse response = new ReSTResponse();
		User user = (User) session.getAttribute("user");
		CustomerOTP customerOTP = new CustomerOTP();
		customerOTP.setCustomerID(customerID);
		customerOTP.setTenantID(tenantID);
		customerOTP.setFieldExecID(user.getUserID());
		customerOTP.setOtpType(otpType);
		customerOTP.setEntityID(orderRefID);
		try {
			otpService.generateOTP(customerOTP, tenantID); 
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			if(exception instanceof CRMException){
				response.setStatus(ReSTResponse.STATUS_FAILURE);
				response.setErrorCode(((CRMException)exception).getErrorCode());
				response.setErrorMsg(((CRMException)exception).getMessage());
			}else{
				response.setStatus(ReSTResponse.STATUS_FAILURE);
				response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
				response.setErrorMsg("Something is not right ! Please contact System Administrator");
			}
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@PutMapping(value = "/verify/{customerID}/{otpType}/{otp}/{tenantID}")
	public ResponseEntity<ReSTResponse> verifyOTP(@PathVariable("customerID") int customerID,
			@PathVariable("otpType") int otpType, @PathVariable("otp") String otp, @PathVariable("tenantID") int tenantID) throws Exception {
		ReSTResponse response = new ReSTResponse();
		try {
			otpService.verifyOTP(customerID, otpType, otp, tenantID);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
		} catch (Exception exception) {
			if(exception instanceof CRMException){
				response.setStatus(ReSTResponse.STATUS_FAILURE);
				response.setErrorCode(((CRMException)exception).getErrorCode());
				response.setErrorMsg(((CRMException)exception).getMessage());
			}else{
				response.setStatus(ReSTResponse.STATUS_FAILURE);
				response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
				response.setErrorMsg("Something is not right ! Please contact System Administrator");
			}
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
			
	}

}
