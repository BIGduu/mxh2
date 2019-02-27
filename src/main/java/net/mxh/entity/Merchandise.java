package net.mxh.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 商品表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "merchandise")
public class Merchandise {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "merchandise_name")
	private String merchandiseName;
	
	@Column(name = "merchandise_code")
	private String merchandiseCode;
	
	@Column(name = "specification")
	private String specification;
	
	@Column(name = "unit")
	private String unit;
	
	@Column(name = "unit_price")
	private BigDecimal unitPrice;
	
	@Column(name = "img")
	private String img;
	
	@Column(name = "brand_name")
	private String brandName;
	
	@Column(name = "is_use")
	private Integer isUse;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	@Column(name = "shipping_cost")
	private BigDecimal shippingCost;
	
	@Column(name = "upstairs_cost")
	private BigDecimal upstairsCost;
	
	@Column(name = "bottom_line")
	private BigDecimal bottomLine;
	
	@Column(name = "total")
	private BigDecimal total;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMerchandiseName() {
		return this.merchandiseName;
	}
	
	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}
	
	public String getMerchandiseCode() {
		return this.merchandiseCode;
	}
	
	public void setMerchandiseCode(String merchandiseCode) {
		this.merchandiseCode = merchandiseCode;
	}
	
	public String getSpecification() {
		return this.specification;
	}
	
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	
	public String getUnit() {
		return this.unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public BigDecimal getUnitPrice() {
		return this.unitPrice;
	}
	
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	public String getImg() {
		return this.img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	public String getBrandName() {
		return this.brandName;
	}
	
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
	public Integer getIsUse() {
		return isUse;
	}
	
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
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
	
	public BigDecimal getShippingCost() {
		return shippingCost;
	}
	
	public void setShippingCost(BigDecimal shippingCost) {
		this.shippingCost = shippingCost;
	}
	
	public BigDecimal getUpstairsCost() {
		return upstairsCost;
	}
	
	public void setUpstairsCost(BigDecimal upstairsCost) {
		this.upstairsCost = upstairsCost;
	}
	
	public BigDecimal getBottomLine() {
		return bottomLine;
	}
	
	public void setBottomLine(BigDecimal bottomLine) {
		this.bottomLine = bottomLine;
	}
	
	public BigDecimal getTotal() {
		return total;
	}
	
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
}
