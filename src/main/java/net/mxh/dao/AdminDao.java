package net.mxh.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Admin;

@Repository
public class AdminDao extends BaseDao<Admin> {
	
	@SuppressWarnings("unchecked")
	public Admin findByRoleIdAndOpenidNotNull(Long roleId) {
		Criteria criteria = currentSession().createCriteria(Admin.class);
		criteria.add(Restrictions.eq("roleId", roleId));
		criteria.add(Restrictions.isNotNull("openid"));
		criteria.setFirstResult(0).setMaxResults(1);
		List<Admin> admins = criteria.list();
		if (admins.isEmpty()) {
			return null;
		}
		return admins.get(0);
	}
	
}
