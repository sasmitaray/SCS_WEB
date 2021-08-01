package com.sales.crm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sales.crm.dao.AreaDAO;
import com.sales.crm.model.Area;

@Service("areaService")
public class AreaService {
	
	@Autowired
	private AreaDAO areaDAO;
	
	public Area getArea(String areaCode, int tenantID){
		return areaDAO.get(areaCode, tenantID);
	}
	
	public void createArea(Area area) throws Exception{
		areaDAO.create(area);
	}
	
	public void updateArea(Area area) throws Exception{
		areaDAO.update(area);
	}
	
	public void deleteArea(String areaCode, int tenantID) throws Exception{
		areaDAO.delete(areaCode, tenantID);
	}
	
	public List<Area> getTenantAreas(int tenantID){
		return areaDAO.getTenantAreas(tenantID);
	}
	
	public List<Area> getBeatAreas(int beatID, int tenantID){
		return areaDAO.getBeatAreas(beatID, tenantID);
	}
	
	public List<Area> getTenantAreasNotMappedToBeat(int tenantID){
		return areaDAO.getTenantAreasNotMappedToBeat(tenantID);
	}
	
	public List<Area> getTenantAreasNotMappedToBeatForEdit(int tenantID, int beatID){
		return areaDAO.getTenantAreasNotMappedToBeatForEdit(tenantID, beatID);
	}
}
