package net.mxh.admin.main.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mxh.admin.main.bean.CommonSearch;
import net.mxh.admin.main.bean.CommonSelect;
import net.mxh.admin.main.bean.UserCheckbox;
import net.mxh.entity.*;
import net.mxh.service.*;
import net.mxh.util.*;
import net.mxh.vo.FinalOrderExcel;
import net.mxh.vo.OrderPageDTO;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * admin/statistics/statisticsList
 * 统计控制层
 * @author bigduu
 */
@SuppressWarnings ("Duplicates")
@Controller
@RequestMapping (value = "admin/statistics")
public class StatisticsController {
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
     * 这个变量是查询总数的懒加载flag
     * @param param
     * @param session
     * @return
     */
    private Map<String, String> paramFlag;

    private static Map<String, Integer> GCountMerchandise;


    /**
     * 数据统计页面
     * @param request
     * @param page
     * @retur
     */
    @RequestMapping (value = "/statisticsListFinal", method = RequestMethod.POST)
    public ModelAndView orderListFinal(@RequestParam Map<String, String> param , HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");

        Integer page = StringUtil.toInteger(param.get("page"));
        param.remove("page");
        List<CommonSearch> storesList = new ArrayList<>();
        //用于判断是否是客户
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

        //单独订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        if (null != orderPage && CollectionUtils.isNotEmpty(orderPage.getOrders())) {
            orderList = orderPage.getOrders();
            //设置每个order的属性
            for (TheOrder order : orderList) {
                order.setPlaceOrderTimeStr(DateUtil.getDateTimeFormat(new Date(order.getPlaceOrderTime())));
                List<String> deliveryNames = deliveryOrderService.findNameByOrderId(order.getId());
                if (CollectionUtils.isNotEmpty(deliveryNames)) {
                    order.setDeliveryNames(String.join("," , deliveryNames));
                }
                //////////////////////////// 获取金额 运费 材
                //获取订单的运费
                List<DeliveryOrder> byOrderId = deliveryOrderService.findByOrderId(order.getId());
                BigDecimal countShippingcosts = new BigDecimal(0);
                for (DeliveryOrder deliveryOrder : byOrderId) {
                    countShippingcosts = countShippingcosts.add(deliveryOrder.getShippingcosts());
                }
                order.setShippingcosts(countShippingcosts);

                //获取消耗类型
                //先查出来有哪些orderInfos
                List<OrderInfo> TMP_orderInfos = orderInfoService.findByOrderIdAndState(order.getId() , CategoryUtil.ORDERINFOSTATE.ONE);
                //将orderinfo的商品信息查出来 并重新组装起来
                List<OrderInfo> orderInfos = new ArrayList<>();
                for (OrderInfo orderInfo : TMP_orderInfos) {
                    orderInfo.setMerchandise(merchandiseService.findById(orderInfo.getMerchandiseId()));
                    orderInfos.add(orderInfo);
                }
                //释放内存
                TMP_orderInfos = null;
                order.setOrderInfos(orderInfos);
                ////////////////////////////////////////////////////
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
        /**
         * 如果传参和flag不一样则查询总数
         * 查询统计的总数
         * @author bigduu
         */
        Long count = null;
        BigDecimal allPrice = new BigDecimal(0);
        Map<String, Integer> countMerchandise = new HashMap<>();
        //粗略的判断下 还没想好怎么判断 先默认执行
        /**
         * 全部数据的懒加载
         */
        Boolean hasChange = false;
        if (paramFlag == null) {
            paramFlag = param;
        }
        //有一个不一样就查询
        for (String key : param.keySet()) {
            if (!param.get(key).equals(paramFlag.get(key))) {
                hasChange = true;
                break;
            }
        }
        if (param.size() == 0 || hasChange || param.get("storesId") != null || param.size() == 1) {
            paramFlag = param;
            //查询总共有多少条数据
            if (orderPage != null) {
                count = orderPage.getCount();
            }

            //查询符合查询条件的总金额
            long start = System.currentTimeMillis();
            OrderPageDTO countOrder = orderService.findBy(param , 1 , Math.toIntExact(count));
            List<TheOrder> orders = countOrder.getOrders();
            //标记3 此处处理param的空数据问题
            Boolean hasParams = false;
            if (param.size() != 0) {
                for (String key : param.keySet()) {
                    if (!param.get(key).equals("")) {
                        hasParams = true;
                        break;
                    }
                }
            }

            for (TheOrder theOrder : orders) {
                allPrice = allPrice.add(theOrder.getAllPrice());
                //标记1
                //因为全部查询的数据量过大,所以采用两种方法解决 如果搜索条件为0则单独查询所有数据
                if (hasParams) {
                    List<OrderInfo> TMP_orderInfos = orderInfoService.findByOrderIdAndState(theOrder.getId() , CategoryUtil.ORDERINFOSTATE.ONE);
                    //将orderinfo的商品信息查出来 并重新组装起来
                    List<OrderInfo> orderInfos = new ArrayList<>();
                    for (OrderInfo orderInfo : TMP_orderInfos) {

                        //如果状态为2的话则为退货
                        if (theOrder.getOrderType() == 2) {
                            //判断map里面是否已经存在这个key 如果不存在则加入 如果存在则数字相加
                            //因为这里为退货 所以数字是负数
                            if (!countMerchandise.containsKey(orderInfo.getMerchandiseName())) {
                                countMerchandise.put(orderInfo.getMerchandiseName() , orderInfo.getNumber().intValue() * -1);
                            } else {
                                Integer integer = countMerchandise.get(orderInfo.getMerchandiseName());
                                integer = integer - orderInfo.getNumber().intValue();
                                countMerchandise.put(orderInfo.getMerchandiseName() , integer);
                            }
                        } else {
                            if (!countMerchandise.containsKey(orderInfo.getMerchandiseName())) {
                                countMerchandise.put(orderInfo.getMerchandiseName() , orderInfo.getNumber().intValue());
                            } else {
                                Integer integer = countMerchandise.get(orderInfo.getMerchandiseName());
                                integer = integer + orderInfo.getNumber().intValue();
                                countMerchandise.put(orderInfo.getMerchandiseName() , integer);
                            }
                        }

                    }
                }
            }

            //标记2
            //传入参数为0时 则单独查询所有使用数据
            start = System.currentTimeMillis();
            Map<String, String> orderType = new HashMap<>();
            if (hasParams == false) {
                for (TheOrder theOrder : orders) {
                    orderType.put(theOrder.getId().toString() , theOrder.getOrderType().toString());
                }
                //查询物料的消耗总数
                List<OrderInfo> all = orderInfoService.findAll();
                for (OrderInfo orderInfo : all) {
                    //如果状态为2的话则为退货
                    String toString = orderInfo.getOrderId().toString();
                    if (orderType.get(toString) != null && orderType.get(toString).equals("2")) {
                        //判断map里面是否已经存在这个key 如果不存在则加入 如果存在则数字相加
                        //因为这里为退货 所以数字是负数
                        if (!countMerchandise.containsKey(orderInfo.getMerchandiseName())) {
                            countMerchandise.put(orderInfo.getMerchandiseName() , orderInfo.getNumber().intValue() * -1);
                        } else {
                            Integer integer = countMerchandise.get(orderInfo.getMerchandiseName());
                            integer = integer - orderInfo.getNumber().intValue();
                            countMerchandise.put(orderInfo.getMerchandiseName() , integer);
                        }
                    } else {
                        if (!countMerchandise.containsKey(orderInfo.getMerchandiseName())) {
                            countMerchandise.put(orderInfo.getMerchandiseName() , orderInfo.getNumber().intValue());
                        } else {
                            Integer integer = countMerchandise.get(orderInfo.getMerchandiseName());
                            integer = integer + orderInfo.getNumber().intValue();
                            countMerchandise.put(orderInfo.getMerchandiseName() , integer);
                        }
                    }

                }
            }
            //查询物料的消耗总数
            //为了节约资源在标记1和标记2 执行
            GCountMerchandise = countMerchandise;
        }
        List<Merchandise> merchandises = merchandiseService.findAll();


        ModelAndView modelAndView = new ModelAndView("/statistics/statisticsList");
        modelAndView.addObject("orderList" , orderList);
        modelAndView.addObject("users" , searchs);
        modelAndView.addObject("storesList" , storesList);
        modelAndView.addObject("stateList" , selectList);
        modelAndView.addObject("page" , page);
        modelAndView.addObject("totalAmount" , totalAmount);
        modelAndView.addObject("allPrice" , allPrice);
        modelAndView.addObject("countMerchandise" , GCountMerchandise);
        modelAndView.addObject("merchandises" , merchandises);

        modelAndView.addObject("admin" , admin);
        modelAndView.addObject("total" , total);
        modelAndView.addObject("count" , count);
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

}