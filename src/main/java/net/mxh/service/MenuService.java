package net.mxh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.MenuDao;
import net.mxh.entity.Menu;
import net.mxh.util.CategoryUtil;

@Service
@Transactional
public class MenuService {
	
	@Autowired
	private MenuDao menuDao;
	
	public void save(Menu entity) {
		menuDao.save(entity);
	}
	
	public void update(Menu entity) {
		menuDao.update(entity);
	}
	
	public void delete(Menu entity) {
		menuDao.delete(entity);
	}
	
	public Menu findById(Long id) {
		return menuDao.findById(Menu.class, id);
	}
	
	public List<Menu> findAll() {
		return menuDao.findAll(Menu.class, "showOrder", true);
	}
	
	public List<Menu> findAll(Integer page, Integer pageSize) {
		return menuDao.findAll(Menu.class, page, pageSize);
	}
	
	public List<Menu> findByMenuLevel(Integer menuLevel) {
		Map<String, Object> param = new HashMap<>();
		param.put("menuLevel", menuLevel);
		param.put("isUse", CategoryUtil.WHETHER.YES);
		return menuDao.findByParam(Menu.class, param, "showOrder", true);
	}
	
	public List<Menu> findByMenuLevel(Integer menuLevel, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("menuLevel", menuLevel);
		param.put("isUse", CategoryUtil.WHETHER.YES);
		return menuDao.findByParam(Menu.class, param, "showOrder", true, page, pageSize);
	}
	
	public List<Menu> findByRoleId(Long roleId, Integer menuLevel) {
		return menuDao.findByRoleId(roleId, menuLevel);
	}
	
	public List<Menu> findByRoleId(Long roleId) {
		return menuDao.findByRoleId(roleId);
	}
	
	public Long count() {
		return menuDao.count(Menu.class);
	}
	
	public List<Menu> findByMenuName(String menuName, Integer page, Integer pageSize) {
		return menuDao.findByMenuName(menuName, page, pageSize);
	}
	
	public Long countByMenuName(String menuName) {
		return menuDao.countByMenuName(menuName);
	}
	
	public void saveOrUpdate(Menu menu) {
		menu.setIsUse(CategoryUtil.WHETHER.YES);
		menuDao.saveOrUpdate(menu);
	}
	
	public long countByMenuLevel(Integer menuLevel) {
		Map<String, Object> param = new HashMap<>();
		param.put("menuLevel", menuLevel);
		param.put("isUse", CategoryUtil.WHETHER.YES);
		return menuDao.countByParam(Menu.class, param);
	}
	
	public Long countByParentId(Long parentId) {
		Map<String, Object> param = new HashMap<>();
		param.put("parentId", parentId);
		return menuDao.countByParam(Menu.class, param);
	}
}
