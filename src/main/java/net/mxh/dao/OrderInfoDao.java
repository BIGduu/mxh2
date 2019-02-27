package net.mxh.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.admin.main.bean.OrderStat;
import net.mxh.entity.OrderInfo;
import net.mxh.util.CategoryUtil;
import net.mxh.util.StringUtil;

@Repository
public class OrderInfoDao extends BaseDao<OrderInfo> {
	
	public void deleteByOrderId(Long orderId) {
		String hql = "delete OrderInfo where orderId = :orderId";
		Query query = currentSession().createQuery(hql);
		query.setParameter("orderId", orderId);
		query.executeUpdate();
	}
	
	public BigDecimal sumBy(Long orderId, Integer state, Long merchandiseId) {
		Criteria criteria = currentSession().createCriteria(OrderInfo.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.add(Restrictions.eq("state", state));
		criteria.add(Restrictions.eq("merchandiseId", merchandiseId));
		criteria.setProjection(Projections.sum("number"));
		Object count = criteria.uniqueResult();
		if (null == count)
			return new BigDecimal(0);
		return new BigDecimal(count.toString());
	}
	
	public BigDecimal sumAllPriceBy(Long orderId, Integer state) {
		Criteria criteria = currentSession().createCriteria(OrderInfo.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		criteria.add(Restrictions.eq("state", state));
		criteria.setProjection(Projections.sum("allPrice"));
		Object sum = criteria.uniqueResult();
		if (null == sum)
			return new BigDecimal(0);
		return new BigDecimal(sum.toString());
	}
	
	public BigDecimal sumShippingcosts(Long deliveryOrderId) {
		String sql =
			"select sum(oi.number * m.shipping_cost) from order_info oi LEFT JOIN merchandise m on oi.merchandise_id = m.id where oi.delivery_order_id = "
				+ deliveryOrderId;
		Query query = currentSession().createSQLQuery(sql);
		Object object = query.uniqueResult();
		if (null == object)
			return new BigDecimal(0);
		return new BigDecimal(object.toString());
	}
	
	public BigDecimal sumUpstairsCost(Long deliveryOrderId) {
		String sql =
			"select sum(oi.number * m.upstairs_cost) from order_info oi LEFT JOIN merchandise m on oi.merchandise_id = m.id where oi.delivery_order_id = "
				+ deliveryOrderId;
		Query query = currentSession().createSQLQuery(sql);
		Object object = query.uniqueResult();
		if (null == object)
			return new BigDecimal(0);
		return new BigDecimal(object.toString());
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> orderInfoToday(Long startTime, Long endTime) {
		String sql =
			"select merchandise_id merchandiseId,sum(number) sum from order_info oi LEFT JOIN t_order o on oi.order_id = o.id WHERE oi.state = "
				+ CategoryUtil.ORDERINFOSTATE.ONE + " and o.place_order_time BETWEEN " + startTime + " and " + endTime
				+ " and o.order_type = " + CategoryUtil.ORDERTYPE.ONE + " GROUP BY oi.merchandise_id";
		Query query = currentSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Map<String, String>> list = new ArrayList<>();
		for (Object[] object : objects) {
			Map<String, String> map = new HashMap<>();
			map.put("merchandiseId", StringUtil.toString(object[0]));
			map.put("sum", StringUtil.toString(object[1]));
			list.add(map);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderStat> findCountList(Map<String, String> param) {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select o.merchandise_id,o.merchandise_name,CONVERT(o.number, SIGNED) number,o.all_price, ");
		sql.append("t.manager_id,t.manager_name,t.stores_id,t.stores_name, ");
		sql.append(
			"t.receiving_building_id,t.receiving_address,FROM_UNIXTIME(ROUND(t.create_time/1000),'%Y-%m-%d') modification_time ");
		sql.append(
			"from order_info o left join t_order t on o.order_id=t.id where t.state=7  and o.state=1 and t.receiving_building_id > 0 ");
		if (StringUtil.isNotEmpty(param.get("startTime"))) {
			sql.append(" AND  create_time >= " + param.get("startTime"));
		}
		
		if (StringUtil.isNotEmpty(param.get("endTime"))) {
			sql.append(" AND  endTime < " + param.get("endTime"));
		}
		
		if (StringUtil.isNotEmpty(param.get("receivingbuildingId"))) {
			sql.append(" AND  receiving_building_id = " + param.get("receivingbuildingId"));
		}
		
		if (StringUtil.isNotEmpty(param.get("managerId"))) {
			sql.append(" AND  manager_id = " + param.get("managerId"));
		}
		
		Query query = currentSession().createSQLQuery(sql.toString());
		List<Object[]> objects = query.list();
		List<OrderStat> list = new ArrayList<OrderStat>();
		for (Object[] object : objects) {
			// Map<String, String> map = new HashMap<>();
			// map.put("merchandise_id", StringUtil.toString(object[0]));
			// map.put("merchandise_name", StringUtil.toString(object[1]));
			// map.put("number", StringUtil.toString(object[2]));
			// map.put("all_price", StringUtil.toString(object[3]));
			// map.put("manager_id", StringUtil.toString(object[4]));
			// map.put("manager_name", StringUtil.toString(object[5]));
			// map.put("stores_id", StringUtil.toString(object[6]));
			// map.put("stores_name", StringUtil.toString(object[7]));
			// map.put("receiving_building_id", StringUtil.toString(object[8]));
			// map.put("receiving_address", StringUtil.toString(object[9]));
			// map.put("modification_time", StringUtil.toString(object[10]));
			// list.add(map);
			OrderStat ol = new OrderStat();
			ol.setMerchandiseId(StringUtil.toString(object[0]));
			ol.setMerchandiseName(StringUtil.toString(object[1]));
			ol.setNumber(BigInteger.valueOf((Long.valueOf(object[2].toString()))));
			ol.setAllPrice(StringUtil.toBigDecimal(object[3]));
			ol.setManagerId(StringUtil.toString(object[4]));
			ol.setManagerName(StringUtil.toString(object[5]));
			ol.setStoresId(StringUtil.toString(object[6]));
			ol.setStoresName(StringUtil.toString(object[7]));
			ol.setReceivingBuildingId(StringUtil.toString(object[8]));
			ol.setReceivingAddress(StringUtil.toString(object[9]));
			ol.setModificationTime(StringUtil.toString(object[10]));
			list.add(ol);
		}
		return list;
	}
	
}
