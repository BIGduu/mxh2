package net.mxh.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Merchandise;

@Repository
public class MerchandiseDao extends BaseDao<Merchandise> {
	
	@SuppressWarnings("unchecked")
	public List<Merchandise> findByIsUse(Integer isUse, Long storesId) {
		String sql = "SELECT m.*  FROM  merchandise m  WHERE m.is_use =  " + isUse
			+ (storesId != null
				? ("        AND NOT EXISTS ( SELECT  1 FROM " + "            stores_not_use_merchandise s "
					+ "        WHERE " + "            s.merchandise_id = m.id "
					+ "   AND s.is_use=0             AND s.stores_id = " + storesId + ") ")
				: "")
			+ " ORDER BY m.create_time DESC";
		System.out.println(sql);
		Query query = currentSession().createSQLQuery(sql).addEntity("m", Merchandise.class);
		List<Object> list = query.list();
		List<Merchandise> merchandises = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Object object : list) {
				merchandises.add((Merchandise)object);
			}
		}
		return merchandises;
	}

	@SuppressWarnings("unchecked")
	public List<Merchandise> findByIsUse(Integer isUse) {
		String sql = "SELECT m.*  FROM  merchandise m  WHERE m.is_use =  " + isUse
				+ " ORDER BY m.create_time DESC";
		System.out.println(sql);
		Query query = currentSession().createSQLQuery(sql).addEntity("m", Merchandise.class);
		List<Object> list = query.list();
		List<Merchandise> merchandises = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Object object : list) {
				merchandises.add((Merchandise)object);
			}
		}
		return merchandises;
	}


}
