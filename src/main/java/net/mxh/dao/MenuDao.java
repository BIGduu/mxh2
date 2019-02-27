package net.mxh.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import net.mxh.entity.Menu;
import net.mxh.util.CategoryUtil;

@Repository
public class MenuDao extends BaseDao<Menu> {
	
	public List<Menu> findByRoleId(Long roleId, Integer menuLevel) {
		String sql = "select sm.* from sys_menu sm LEFT JOIN role_menu rm on sm.id = rm.menu_id where rm.role_id = "
			+ roleId + " and sm.is_use = " + CategoryUtil.WHETHER.YES + " and sm.menu_level = " + menuLevel
			+ " order by sm.show_order";
		Query query = currentSession().createSQLQuery(sql).addEntity("sm", Menu.class);
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		List<Menu> menus = new ArrayList<Menu>();
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			menus.add((Menu)object);
		}
		return menus;
	}
	
	public List<Menu> findByRoleId(Long roleId) {
		String sql = "select sm.* from sys_menu sm LEFT JOIN role_menu rm on sm.id = rm.menu_id where rm.role_id = "
			+ roleId + " and sm.is_use = " + CategoryUtil.WHETHER.YES + " order by sm.show_order";
		Query query = currentSession().createSQLQuery(sql).addEntity("sm", Menu.class);
		@SuppressWarnings("unchecked")
		List<Object> list = query.list();
		List<Menu> menus = new ArrayList<Menu>();
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			Object object = iterator.next();
			menus.add((Menu)object);
		}
		return menus;
	}
	
	@SuppressWarnings("unchecked")
	public List<Menu> findByMenuName(String menuName, Integer page, Integer pageSize) {
		Criteria criteria = currentSession().createCriteria(Menu.class);
		criteria.add(Restrictions.like("menuName", "%" + menuName + "%"));
		criteria.add(Restrictions.eq("isUse", CategoryUtil.WHETHER.YES));
		criteria.setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
		return criteria.list();
	}
	
	public Long countByMenuName(String menuName) {
		Criteria criteria = currentSession().createCriteria(Menu.class);
		criteria.add(Restrictions.like("menuName", "%" + menuName + "%"));
		criteria.add(Restrictions.eq("isUse", CategoryUtil.WHETHER.YES));
		criteria.setProjection(Projections.rowCount());
		return (Long)criteria.uniqueResult();
	}
	
}
