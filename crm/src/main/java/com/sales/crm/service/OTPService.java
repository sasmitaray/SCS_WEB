package com.sales.crm.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.OTPDAO;
import com.sales.crm.exception.CRMException;
import com.sales.crm.exception.ErrorCodes;
import com.sales.crm.model.CustomerOTP;

@Service("otpService")
public class OTPService {

	@Autowired
	OTPDAO otpDAO;
	
	@Autowired
	CustomerService customerService;
	
	private static Logger logger = Logger.getLogger(OTPService.class);
	
	
	public void generateOTP(CustomerOTP customerOTP, int tenantID) throws Exception{
		
		int customerID = customerOTP.getCustomerID();
		String date = new SimpleDateFormat("dd").format(new Date());
		int otpType = customerOTP.getOtpType();
		
		customerOTP.setGenaratedOTP(generateRandomNumber());
		boolean sendSMSStatus = true;
		try {
			//String mobileNo = customerService.getCustomerPrimaryMobileNo(customerID, tenantID);
			//if (mobileNo.trim().isEmpty() || mobileNo.trim().equals("+91")) {
			//	throw new CRMException(ErrorCodes.OTP_NO_MOBILE_NO,
			//			"OTP could not be generated successfully as customer has not registered primary mobile number");
			//}
			int otpID = otpDAO.generateOTP(customerOTP);
			// Call SMS Gateway
			// retry 3 times
			//for (int i = 0; i < 3; i++) {
			//	if (sendSms(mobileNo, otp, otpType)) {
			//		sendSMSStatus = true;
			//		logger.info("OTP is successfully sent to customer");
			//		break;
			//	}
			//}

			if (!sendSMSStatus) {
				logger.error("OTP SMS failed to send, removing OTP entry from DB");
				otpDAO.removeGeneratedOTP(otpID, tenantID);
				throw new CRMException(ErrorCodes.OTP_DISPATCH_FAILED,
						"SMS could not be sent to the customer through gateway.");
			}
		}catch(Exception exception){
			throw exception;
		}
		
	}
	
	private String generateRandomNumber() {
		 Random rnd = new Random();
		 int number = rnd.nextInt(999999);
         return String.format("%06d", number);
	}

	private boolean sendSms(String mobileNo, String otp, int otpType) {
		try {
			
			String msg = "";
			String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			switch(otpType){
			case CustomerOTP.OTP_ORDER_BOOKING:
				msg = "["+ date +"]" + "OTP for order booking is "+otp;
				break;
			case CustomerOTP.OTP_DELIVERY_CONF:
				msg = "["+ date +"]" + "OTP for delivery confirmation is "+otp;
				break;
			case CustomerOTP.OTP_PAYMENT_CONF:
				msg = "["+ date +"]" + "OTP for payment confirmation is "+otp;
				break;
			
			}
			
			// Construct data
			String apiKey = "apikey=" + "OTg5M2ZmMDEwODI0YzMzOWY3YTY0NDVlMjYzYzQ4MGY=";
			String message = "&message=" + msg;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + mobileNo;
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
			String data = apiKey + numbers + message + sender;
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
			conn.getOutputStream().write(data.getBytes("UTF-8"));
			final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			final StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				stringBuffer.append(line);
			}
			rd.close();
			JSONObject object = new JSONObject(stringBuffer.toString());
			if(object.getString("status").equals("success")){
				return true;
			}else{
				logger.error("OTP SMS Failure \n" + stringBuffer.toString());
			}
		} catch (Exception e) {
			logger.error("Error SMS "+e);
			return false;
		}
		return false;
	}
	
	public void verifyOTP(int customerID, int otpType, String otp, int tenantID) throws Exception{
		otpDAO.verifyOTP(customerID, otpType, otp, tenantID);
	}
	
	public List<CustomerOTP> getOTPReport(int tenantID){
		return otpDAO.getOTPReport(tenantID);
	}
	
	public void deleteOTP(int otpID, int tenantID) throws Exception{
		otpDAO.removeGeneratedOTP(otpID, tenantID);
	}
}
