package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.RoleMenuDao;
import net.mxh.entity.RoleMenu;
import net.mxh.entity.RoleMenuId;

@Service
@Transactional
public class RoleMenuService {
	
	@Autowired
	private RoleMenuDao roleMenuDao;
	
	public void save(RoleMenu entity) {
		roleMenuDao.save(entity);
	}
	
	public void update(RoleMenu entity) {
		roleMenuDao.update(entity);
	}
	
	public void delete(RoleMenu entity) {
		roleMenuDao.delete(entity);
	}
	
	public RoleMenu findById(Long id) {
		return roleMenuDao.findById(RoleMenu.class, id);
	}
	
	public List<RoleMenu> findAll() {
		return roleMenuDao.findAll(RoleMenu.class);
	}
	
	public void saveRoleMenus(Long roleId, String[] menuIds) {
		roleMenuDao.deleteByRoleId(roleId);
		for (String menuId : menuIds) {
			roleMenuDao.save(new RoleMenu(new RoleMenuId(roleId, Long.valueOf(menuId))));
		}
	}
	
}
