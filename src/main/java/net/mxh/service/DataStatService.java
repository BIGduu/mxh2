package net.mxh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.mxh.admin.main.bean.OrderStat;
import net.mxh.dao.OrderStatDao;

@Service
@Transactional
public class DataStatService {
	@Autowired
	private OrderStatDao orderStatDao;
	
	public List<OrderStat> getOrderList(List<OrderStat> list) {
		return orderStatDao.getOrderList(list);
	}
	
	public List<OrderStat> getMerchandiseList() {
		return orderStatDao.getMerchandiseList();
	}
}
