package net.mxh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description 工地表
 * @author
 * @date
 */
@Entity
@Table(name = "building")
public class Building {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "building_name")
	private String buildingName;
	
	@Column(name = "manager_id")
	private Long managerId;
	
	@Column(name = "manager_name")
	private String managerName;
	
	@Column(name = "creator_id")
	private Long creatorId;
	
	@Column(name = "create_time")
	private Long createTime;
	
	@Column(name = "modifier_id")
	private Long modifierId;
	
	@Column(name = "modification_time")
	private Long modificationTime;
	
	@Column(name = "area")
	private Long area;
	
	@Column(name = "start_time")
	private Long startTime;
	
	public Long getArea() {
		return area;
	}
	
	public void setArea(Long area) {
		this.area = area;
	}
	
	public Long getStartTime() {
		return startTime;
	}
	
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	
	@Transient
	private String startTimeStr;
	
	public String getStartTimeStr() {
		return createTimeStr;
	}
	
	public void setStartTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public Long getManagerId() {
		return managerId;
	}
	
	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}
	
	public String getManagerName() {
		return managerName;
	}
	
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	@Transient
	private String createTimeStr;
	
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getBuildingName() {
		return buildingName;
	}
	
	public void setBuildingName(String buildingName) {
		this.buildingName = buildingName;
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
