package net.mxh.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.UserDao;
import net.mxh.entity.User;
import net.mxh.vo.PageDTO;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private SigninService signinService;
	
	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	public void save(User entity) {
		userDao.save(entity);
	}
	
	public void update(User entity) {
		userDao.update(entity);
	}
	
	public void delete(User entity) {
		userDao.delete(entity);
	}
	
	public User findById(Long id) {
		return userDao.findById(User.class, id);
	}
	
	public List<User> findAll() {
		return userDao.findAll(User.class);
	}
	
	public List<User> findAll(Integer page, Integer pageSize) {
		return userDao.findAll(User.class, "createTime", false, page, pageSize);
	}
	
	public List<User> findByUsername(String username, Integer page, Integer pageSize) {
		return userDao.findByUsername(username, page, pageSize);
	}
	
	public List<User> findByUsername(String username, Long storesId, Long departmentId, Integer page,
		Integer pageSize) {
		return userDao.findByUsername(username, storesId, departmentId, page, pageSize);
	}
	/**
	 * @author bigduu
	 */

	public List<User> findByStoresId(Long storesId, Integer page,
										Integer pageSize){
		Map<String, Object> param = new HashMap<>();
		if (null != storesId){
			param.put("storesId", storesId);
		}else {
			return null;
		}
		List<User> users = userDao.findByStoresId(storesId,page,pageSize);

		return users;
	}


	public List<User> findDeliveryByUsername(String username, Long departmentId, Integer page, Integer pageSize) {
		return userDao.findDelieveryByUsername(username, departmentId, page, pageSize);
	}
	
	public long count() {
		return userDao.count(User.class);
	}
	
	public Long countByUsername(String username) {
		return userDao.countByUsername(username);
	}

	public Long countByStoresId(Long storesId) {
		return userDao.countByStoresId(storesId);
	}
	
	public Long countByUsername(String username, Long storesId, Long departmentId) {
		return userDao.countByUsername(username, storesId, departmentId);
	}
	
	public Long countDeliveryByUsername(String username, Long departmentId) {
		return userDao.countDeliveryByUsername(username, departmentId);
	}
	
	public void saveOrUpdate(User user) {
		userDao.saveOrUpdate(user);
	}
	
	public void delete(Long userId) {
		userDao.delete(findById(userId));
	}
	
	public PageDTO<User> findByStoresIdAndDepartMentId(Long storesId, Long departmentId, Integer page,
		Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		if (null != storesId) {
			param.put("storesId", storesId);
		}
		param.put("departmentId", departmentId);
		return userDao.findPageListByParam(User.class, param, "createTime", false, page, pageSize);
	}


	
	public PageDTO<User> findByDepartmentId(Long departmentId, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("departmentId", departmentId);
		return userDao.findPageListByParam(User.class, param, "createTime", false, page, pageSize);
	}
	
	public List<User> findByDepartmentId(Long departmentId) {
		Map<String, Object> param = new HashMap<>();
		param.put("departmentId", departmentId);
		return userDao.findByParam(User.class, param, "createTime", false);
	}
	
	public List<User> findByIdAndStoresId(Long departmentId, Long storesId) {
		Map<String, Object> param = new HashMap<>();
		param.put("departmentId", departmentId);
		if (null != storesId) {
			param.put("storesId", storesId);
		}
		return userDao.findByParam(User.class, param, "createTime", false);
	}
	
	/**
	 * @description 可用的司机
	 * @author ZhongHan
	 * @date 2017年11月29日
	 * @return
	 */
	public List<User> findCanUse() {
		List<Long> userIds = signinService.findBy();
		List<Long> userIdsno = deliveryOrderService.findNotCanUse();
		List<Long> userIdCanUse = new ArrayList<>();
		for (Long userId : userIds) {
			if (!userIdsno.contains(userId))
				userIdCanUse.add(userId);
		}
		return userDao.findByIds(userIdCanUse);
		// return userDao.findAll(User.class);
	}
	
	public Long countByTelephone(String telephone) {
		Map<String, Object> param = new HashMap<>();
		param.put("telephone", telephone);
		return userDao.countByParam(User.class, param);
	}
	
	public User findByOpenid(String openid) {
		Map<String, Object> param = new HashMap<>();
		param.put("openid", openid);
		return userDao.findOne(User.class, param);
	}
}
