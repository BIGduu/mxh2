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
 * @description 库存表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "storage")
public class Storage {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "merchandise_id")
	private Long merchandiseId;
	
	@Column(name = "merchandise_name")
	private String merchandiseName;
	
	@Column(name = "number")
	private BigDecimal number;
	
	@Column(name = "total_price")
	private Float totalPrice;
	
	@Column(name = "is_pay")
	private Integer isPay;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	@Column(name = "type")
	private Integer type;
	
	@Column(name = "delivery_company")
	private String deliveryCompany;
	
	@Column(name = "receipt_number")
	private String receiptNumber;
	
	@Column(name = "billing_quantity")
	private BigDecimal billingQuantity;
	
	@Column(name = "billing_unit")
	private String billingUnit;
	
	@Column(name = "purchase_price")
	private BigDecimal purchasePrice;
	
	@Column(name = "shipping_price")
	private BigDecimal shippingPrice;
	
	@Column(name = "purchase_date")
	private Long purchaseDate;
	
	@Column(name = "purchase_name")
	private String purchaseName;
	
	@Column(name = "shipping_date")
	private Long shippingDate;
	
	@Column(name = "shipping_name")
	private String shippingName;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Transient
	private String createTimeStr;
	
	@Transient
	private String shippingDateStr;
	
	@Transient
	private String purchaseDateStr;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public Float getTotalPrice() {
		return this.totalPrice;
	}
	
	public void setTotalPrice(Float totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Integer getIsPay() {
		return this.isPay;
	}
	
	public void setIsPay(Integer isPay) {
		this.isPay = isPay;
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
	
	public Integer getType() {
		return type;
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public String getDeliveryCompany() {
		return deliveryCompany;
	}
	
	public void setDeliveryCompany(String deliveryCompany) {
		this.deliveryCompany = deliveryCompany;
	}
	
	public String getReceiptNumber() {
		return receiptNumber;
	}
	
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	
	public BigDecimal getBillingQuantity() {
		return billingQuantity;
	}
	
	public void setBillingQuantity(BigDecimal billingQuantity) {
		this.billingQuantity = billingQuantity;
	}
	
	public String getBillingUnit() {
		return billingUnit;
	}
	
	public void setBillingUnit(String billingUnit) {
		this.billingUnit = billingUnit;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	public BigDecimal getShippingPrice() {
		return shippingPrice;
	}
	
	public void setShippingPrice(BigDecimal shippingPrice) {
		this.shippingPrice = shippingPrice;
	}
	
	public Long getPurchaseDate() {
		return purchaseDate;
	}
	
	public void setPurchaseDate(Long purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	
	public String getPurchaseName() {
		return purchaseName;
	}
	
	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}
	
	public Long getShippingDate() {
		return shippingDate;
	}
	
	public void setShippingDate(Long shippingDate) {
		this.shippingDate = shippingDate;
	}
	
	public String getShippingName() {
		return shippingName;
	}
	
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getShippingDateStr() {
		return shippingDateStr;
	}
	
	public void setShippingDateStr(String shippingDateStr) {
		this.shippingDateStr = shippingDateStr;
	}
	
	public String getPurchaseDateStr() {
		return purchaseDateStr;
	}
	
	public void setPurchaseDateStr(String purchaseDateStr) {
		this.purchaseDateStr = purchaseDateStr;
	}
	
}
