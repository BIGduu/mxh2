package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.RoleDao;
import net.mxh.entity.Role;

@Service
@Transactional
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;
	
	public void save(Role entity) {
		roleDao.save(entity);
	}
	
	public void update(Role entity) {
		roleDao.update(entity);
	}
	
	public void delete(Role entity) {
		roleDao.delete(entity);
	}
	
	public Role findById(Long id) {
		return roleDao.findById(Role.class, id);
	}
	
	public List<Role> findAll() {
		return roleDao.findAll(Role.class);
	}
	
	public List<Role> findAll(Integer page, Integer pageSize) {
		return roleDao.findAll(Role.class, page, pageSize);
	}
	
	public long count() {
		return roleDao.count(Role.class);
	}
	
	public void saveOrUpdate(Role role) {
		roleDao.saveOrUpdate(role);
	}
	
	public void delete(Long roleId) {
		roleDao.delete(findById(roleId));
	}
}
