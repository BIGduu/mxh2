package net.mxh.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.mxh.admin.main.bean.MenuGroup;

/**
 * @description 后台用户表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "admin")
public class Admin {
	
	@Id
	@Column(name = "admin_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long adminId;
	
	@Column(name = "admin_name")
	private String adminName; // 账号
	
	private String username;
	
	private String password;
	
	@Column(name = "is_use")
	private Integer isUse; // 是否可用：1、可用；2、不可用
	
	@Column(name = "stores_id")
	private Long storesId; // 门店id
	
	@Column(name = "stores_name")
	private String storesName;
	
	@Column(name = "telephone")
	private String telephone;
	
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "role_name")
	private String roleName;
	
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
	private List<MenuGroup> menuGroups;
	
	@Transient
	private Set<String> menuUrls;
	
	@Transient
	private Set<Integer> stateList = new HashSet<>();
	
	@Transient
	private Set<Integer> checkList = new HashSet<>();
	
	public Long getAdminId() {
		return this.adminId;
	}
	
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	
	public String getAdminName() {
		return this.adminName;
	}
	
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Integer getIsUse() {
		return this.isUse;
	}
	
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
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
	
	public String getTelephone() {
		return this.telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public Long getRoleId() {
		return this.roleId;
	}
	
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public String getRoleName() {
		return this.roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
	public List<MenuGroup> getMenuGroups() {
		return menuGroups;
	}
	
	public void setMenuGroups(List<MenuGroup> menuGroups) {
		this.menuGroups = menuGroups;
	}
	
	public Set<String> getMenuUrls() {
		return menuUrls;
	}
	
	public void setMenuUrls(Set<String> menuUrls) {
		this.menuUrls = menuUrls;
	}
	
	public Set<Integer> getStateList() {
		return stateList;
	}
	
	public void setStateList(Set<Integer> stateList) {
		this.stateList = stateList;
	}
	
	public Set<Integer> getCheckList() {
		return checkList;
	}
	
	public void setCheckList(Set<Integer> checkList) {
		this.checkList = checkList;
	}
	
	public String getOpenid() {
		return openid;
	}
	
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
}
