package net.mxh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.AdminDao;
import net.mxh.entity.Admin;

@Service
@Transactional
public class AdminService {
	
	@Autowired
	private AdminDao adminDao;
	
	public void save(Admin entity) {
		adminDao.save(entity);
	}
	
	public void update(Admin entity) {
		adminDao.update(entity);
	}
	
	public void delete(Admin entity) {
		adminDao.delete(entity);
	}
	
	public Admin findById(Long id) {
		return adminDao.findById(Admin.class, id);
	}
	
	public List<Admin> findAll() {
		return adminDao.findAll(Admin.class);
	}
	
	public List<Admin> findAll(Integer page, Integer pageSize) {
		return adminDao.findAll(Admin.class, page, pageSize);
	}
	
	public Long count() {
		return adminDao.count(Admin.class);
	}
	
	public Admin findByAdminName(String adminName) {
		Map<String, Object> param = new HashMap<>();
		param.put("adminName", adminName);
		return adminDao.findOne(Admin.class, param);
	}
	
	public Long countByStoresId(Long storesId) {
		Map<String, Object> param = new HashMap<>();
		param.put("storesId", storesId);
		return adminDao.countByParam(Admin.class, param);
	}
	
	public Admin findByOpenid(String openid) {
		Map<String, Object> param = new HashMap<>();
		param.put("openid", openid);
		return adminDao.findOne(Admin.class, param);
	}
	
	public Admin findByRoleIdAndOpenidNotNull(Long roleId) {
		return adminDao.findByRoleIdAndOpenidNotNull(roleId);
	}
}
