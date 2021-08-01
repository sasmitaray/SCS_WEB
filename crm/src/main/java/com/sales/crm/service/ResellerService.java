package com.sales.crm.service;

import java.security.DrbgParameters.Reseed;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.CustomerDAO;
import com.sales.crm.dao.ResellerDAO;
import com.sales.crm.model.Address;
import com.sales.crm.model.BusinessEntity;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Reseller;
import com.sales.crm.model.Tenant;
import com.sales.crm.model.TenantType;
import com.sales.crm.model.User;

@Service("resellerService")
public class ResellerService {
	
	@Autowired
	private ResellerDAO resellerDAO;
	
	@Autowired
	private CustomerDAO customerService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TenantService tenantService;
	
	@Autowired
	BeatService beatService;
	
	@Autowired
	AreaService areaService;
	
	@Autowired
	DataSourceTransactionManager transactionManager;
	
	public Reseller getReseller(int resellerID){
		//Reseller reseller = resellerDAO.get(resellerID);
		//reseller.setCustomers(customerService.getTenantCustomers(resellerID));
		Tenant tenant = tenantService.getTenant(resellerID);
		Reseller reseller = new Reseller();
		reseller.setTenantID(tenant.getTenantID());
		reseller.setName(tenant.getName());
		reseller.setDescription(tenant.getDescription());
		reseller.setCode(tenant.getCode());
		reseller.setDateCreated(tenant.getDateCreated());
		reseller.setDateModified(tenant.getDateModified());
		//Set Address
		reseller.setAddress(tenant.getAddress());
		//Set Customers
		reseller.setCustomers(customerService.getTenantCustomers(resellerID));
		//Beats
		reseller.setBeats(beatService.getTenantBeats(resellerID));
		//Area
		reseller.setAreas(areaService.getTenantAreas(resellerID));
		return reseller;
	}
	
	public void updateReseller(Reseller reseller){
		Tenant tenant = new Tenant();
		tenant.setAddress(reseller.getAddress());
		tenant.setCode(reseller.getCode());
		tenant.setDateCreated(reseller.getDateCreated());
		tenant.setDateModified(reseller.getDateModified());
		tenant.setDescription(reseller.getDescription());
		tenant.setName(reseller.getName());
		tenant.setStatusID(reseller.getStatusID());
		tenant.setStatusText(reseller.getStatusText());
		tenant.setTenantID(reseller.getResellerID());
		tenant.setTenantType(TenantType.RESELLER.getTenantType());
		tenantService.updateTenant(tenant);
	}
	
	
	/**
	public void createReseller(Reseller reseller) throws Exception{
		resellerDAO.create(reseller);
	}
	
	
	public void deleteReseller(int resellerID){
		resellerDAO.delete(resellerID);
	}
	
	public boolean isEmailIDAlreadyUsed(String emailID) throws Exception{
		return resellerDAO.isEmailIDAlreadyUsed(emailID);
	}
	
	*/
	public List<Reseller> getResellers() throws Exception{
		return resellerDAO.getResellers();
	}
	
	public void activateReseller(int resellerID){
		try{
			Reseller reseller = resellerDAO.get(resellerID);
			//update reseller
			reseller.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
			resellerDAO.update(reseller);
			//create user
			User user = new User();
			
			user.setPassword(String.valueOf(new Date().getTime()));
			user.setTenantID(resellerID);
			for(Address address : reseller.getAddress()){
				if(address.getAddrressType() == 1){
					user.setEmailID(address.getEmailID());
					user.setUserName(address.getEmailID());
					user.setMobileNo(address.getMobileNumberPrimary());
					user.setFirstName(address.getContactPerson());
					break;
				}
			}
			//Add Role
			Set<Integer> roleIds = new HashSet<Integer>();
			roleIds.add(100);
			user.setRoleIDs(roleIds);
			userService.createUser(user);
			
			//send email
			sendMail(user.getEmailID(), user.getUserName(), user.getPassword());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	private boolean sendMail(String emailID, String userName, String pass) {
		
		String msg = "Dear Reseller \n you are successfully on-boarded to the system. Please find the login details below. \n\n\n URL : http://35.189.180.185:8080/crm \n user name: "+ userName +"\n Password: "+ pass + "\n\n\n Regards, \n Team";

		final String username = "sasmita.less.sec@gmail.com";
		final String password = "m1001636";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(emailID));
			message.setSubject("You are onboared");
			message.setText(msg);
			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
