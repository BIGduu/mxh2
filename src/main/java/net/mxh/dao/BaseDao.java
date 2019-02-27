package net.mxh.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import net.mxh.vo.PageDTO;

public class BaseDao<T> extends HibernateDaoSupport {
	
	@Resource(name = "sessionFactory")
	public void setSuperSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	public void save(T entity) {
		currentSession().save(entity);
	}
	
	public void saveOrUpdate(T entity) {
		currentSession().saveOrUpdate(entity);
	}
	
	public void update(T entity) {
		currentSession().update(entity);
	}
	
	public void delete(T entity) {
		currentSession().delete(entity);
	}
	
	public void deleteAll(List<T> entities) {
		getHibernateTemplate().deleteAll(entities);
	}
	
	@SuppressWarnings("unchecked")
	public T findById(Class<T> entityClass, Serializable id) {
		return (T)currentSession().get(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> entityClass) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> entityClass, String orderColumn, boolean isAsc) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		if (isAsc) {
			criteria.addOrder(Order.asc(orderColumn));
		} else {
			criteria.addOrder(Order.desc(orderColumn));
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> entityClass, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAll(Class<T> entityClass, String orderColumn, boolean isAsc, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		if (isAsc) {
			criteria.addOrder(Order.asc(orderColumn));
		} else {
			criteria.addOrder(Order.desc(orderColumn));
		}
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByParam(Class<T> entityClass, Map<String, Object> param, String orderColumn, boolean isAsc) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		if (isAsc) {
			criteria.addOrder(Order.asc(orderColumn));
		} else {
			criteria.addOrder(Order.desc(orderColumn));
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByParam(Class<T> entityClass, Map<String, Object> param) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByParam(Class<T> entityClass, Map<String, Object> param, String orderColumn, boolean isAsc,
		Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		if (isAsc)
			criteria.addOrder(Order.asc(orderColumn));
		else
			criteria.addOrder(Order.desc(orderColumn));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findByParam(Class<T> entityClass, Map<String, Object> param, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	public PageDTO<T> findPageListByParam(Class<T> entityClass, Map<String, Object> param, String orderColumn,
		boolean isAsc, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		if (isAsc) {
			criteria.addOrder(Order.asc(orderColumn));
		}
		else {
			criteria.addOrder(Order.desc(orderColumn));
		}
		
		Integer total = criteria.list().size();
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		PageDTO<T> pageDTO = new PageDTO<T>();
		pageDTO.setTotal(total);
		pageDTO.setList(criteria.list());
		return pageDTO;
	}
	
	@SuppressWarnings("unchecked")
	public T findOne(Class<T> entityClass, Map<String, Object> param) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		List<T> list = criteria.list();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	public Long count(Class<T> entityClass) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	public Long countByParam(Class<T> entityClass, Map<String, Object> param) {
		Criteria criteria = currentSession().createCriteria(entityClass);
		for (String key : param.keySet()) {
			Object value = param.get(key);
			if (null != value)
				criteria.add(Restrictions.eq(key, value));
		}
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
}
