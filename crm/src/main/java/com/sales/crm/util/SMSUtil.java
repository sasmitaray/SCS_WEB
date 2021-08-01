package com.sales.crm.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class SMSUtil {
	
	private static Logger logger = Logger.getLogger(SMSUtil.class);

	
	public static boolean sendSms(String mobileNo, String msg) {
		try {
			
			// Construct data
			String user = "username=" + "sasmita.rout@hotmail.com";
			String hash = "&hash=" + "Welcome123";
			String message = "&message=" + msg;
			String sender = "&sender=" + "TXTLCL";
			String numbers = "&numbers=" + mobileNo;
			
			// Send data
			HttpURLConnection conn = (HttpURLConnection) new URL("http://api.textlocal.in/send/?").openConnection();
			String data = user + hash + numbers + message + sender;
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

}
