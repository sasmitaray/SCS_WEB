package com.sales.crm.dao;


import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.model.Address;
import com.sales.crm.model.Customer;
import com.sales.crm.model.EntityStatusEnum;
import com.sales.crm.model.Tenant;

@Repository("tenantDAO")
public class TenantDAOImpl implements TenantDAO{
	
	private static Map<Integer, String> statusMap = new HashMap<Integer, String>();
	static{
		statusMap.put(0, "Inactive");
		statusMap.put(1, "Self Registered");
		statusMap.put(2, "Active");
		statusMap.put(3, "Cancelled");
	}

	@Autowired
	private SessionFactory sessionFactory;
	
	private static SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static Logger logger = Logger.getLogger(TenantDAOImpl.class);

	@Override
	public void create(Tenant tenant) throws Exception{
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			tenant.setDateCreated(new Date());
			//Save tenant
			session.save(tenant);
			for(Address address : tenant.getAddress()){
				address.setDateCreated(new Date());
				address.setTenantID(tenant.getTenantID());
				address.setCode(UUID.randomUUID().toString());
				address.setStatusID(EntityStatusEnum.ACTIVE.getEntityStatus());
				//Save Adddress
				session.save(address);
				//Insert into Tenant Address
				SQLQuery query = session.createSQLQuery("INSERT INTO TENANT_ADDRESS (ADDRESS_ID, TENANT_ID, DATE_CREATED) VALUES (?, ? , CURDATE())");
				query.setInteger(0, address.getId());
				query.setInteger(1, tenant.getTenantID());
				query.executeUpdate();
			}
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating tenant.", e);
			if(transaction != null){
				transaction.rollback();
			}
			throw e;
		}finally{
			if(session != null){
				session.close();
			}
		}
	}
	
	@Override
	public Tenant get(int tenantID) {
		Session session = null;
		Tenant tenant = null;
		try{
			session = sessionFactory.openSession();
			tenant = (Tenant)session.get(Tenant.class, tenantID);
			// Get Address
			SQLQuery addressQuery = session.createSQLQuery(
					"SELECT a.* FROM Address a, TENANT_ADDRESS b WHERE a.ID = b.ADDRESS_ID AND b.TENANT_ID = ?");
			addressQuery.setParameter(0, tenantID);
			List addresses = addressQuery.list();
			List<Address> addressList = new ArrayList<Address>(2);
			for (Object obj : addresses) {
				Object[] objs = (Object[]) obj;
				Address address = new Address();
				address.setId(Integer.valueOf(String.valueOf(objs[0])));
				address.setCode(String.valueOf(objs[1]));
				address.setAddressLine1(String.valueOf(objs[2]));
				address.setAddressLine2(String.valueOf(objs[3]));
				address.setStreet(String.valueOf(objs[4]));
				address.setCity(String.valueOf(objs[5]));
				address.setState(String.valueOf(objs[6]));
				address.setCountry(String.valueOf(objs[7]));
				address.setPostalCode(String.valueOf(objs[8]));
				address.setContactPerson(String.valueOf(objs[9]));
				address.setPhoneNumber(String.valueOf(objs[10]));
				address.setMobileNumberPrimary(String.valueOf(objs[11]));
				address.setMobileNumberSecondary(String.valueOf(objs[12]));
				address.setEmailID(String.valueOf(objs[13]));
				address.setAddrressType(Integer.valueOf(String.valueOf(objs[14])));
				address.setTenantID(Integer.valueOf(String.valueOf(objs[15])));
				address.setDateCreated(new Date(dbFormat.parse(String.valueOf(objs[17])).getTime()));
				if (objs[18] != null) {
					address.setDateModified(new Date(dbFormat.parse(String.valueOf(objs[18])).getTime()));
				}
				addressList.add(address);
			}
			tenant.setAddress(addressList);
		}catch(Exception exception){
			logger.error("Error while fetching tenant details", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return tenant;
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
			logger.error("Error while updating tenant", e);
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
	public void delete(int tenantID) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			Tenant tenant = (Tenant)session.get(Tenant.class, tenantID);
			transaction = session.beginTransaction();
			session.delete(tenant);
			transaction.commit();
		}catch(Exception exception){
			logger.error("Error while deleting tenant", exception);
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
					"SELECT COUNT(*) COUNT FROM ADDRESS a, TENANT_ADDRESS b where a.ID = b.ADDRESS_ID AND a.ADDRESS_TYPE=1 AND a.EMAIL_ID= ?");
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

	@Override
	public List<Tenant> getTenants() throws Exception{
		Session session = null;
		List<Tenant> tenants = new ArrayList<Tenant>();
		try{
			session = sessionFactory.openSession();
			Query tenantsQuery = session.createQuery("FROM Tenant");
			tenants = tenantsQuery.list();
			if(tenants != null){
				for(Iterator<Tenant> itr= tenants.iterator(); itr.hasNext(); ){
					//Skip appowner
					Tenant tenant = itr.next();
					if(tenant.getTenantID() == -1){
						itr.remove();
						continue;
					}
					tenant.setStatusText(statusMap.get(tenant.getStatusID()));
				}
			}
		}catch(Exception exception){
			logger.error("Error while fetching tenants", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return tenants;
	}
	
	
	
}
