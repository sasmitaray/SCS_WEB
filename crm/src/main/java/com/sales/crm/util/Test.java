package com.sales.crm.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;  

public class Test {

	public static void main(String[] args) {
		
		/**

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
				InternetAddress.parse("srout@cisco.com"));
			message.setSubject("Testing Subject");
			String userName="Sasmita";
			String pass = "sasmitapass";		
			message.setText("Dear Reseller \n you are successfully on-boarded to the system. Please find the login details below. \n\n\n URL : http://localhost:8080/crm \n user name: "+ userName +"\n Password "+ pass + "\n\n\n Regards, \n Team");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		**/
		System.out.println(new SimpleDateFormat("SSS").format(new Date()) );
		System.out.println("Sasmita".substring(0, 3));
	}
	  
}
