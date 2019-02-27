package net.mxh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description 用户表（司机和用户）
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "user")
public class User {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "telephone")
	private String telephone;
	
	@Column(name = "department_id")
	private Long departmentId;
	
	@Column(name = "department_name")
	private String departmentName;
	
	@Column(name = "stores_id")
	private Long storesId;
	
	@Column(name = "stores_name")
	private String storesName;
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	@Column(name = "openid")
	private String openid;
	
	@Transient
	private String createTimeStr;
	
	@Transient
	private Integer isSign;
	
	public Long getId() {
		return this.id;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getDepartmentId() {
		return this.departmentId;
	}
	
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	public String getDepartmentName() {
		return this.departmentName;
	}
	
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	
	public Long getStoresId() {
		return this.storesId;
	}
	
	public void setStoresId(Long storesId) {
		this.storesId = storesId;
	}
	
	public String getStoresName() {
		return this.storesName;
	}
	
	public void setStoresName(String storesName) {
		this.storesName = storesName;
	}
	
	public Integer getStatus() {
		return this.status;
	}
	
	public void setStatus(Integer status) {
		this.status = status;
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
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
	public Integer getIsSign() {
		return isSign;
	}
	
	public void setIsSign(Integer isSign) {
		this.isSign = isSign;
	}
	
}
