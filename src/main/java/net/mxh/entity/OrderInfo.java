package net.mxh.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description 订单详情表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "order_info")
public class OrderInfo {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "delivery_order_id")
	private Long deliveryOrderId;
	
	@Column(name = "merchandise_id")
	private Long merchandiseId;
	
	@Column(name = "merchandise_name")
	private String merchandiseName;
	
	@Column(name = "number")
	private BigDecimal number;
	
	@Column(name = "all_price")
	private BigDecimal allPrice;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "order_id")
	private Long orderId;
	
	@Column(name = "state")
	private Integer state;
	
	@Transient
	private Merchandise merchandise;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDeliveryOrderId() {
		return this.deliveryOrderId;
	}
	
	public void setDeliveryOrderId(Long deliveryOrderId) {
		this.deliveryOrderId = deliveryOrderId;
	}
	
	public Long getMerchandiseId() {
		return this.merchandiseId;
	}
	
	public void setMerchandiseId(Long merchandiseId) {
		this.merchandiseId = merchandiseId;
	}
	
	public String getMerchandiseName() {
		return this.merchandiseName;
	}
	
	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}
	
	public BigDecimal getNumber() {
		return this.number;
	}
	
	public void setNumber(BigDecimal number) {
		this.number = number;
	}
	
	public BigDecimal getAllPrice() {
		return this.allPrice;
	}
	
	public void setAllPrice(BigDecimal allPrice) {
		this.allPrice = allPrice;
	}
	
	public String getRemarks() {
		return this.remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public Long getOrderId() {
		return this.orderId;
	}
	
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	public Integer getState() {
		return state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Merchandise getMerchandise() {
		return merchandise;
	}
	
	public void setMerchandise(Merchandise merchandise) {
		this.merchandise = merchandise;
	}
	
}
