package net.mxh.admin.main.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Admin;
import net.mxh.entity.Role;
import net.mxh.entity.Stores;
import net.mxh.service.AdminService;
import net.mxh.service.RoleService;
import net.mxh.service.StoresService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.MD5;
import net.mxh.util.ValidateUtil;

@Controller
@RequestMapping(value = "admin/admin")
public class AdminController {
	
	private static int pageSize = 10;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private StoresService storesService;
	
	/**
	 * 查询所有的后台用户
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/adminList", method = RequestMethod.POST)
	public ModelAndView adminList(HttpServletRequest request, Integer page) {
		List<Admin> adminlist = adminService.findAll(page, pageSize);
		long total = adminService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		
		ModelAndView modelAndView = new ModelAndView("/admin/adminList");
		modelAndView.addObject("adminList", adminlist);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		
		return modelAndView;
	}
	
	/**
	 * 修改用户启用，禁用状态
	 * @param request
	 * @param oldEnabled
	 * @param adminId
	 * @return
	 */
	@RequestMapping(value = "/adminEnable", method = RequestMethod.GET)
	@ResponseBody
	public JsonResultData updateEnabled(Integer isUse, Long adminId) {
		JsonResultData result = JsonResultData.success();
		Admin admin = adminService.findById(adminId);
		if (CategoryUtil.WHETHER.YES == isUse) {
			admin.setIsUse(CategoryUtil.WHETHER.NO);
		} else {
			admin.setIsUse(CategoryUtil.WHETHER.YES);
		}
		adminService.update(admin);
		return result;
	}
	
	/**
	 * 修改新增用户信息
	 * @param page
	 * @param adminId
	 * @return
	 */
	@RequestMapping(value = "/adminEdit", method = RequestMethod.GET)
	public ModelAndView adminEdit(Integer page, Long adminId, String title) {
		ModelAndView modelAndView = new ModelAndView("/admin/adminEdit");
		modelAndView.addObject("adminId", adminId);
		modelAndView.addObject("page", page);
		if (adminId != 0) {
			Admin admin = adminService.findById(adminId);
			modelAndView.addObject("adminEdit", admin);
		}
		
		List<Role> roles = roleService.findAll();
		for (int i = 0; i < roles.size(); i++) {
			if ("admin".equals(roles.get(i).getRoleCode())) {
				roles.remove(i);
			}
		}
		
		List<Stores> storesList = storesService.findAll();
		
		modelAndView.addObject("roles", roles);
		modelAndView.addObject("storesList", storesList);
		return modelAndView;
		
	}
	
	/**
	 * 保存用户信息
	 * @param admin
	 * @return
	 */
	@RequestMapping(value = "/adminSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData saveAdmin(Admin admin) {
		JsonResultData result = JsonResultData.success();
		if (!ValidateUtil.lengthBetween(admin.getUsername(), 2, 10)) {
			return result.turnError("名字的长度必须是2-10位字符");
		}
		if (0 == admin.getAdminId()) {
			if (!ValidateUtil.onlyLittleLetter(admin.getAdminName(), 4, 10)) {
				return result.turnError("账号必须是4-10位的小写字母组成");
			}
			admin.setAdminId(null);
			admin.setIsUse(CategoryUtil.WHETHER.YES);
			admin.setPassword(MD5.md5((admin.getAdminName() + admin.getPassword())) + "6");
			if (null != admin.getRoleId()) {
				Role role = roleService.findById(admin.getRoleId());
				admin.setRoleName(role.getRoleName());
			}
			if (null != admin.getStoresId()) {
				Stores stores = storesService.findById(admin.getStoresId());
				admin.setStoresName(stores.getStoresName());
			}
			adminService.save(admin);
		}
		else {
			Admin storeAdmin = adminService.findById(admin.getAdminId());
			storeAdmin.setUsername(admin.getUsername());
			storeAdmin.setTelephone(admin.getTelephone());
			if (null != admin.getRoleId() && admin.getRoleId() != storeAdmin.getRoleId()) {
				storeAdmin.setRoleId(admin.getRoleId());
				Role role = roleService.findById(admin.getRoleId());
				storeAdmin.setRoleName(role.getRoleName());
			}
			if (admin.getStoresId() != storeAdmin.getStoresId()) {
				if (null != admin.getStoresId()) {
					storeAdmin.setStoresId(admin.getStoresId());
					Stores stores = storesService.findById(admin.getStoresId());
					storeAdmin.setStoresName(stores.getStoresName());
				}
				else {
					storeAdmin.setStoresId(null);
					storeAdmin.setStoresName(null);
				}
				
			}
			
			adminService.update(storeAdmin);
		}
		
		return result;
	}
	
	/**
	 * 验证密码
	 * @param page
	 * @param adminId
	 * @return
	 */
	@RequestMapping(value = "/passwordVerify", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData passwordVerify(String password, HttpSession session) {
		JsonResultData result = JsonResultData.success();
		Admin admin = (Admin)session.getAttribute("admin");
		Admin admin2 = adminService.findById(admin.getAdminId());
		if (!admin2.getPassword().equals(password)) {
			return result.turnError("密码错误");
		}
		
		return result;
	}
	
	/**
	 * 修改密码
	 * @param page
	 * @param adminId
	 * @return
	 */
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData password(String password, String passwordNew, HttpSession session) {
		JsonResultData result = JsonResultData.success();
		Admin admin = (Admin)session.getAttribute("admin");
		Admin admin2 = adminService.findById(admin.getAdminId());
		if (admin2.getPassword().equals(password)) {
			admin2.setPassword(passwordNew);
			adminService.update(admin2);
		}
		else {
			return result.turnError("密码错误");
		}
		
		return result;
	}
}
