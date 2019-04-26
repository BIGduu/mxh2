package net.mxh.admin.main.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import net.mxh.service.*;
import net.mxh.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.OrderStat;
import net.mxh.entity.Admin;
import net.mxh.entity.Building;
import net.mxh.entity.User;

@Controller
@RequestMapping(value = "admin/building")
public class BuildingController {
	
	private static int pageSize = 10;
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private AdminService adminService;

	@Autowired
	private OrderService orderService;


	@Autowired
	private UserService userService;

	
	/**
	 * 工地列表
	 *
	 *
	 * @return
	 */
	@RequestMapping(value = "/buildingList", method = RequestMethod.POST)
	public ModelAndView buildingList(@RequestParam Map<String, Object> param , HttpSession session) {

		Integer page = StringUtil.toInteger(param.get("page"));
		Admin admin = (Admin) session.getAttribute("admin");
		List<Building> buildingList;
		long total;
		if(admin.getRoleId()<4){
			buildingList = buildingService.findAll(page, pageSize);
			total = buildingService.count();
			total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		}else{
			if(param.get("receivingAddress")!=""){
				param.put("buildingName",param.get("receivingAddress"));
			}
			if(param.get("managerName")==""){
				param.remove("managerName");
			}

			param.put("storesId", admin.getStoresId());

			param.remove("receivingAddress");

			param.remove("page");
			buildingList = buildingService.findByParam(param,"createTime",false);
			total = 1;
		}

		for (Building building : buildingList) {
			building.setCreateTimeStr(DateUtil.getDateTimeFormat(new Date(building.getCreateTime())));
		}


		ModelAndView modelAndView = new ModelAndView("/building/buildingList");
		modelAndView.addObject("buildingList", buildingList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 修改新增工地页面
	 * @param page
	 * @param buildingId
	 * @return
	 */
	@RequestMapping(value = "/buildingEdit", method = RequestMethod.GET)
	public ModelAndView buildingEdit(Integer page, Long buildingId) {
		
		ModelAndView modelAndView = new ModelAndView("/building/buildingEdit");
		modelAndView.addObject("buildingId", buildingId);
		modelAndView.addObject("page", page);
		modelAndView.addObject("users", userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE));
		if (buildingId != 0) {
			Building building = buildingService.findById(buildingId);
			building.setStartTimeStr(DateUtil.getDateTimeFormat(new Date(building.getStartTime())));
			modelAndView.addObject("building", building);
		}
		return modelAndView;
	}
	
	/**
	 * 保存修改工地
	 * @param building
	 * @return
	 */
	@RequestMapping(value = "/buildingSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData saveBuilding(Building building, HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		JsonResultData result = JsonResultData.success();
		if (!ValidateUtil.lengthBetween(building.getBuildingName(), 2, 50)) {
			return result.turnError("工地的长度必须是2-50位字符");
		}
		User user = userService.findById(building.getManagerId());
		building.setManagerName(user.getUsername());
		building.setStoresId(user.getStoresId());
		if (null == building.getId() || 0 == building.getId()) {
			building.setId(null);
			building.setCreateTime(System.currentTimeMillis());
			building.setCreatorId(admin.getAdminId());
			building.setStartTime(DateUtil.getDateTimeFormat(building.getStartTimeStr()).getTime());
			buildingService.save(building);
		}
		else {
			Building building2 = buildingService.findById(building.getId());
			building.setCreateTime(building2.getCreateTime());
			building.setCreatorId(building2.getCreatorId());
			building.setModificationTime(new Date().getTime());
			building.setModifierId(admin.getAdminId());
			building.setBuildingName(building.getBuildingName());
			building.setStartTime(DateUtil.getDateTimeFormat(building.getStartTimeStr()).getTime());
			building.setArea(building.getArea());
			buildingService.update(building);
		}
		return result;
	}
	
	/**
	 * 删除工地
	 * @param buildingId
	 * @return 返回成功信息
	 */
	@RequestMapping(value = "/buildingDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData buildingDelete(Long buildingId) {
		JsonResultData result = JsonResultData.success();
		Building building = buildingService.findById(buildingId);
		if (orderService.countByReceivingAddress(building.getBuildingName()) <= 0) {
			buildingService.delete(building);
		} else {
			return result.turnError("此门店内还有用户，无法删除");
		}
		
		return result;
	}
	
	/**
	 * 修改门店可用产品列表
	 * @param storesId
	 * @param merchandiseId
	 * @param remarks
	 * @return
	 */
	// @RequestMapping(value = "/updateStoresUsedMerchandise", method = RequestMethod.POST)
	// @ResponseBody
	// public JsonResultData updateStoresUsedMerchandise(Long storesId, Long merchandiseId, Integer isUse, String
	// remarks,
	// HttpSession session) {
	// JsonResultData result = JsonResultData.success();
	// Admin admin = (Admin)session.getAttribute("admin");
	// if (null == admin) {
	// result.turnError("用户不存在，请重新登陆。");
	// }
	// List<StoresNotUseMerchandise> list =
	// storesMerchandiseService.findByStoresIdAndMerchandiseId(storesId, merchandiseId);
	// if (CollectionUtils.isEmpty(list)) {
	// StoresNotUseMerchandise storesNotUseMerchandise = new StoresNotUseMerchandise();
	// storesNotUseMerchandise.setStoresId(storesId);
	// storesNotUseMerchandise.setMerchandiseId(merchandiseId);
	// storesNotUseMerchandise.setRemarks(remarks);
	// storesNotUseMerchandise.setIsUse(isUse);
	// storesNotUseMerchandise.setCreatorId(admin.getAdminId());
	// storesNotUseMerchandise.setCreateTime(new Date().getTime());
	// storesNotUseMerchandise.setModifierId(admin.getAdminId());
	// storesNotUseMerchandise.setModificationTime(new Date().getTime());
	// storesMerchandiseService.save(storesNotUseMerchandise);
	// }
	// else if (CollectionUtils.isNotEmpty(list) && list.size() >= 2) {
	// storesMerchandiseService.deleteAll(list);
	// }
	// else {
	// StoresNotUseMerchandise storesNotUseMerchandise = list.get(0);
	// storesNotUseMerchandise.setIsUse(isUse);
	// storesNotUseMerchandise.setRemarks(remarks);
	// storesMerchandiseService.update(storesNotUseMerchandise);
	// }
	// return result;
	// }
	
	/**
	 * 修改门店可用产品列表
	 * @param storesId
	 * @param merchandiseId
	 * @param remarks
	 * @return
	 */
	// @RequestMapping(value = "/getNoUsedMerchandiseByStoresId", method = RequestMethod.POST)
	// @ResponseBody
	// public JsonResultData getNoUsedMerchandiseByStoresId(Long storesId, HttpSession session) {
	// JsonResultData result = JsonResultData.success();
	// Admin admin = (Admin)session.getAttribute("admin");
	// if (null == admin) {
	// result.turnError("用户不存在，请重新登陆。");
	// }
	// List<StoresNotUseMerchandise> list = storesMerchandiseService.findByStoresId(storesId, CategoryUtil.WHETHER.NO);
	// Map<String, Object> data = new HashMap<String, Object>();
	// data.put("list", list);
	// result.setData(data);
	// return result;
	// }
}
