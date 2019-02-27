package net.mxh.admin.main.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Admin;
import net.mxh.entity.Stores;
import net.mxh.entity.StoresNotUseMerchandise;
import net.mxh.service.AdminService;
import net.mxh.service.StoresMerchandiseService;
import net.mxh.service.StoresService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.ValidateUtil;

@Controller
@RequestMapping(value = "admin/stores")
public class StoresController {
	
	private static int pageSize = 10;
	
	@Autowired
	private StoresService storesService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private StoresMerchandiseService storesMerchandiseService;
	
	/**
	 * 门店列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/storesList", method = RequestMethod.POST)
	public ModelAndView storesList(Integer page) {
		List<Stores> storesList = storesService.findAll(page, pageSize);
		long total = storesService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/stores/storesList");
		modelAndView.addObject("storesList", storesList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 修改新增门店页面
	 * @param page
	 * @param storesId
	 * @return
	 */
	@RequestMapping(value = "/storesEdit", method = RequestMethod.GET)
	public ModelAndView storesEdit(Integer page, Long storesId, String storesName) {
		ModelAndView modelAndView = new ModelAndView("/stores/storesEdit");
		modelAndView.addObject("storesId", storesId);
		modelAndView.addObject("page", page);
		if (storesId != 0) {
			modelAndView.addObject("storesName", storesName);
		}
		
		return modelAndView;
	}
	
	/**
	 * 保存修改门店
	 * @param stores
	 * @return
	 */
	@RequestMapping(value = "/storesSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData saveStores(Stores stores) {
		JsonResultData result = JsonResultData.success();
		if (!ValidateUtil.lengthBetween(stores.getStoresName(), 2, 50)) {
			return result.turnError("门店的长度必须是2-50位字符");
		}
		if (null == stores.getId() || 0 == stores.getId()) {
			stores.setId(null);
			storesService.save(stores);
		}
		else {
			stores.setStoresName(stores.getStoresName());
			storesService.update(stores);
		}
		return result;
	}
	
	/**
	 * 删除门店
	 * @param stores
	 * @return
	 */
	@RequestMapping(value = "/storesDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData storesDelete(Long storesId) {
		JsonResultData result = JsonResultData.success();
		Stores stores = storesService.findById(storesId);
		if (adminService.countByStoresId(storesId) == 0)
			storesService.delete(stores);
		else
			return result.turnError("此门店内还有用户，无法删除");
		
		return result;
	}
	
	/**
	 * 修改门店可用产品列表
	 * @param storesId
	 * @param merchandiseId
	 * @param remarks
	 * @return
	 */
	@RequestMapping(value = "/updateStoresUsedMerchandise", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData updateStoresUsedMerchandise(Long storesId, Long merchandiseId, Integer isUse, String remarks,
		HttpSession session) {
		JsonResultData result = JsonResultData.success();
		Admin admin = (Admin)session.getAttribute("admin");
		if (null == admin) {
			result.turnError("用户不存在，请重新登陆。");
		}
		List<StoresNotUseMerchandise> list =
			storesMerchandiseService.findByStoresIdAndMerchandiseId(storesId, merchandiseId);
		if (CollectionUtils.isEmpty(list)) {
			StoresNotUseMerchandise storesNotUseMerchandise = new StoresNotUseMerchandise();
			storesNotUseMerchandise.setStoresId(storesId);
			storesNotUseMerchandise.setMerchandiseId(merchandiseId);
			storesNotUseMerchandise.setRemarks(remarks);
			storesNotUseMerchandise.setIsUse(isUse);
			storesNotUseMerchandise.setCreatorId(admin.getAdminId());
			storesNotUseMerchandise.setCreateTime(new Date().getTime());
			storesNotUseMerchandise.setModifierId(admin.getAdminId());
			storesNotUseMerchandise.setModificationTime(new Date().getTime());
			storesMerchandiseService.save(storesNotUseMerchandise);
		}
		else if (CollectionUtils.isNotEmpty(list) && list.size() >= 2) {
			storesMerchandiseService.deleteAll(list);
		}
		else {
			StoresNotUseMerchandise storesNotUseMerchandise = list.get(0);
			storesNotUseMerchandise.setIsUse(isUse);
			storesNotUseMerchandise.setRemarks(remarks);
			storesMerchandiseService.update(storesNotUseMerchandise);
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
	@RequestMapping(value = "/getNoUsedMerchandiseByStoresId", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData getNoUsedMerchandiseByStoresId(Long storesId, HttpSession session) {
		JsonResultData result = JsonResultData.success();
		Admin admin = (Admin)session.getAttribute("admin");
		if (null == admin) {
			result.turnError("用户不存在，请重新登陆。");
		}
		List<StoresNotUseMerchandise> list = storesMerchandiseService.findByStoresId(storesId, CategoryUtil.WHETHER.NO);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", list);
		result.setData(data);
		return result;
	}
}
