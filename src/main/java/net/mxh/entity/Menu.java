package net.mxh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description 菜单表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "sys_menu")
public class Menu {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "menu_name")
	private String menuName;
	
	@Column(name = "link_url")
	private String linkUrl;
	
	@Column(name = "icon")
	private String icon;
	
	@Column(name = "menu_level")
	private Integer menuLevel;
	
	@Column(name = "parent_id")
	private Long parentId;
	
	@Column(name = "show_order")
	private Integer showOrder;
	
	@Column(name = "is_use")
	private Integer isUse;
	
	@Column(name = "memo")
	private String memo;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	@Transient
	private String parentName;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getMenuName() {
		return this.menuName;
	}
	
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public String getLinkUrl() {
		return this.linkUrl;
	}
	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public Integer getMenuLevel() {
		return this.menuLevel;
	}
	
	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}
	
	public Long getParentId() {
		return this.parentId;
	}
	
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	public int getShowOrder() {
		return this.showOrder;
	}
	
	public void setShowOrder(int showOrder) {
		this.showOrder = showOrder;
	}
	
	public Integer getIsUse() {
		return isUse;
	}
	
	public void setIsUse(Integer isUse) {
		this.isUse = isUse;
	}
	
	public void setShowOrder(Integer showOrder) {
		this.showOrder = showOrder;
	}
	
	public String getMemo() {
		return this.memo;
	}
	
	public void setMemo(String memo) {
		this.memo = memo;
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
	
	public String getParentName() {
		return parentName;
	}
	
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	
}
