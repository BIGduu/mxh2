package net.mxh.admin.main.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import net.mxh.admin.main.bean.CommonSearch;
import net.mxh.admin.main.bean.CommonSelect;
import net.mxh.admin.main.bean.UserCheckbox;
import net.mxh.entity.Admin;
import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.Img;
import net.mxh.entity.Merchandise;
import net.mxh.entity.OrderInfo;
import net.mxh.entity.Stores;
import net.mxh.entity.TheOrder;
import net.mxh.entity.UpstairsDetail;
import net.mxh.entity.User;
import net.mxh.service.DeliveryOrderService;
import net.mxh.service.ImgService;
import net.mxh.service.MerchandiseService;
import net.mxh.service.OrderInfoService;
import net.mxh.service.OrderService;
import net.mxh.service.StoresService;
import net.mxh.service.UpstairsDetailService;
import net.mxh.service.UserService;
import net.mxh.util.CategoryUtil;
import net.mxh.util.DateUtil;
import net.mxh.util.ExportFinalBoolExcel;
import net.mxh.util.ImageUploadUtil;
import net.mxh.util.JsonResultData;
import net.mxh.util.StringUtil;
import net.mxh.vo.FinalOrderExcel;
import net.mxh.vo.OrderPageDTO;

@SuppressWarnings ({"Duplicates" , "javadoc"})
@Controller
@RequestMapping (value = "admin/order")
public class OrderController {

    private static int pageSize = 10;

    private static int maxSize = 10000;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private DeliveryOrderService deliveryOrderService;

    @Autowired
    private MerchandiseService merchandiseService;

    @Autowired
    private StoresService storesService;

    @Autowired
    private UpstairsDetailService upstairsDetailService;

    @Autowired
    private ImgService imgService;

    private static List<CommonSelect> selectList = new ArrayList<>();

    static {
        selectList.add(new CommonSelect(0 , "异常"));
        selectList.add(new CommonSelect(1 , "未审核"));
        selectList.add(new CommonSelect(2 , "已审核"));
        selectList.add(new CommonSelect(6 , "已送达"));
        selectList.add(new CommonSelect(7 , "已完成"));
    }

    private static List<CommonSelect> checkList = new ArrayList<>();

    static {
        checkList.add(new CommonSelect(0 , "未对账"));
        checkList.add(new CommonSelect(1 , "已提交对账"));
        checkList.add(new CommonSelect(2 , "材料部已审核"));
    }





    /**
     * 进行中的订单列表
     * @param request
     * @param page
     * @return
     */

    @RequestMapping (value = "/orderList", method = RequestMethod.POST)
    public ModelAndView orderList(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("page");
        param.put("type" , "1");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        long total = 0;
        List<TheOrder> orderList = new ArrayList<>();
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList() , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join("," , deliveryNames));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }
        List<User> deliverys = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.TOW);
        List<CommonSearch> deliveryList = new ArrayList<>();
        for (User user : deliverys) {
            deliveryList.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/orderList");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("deliveryList" , deliveryList);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , selectList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) {
            for (String key : param.keySet()) {
                if (StringUtil.isNotEmpty(param.get(key))) {
                    map.put(key , param.get(key));
                }
            }
        }

        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * 订单列表(已完成)
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/orderListFinal", method = RequestMethod.POST)
    public ModelAndView orderListFinal(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("page");
        param.put("type" , "2");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        List<TheOrder> orderList = new ArrayList<>();
        long total = 0;
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList() , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                if (CollectionUtils.isNotEmpty(deliveryNames)) {
                    order.setDeliveryNames(String.join("," , deliveryNames));
                }
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        List<User> deliverys = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.TOW);
        List<CommonSearch> deliveryList = new ArrayList<>();
        for (User user : deliverys) {
            deliveryList.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/orderListFinal");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("deliveryList" , deliveryList);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , selectList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) {
            for (String key : param.keySet()) {
                if (StringUtil.isNotEmpty(param.get(key))) {
                    map.put(key , param.get(key));
                }
            }
        }

        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * 导出订单列表(已完成)
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/exportOrderListFinal", method = RequestMethod.POST)
    public ModelAndView exportOrderListFinal(HttpServletRequest request, HttpSession session,
                                             HttpServletResponse response) {

        Admin admin = (Admin)session.getAttribute("admin");
        Integer page = StringUtil.toInteger(request.getParameter("page"));
        Map<String, String> param = new HashMap<>();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            param.put(paraName, request.getParameter(paraName));
        }

        param.remove("page");
        param.put("type", "2");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId", admin.getStoresId().toString());
        }
        else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId(), stores2.getStoresName()));
            }
        }
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList(), param, 1, maxSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            List<TheOrder> orderList = orderPage.getOrders();
            List<FinalOrderExcel> finalOrderExcels = new ArrayList<>();
            List<Merchandise> merchandises = merchandiseService.findByIsUse(1);
            List<String> merchandiseNames = null;
            List<String> numberPriceList = null;
            Map<Integer, Long> indexMerchandiseIdMap = new HashMap<Integer, Long>();
            if (CollectionUtils.isNotEmpty(merchandises)) {
                merchandiseNames = new ArrayList<String>(merchandises.size()*2);
                numberPriceList = new ArrayList<String>(merchandises.size()*2);
                for (int i = 0; i < merchandises.size(); i++) {
                    Merchandise merchandise = merchandises.get(i);
                    merchandiseNames.add(merchandise.getMerchandiseName());
                    //判断单价有没有小数位
                    if(merchandise.getUnitPrice().doubleValue() > merchandise.getUnitPrice().intValue()){
                        numberPriceList.add(merchandise.getUnitPrice()+"元/"+merchandise.getUnit());
                    }else {
                        numberPriceList.add(merchandise.getUnitPrice().intValue()+"元/"+merchandise.getUnit());
                    }
                    indexMerchandiseIdMap.put(i, merchandise.getId());

                }
            }
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                FinalOrderExcel finalOrderExcel = new FinalOrderExcel();

                finalOrderExcel.setManagerName(order.getManagerName());
                BigDecimal sumUpstaircosts = BigDecimal.ZERO;

                if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
                    sumUpstaircosts = deliveryOrderService.sumUpstaircosts(order.getId());
                    finalOrderExcel.setUpstaircosts(sumUpstaircosts);
                }
                if (CategoryUtil.ORDERTYPE.TOW.equals(order.getOrderType())) {
                    BigDecimal totalShippingcosts =
                            deliveryOrderService.sumByOrderId(order.getId()).multiply(new BigDecimal("2"));
                    finalOrderExcel.setShippingcosts(totalShippingcosts);
                    finalOrderExcel.setAllPrice(order.getAllPrice().add(totalShippingcosts).add(sumUpstaircosts));
                }
                else {
                    finalOrderExcel.setAllPrice(order.getAllPrice().add(sumUpstaircosts));
                }

                finalOrderExcel.setPlaceOrderTime(new Date(order.getPlaceOrderTime()));
                finalOrderExcel.setReceivingAddress(order.getReceivingAddress());
                List<OrderInfo> orderInfos =
                        orderInfoService.findByOrderIdAndState(order.getId(), CategoryUtil.ORDERINFOSTATE.ONE);

                if (CollectionUtils.isNotEmpty(orderInfos)) {
                    List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
                    if (CollectionUtils.isNotEmpty(merchandises)) {
                        for (int i = 0; i < merchandises.size(); i++) {
                            Long merchandiseId = indexMerchandiseIdMap.get(i);
                            boolean isMathced = false;
                            for (OrderInfo orderInfo : orderInfos) {
                                if (orderInfo.getMerchandiseId() == merchandiseId) {
                                    orderInfoList.add(orderInfo);
                                    isMathced = true;
                                    break;
                                }
                            }
                            if (!isMathced) {
                                orderInfoList.add(null);
                            }
                        }
                        finalOrderExcel.setOrderInfos(orderInfoList);
                    }
                }

                finalOrderExcel.setRemarks(order.getRemarks());
                finalOrderExcels.add(finalOrderExcel);
            }

            try {
                ServletOutputStream out = response.getOutputStream();
                String fileName = "已完成订单列表excel" + DateUtil.getDateTimeFormat(new Date()) + ".xls";
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                //response.setContentType("application/binary;charset=ISO8859-1");
                response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                ExportFinalBoolExcel excel = new ExportFinalBoolExcel();
                List<String> headerList = new ArrayList<String>();
                headerList.add("下单日期");
                headerList.add("项目经理");
                headerList.add("地址");
                headerList.add("总金额");
                headerList.add("步楼上楼费");
                headerList.add("运费");
                headerList.addAll(merchandiseNames);
                headerList.add("备注");
                excel.exportExcel("已完成订单导出excel", headerList, numberPriceList, finalOrderExcels, out);
            }
            catch (IOException e) {

            }
        }
        return null;
    }

    /**
     * 导出对账订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/exportOrderCheckList", method = RequestMethod.POST)
    public ModelAndView exportOrderCheckList(HttpServletRequest request, HttpSession session,
                                             HttpServletResponse response) {
        Admin admin = (Admin)session.getAttribute("admin");
        Map<String, String> param = new HashMap<>();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            param.put(paraName, request.getParameter(paraName));
        }

        String checkStateInput = param.get("checkStateInput");
        if (checkStateInput != null && !checkStateInput.equals("")){
            param.put("checkState",param.get("checkStateInput"));
            param.remove("checkStateInput");
        }

        param.remove("page");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId", admin.getStoresId().toString());
        }
        else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId(), stores2.getStoresName()));
            }
        }
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList(), param, 1, maxSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            List<TheOrder> orderList = orderPage.getOrders();
            List<FinalOrderExcel> finalOrderExcels = new ArrayList<>();
            List<Merchandise> merchandises = merchandiseService.findByIsUse(1);
            List<String> merchandiseNames = null;
            List<String> numberPriceList = null;
            Map<Integer, Long> indexMerchandiseIdMap = new HashMap<Integer, Long>();
            if (CollectionUtils.isNotEmpty(merchandises)) {
                merchandiseNames = new ArrayList<String>(merchandises.size() * 2);
                numberPriceList = new ArrayList<String>(merchandises.size() * 2);
                for (int i = 0; i < merchandises.size(); i++) {
                    Merchandise merchandise = merchandises.get(i);
                    merchandiseNames.add(merchandise.getMerchandiseName());
                    //判断单价有没有小数位
                    if(merchandise.getUnitPrice().doubleValue() > merchandise.getUnitPrice().intValue()){
                        numberPriceList.add(merchandise.getUnitPrice()+"元/"+merchandise.getUnit());
                    }else {
                        numberPriceList.add(merchandise.getUnitPrice().intValue()+"元/"+merchandise.getUnit());
                    }
                    indexMerchandiseIdMap.put(i, merchandise.getId());
                }
            }
            System.out.println("订单条数:"+orderList.size());
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                FinalOrderExcel finalOrderExcel = new FinalOrderExcel();
                finalOrderExcel.setManagerName(order.getManagerName());
                BigDecimal sumUpstaircosts = BigDecimal.ZERO;
                if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
                    sumUpstaircosts = deliveryOrderService.sumUpstaircosts(order.getId());
                    finalOrderExcel.setUpstaircosts(sumUpstaircosts);
                }
                if (CategoryUtil.ORDERTYPE.TOW.equals(order.getOrderType())) {
                    BigDecimal totalShippingcosts =
                            deliveryOrderService.sumByOrderId(order.getId()).multiply(new BigDecimal("2"));
                    finalOrderExcel.setShippingcosts(totalShippingcosts);
                    finalOrderExcel.setAllPrice(order.getAllPrice().add(totalShippingcosts).add(sumUpstaircosts));
                }
                else {
                    finalOrderExcel.setAllPrice(order.getAllPrice().add(sumUpstaircosts));
                }
                finalOrderExcel.setPlaceOrderTime(new Date(order.getPlaceOrderTime()));
                finalOrderExcel.setReceivingAddress(order.getReceivingAddress());
                List<OrderInfo> orderInfos =
                        orderInfoService.findByOrderIdAndState(order.getId(), CategoryUtil.ORDERINFOSTATE.ONE);
                if (CollectionUtils.isNotEmpty(orderInfos)) {
                    List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
                    if (CollectionUtils.isNotEmpty(merchandises)) {
                        for (int i = 0; i < merchandises.size(); i++) {
                            Long merchandiseId = indexMerchandiseIdMap.get(i);
                            boolean isMathced = false;
                            for (OrderInfo orderInfo : orderInfos) {
                                if (orderInfo.getMerchandiseId() == merchandiseId) {
                                    orderInfoList.add(orderInfo);
                                    isMathced = true;
                                    break;
                                }
                            }
                            if (!isMathced) {
                                orderInfoList.add(null);
                            }
                        }

                        finalOrderExcel.setOrderInfos(orderInfoList);
                    }
                }

                finalOrderExcel.setRemarks(order.getRemarks());
                finalOrderExcels.add(finalOrderExcel);
            }

            try {
                ServletOutputStream out = response.getOutputStream();
                String fileName = "对账订单" + DateUtil.getDateTimeFormat(new Date()) + ".xls";
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentType("application/binary;charset=ISO8859-1");
                // response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                ExportFinalBoolExcel excel = new ExportFinalBoolExcel();
                List<String> headerList = new ArrayList<String>();
                headerList.add("下单日期");
                headerList.add("项目经理");
                headerList.add("地址");
                headerList.add("总金额");
                headerList.add("步梯上楼费");
                headerList.add("运费");
                headerList.addAll(merchandiseNames);
                headerList.add("备注");
                excel.exportExcel("已完成订单导出excel", headerList, numberPriceList, finalOrderExcels, out);
            }
            catch (IOException e) {

            }
        }
        return null;
    }

    /**
     * 对账订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/orderListCheck", method = RequestMethod.POST)
    public ModelAndView orderListCheck(@RequestParam Map<String, String> param , HttpSession session) {

        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        String checkStateInput = param.get("checkStateInput");
        if (checkStateInput != null && !checkStateInput.equals("")){
            param.put("checkState",param.get("checkStateInput"));
            param.remove("checkStateInput");
        }
        param.remove("total");
        param.remove("page");
        param.put("type" , "3");

        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        List<TheOrder> orderList = new ArrayList<>();
        long total = 0;
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList() , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join("," , deliveryNames));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/orderListCheck");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , checkList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkStateInput" ,checkStateInput);
        modelAndView.addObject("checkStateList" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) for (String key : param.keySet()) {
            if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
        }
        modelAndView.addAllObjects(map);

        return modelAndView;
    }

    /**
     * 客户财务对账订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/custFinanOrderListCheck", method = RequestMethod.POST)
    public ModelAndView custFinanOrderListCheck(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("total");
        param.remove("page");
        param.put("type" , "5");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        List<TheOrder> orderList = new ArrayList<>();
        long total = 0;
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList() , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join("," , deliveryNames));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/custFinanOrderListCheck");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , checkList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) for (String key : param.keySet()) {
            if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
        }

        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * 客户材料部对账订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/custMaterialsOrderListCheck", method = RequestMethod.POST)
    public ModelAndView custMaterialsOrderListCheck(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("total");
        param.remove("page");
        param.put("type" , "4");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        List<TheOrder> orderList = new ArrayList<>();
        long total = 0;
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList() , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join("," , deliveryNames));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/custMaterialsOrderListCheck");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , checkList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) {
            for (String key : param.keySet()) {
                if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
            }
        }
        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * 司机对账订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/driverOrderListCheck", method = RequestMethod.POST)
    public ModelAndView driverOrderListCheck(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("total");
        param.remove("page");
        param.put("type" , "3");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        List<TheOrder> orderList = new ArrayList<>();
        long total = 0;
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList() , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join("," , deliveryNames));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/driverOrderListCheck");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , checkList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) for (String key : param.keySet()) {
            if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
        }

        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * @param page
     * @param orderId
     * @return
     */
    @RequestMapping (value = "/orderEdit", method = RequestMethod.GET)
    public ModelAndView orderEdit(Integer page , Long orderId , String orderName) {
        ModelAndView modelAndView = new ModelAndView("/order/orderEdit");
        modelAndView.addObject("orderId" , orderId);
        modelAndView.addObject("page" , page);
        if (orderId != 0) {
            modelAndView.addObject("orderName" , orderName);
        }

        return modelAndView;
    }

    /**
     * @param order
     * @return
     */
    @RequestMapping (value = "/orderSave", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData saveTheOrder(TheOrder order) {
        JsonResultData result = JsonResultData.success();
        if (null == order.getId() || 0 == order.getId()) {
            order.setId(null);
            orderService.save(order);
        } else {
            orderService.update(order);
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 审核通过
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/examine", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData examine(Long orderId , HttpSession session) {
        JsonResultData result = JsonResultData.success();

        try {
            TheOrder order = orderService.findById(orderId);
            if (order.getState() != CategoryUtil.ORDERSTATUS.ONE) {
                return result.turnError("此订单的状态不是未审核，不能审核！！");
            }
            order.setState(CategoryUtil.ORDERSTATUS.TOW);
            orderService.update(order);
            Admin admin = (Admin) session.getAttribute("admin");
            if (admin.getRoleId() == 2L)
                if (orderService.countByStateAndstoresId(CategoryUtil.ORDERSTATUS.ONE , admin.getStoresId()) == 0)
                    result.addObject("audio" , 0);
        } catch (Exception e) {
            result.turnError("审核失败");
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 审核失败并删除
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData delete(Long orderId , HttpSession session) {
        JsonResultData result = JsonResultData.success();

        try {
            TheOrder order = orderService.findById(orderId);
            if (order.getState() != CategoryUtil.ORDERSTATUS.ONE) {
                return result.turnError("此订单的状态不是未审核，不能审核！！");
            }
            orderService.delete(order);
            Admin admin = (Admin) session.getAttribute("admin");
            if (admin.getRoleId() == 2L)
                if (orderService.countByStateAndstoresId(CategoryUtil.ORDERSTATUS.ONE , admin.getStoresId()) == 0)
                    result.addObject("audio" , 0);
        } catch (Exception e) {

            result.turnError("审核失败");
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 异常已处理
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/abnormal", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData abnormal(Long orderId) {
        JsonResultData result = JsonResultData.success();

        try {
            TheOrder order = orderService.findById(orderId);
            if (order.getState() != CategoryUtil.ORDERSTATUS.ZERO) {
                return result.turnError("此订单的状态不是异常订单");
            }
            order.setState(CategoryUtil.ORDERSTATUS.SIX);
            orderService.update(order);
        } catch (Exception e) {

            result.turnError("审核失败");
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 订单分配页面
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/allocationPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView allocationPage(Long orderId , Integer page , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (null == admin) {
            return new ModelAndView("/template/errors/500" , new HashMap<String, Object>());
        }
        TheOrder order = orderService.findById(orderId);
        List<OrderInfo> orderInfos = orderInfoService.findByOrderIdAndState(orderId , CategoryUtil.ORDERINFOSTATE.ONE);
        BigDecimal totalAmount = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(orderInfos)) {
            for (OrderInfo orderInfo : orderInfos) {
                orderInfo.setMerchandise(merchandiseService.findById(orderInfo.getMerchandiseId()));
                if (null != orderInfo.getAllPrice()) {
                    totalAmount = totalAmount.add(orderInfo.getAllPrice());
                }
            }
        }
        List<DeliveryOrder> deliveryOrders = deliveryOrderService.findByOrderId(orderId);
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
        }

        List<User> users = userService.findCanUse();
        List<UserCheckbox> userCheckboxs = new ArrayList<>();
        for (User user : users) {
            UserCheckbox checkbox = new UserCheckbox(user.getId() , user.getUsername() , user.getTelephone() , 0);
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                if (deliveryOrder.getDeliveryId() == user.getId()) {
                    if (CategoryUtil.ORDERSTATUS.FIVE == deliveryOrder.getState() || CategoryUtil.ORDERSTATUS.SIX == deliveryOrder.getState()) {
                        checkbox.setState(2);
                    } else {
                        checkbox.setState(1);
                    }
                }
            }
            userCheckboxs.add(checkbox);
        }
        ModelAndView modelAndView = new ModelAndView("/order/orderAllocation");
        BigDecimal sumUpstaircosts = new BigDecimal(0);
        if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
            sumUpstaircosts = deliveryOrderService.sumUpstaircosts(orderId);
            List<UpstairsDetail> upstairsDetails = upstairsDetailService.findListByOrderId(orderId);
            modelAndView.addObject("upstairsDetails" , upstairsDetails);
            List<Img> imgs = imgService.findByOrderId(orderId , CategoryUtil.IMG_TYPE.UPSTAIRES.getType());
            if (CollectionUtils.isNotEmpty(imgs)) {
                for (Img img : imgs) {
                    img.setImg(ImageUploadUtil.getRealUrl(img.getImg()));
                }
            }
            modelAndView.addObject("upstairsImgs" , imgs);
        }
        if (order.getState() == CategoryUtil.ORDERSTATUS.SIX || order.getState() == CategoryUtil.ORDERSTATUS.SEVEN) {
            if (CategoryUtil.ORDERTYPE.TOW == order.getOrderType()) {
                BigDecimal totalShippingcosts = deliveryOrderService.sumByOrderId(orderId).multiply(new BigDecimal("2"));
                if (BigDecimal.ZERO.compareTo(totalShippingcosts) != 0) {
                    modelAndView.addObject("isShowShippingcosts" , true);
                }
                modelAndView.addObject("totalAmountAll" , totalAmount.add(totalShippingcosts).add(sumUpstaircosts));
                modelAndView.addObject("totalShippingcosts" , totalShippingcosts);
            } else if (CategoryUtil.ORDERTYPE.THREE == order.getOrderType()) {
                modelAndView.addObject("totalAmountAll" , orderService.totalAmount(orderId));
                modelAndView.addObject("totalShippingcosts" , deliveryOrderService.sumByOrderId(orderId));
            } else {
                modelAndView.addObject("totalAmountAll" , totalAmount.add(sumUpstaircosts));
            }
            if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
                modelAndView.addObject("totalUpstaircosts" , sumUpstaircosts);
            }
        } else {
            modelAndView.addObject("totalAmountAll" , totalAmount);
        }
        modelAndView.addObject("orderId" , orderId);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("order" , order);
        modelAndView.addObject("orderInfos" , orderInfos);
        modelAndView.addObject("deliveryOrders" , deliveryOrders);
        modelAndView.addObject("userCheckboxs" , userCheckboxs);
        modelAndView.addObject("totalAmount" , totalAmount);
        return modelAndView;
    }

    /**
     * @param orderId
     * @return
     * @description 司机查看的订单页面
     */
    @RequestMapping (value = "/allocationPageForDriver", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView allocationPageForDriver(Long orderId , Integer page , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (null == admin) {
            return new ModelAndView("/template/errors/500" , new HashMap<String, Object>());
        }
        TheOrder order = orderService.findById(orderId);
        List<OrderInfo> orderInfos = orderInfoService.findByOrderIdAndState(orderId , CategoryUtil.ORDERINFOSTATE.ONE);
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderInfo orderInfo : orderInfos) {
            orderInfo.setMerchandise(merchandiseService.findById(orderInfo.getMerchandiseId()));
            if (null != orderInfo.getAllPrice()) {
                totalAmount = totalAmount.add(orderInfo.getAllPrice());
            }
        }

        List<DeliveryOrder> deliveryOrders = deliveryOrderService.findByOrderId(orderId);
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
        }

        List<User> users = userService.findCanUse();
        List<UserCheckbox> userCheckboxs = new ArrayList<>();
        for (User user : users) {
            UserCheckbox checkbox = new UserCheckbox(user.getId() , user.getUsername() , user.getTelephone() , 0);
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                if (deliveryOrder.getDeliveryId() == user.getId()) {
                    if (CategoryUtil.ORDERSTATUS.FIVE == deliveryOrder.getState() || CategoryUtil.ORDERSTATUS.SIX == deliveryOrder.getState())
                        checkbox.setState(2);
                    else checkbox.setState(1);
                }
            }
            userCheckboxs.add(checkbox);
        }
        ModelAndView modelAndView = new ModelAndView("/order/orderAllocationForDriver");
        if (order.getState() == CategoryUtil.ORDERSTATUS.SIX || order.getState() == CategoryUtil.ORDERSTATUS.SEVEN) {
            if (CategoryUtil.ORDERTYPE.TOW == order.getOrderType()) {
                modelAndView.addObject("totalAmountAll" , orderService.totalAmount(orderId).multiply(new BigDecimal(-1)));
                modelAndView.addObject("totalShippingcosts" , deliveryOrderService.sumByOrderId(orderId).multiply(new BigDecimal("2")));
            } else if (CategoryUtil.ORDERTYPE.THREE == order.getOrderType()) {
                modelAndView.addObject("totalAmountAll" , orderService.totalAmountForDriver(orderId));
                modelAndView.addObject("totalShippingcosts" , deliveryOrderService.sumByOrderId(orderId));
            } else {
                modelAndView.addObject("totalAmountAll" , orderService.totalAmountForDriver(orderId));
            }
            if (CategoryUtil.WHETHER.YES == order.getUpstairs())
                modelAndView.addObject("totalUpstaircosts" , deliveryOrderService.sumUpstaircosts(orderId));
        }
        modelAndView.addObject("orderId" , orderId);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("order" , order);
        modelAndView.addObject("orderInfos" , orderInfos);
        modelAndView.addObject("deliveryOrders" , deliveryOrders);
        modelAndView.addObject("userCheckboxs" , userCheckboxs);
        modelAndView.addObject("totalAmount" , totalAmount);
        return modelAndView;
    }

    /**
     * @param orderId
     * @return
     * @description 图片查询
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/images", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultData images(Long deliveryOrderId) {
        JsonResultData result = JsonResultData.success();
        try {
            DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
            List<String> images = new ArrayList<>();
            if (StringUtil.isNotEmpty(deliveryOrder.getImg())) {
                String[] imgs = deliveryOrder.getImg().split(",");
                for (String img : imgs) {
                    images.add(ImageUploadUtil.getRealUrl(img));
                }
            }
            result.addObject("images" , images);
        } catch (Exception e) {
            return result.turnError("查询图片失败:" + e.getMessage());
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 配送司机保存
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/deliveryOrderSave", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData deliveryOrderSave(Long orderId , Long deliveryId , String orderInfoStr ,
                                            Integer isShippingcosts) {
        JsonResultData result = JsonResultData.success();

        try {
            DeliveryOrder deliveryOrder = new DeliveryOrder();
            deliveryOrderService.save(orderId , deliveryId , orderInfoStr , deliveryOrder , isShippingcosts);
            result.addObject("deliveryOrderId" , deliveryOrder.getId());
        } catch (Exception e) {
            return result.turnError("保存失败:" + e.getMessage());
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 配送司机删除
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/deliveryOrderDel", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData deliveryOrderDel(Long deliveryOrderId) {
        JsonResultData result = JsonResultData.success();

        try {
            DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
            if (deliveryOrder.getState() == CategoryUtil.ORDERSTATUS.FIVE) {
                return result.turnError("该司机已出库无法删除");
            } else if (deliveryOrder.getState() == CategoryUtil.ORDERSTATUS.ELEVEN) {
                return result.turnError("该司机已送达无法删除");
            }
            deliveryOrderService.delete(deliveryOrder);
        } catch (Exception e) {
            return result.turnError("删除失败");
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 可用司机列表
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/deliveryList", method = RequestMethod.GET)
    @ResponseBody
    public JsonResultData deliveryList() {
        JsonResultData result = JsonResultData.success();
        List<User> users = userService.findCanUse();
        List<UserCheckbox> userCheckboxs = new ArrayList<>();
        for (User user : users) {
            UserCheckbox checkbox = new UserCheckbox(user.getId() , user.getUsername() , user.getTelephone() , 0);
            userCheckboxs.add(checkbox);
        }
        result.addObject("userCheckboxs" , userCheckboxs);

        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 提交对账
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/checkSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData checkSubmit(Long orderId) {
        JsonResultData result = JsonResultData.success();
        TheOrder order = orderService.findById(orderId);
        if (order.getState() != CategoryUtil.ORDERSTATUS.SEVEN) {
            return result.turnError("此订单的还未完成，不能提交对账！！");
        } else if (order.getCheckState() != CategoryUtil.CHECKSTATUS.ZERO) {
            return result.turnError("此订单已提交对账！！");
        }
        order.setCheckState(CategoryUtil.CHECKSTATUS.ONE);
        order.setReasonOne(null);
        orderService.update(order);
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 批量修改
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/checkSubmits", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData checkSubmits(String orderIdStr) {
        JsonResultData result = JsonResultData.success();
        String[] orderIds = {};
        if (StringUtil.isNotEmpty(orderIdStr)) {
            orderIds = orderIdStr.split(",");
        }
        for (String orderId : orderIds) {
            try {
                TheOrder order = orderService.findById(Long.parseLong(orderId));
                if (order.getState() == CategoryUtil.ORDERSTATUS.SEVEN) {
                    if (CategoryUtil.CHECKSTATUS.ZERO == order.getCheckState()) {
                        order.setCheckState(CategoryUtil.CHECKSTATUS.ONE);
                        order.setReasonOne(null);
                    } else if (CategoryUtil.CHECKSTATUS.ONE == order.getCheckState()) {
                        order.setCheckState(CategoryUtil.CHECKSTATUS.TOW);
                        order.setReasonTow(null);
                    } else if (CategoryUtil.CHECKSTATUS.TOW == order.getCheckState())
                        order.setCheckState(CategoryUtil.CHECKSTATUS.THREE);
                    orderService.update(order);
                }
            } catch (NumberFormatException e) {

            }
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 批量驳回
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/batchReject", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData batchReject(String orderIdStr , String reason) {
        JsonResultData result = JsonResultData.success();
        try {
            String[] orderIds = {};
            if (StringUtil.isNotEmpty(orderIdStr)) {
                orderIds = orderIdStr.split(",");
            }
            for (String orderId : orderIds) {
                TheOrder order = orderService.findById(Long.parseLong(orderId));
                if (order.getState() == CategoryUtil.ORDERSTATUS.SEVEN) {
                    if (CategoryUtil.CHECKSTATUS.ONE == order.getCheckState()) {
                        order.setCheckState(CategoryUtil.CHECKSTATUS.ZERO);
                        order.setReasonOne(reason);
                    } else if (CategoryUtil.CHECKSTATUS.TOW == order.getCheckState()) {
                        order.setCheckState(CategoryUtil.CHECKSTATUS.ONE);
                        order.setReasonTow(reason);
                    }
                    orderService.update(order);
                }
            }
        } catch (NumberFormatException e) {

        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 客户材料部审核成功
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/checkExamine", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData checkExamine(Long orderId) {
        JsonResultData result = JsonResultData.success();
        TheOrder order = orderService.findById(orderId);
        if (order.getState() != CategoryUtil.ORDERSTATUS.SEVEN) {
            return result.turnError("此订单的还未完成，不能提交对账！！");
        } else if (order.getCheckState() != CategoryUtil.CHECKSTATUS.ONE) {
            return result.turnError("此订单状态错误！！");
        }
        order.setCheckState(CategoryUtil.CHECKSTATUS.TOW);
        order.setReasonTow(null);
        orderService.update(order);
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 客户财务部审核成功
     * @author ZhongHan
     * @date 2017年11月28日
     */
    @RequestMapping (value = "/check", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData check(Long orderId) {
        JsonResultData result = JsonResultData.success();
        TheOrder order = orderService.findById(orderId);
        if (order.getState() != CategoryUtil.ORDERSTATUS.SEVEN) {
            return result.turnError("此订单的还未完成，不能提交对账！！");
        } else if (order.getCheckState() != CategoryUtil.CHECKSTATUS.TOW) {
            return result.turnError("此订单状态错误！！");
        }
        order.setCheckState(CategoryUtil.CHECKSTATUS.THREE);
        orderService.update(order);
        return result;
    }


    /**
     * @param page
     * @param orderId
     * @return
     */
    @RequestMapping (value = "/orderDetail", method = RequestMethod.GET)
    public ModelAndView orderDetail(Long deliveryOrderId,Long orderId) {
        ModelAndView modelAndView = new ModelAndView("/order/orderDetail");
        List<OrderInfo> orderInfos;
        //如果分配了司机 则根据司机ID息查订单详情 否则根据订单ID查
        if(deliveryOrderId<0){
            orderInfos = orderInfoService.findByOrderIdAndState(orderId,1);
            modelAndView.addObject("deliveryName" , "");
        }else{
            orderInfos = orderInfoService.findByDeliveryOrderId(deliveryOrderId);
            DeliveryOrder deliveryOrder = deliveryOrderService.findById(deliveryOrderId);
            modelAndView.addObject("deliveryName" , deliveryOrder.getDeliveryName());
        }

        TheOrder order = orderService.findById(orderId);
        for (OrderInfo orderInfo : orderInfos) {
            orderInfo.setMerchandise(merchandiseService.findById(orderInfo.getMerchandiseId()));
        }
        User user = userService.findById(order.getManagerId());
        String number = user.getStoresName() + DateUtil.getDateFormat(new Date(order.getPlaceOrderTime()) , "yyyyMMdd") + 00 + deliveryOrderService.countByOrderIdAndId(order.getId() , deliveryOrderId);
        modelAndView.addObject("order" , order);
        modelAndView.addObject("orderInfos" , orderInfos);
        modelAndView.addObject("number" , number);
        return modelAndView;
    }

    /**
     * @param orderId
     * @param remarks
     * @return
     * @description 方法描述
     * @author ljh
     * @date 2018年7月22日
     */
    @RequestMapping (value = "/orderRemark", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData orderRemark(Long orderId , HttpSession session , String remarks) {
        JsonResultData result = JsonResultData.success();
        try {
            TheOrder order = orderService.findById(orderId);
            if (order.getCheckState() != CategoryUtil.CHECKSTATUS.ZERO) {
                return result.turnError("此订单状态错误！！");
            }
            order.setRemarks(remarks);
            orderService.update(order);
        } catch (Exception e) {
            result.turnError("审核失败");
        }
        return result;
    }

    /**
     * 订单详情列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/orderInfoList")
    public ModelAndView orderInfoList(Long deliveryOrderId , HttpSession session) {
        List<OrderInfo> orderInfoList = orderInfoService.findByDeliveryOrderId(deliveryOrderId);
        long total = orderInfoList.size();
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : total / pageSize + 1);
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/deliveryOrder/orderInfoList");
        modelAndView.addObject("orderInfoList" , orderInfoList);
        modelAndView.addObject("deliveryOrderId" , deliveryOrderId);
        modelAndView.addObject("total" , total);
        return modelAndView;
    }

    /**
     * 订单详情列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/updateNumber")
    @ResponseBody
    public JsonResultData updateNumber(Long orderInfoId , BigDecimal number) {
        JsonResultData result = JsonResultData.success();
        OrderInfo orderInfo = orderInfoService.findById(orderInfoId);
        if (orderInfo.getState() == CategoryUtil.ORDERINFOSTATE.TOW && orderInfo.getNumber().compareTo(number) != 0) {
            orderInfoService.updateNumber(orderInfo , number);
        }
        return result;
    }

    /**
     * 计算订单总金额
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/totalAmounts")
    @ResponseBody
    public JsonResultData totalAmounts(String orderIdStr) {
        JsonResultData result = JsonResultData.success();
        String[] orderIds = orderIdStr.split(",");
        try {
            for (String orderId : orderIds) {
                Long.parseLong(orderId);
            }
        } catch (NumberFormatException e) {
            return result.turnError("参数错误");
        }

        if (StringUtil.isNotEmpty(orderIdStr)) result.addObject("totalAmounts" , orderService.totalAmounts(orderIdStr));
        else result.addObject("totalAmounts" , 0);

        return result;
    }

    /**
     * 客户材料部新增的订单列表
     * @param page
     * @return
     */
    @RequestMapping (value = "/custOrderList", method = RequestMethod.POST)
    public ModelAndView custOrderList(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("page");
        param.put("state" , CategoryUtil.ORDERSTATUS.ONE.toString());
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
            param.put("storesName" , admin.getStoresName());
        }
        long total = 0;
        List<TheOrder> orderList = new ArrayList<>();
        Set<Integer> stateList = new HashSet<Integer>();
        stateList.add(1);
        OrderPageDTO orderPage = orderService.findBy(stateList , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByIdAndStoresId(CategoryUtil.DEPARTMENTID.ONE , admin.getStoresId());
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/custOrderList");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) {
            for (String key : param.keySet()) {
                if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
            }
        }
        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * 客户材料部新增订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/custOrderAdd", method = RequestMethod.POST)
    public ModelAndView custOrderAdd(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        List<User> users = userService.findByIdAndStoresId(CategoryUtil.DEPARTMENTID.ONE , admin.getStoresId());
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }
        Long storesId = admin.getStoresId();
        List<Merchandise> merchandiseList = merchandiseService.findByIsUse(CategoryUtil.WHETHER.YES , storesId);
        for (Merchandise merchandise : merchandiseList) {
            merchandise.setImg(ImageUploadUtil.getRealUrl(merchandise.getImg()));
        }
        ModelAndView modelAndView = new ModelAndView("/order/custOrderAdd");
        modelAndView.addObject("merchandiseList" , merchandiseList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("stateList" , selectList);
        modelAndView.addObject("checkState" , admin.getCheckList());
        if (null != admin.getStoresId()) {
            modelAndView.addObject("storesId" , admin.getStoresId().toString());
            modelAndView.addObject("storesName" , admin.getStoresName());
        }
        return modelAndView;
    }

    /**
     * 提交订单
     * @return
     */
    @RequestMapping (value = "/orderAdd", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData orderAdd(String orderInfoStr , String receivingAddress , String upstairs , Long managerId ,
                                   Integer orderType , HttpSession session) {
        JsonResultData resultData = JsonResultData.success();
        Admin admin = (Admin) session.getAttribute("admin");
        TheOrder order = new TheOrder();
        if (null != admin) {
            order.setStoresId(admin.getStoresId());
            order.setStoresName(admin.getStoresName());
            order.setCreatorId(admin.getAdminId());
            order.setModifierId(admin.getAdminId());
        }
        order.setPlaceOrderTime(System.currentTimeMillis());
        User user = userService.findById(managerId);
        if (null != user) {
            order.setManagerId(user.getId());
            order.setManagerName(user.getUsername());
            order.setManagerTel(user.getTelephone());
            order.setStoresId(user.getStoresId());
            order.setStoresName(user.getStoresName());
        }
        order.setReceivingAddress(receivingAddress);
        order.setIsPay(CategoryUtil.WHETHER.NO);
        order.setOrderType(orderType);
        order.setState(CategoryUtil.ORDERSTATUS.ONE);
        order.setCreateTime(System.currentTimeMillis());
        order.setModificationTime(System.currentTimeMillis());
        order.setCheckState(CategoryUtil.CHECKSTATUS.ZERO);
        if (StringUtil.isNotEmpty(upstairs) && CategoryUtil.WHETHER.YES.toString().equals(upstairs)) {
            order.setUpstairs(CategoryUtil.WHETHER.YES);
        } else {
            order.setUpstairs(CategoryUtil.WHETHER.NO);
        }
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
            BigDecimal orderInfoAllPrice = new BigDecimal("0");
            if (CategoryUtil.ORDERTYPE.THREE == orderType) {
                orderInfoAllPrice = merchandise.getShippingCost().multiply(number);
                orderInfo.setAllPrice(orderInfoAllPrice);
            } else if (CategoryUtil.ORDERTYPE.TOW == orderType) {
                orderInfoAllPrice = merchandise.getUnitPrice().multiply(number).multiply(new BigDecimal(-1));
                orderInfo.setAllPrice(orderInfoAllPrice);
            } else {
                orderInfo.setAllPrice(merchandise.getUnitPrice().multiply(number));
            }
            orderInfo.setState(CategoryUtil.ORDERINFOSTATE.ONE);
            orderInfos.add(orderInfo);
            allPrice = allPrice.add(orderInfo.getAllPrice());
        }
        order.setAllPrice(allPrice);
        orderService.save(order , orderInfos);
        return resultData;
    }

    /**
     * 步梯上楼需要文员订单增加上楼详情的订单列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/upstairOrderList", method = RequestMethod.POST)
    public ModelAndView upstairOrderList(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("page");
        param.put("state" , CategoryUtil.ORDERSTATUS.ELEVEN.toString());
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
            param.put("storesName" , admin.getStoresName());
        }
        long total = 0;
        List<TheOrder> orderList = new ArrayList<>();
        Set<Integer> stateList = new HashSet<Integer>();
        stateList.add(11);
        OrderPageDTO orderPage = orderService.findBy(stateList , param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByIdAndStoresId(CategoryUtil.DEPARTMENTID.ONE , admin.getStoresId());
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/upstairOrderList");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());
        modelAndView.addObject("adminId" , admin.getAdminId());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) {
            for (String key : param.keySet()) {
                if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
            }
        }
        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * 进入填写上楼详情页面
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/upstairOrderDetailAdd", method = RequestMethod.POST)
    public ModelAndView upstairOrderDetailAdd(Long orderId , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        List<UpstairsDetail> upstairsDetailList = upstairsDetailService.findByOrderId(orderId);
        ModelAndView modelAndView = new ModelAndView("/order/upstairOrderDetailAdd");
        modelAndView.addObject("upstairsDetailList" , upstairsDetailList);
        modelAndView.addObject("stateList" , selectList);
        modelAndView.addObject("adminId" , admin.getAdminId());
        return modelAndView;
    }

    /**
     * @param orderId
     * @return
     * @description 保存上楼详情
     * @author ljh
     * @date 2018年9月20日
     */
    @RequestMapping (value = "/upstairDetailSave", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData upstairDetailSave(
            @RequestParam (value = "upstairDetailsStr", required = false) String upstairDetailsStr , Long adminId ,
            Long orderId) {
        JsonResultData result = JsonResultData.success();
        try {
            upstairsDetailService.save(upstairDetailsStr , adminId , orderId);
        } catch (Exception e) {
            return result.turnError("保存上楼详情失败:" + e.getMessage());
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 保存上楼详情
     * @author ljh
     * @date 2018年9月20日
     */
    @RequestMapping (value = "/upstairImgUpload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData upstairImgUpload(Long adminId , Long orderId ,
                                           @RequestParam (value = "files", required = false) CommonsMultipartFile files[]) {
        JsonResultData result = JsonResultData.success();
        List<String> imgList = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                if (file.getSize() > 0) {
                    if (ImageUploadUtil.checkImageFile(file)) {
                        return result.turnError("图片格式必须是png、jpg，且图片大小不能超过2M");
                    }
                    try {
                        imgList.add(ImageUploadUtil.imgUpload(file));
                    } catch (Exception e) {
                        return result.turnError("图片上传失败，请稍后再试");
                    }
                }
            }
        }

        try {
            String imgListStr = String.join("," , imgList);
            if (StringUtils.isNotBlank(imgListStr) && null != imgListStr.split(",")) {
                String[] imgArr = imgListStr.split(",");
                for (String imgStr : imgArr) {
                    Img img = new Img();
                    img.setCreatorId(adminId);
                    img.setCreateTime(new Date().getTime());
                    img.setImg(imgStr);
                    img.setOrderId(orderId);
                    img.setType(CategoryUtil.IMG_TYPE.UPSTAIRES.getType());
                    imgService.save(img);
                }
            }
        } catch (Exception e) {
            return result.turnError("上传图片失败:" + e.getMessage());
        }
        return result;
    }

    /**
     * @param orderId
     * @return
     * @description 无上楼详情审批
     * @author ljh
     * @date 2018年9月20日
     */
    @RequestMapping (value = "/noUpstairDetailCheck", method = RequestMethod.POST)
    @ResponseBody
    public JsonResultData noUpstairDetailCheck(Long orderId , Long adminId) {
        JsonResultData result = JsonResultData.success();
        try {
            upstairsDetailService.saveNoUpstairDetailCheck(adminId , orderId);
        } catch (Exception e) {
            return result.turnError("审批失败:" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出步梯上楼详情对账订单
     * @param request
     * @param page
     * @return
     */
    @RequestMapping(value = "/exportUpstairsList", method = RequestMethod.POST)
    public ModelAndView exportUpstairsList(HttpServletRequest request, HttpSession session,
                                           HttpServletResponse response) {
        Admin admin = (Admin)session.getAttribute("admin");
        Map<String, String> param = new HashMap<>();
        Enumeration<String> enu = request.getParameterNames();
        while (enu.hasMoreElements()) {
            String paraName = enu.nextElement();
            param.put(paraName, request.getParameter(paraName));
        }

        param.remove("page");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId", admin.getStoresId().toString());
        }
        else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId(), stores2.getStoresName()));
            }
        }
        OrderPageDTO orderPage = orderService.findBy(admin.getStateList(), param, 1, maxSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            List<TheOrder> orderList = orderPage.getOrders();
            List<FinalOrderExcel> finalOrderExcels = new ArrayList<>();
            List<Merchandise> merchandises = merchandiseService.findByIsUse(1);
            List<String> merchandiseNames = null;
            List<String> numberPriceList = null;
            Map<Integer, Long> indexMerchandiseIdMap = new HashMap<Integer, Long>();
            if (CollectionUtils.isNotEmpty(merchandises)) {
                merchandiseNames = new ArrayList<String>(merchandises.size() * 2);
                numberPriceList = new ArrayList<String>(merchandises.size() * 2);
                for (int i = 0; i < merchandises.size(); i++) {
                    Merchandise merchandise = merchandises.get(i);
                    merchandiseNames.add(merchandise.getMerchandiseName());
                    //判断单价有没有小数位
                    if(merchandise.getUnitPrice().doubleValue() > merchandise.getUnitPrice().intValue()){
                        numberPriceList.add(merchandise.getUnitPrice()+"元/"+merchandise.getUnit());
                    }else {
                        numberPriceList.add(merchandise.getUnitPrice().intValue()+"元/"+merchandise.getUnit());
                    }
                    indexMerchandiseIdMap.put(i, merchandise.getId());
                }
            }
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join(",", deliveryNames));
                FinalOrderExcel finalOrderExcel = new FinalOrderExcel();
                finalOrderExcel.setManagerName(order.getManagerName());
                finalOrderExcel.setAllPrice(order.getAllPrice());
                finalOrderExcel.setPlaceOrderTime(new Date(order.getPlaceOrderTime()));
                finalOrderExcel.setReceivingAddress(order.getReceivingAddress());
                List<OrderInfo> orderInfos =
                        orderInfoService.findByOrderIdAndState(order.getId(), CategoryUtil.ORDERINFOSTATE.ONE);
                if (CollectionUtils.isNotEmpty(orderInfos)) {
                    List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
                    if (CollectionUtils.isNotEmpty(merchandises)) {
                        for (int i = 0; i < merchandises.size(); i++) {
                            Long merchandiseId = indexMerchandiseIdMap.get(i);
                            boolean isMathced = false;
                            for (OrderInfo orderInfo : orderInfos) {
                                if (orderInfo.getMerchandiseId() == merchandiseId) {
                                    orderInfoList.add(orderInfo);
                                    isMathced = true;
                                    break;
                                }
                            }
                            if (!isMathced) {
                                orderInfoList.add(null);
                            }
                        }

                        finalOrderExcel.setOrderInfos(orderInfoList);
                    }
                }

                finalOrderExcel.setRemarks(order.getRemarks());
                finalOrderExcels.add(finalOrderExcel);
            }
            try {
                ServletOutputStream out = response.getOutputStream();
                String fileName = "对账订单excel" + DateUtil.getDateTimeFormat(new Date()) + ".xls";
                fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
//                response.setContentType("application/binary;charset=ISO8859-1");
                 response.setContentType("application/vnd.ms-excel;charset=UTF-8");
                response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
                ExportFinalBoolExcel excel = new ExportFinalBoolExcel();
                List<String> headerList = new ArrayList<String>();
                headerList.add("下单日期");
                headerList.add("项目经理");
                headerList.add("地址");
                headerList.add("总金额");
                headerList.add("步梯上楼费");
                headerList.add("运费");
                headerList.addAll(merchandiseNames);
                headerList.add("备注");
                excel.exportExcel("对账订单excel", headerList, numberPriceList, finalOrderExcels, out);
            }
            catch (IOException e) {

            }
        }
        return null;
    }

    /**
     * 订单列表
     * @param request
     * @param page
     * @return
     */
    @RequestMapping (value = "/queryOrderList", method = RequestMethod.POST)
    public ModelAndView queryOrderList(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("page");
        List<CommonSearch> storesList = new ArrayList<>();
        if (null != admin.getStoresId()) {
            param.put("storesId" , admin.getStoresId().toString());
        } else {
            List<Stores> stores = storesService.findAll();
            for (Stores stores2 : stores) {
                storesList.add(new CommonSearch(stores2.getId() , stores2.getStoresName()));
            }
        }
        long total = 0;
        List<TheOrder> orderList = new ArrayList<>();
        OrderPageDTO orderPage = orderService.findBy(param , page , pageSize);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                order.setDeliveryNames(String.join("," , deliveryNames));
            }
            total = orderPage.getTotal();
        }
        total = (total / pageSize == 0) ? (1) : (total % pageSize == 0 ? total / pageSize : (total / pageSize + 1));
        List<User> users = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.ONE);
        List<CommonSearch> searchs = new ArrayList<>();
        for (User user : users) {
            searchs.add(new CommonSearch(user.getId() , user.getUsername()));
        }
        List<User> deliverys = userService.findByDepartmentId(CategoryUtil.DEPARTMENTID.TOW);
        List<CommonSearch> deliveryList = new ArrayList<>();
        for (User user : deliverys) {
            deliveryList.add(new CommonSearch(user.getId() , user.getUsername()));
        }

        ModelAndView modelAndView = new ModelAndView("/order/queryOrderList");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("deliveryList" , deliveryList);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , selectList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("checkState" , admin.getCheckList());

        Map<String, String> map = new HashMap<>();
        if (!param.isEmpty()) for (String key : param.keySet()) {
            if (StringUtil.isNotEmpty(param.get(key))) map.put(key , param.get(key));
        }

        modelAndView.addAllObjects(map);
        return modelAndView;
    }

    /**
     * @param orderId
     * @return
     * @description 订单查询页面
     * @author ljh
     * @date 2018年10月23日
     */
    @RequestMapping (value = "/queryAllocationPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryAllocationPage(Long orderId , Integer page , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        if (null == admin) {
            return new ModelAndView("/template/errors/500" , new HashMap<String, Object>());
        }
        TheOrder order = orderService.findById(orderId);
        List<OrderInfo> orderInfos = orderInfoService.findByOrderIdAndState(orderId , CategoryUtil.ORDERINFOSTATE.ONE);
        BigDecimal totalAmount = new BigDecimal(0);
        if (CollectionUtils.isNotEmpty(orderInfos)) {
            for (OrderInfo orderInfo : orderInfos) {
                orderInfo.setMerchandise(merchandiseService.findById(orderInfo.getMerchandiseId()));
                if (null != orderInfo.getAllPrice()) {
                    totalAmount = totalAmount.add(orderInfo.getAllPrice());
                }
            }
        }
        List<DeliveryOrder> deliveryOrders = deliveryOrderService.findByOrderId(orderId);
        for (DeliveryOrder deliveryOrder : deliveryOrders) {
            deliveryOrder.setOrderInfos(orderInfoService.findByDeliveryOrderId(deliveryOrder.getId()));
        }

        List<User> users = userService.findCanUse();
        List<UserCheckbox> userCheckboxs = new ArrayList<>();
        for (User user : users) {
            UserCheckbox checkbox = new UserCheckbox(user.getId() , user.getUsername() , user.getTelephone() , 0);
            for (DeliveryOrder deliveryOrder : deliveryOrders) {
                if (deliveryOrder.getDeliveryId() == user.getId()) {
                    if (CategoryUtil.ORDERSTATUS.FIVE == deliveryOrder.getState() || CategoryUtil.ORDERSTATUS.SIX == deliveryOrder.getState()) {
                        checkbox.setState(2);
                    } else {
                        checkbox.setState(1);
                    }
                }
            }
            userCheckboxs.add(checkbox);
        }
        ModelAndView modelAndView = new ModelAndView("/order/queryOrderAllocation");
        BigDecimal sumUpstaircosts = new BigDecimal(0);
        if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
            sumUpstaircosts = deliveryOrderService.sumUpstaircosts(orderId);
            List<UpstairsDetail> upstairsDetails = upstairsDetailService.findListByOrderId(orderId);
            modelAndView.addObject("upstairsDetails" , upstairsDetails);
            List<Img> imgs = imgService.findByOrderId(orderId , CategoryUtil.IMG_TYPE.UPSTAIRES.getType());
            if (CollectionUtils.isNotEmpty(imgs)) {
                for (Img img : imgs) {
                    img.setImg(ImageUploadUtil.getRealUrl(img.getImg()));
                }
            }
            modelAndView.addObject("upstairsImgs" , imgs);
        }
        if (order.getState() == CategoryUtil.ORDERSTATUS.SIX || order.getState() == CategoryUtil.ORDERSTATUS.SEVEN) {
            if (CategoryUtil.ORDERTYPE.TOW == order.getOrderType()) {
                BigDecimal totalShippingcosts = deliveryOrderService.sumByOrderId(orderId).multiply(new BigDecimal("2"));
                if (BigDecimal.ZERO.compareTo(totalShippingcosts) != 0) {
                    modelAndView.addObject("isShowShippingcosts" , true);
                }
                modelAndView.addObject("totalAmountAll" , totalAmount.add(totalShippingcosts).add(sumUpstaircosts));
                modelAndView.addObject("totalShippingcosts" , totalShippingcosts);
            } else if (CategoryUtil.ORDERTYPE.THREE == order.getOrderType()) {
                modelAndView.addObject("totalAmountAll" , orderService.totalAmount(orderId));
                modelAndView.addObject("totalShippingcosts" , deliveryOrderService.sumByOrderId(orderId));
            } else {
                modelAndView.addObject("totalAmountAll" , totalAmount.add(sumUpstaircosts));
            }
            if (CategoryUtil.WHETHER.YES == order.getUpstairs()) {
                modelAndView.addObject("totalUpstaircosts" , sumUpstaircosts);
            }
        } else {
            modelAndView.addObject("totalAmountAll" , totalAmount);
        }
        modelAndView.addObject("orderId" , orderId);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("order" , order);
        modelAndView.addObject("orderInfos" , orderInfos);
        modelAndView.addObject("deliveryOrders" , deliveryOrders);
        modelAndView.addObject("userCheckboxs" , userCheckboxs);
        modelAndView.addObject("totalAmount" , totalAmount);
        return modelAndView;
    }
}
