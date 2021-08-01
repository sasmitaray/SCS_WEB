package com.sales.crm.dao;

import java.util.List;

import com.sales.crm.model.Area;

public interface AreaDAO {
	
	void create(Area area) throws Exception;
	
	Area get(String areaCode, int tenantID);
	
	void update(Area area) throws Exception;
	
	void delete(String areaCode, int tenantID) throws Exception;
	
	List<Area> getTenantAreas(int tenantID);
	
	List<Area> getBeatAreas(int beatID, int tenantID);
	
	public List<Area> getTenantAreasNotMappedToBeat(int tenantID);
	
	public List<Area> getTenantAreasNotMappedToBeatForEdit(int tenantID, int beatID);

}
