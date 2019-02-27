package net.mxh.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.mxh.dao.DeliveryOrderDao;
import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.Merchandise;
import net.mxh.entity.OrderInfo;
import net.mxh.entity.Storage;
import net.mxh.entity.TheOrder;
import net.mxh.entity.User;
import net.mxh.task.WeixinTask;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.StringUtil;

@Service
@Transactional
public class DeliveryOrderService {
	
	@Autowired
	private DeliveryOrderDao deliveryOrderDao;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	public void save(DeliveryOrder entity) {
		deliveryOrderDao.save(entity);
	}
	
	public void update(DeliveryOrder entity) {
		deliveryOrderDao.update(entity);
	}
	
	public DeliveryOrder findById(Long id) {
		return deliveryOrderDao.findById(DeliveryOrder.class, id);
	}

	public List<DeliveryOrder> findAll() {
		return deliveryOrderDao.findAll(DeliveryOrder.class);
	}
	
	public List<DeliveryOrder> findAll(Integer page, Integer pageSize) {
		return deliveryOrderDao.findAll(DeliveryOrder.class, page, pageSize);
	}
	
	public List<DeliveryOrder> findByStateAndOrderType(Integer state, Integer orderType, Integer page,
		Integer pageSize) {
		return deliveryOrderDao.findByStateAndOrderType(state, orderType, page, pageSize);
	}
	
	public Long countByStateAndOrderType(Integer state, Integer orderType) {
		return deliveryOrderDao.countByStateAndOrderType(state, orderType);
	}
	
	public long count() {
		return deliveryOrderDao.count(DeliveryOrder.class);
	}
	
	public BigDecimal sumByOrderId(Long orderId) {
		return deliveryOrderDao.sumByOrderId(orderId);
	}
	
	public BigDecimal sumUpstairCostsByOrderId(Long orderId) {
		return deliveryOrderDao.sumUpstairCostsByOrderId(orderId);
	}
	
	public BigDecimal sumUpstaircosts(Long orderId) {
		return deliveryOrderDao.sumUpstaircosts(orderId);
	}
	
	public void saveOrUpdate(DeliveryOrder deliveryOrder) {
		deliveryOrderDao.saveOrUpdate(deliveryOrder);
	}
	
	public void delete(DeliveryOrder deliveryOrder) {
		List<OrderInfo> orderInfos = orderInfoService.findByDeliveryOrderId(deliveryOrder.getId());
		for (OrderInfo orderInfo : orderInfos) {
			orderInfoService.delete(orderInfo);
		}
		deliveryOrderDao.delete(deliveryOrder);
	}
	
	public List<DeliveryOrder> findByOrderId(Long orderId) {
		Map<String, Object> param = new HashMap<>();
		param.put("orderId", orderId);
		return deliveryOrderDao.findByParam(DeliveryOrder.class, param);
	}
	
	public List<String> findNameByOrderId(Long orderId) {
		return deliveryOrderDao.findNameByOrderId(orderId);
	}
	
	/**
	 * @description 查询
	 * @author ZhongHan
	 * @date 2017年11月29日
	 * @return
	 */
	public List<Long> findNotCanUse() {
		return deliveryOrderDao.findNotCanUse();
	}
	
	public void save(Long orderId, Long deliveryId, String orderInfoStr, DeliveryOrder deliveryOrder,
		Integer isShippingcosts)
		throws Exception {
		BigDecimal shippingcosts = new BigDecimal(0);
		JSONArray array = JSONArray.parseArray(orderInfoStr);
		for (Object object : array) {
			JSONObject jsonObject = JSONObject.parseObject(object.toString());
			String numberStr = jsonObject.getString("number");
			if (StringUtil.isNotEmpty(numberStr)) {
				BigDecimal total = orderInfoService
					.sumBy(orderId, CategoryUtil.ORDERINFOSTATE.ONE, jsonObject.getLong("merchandiseId"));
				BigDecimal sum = orderInfoService
					.sumBy(orderId, CategoryUtil.ORDERINFOSTATE.TOW, jsonObject.getLong("merchandiseId"));
				BigDecimal number = new BigDecimal(numberStr);
				if (total.subtract(sum).subtract(number).compareTo(new BigDecimal(0)) == -1) {
					throw new Exception(jsonObject.getString("merchandiseName") + "分配的数量大于订单中的数量");
				}
			}
		}
		
		Map<String, Object> param = new HashMap<>();
		param.put("deliveryId", deliveryId);
		param.put("orderId", orderId);
		// if (deliveryOrderDao.countByParam(DeliveryOrder.class, param) > 0) {
		// throw new Exception("已经给该司机分配过了，不能再分配");
		// }
		TheOrder order = orderService.findById(orderId);
		if (order.getOrderType() == CategoryUtil.ORDERTYPE.ONE) {
			isShippingcosts = CategoryUtil.WHETHER.YES;
		}
		User user = userService.findById(deliveryId);
		deliveryOrder.setOrderId(orderId);
		deliveryOrder.setDeliveryId(deliveryId);
		deliveryOrder.setDeliveryName(user.getUsername());
		deliveryOrder.setDeliveryTel(user.getTelephone());
		deliveryOrder.setUpstaircosts(new BigDecimal(0));
		deliveryOrder.setState(CategoryUtil.ORDERSTATUS.THREE);
		deliveryOrderDao.save(deliveryOrder);
		
		for (Object object : array) {
			JSONObject jsonObject = JSONObject.parseObject(object.toString());
			String numberStr = jsonObject.getString("number");
			if (StringUtil.isNotEmpty(numberStr)) {
				OrderInfo info = new OrderInfo();
				BigDecimal total = orderInfoService
					.sumBy(orderId, CategoryUtil.ORDERINFOSTATE.ONE, jsonObject.getLong("merchandiseId"));
				BigDecimal sum = orderInfoService
					.sumBy(orderId, CategoryUtil.ORDERINFOSTATE.TOW, jsonObject.getLong("merchandiseId"));
				BigDecimal number = new BigDecimal(numberStr);
				if (total.subtract(sum).subtract(number).compareTo(new BigDecimal(0)) == -1) {
					throw new Exception(jsonObject.getString("merchandiseName") + "分配的数量大于订单中的数量");
				}
				info.setDeliveryOrderId(deliveryOrder.getId());
				info.setMerchandiseId(jsonObject.getLong("merchandiseId"));
				info.setMerchandiseName(jsonObject.getString("merchandiseName"));
				info.setNumber(number);
				info.setState(CategoryUtil.ORDERINFOSTATE.TOW);
				info.setOrderId(orderId);
				orderInfoService.save(info);
				if (CategoryUtil.WHETHER.YES == isShippingcosts) {
					Merchandise merchandise = merchandiseService.findById(info.getMerchandiseId());
					shippingcosts = shippingcosts.add(merchandise.getShippingCost().multiply(info.getNumber()));
				}
			}
		}
		// if (order.getOrderType() == CategoryUtil.ORDERTYPE.TOW) {
		// shippingcosts = shippingcosts.multiply(new BigDecimal("2"));
		// }
		deliveryOrder.setShippingcosts(shippingcosts);
		
		if (StringUtil.isNotEmpty(user.getOpenid())) {
			Map<String, String> map = new HashMap<>();
			map.put("first", "你有新的订单");
			map.put("keyword1", "");
			map.put("keyword2", "");
			map.put("keyword3", order.getManagerName() + "(电话：" + order.getManagerTel() + ")");
			map.put("keyword4", "");
			map.put("keyword5", "送货地址：" + order.getReceivingAddress());
			map.put("remark", "");
			WeixinTask.sendMessage(user.getOpenid(), CategoryUtil.TEMPLATEID.ONE, "", map);
		}
	}
	
	public Long countByOrderIdAndStateNot(Long orderId, Integer state) {
		return deliveryOrderDao.countByOrderIdAndStateNot(orderId, state);
	}
	
	public Long countByOrderIdAndId(Long orderId, Long id) {
		return deliveryOrderDao.countByOrderIdAndId(orderId, id);
	}
	
	public void updateFinsh(DeliveryOrder deliveryOrder, TheOrder order) {
		deliveryOrderDao.update(deliveryOrder);
		int deliveryOrderType = CategoryUtil.ORDERSTATUS.SIX;
		if (CategoryUtil.ORDERTYPE.TOW == order.getOrderType()) {
			deliveryOrderType = CategoryUtil.ORDERSTATUS.EIGHT;
		}
		Long count = deliveryOrderDao.countByOrderIdAndStateNot(deliveryOrder.getOrderId(), deliveryOrderType);
		if (count == 0) {
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
				order.setState(CategoryUtil.ORDERSTATUS.ELEVEN);
				order.setSenttoTime(System.currentTimeMillis());
				orderService.update(order);
				User user = userService.findById(order.getManagerId());
				if (StringUtil.isNotEmpty(user.getOpenid())) {
					Map<String, String> map = new HashMap<>();
					map.put("first", "货物已经到达目的地");
					map.put("keyword1", DateUtil.getDateFormat(new Date()));
					map.put("keyword2", "明兄辉建材");
					map.put("keyword3", order.getReceivingAddress());
					map.put("remark", "司机配送到目的地，拍照完成");
					WeixinTask.sendMessage(user.getOpenid(), CategoryUtil.TEMPLATEID.THREE, "", map);
				}
			}
		}
	}
	
	/**
	 * @description 确认入库
	 * @author ZhongHan
	 * @date 2017年12月1日
	 * @param deliveryOrder
	 */
	public void updateStorage(DeliveryOrder deliveryOrder) {
		deliveryOrderDao.update(deliveryOrder);
		List<OrderInfo> orderInfos = orderInfoService.findByDeliveryOrderId(deliveryOrder.getId());
		for (OrderInfo orderInfo : orderInfos) {
			Storage storage = new Storage();
			storage.setMerchandiseId(orderInfo.getMerchandiseId());
			storage.setMerchandiseName(orderInfo.getMerchandiseName());
			storage.setNumber(orderInfo.getNumber());
			storage.setType(CategoryUtil.STORAGETYPE.FOUR);
			storage.setCreateTime(System.currentTimeMillis());
			storageService.save(storage);
		}
		Long count =
			deliveryOrderDao.countByOrderIdAndStateNot(deliveryOrder.getOrderId(), CategoryUtil.ORDERSTATUS.SIX);
		if (count == 0) {
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
				order.setState(CategoryUtil.ORDERSTATUS.SIX);
				order.setSenttoTime(System.currentTimeMillis());
				orderService.update(order);
				User user = userService.findById(order.getManagerId());
				if (StringUtil.isNotEmpty(user.getOpenid())) {
					Map<String, String> map = new HashMap<>();
					map.put("first", "货物已经到达目的地");
					map.put("keyword1", DateUtil.getDateFormat(new Date()));
					map.put("keyword2", "明兄辉建材");
					map.put("keyword3", order.getReceivingAddress());
					map.put("remark", "仓库已经确认");
					WeixinTask.sendMessage(user.getOpenid(), CategoryUtil.TEMPLATEID.THREE, "", map);
				}
			}
		}
	}
	
	/**
	 * @description 确认出库
	 * @author ZhongHan
	 * @date 2017年12月1日
	 * @param deliveryOrder
	 */
	public void updateLibrary(DeliveryOrder deliveryOrder) {
		deliveryOrderDao.update(deliveryOrder);
		List<OrderInfo> orderInfos = orderInfoService.findByDeliveryOrderId(deliveryOrder.getId());
		for (OrderInfo orderInfo : orderInfos) {
			Storage storage = new Storage();
			storage.setMerchandiseId(orderInfo.getMerchandiseId());
			storage.setMerchandiseName(orderInfo.getMerchandiseName());
			storage.setNumber(orderInfo.getNumber());
			storage.setType(CategoryUtil.STORAGETYPE.TOW);
			storage.setCreateTime(System.currentTimeMillis());
			Merchandise merchandise = merchandiseService.findById(storage.getMerchandiseId());
			storageService.save(storage, merchandise);
		}
	}
	
	public List<DeliveryOrder> findByUnFinsh(Long deliveryId, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("deliveryId", deliveryId);
		return deliveryOrderDao.findByUnFinsh(deliveryId, page, pageSize);
	}
	
	public List<DeliveryOrder> findBy(Long storesId, Integer page, Integer pageSize) {
		return deliveryOrderDao.findBy(storesId, page, pageSize);
	}
	
	public List<Map<String, String>> findBy(Long startTime, Long endTime) {
		return deliveryOrderDao.findBy(startTime, endTime);
	}
	
	public List<String> findBy(String deliveryName) {
		return deliveryOrderDao.findBy(deliveryName);
	}
}
