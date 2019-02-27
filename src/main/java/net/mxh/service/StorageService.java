package net.mxh.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.StorageDao;
import net.mxh.entity.Merchandise;
import net.mxh.entity.Storage;
import net.mxh.util.CategoryUtil;

@Service
@Transactional
public class StorageService {
	
	@Autowired
	private StorageDao storageDao;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	public void save(Storage storage, Merchandise merchandise) {
		storageDao.save(storage);
		if (storage.getType() == CategoryUtil.STORAGETYPE.ONE) {
			merchandise.setTotal(merchandise.getTotal().add(storage.getNumber()));
		}
		else {
			merchandise.setTotal(merchandise.getTotal().subtract(storage.getNumber()));
		}
		
		merchandiseService.update(merchandise);
	}
	
	public void save(Storage storage) {
		storageDao.save(storage);
		Merchandise merchandise = merchandiseService.findById(storage.getMerchandiseId());
		if (storage.getType() == CategoryUtil.STORAGETYPE.ONE) {
			merchandise.setTotal(merchandise.getTotal().add(storage.getNumber()));
		}
		else {
			merchandise.setTotal(merchandise.getTotal().subtract(storage.getNumber()));
		}
		
		merchandiseService.update(merchandise);
	}
	
	public void update(Storage entity) {
		storageDao.update(entity);
	}
	
	public void delete(Storage entity) {
		storageDao.delete(entity);
	}
	
	public Storage findById(Long id) {
		return storageDao.findById(Storage.class, id);
	}
	
	public List<Storage> findAll() {
		return storageDao.findAll(Storage.class);
	}
	
	public List<Storage> findAll(Integer page, Integer pageSize) {
		return storageDao.findAll(Storage.class, page, pageSize);
	}
	
	public long count() {
		return storageDao.count(Storage.class);
	}
	
	public void saveOrUpdate(Storage storage) {
		storageDao.saveOrUpdate(storage);
	}
	
	public void delete(Long storageId) {
		storageDao.delete(findById(storageId));
	}
	
	public BigDecimal sumBy(Long merchandiseId, Integer type) {
		return storageDao.sumBy(merchandiseId, type);
	}
	
	public BigDecimal sumBy(Long merchandiseId, Integer type, Long beginTime, Long endTime) {
		return storageDao.sumBy(merchandiseId, type, beginTime, endTime);
	}
	
	public List<Storage> findBy(Integer type, Long beginTime, Long endTime) {
		return storageDao.findBy(type, beginTime, endTime);
	}
	
	public List<Map<String, String>> findBy(Long beginTime, Long endTime) {
		return storageDao.findBy(beginTime, endTime);
	}
}
