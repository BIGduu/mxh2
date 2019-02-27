package net.mxh.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Storage;
import net.mxh.util.CategoryUtil;
import net.mxh.util.StringUtil;

@Repository
public class StorageDao extends BaseDao<Storage> {
	/**
	 * @author bigduu
	 */
	public BigDecimal sumBy(Long merchandiseId, Integer type) {
		Criteria criteria = currentSession().createCriteria(Storage.class);
		criteria.add(Restrictions.eq("merchandiseId", merchandiseId));
		criteria.add(Restrictions.eq("type", type));
		criteria.setProjection(Projections.sum("number"));
		Object sum = criteria.uniqueResult();
		if (null == sum) {
			return new BigDecimal(0);
		}
		return new BigDecimal(sum.toString());
	}
	
	public BigDecimal sumBy(Long merchandiseId, Integer type, Long beginTime, Long endTime) {
		Criteria criteria = currentSession().createCriteria(Storage.class);
		criteria.add(Restrictions.eq("merchandiseId", merchandiseId));
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.between("createTime", beginTime, endTime));
		criteria.setProjection(Projections.sum("number"));
		Object sum = criteria.uniqueResult();
		if (null == sum)
			return new BigDecimal(0);
		return new BigDecimal(sum.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Storage> findBy(Integer type, Long beginTime, Long endTime) {
		Criteria criteria = currentSession().createCriteria(Storage.class);
		criteria.add(Restrictions.eq("type", type));
		criteria.add(Restrictions.between("createTime", beginTime, endTime));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * @author bigduu
	 */
	public List<Map<String, String>> findBy(Long beginTime, Long endTime) {
//		String sql =
//			"select merchandise_name merchandiseName, sum(billing_quantity) billingQuantitySum, billing_unit billingUnit, sum(purchase_price * billing_quantity) purchasePriceSum  from `storage` where type = "
//				+ CategoryUtil.STORAGETYPE.ONE + " GROUP BY merchandise_id";
		String sql =
			"select merchandise_name merchandiseName, sum(billing_quantity) billingQuantitySum, billing_unit billingUnit, sum(purchase_price * billing_quantity) purchasePriceSum  from `storage` where type = "
				+ CategoryUtil.STORAGETYPE.ONE + " and create_time BETWEEN " + beginTime + " and " + endTime
				+ " GROUP BY merchandise_id";
		
		Query query = currentSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Map<String, String>> list = new ArrayList<>();
		for (Object[] object : objects) {
			Map<String, String> map = new HashMap<>();
			map.put("merchandiseName", StringUtil.toString(object[0]));
			map.put("billingQuantitySum", StringUtil.toString(object[1]));
			map.put("billingUnit", StringUtil.toString(object[2]));
			map.put("purchaseAmountSum", StringUtil.toString(object[3]));
			list.add(map);
		}
		return list;
	}
	
}
