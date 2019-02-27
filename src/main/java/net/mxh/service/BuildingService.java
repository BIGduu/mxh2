package net.mxh.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.BuildingDao;
import net.mxh.entity.Building;

@Service
@Transactional
public class BuildingService {
	
	@Autowired
	private BuildingDao buildingDao;
	
	public void save(Building entity) {
		buildingDao.save(entity);
	}
	
	public void update(Building entity) {
		buildingDao.update(entity);
	}
	
	public void delete(Building entity) {
		buildingDao.delete(entity);
	}
	
	public Building findById(Long id) {
		return buildingDao.findById(Building.class, id);
	}
	
	public List<Building> findAll() {
		return buildingDao.findAll(Building.class);
	}
	
	public List<Building> findAll(Integer page, Integer pageSize) {
		return buildingDao.findAll(Building.class, page, pageSize);
	}

	public List<Building> findByParam(Map<String, Object> param, String orderColumn, boolean isAsc) {
		return buildingDao.findByParam(Building.class,param,orderColumn,isAsc);
	}
	
	public long count() {
		return buildingDao.count(Building.class);
	}
	
	public void saveOrUpdate(Building stores) {
		buildingDao.saveOrUpdate(stores);
	}
	
	public void delete(Long buildingId) {
		buildingDao.delete(findById(buildingId));
	}
	
	public List<Building> findByManagerId(Long managerId) {
		return buildingDao.findByManagerId(managerId);
	}
}
