package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.ImgDao;
import net.mxh.entity.Img;

@Service
@Transactional
public class ImgService {
	@Autowired
	private ImgDao imgDao;
	
	public void save(Img entity) {
		imgDao.save(entity);
	}
	
	public void update(Img entity) {
		imgDao.update(entity);
	}
	
	public Img findById(Long id) {
		return imgDao.findById(Img.class, id);
	}
	
	public List<Img> findAll() {
		return imgDao.findAll(Img.class);
	}
	
	public List<Img> findAll(Integer page, Integer pageSize) {
		return imgDao.findAll(Img.class, page, pageSize);
	}
	
	public List<Img> findByOrderId(Long orderId, Integer type) {
		return imgDao.findByOrderId(orderId, type);
	}
	
}
