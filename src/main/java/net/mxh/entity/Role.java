package net.mxh.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @description 角色表
 * @author ZhongHan
 * @date 2017年11月23日
 */
@Entity
@Table(name = "sys_role")
public class Role {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "role_name")
	private String roleName;
	
	@Column(name = "role_code")
	private String roleCode;
	
	@Column(name = "states")
	private String states;
	
	@Column(name = "checks")
	private String checks;
	
	@Transient
	private String[] stateList = {};
	
	@Transient
	private String[] checkList = {};
	
	public Long getId() {
		return this.id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getRoleName() {
		return this.roleName;
	}
	
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	public String getRoleCode() {
		return this.roleCode;
	}
	
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	
	public String getStates() {
		return states;
	}
	
	public void setStates(String states) {
		this.states = states;
	}
	
	public String[] getStateList() {
		return stateList;
	}
	
	public void setStateList(String[] stateList) {
		this.stateList = stateList;
	}
	
	public String getChecks() {
		return checks;
	}
	
	public void setChecks(String checks) {
		this.checks = checks;
	}
	
	public String[] getCheckList() {
		return checkList;
	}
	
	public void setCheckList(String[] checkList) {
		this.checkList = checkList;
	}
	
}
