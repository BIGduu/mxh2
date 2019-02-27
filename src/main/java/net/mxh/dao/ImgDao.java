package net.mxh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Img;

@Repository
public class ImgDao extends BaseDao<Img> {
	@SuppressWarnings("unchecked")
	public List<Img> findByOrderId(Long orderId, Integer type) {
		Criteria criteria = currentSession().createCriteria(Img.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.add(Restrictions.eq("type", type));
		return criteria.list();
	}
}
