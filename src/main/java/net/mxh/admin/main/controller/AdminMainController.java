package net.mxh.admin.main.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.Admin;
import net.mxh.entity.Merchandise;
import net.mxh.service.DeliveryOrderService;
import net.mxh.service.MerchandiseService;
import net.mxh.service.OrderInfoService;
import net.mxh.service.OrderService;
import net.mxh.service.StorageService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;

@Controller
@RequestMapping(value = "admin")
public class AdminMainController {
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	@RequestMapping(value = "/main", method = RequestMethod.GET)
	public ModelAndView main(HttpServletRequest request) {
		return new ModelAndView("/main/main");
	}
	
	@RequestMapping(value = "/mainIndex", method = RequestMethod.POST)
	public ModelAndView mainIndex(HttpServletRequest request, HttpSession session) {
		ModelAndView view = new ModelAndView("/main/mainIndex");
		Admin admin = (Admin)session.getAttribute("admin");
		if (admin.getRoleId() == 1 || admin.getRoleId() == 2) {
			Long beginTime = DateUtil.getBeginTimeOfDate().getTime();
			Long endTime = DateUtil.getEndTimeOfDate().getTime();
			List<Merchandise> merchandises = merchandiseService.findAll();
			List<Map<String, String>> list = new ArrayList<>();
			List<Map<String, String>> orderInfoToday = orderInfoService.orderInfoToday(beginTime, endTime);
			for (Merchandise merchandise : merchandises) {
				Map<String, String> merchandiseMap = new HashMap<>();
				merchandiseMap.put("merchandiseName", merchandise.getMerchandiseName());
				for (Map<String, String> map : orderInfoToday) {
					if (merchandise.getId().toString().equals(map.get("merchandiseId"))) {
						merchandiseMap.put("orderSum", map.get("sum"));
						break;
					}
				}
				
				merchandiseMap.put("purchaseSum",
					storageService.sumBy(merchandise.getId(), CategoryUtil.STORAGETYPE.ONE, beginTime, endTime)
						.toPlainString());
				
				merchandiseMap.put("returnSum",
					storageService.sumBy(merchandise.getId(), CategoryUtil.STORAGETYPE.FOUR, beginTime, endTime)
						.toPlainString());
				
				merchandiseMap.put("outSum",
					storageService.sumBy(merchandise.getId(), CategoryUtil.STORAGETYPE.TOW, beginTime, endTime)
						.toPlainString());
				
				merchandiseMap.put("total", merchandise.getTotal().toPlainString());
				list.add(merchandiseMap);
			}
			List<Map<String, String>> storages = storageService.findBy(beginTime, endTime);
			
			List<Map<String, String>> receivables = orderService.findBy(beginTime, endTime);
			
			List<Map<String, String>> freights = deliveryOrderService.findBy(beginTime, endTime);
			
			// view.addObject("count", count);
			// view.addObject("merchandises", merchandises);
			view.addObject("storages", storages);
			view.addObject("receivables", receivables);
			view.addObject("freights", freights);
			view.addObject("list", list);
			view.addObject("isShow", 1);
		}
		else {
			view.addObject("isShow", 0);
		}
		return view;
	}
	
	@RequestMapping(value = "/audio")
	public void audio(HttpServletResponse response, HttpSession session) {
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("expires", -1);
		PrintWriter writer = null;
		try {
			Admin admin = (Admin)session.getAttribute("admin");
			writer = response.getWriter();
			writer.write("retry: 10000 \n");
			while (true) {
				if (admin.getRoleId() == 2L)
					if (orderService.countByStateAndstoresId(CategoryUtil.ORDERSTATUS.ONE, admin.getStoresId()) == 0)
						writer.write("data: 0 \n\n");
					else
						writer.write("data: 1 \n\n");
				else {
					writer.write("data: 2 \n\n");
					break;
				}
				writer.flush();
				Thread.sleep(30000);
			}
			
		}
		catch (Exception e) {
			writer.write("data: 2 \n\n");
			writer.flush();
			e.printStackTrace();
		}
		writer.close();
	}
}
