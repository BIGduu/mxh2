package net.mxh.admin.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Department;
import net.mxh.service.DepartmentService;
import net.mxh.util.JsonResultData;
import net.mxh.util.ValidateUtil;

@Controller
@RequestMapping(value = "admin/department")
public class DepartmentController {
	
	private static int pageSize = 10;
	
	@Autowired
	private DepartmentService departmentService;
	
	/**
	 * 部门列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/departmentList", method = RequestMethod.POST)
	public ModelAndView departmentList(Integer page) {
		List<Department> departmentList = departmentService.findAll(page, pageSize);
		long total = departmentService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/department/departmentList");
		modelAndView.addObject("departmentList", departmentList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 修改新增部门页面
	 * @param page
	 * @param departmentId
	 * @return
	 */
	@RequestMapping(value = "/departmentEdit", method = RequestMethod.GET)
	public ModelAndView departmentEdit(Integer page, Long departmentId, String departmentName) {
		ModelAndView modelAndView = new ModelAndView("/department/departmentEdit");
		modelAndView.addObject("departmentId", departmentId);
		modelAndView.addObject("page", page);
		if (departmentId != 0) {
			modelAndView.addObject("departmentName", departmentName);
		}
		
		return modelAndView;
	}
	
	/**
	 * 保存修改部门
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "/departmentSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData saveDepartment(Department department) {
		JsonResultData result = JsonResultData.success();
		if (!ValidateUtil.lengthBetween(department.getDepartmentName(), 2, 50)) {
			return result.turnError("部门的长度必须是2-50位字符");
		}
		if (null == department.getId() || 0 == department.getId()) {
			department.setId(null);
			departmentService.save(department);
		}
		else {
			department.setDepartmentName(department.getDepartmentName());
			departmentService.update(department);
		}
		return result;
	}
	
	/**
	 * 删除部门
	 * @param department
	 * @return
	 */
	@RequestMapping(value = "/departmentDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData departmentDelete(Long departmentId) {
		JsonResultData result = JsonResultData.success();
		Department department = departmentService.findById(departmentId);
		departmentService.delete(department);
		return result;
	}
	
}
