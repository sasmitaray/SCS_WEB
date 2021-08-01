package com.sales.crm.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Role;
import com.sales.crm.model.SecurityQuestion;
import com.sales.crm.model.Tenant;
import com.sales.crm.model.User;

@Repository("userDAO")
public class UserDAOImpl implements UserDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(UserDAOImpl.class);
	
	private static List<SecurityQuestion> securityQuestions;
	
	private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public void create(User user) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			user.setDateCreated(new Date());
			//If user name is empty set that to null
			if(user.getUserName() != null && user.getUserName().trim().isEmpty()){
				user.setUserName(null);
			}
			if(user.getEmailID() != null && user.getEmailID().trim().isEmpty()){
				user.setEmailID(null);
			}
			user.setCode(UUID.randomUUID().toString());
			user.setStatusID(EntityStatusEnum.NOT_APPLICABLE.getEntityStatus());
			// save user
			session.save(user);
			// create tenant_user
			SQLQuery createTenantUser = session.createSQLQuery("INSERT INTO TENANT_USER (TENANT_ID, USER_ID, DATE_CREATED) VALUES  (?, ?, CURDATE())");
			createTenantUser.setParameter(0, user.getTenantID());
			createTenantUser.setParameter(1, user.getUserID());
			createTenantUser.executeUpdate();
			// create user_role
			if (user.getRoleIDs() != null && user.getRoleIDs().size() > 0) {
				for (int roleID : user.getRoleIDs()) {
					SQLQuery createUserRole = session.createSQLQuery("INSERT INTO USER_ROLE (USER_ID, ROLE_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())");
					createUserRole.setParameter(0, user.getUserID());
					createUserRole.setParameter(1, roleID);
					createUserRole.setParameter(2, user.getTenantID());
					createUserRole.executeUpdate();
				}
			}
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while creating user", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@Override
	public User get(int userID) {

		Session session = null;
		User user = null;
		List<Role> roles = new ArrayList<Role>();
		try{
			session = sessionFactory.openSession();
			user = (User)session.get(User.class, userID);
			Set<Integer> roleIDs = new HashSet<Integer>();
			//Get Roles
			SQLQuery query = session.createSQLQuery("SELECT a.ID, a.ROLE_NAME, a.DESCRIPTION FROM ROLES a, USER_ROLE b WHERE a.ID=b.ROLE_ID AND b.USER_ID=? ");
			query.setParameter(0, user.getUserID());
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				Role role = new Role();
				role.setRoleID(Integer.valueOf(String.valueOf(objs[0])));
				role.setRoleName(String.valueOf(objs[1]));
				role.setDescription(String.valueOf(objs[2]));
				roles.add(role);
				roleIDs.add(role.getRoleID());
			}
			//Get Tenant ID
			SQLQuery tenantIDQuery = session.createSQLQuery("SELECT a.ID FROM TENANTS a, TENANT_USER b WHERE a.ID=b.TENANT_ID AND b.USER_ID= ?");
			tenantIDQuery.setParameter(0, userID);
			List ids = tenantIDQuery.list();
			if(ids != null && ids.size() == 1){
				user.setTenantID(Integer.valueOf(String.valueOf(ids.get(0))));
			}
			if(roles.size() > 0){
				user.setRoles(roles);
				user.setRoleIDs(roleIDs);
			}
		}catch(Exception exception){
			logger.error("Error while getting user", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return user;
	
	}

	@Override
	public void update(User user) throws Exception{

		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			user.setDateModified(new Date());
			session.update(user);
			//update user_role
			//TODO: Need to use update instead of delete and then insert
			if(user.getRoleIDs() != null && user.getRoleIDs().size() > 0){
				SQLQuery deleteUserRole = session.createSQLQuery("DELETE FROM USER_ROLE WHERE USER_ID= ?");
				deleteUserRole.setParameter(0, user.getUserID());
				deleteUserRole.executeUpdate();
				for(int roleID : user.getRoleIDs()){
					SQLQuery createUserRole = session.createSQLQuery("INSERT INTO USER_ROLE (USER_ID, ROLE_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())");
					createUserRole.setParameter(0, user.getUserID());
					createUserRole.setParameter(1, roleID);
					createUserRole.setParameter(2, user.getTenantID());
					createUserRole.executeUpdate();
				}
			}
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while updating user", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	
		
	}

	@Override
	public void delete(int userID, int tenantID) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			User user = (User)session.get(User.class, userID);
			transaction = session.beginTransaction();
			
			//Delete from TENANT_USER
			SQLQuery tenantUserQuery = session.createSQLQuery("DELETE FROM TENANT_USER WHERE USER_ID=? AND TENANT_ID = ?");
			tenantUserQuery.setParameter(0, userID);
			tenantUserQuery.setParameter(1, tenantID);
			tenantUserQuery.executeUpdate();
			
			//Delete from USER_ROLE
			SQLQuery userRoleQuery = session.createSQLQuery("DELETE FROM USER_ROLE WHERE USER_ID=? AND TENANT_ID = ?");
			userRoleQuery.setParameter(0, userID);
			userRoleQuery.setParameter(1, tenantID);
			userRoleQuery.executeUpdate();
			
			//Delete user
			session.delete(user);
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deleting user", exception);
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	}

	//TODO : SQL needs to be improved
	@Override
	public List<User> getTenantUsers(int tenantID, int loggedInUserID) {

		Session session = null;
		List<User> users = new ArrayList<User>(); 
		try{
			session = sessionFactory.openSession();
			//Get USERS IDS from tenant id
			SQLQuery getUserIds = session.createSQLQuery("SELECT USER_ID FROM TENANT_USER WHERE TENANT_ID = ? AND USER_ID != ?");
			getUserIds.setParameter(0, tenantID);
			getUserIds.setParameter(1, loggedInUserID);
			List userIDs = getUserIds.list();
			if(userIDs.size() > 0){
				Query userQuery = session.createQuery("FROM User WHERE userID IN (:ids)");
				userQuery.setParameterList("ids", userIDs);
				users = userQuery.list();
			}
			
			Map<Integer, List<Role>> rolesMap = new HashMap<Integer, List<Role>>();
			if(userIDs.size() > 0){
				SQLQuery getRoles = session.createSQLQuery("SELECT b.USER_ID, a.ID, a.ROLE_NAME, a.DESCRIPTION FROM  ROLES a, USER_ROLE b WHERE b.ROLE_ID=a.ID AND b.USER_ID IN (" +StringUtils.join(userIDs, ",") +")");
				//getRoles.setParameter(0, userIDs);
				List results = getRoles.list();
				for(Object obj : results){
					Object[] objs = (Object[])obj;
					int userID = Integer.valueOf(String.valueOf(objs[0]));
					Role role = new Role();
					role.setRoleID(Integer.valueOf(String.valueOf(objs[1])));
					role.setRoleName(String.valueOf(objs[2]));
					role.setDescription(String.valueOf(objs[3]));
					if(rolesMap.get(userID) == null){
						rolesMap.put(userID, new ArrayList<Role>());
					}
					rolesMap.get(userID).add(role);
				}
			}
			
			//Set Role in Users
			for(User user : users){
				if(rolesMap.containsKey(user.getUserID())){
					user.setRoles(rolesMap.get(user.getUserID()));
				}
			}
		}catch(Exception exception){
			logger.error("Error while getting tenant users", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return users;
	}

	@Override
	public List<User> getUserByRole(int tenantID, int roleID) {

		Session session = null;
		List<User> users = new ArrayList<User>(); 
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.ID, a.USER_NAME, a.DESCRIPTION, a.EMAIL_ID, a.MOBILE_NO, a.FIRST_NAME, a.LAST_NAME, a.STATUS_ID, a.DATE_CREATED, a.DATE_MODIFIED FROM USERS a, USER_ROLE b, TENANT_USER c WHERE a.ID=b.USER_ID AND a.ID=c.USER_ID AND b.ROLE_ID= ? AND c.TENANT_ID=?");
			query.setParameter(0, roleID);
			query.setParameter(1, tenantID);
			List results = query.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				User user = new User();
				user.setUserID(Integer.valueOf(String.valueOf(objs[0])));
				user.setUserName(String.valueOf(objs[1]));
				user.setDescription(String.valueOf(objs[2]));
				user.setEmailID(String.valueOf(objs[3]));
				user.setMobileNo(String.valueOf(objs[4]));
				user.setFirstName(String.valueOf(objs[5]));
				user.setLastName(String.valueOf(objs[6]));
				user.setStatusID(Integer.valueOf(String.valueOf(objs[7])));
				if(objs[8] != null){
					user.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[8])).getTime()));
				}
				
				if(objs[9] != null){
					user.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[9])).getTime()));
				}
				user.setTenantID(Integer.valueOf(tenantID));
				Role role = new Role();
				switch(roleID){
					case 1:
						role.setDescription("Admin Role");
						break;
					case 2:
						role.setDescription("Sales Executive");
						break;
					case 3:
						role.setDescription("Delivery Executive");
						break;
					case 4:
						role.setDescription("Collection Executive");
						break;
				}
				List<Role> roles = new ArrayList<Role>();
				roles.add(role);
				user.setRoles(roles);
				users.add(user);
			}
		}catch(Exception exception){
			logger.error("Error while getting user by Role.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return users;
	}

	@Override
	public Tenant getUserTenant(int userId) {

		Session session = null;
		try{
			session = sessionFactory.openSession();
			//Get Tenant ID
			SQLQuery tenantIDQuery = session.createSQLQuery("SELECT a.ID FROM TENANTS a, TENANT_USER b WHERE a.ID=b.TENANT_ID AND b.USER_ID= ?");
			tenantIDQuery.setParameter(0, userId);
			List ids = tenantIDQuery.list();
			if(ids != null && ids.size() == 1){
				return (Tenant)session.get(Tenant.class, Integer.valueOf(String.valueOf(ids.get(0))));
			}
		}catch(Exception exception){
			logger.error("Error while getting user's tenant.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return null;
	
	}

	@Override
	public User getUser(String userName) throws Exception{
		Session session = null;
		User user = null;
		try{
			session = sessionFactory.openSession();
			Query userQuery = session.createQuery("from User where userName = :userName");
			userQuery.setParameter("userName", userName);
			List users = userQuery.list();
			if(users != null && users.size() == 1){
				user = (User)users.get(0);
				//Get Roles
				List<Role> roles = new ArrayList<Role>();
				SQLQuery rolesQuery = session.createSQLQuery("SELECT a.ID, a.ROLE_NAME, a.DESCRIPTION FROM ROLES a, USER_ROLE b WHERE a.ID=b.ROLE_ID AND b.USER_ID=? ");
				rolesQuery.setParameter(0, user.getUserID());
				List results = rolesQuery.list();
				for(Object obj : results){
					Object[] objs = (Object[])obj;
					Role role = new Role();
					role.setRoleID(Integer.valueOf(String.valueOf(objs[0])));
					role.setRoleName(String.valueOf(objs[1]));
					role.setDescription(String.valueOf(objs[2]));
					roles.add(role);
				}
				//Get Tenant ID
				SQLQuery tenantIDQuery = session.createSQLQuery("SELECT a.ID FROM TENANTS a, TENANT_USER b WHERE a.ID=b.TENANT_ID AND b.USER_ID= ?");
				tenantIDQuery.setParameter(0, user.getUserID());
				List ids = tenantIDQuery.list();
				if(ids != null && ids.size() == 1){
					user.setTenantID(Integer.valueOf(String.valueOf(ids.get(0))));
				}
				if(roles.size() > 0){
					user.setRoles(roles);
				}
			}
		}catch(Exception exception){
			logger.error("Error while getting user by user name.", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return user;
	}

	@Override
	public boolean validateUserCredential(String userName, String password) {
		Session session = null;
		try{
			session = sessionFactory.openSession();
			SQLQuery userQuery = session.createSQLQuery("SELECT PASSWORD FROM USERS WHERE USER_NAME=?");
			userQuery.setParameter(0, userName);
			//userQuery.setParameter(1,  password);
			
			List<String> list = userQuery.list();
			if(list != null && list.size() == 1 && !list.get(0).isBlank()) {
				String pass = list.get(0);
				return pass.equals(password);
				
			}
		}catch(Exception exception){
			logger.error("Error while validating user credential", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}
	
	@Override
	public int[] isUserNameEmailIDPresent(String userName, String email){
		Session session = null;
		int[] values = new int[2];
		values[0] = 0;
		values[1] = 0;
		try{
			session = sessionFactory.openSession();
			//user name
			SQLQuery userQuery = session.createSQLQuery("SELECT COUNT(*) FROM USERS WHERE USER_NAME=? ");
			userQuery.setParameter(0, userName);
			List userCounts = userQuery.list();
			if(userCounts != null && userCounts.size() == 1 && ((BigInteger)userCounts.get(0)).intValue() == 1){
				values[0] = 1;
			}
			//email
			if(email != null && !email.isEmpty()){
				SQLQuery emailQuery = session.createSQLQuery("SELECT COUNT(*) FROM USERS WHERE EMAIL_ID=? ");
				emailQuery.setParameter(0, email);
				List emailCounts = emailQuery.list();
				if(emailCounts != null && emailCounts.size() == 1 && ((BigInteger)emailCounts.get(0)).intValue() == 1){
					values[1] = 1;
				}
			}
		}catch(Exception exception){
			logger.error("Error while validating user credential", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return values;
	}
	
	@Override
	public void updatePassword(User user)throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery userQuery = session.createSQLQuery("UPDATE USERS SET PASSWORD=?, LOGGED_IN=?, STATUS_ID = ?, DATE_MODIFIED=CURDATE() WHERE ID= ?");
			userQuery.setParameter(0, user.getNewPassword());
			userQuery.setParameter(1,  user.getLoggedIn());
			userQuery.setParameter(2, EntityStatusEnum.ACTIVE.getEntityStatus());
			userQuery.setParameter(3, user.getUserID());
			if( user.getSecQuestionAnsws() != null &&
					user.getSecQuestionAnsws().size() > 0){
				SQLQuery secQuery = session.createSQLQuery("INSERT INTO USER_SECURITY_QUESTIONS (USER_ID, SECURITY_QUESTION_ID, ANSWER, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, ?, CURDATE())");
				int index = 0;
				for(SecurityQuestion question : user.getSecurityQuestions()){
					secQuery.setParameter(0, user.getUserID());
					secQuery.setParameter(1,question.getId());
					secQuery.setParameter(2, user.getSecQuestionAnsws().get(index));
					secQuery.setParameter(3, user.getTenantID());
					secQuery.executeUpdate();
					++index;
				}
			}
			userQuery.executeUpdate();
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while updating password", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}

	@Override
	public List<SecurityQuestion> getAllSecurityQuestions() {
		if(securityQuestions != null && securityQuestions.size() > 0){
			return securityQuestions;
		}
		Session session = null;
		try{
			securityQuestions = new ArrayList<SecurityQuestion>();
			session = sessionFactory.openSession();
			SQLQuery sqQuery = session.createSQLQuery("SELECT ID, QUESTION FROM SECURITY_QUESTIONS");
			List results = sqQuery.list();
			for(Object obj : results){
				Object[] objs = (Object[])obj;
				SecurityQuestion secQuestion = new SecurityQuestion();
				secQuestion.setId(Integer.valueOf(String.valueOf(objs[0])));
				secQuestion.setQuestion(String.valueOf(objs[1]));
				securityQuestions.add(secQuestion);
			}
			return securityQuestions;
		}catch(Exception exception){
			exception.printStackTrace();
		}finally{
			if(session != null){
				session.close();
			}
		}
		return new ArrayList<SecurityQuestion>();
	}

	/**
	@Override
	public void updateFirstLoginPassword(final User user) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//insert security question answer
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO USER_SECURITY_QUESTIONS (USER_ID, SECURITY_QUESTION_ID) VALUES (?, ?)";
						pstmt = connection.prepareStatement(sqlInsert);
						
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
		}catch(Exception exception){
			logger.error("Error while updating password for first login.", exception);
			if(transaction != null){
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	}
	**/
	

}
