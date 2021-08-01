package com.sales.crm.dao;

import java.util.List;

import com.sales.crm.model.SecurityQuestion;
import com.sales.crm.model.Tenant;
import com.sales.crm.model.User;

public interface UserDAO {
	
	void create(User user) throws Exception;
	
	User get(int userID);
	
	void update(User user) throws Exception;
	
	void delete(int userID, int tenantID);
	
	List<User> getTenantUsers(int tenantID, int loggedInUserID);
	
	List<User> getUserByRole(int tenantID, int roleID);
	
	Tenant getUserTenant(int userId);
	
	User getUser(String userName) throws Exception;
	
	boolean validateUserCredential(String userName, String password);
	
	int[] isUserNameEmailIDPresent(String userName, String email);
	
	void updatePassword(User user) throws Exception;
	
	List<SecurityQuestion> getAllSecurityQuestions();
	
	//void updateFirstLoginPassword(User user);
}
