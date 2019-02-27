package net.mxh.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.admin.main.bean.OrderStat;
import net.mxh.dao.OrderInfoDao;
import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.Merchandise;
import net.mxh.entity.OrderInfo;
import net.mxh.entity.TheOrder;
import net.mxh.util.CategoryUtil;

@Service
@Transactional
public class OrderInfoService {
	
	@Autowired
	private OrderInfoDao orderInfoDao;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	public void save(OrderInfo entity) {
		orderInfoDao.save(entity);
	}
	
	public void update(OrderInfo entity) {
		orderInfoDao.update(entity);
	}
	
	public void delete(OrderInfo entity) {
		orderInfoDao.delete(entity);
	}
	
	public OrderInfo findById(Long id) {
		return orderInfoDao.findById(OrderInfo.class, id);
	}
	
	public List<OrderInfo> findAll() {
		return orderInfoDao.findAll(OrderInfo.class);
	}
	
	public List<OrderInfo> findAll(Integer page, Integer pageSize) {
		return orderInfoDao.findAll(OrderInfo.class, "createTime", false, page, pageSize);
	}
	
	public long count() {
		return orderInfoDao.count(OrderInfo.class);
	}
	
	public void saveOrUpdate(OrderInfo orderInfo) {
		orderInfoDao.saveOrUpdate(orderInfo);
	}
	
	public void deleteByOrderId(Long orderId) {
		orderInfoDao.deleteByOrderId(orderId);
	}
	
	public List<OrderInfo> findByOrderIdAndState(Long orderId, Integer state) {
		Map<String, Object> param = new HashMap<>();
		param.put("orderId", orderId);
		if (state != null) {
			param.put("state", state);
		}
		return orderInfoDao.findByParam(OrderInfo.class, param);
	}

	public List<OrderInfo> findByOrderId(Long orderId) {
		Map<String, Object> param = new HashMap<>();
		param.put("orderId", orderId);
		return orderInfoDao.findByParam(OrderInfo.class, param);
	}

	/**
	 * @author bigduu
	 */
	public OrderInfo findOneBy(Long orderId, Integer state, Long merchandiseId) {
		Map<String, Object> param = new HashMap<>();
		param.put("orderId", orderId);
		param.put("state", state);
		param.put("merchandiseId", merchandiseId);
		List<OrderInfo> infos = orderInfoDao.findByParam(OrderInfo.class, param);
		if (infos.isEmpty()) {
			return null;
		}
		return infos.get(0);
	}
	
	public List<OrderInfo> findByDeliveryOrderId(Long deliveryOrderId) {
		Map<String, Object> param = new HashMap<>();
		param.put("deliveryOrderId", deliveryOrderId);
		return orderInfoDao.findByParam(OrderInfo.class, param);
	}
	
	public BigDecimal sumBy(Long orderId, Integer state, Long merchandiseId) {
		return orderInfoDao.sumBy(orderId, state, merchandiseId);
	}
	
	public List<Map<String, String>> orderInfoToday(Long startTime, Long endTime) {
		return orderInfoDao.orderInfoToday(startTime, endTime);
	}
	
	public void updateNumber(OrderInfo orderInfo, BigDecimal number) {
		TheOrder order = orderService.findById(orderInfo.getOrderId());
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(orderInfo.getDeliveryOrderId());
		OrderInfo info =
			findOneBy(orderInfo.getOrderId(), CategoryUtil.ORDERINFOSTATE.ONE, orderInfo.getMerchandiseId());
		info.setNumber(info.getNumber().subtract(orderInfo.getNumber()).add(number));
		orderInfo.setNumber(number);
		orderInfoDao.update(orderInfo);
		Merchandise merchandise = merchandiseService.findById(orderInfo.getMerchandiseId());
		info.setAllPrice(merchandise.getUnitPrice().multiply(info.getNumber()));
		orderInfoDao.update(info);
		order.setAllPrice(orderInfoDao.sumAllPriceBy(order.getId(), CategoryUtil.ORDERINFOSTATE.ONE));
		orderService.update(order);
		if (deliveryOrder.getShippingcosts().compareTo(new BigDecimal("0")) == 1) {
			BigDecimal shippingcosts = orderInfoDao.sumShippingcosts(deliveryOrder.getId());
			// if (order.getOrderType() == CategoryUtil.ORDERTYPE.TOW)
			// shippingcosts = shippingcosts.multiply(new BigDecimal("2"));
			deliveryOrder.setShippingcosts(shippingcosts);
		}
		BigDecimal upstaircosts = orderInfoDao.sumUpstairsCost(deliveryOrder.getId());
		upstaircosts =
			upstaircosts.multiply(deliveryOrder.getFloor() != null ? deliveryOrder.getFloor() : BigDecimal.ZERO);
		deliveryOrder.setUpstaircosts(upstaircosts);
		
		deliveryOrderService.update(deliveryOrder);
	}
	
	public List<OrderStat> findCountList(Map<String, String> param) {
		return orderInfoDao.findCountList(param);
	}
}
