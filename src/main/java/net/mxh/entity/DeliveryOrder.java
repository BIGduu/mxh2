package net.mxh.entity;

import java.io.Serializable;
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
 * @description 订单司机表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "delivery_order")
public class DeliveryOrder implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "order_id")
	private Long orderId;
	
	@Column(name = "delivery_id")
	private Long deliveryId;
	
	@Column(name = "delivery_name")
	private String deliveryName;
	
	@Column(name = "delivery_tel")
	private String deliveryTel;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "img")
	private String img;
	
	@Column(name = "state")
	private Integer state; // 状态：3、已分配；4、装车中；5、已出库；6、已送达；7、已完成；8、司机驳回
	//运费
	@Column(name = "shippingcosts")
	private BigDecimal shippingcosts;
	
	@Column(name = "upstaircosts")
	private BigDecimal upstaircosts;
	
	@Column(name = "upstair_persion")
	private String upstairPersion;
	
	@Column(name = "floor")
	private BigDecimal floor;
	
	@Transient
	private List<OrderInfo> orderInfos;
	
	@Transient
	private String receivingAddress;
	
	@Transient
	private Integer orderType;
	
	@Transient
	private String managerName;
	
	@Transient
	private String managerTel;
	
	@Transient
	private String storesName;
	
	@Transient
	private Integer upstairs;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getOrderId() {
		return this.orderId;
	}
	
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public Long getDeliveryId() {
		return this.deliveryId;
	}
	
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	public String getDeliveryName() {
		return this.deliveryName;
	}
	
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	
	public String getDeliveryTel() {
		return this.deliveryTel;
	}
	
	public void setDeliveryTel(String deliveryTel) {
		this.deliveryTel = deliveryTel;
	}
	
	public String getRemarks() {
		return this.remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getImg() {
		return this.img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public Integer getState() {
		return state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public BigDecimal getShippingcosts() {
		return shippingcosts;
	}
	
	public void setShippingcosts(BigDecimal shippingcosts) {
		this.shippingcosts = shippingcosts;
	}
	
	public BigDecimal getUpstaircosts() {
		return upstaircosts;
	}
	
	public void setUpstaircosts(BigDecimal upstaircosts) {
		this.upstaircosts = upstaircosts;
	}
	
	public List<OrderInfo> getOrderInfos() {
		return orderInfos;
	}
	
	public void setOrderInfos(List<OrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	
	public String getReceivingAddress() {
		return receivingAddress;
	}
	
	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}
	
	public Integer getOrderType() {
		return orderType;
	}
	
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	
	public String getManagerName() {
		return managerName;
	}
	
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	public String getManagerTel() {
		return managerTel;
	}
	
	public void setManagerTel(String managerTel) {
		this.managerTel = managerTel;
	}
	
	public String getStoresName() {
		return storesName;
	}
	
	public void setStoresName(String storesName) {
		this.storesName = storesName;
	}
	
	public Integer getUpstairs() {
		return upstairs;
	}
	
	public void setUpstairs(Integer upstairs) {
		this.upstairs = upstairs;
	}
	
	public String getUpstairPersion() {
		return upstairPersion;
	}
	
	public void setUpstairPersion(String upstairPersion) {
		this.upstairPersion = upstairPersion;
	}
	
	public BigDecimal getFloor() {
		return floor;
	}
	
	public void setFloor(BigDecimal floor) {
		this.floor = floor;
	}
	
}
