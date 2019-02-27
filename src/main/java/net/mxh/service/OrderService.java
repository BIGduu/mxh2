package net.mxh.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.dao.OrderDao;
import net.mxh.entity.OrderInfo;
import net.mxh.entity.TheOrder;
import net.mxh.util.CategoryUtil;
import net.mxh.util.StringUtil;
import net.mxh.vo.OrderPageDTO;

@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	public void save(TheOrder entity) {
		orderDao.save(entity);
	}
	
	public void update(TheOrder entity) {
		orderDao.update(entity);
	}
	
	public void delete(TheOrder order) {
		orderDao.delete(order);
		orderInfoService.deleteByOrderId(order.getId());
	}
	
	public TheOrder findById(Long id) {
		return orderDao.findById(TheOrder.class, id);
	}
	
	public List<TheOrder> findAll() {
		return orderDao.findAll(TheOrder.class, "placeOrderTime", false);
	}
	
	public List<TheOrder> findAll(Integer page, Integer pageSize) {
		return orderDao.findAll(TheOrder.class, "placeOrderTime", false, page, pageSize);
	}
	
	public Long count() {
		return orderDao.count(TheOrder.class);
	}
	
	public void saveOrUpdate(TheOrder order) {
		orderDao.saveOrUpdate(order);
	}
	
	public void delete(Long orderId) {
		orderDao.delete(findById(orderId));
	}
	
	public List<TheOrder> findByOrderCode(String orderCode, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("orderCode", orderCode);
		return orderDao.findByParam(TheOrder.class, param, "placeOrderTime", false, page, pageSize);
	}
	
	public Long countByOrderCode(String orderCode, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("orderCode", orderCode);
		return orderDao.countByParam(TheOrder.class, param);
	}

	public Long countByReceivingAddress(String receivingAddress) {
		Map<String, Object> param = new HashMap<>();
		param.put("receivingAddress", receivingAddress);
		return orderDao.countByParam(TheOrder.class, param);
	}


	
	public List<TheOrder> findByManagerId(Long managerId, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("managerId", managerId);
		return orderDao.findByParam(TheOrder.class, param, "placeOrderTime", false, page, pageSize);
	}
	
	public List<TheOrder> findByState(Integer state, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("state", state);
		return orderDao.findByParam(TheOrder.class, param, "placeOrderTime", false, page, pageSize);
	}
	
	public List<TheOrder> findByState(Integer state) {
		Map<String, Object> param = new HashMap<>();
		param.put("state", state);
		return orderDao.findByParam(TheOrder.class, param);
	}
	
	public void save(TheOrder order, List<OrderInfo> orderInfos) {
		save(order);
		order.setOrderCode(String.valueOf(1000000 + order.getId()));
		for (OrderInfo orderInfo : orderInfos) {
			orderInfo.setOrderId(order.getId());
			orderInfoService.save(orderInfo);
		}
	}
	
	/**
	 * @description 查询已完成订单
	 * @author ZhongHan
	 * @date 2017年11月27日
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<TheOrder> findFinish(Long managerId, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("managerId", managerId);
		param.put("state", CategoryUtil.ORDERSTATUS.SEVEN);
		return orderDao.findByParam(TheOrder.class, param, "placeOrderTime", false, page, pageSize);
	}
	
	/**
	 * @description 查询未完成订单
	 * @author ZhongHan
	 * @date 2017年11月27日
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<TheOrder> findUnFinish(Long managerId, Integer page, Integer pageSize) {
		return orderDao.findUnFinish(managerId, page, pageSize);
	}
	
	/**
	 * @description 查询退货订单
	 * @author ZhongHan
	 * @date 2017年11月27日
	 * @param userId
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<TheOrder> findReturn(Long managerId, Integer page, Integer pageSize) {
		Map<String, Object> param = new HashMap<>();
		param.put("managerId", managerId);
		param.put("orderType", CategoryUtil.ORDERTYPE.TOW);
		return orderDao.findByParam(TheOrder.class, param, "placeOrderTime", false, page, pageSize);
	}
	
	public List<TheOrder> findByStates(Set<Integer> stateList, Integer page, Integer pageSize) {
		return orderDao.findByStates(stateList, page, pageSize);
	}
	
	/**
	 * @description 根据订单状态查询
	 * @author ZhongHan
	 * @date 2017年11月28日
	 * @param stateList
	 * @return
	 */
	public Long findByStates(String[] stateList) {
		return orderDao.countByStates(stateList);
	}
	/**
	 * @author bigduu
	 */
	public OrderPageDTO findBy(Set<Integer> stateList, Map<String, String> param, Integer page, int pageSize) {
		//此处查询司机
		List<String> orderIds = new ArrayList<>();
		if (StringUtil.isNotEmpty(param.get("deliveryName"))) {
			orderIds = deliveryOrderService.findBy(param.get("deliveryName"));
			if (orderIds.isEmpty()) {
				return null;
			}
		}
		return orderDao.findBy(stateList, param, page, pageSize, orderIds);
	}

	public OrderPageDTO findByManagerName(Set<Integer> stateList, Map<String, String> param, Integer page, int pageSize){
		//orderIds 司机表
		List<String> orderIds = new ArrayList<>();
		if (StringUtil.isNotEmpty(param.get("deliveryName"))) {
			orderIds = deliveryOrderService.findBy(param.get("deliveryName"));
			if (orderIds.isEmpty()) {
				return null;
			}
		}
		return null;
	}
	
	public OrderPageDTO findBy(Map<String, String> param, Integer page, int pageSize) {
		return orderDao.findBy(param, page, pageSize);
	}
	
	public Long countByStateAndstoresId(Integer state, Long storesId) {
		Map<String, Object> param = new HashMap<>();
		param.put("state", state);
		if (null != storesId) {
			param.put("storesId", storesId);
		}
		return orderDao.countByParam(TheOrder.class, param);
	}
	
	public Long countToday(Long storesId) {
		return orderDao.countToday(storesId);
	}
	
	public List<TheOrder> findUnconfirmed() {
		return orderDao.findUnconfirmed();
	}
	
	public List<Map<String, String>> findBy(Long startTime, Long endTime) {
		return orderDao.findBy(startTime, endTime);
	}
	
	public BigDecimal totalAmount(Long orderId) {
		return orderDao.totalAmount(orderId);
	}
	
	public BigDecimal totalAmountForDriver(Long orderId) {
		return orderDao.totalAmountForDriver(orderId);
	}
	
	public BigDecimal totalAmounts(String orderIdStr) {
		return orderDao.totalAmounts(orderIdStr);
	}
	
	public List<String> addressList(Long managerId) {
		return orderDao.addressList(managerId);
	}
	
}
