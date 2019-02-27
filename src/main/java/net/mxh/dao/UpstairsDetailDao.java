package net.mxh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import net.mxh.entity.UpstairsDetail;

@Repository
public class UpstairsDetailDao extends BaseDao<UpstairsDetail> {
	@SuppressWarnings("unchecked")
	public List<UpstairsDetail> findByOrderId(Long orderId) {
		String sql = "SELECT  " 
			+ "    do.order_id orderId, " 
			+ "    oi.delivery_order_id deliveryOrderId, "
			+ "    do.delivery_name deliveryName, " 
			+ "    do.delivery_id deliveryId, " 
			+ "    TRUNCATE(oi.number, 1) number, "
			+ "    oi.merchandise_id merchandiseId, "
			+ "    oi.merchandise_name merchandiseName, "
			+ "    m.specification,"
			+ "    m.upstairs_cost upstairsCost "
			+ "FROM "
			+ "    t_order t, "
			+ "    order_info oi, "
			+ "    delivery_order do, "
			+ "    merchandise m "
			+ "WHERE "
			+ "    t.id = oi.order_id "
			+ "        AND oi.delivery_order_id = do.id "
			+ "        AND t.id = do.order_id "
			+ "        AND oi.merchandise_id = m.id "
			+ "        AND oi.state = 2 "
			+ "        AND t.id = " + orderId
			+ "  ORDER BY do.delivery_id , oi.merchandise_id ";
		Query query = currentSession().createSQLQuery(sql)
			.addScalar("orderId", LongType.INSTANCE)
			.addScalar("deliveryOrderId", LongType.INSTANCE)
			.addScalar("deliveryName", StringType.INSTANCE)
			.addScalar("deliveryId", LongType.INSTANCE)
			.addScalar("number", BigDecimalType.INSTANCE)
			.addScalar("merchandiseId", LongType.INSTANCE)
			.addScalar("merchandiseName", StringType.INSTANCE)
			.addScalar("specification", StringType.INSTANCE)
			.addScalar("upstairsCost", BigDecimalType.INSTANCE)
			.setResultTransformer(Transformers.aliasToBean(UpstairsDetail.class));
		List<UpstairsDetail> upstairsDetails = query.list();
		return upstairsDetails;
	}
	
	@SuppressWarnings("unchecked")
	public List<UpstairsDetail> findListByOrderId(Long orderId) {
		Criteria criteria = currentSession().createCriteria(UpstairsDetail.class);
		criteria.add(Restrictions.eq("orderId", orderId));
		return criteria.list();
	}
}
