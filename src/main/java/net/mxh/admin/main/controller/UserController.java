package net.mxh.admin.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.CommonSelect;
import net.mxh.entity.Admin;
import net.mxh.entity.Signin;
import net.mxh.entity.User;
import net.mxh.service.SigninService;
import net.mxh.service.UserService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.JsonResultData;
import net.mxh.vo.PageDTO;

@Controller
@RequestMapping(value = "admin/user")
public class UserController {
	
	private static int pageSize = 10;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SigninService signinService;
	
	private static List<CommonSelect> selectList = new ArrayList<>();
	
	static {
		selectList.add(new CommonSelect(0, "全部"));
		selectList.add(new CommonSelect(1, "用户名称"));
	}
	
	/**
	 * 用户列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/userList", method = RequestMethod.POST)
	public ModelAndView userList(Integer page, String selectId, String searchName,HttpSession session) {
		System.out.println(searchName);
		if (selectId == null) {
			selectId = "0";
			searchName = "";
		}
		searchName = searchName.trim();
		
		List<User> userList = new ArrayList<>();
		Long total = 0L;
		Admin admin = (Admin) session.getAttribute("admin");
		/**
		 * @author bigduu
		 */
		//如果不是最终管理员 或者不是管理员登录 以下代码 因为要求除了最终管理员其他员工不能看到其他门店的员工信息
		if (null == admin || admin.getRoleId() <= 2 ){
			if ("0".equals(selectId)) {
				userList = userService.findAll(page, pageSize);
				total = userService.count();
			}
			else {
				userList = userService.findByUsername(searchName, page, pageSize);
				total = userService.countByUsername(searchName);
			}
		}else{
			userList = userService.findByStoresId(admin.getStoresId(), page, pageSize);
			total = userService.countByStoresId(admin.getStoresId());
		}
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				user.setCreateTimeStr(DateUtil.getDateTimeFormat(new Date(user.getCreateTime())));
				if (user.getStatus() == CategoryUtil.USERSTATUS.THREE
					&& user.getDepartmentId() == CategoryUtil.DEPARTMENTID.TOW) {
					user.setIsSign(signinService.countByYesterday(user.getId()).intValue());
				}
			}
		}
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/user/userList");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("selectId", selectId);
		modelAndView.addObject("selectList", selectList);
		modelAndView.addObject("searchName", searchName);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 配送部用户列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/deliveryUserList", method = RequestMethod.POST)
	public ModelAndView deliveryUserList(Integer page, String selectId, String searchName, HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		if (null == admin) {
			return new ModelAndView("/template/errors/500", new HashMap<String, Object>());
		}
		if (selectId == null) {
			selectId = "0";
			searchName = "";
		}
		searchName = searchName.trim();
		Long storesId = admin.getStoresId();
		List<User> userList = new ArrayList<>();
		Long total = 0L;
		if ("0".equals(selectId)) {
			PageDTO<User> pageDTO =
				userService.findByStoresIdAndDepartMentId(storesId, CategoryUtil.DEPARTMENTID.TOW, page, pageSize);
			if (pageDTO != null) {
				userList = pageDTO.getList();
				total = pageDTO.getTotal().longValue();
			}
		}
		else {
			userList = userService.findByUsername(searchName, storesId, CategoryUtil.DEPARTMENTID.TOW, page, pageSize);
			total = userService.countByUsername(searchName, storesId, CategoryUtil.DEPARTMENTID.TOW);
		}
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				user.setCreateTimeStr(DateUtil.getDateTimeFormat(new Date(user.getCreateTime())));
				if (user.getStatus() == CategoryUtil.USERSTATUS.THREE
					&& user.getDepartmentId() == CategoryUtil.DEPARTMENTID.TOW)
					user.setIsSign(signinService.countByYesterday(user.getId()).intValue());
			}
		}
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/user/deliveryUserList");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("selectId", selectId);
		modelAndView.addObject("selectList", selectList);
		modelAndView.addObject("searchName", searchName);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 门店项目经理列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/customerUserList", method = RequestMethod.POST)
	public ModelAndView customerUserList(Integer page, String selectId, String searchName, HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		if (null == admin) {
			return new ModelAndView("/template/errors/500", new HashMap<String, Object>());
		}
		Long storesId = admin.getStoresId();
		if (selectId == null) {
			selectId = "0";
			searchName = "";
		}
		searchName = searchName.trim();
		
		List<User> userList = new ArrayList<>();
		Long total = 0L;
		if ("0".equals(selectId)) {
			PageDTO<User> pageDTO =
				userService.findByStoresIdAndDepartMentId(storesId, CategoryUtil.DEPARTMENTID.ONE, page, pageSize);
			if (pageDTO != null) {
				userList = pageDTO.getList();
				total = pageDTO.getTotal().longValue();
			}
		}
		else {
			userList = userService.findByUsername(searchName, storesId, CategoryUtil.DEPARTMENTID.ONE, page, pageSize);
			total = userService.countByUsername(searchName, storesId, CategoryUtil.DEPARTMENTID.ONE);
		}
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				user.setCreateTimeStr(DateUtil.getDateTimeFormat(new Date(user.getCreateTime())));
				if (user.getStatus() == CategoryUtil.USERSTATUS.THREE
					&& user.getDepartmentId() == CategoryUtil.DEPARTMENTID.TOW)
					user.setIsSign(signinService.countByYesterday(user.getId()).intValue());
			}
		}
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/user/customerUserList");
		modelAndView.addObject("userList", userList);
		modelAndView.addObject("selectId", selectId);
		modelAndView.addObject("selectList", selectList);
		modelAndView.addObject("searchName", searchName);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 修改
	 * @param stores
	 * @return
	 */
	@RequestMapping(value = "/userEdit", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData userEdit(Integer status, Long userId) {
		JsonResultData result = JsonResultData.success();
		User user = userService.findById(userId);
		user.setStatus(status);
		userService.update(user);
		return result;
	}
	
	/**
	 * 修改
	 * @param stores
	 * @return
	 */
	@RequestMapping(value = "/sign", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData sign(Long userId) {
		JsonResultData result = JsonResultData.success();
		Signin signin = new Signin();
		signin.setUserId(userId);
		signin.setSignTime(DateUtil.getYesterday(20, 0).getTime());
		signinService.save(signin);
		return result;
	}
	
	/**
	 * 注销用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/userDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData userDelete(Long userId) {
		JsonResultData result = JsonResultData.success();
		User user = userService.findById(userId);
		user.setStatus(CategoryUtil.USERSTATUS.FOUR);
		user.setOpenid(null);
		userService.update(user);
		return result;
	}
	
}
