package net.mxh.task;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.mxh.entity.Admin;
import net.mxh.entity.Merchandise;
import net.mxh.entity.TheOrder;
import net.mxh.service.AdminService;
import net.mxh.service.MerchandiseService;
import net.mxh.service.OrderService;
import net.mxh.util.CategoryUtil;

@Component
public class TimeTask {
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private AdminService adminService;
	
	@Scheduled(cron = "0 0 17 * * ?")
	public void timing() {
		Admin admin = adminService.findByRoleIdAndOpenidNotNull(3L);
		if (null != admin) {
			List<Merchandise> merchandises = merchandiseService.findByIsUse(CategoryUtil.WHETHER.YES, null);
			for (Merchandise merchandise : merchandises) {
				if (merchandise.getBottomLine() != null && merchandise.getBottomLine().compareTo(new BigDecimal(0)) == 1
					&& merchandise.getBottomLine().compareTo(merchandise.getTotal()) == 1) {
					Map<String, String> map = new HashMap<>();
					map.put("first", "库存已不足");
					map.put("keyword1", merchandise.getMerchandiseName());
					map.put("keyword2", merchandise.getTotal().toString());
					map.put("keyword3", merchandise.getBottomLine().toString());
					map.put("remark", "请抓紧时间补货");
					WeixinTask.sendMessage(admin.getOpenid(), CategoryUtil.TEMPLATEID.ZERO, "", map);
				}
			}
		}
	}
	
	@Scheduled(cron = "0 0 22 * * ?")
	public void confirm() {
		List<TheOrder> orders = orderService.findUnconfirmed();
		for (TheOrder order : orders) {
			order.setState(CategoryUtil.ORDERSTATUS.SEVEN);
			orderService.update(order);
		}
	}
	
}
