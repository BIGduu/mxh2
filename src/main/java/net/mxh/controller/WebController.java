package net.mxh.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sun.org.glassfish.gmbal.ParameterNames;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.mxh.entity.Admin;
import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.Department;
import net.mxh.entity.Img;
import net.mxh.entity.Merchandise;
import net.mxh.entity.OrderInfo;
import net.mxh.entity.Signin;
import net.mxh.entity.Storage;
import net.mxh.entity.Stores;
import net.mxh.entity.TheOrder;
import net.mxh.entity.User;
import net.mxh.service.AdminService;
import net.mxh.service.BuildingService;
import net.mxh.service.DeliveryOrderService;
import net.mxh.service.DepartmentService;
import net.mxh.service.ImgService;
import net.mxh.service.MerchandiseService;
import net.mxh.service.OrderInfoService;
import net.mxh.service.OrderService;
import net.mxh.service.SigninService;
import net.mxh.service.StorageService;
import net.mxh.service.StoresService;
import net.mxh.service.UserService;
import net.mxh.task.WeixinTask;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.ImageUploadUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.StringUtil;
import net.mxh.util.ValidateUtil;

@Controller
@RequestMapping(value = "web")
public class WebController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MerchandiseService merchandiseService;
	
	@Autowired
	private StoresService storesService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BuildingService buildingService;
	
	@Autowired
	private DeliveryOrderService deliveryOrderService;
	
	@Autowired
	private OrderInfoService orderInfoService;
	
	@Autowired
	private SigninService signinService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ImgService imgService;
	
	@RequestMapping("/authorizeUrl")
	public ModelAndView authorizeUrl(HttpServletRequest request) {
		ModelAndView modelAndView = new ModelAndView("/weixin/openId");
		
		try {
			String redirectUri = URLEncoder.encode(CategoryUtil.url + "/weixin/openId", "utf-8");
			String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WeixinTask.appid
				+ "&redirect_uri=" + redirectUri
				+ "&response_type=code&scope=snsapi_base&state=yubay&connect_redirect=1#wechat_redirect";
			modelAndView.addObject("authorizeUrl", url);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return modelAndView;
	}
	
	/**
	 * 用户注册
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerPage() {
		ModelAndView modelAndView = new ModelAndView("/weixin/register");
		List<Stores> storesList = storesService.findAll();
		List<Department> departmentList = departmentService.findAll();
		modelAndView.addObject("storesList", storesList);
		modelAndView.addObject("departmentList", departmentList);
		return modelAndView;
	}
	
	/**
	 * 用户注册
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(User user, HttpSession session, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", 200);
		result.put("message", "successfully");
		
		String openid = (String)session.getAttribute("openid");
		if (openid == null) {
			result.put("status", "309");
			result.put("message", "请从微信公众号进入此页面");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		if (!ValidateUtil.isMobile(user.getTelephone())) {
			result.put("status", "308");
			result.put("message", "请传入正确的手机格式");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		user.setOpenid(openid);
		user.setCreateTime(System.currentTimeMillis());
		user.setStatus(CategoryUtil.USERSTATUS.ONE);
		if (user.getDepartmentId().equals(CategoryUtil.DEPARTMENTID.TOW)) {
			user.setStoresId(null);
			user.setStoresName(null);
		}
		userService.save(user);
		session.invalidate();
		session = request.getSession();
		session.setAttribute("user", user);
		session.setAttribute("type", user.getDepartmentId());
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 库管登录
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginPage() {
		return new ModelAndView("/weixin/login");
	}
	
	/**
	 * 库管登录
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(String username, String password, HttpSession session, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", 200);
		result.put("message", "successfully");
		
		String openid = (String)session.getAttribute("openid");
		//用于本地测试的openid
//		openid = "oAG0V0kE4Cj7XBzWnJNxMeOKX4XA";

		if (openid == null) {
			result.put("status", "309");
			result.put("message", "请从微信公众号进入此页面");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}

		Admin admin = adminService.findByAdminName(username);
		if (admin == null) {
			result.put("status", "309");
			result.put("message", "用户不存在");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		if (!admin.getRoleId().equals(3L)) {
			result.put("status", "309");
			result.put("message", "只有仓管才能登陆");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		if (!password.trim().equals(admin.getPassword())) {
			result.put("status", "309");
			result.put("message", "密码错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		admin.setOpenid(openid);
		adminService.update(admin);
		session.invalidate();
		session = request.getSession();
		session.setAttribute("admin", admin);
		session.setAttribute("type", CategoryUtil.TYPE.THREE);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 注销登录
	 * @return
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public ModelAndView logout(HttpSession session) {
		Admin admin = (Admin)session.getAttribute("admin");
		admin.setOpenid(null);
		adminService.update(admin);
		session.invalidate();
		return new ModelAndView("/weixin/login");
	}
	
	/**
	 * 商品列表
	 * @return
	 */
	@RequestMapping(value = "/merchandiseList", method = RequestMethod.GET)
	@ResponseBody
	public String merchandiseList(HttpSession session) {
		User user = (User)session.getAttribute("user");
		Long storesId = null;
		if (user != null) {
			storesId = user.getStoresId();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		List<Merchandise> merchandises = merchandiseService.findByIsUse(CategoryUtil.WHETHER.YES, storesId);
		for (Merchandise merchandise : merchandises) {
			merchandise.setImg(ImageUploadUtil.getRealUrl(merchandise.getImg()));
		}
		data.put("merchandises", merchandises);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.JSONFILTERS.MERCHANDISELIST, CategoryUtil.features);
	}
	
	/**
	 * 司机送达拍照提交
	 * @return
	 */
	@RequestMapping(value = "/deliverySubmit", method = RequestMethod.POST)
	@ResponseBody
	public String deliverySubmit(HttpSession session, Long deliveryOrderId, String serverIds, BigDecimal upstairs,
		String upstairPersion) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		User user = (User)session.getAttribute("user");
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		if (null == deliveryOrder || !deliveryOrder.getDeliveryId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		String[] serverIdArr = {};
		if (StringUtil.isNotEmpty(serverIds)) {
			serverIdArr = serverIds.split(",");
		}
		else {
			result.put("status", "302");
			result.put("message", "图片不能为空");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		List<String> imgList = new ArrayList<>();
		for (String serverId : serverIdArr) {
			imgList.add(WeixinTask.image(serverId));
		}
		deliveryOrder.setImg(String.join(",", imgList));
		
		TheOrder order = orderService.findById(deliveryOrder.getOrderId());
		if (order.getOrderType() == CategoryUtil.ORDERTYPE.TOW) {
			deliveryOrder.setState(CategoryUtil.ORDERSTATUS.EIGHT);
		}
		else {
			deliveryOrder.setState(CategoryUtil.ORDERSTATUS.SIX);
		}
		
		if (null != upstairs && upstairs.intValue() > 0) {
			BigDecimal upstaircosts = new BigDecimal(0);
			List<OrderInfo> orderInfos = orderInfoService.findByDeliveryOrderId(deliveryOrderId);
			for (OrderInfo orderInfo : orderInfos) {
				Merchandise merchandise = merchandiseService.findById(orderInfo.getMerchandiseId());
				upstaircosts =
					upstaircosts.add(merchandise.getUpstairsCost().multiply(orderInfo.getNumber()).multiply(upstairs));
			}
			deliveryOrder.setUpstaircosts(upstaircosts);
		}
		deliveryOrder.setFloor(upstairs);
		deliveryOrder.setUpstairPersion(upstairPersion);
		deliveryOrderService.updateFinsh(deliveryOrder, order);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 项目经理确认
	 * @return
	 */
	@RequestMapping(value = "/confirmOrder", method = RequestMethod.POST)
	@ResponseBody
	public String confirmOrder(HttpSession session, Long orderId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		TheOrder order = orderService.findById(orderId);
		User user = (User)session.getAttribute("user");
		if (null == order || !order.getManagerId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		if (order.getState() != CategoryUtil.ORDERSTATUS.SIX) {
			result.put("status", "400");
			result.put("message", "订单状态错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		order.setState(CategoryUtil.ORDERSTATUS.SEVEN);
		order.setCheckState(CategoryUtil.CHECKSTATUS.ZERO);
		orderService.update(order);
		
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 订单异常
	 * @return
	 */
	@RequestMapping(value = "/abnormalOrder", method = RequestMethod.POST)
	@ResponseBody
	public String abnormalOrder(@RequestParam Map<String, String> param,HttpSession session, Long orderId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");

		TheOrder order = orderService.findById(orderId);
		order.setRemarks(param.get("abnormalReason"));
		orderService.update(order);
		User user = (User)session.getAttribute("user");
		if (null == order || !order.getManagerId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		if (order.getState() != CategoryUtil.ORDERSTATUS.SIX) {
			result.put("status", "400");
			result.put("message", "订单状态错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		order.setState(CategoryUtil.ORDERSTATUS.ZERO);
		orderService.update(order);
		
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 订单取消
	 * @return
	 */
	@RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
	@ResponseBody
	public String cancelOrder(HttpSession session, Long orderId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		if (null == orderId) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		TheOrder order = orderService.findById(orderId);
		if (null == order) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		User user = (User)session.getAttribute("user");
		if (null == user) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		if (!order.getManagerId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		if (order.getState() != CategoryUtil.ORDERSTATUS.ONE) {
			result.put("status", "400");
			result.put("message", "订单已审核通过，无法取消");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		orderService.delete(order);
		
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 订单列表页面
	 * @return
	 */
	@RequestMapping(value = "/orderListPage", method = RequestMethod.GET)
	public ModelAndView orderListPage(Integer type) {
		ModelAndView modelAndView = new ModelAndView("/weixin/orderList");
		modelAndView.addObject("type", type);
		return modelAndView;
	}
	
	/**
	 * 订单列表
	 * @return
	 */
	@RequestMapping(value = "/orderList", method = RequestMethod.GET)
	@ResponseBody
	public String orderList(HttpSession session, Integer page, Integer pageSize, Integer type) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", 200);
		result.put("message", "successfully");
		List<TheOrder> orders = new ArrayList<>();
		User user = (User)session.getAttribute("user");
		if (type == 1)
			orders = orderService.findUnFinish(user.getId(), page, pageSize);
		else
			orders = orderService.findFinish(user.getId(), page, pageSize);
		for (TheOrder order : orders) {
			order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
			if (CategoryUtil.ORDERTYPE.ONE != order.getOrderType()) {
				order.setShippingcosts(deliveryOrderService.sumByOrderId(order.getId()));
				if (CategoryUtil.ORDERTYPE.TOW == order.getOrderType())
					order.setShippingcosts(order.getShippingcosts().multiply(new BigDecimal("2")));
			}
		}
		data.put("orderList", orders);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 订单详情
	 * @return
	 */
	@RequestMapping(value = "/orderDetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView orderDetail(HttpSession session, Long orderId) {
		ModelAndView modelAndView = new ModelAndView("/weixin/orderDetail");
		
		TheOrder order = orderService.findById(orderId);
		order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
		order.setShippingcosts(deliveryOrderService.sumByOrderId(order.getId()));
		order.setUpstaircosts(deliveryOrderService.sumUpstairCostsByOrderId(order.getId()));
		if (CategoryUtil.ORDERTYPE.TOW == order.getOrderType()) {
			order.setShippingcosts(order.getShippingcosts().multiply(new BigDecimal("2")));
		}
		List<OrderInfo> orderInfos = orderInfoService.findByOrderIdAndState(orderId, CategoryUtil.ORDERINFOSTATE.ONE);
		List<String> images = new ArrayList<>();
		if (order.getState() == CategoryUtil.ORDERSTATUS.SIX || order.getState() == CategoryUtil.ORDERSTATUS.SEVEN) {
			List<DeliveryOrder> deliveryOrders = deliveryOrderService.findByOrderId(orderId);
			for (DeliveryOrder deliveryOrder : deliveryOrders) {
				if (StringUtil.isNotEmpty(deliveryOrder.getImg())) {
					String[] imgs = deliveryOrder.getImg().split(",");
					for (String string : imgs) {
						images.add(ImageUploadUtil.getRealUrl(string));
					}
				}
			}
			if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
				List<Img> imgs = imgService.findByOrderId(orderId, CategoryUtil.IMG_TYPE.UPSTAIRES.getType());
				if (CollectionUtils.isNotEmpty(imgs)) {
					for (Img img : imgs) {
						images.add(ImageUploadUtil.getRealUrl(img.getImg()));
					}
				}
			}
		}
		modelAndView.addObject("images", images);
		modelAndView.addObject("order", order);
		modelAndView.addObject("orderInfos", orderInfos);
		return modelAndView;
	}
	
	/**
	 * 司机端订单列表页面
	 * @return
	 */
	@RequestMapping(value = "/deliveryOrderListPage", method = RequestMethod.GET)
	public ModelAndView deliveryOrderListPage() {
		ModelAndView modelAndView = new ModelAndView("/weixin/deliveryOrderList");
		return modelAndView;
	}
	
	/**
	 * 司机订单列表
	 * @return
	 */
	@RequestMapping(value = "/deliveryOrderList", method = RequestMethod.GET)
	@ResponseBody
	public String deliveryOrderList(HttpSession session, Integer page, Integer pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", 200);
		result.put("message", "successfully");
		User user = (User)session.getAttribute("user");
		List<DeliveryOrder> deliveryOrders = new ArrayList<>();
		deliveryOrders = deliveryOrderService.findByUnFinsh(user.getId(), page, pageSize);
		for (DeliveryOrder deliveryOrder : deliveryOrders) {
			TheOrder order = orderService.findById(deliveryOrder.getOrderId());
			deliveryOrder.setReceivingAddress(order.getReceivingAddress());
			deliveryOrder.setOrderType(order.getOrderType());
			deliveryOrder.setManagerName(order.getManagerName());
			deliveryOrder.setManagerTel(order.getManagerTel());
			deliveryOrder.setStoresName(order.getStoresName());
			deliveryOrder.setUpstairs(order.getUpstairs());
		}
		data.put("deliveryOrderList", deliveryOrders);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.JSONFILTERS.DELIVERYORDERTWO, CategoryUtil.features);
	}
	
	/**
	 * 司机订单详情
	 * @return
	 */
	@RequestMapping(value = "/deliveryOrderDetail", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView deliveryOrderDetail(HttpSession session, Long deliveryOrderId) {
		ModelAndView modelAndView = new ModelAndView("/weixin/deliveryOrderDetail");
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
		TheOrder order = orderService.findById(deliveryOrder.getOrderId());
		deliveryOrder.setReceivingAddress(order.getReceivingAddress());
		deliveryOrder.setOrderType(order.getOrderType());
		deliveryOrder.setManagerName(order.getManagerName());
		deliveryOrder.setManagerTel(order.getManagerTel());
		deliveryOrder.setStoresName(order.getStoresName());
		deliveryOrder.setUpstairs(order.getUpstairs());
		List<String> images = new ArrayList<>();
		if (StringUtil.isNotEmpty(deliveryOrder.getImg())) {
			String[] imgs = deliveryOrder.getImg().split(",");
			for (String string : imgs) {
				images.add(ImageUploadUtil.getRealUrl(string));
			}
		}
		modelAndView.addObject("deliveryOrder", deliveryOrder);
		modelAndView.addObject("images", images);
		return modelAndView;
	}
	
	/**
	 * 司机接受订单
	 * @return
	 */
	@RequestMapping(value = "/deliveryAccept", method = RequestMethod.POST)
	@ResponseBody
	public String deliveryAccept(HttpSession session, Long deliveryOrderId, String reason) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		User user = (User)session.getAttribute("user");
		if (null == deliveryOrder || !deliveryOrder.getDeliveryId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.THREE) {
			result.put("status", "400");
			result.put("message", "订单状态错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		TheOrder order = orderService.findById(deliveryOrder.getOrderId());
		if (CategoryUtil.ORDERTYPE.ONE == order.getOrderType()) {
			deliveryOrder.setState(CategoryUtil.ORDERSTATUS.TEN);
			// order.setState(CategoryUtil.ORDERSTATUS.TEN);
		}
		else {
			deliveryOrder.setState(CategoryUtil.ORDERSTATUS.FIVE);
			boolean flag = true;
			List<OrderInfo> infos =
				orderInfoService.findByOrderIdAndState(deliveryOrder.getOrderId(), CategoryUtil.ORDERINFOSTATE.ONE);
			for (OrderInfo orderInfo : infos) {
				BigDecimal number = orderInfoService.sumBy(orderInfo.getOrderId(),
					CategoryUtil.ORDERINFOSTATE.TOW,
					orderInfo.getMerchandiseId());
				if (number.compareTo(orderInfo.getNumber()) == -1) {
					flag = false;
					break;
				}
			}
			if (flag && null != order && !CategoryUtil.ORDERSTATUS.ZERO.equals(order.getState())) {
				order.setState(CategoryUtil.ORDERSTATUS.FIVE);
				orderService.update(order);
			}
		}
		if (StringUtil.isNotEmpty(reason)) {
			deliveryOrder.setRemarks(reason);
		}
		deliveryOrderService.update(deliveryOrder);
		
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 司机装货完成
	 * @return
	 */
	@RequestMapping(value = "/shipment", method = RequestMethod.POST)
	@ResponseBody
	public String shipment(HttpSession session, Long deliveryOrderId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		User user = (User)session.getAttribute("user");
		if (null == deliveryOrder || !deliveryOrder.getDeliveryId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.TEN) {
			result.put("status", "400");
			result.put("message", "订单状态错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		deliveryOrder.setState(CategoryUtil.ORDERSTATUS.FOUR);
		deliveryOrderService.update(deliveryOrder);
		Admin admin = adminService.findByRoleIdAndOpenidNotNull(3L);
		if (null != admin && StringUtil.isNotEmpty(admin.getOpenid())) {
			Map<String, String> map = new HashMap<>();
			map.put("first", "司机已经装货完成");
			map.put("keyword1", "");
			map.put("keyword2", DateUtil.getDateFormat(new Date()));
			map.put("keyword3", "");
			map.put("keyword4", deliveryOrder.getDeliveryName());
			map.put("keyword5", "");
			WeixinTask.sendMessage(admin.getOpenid(), CategoryUtil.TEMPLATEID.TWO, "", map);
		}
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 司机拒绝订单
	 * @return
	 */
	@RequestMapping(value = "/deliveryRefuse", method = RequestMethod.POST)
	@ResponseBody
	public String deliveryRefuse(HttpSession session, Long deliveryOrderId, String reason) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		User user = (User)session.getAttribute("user");
		if (null == deliveryOrder || !deliveryOrder.getDeliveryId().equals(user.getId())) {
			result.put("status", "302");
			result.put("message", "订单错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.FIVE) {
			result.put("status", "400");
			result.put("message", "订单状态错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		deliveryOrder.setState(CategoryUtil.ORDERSTATUS.NINE);
		if (StringUtil.isNotEmpty(reason)) {
			deliveryOrder.setRemarks(reason);
		}
		deliveryOrderService.update(deliveryOrder);
		
		/*
		 * TheOrder order = orderService.findById(deliveryOrder.getOrderId());
		 * order.setState(CategoryUtil.ORDERSTATUS.NINE); orderService.update(order);
		 */
		
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 签到
	 * @return
	 */
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	@ResponseBody
	public String signin(HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		Long now = System.currentTimeMillis();
		if (now < DateUtil.getToday(19, 0) || now > DateUtil.getToday(22, 0)) {
			result.put("status", "400");
			result.put("message", "请在当天晚上19~22点签到");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		User user = (User)session.getAttribute("user");
		if (signinService.countByTime(user.getId()) > 0) {
			result.put("status", "400");
			result.put("message", "当日已签到");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		Signin signin = new Signin();
		signin.setSignTime(now);
		signin.setUserId(user.getId());
		signinService.save(signin);
		session.setAttribute("sign", 1);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 提交订单
	 * @return
	 */
	@RequestMapping(value = "/orderAdd", method = RequestMethod.POST)
	@ResponseBody
	public String orderAdd(String orderInfoStr, HttpSession session, Long receivingBuildingId, String receivingAddress,
		Integer orderType, String upstairs) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", "200");
		result.put("message", "successfully");
		User user = (User)session.getAttribute("user");
		TheOrder order = new TheOrder();
		order.setPlaceOrderTime(System.currentTimeMillis());
		order.setManagerId(user.getId());
		order.setManagerName(user.getUsername());
		order.setManagerTel(user.getTelephone());
		order.setReceivingBuildingId(receivingBuildingId);
		order.setReceivingAddress(receivingAddress);
		order.setIsPay(CategoryUtil.WHETHER.NO);
		order.setStoresId(user.getStoresId());
		order.setStoresName(user.getStoresName());
		order.setOrderType(orderType);
		order.setState(CategoryUtil.ORDERSTATUS.ONE);
		order.setCreateTime(System.currentTimeMillis());
		order.setCreatorId(user.getId());
		order.setModificationTime(System.currentTimeMillis());
		order.setModifierId(user.getId());
		order.setCheckState(CategoryUtil.CHECKSTATUS.ZERO);
		if (StringUtil.isNotEmpty(upstairs))
			order.setUpstairs(CategoryUtil.WHETHER.YES);
		else
			order.setUpstairs(CategoryUtil.WHETHER.NO);
		JSONArray array = JSONArray.parseArray(orderInfoStr);
		List<OrderInfo> orderInfos = new ArrayList<>();
		BigDecimal allPrice = new BigDecimal("0");
		for (Object object : array) {
			JSONObject jsonObject = JSONObject.parseObject(object.toString());
			OrderInfo orderInfo = new OrderInfo();
			Long merchandiseId = jsonObject.getLong("merchandiseId");
			BigDecimal number = new BigDecimal(jsonObject.getString("number"));
			Merchandise merchandise = merchandiseService.findById(merchandiseId);
			orderInfo.setMerchandiseId(jsonObject.getLong("merchandiseId"));
			orderInfo.setMerchandiseName(merchandise.getMerchandiseName());
			orderInfo.setNumber(number);
			if (CategoryUtil.ORDERTYPE.THREE == orderType) {
				orderInfo.setAllPrice(merchandise.getShippingCost().multiply(number));
			}
			else if (CategoryUtil.ORDERTYPE.TOW == orderType) {
				orderInfo.setAllPrice(merchandise.getUnitPrice().multiply(number).multiply(new BigDecimal(-1)));
			}
			else {
				orderInfo.setAllPrice(merchandise.getUnitPrice().multiply(number));
			}
			orderInfo.setState(CategoryUtil.ORDERINFOSTATE.ONE);
			orderInfos.add(orderInfo);
			allPrice = allPrice.add(orderInfo.getAllPrice());
		}
		order.setAllPrice(allPrice);
		orderService.save(order, orderInfos);
		
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 购物车页面
	 * @return
	 */
	@RequestMapping(value = "/shoppingcard", method = RequestMethod.GET)
	public ModelAndView shoppingcard(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("/weixin/shoppingcard");
		return modelAndView;
	}
	
	/**
	 * 商品列表
	 * @return
	 */
	@RequestMapping(value = "/product", method = RequestMethod.GET)
	public ModelAndView product(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("/weixin/product");
		return modelAndView;
	}
	
	/**
	 * 购物车页面
	 * @return
	 */
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public ModelAndView my(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("/weixin/my");
		return modelAndView;
	}
	
	/**
	 * 错误页面
	 * @return
	 */
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public ModelAndView error(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("/weixin/error");
		String message = StringUtil.toString(session.getAttribute("message"));
		if (StringUtil.isNotEmpty(message)) {
			modelAndView.addObject("message", message);
			session.removeAttribute("message");
		}
		return modelAndView;
	}
	
	/**
	 * 填写地址
	 * @return
	 */
	@RequestMapping(value = "/addressadd", method = RequestMethod.GET)
	public ModelAndView addressadd(HttpSession session) {
		ModelAndView modelAndView = new ModelAndView("/weixin/addressadd");
		User user = (User)session.getAttribute("user");
		// modelAndView.addObject("addressList", orderService.addressList(user.getId()));
		modelAndView.addObject("buildingList", buildingService.findByManagerId(user.getId()));
		return modelAndView;
	}
	
	/**
	 * 出入库页面
	 * @return
	 */
	@RequestMapping(value = "/outofStoragePage", method = RequestMethod.GET)
	public ModelAndView outofStoragePage() {
		ModelAndView modelAndView = new ModelAndView("/weixin/outofStorage");
		return modelAndView;
	}
	
	/**
	 * 出入库订单列表
	 * @return
	 */
	@RequestMapping(value = "/outofStorage", method = RequestMethod.GET)
	@ResponseBody
	public String outofStorage(HttpSession session, Integer page, Integer pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", 200);
		result.put("message", "successfully");
		Admin admin = (Admin)session.getAttribute("admin");
		
		List<DeliveryOrder> deliveryOrders = new ArrayList<>();
		deliveryOrders = deliveryOrderService.findBy(admin.getStoresId(), page, pageSize);
		for (DeliveryOrder deliveryOrder : deliveryOrders) {
			deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
		}
		data.put("deliveryOrderList", deliveryOrders);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
	
	/**
	 * 确认出库
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/confirmLibrary", method = RequestMethod.POST)
	@ResponseBody
	public JsonResultData confirmLibrary(HttpSession session, Long deliveryOrderId) {
		JsonResultData result = JsonResultData.success();
		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
		if (null == deliveryOrder) {
			return result.turnError("订单错误");
		}
		
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.FOUR) {
			return result.turnError("订单状态不是装车中,无法确认出库");
		}
		deliveryOrder.setState(CategoryUtil.ORDERSTATUS.FIVE);
		deliveryOrderService.updateLibrary(deliveryOrder);
		
		boolean flag = true;
		List<OrderInfo> infos =
			orderInfoService.findByOrderIdAndState(deliveryOrder.getOrderId(), CategoryUtil.ORDERINFOSTATE.ONE);
		for (OrderInfo orderInfo : infos) {
			BigDecimal number = orderInfoService.sumBy(orderInfo.getOrderId(),
				CategoryUtil.ORDERINFOSTATE.TOW,
				orderInfo.getMerchandiseId());
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
	public JsonResultData confirmStorage(HttpSession session, @RequestParam Map<String,String> param) {
		//因为param里面有3个参数 去掉id这个key方便后面遍历
		Long deliveryOrderId = Long.parseLong(param.get("deliveryOrderId"));
		param.remove("deliveryOrderId");
		JsonResultData result = JsonResultData.success();

		DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);

		if (null == deliveryOrder) {
			return result.turnError("订单错误");
		}
		if (deliveryOrder.getState() != CategoryUtil.ORDERSTATUS.EIGHT) {
			return result.turnError("订单状态不是已送达,无法确认入库");
		}

		//deliveryOrderId不为空的话
		TheOrder theOrder = orderService.findById(deliveryOrder.getOrderId());
		List<OrderInfo> orderInfos = orderInfoService.findByOrderIdAndState(theOrder.getId(), 2);

		//用来统计是否所有的货物的数量都入库了
		Integer sum = 0;
		//是否都是可以通过的flag
		Boolean flag = false;
		Map<OrderInfo,BigDecimal> updataList = new HashMap<>();
		for (OrderInfo orderInfo:orderInfos){
			String s = param.get(orderInfo.getMerchandiseName());
			BigDecimal number = new BigDecimal(s);
			if (orderInfo.getNumber().compareTo(number) == -1){
				flag = true;
			}
			if (orderInfo.getNumber().compareTo(number) == 0){
				sum++;
			}
			if (orderInfo.getNumber().compareTo(number) == 1){
				updataList.put(orderInfo, number);
			}
		}
		if (flag){
			return result.turnError("入库数量大于订单数量");
		}else{
			if (sum == param.size()){
				deliveryOrder.setState(CategoryUtil.ORDERSTATUS.SIX);
				deliveryOrderService.updateStorage(deliveryOrder);
			}else{
				Set<OrderInfo> orderInfos1 = updataList.keySet();
				for (OrderInfo orderInfo:orderInfos1){
					BigDecimal bigDecimal = updataList.get(orderInfo);
					BigDecimal subtract = orderInfo.getNumber().subtract(bigDecimal);
					orderInfoService.updateNumber(orderInfo, subtract);
				}
			}
		}
		return result;
	}
	
	/**
	 * 库存管理
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/storageListPage", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView storageListPage(Long deliveryOrderId) {
		ModelAndView modelAndView = new ModelAndView("/weixin/storageList");
		return modelAndView;
	}
	
	/**
	 * 库存管理
	 * @param request
	 * @param page
	 * @return
	 */
	@RequestMapping(value = "/storageList", method = RequestMethod.GET)
	@ResponseBody
	public String storageList(HttpSession session, Integer page, Integer pageSize) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		result.put("status", 200);
		result.put("message", "successfully");
		
		List<Storage> storages = storageService.findAll(page, pageSize);
		for (Storage storage : storages) {
			storage.setCreateTimeStr(DateUtil.getDateFormat(new Date(storage.getCreateTime())));
		}
		data.put("storages", storages);
		result.put("data", data);
		return JSONObject.toJSONString(result, CategoryUtil.JSONFILTERS.STORAGE, CategoryUtil.features);
	}
	
	@RequestMapping(value = "/signature", method = RequestMethod.POST)
	@ResponseBody
	public String signature(HttpServletRequest request, String url) {
		Map<String, Object> result = new HashMap<String, Object>();
		if (!url.startsWith(CategoryUtil.url + "/web")) {
			result.put("status", 400);
			result.put("message", "地址错误");
			return JSONObject.toJSONString(result, CategoryUtil.features);
		}
		
		String nonceStr = UUID.randomUUID().toString().replace("-", "");
		Long timestamp = System.currentTimeMillis() / 1000;
		result.put("appId", WeixinTask.appid);
		result.put("timestamp", timestamp);
		result.put("nonceStr", nonceStr);
		result.put("signature", WeixinTask.signature(url, nonceStr, timestamp));
		
		result.put("status", 200);
		result.put("message", "successfully");
		return JSONObject.toJSONString(result, CategoryUtil.features);
	}
}
