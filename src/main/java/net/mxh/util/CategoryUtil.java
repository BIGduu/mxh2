package net.mxh.util;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;

import net.mxh.entity.DeliveryOrder;
import net.mxh.entity.Merchandise;
import net.mxh.entity.User;

/**
 * 常量
 * @description 类描述
 * @author ZhongHan
 * @date 2017年8月31日
 */
public interface CategoryUtil {
	
	public final static SerializerFeature[] features = {SerializerFeature.WriteMapNullValue,
		SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullListAsEmpty,
		SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.DisableCircularReferenceDetect};
	
	public final static String fileUrl = PropertiesUtil.getPropertiesKey("fileUrl");
	
	public final static String url = PropertiesUtil.getPropertiesKey("url");
	
	public static interface WHETHER {
		
		public static final Integer NO = 0; // 不可用
		
		public static final Integer YES = 1;// 可用
		
	}
	
	public static interface TYPE {
		
		public static final Integer ONE = 1; // 工程部
		
		public static final Integer TOW = 2;// 配送部
		
		public static final Integer THREE = 3;// 仓管
		
	}
	
	public static enum IMG_TYPE {
		UPSTAIRES(1);
		private Integer type;
		
		private IMG_TYPE(Integer type) {
			this.type = type;
		}
		
		public Integer getType() {
			return this.type;
		}
	}
	
	// menu menuLevel
	public static interface MENULEVEL {
		
		public static final Integer ONE = 1;
		
		public static final Integer TOW = 2;
		
	}
	
	// user status 状态：1、未审核；2、审核未通过3、审核通过；4、注销
	public static interface USERSTATUS {
		
		public static final Integer ONE = 1; // 未审核
		
		public static final Integer TOW = 2; // 审核未通过
		
		public static final Integer THREE = 3; // 审核通过
		
		public static final Integer FOUR = 4; // 注销
		
	}
	
	// sys_role ID：1、超级管理员；2、文员 3、仓管；4、财务;5、客户材料部管理员；6、客户财务部管理员
	public static interface ROLE_ID {
		
		public static final Integer ONE = 1; // 超级管理员
		
		public static final Integer TOW = 2; // 文员
		
		public static final Integer THREE = 3; // 仓管
		
		public static final Integer FOUR = 4; // 财务
		
		public static final Integer FIVE = 5; // 客户材料部管理员
		
		public static final Integer SIX = 6; // 客户财务部管理员
		
	}
	
	// theorder status ：1、未审核；2、已审核；3、已分配；4、装车中；5、已出库；6、已送达；7、已完成；0、异常
	public static interface ORDERSTATUS {
		
		public static final Integer ZERO = 0; // 异常
		
		public static final Integer ONE = 1; // 未审核待文员审核
		
		public static final Integer TOW = 2; // 已审核待文员分配
		
		public static final Integer THREE = 3; // 已分配待司机确认
		
		public static final Integer FOUR = 4; // 装车完成待仓管确认出库
		
		public static final Integer FIVE = 5; // 已出库待司机送达
		
		public static final Integer SIX = 6; // 已送达待客户确认（退货订单待仓库管理员确认）
		
		public static final Integer SEVEN = 7; // 已完成
		
		public static final Integer EIGHT = 8; // 仓库管理员确认入库
		
		public static final Integer NINE = 9; // 司机拒绝或应特殊情况无法运送
		
		public static final Integer TEN = 10; // 司机已确认装车中
		
		public static final Integer ELEVEN = 11; // 待文员处理步梯上楼费 （如果是步梯上楼，有上楼费，则配送完成时状态变成11，待文员新增上楼详情后，状态变成6）
		
		// public static final Integer TWELVE = 12; // 待项目经理确认步梯上楼运费和上楼费
		// (如果是步梯上楼，文员新增步梯上楼详情后，状态变成12，由项目经理在手机端确认，确认后状态变成已经完成，即等待对账)
	}
	
	// theorder checkStatus ： 0:未对账 1已提交对账 2材料部已审核 3财务部已核对
	public static interface CHECKSTATUS {
		
		public static final Integer ZERO = 0;
		
		public static final Integer ONE = 1;
		
		public static final Integer TOW = 2;
		
		public static final Integer THREE = 3;
		
	}
	
	// theorder orderType ：1、配货订单；2、退货订单；3、转货订单
	public static interface ORDERTYPE {
		
		public static final Integer ONE = 1; // 配货订单
		
		public static final Integer TOW = 2; // 退货订单
		
		public static final Integer THREE = 3; // 转货订单
		
	}
	
	// storage type ：1、进；2、出;3、损耗 4：退货
	public static interface STORAGETYPE {
		
		public static final Integer ONE = 1;
		
		public static final Integer TOW = 2;
		
		public static final Integer THREE = 3;
		
		public static final Integer FOUR = 4;
		
	}
	
	// storage type ：1、工程部；2、配送部
	public static interface DEPARTMENTID {
		
		public static final Long ONE = 1L;
		
		public static final Long TOW = 2L;
		
	}
	
	// orderInfo state ：1、原始订单货物；2、已分配订单货物
	public static interface ORDERINFOSTATE {
		
		public static final Integer ONE = 1;
		
		public static final Integer TOW = 2;
		
	}
	
	// 微信通知模板id
	public static interface TEMPLATEID {
		
		public static final String ZERO = "j-odVAIUHXTSjwPQoGv0h-bsM2RoxYQuCsgKy_Nhr0k"; // 库存不足
		
		public static final String ONE = "4_Fd8pPf95jAGjkH9l-VEkSaV3uit-W2AtAKuuraXM8"; // 司机有新的订单
		
		public static final String TWO = "yHl5Ui008tmrltgsKG6pFrUOGvbnoO6SDD2VyhcPzng"; // 司机已装车成功
		
		public static final String THREE = "zIhUvta65bNXQDIAkuUaIfmtytwWh2F-tnrFipGUO8Q"; // 货物送达
		
	}
	
	public static interface JSONFILTERS {
		
		public static final SimplePropertyPreFilter USERINFO =
			new SimplePropertyPreFilter(User.class, "username", "departmentId", "storesName", "createTimeStr"); // 订单输出到前端字段
		
		public static final SimplePropertyPreFilter USERBRIEF =
			new SimplePropertyPreFilter(User.class, "id", "username");
		
		public static final SimplePropertyPreFilter MERCHANDISELIST = new SimplePropertyPreFilter(Merchandise.class,
			"id", "merchandiseName", "specification", "unit", "unitPrice", "img", "brandName");
		
		public static final SimplePropertyPreFilter DELIVERYORDER = new SimplePropertyPreFilter(DeliveryOrder.class,
			"id", "state", "orderInfos", "receivingAddress", "orderType");
		
		public static final SimplePropertyPreFilter DELIVERYORDERTWO =
			new SimplePropertyPreFilter(DeliveryOrder.class, "id", "state", "orderInfos", "receivingAddress",
				"orderType", "shippingcosts", "managerName", "managerTel", "storesName", "upstairs");
		
		public static final SimplePropertyPreFilter ORDRE = new SimplePropertyPreFilter(DeliveryOrder.class, "id",
			"orderCode", "receivingAddress", "state", "orderType", "placeOrderTimeStr");
		
		public static final SimplePropertyPreFilter STORAGE = new SimplePropertyPreFilter(DeliveryOrder.class,
			"merchandiseName", "number", "totalPrice", "isPay", "createTimeStr", "type");
	}
}
