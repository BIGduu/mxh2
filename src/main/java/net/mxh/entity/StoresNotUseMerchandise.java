package net.mxh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 门店不能使用的商品
 * @author ljh
 * @date 2018年7月29日
 */
@Entity
@Table(name = "stores_not_use_merchandise")
public class StoresNotUseMerchandise {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "stores_id")
	private Long storesId;
	
	@Column(name = "merchandise_id")
	private Long merchandiseId;
	
	@Column(name = "is_use")
	private Integer isUse;
	
	@Column(name = "remarks")
	private String remarks;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getStoresId() {
		return storesId;
	}
	
	public void setStoresId(Long storesId) {
		this.storesId = storesId;
	}
	
	public Long getMerchandiseId() {
		return merchandiseId;
	}
	
	public void setMerchandiseId(Long merchandiseId) {
		this.merchandiseId = merchandiseId;
	}
	
	public Integer getIsUse() {
		return isUse;
	}
	
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
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
	
}
