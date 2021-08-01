package com.sales.crm.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.crm.exception.ErrorCodes;
import com.sales.crm.model.ReSTResponse;
import com.sales.crm.model.User;
import com.sales.crm.service.UserService;

@RestController
@RequestMapping("/rest/userReST")
public class UserReSTController {

	@Autowired
	UserService userService;

	@Autowired
	HttpSession session;

	@GetMapping(value = "/validateUser/{userName}/{password}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ReSTResponse> validateUser(@PathVariable("userName") String userName,
			@PathVariable("password") String password) {
		ReSTResponse response = new ReSTResponse();
		try{
			if(userService.validateUserCredential(userName, password)){
				User user = userService.getUser(userName);
				List<User> users = new ArrayList<User>();
				users.add(user);
				response.setBusinessEntities(users);
				response.setStatus(ReSTResponse.STATUS_SUCCESS);
			}else{
				response.setStatus(ReSTResponse.STATUS_FAILURE);
				response.setErrorCode(ErrorCodes.USER_INVALID);
				response.setErrorMsg("Invalid User Name or Password.");
			}
		}catch(Exception exception){
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Oops! Something is wrong. Please re-try after sometime and if error persists, contact System Administrator.");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/{userName}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ReSTResponse> getUserByUserName(@PathVariable("userName") String userName) {
		ReSTResponse response = new ReSTResponse();
		try{
			User user = userService.getUser(userName);
			response.setStatus(ReSTResponse.STATUS_SUCCESS);
			List<User> users = new ArrayList<User>();
			users.add(user);
			response.setBusinessEntities(users);
		}catch(Exception exception){
			response.setStatus(ReSTResponse.STATUS_FAILURE);
			response.setErrorCode(ErrorCodes.SYSTEM_ERROR);
			response.setErrorMsg("Oops! Something is wrong. Please re-try after sometime and if error persists, contact System Administrator.");
		}
		return new ResponseEntity<ReSTResponse>(response, HttpStatus.OK);
	}

}
