package net.mxh.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.TheOrder;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.StringUtil;
import net.mxh.vo.OrderPageDTO;
//
@SuppressWarnings ("Duplicates")
@Repository
public class OrderDao extends BaseDao<TheOrder> {
	
	@SuppressWarnings("unchecked")
	public List<TheOrder> findUnFinish(Long managerId, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		criteria.add(Restrictions.eq("managerId", managerId));
		criteria.add(Restrictions.not(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN)));
		criteria.addOrder(Order.desc("placeOrderTime"));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<TheOrder> findByStates(Set<Integer> stateList, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		criteria.add(Restrictions.in("state", stateList));
		criteria.addOrder(Order.desc("placeOrderTime"));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public OrderPageDTO findBy(Set<Integer> stateList, Map<String, String> param, Integer page, Integer pageSize,
		List<String> orderIds) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		criteria.add(Restrictions.in("state", stateList));
		
		if (null != param.get("type")) {
			Integer type = Integer.parseInt(param.get("type"));
			if (type == 1) {
				criteria.add(Restrictions.not(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN)));
			}
			else if (type == 2) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", CategoryUtil.CHECKSTATUS.THREE));
			}else if(type == 3 && param.get("checkState") != null && !param.get("checkState").equals("") ){
				Integer checkState = Integer.parseInt(param.get("checkState"));
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", checkState));
			}
			else if (type == 3) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.in("checkState",
					new Object[] {CategoryUtil.CHECKSTATUS.ZERO, CategoryUtil.CHECKSTATUS.THREE}));
			}
			else if (type == 4) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", CategoryUtil.CHECKSTATUS.ONE));
			}
			else if (type == 5) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", CategoryUtil.CHECKSTATUS.TOW));
			}
		}
		
		if (!orderIds.isEmpty()) {
			criteria.add(Restrictions.in("id", orderIds));
		}

		if (StringUtil.isNotEmpty(param.get("orderCode"))) {
			criteria.add(Restrictions.eq("orderCode", param.get("orderCode")));
		}

		/**
		 * @author bigduu
		 */
		if (StringUtil.isNotEmpty(param.get("managerName"))) {
			criteria.add(Restrictions.eq("managerName", param.get("managerName")));
		}

		if (StringUtil.isNotEmpty(param.get("state"))) {
			criteria.add(Restrictions.eq("state", Integer.parseInt(param.get("state"))));
		}

		if (StringUtil.isNotEmpty(param.get("storesId"))) {
			criteria.add(Restrictions.eq("storesId", Long.parseLong(param.get("storesId"))));
		}

		if (StringUtil.isNotEmpty(param.get("beginTime"))) {
			criteria
				.add(Restrictions.ge("placeOrderTime", DateUtil.getDateTimeFormat(param.get("beginTime")).getTime()));
		}

		if (StringUtil.isNotEmpty(param.get("endTime"))) {
			criteria.add(Restrictions.lt("placeOrderTime", DateUtil.getDateTimeFormat(param.get("endTime")).getTime()));
		}

		if (StringUtil.isNotEmpty(param.get("receivingAddress"))) {
			criteria.add(Restrictions.like("receivingAddress", "%" + param.get("receivingAddress") + "%"));
		}

		criteria.addOrder(Order.desc("placeOrderTime"));
		Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		criteria.setProjection(null);
		Integer total = criteria.list().size();
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		OrderPageDTO orderPage = new OrderPageDTO();
		orderPage.setTotal(total);
		orderPage.setOrders(criteria.list());
		orderPage.setCount(count);
		return orderPage;
	}

	@SuppressWarnings("unchecked")
	public OrderPageDTO findBy(Map<String, String> param, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		
		if (null != param.get("type")) {
			Integer type = Integer.parseInt(param.get("type"));
			if (type == 1) {
				criteria.add(Restrictions.not(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN)));
			}
			else if (type == 2) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", CategoryUtil.CHECKSTATUS.THREE));
			}
			else if (type == 3) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.in("checkState",
					new Object[] {CategoryUtil.CHECKSTATUS.ZERO, CategoryUtil.CHECKSTATUS.THREE}));
			}
			else if (type == 4) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", CategoryUtil.CHECKSTATUS.ONE));
			}
			else if (type == 5) {
				criteria.add(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.SEVEN));
				criteria.add(Restrictions.eq("checkState", CategoryUtil.CHECKSTATUS.TOW));
			}
		}

		if (StringUtil.isNotEmpty(param.get("orderCode"))) {
			criteria.add(Restrictions.eq("orderCode", param.get("orderCode")));
		}

		if (StringUtil.isNotEmpty(param.get("managerId"))) {
			criteria.add(Restrictions.eq("managerId", Long.parseLong(param.get("managerId"))));
		}

		if (StringUtil.isNotEmpty(param.get("state"))) {
			criteria.add(Restrictions.eq("state", Integer.parseInt(param.get("state"))));
		}

		if (StringUtil.isNotEmpty(param.get("storesId"))) {
			criteria.add(Restrictions.eq("storesId", Long.parseLong(param.get("storesId"))));
		}

		if (StringUtil.isNotEmpty(param.get("beginTime"))) {
			criteria
				.add(Restrictions.ge("placeOrderTime", DateUtil.getDateTimeFormat(param.get("beginTime")).getTime()));
		}

		if (StringUtil.isNotEmpty(param.get("endTime"))) {
			criteria.add(Restrictions.lt("placeOrderTime", DateUtil.getDateTimeFormat(param.get("endTime")).getTime()));
		}

		if (StringUtil.isNotEmpty(param.get("receivingAddress"))) {
			criteria.add(Restrictions.like("receivingAddress", "%" + param.get("receivingAddress") + "%"));
		}

		criteria.addOrder(Order.desc("placeOrderTime"));

		Integer total = criteria.list().size();

		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);

		OrderPageDTO orderPage = new OrderPageDTO();
		orderPage.setTotal(total);
		orderPage.setOrders(criteria.list());
		return orderPage;
	}
	
	public Long countByStates(String[] stateList) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		if (stateList.length > 0) {
			criteria.add(Restrictions.in("state", stateList));
		}
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	public Long countToday(Long storesId) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		criteria.add(Restrictions.not(Restrictions.eq("state", CategoryUtil.ORDERSTATUS.ONE)));
		criteria.add(Restrictions
			.between("placeOrderTime", DateUtil.getBeginTimeOfDate().getTime(), DateUtil.getEndTimeOfDate().getTime()));
		if (null != storesId) {
			criteria.add(Restrictions.eq("storesId", "storesId"));
		}
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<TheOrder> findUnconfirmed() {
		String hql = "from TheOrder where senttoTime < :senttoTime and  state = :six";
		Query query = currentSession().createQuery(hql);
		query.setParameter("senttoTime", DateUtil.getBeginTimeOfDate().getTime());
		query.setParameter("six", CategoryUtil.ORDERSTATUS.SIX);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> findBy(Long startTime, Long endTime) {
		String sql =
			"select s.stores_name, SUM(o.all_price) from stores s LEFT JOIN t_order o on s.id = o.stores_id where o.order_type = "
				+ CategoryUtil.ORDERTYPE.ONE + " and o.state = " + CategoryUtil.ORDERSTATUS.SEVEN
				+ " and place_order_time BETWEEN " + startTime + " and " + endTime + " GROUP BY s.id";
		Query query = currentSession().createSQLQuery(sql);
		List<Object[]> objects = query.list();
		List<Map<String, String>> list = new ArrayList<>();
		for (Object[] object : objects) {
			Map<String, String> map = new HashMap<>();
			map.put("storesName", StringUtil.toString(object[0]));
			map.put("allPrice", StringUtil.toString(object[1]));
			list.add(map);
		}
		return list;
	}
	
	public BigDecimal totalAmount(Long orderId) {
		String sql =
			"select o.all_price + sum(shippingcosts) + sum(upstaircosts) from t_order o LEFT JOIN delivery_order d on o.id = d.order_id where o.id = "
				+ orderId;
		Query query = currentSession().createSQLQuery(sql);
		Object object = query.uniqueResult();
		if (null == object) {
			return new BigDecimal(0);
		}
		return new BigDecimal(object.toString());
	}
	
	public BigDecimal totalAmountForDriver(Long orderId) {
		String sql =
			"select o.all_price from t_order o LEFT JOIN delivery_order d on o.id = d.order_id where o.id = " + orderId;
		Query query = currentSession().createSQLQuery(sql);
		Object object = query.uniqueResult();
		if (null == object) {
			return new BigDecimal(0);
		}
		return new BigDecimal(object.toString());
	}
	
	@SuppressWarnings("unchecked")
	public BigDecimal totalAmounts(String orderIdStr) {
		// DOTO sql 注入
		String sql =
			"select o.all_price + sum(shippingcosts) + sum(upstaircosts) from t_order o LEFT JOIN delivery_order d on o.id = d.order_id where o.id in ("
				+ orderIdStr + ") GROUP BY order_id";
		Query query = currentSession().createSQLQuery(sql);
		List<Object> object = query.list();
		BigDecimal total = new BigDecimal(0);
		for (Object amount : object) {
			if (null != amount) {
				total = total.add(new BigDecimal(amount.toString()));
			}
		}
		return total;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> addressList(Long managerId) {
		Criteria criteria = currentSession().createCriteria(TheOrder.class);
		criteria.add(Restrictions.eq("managerId", managerId));
		criteria.setProjection(Projections.distinct(Projections.property("receivingAddress")));
		criteria.addOrder(Order.desc("placeOrderTime"));
		criteria.setFirstResult(0).setMaxResults(5);
		return criteria.list();
	}
}
