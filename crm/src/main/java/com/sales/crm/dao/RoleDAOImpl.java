package com.sales.crm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sales.crm.model.Customer;
import com.sales.crm.model.Permission;
import com.sales.crm.model.Resource;
import com.sales.crm.model.ResourcePermission;
import com.sales.crm.model.Role;

@Repository
public class RoleDAOImpl implements RoleDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	private static Logger logger = Logger.getLogger(RoleDAOImpl.class);

	@Override
	public void create(Role role) {
		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			role.setDateCreated(new Date());
			role.setCode(UUID.randomUUID().toString());
			session.save(role);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while creating Role.", e);
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
	public Role get(int roleID, int tenantID) {

		Session session = null;
		Role role = null;
		try{
			session = sessionFactory.openSession();
			Query query = session.createQuery("from Role where roleID = :roleID AND tenantID = :tenantID");
			query.setParameter("roleID", roleID);
			query.setParameter("tenantID", tenantID);
			if (query.list() != null && query.list().size() == 1) {
				role = (Role) query.list().get(0);
			}
		}catch(Exception exception){
			logger.error("Error while fetching role", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return role;
	
	}

	@Override
	public void update(Role role) {

		Session session = null;
		Transaction transaction = null;
		try{
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			role.setDateModified(new Date());
			session.update(role);
			transaction.commit();
		}catch(Exception e){
			logger.error("Error while updating role.", e);
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
	public void delete(int roleID, int tenantID) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			Query query = session.createQuery("delete from Role where roleID = :roleID AND tenantID = :tenantID");
			query.setParameter("roleID", roleID);
			query.setParameter("tenantID", tenantID);
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while deleting role", exception);
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}

	}

	@Override
	public List<Role> getRoles(List<Integer> roleIDs, int tenantID) {
		Session session = null;
		List<Role> roles = null; 
		try{
			session = sessionFactory.openSession();
			//Get the highest rank
			SQLQuery rankQuery = session.createSQLQuery("SELECT MAX(RANKS) FROM ROLES WHERE ID IN (" +StringUtils.join(roleIDs, ",") + ") AND TENANT_ID = ?");
			rankQuery.setParameter(0, tenantID);
			List<Integer> rankList  = rankQuery.list();
			Query query = session.createQuery("from Role where rank < :rank AND tenantID = :tenantID");
			query.setParameter("rank", rankList.get(0));
			query.setParameter("tenantID", tenantID);
			if (query.list() != null && query.list().size() > 0) {
				roles = (List<Role>) query.list();
			}
		}catch(Exception exception){
			logger.error("Error while fetching roles", exception);
		}finally{
			if(session != null){
				session.close();
			}
		}
		return roles;
	}
	
	@Override
	public List<ResourcePermission> getResourcePermissions(int tenantID) throws Exception{
		Session session = null;
		List<ResourcePermission> resourcePermissions = new ArrayList<ResourcePermission>();
		SQLQuery sqlQuery = null;
		
		try{
			session = sessionFactory.openSession();
			sqlQuery = session.createSQLQuery(
					"SELECT a.ID RESOURCE_ID, a.RESOURCE_KEY, a.NAME RESOURCE_NAME, a.DESCRIPTION RESOURCE_DESCRIPTION , b.ID PERMISSION_ID, b.PERMISSION_KEY, b.NAME PERMISSION_NAME, b.DESCRIPTION PERMISSION_DESCRIPTION, c.ID RESOURCE_PERMISSION_ID  FROM RESOURCE a, PERMISSION b, RESOURCE_PERMISSION c WHERE a.ID=c.RESOURCE_ID AND b.ID=c.PERMISSION_ID AND a.TENANT_ID=a.TENANT_ID=a.TENANT_ID=? AND c.ID NOT IN (25, 28, 29, 30)");
			sqlQuery.setParameter(0, 1);
			List lists = sqlQuery.list();
			for(Object obj : lists){
				Object[] objs = (Object[])obj;
				//Resource
				Resource resource = new Resource();
				resource.setResourceID(Integer.valueOf(String.valueOf(objs[0])));
				resource.setResourceKey(String.valueOf(objs[1]));
				resource.setName(String.valueOf(objs[2]));
				resource.setDescription(String.valueOf(objs[3]));
				//Permission
				Permission permission = new Permission();
				permission.setPermissionID(Integer.valueOf(String.valueOf(objs[4])));
				permission.setPermissionKey(String.valueOf(objs[5]));
				permission.setName(String.valueOf(objs[6]));
				permission.setDescription(String.valueOf(objs[7]));
				
				ResourcePermission resourcePermission = new ResourcePermission();
				resourcePermission.setResource(resource);
				resourcePermission.setPermission(permission);
				resourcePermission.setId(Integer.valueOf(String.valueOf(objs[8])));
				
				resourcePermissions.add(resourcePermission);
			}
		}catch(Exception exception){
			logger.error("Error while fetching resource permission present in the system.", exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return resourcePermissions;
	}
	
	@Override
	public List<ResourcePermission> getRoleResourcePermissions(List<Integer> roleIDs, int tenantID) throws Exception{
		Session session = null;
		List<ResourcePermission> resourcePermissions = getResourcePermissions(tenantID);
		SQLQuery sqlQuery ;
		try{
			session = sessionFactory.openSession();
			//SuperAdmin will have all access
			if(roleIDs.contains(100)){
				sqlQuery = session.createSQLQuery(
						"SELECT ID FROM RESOURCE_PERMISSION WHERE ID NOT IN (25, 28, 29, 30)");
			}else{
				sqlQuery = session.createSQLQuery(
						"SELECT RESOURCE_PERMISSION_ID FROM ROLE_RESOURCE_PERMISSION WHERE ROLE_ID IN (" +StringUtils.join(roleIDs, ",") + ") AND TENANT_ID= ? AND ROLE_ID != 100");
				sqlQuery.setParameter(0, tenantID);
			}
			List<Integer> resourcePermIDList = sqlQuery.list();
			for(ResourcePermission resourcePermission : resourcePermissions){
				if(resourcePermIDList.contains(resourcePermission.getId())){
					resourcePermission.setPresent(true);
				}
			}
		}catch(Exception exception){
			logger.error("Error while fetching resource permission for tenant "+ tenantID + " and role " + StringUtils.join(roleIDs, ","), exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return resourcePermissions;
	}

	@Override
	public void saveRoleResourcePermission(final List<ResourcePermission> resourcePermissions, int roleID, int tenantID) throws Exception {
		Session session = null;
		Transaction transaction = null;
		try {
			session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			//Delete resourcepermissions
			SQLQuery deleteResPermQry = session.createSQLQuery("DELETE FROM ROLE_RESOURCE_PERMISSION WHERE ROLE_ID= ? AND TENANT_ID = ?");
			deleteResPermQry.setParameter(0, roleID);
			deleteResPermQry.setParameter(1, tenantID);
			deleteResPermQry.executeUpdate();
			// get Connction from Session
			session.doWork(new Work() {
				@Override
				public void execute(Connection connection) throws SQLException {
					PreparedStatement pstmt = null;
					try {
						String sqlInsert = "INSERT INTO ROLE_RESOURCE_PERMISSION (ROLE_ID, RESOURCE_PERMISSION_ID, TENANT_ID, DATE_CREATED, DATE_MODIFIED) VALUES (?, ?, ?, ?, ?)";
						pstmt = connection.prepareStatement(sqlInsert);
						for (ResourcePermission resourcePermission : resourcePermissions) {
							pstmt.setInt(1, resourcePermission.getRoleID());
							pstmt.setInt(2, resourcePermission.getId());
							pstmt.setInt(3, resourcePermission.getTenantID());
							pstmt.setDate(4, new java.sql.Date(new Date().getTime()));
							pstmt.setDate(5, new java.sql.Date(new Date().getTime()));
							pstmt.addBatch();
						}
						pstmt.executeBatch();
					} finally {
						pstmt.close();
					}
				}
			});
			transaction.commit();
		} catch (Exception exception) {
			logger.error("Error while saving resource permission.", exception);
			if(transaction != null){
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
	public List<Integer> getRoleResourcePermissionIDs(List<Integer> roleIDs, int tenantID) throws Exception{
		Session session = null;
		List<Integer> resourcePermIDList = new ArrayList<Integer>();
		SQLQuery sqlQuery;
		try{
			session = sessionFactory.openSession();
			if(roleIDs.contains(1)){
				sqlQuery = session.createSQLQuery(
						"SELECT ID FROM RESOURCE_PERMISSION WHERE ID NOT IN (25, 28, 29, 30)");
			}else{
				sqlQuery = session.createSQLQuery(
					"SELECT RESOURCE_PERMISSION_ID FROM ROLE_RESOURCE_PERMISSION WHERE ROLE_ID IN (" +StringUtils.join(roleIDs, ",") + ") AND TENANT_ID= ? AND ROLE_ID != 1");
				sqlQuery.setParameter(0, tenantID);
			}
			resourcePermIDList = sqlQuery.list();
		}catch(Exception exception){
			logger.error("Error while fetching resource permission IDs for tenant "+ tenantID + " and role " + StringUtils.join(roleIDs, ","), exception);
			throw exception;
		}finally{
			if(session != null){
				session.close();
			}
		}
		return resourcePermIDList;
	}
}
