package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.DepartmentDao;
import net.mxh.entity.Department;

@Service
@Transactional
public class DepartmentService {
	
	@Autowired
	private DepartmentDao departmentDao;
	
	public void save(Department entity) {
		departmentDao.save(entity);
	}
	
	public void update(Department entity) {
		departmentDao.update(entity);
	}
	
	public void delete(Department entity) {
		departmentDao.delete(entity);
	}
	
	public Department findById(Long id) {
		return departmentDao.findById(Department.class, id);
	}
	
	public List<Department> findAll() {
		return departmentDao.findAll(Department.class);
	}
	
	public List<Department> findAll(Integer page, Integer pageSize) {
		return departmentDao.findAll(Department.class, page, pageSize);
	}
	
	public long count() {
		return departmentDao.count(Department.class);
	}
	
	public void saveOrUpdate(Department department) {
		departmentDao.saveOrUpdate(department);
	}
	
	public void delete(Long departmentId) {
		departmentDao.delete(findById(departmentId));
	}
}
