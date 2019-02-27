package net.mxh.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.TheOrder;
import net.mxh.util.CategoryUtil;
import net.mxh.util.StringUtil;

@Repository
public class DeliveryOrderDao extends BaseDao<DeliveryOrder> {
	
	@SuppressWarnings("unchecked")
	public List<Long> findNotCanUse() {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.FIVE));
		criteria.setProjection(Projections.property("deliveryId"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryOrder> findByStateAndOrderType(Integer state, Integer orderType, Integer page,
		Integer pageSize) {
		String sql = "select d.* from delivery_order d LEFT JOIN t_order o on d.order_id = o.id where d.state = "
			+ state + " and o.order_type = " + orderType + " ORDER BY d.id DESC";
		Query query = currentSession().createSQLQuery(sql).addEntity("d", DeliveryOrder.class);
		query.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		List<Object> list = query.list();
		List<DeliveryOrder> deliveryOrders = new ArrayList<>();
		for (Object object : list) {
			deliveryOrders.add((DeliveryOrder)object);
		}
		return deliveryOrders;
	}

	/**
	 *
	 * @author bigduu
	 * @param orderId
	 * @param state
	 * @return
	 */
	public Long countByOrderIdAndStateNot(Long orderId, Integer state) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.add(Restrictions.not(Restrictions.eq("state", state)));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	public Long countByOrderIdAndId(Long orderId, Long id) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.add(Restrictions.le("id", id));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	public BigDecimal sumByOrderId(Long orderId) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.setProjection(Projections.sum("shippingcosts"));
		Object object = criteria.uniqueResult();
		if (null != object)
			return new BigDecimal(object.toString());
		return new BigDecimal(0);
	}
	
	public BigDecimal sumUpstairCostsByOrderId(Long orderId) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.setProjection(Projections.sum("upstaircosts"));
		Object object = criteria.uniqueResult();
		if (null != object)
			return new BigDecimal(object.toString());
		return new BigDecimal(0);
	}
	
	public BigDecimal sumUpstaircosts(Long orderId) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.setProjection(Projections.sum("upstaircosts"));
		Object object = criteria.uniqueResult();
		if (null != object)
			return new BigDecimal(object.toString());
		return new BigDecimal(0);
	}
	
	public Long countByStateAndOrderType(Integer state, Integer orderType) {
		String sql = "select count(*) from delivery_order d LEFT JOIN t_order o on d.order_id = o.id where d.state = "
			+ state + " and o.order_type = " + orderType;
		Query query = currentSession().createSQLQuery(sql);
		Long count = Long.parseLong(query.uniqueResult().toString());
		return count;
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryOrder> findByUnFinsh(Long deliveryId, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("deliveryId", deliveryId));
		criteria.add(Restrictions.not(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN)));
		criteria.addOrder(Order.desc("id"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryOrder> findByFinsh(Long deliveryId, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("deliveryId", deliveryId));
		criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findNameByOrderId(Long orderId) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.setProjection(Projections.property("deliveryName"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryOrder> findByOrderId(Long orderId) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<DeliveryOrder> findBy(Long storesId, Integer page, Integer pageSize) {
		String sql =
			"select d.*,o.* from delivery_order d LEFT JOIN t_order o on d.order_id = o.id where ((o.order_type = "
				+ CategoryUtil.ORDERTYPE.ONE + " and d.state = " + CategoryUtil.ORDERSTATUS.FOUR
				+ ") or (o.order_type = " + CategoryUtil.ORDERTYPE.TOW + " and d.state = "
				+ CategoryUtil.ORDERSTATUS.EIGHT + "))";
		if (null != storesId)
			sql += " and o.stores_id = " + storesId;
		sql += " ORDER BY d.id DESC";
		Query query =
			currentSession().createSQLQuery(sql).addEntity("d", DeliveryOrder.class).addEntity("o", TheOrder.class);
		query.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		List<Object[]> list = query.list();
		List<DeliveryOrder> deliveryOrders = new ArrayList<>();
		for (Object[] object : list) {
			DeliveryOrder deliveryOrder = (DeliveryOrder)object[0];
			TheOrder theOrder = (TheOrder)object[1];
			deliveryOrder.setOrderType(theOrder.getOrderType());
			deliveryOrder.setReceivingAddress(theOrder.getReceivingAddress());
			deliveryOrders.add(deliveryOrder);
		}
		return deliveryOrders;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findBy(Long startTime, Long endTime) {
		String sql =
			"select d.delivery_name, sum(shippingcosts) from delivery_order d LEFT JOIN t_order o on d.order_id = o.id where o.place_order_time BETWEEN "
				+ startTime + " and " + endTime + " and d.state = " + CategoryUtil.ORDERSTATUS.SIX
				+ " GROUP BY d.delivery_id";
		Query query = currentSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Map<String, String>> list = new ArrayList<>();
		for (Object[] object : objects) {
			Map<String, String> map = new HashMap<>();
			map.put("deliveryName", StringUtil.toString(object[0]));
			map.put("shippingcosts", StringUtil.toString(object[1]));
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findBy(String deliveryName) {
		Criteria criteria = currentSession().createCriteria(DeliveryOrder.class);
		criteria.add(Restrictions.eq("deliveryName", deliveryName));
		criteria.setProjection(Projections.property("orderId"));
		return criteria.list();
	}
}
