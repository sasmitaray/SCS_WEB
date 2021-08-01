package com.sales.crm.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.exception.CRMException;
import com.sales.crm.exception.ErrorCodes;
import com.sales.crm.model.CustomerOTP;
import com.sales.crm.model.EntityStatusEnum;

@Repository("otpDAO")
public class OTPDAOImpl implements OTPDAO{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(OTPDAOImpl.class);
	
	private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private static Map<Integer, String> otpStatusMap = new HashMap<Integer, String>();
	
	static {
		otpStatusMap.put(EntityStatusEnum.OTP_GENERATED.getEntityStatus(), "Not Used");
		otpStatusMap.put(EntityStatusEnum.OTP_VERIFIED.getEntityStatus(), "Verified");
	}
	

	@Override
	public int generateOTP(CustomerOTP customerOTP) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("SELECT a.*  " + 
					"FROM   customer_otp a, " + 
					"       otp_entity b " + 
					"WHERE  a.id = b.otp_id " + 
					"       AND a.tenant_id = b.tenant_id " + 
					"       AND a.customer_id = ? " + 
					"       AND a.otp_type = ? " + 
					"       AND a.status_id != ? " + 
					"       AND a.tenant_id = ? " + 
					"       AND b.entity_id = ? ");
			query.setInteger(0, customerOTP.getCustomerID());
			query.setInteger(1, customerOTP.getOtpType());
			query.setInteger(2, EntityStatusEnum.OTP_VERIFIED.getEntityStatus());
			query.setInteger(3, customerOTP.getTenantID());
			query.setInteger(4, customerOTP.getEntityID());
			List dbCustomerOTPList = query.list();
			for(Object obj : dbCustomerOTPList){
				Object[] objs = (Object[])obj;
				if(objs[4] != null ||
						objs[7] != null){
					logger.error("OTP is already used for customer of OTP Type "+customerOTP.getOtpType() );	
					throw new CRMException(ErrorCodes.OTP_ALREADY_VERIFIED, "OTP for this customer is already used");
				}else{
					logger.warn("OTP generated earlier but not used and regenerating again.");
					Query deleteQuery = session.createQuery("delete from CustomerOTP where otpID = :otpID");
					deleteQuery.setParameter("otpID", Integer.valueOf(String.valueOf(objs[0])));
					deleteQuery.executeUpdate();
				}
			
			}
			
			customerOTP.setOtpGeneratedDateTime(new Date());
			customerOTP.setDateCreated(new Date());
			customerOTP.setStatusID(EntityStatusEnum.OTP_GENERATED.getEntityStatus());
			session.save(customerOTP);
			//update otp_entity table
			SQLQuery otpEntityQry = session.createSQLQuery("INSERT INTO OTP_ENTITY (OTP_ID, ENTITY_ID, TENANT_ID, DATE_CREATED) VALUES (?, ?, ?, CURDATE())");
			otpEntityQry.setInteger(0, customerOTP.getOtpID());
			otpEntityQry.setInteger(1, customerOTP.getEntityID());
			otpEntityQry.setInteger(2, customerOTP.getTenantID());
			otpEntityQry.executeUpdate();
			transaction.commit();
			return customerOTP.getOtpID();
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
	public void removeGeneratedOTP(int otpID, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery("DELETE FROM CUSTOMER_OTP WHERE ID=? AND TENANT_ID = ?");
			query.setParameter( 0, otpID);
			query.setParameter( 1, tenantID);
			query.executeUpdate();
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while getting Trimmed customer", exception);
			if (transaction != null) {
				transaction.rollback();
			}
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	/**
	 * Verify passed OTP is same, if same update the row or return false;
	 * 
	 * @param customerID
	 * @param otpType
	 * @param otp
	 * @return
	 * @throws Exception
	 */
	@Override
	public void verifyAndStoreOTP(int customerID, int otpType, String otp, int tenantID) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			SQLQuery query = session.createSQLQuery(" SELECT ID FROM CUSTOMER_OTP WHERE CUSTOMER_ID= ? AND OTP_TYPE= ? AND GENERTAED_OTP= ? AND TENANT_ID = ? AND DATE(GENERATED_DATE_TIME)= CURDATE() AND SUBMITTED_DATE_TIME IS NULL AND SUBMITTED_OTP IS NULL");
			query.setParameter(0, customerID);
			query.setParameter(1, otpType);
			query.setParameter(2, otp);
			query.setParameter(3, tenantID);
			List results = query.list();
			if(results != null && results.size() == 1){
				for(Object obj : results){
					int otpID = Integer.valueOf(String.valueOf(obj));
					SQLQuery updateQuery = session.createSQLQuery("UPDATE CUSTOMER_OTP SET SUBMITTED_OTP=?, STATUS_ID= ? SUBMITTED_DATE_TIME=NOW(), DATE_MODIFIED=CURDATE() WHERE ID=? AND TENANT_ID = ? ");
					updateQuery.setParameter(0, otp);
					updateQuery.setParameter(1, EntityStatusEnum.OTP_VERIFIED.getEntityStatus());
					updateQuery.setParameter(2, otpID);
					updateQuery.setParameter(3, tenantID);
					updateQuery.executeUpdate();
				}
			}else{
				logger.error("OTP "+ otp +" not found or expired for customer "+ customerID + ", otp type "+ otpType + ".");
				throw new CRMException(ErrorCodes.OTP_MISMATCH, "OTP supplied could not match with the OTP generated.");
			}
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while verifying OTP", exception);
			if (transaction != null) {
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
	public void verifyOTP(int customerID, int otpType, String otp, int tenantID) throws Exception{
		Session session = null;
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery(" SELECT ID FROM CUSTOMER_OTP WHERE CUSTOMER_ID= ? AND OTP_TYPE= ? AND GENERTAED_OTP= ? AND TENANT_ID = ? AND DATE(GENERATED_DATE_TIME)= CURDATE() AND SUBMITTED_DATE_TIME IS NULL AND SUBMITTED_OTP IS NULL");
			query.setParameter(0, customerID);
			query.setParameter(1, otpType);
			query.setParameter(2, otp);
			query.setParameter(3, tenantID);
			List results = query.list();
			if(results != null && results.size() == 1){
				//
			}else{
				logger.error("OTP "+ otp +" not found or expired for customer "+ customerID + ", otp type "+ otpType + ".");
				throw new CRMException(ErrorCodes.OTP_MISMATCH, "OTP supplied could not match with the OTP generated.");
			}
		}catch(Exception exception){
			logger.error("Error while verifying OTP", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	@Override
	public List<CustomerOTP> getOTPReport(int tenantID){
		Session session = null;
		List<CustomerOTP> customerOTPs = new ArrayList<CustomerOTP>();
		try{
			session = sessionFactory.openSession();
			SQLQuery query = session.createSQLQuery("SELECT a.*, b.NAME, c.FIRST_NAME, c.LAST_NAME FROM CUSTOMER_OTP a, CUSTOMERS b, USERS c WHERE a.CUSTOMER_ID = b.ID AND a.FIELD_EXEC_ID = c.ID AND a.TENANT_ID = ? ORDER BY a.GENERATED_DATE_TIME DESC");
			query.setParameter(0, tenantID);
			List results = query.list();
			if(results != null && results.size() > 0){
				for(Object obj : results){
					Object[] objs = (Object[])obj;
					CustomerOTP customerOTP = new CustomerOTP();
					customerOTP.setOtpID(Integer.parseInt(String.valueOf(objs[0])));
					customerOTP.setCustomerName(String.valueOf(objs[13]));
					customerOTP.setSalesExecName(String.valueOf(objs[14]) +" "+ String.valueOf(objs[15]));
					customerOTP.setGenaratedOTP(String.valueOf(objs[3]));
					int otpType = Integer.valueOf(String.valueOf(objs[5]));
					switch(otpType){
					case 1:
						customerOTP.setOtpStringType("Order Booking");
						break;
					case 2:
						customerOTP.setOtpStringType("Delivery Confirmation");
						break;
					case 3:
						customerOTP.setOtpStringType("Payment Confirmation");
						break;
					}
					customerOTP.setOtpStatus(otpStatusMap.get(Integer.parseInt(String.valueOf(objs[9]))));
					customerOTP.setStringDateGenerated(String.valueOf(objs[6]));
					customerOTP.setStringDateUsed(String.valueOf(objs[7]).equals("null") ? "" : String.valueOf(objs[7]));
					customerOTP.setTenantID(Integer.parseInt(String.valueOf(objs[10])));
					customerOTPs.add(customerOTP);
				}
			}
				
		}catch(Exception exception){
			logger.error("Error while getting OTPs.", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		
		return customerOTPs;
		
	}

}
