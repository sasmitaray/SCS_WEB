package com.sales.crm.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class EmailUtil {

	private static Logger logger = Logger.getLogger(EmailUtil.class);

	public static boolean sendMail(String emailID, String subject, String msg) {

		final String username = "sasmita.less.sec@gmail.com";
		final String password = "m1001636";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from-email@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailID));
			message.setSubject(subject);
			message.setText(msg);
			Transport.send(message);

		} catch (Exception exception) {
			logger.error("Email could not be sent to "+ emailID, exception);
			return false;
		}

		return true;
	}
}
