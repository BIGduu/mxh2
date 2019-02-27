package net.mxh.dao;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import net.mxh.entity.RoleMenu;

@Repository
public class RoleMenuDao extends BaseDao<RoleMenu> {
	
	public void deleteByRoleId(Long roleId) {
		String hql = "delete RoleMenu where id.roleId = " + roleId;
		Query query = currentSession().createQuery(hql);
		query.executeUpdate();
	}
	
}
