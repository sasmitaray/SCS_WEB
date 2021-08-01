package com.sales.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sales.crm.dao.ResellerDAO;
import com.sales.crm.model.Address;
import com.sales.crm.model.Customer;
import com.sales.crm.model.Reseller;
import com.sales.crm.util.HibernateUtil;

public class MainClass {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		
		ResellerDAO dao = (ResellerDAO)context.getBean("resellerDAO");
		//dao.create(reseller);
		
		SessionFactory sessionFactory = new Configuration().configure(
				"/com/mkyong/persistence/hibernate.cfg.xml")
				.buildSessionFactory();

	}

	
	
}
