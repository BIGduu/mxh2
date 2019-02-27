package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.SigninDao;
import net.mxh.entity.Signin;

@Service
@Transactional
public class SigninService {
	
	@Autowired
	private SigninDao signinDao;
	
	public void save(Signin entity) {
		signinDao.save(entity);
	}
	
	public void update(Signin entity) {
		signinDao.update(entity);
	}
	
	public void delete(Signin entity) {
		signinDao.delete(entity);
	}
	
	public Signin findById(Long id) {
		return signinDao.findById(Signin.class, id);
	}
	
	public List<Signin> findAll() {
		return signinDao.findAll(Signin.class);
	}
	
	public List<Signin> findAll(Integer page, Integer pageSize) {
		return signinDao.findAll(Signin.class, page, pageSize);
	}
	
	public long count() {
		return signinDao.count(Signin.class);
	}
	
	public void saveOrUpdate(Signin signin) {
		signinDao.saveOrUpdate(signin);
	}
	
	public void delete(Long signinId) {
		signinDao.delete(findById(signinId));
	}
	
	public List<Long> findBy() {
		return signinDao.findBy();
	}
	
	public Long countByTime(Long userId) {
		return signinDao.countByTime(userId);
	}
	
	public Long countByYesterday(Long userId) {
		return signinDao.countByYesterday(userId);
	}
}
