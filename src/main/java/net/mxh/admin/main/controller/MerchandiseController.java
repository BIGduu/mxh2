package net.mxh.admin.main.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Merchandise;
import net.mxh.entity.StoresNotUseMerchandise;
import net.mxh.service.MerchandiseService;
import net.mxh.service.StoresMerchandiseService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.ImageUploadUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.StringUtil;

@Controller
@RequestMapping(value = "admin/merchandise")
public class MerchandiseController {
	
	private static int pageSize = 10;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	@Autowired
	private StoresMerchandiseService storesMerchandiseService;
	
	/**
	 * 商品列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/merchandiseList", method = RequestMethod.POST)
	public ModelAndView merchandiseList(Integer page) {
		List<Merchandise> merchandiseList = merchandiseService.findByIsUse(CategoryUtil.WHETHER.YES, page, pageSize);
		long total = merchandiseService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/merchandise/merchandiseList");
		modelAndView.addObject("merchandiseList", merchandiseList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 某门店对应的商品列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/merchandiseList2Auth", method = RequestMethod.POST)
	public ModelAndView merchandiseList2Auth(Long storesId, Integer page) {
		List<Merchandise> merchandiseList = merchandiseService.findByIsUse(CategoryUtil.WHETHER.YES, page, pageSize);
		long total = merchandiseService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/merchandise/merchandiseList2Auth");
		List<StoresNotUseMerchandise> list = storesMerchandiseService.findByStoresId(storesId, CategoryUtil.WHETHER.NO);
		if (CollectionUtils.isNotEmpty(merchandiseList) && CollectionUtils.isNotEmpty(list)) {
			Map<Long, Integer> isUsedMap = new HashMap<Long, Integer>();
			for (StoresNotUseMerchandise storesNotUseMerchandise : list) {
				isUsedMap.put(storesNotUseMerchandise.getMerchandiseId(), storesNotUseMerchandise.getIsUse());
			}
			for (Merchandise merchandise : merchandiseList) {
				Integer isUesd = isUsedMap.get(merchandise.getId());
				if (null != isUesd && CategoryUtil.WHETHER.NO.equals(isUesd)) {
					merchandise.setIsUse(CategoryUtil.WHETHER.NO);
				}
				else {
					merchandise.setIsUse(CategoryUtil.WHETHER.YES);
				}
			}
		}
		modelAndView.addObject("merchandiseList", merchandiseList);
		modelAndView.addObject("storesId", storesId);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 商品列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/merchandiseRecoveryList", method = RequestMethod.POST)
	public ModelAndView merchandiseRecoveryList(Integer page) {
		List<Merchandise> merchandiseList = merchandiseService.findByIsUse(CategoryUtil.WHETHER.NO, page, pageSize);
		long total = merchandiseService.count();
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/merchandise/merchandiseRecoveryList");
		modelAndView.addObject("merchandiseList", merchandiseList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 修改新增商品页面
	 * @param page
	 * @param merchandiseId
	 * @return
	 */
	@RequestMapping(value = "/merchandiseEdit", method = RequestMethod.GET)
	public ModelAndView merchandiseEdit(Integer page, Long merchandiseId) {
		ModelAndView modelAndView = new ModelAndView("/merchandise/merchandiseEdit");
		modelAndView.addObject("merchandiseId", merchandiseId);
		modelAndView.addObject("page", page);
		if (merchandiseId != 0) {
			Merchandise merchandise = merchandiseService.findById(merchandiseId);
			merchandise.setImg(ImageUploadUtil.getRealUrl(merchandise.getImg()));
			modelAndView.addObject("merchandise", merchandise);
		}
		
		return modelAndView;
	}
	
	/**
	 * 保存修改商品
	 * @param merchandise
	 * @return
	 */
	@RequestMapping(value = "/merchandiseSave", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData saveMerchandise(Merchandise merchandise, MultipartFile file) {
		JsonResultData result = JsonResultData.success();
		String img = null;
		if (file.getSize() > 0) {
			if (ImageUploadUtil.checkImageFile(file)) {
				return result.turnError("图片格式必须是png、jpg，且图片大小不能超过2M");
			}
			
			try {
				img = ImageUploadUtil.imgUpload(file);
			}
			catch (Exception e) {
				return result.turnError("图片上传失败，请稍后再试");
			}
		}
		
		if (null == merchandise.getId() || 0 == merchandise.getId()) {
			merchandise.setId(null);
			if (StringUtil.isNotEmpty(img))
				merchandise.setImg(img);
			merchandise.setIsUse(CategoryUtil.WHETHER.YES);
			merchandise.setTotal(new BigDecimal(0));
			merchandiseService.save(merchandise);
		}
		else {
			Merchandise merchandise2 = merchandiseService.findById(merchandise.getId());
			if (StringUtil.isNotEmpty(img))
				merchandise2.setImg(img);
			merchandise2.setMerchandiseName(merchandise.getMerchandiseName());
			merchandise2.setMerchandiseCode(merchandise.getMerchandiseCode());
			merchandise2.setBrandName(merchandise.getBrandName());
			merchandise2.setSpecification(merchandise.getSpecification());
			merchandise2.setUnitPrice(merchandise.getUnitPrice());
			merchandise2.setUnit(merchandise.getUnit());
			merchandise2.setShippingCost(merchandise.getShippingCost());
			merchandise2.setUpstairsCost(merchandise.getUpstairsCost());
			merchandiseService.update(merchandise2);
		}
		return result;
	}
	
	/**
	 * 删除商品
	 * @param merchandise
	 * @return
	 */
	@RequestMapping(value = "/merchandiseDelete", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData merchandiseDelete(Long merchandiseId) {
		JsonResultData result = JsonResultData.success();
		Merchandise merchandise = merchandiseService.findById(merchandiseId);
		if (CategoryUtil.WHETHER.YES == merchandise.getIsUse())
			merchandise.setIsUse(CategoryUtil.WHETHER.NO);
		else
			merchandise.setIsUse(CategoryUtil.WHETHER.YES);
		
		merchandiseService.update(merchandise);
		return result;
	}
	
}
