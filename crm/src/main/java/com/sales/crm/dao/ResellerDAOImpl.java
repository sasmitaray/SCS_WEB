package com.sales.crm.dao;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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

import com.sales.crm.model.Address;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Reseller;
import com.sales.crm.model.Tenant;

@Repository("resellerDAO")
public class ResellerDAOImpl implements ResellerDAO{
	
	private static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	static{
		statusMap.put(2, "Active");
		statusMap.put(3, "Inactive");
		statusMap.put(4, "Cancelled");
		statusMap.put(10, "Self Registered");
	}

	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(ResellerDAOImpl.class);

	@Override
	public Reseller get(int tenantID) {
		Session session = null;
		Tenant tenant = null;
		try{
			session = sessionFactory.openSession();
			tenant = (Tenant)session.get(Tenant.class, tenantID);
		}catch(Exception exception){
			logger.error("Error while fetching reseller details", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return (Reseller)tenant;
	}



	@Override
	public void update(Tenant tenant) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			tenant.setDateModified(new Date());
			for(Address address : tenant.getAddress()){
				address.setDateModified(new Date());
			}
			session.update(tenant);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while updating reseller", e);
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	}


	
	/**
	@Override
	public void delete(int resellerID) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			Reseller reseller = (Reseller)session.get(Reseller.class, resellerID);
			transaction = session.beginTransaction();
			session.delete(reseller);
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deleting reseller", exception);
			if(transaction != null){
				transaction.rollback();
			}
		}finally{
			if(session != null){
				session.close();
			}
		}
		
	}
	

	@Override
	public boolean isEmailIDAlreadyUsed(String emailID) throws Exception{

		Session session = null;
		try{
			session = sessionFactory.openSession();
			SQLQuery emailQuery = session.createSQLQuery(
					"SELECT COUNT(*) COUNT FROM ADDRESS a, RESELLER_ADDRESS b where a.ID = b.ADDRESS_ID AND a.ADDRESS_TYPE=1 AND a.EMAIL_ID= ?");
			emailQuery.setParameter(0, emailID);
			List counts = emailQuery.list();
			if(counts != null && counts.size() == 1 && ((BigInteger)counts.get(0)).intValue() > 0){
				return true;
			}
		}catch(Exception exception){
			logger.error("Error while validating user credential", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return false;
	}

	*/
	@Override
	public List<Reseller> getResellers() throws Exception{
		Session session = null;
		List<Tenant> tenants = new ArrayList<Tenant>();
		List<Reseller> resellers = new ArrayList<Reseller>();
		try{
			session = sessionFactory.openSession();
			Query resellersQuery = session.createQuery("FROM Tenant");
			tenants = resellersQuery.list();
			if(tenants != null){
				for(Iterator<Tenant> itr= tenants.iterator(); itr.hasNext(); ){
					//Skip appowner
					Tenant tenant = itr.next();
					if(tenant.getTenantID() == -1){
						itr.remove();
						continue;
					}
					tenant.setStatusText(statusMap.get(tenant.getStatusID()));
					resellers.add((Reseller)tenant);
				}
			}
		}catch(Exception exception){
			logger.error("Error while fetching resellers", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return resellers;
	}
	
	
	
}
