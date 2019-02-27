package net.mxh.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import net.mxh.admin.main.bean.OrderStat;
import net.mxh.util.StringUtil;

@Repository
public class OrderStatDao extends BaseDao<OrderStat> {
	
	@SuppressWarnings("unchecked")
	public List<OrderStat> getOrderList(List<OrderStat> list) {
		StringBuffer sql = new StringBuffer();
		sql.append(
			"SELECT t.stores_id,t.stores_name,t.manager_id,t.manager_name,t.receiving_building_id,t.receiving_address,FROM_UNIXTIME(ROUND(t.modification_time/1000),'%Y-%m-%d') modification_time,");
		for (OrderStat oi : list) {
			sql.append("MAX(CASE o.merchandise_name WHEN '")
				.append(oi.getMerchandiseName())
				.append("' THEN o.number ELSE 0 END ) '")
				.append(oi.getMerchandiseName() + "',");
		}
		sql.append(" sum(o.all_price) all_price ")
			.append("FROM order_info o LEFT JOIN t_order t ON o.order_id=t.id WHERE t.state=7 ")
			.append(
				"GROUP BY  t.stores_id,t.stores_name,t.manager_id,t.manager_name,t.receiving_address,FROM_UNIXTIME(ROUND(t.modification_time/1000),'%Y-%m-%d') ")
			.append(
				"ORDER BY t.stores_id,t.stores_name,t.manager_id,t.manager_name,t.receiving_address,DATE_FORMAT(FROM_UNIXTIME(ROUND(t.modification_time/1000),'%Y-%m-%d'),'%Y-%m-%d') ASC");
		System.out.println(sql.toString());
		
		// Query query = currentSession().createSQLQuery(sql.toString());
		// List<Object[]> objects = query.list();
		List<OrderStat> relist = new ArrayList<>();
		
		return relist;
		// for (Object[] object : objects) {
		// OrderStat ol = new OrderStat();
		// ol.setAllPrice(StringUtil.toString(object[0]));
		// ol.setManagerId(StringUtil.toString(object[1]));
		// ol.setManagerName(StringUtil.toString(object[2]));
		// ol.setStoresId(StringUtil.toString(object[3]));
		// ol.setStoresName(StringUtil.toString(object[4]));
		// ol.setReceivingBuildingId(StringUtil.toString(object[5]));
		// ol.setReceivingAddress(StringUtil.toString(object[6]));
		// ol.setModificationTime(StringUtil.toString(object[7]));
		// relist.add(ol);
		// }
		// return list;
		
		// return jdbcTemplate.query(sql.toString(), new RowMapper<OrderStat>() {
		// @Override
		// public OrderStat mapRow(ResultSet rs, int rowNum)
		// throws SQLException {
		// OrderStat ol = new OrderStat();
		// ol.setAllPrice(rs.getString("all_price"));
		// ol.setManagerId(rs.getString("manager_id"));
		// ol.setManagerName(rs.getString("manager_name"));
		// ol.setStoresId(rs.getString("stores_id"));
		// ol.setStoresName(rs.getString("stores_name"));
		// ol.setReceivingAddress(rs.getString("receiving_address"));
		// ol.setModificationTime(rs.getString("modification_time"));
		// return ol;
		// }
		// });
	}
	
	@SuppressWarnings("unchecked")
	public List<OrderStat> getMerchandiseList() {
		String sql = "SELECT distinct o.merchandise_id,o.merchandise_name "
			+ "FROM order_info o LEFT JOIN t_order t ON o.order_id=t.id WHERE t.state=7";
		Query query = currentSession().createSQLQuery(sql.toString());
		List<Object[]> objects = query.list();
		List<OrderStat> list = new ArrayList<>();
		for (Object[] object : objects) {
			OrderStat ol = new OrderStat();
			ol.setMerchandiseId(StringUtil.toString(object[0]));
			ol.setMerchandiseName(StringUtil.toString(object[1]));
			list.add(ol);
		}
		return list;
		
		// return jdbcTemplate.query(sql, new RowMapper<OrderStat>() {
		// @Override
		// public OrderStat mapRow(ResultSet rs, int rowNum)
		// throws SQLException {
		// OrderStat ol = new OrderStat();
		// ol.setMerchandiseId(rs.getString("merchandise_id"));
		// ol.setMerchandiseName(rs.getString("merchandise_name"));
		// return ol;
		// }
		// });
	}
	
}
