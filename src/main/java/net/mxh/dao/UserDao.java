package net.mxh.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.User;
import net.mxh.util.CategoryUtil;

@Repository
public class UserDao extends BaseDao<User> {
	
	@SuppressWarnings("unchecked")
	public List<User> findByUsername(String username, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(User.class);
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	public Long countByUsername(String username) {
		Criteria criteria = currentSession().createCriteria(User.class);
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	/**
	 * @author bigduu
	 */

	public List<User> findByStoresId(Long storesId,Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(User.class);
		if (null != storesId) {
			criteria.add(Restrictions.eq("storesId", storesId));
		}
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	/**
	 * @author bigduu
	 */

	public Long countByStoresId(Long storesId) {
		Criteria criteria = currentSession().createCriteria(User.class);
		if (null != storesId) {
			criteria.add(Restrictions.eq("storesId", storesId));
		}
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findByUsername(String username, Long storesId, Long departmentId, Integer page,
		Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(User.class);
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		if (null != storesId) {
			criteria.add(Restrictions.eq("storesId", storesId));
		}
		criteria.add(Restrictions.eq("departmentId", departmentId));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	public Long countByUsername(String username, Long storesId, Long departmentId) {
		Criteria criteria = currentSession().createCriteria(User.class);
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		if (null != storesId) {
			criteria.add(Restrictions.eq("storesId", storesId));
		}
		criteria.add(Restrictions.eq("departmentId", departmentId));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findDelieveryByUsername(String username, Long departmentId, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(User.class);
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		criteria.add(Restrictions.eq("departmentId", departmentId));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	public Long countDeliveryByUsername(String username, Long departmentId) {
		Criteria criteria = currentSession().createCriteria(User.class);
		criteria.add(Restrictions.like("username", "%" + username + "%"));
		criteria.add(Restrictions.eq("departmentId", departmentId));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<User> findByIds(List<Long> ids) {
		Criteria criteria = currentSession().createCriteria(User.class);
		if (!ids.isEmpty()) {
			criteria.add(Restrictions.in("id", ids));
		}
		else {
			return new ArrayList<>();
		}
		criteria.add(Restrictions.eq("status", CategoryUtil.USERSTATUS.THREE));
		return criteria.list();
	}
	
}
