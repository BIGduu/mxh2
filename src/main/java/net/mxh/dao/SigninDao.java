package net.mxh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Signin;
import net.mxh.util.DateUtil;

@Repository
public class SigninDao extends BaseDao<Signin> {
	
	@SuppressWarnings("unchecked")
	public List<Long> findBy() {
		Criteria criteria = currentSession().createCriteria(Signin.class);
		criteria.add(Restrictions
			.between("signTime", DateUtil.getYesterday(19, 0).getTime(), DateUtil.getYesterday(22, 0).getTime()));
		criteria.setProjection(Projections.property("userId"));
		return criteria.list();
	}
	
	public Long countByTime(Long userId) {
		Criteria criteria = currentSession().createCriteria(Signin.class);
		criteria.add(Restrictions.between("signTime", DateUtil.getToday(19, 0), DateUtil.getToday(22, 0)));
		criteria.add(Restrictions.eq("userId", userId));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	public Long countByYesterday(Long userId) {
		Criteria criteria = currentSession().createCriteria(Signin.class);
		criteria.add(Restrictions
			.between("signTime", DateUtil.getYesterday(19, 0).getTime(), DateUtil.getYesterday(22, 0).getTime()));
		criteria.add(Restrictions.eq("userId", userId));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
}
