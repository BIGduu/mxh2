package net.mxh.admin.main.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.CommonCheckbox;
import net.mxh.admin.main.bean.MenuSelect;
import net.mxh.entity.Menu;
import net.mxh.entity.Role;
import net.mxh.service.MenuService;
import net.mxh.service.RoleMenuService;
import net.mxh.service.RoleService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.StringUtil;
import net.mxh.util.ValidateUtil;

@Controller
@RequestMapping(value = "admin/role")
public class RoleController {
	
	private static int pageSize = 10;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private RoleMenuService roleMenuService;
	
	@Autowired
	private MenuService menuService;
	
	private static List<CommonCheckbox> stateList = new ArrayList<>();
	
	static {
		stateList.add(new CommonCheckbox(0, "异常", false));
		stateList.add(new CommonCheckbox(1, "未审核", false));
		stateList.add(new CommonCheckbox(2, "已审核", false));
		stateList.add(new CommonCheckbox(3, "已分配", false));
		stateList.add(new CommonCheckbox(4, "装车完成", false));
		stateList.add(new CommonCheckbox(5, "已出库", false));
		stateList.add(new CommonCheckbox(6, "已送达", false));
		stateList.add(new CommonCheckbox(7, "已完成", false));
	}
	
	private static List<CommonCheckbox> checkList = new ArrayList<>();
	
	static {
		checkList.add(new CommonCheckbox(0, "未对账", false));
		checkList.add(new CommonCheckbox(1, "已提交对账", false));
		checkList.add(new CommonCheckbox(2, "客户材料部已审核", false));
		checkList.add(new CommonCheckbox(3, "财务部已核对", false));
	}
	
	@RequestMapping(value = "/roleList", method = RequestMethod.POST)
	public ModelAndView roleList(Integer page) {
		List<Role> roleList = roleService.findAll(page, pageSize);
		long total = roleService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/role/roleList");
		modelAndView.addObject("roleList", roleList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	@RequestMapping(value = "/roleEdit", method = RequestMethod.POST)
	public ModelAndView roleEdit(Integer page, Long roleId) {
		ModelAndView modelAndView = new ModelAndView("/role/roleEdit");
		if (null == roleId || 0 == roleId) {
			for (CommonCheckbox commonCheckbox : stateList)
				commonCheckbox.setChecked(false);
			modelAndView.addObject("role", new Role());
		}
		else {
			Role role = roleService.findById(roleId);

				for (CommonCheckbox commonCheckbox : stateList) {
					if (role.getStates().indexOf(commonCheckbox.getId().toString()) != -1) {
						commonCheckbox.setChecked(true);
					}else{
						commonCheckbox.setChecked(false);
					}

				}

				for (CommonCheckbox commonCheckbox : checkList) {
					if (role.getChecks().indexOf(commonCheckbox.getId().toString()) != -1) {
						commonCheckbox.setChecked(true);
					}else{
						commonCheckbox.setChecked(false);
					}
				}
			modelAndView.addObject("role", role);
		}
		modelAndView.addObject("stateList", stateList);
		modelAndView.addObject("checkList", checkList);
		modelAndView.addObject("roleId", roleId);
		modelAndView.addObject("page", page);
		return modelAndView;
	}



	@RequestMapping(value = "/roleSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData roleSave(Role role, Long roleId) {
		JsonResultData result = JsonResultData.success();
		if (!ValidateUtil.lengthBetween(role.getRoleName(), 2, 10)) {
			return result.turnError("角色名字的长度必须是2-10位字符");
		}
		try {
			role.setStates(String.join(",", role.getStateList()));
			role.setChecks(String.join(",", role.getCheckList()));
			if (null == roleId || 0 == roleId)
				role.setId(null);
			else {
				role.setId(roleId);
				Role roleOld = roleService.findById(roleId);
				role.setRoleCode(roleOld.getRoleCode());
			}
			roleService.saveOrUpdate(role);
		}
		catch (Exception e) {
			e.printStackTrace();
			result.turnError("保存失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/roleMenuList", method = RequestMethod.POST)
	public ModelAndView roleMenuList(Integer page, Long roleId, String roleName) {
		ModelAndView modelAndView = new ModelAndView("/role/roleMenuList");
		modelAndView.addObject("roleId", roleId);
		modelAndView.addObject("roleName", roleName);
		modelAndView.addObject("page", page);
		List<Menu> roleMenuList = menuService.findByRoleId(roleId);
		Set<Long> menuIds = new HashSet<>();
		for (Menu menu : roleMenuList) {
			menuIds.add(menu.getId());
		}
		List<Menu> allMenus = menuService.findByMenuLevel(CategoryUtil.MENULEVEL.TOW);
		
		List<MenuSelect> menuSelectList = new ArrayList<MenuSelect>();
		for (Menu allMenu : allMenus) {
			MenuSelect menuSelect = new MenuSelect();
			menuSelect.setMenuId(allMenu.getId());
			menuSelect.setMenuName(allMenu.getMenuName());
			menuSelect.setLinkUrl(allMenu.getLinkUrl());
			menuSelect.setSelected(menuIds.contains(allMenu.getId()) ? 1 : 0);
			menuSelectList.add(menuSelect);
		}
		modelAndView.addObject("menuSelectList", menuSelectList);
		return modelAndView;
	}
	
	@RequestMapping(value = "/roleMenuSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData roleMenuSave(Long roleId, String ids) {
		JsonResultData result = JsonResultData.success();
		String[] menuIds = ids.split(",");
		try {
			roleMenuService.saveRoleMenus(roleId, menuIds);
		}
		catch (Exception e) {
			e.printStackTrace();
			result.turnError("保存失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/roleDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData roleDelete(Long roleId) {
		JsonResultData result = JsonResultData.success();
		try {
			roleService.delete(roleId);
		}
		catch (Exception e) {
			e.printStackTrace();
			result.turnError("删除失败");
		}
		return result;
	}
}
