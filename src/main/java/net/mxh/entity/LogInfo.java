package net.mxh.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 日志表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "sys_log_info")
public class LogInfo {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "admin_id")
	private Long adminId;
	
	@Column(name = "admin_name")
	private String adminName;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "create_time")
	private Date createTime;
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
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
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
