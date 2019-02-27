package net.mxh.admin.main.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.OrderInfo;
import net.mxh.entity.TheOrder;
import net.mxh.entity.User;
import net.mxh.service.DeliveryOrderService;
import net.mxh.service.OrderInfoService;
import net.mxh.service.OrderService;
import net.mxh.service.UserService;
import net.mxh.task.WeixinTask;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.StringUtil;

@Controller
@RequestMapping(value = "admin/deliveryOrder")
public class DeliveryOrderController {
	
	private static int pageSize = 10;
	
	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	/**
	 * 待出库订单列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/deliveryOrderList", method = RequestMethod.POST)
	public ModelAndView deliveryOrderList(Integer page) {
		
		List<DeliveryOrder> deliveryOrderList = new ArrayList<>();
		Long total = 0L;
		deliveryOrderList = deliveryOrderService
			.findByStateAndOrderType(CategoryUtil.ORDERSTATUS.FOUR, CategoryUtil.ORDERTYPE.ONE, page, pageSize);
		total =
			deliveryOrderService.countByStateAndOrderType(CategoryUtil.ORDERSTATUS.FOUR, CategoryUtil.ORDERTYPE.ONE);
		for (DeliveryOrder deliveryOrder : deliveryOrderList) {
			deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
		}
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/deliveryOrder/deliveryOrderList");
		modelAndView.addObject("deliveryOrderList", deliveryOrderList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 确认入库订单列表
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/storageConfirmList", method = RequestMethod.POST)
	public ModelAndView storageConfirmList(Integer page) {
		
		List<DeliveryOrder> deliveryOrderList = new ArrayList<>();
		Long total = 0L;
		deliveryOrderList = deliveryOrderService
			.findByStateAndOrderType(CategoryUtil.ORDERSTATUS.EIGHT, CategoryUtil.ORDERTYPE.TOW, page, pageSize);
		total =
			deliveryOrderService.countByStateAndOrderType(CategoryUtil.ORDERSTATUS.EIGHT, CategoryUtil.ORDERTYPE.TOW);
		for (DeliveryOrder deliveryOrder : deliveryOrderList) {
			deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
		}
		total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
		ModelAndView modelAndView = new ModelAndView("/deliveryOrder/storageConfirmList");
		modelAndView.addObject("deliveryOrderList", deliveryOrderList);
		modelAndView.addObject("page", page);
		modelAndView.addObject("total", total);
		return modelAndView;
	}
	
	/**
	 * 确认出库
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/confirmLibrary", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData confirmLibrary(Long deliveryOrderId) {
		JsonResultData result = JsonResultData.success();
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		if (null == deliveryOrder)
			return result.turnError("该订单已删除");
		
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.FOUR) {
			return result.turnError("订单状态不是装车中,无法确认出库");
		}
		deliveryOrder.setState(CategoryUtil.ORDERSTATUS.FIVE);
		deliveryOrderService.updateLibrary(deliveryOrder);
		
		boolean flag = true;
		List<OrderInfo> infos =
			orderInfoService.findByOrderIdAndState(deliveryOrder.getOrderId(), CategoryUtil.ORDERINFOSTATE.ONE);
		for (OrderInfo orderInfo : infos) {
			BigDecimal number = orderInfoService
				.sumBy(orderInfo.getOrderId(), CategoryUtil.ORDERINFOSTATE.TOW, orderInfo.getMerchandiseId());
			if (number.compareTo(orderInfo.getNumber()) == -1) {
				flag = false;
				break;
			}
		}
		if (flag) {
			TheOrder order = orderService.findById(deliveryOrder.getOrderId());
			if (null != order && !CategoryUtil.ORDERSTATUS.ZERO.equals(order.getState())) {
				order.setState(CategoryUtil.ORDERSTATUS.FIVE);
				orderService.update(order);
				User user = userService.findById(deliveryOrder.getDeliveryId());
				if (StringUtil.isNotEmpty(user.getOpenid())) {
					Map<String, String> map = new HashMap<>();
					map.put("first", "出库已经确认");
					map.put("keyword1", "");
					map.put("keyword2", DateUtil.getDateFormat(new Date()));
					map.put("keyword3", "");
					map.put("keyword4", deliveryOrder.getDeliveryName());
					map.put("keyword5", "");
					WeixinTask.sendMessage(user.getOpenid(), CategoryUtil.TEMPLATEID.TWO, "", map);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 确认入库
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/confirmStorage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData confirmStorage(Long deliveryOrderId) {
		JsonResultData result = JsonResultData.success();
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		if (null == deliveryOrder)
			return result.turnError("该订单已删除");
		
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.EIGHT) {
			return result.turnError("订单状态不是已送达,无法确认入库");
		}
		deliveryOrder.setState(CategoryUtil.ORDERSTATUS.SIX);
		deliveryOrderService.updateStorage(deliveryOrder);
		return result;
	}
	
}
