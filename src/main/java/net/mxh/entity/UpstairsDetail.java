package net.mxh.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 配送上楼单明细表
 * @author ljh
 * @date 20180908
 */
@Entity
@Table(name = "upstairs_detail")
public class UpstairsDetail {
	
	@Id
	@Column(name = "upstairs_detail_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long upstairsDetailId;
	
	@Column(name = "order_id")
	private Long orderId;
	
	@Column(name = "delivery_order_id")
	private Long deliveryOrderId;
	
	@Column(name = "delivery_id")
	private Long deliveryId;
	
	@Column(name = "delivery_name")
	private String deliveryName;
	
	@Column(name = "merchandise_id")
	private Long merchandiseId;
	
	@Column(name = "merchandise_name")
	private String merchandiseName;
	
	@Column(name = "specification")
	private String specification;
	
	@Column(name = "upstairs_cost")
	private BigDecimal upstairsCost;
	
	@Column(name = "number")
	private BigDecimal number;
	
	@Column(name = "floor")
	private BigDecimal floor;
	
	@Column(name = "cost")
	private BigDecimal cost;
	
	@Column(name = "upstair_persion")
	private Long upstairPersion;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	public Long getUpstairsDetailId() {
		return upstairsDetailId;
	}
	
	public void setUpstairsDetailId(Long upstairsDetailId) {
		this.upstairsDetailId = upstairsDetailId;
	}
	
	public Long getOrderId() {
		return orderId;
	}
	
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public Long getDeliveryOrderId() {
		return deliveryOrderId;
	}
	
	public void setDeliveryOrderId(Long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}
	
	public Long getDeliveryId() {
		return deliveryId;
	}
	
	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}
	
	public Long getMerchandiseId() {
		return merchandiseId;
	}
	
	public void setMerchandiseId(Long merchandiseId) {
		this.merchandiseId = merchandiseId;
	}
	
	public BigDecimal getUpstairsCost() {
		return upstairsCost;
	}
	
	public void setUpstairsCost(BigDecimal upstairsCost) {
		this.upstairsCost = upstairsCost;
	}
	
	public BigDecimal getNumber() {
		return number;
	}
	
	public void setNumber(BigDecimal number) {
		this.number = number;
	}
	
	public BigDecimal getFloor() {
		return floor;
	}
	
	public void setFloor(BigDecimal floor) {
		this.floor = floor;
	}
	
	public BigDecimal getCost() {
		return cost;
	}
	
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public Long getUpstairPersion() {
		return upstairPersion;
	}
	
	public void setUpstairPersion(Long upstairPersion) {
		this.upstairPersion = upstairPersion;
	}
	
	public Long getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	
	public Long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	public Long getModifierId() {
		return modifierId;
	}
	
	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}
	
	public Long getModificationTime() {
		return modificationTime;
	}
	
	public void setModificationTime(Long modificationTime) {
		this.modificationTime = modificationTime;
	}
	
	public String getDeliveryName() {
		return deliveryName;
	}
	
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	
	public String getMerchandiseName() {
		return merchandiseName;
	}
	
	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}
	
	public String getSpecification() {
		return specification;
	}
	
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	
}
