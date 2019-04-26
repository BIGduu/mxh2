package net.mxh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Building;

@Repository
public class BuildingDao extends BaseDao<Building> {
	
	@SuppressWarnings("unchecked")
	public List<Building> findByManagerId(Long managerId) {
		Criteria criteria = currentSession().createCriteria(Building.class);
		criteria.add(Restrictions.eq("managerId", managerId));
		return criteria.list();
	}

	public List<Building> findByStoresId(Long storesId) {
		Criteria criteria = currentSession().createCriteria(Building.class);
		criteria.add(Restrictions.eq("storesId",storesId));
		return criteria.list();
	}

}
