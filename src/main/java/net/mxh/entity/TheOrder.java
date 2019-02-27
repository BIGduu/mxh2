package net.mxh.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description 订单表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "t_order")
public class TheOrder {

	/**
	 *  储存orderInfo信息用于提交到前端统计页面
	 *  @authot bigduu
	 **/


	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "order_code")
	private String orderCode;
	
	@Column(name = "place_order_time")
	private Long placeOrderTime;
	
	@Column(name = "sentto_time")
	private Long senttoTime;
	
	@Column(name = "manager_id")
	private Long managerId;
	
	@Column(name = "manager_name")
	private String managerName;
	
	@Column(name = "manager_tel")
	private String managerTel;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "receiving_building_id")
	private Long receivingBuildingId;

	public Long getReceivingBuildingId() {
		return receivingBuildingId;
	}
	
	public void setReceivingBuildingId(Long receivingBuildingId) {
		this.receivingBuildingId = receivingBuildingId;
	}
	
	@Column(name = "receiving_address")
	private String receivingAddress;
	
	@Column(name = "is_pay")
	private Integer isPay;
	
	@Column(name = "state")
	private Integer state;
	
	@Column(name = "stores_id")
	private Long storesId;
	
	@Column(name = "stores_name")
	private String storesName;
	
	@Column(name = "order_type")
	private Integer orderType;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	@Column(name = "is_reconciliation")
	private Integer isReconciliation;
	
	@Column(name = "reconciliation_step")
	private Integer reconciliationStep;
	
	@Column(name = "is_split")
	private Integer isSplit;
	
	@Column(name = "check_state")
	private Integer checkState;
	
	@Column(name = "upstairs")
	private Integer upstairs;
	
	@Column(name = "all_price")
	private BigDecimal allPrice;
	
	@Column(name = "reason_one")
	private String reasonOne;
	
	@Column(name = "reason_tow")
	private String reasonTow;
	
	@Transient
	private String placeOrderTimeStr;
	
	@Transient
	private String deliveryNames;
	
	@Transient
	private BigDecimal shippingcosts;
	
	@Transient
	private BigDecimal upstaircosts;
	/**
	 * @author bigduu
	 */
	@Transient
	private List<OrderInfo> orderInfos;
	/**
	 * @author bigduu
	 */
	public List<OrderInfo> getOrderInfos() {
		return orderInfos;
	}
	/**
	 * @author bigduu
	 */
	public void setOrderInfos(List<OrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}

	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getOrderCode() {
		return this.orderCode;
	}
	
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	
	public Long getPlaceOrderTime() {
		return this.placeOrderTime;
	}
	
	public void setPlaceOrderTime(Long placeOrderTime) {
		this.placeOrderTime = placeOrderTime;
	}
	
	public Long getManagerId() {
		return this.managerId;
	}
	
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	
	public String getManagerName() {
		return this.managerName;
	}
	
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	public String getManagerTel() {
		return this.managerTel;
	}
	
	public void setManagerTel(String managerTel) {
		this.managerTel = managerTel;
	}
	
	public String getReceivingAddress() {
		return this.receivingAddress;
	}
	
	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}
	
	public Integer getIsPay() {
		return this.isPay;
	}
	
	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
	}
	
	public Integer getState() {
		return this.state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Integer getOrderType() {
		return orderType;
	}
	
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	public Long getCreatorId() {
		return this.creatorId;
	}
	
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Long getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Long getModifierId() {
		return this.modifierId;
	}
	
	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}
	
	public Long getModificationTime() {
		return this.modificationTime;
	}
	
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}
	
	public Long getStoresId() {
		return storesId;
	}
	
	public void setStoresId(Long storesId) {
		this.storesId = storesId;
	}
	
	public String getStoresName() {
		return storesName;
	}
	
	public void setStoresName(String storesName) {
		this.storesName = storesName;
	}
	
	public Integer getIsReconciliation() {
		return isReconciliation;
	}
	
	public void setIsReconciliation(Integer isReconciliation) {
		this.isReconciliation = isReconciliation;
	}
	
	public Integer getReconciliationStep() {
		return reconciliationStep;
	}
	
	public void setReconciliationStep(Integer reconciliationStep) {
		this.reconciliationStep = reconciliationStep;
	}
	
	public Integer getIsSplit() {
		return isSplit;
	}
	
	public void setIsSplit(Integer isSplit) {
		this.isSplit = isSplit;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getPlaceOrderTimeStr() {
		return placeOrderTimeStr;
	}
	
	public void setPlaceOrderTimeStr(String placeOrderTimeStr) {
		this.placeOrderTimeStr = placeOrderTimeStr;
	}
	
	public String getDeliveryNames() {
		return deliveryNames;
	}
	
	public void setDeliveryNames(String deliveryNames) {
		this.deliveryNames = deliveryNames;
	}
	
	public Integer getCheckState() {
		return checkState;
	}
	
	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}
	
	public BigDecimal getShippingcosts() {
		return shippingcosts;
	}
	
	public void setShippingcosts(BigDecimal shippingcosts) {
		this.shippingcosts = shippingcosts;
	}
	
	public Long getSenttoTime() {
		return senttoTime;
	}
	
	public void setSenttoTime(Long senttoTime) {
		this.senttoTime = senttoTime;
	}
	
	public Integer getUpstairs() {
		return upstairs;
	}
	
	public void setUpstairs(Integer upstairs) {
		this.upstairs = upstairs;
	}
	
	public BigDecimal getAllPrice() {
		return allPrice;
	}
	
	public void setAllPrice(BigDecimal allPrice) {
		this.allPrice = allPrice;
	}
	
	public String getReasonOne() {
		return reasonOne;
	}
	
	public void setReasonOne(String reasonOne) {
		this.reasonOne = reasonOne;
	}
	
	public String getReasonTow() {
		return reasonTow;
	}
	
	public void setReasonTow(String reasonTow) {
		this.reasonTow = reasonTow;
	}
	
	public BigDecimal getUpstaircosts() {
		return upstaircosts;
	}
	
	public void setUpstaircosts(BigDecimal upstaircosts) {
		this.upstaircosts = upstaircosts;
	}
	
}
