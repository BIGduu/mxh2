package net.mxh.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.mxh.dao.DeliveryOrderDao;
import net.mxh.dao.UpstairsDetailDao;
import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.TheOrder;
import net.mxh.entity.UpstairsDetail;
import net.mxh.util.CategoryUtil;

@Service
@Transactional
public class UpstairsDetailService {
	
	@Autowired
	private UpstairsDetailDao upstairsDetailDao;
	
	@Autowired
	private DeliveryOrderDao deliveryOrderDao;
	
	@Autowired
	private OrderService orderService;
	
	public void save(UpstairsDetail entity) {
		upstairsDetailDao.save(entity);
	}
	
	public void update(UpstairsDetail entity) {
		upstairsDetailDao.update(entity);
	}
	
	public UpstairsDetail findById(Long id) {
		return upstairsDetailDao.findById(UpstairsDetail.class, id);
	}
	
	public List<UpstairsDetail> findAll() {
		return upstairsDetailDao.findAll(UpstairsDetail.class);
	}
	
	public List<UpstairsDetail> findAll(Integer page, Integer pageSize) {
		return upstairsDetailDao.findAll(UpstairsDetail.class, page, pageSize);
	}
	
	public long count() {
		return upstairsDetailDao.count(UpstairsDetail.class);
	}
	
	public void saveOrUpdate(UpstairsDetail upstairsDetail) {
		upstairsDetailDao.saveOrUpdate(upstairsDetail);
	}
	
	public List<UpstairsDetail> findByOrderId(Long orderId) {
		return upstairsDetailDao.findByOrderId(orderId);
	}
	
	public void save(String upstairDetailsStr, Long adminId, Long orderId)
		throws Exception {
		Map<Long, BigDecimal> deliveryOrderShippingcostsMap = new HashMap<Long, BigDecimal>();
		JSONArray array = JSONArray.parseArray(upstairDetailsStr);
		for (Object object : array) {
			JSONObject jsonObject = JSONObject.parseObject(object.toString());
			BigDecimal number = jsonObject.getBigDecimal("number");
			Long orderIdT = jsonObject.getLong("orderId");
			Long deliveryId = jsonObject.getLong("deliveryId");
			Long merchandiseId = jsonObject.getLong("merchandiseId");
			Long deliveryOrderId = jsonObject.getLong("deliveryOrderId");
			String deliveryName = jsonObject.getString("deliveryName");
			String merchandiseName = jsonObject.getString("merchandiseName");
			String specification = jsonObject.getString("specification");
			BigDecimal upstairsCost = jsonObject.getBigDecimal("upstairsCost");
			BigDecimal floor = jsonObject.getBigDecimal("floor");
			UpstairsDetail upstairDetail = new UpstairsDetail();
			upstairDetail.setOrderId(orderIdT);
			upstairDetail.setDeliveryId(deliveryId);
			upstairDetail.setUpstairPersion(deliveryId);
			upstairDetail.setDeliveryName(deliveryName);
			upstairDetail.setDeliveryOrderId(deliveryOrderId);
			upstairDetail.setMerchandiseId(merchandiseId);
			upstairDetail.setMerchandiseName(merchandiseName);
			upstairDetail.setSpecification(specification);
			upstairDetail.setNumber(number);
			upstairDetail.setFloor(floor);
			upstairDetail.setUpstairsCost(upstairsCost);
			BigDecimal cost = number.multiply(upstairsCost).multiply(floor);
			upstairDetail.setCost(cost);
			upstairDetail.setCreateTime(new Date().getTime());
			upstairDetail.setCreatorId(adminId);
			upstairDetail.setModificationTime(new Date().getTime());
			upstairDetail.setModifierId(adminId);
			upstairsDetailDao.save(upstairDetail);
			if (null != deliveryOrderShippingcostsMap.get(deliveryOrderId)) {
				BigDecimal shippingcosts = deliveryOrderShippingcostsMap.get(deliveryOrderId);
				deliveryOrderShippingcostsMap.put(deliveryOrderId, shippingcosts.add(cost));
			}
			else {
				deliveryOrderShippingcostsMap.put(deliveryOrderId, cost);
			}
			
		}
		
		TheOrder order = orderService.findById(orderId);
		order.setState(CategoryUtil.ORDERSTATUS.SIX);
		order.setUpstairs(CategoryUtil.WHETHER.YES);
		order.setModificationTime(new Date().getTime());
		order.setModifierId(adminId);
		orderService.update(order);
		
		for (Long deliverOrderId : deliveryOrderShippingcostsMap.keySet()) {
			DeliveryOrder deliveryOrder = deliveryOrderDao.findById(DeliveryOrder.class, deliverOrderId);
			if (order.getOrderType() == CategoryUtil.ORDERTYPE.TOW) {
				deliveryOrder.setState(CategoryUtil.ORDERSTATUS.EIGHT);
			}
			else {
				deliveryOrder.setState(CategoryUtil.ORDERSTATUS.SIX);
			}
			deliveryOrder.setUpstaircosts(deliveryOrderShippingcostsMap.get(deliverOrderId));
			deliveryOrderDao.save(deliveryOrder);
		}
	}
	
	public void saveNoUpstairDetailCheck(Long adminId, Long orderId) {
		TheOrder order = orderService.findById(orderId);
		order.setState(CategoryUtil.ORDERSTATUS.SIX);
		order.setUpstairs(CategoryUtil.WHETHER.NO);
		order.setModificationTime(new Date().getTime());
		order.setModifierId(adminId);
		orderService.update(order);
		
		List<DeliveryOrder> deliveryOrderList = deliveryOrderDao.findByOrderId(orderId);
		if (CollectionUtils.isNotEmpty(deliveryOrderList)) {
			for (DeliveryOrder deliveryOrder : deliveryOrderList) {
				deliveryOrder.setUpstaircosts(BigDecimal.ZERO);
				deliveryOrder.setFloor(null);
				if (order.getOrderType() == CategoryUtil.ORDERTYPE.TOW) {
					deliveryOrder.setState(CategoryUtil.ORDERSTATUS.EIGHT);
				}
				else {
					deliveryOrder.setState(CategoryUtil.ORDERSTATUS.SIX);
				}
				deliveryOrderDao.save(deliveryOrder);
			}
		}
	}
	
	public List<UpstairsDetail> findListByOrderId(Long orderId) {
		return upstairsDetailDao.findListByOrderId(orderId);
	}
}
