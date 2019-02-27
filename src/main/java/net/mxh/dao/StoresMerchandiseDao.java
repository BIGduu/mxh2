package net.mxh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.StoresNotUseMerchandise;

@Repository
public class StoresMerchandiseDao extends BaseDao<StoresNotUseMerchandise> {
	@SuppressWarnings("unchecked")
	public List<StoresNotUseMerchandise> findByStoresIdAndMerchandiseId(Long storesId, Long merchandiseId) {
		Criteria criteria = currentSession().createCriteria(StoresNotUseMerchandise.class);
		criteria.add(Restrictions.eq("storesId", storesId));
		criteria.add(Restrictions.eq("merchandiseId", merchandiseId));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<StoresNotUseMerchandise> findByStoresId(Long storesId, Integer isUse) {
		Criteria criteria = currentSession().createCriteria(StoresNotUseMerchandise.class);
		criteria.add(Restrictions.eq("storesId", storesId));
		criteria.add(Restrictions.eq("isUse", isUse));
		return criteria.list();
	}
	
	@Override
	public void deleteAll(List<StoresNotUseMerchandise> list) {
		super.deleteAll(list);
	}
}
