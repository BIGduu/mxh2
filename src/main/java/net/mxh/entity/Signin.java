package net.mxh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @description 签到表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "signin")
public class Signin {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "sign_time")
	private Long signTime;
	
	@Column(name = "is_off")
	private Long isOff;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public Long getSignTime() {
		return signTime;
	}
	
	public void setSignTime(Long signTime) {
		this.signTime = signTime;
	}
	
	public Long getIsOff() {
		return isOff;
	}
	
	public void setIsOff(Long isOff) {
		this.isOff = isOff;
	}
	
}
