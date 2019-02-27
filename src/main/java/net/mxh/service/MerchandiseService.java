package net.mxh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.MerchandiseDao;
import net.mxh.entity.Merchandise;

@Service
@Transactional
public class MerchandiseService {
	
	@Autowired
	private MerchandiseDao merchandiseDao;
	
	public void save(Merchandise entity) {
		merchandiseDao.save(entity);
	}
	
	public void update(Merchandise entity) {
		merchandiseDao.update(entity);
	}
	
	public void delete(Merchandise entity) {
		merchandiseDao.delete(entity);
	}
	
	public Merchandise findById(Long id) {
		return merchandiseDao.findById(Merchandise.class, id);
	}
	
	public List<Merchandise> findAll() {
		return merchandiseDao.findAll(Merchandise.class);
	}
	
	public List<Merchandise> findAll(Integer page, Integer pageSize) {
		return merchandiseDao.findAll(Merchandise.class, page, pageSize);
	}

	public List<Merchandise> findByIsUse(Integer isUse){
		return merchandiseDao.findByIsUse(isUse);
	}
	
	public List<Merchandise> findByIsUse(Integer isUse, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("isUse", isUse);
		return merchandiseDao.findByParam(Merchandise.class, param, "createTime", false, page, pageSize);
	}
	
	public List<Merchandise> findByIsUse(Integer isUse, Long storesId) {
		Map<String, Object> param = new HashMap<>();
		param.put("isUse", isUse);
		return merchandiseDao.findByIsUse(isUse, storesId);
	}
	
	public long count() {
		return merchandiseDao.count(Merchandise.class);
	}
	
	public void saveOrUpdate(Merchandise merchandise) {
		merchandiseDao.saveOrUpdate(merchandise);
	}
	
	public void delete(Long merchandiseId) {
		merchandiseDao.delete(findById(merchandiseId));
	}
}
