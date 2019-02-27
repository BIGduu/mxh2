package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.StoresDao;
import net.mxh.entity.Stores;

@Service
@Transactional
public class StoresService {
	
	@Autowired
	private StoresDao storesDao;
	
	public void save(Stores entity) {
		storesDao.save(entity);
	}
	
	public void update(Stores entity) {
		storesDao.update(entity);
	}
	
	public void delete(Stores entity) {
		storesDao.delete(entity);
	}
	
	public Stores findById(Long id) {
		return storesDao.findById(Stores.class, id);
	}
	
	public List<Stores> findAll() {
		return storesDao.findAll(Stores.class);
	}
	
	public List<Stores> findAll(Integer page, Integer pageSize) {
		return storesDao.findAll(Stores.class, page, pageSize);
	}
	
	public long count() {
		return storesDao.count(Stores.class);
	}
	
	public void saveOrUpdate(Stores stores) {
		storesDao.saveOrUpdate(stores);
	}
	
	public void delete(Long storesId) {
		storesDao.delete(findById(storesId));
	}
}
