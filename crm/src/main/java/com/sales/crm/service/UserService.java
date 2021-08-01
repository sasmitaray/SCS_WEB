package com.sales.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.UserDAO;
import com.sales.crm.model.SecurityQuestion;
import com.sales.crm.model.Tenant;
import com.sales.crm.model.User;

@Service("userService")
public class UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	public User getUser(int userID){
		return userDAO.get(userID);
	}
	
	public User getUser(String userName) throws Exception{
		return userDAO.getUser(userName);
	}
	
	public void createUser(User user) throws Exception{
		//user.setPassword(saltPassword(user.getPassword()));
		userDAO.create(user);
	}
	
	public void updateUser(User user) throws Exception{
		userDAO.update(user);
	}
	
	public void deleteUser(int userID, int tenantID){
		userDAO.delete(userID, tenantID);
	}
	
	public List<User> getTenantUsers(int tenantID, int loggedInUserID){
		return userDAO.getTenantUsers(tenantID, loggedInUserID);
	}
	
	public Tenant getUserTenant(int userId){
		return userDAO.getUserTenant(userId);
	}
	
	public boolean validateUserCredential(String userName, String password){
		return userDAO.validateUserCredential(userName, password);
	}
	
	public int[] isUserNameEmailIDPresent(String userName, String email){
		return userDAO.isUserNameEmailIDPresent(userName, email);
	}
	
	public void updatePassword(User user) throws Exception{
		//user.setNewPassword(saltPassword(user.getNewPassword()));
		userDAO.updatePassword(user);
	}
	
	public List<SecurityQuestion> getAllSecurityQuestions(){
		return userDAO.getAllSecurityQuestions();
	}
	
	/**
	public void updateFirstLoginPassword(User user){
		userDAO.updateFirstLoginPassword(user);
	}
	**/
	
	public List<User> getUsersByRole(int tenantID, int roleID){
		return userDAO.getUserByRole(tenantID, roleID);
	}
	
	//private String saltPassword(String orgPassword) {
	//	return BCrypt.hashpw(orgPassword, BCrypt.gensalt(12));
	//}
	
}
