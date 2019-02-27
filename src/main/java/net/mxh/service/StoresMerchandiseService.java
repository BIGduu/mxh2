package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.StoresMerchandiseDao;
import net.mxh.entity.StoresNotUseMerchandise;

@Service
@Transactional
public class StoresMerchandiseService {
	
	@Autowired
	private StoresMerchandiseDao storesMerchandiseDao;
	
	public void save(StoresNotUseMerchandise entity) {
		storesMerchandiseDao.save(entity);
	}
	
	public void update(StoresNotUseMerchandise entity) {
		storesMerchandiseDao.update(entity);
	}
	
	public void delete(StoresNotUseMerchandise entity) {
		storesMerchandiseDao.delete(entity);
	}
	
	public StoresNotUseMerchandise findById(Long id) {
		return storesMerchandiseDao.findById(StoresNotUseMerchandise.class, id);
	}
	
	public List<StoresNotUseMerchandise> findAll() {
		return storesMerchandiseDao.findAll(StoresNotUseMerchandise.class);
	}
	
	public List<StoresNotUseMerchandise> findAll(Integer page, Integer pageSize) {
		return storesMerchandiseDao.findAll(StoresNotUseMerchandise.class, page, pageSize);
	}
	
	public long count() {
		return storesMerchandiseDao.count(StoresNotUseMerchandise.class);
	}
	
	public void saveOrUpdate(StoresNotUseMerchandise stores) {
		storesMerchandiseDao.saveOrUpdate(stores);
	}
	
	public void delete(Long id) {
		storesMerchandiseDao.delete(findById(id));
	}
	
	public void deleteAll(List<StoresNotUseMerchandise> list) {
		storesMerchandiseDao.deleteAll(list);
	}
	
	public List<StoresNotUseMerchandise> findByStoresIdAndMerchandiseId(Long storesId, Long merchandiseId) {
		return storesMerchandiseDao.findByStoresIdAndMerchandiseId(storesId, merchandiseId);
	}
	
	public List<StoresNotUseMerchandise> findByStoresId(Long storesId, Integer isUse) {
		return storesMerchandiseDao.findByStoresId(storesId, isUse);
	}
}
