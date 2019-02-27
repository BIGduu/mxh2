package net.mxh.admin.main.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.admin.main.bean.CommonSelect;
import net.mxh.entity.Admin;
import net.mxh.entity.Merchandise;
import net.mxh.entity.Storage;
import net.mxh.service.MerchandiseService;
import net.mxh.service.StorageService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.JsonResultData;

@Controller
@RequestMapping(value = "admin/storage")
public class StorageController {
	
	private static int pageSize = 10;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	private static List<CommonSelect> typeList = new ArrayList<>();
	
	static {
		typeList.add(new CommonSelect(1, "进"));
		typeList.add(new CommonSelect(2, "出"));
		typeList.add(new CommonSelect(3, "损耗"));
		typeList.add(new CommonSelect(4, "退货"));
	}
	
	/**
	 * 库存列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/storageList", method = RequestMethod.POST)
	public ModelAndView storageList(Integer page) {
		List<Storage> storageList = storageService.findAll(page, pageSize);
		for (Storage storage : storageList) {
			storage.setCreateTimeStr(DateUtil.getDateTimeFormat(new Date(storage.getCreateTime())));
		}
		long total = storageService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/storage/storageList");
		modelAndView.addObject("storageList", storageList);
		modelAndView.addObject("typeList", typeList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 修改新增库存
	 * @param page
	 * @param storageId
	 * @return
	 */
	@RequestMapping(value = "/storageEdit", method = RequestMethod.GET)
	public ModelAndView storageEdit(Integer page, Long storageId) {
		ModelAndView modelAndView = new ModelAndView("/storage/storageEdit");
		modelAndView.addObject("storageId", storageId);
		modelAndView.addObject("page", page);
		modelAndView.addObject("typeList", typeList);
		modelAndView.addObject("merchandises", merchandiseService.findByIsUse(CategoryUtil.WHETHER.YES, null));
		if (storageId != 0) {
			Storage storage = storageService.findById(storageId);
			if (storage.getShippingDate() != null)
				storage.setShippingDateStr(DateUtil.getDateTimeFormat(new Date(storage.getShippingDate())));
			if (storage.getPurchaseDate() != null)
				storage.setPurchaseDateStr(DateUtil.getDateTimeFormat(new Date(storage.getPurchaseDate())));
			modelAndView.addObject("storage", storage);
		}
		
		return modelAndView;
	}
	
	/**
	 * 保存修改库存
	 * @param storage
	 * @return
	 */
	@RequestMapping(value = "/storageSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData saveStorage(Storage storage, HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		JsonResultData result = JsonResultData.success();
		Merchandise merchandise = merchandiseService.findById(storage.getMerchandiseId());
		storage.setMerchandiseName(merchandise.getMerchandiseName());
		if (null == storage.getId() || 0 == storage.getId()) {
			storage.setId(null);
			storage.setCreateTime(System.currentTimeMillis());
			storage.setCreatorId(admin.getAdminId());
			storage.setShippingDate(DateUtil.getDateTimeFormat(storage.getShippingDateStr()).getTime());
			storage.setPurchaseDate(DateUtil.getDateTimeFormat(storage.getPurchaseDateStr()).getTime());
			storageService.save(storage, merchandise);
		}
		else {
			Storage storage2 = storageService.findById(storage.getId());
			storage.setCreateTime(storage2.getCreateTime());
			storage.setCreatorId(storage2.getCreatorId());
			storage.setModificationTime(new Date().getTime());
			storage.setModifierId(admin.getAdminId());
			storageService.update(storage);
		}
		return result;
	}
	
	/**
	 * 保存修改库存
	 * @param storage
	 * @return
	 */
	@RequestMapping(value = "/isPay", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData isPay(Integer isPay, Long storageId, HttpSession session) {
		JsonResultData result = JsonResultData.success();
		try {
			Admin admin = (Admin)session.getAttribute("admin");
			Storage storage = storageService.findById(storageId);
			if (CategoryUtil.WHETHER.NO == isPay)
				storage.setIsPay(CategoryUtil.WHETHER.YES);
			else
				storage.setIsPay(CategoryUtil.WHETHER.NO);
			storage.setModificationTime(System.currentTimeMillis());
			storage.setModifierId(admin.getAdminId());
			storageService.update(storage);
		}
		catch (Exception e) {
			e.printStackTrace();
			result.turnError("修改失败");
		}
		return result;
	}
	
	/**
	 * 删除库存
	 * @param storage
	 * @return
	 */
	@RequestMapping(value = "/storageDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData storageDelete(Long storageId) {
		JsonResultData result = JsonResultData.success();
		Storage storage = storageService.findById(storageId);
		storageService.delete(storage);
		
		return result;
	}
	
}
