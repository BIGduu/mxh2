package net.mxh.entity;

import java.io.Serializable;

import javax.persistence.Column;

public class RoleMenuId implements Serializable {
	
	private static final long serialVersionUID = 8484878L;
	
	@Column(name = "role_id")
	private Long roleId;
	
	@Column(name = "menu_id")
	private Long menuId;
	
	public RoleMenuId() {
	}
	
	public RoleMenuId(Long roleId, Long menuId) {
		this.roleId = roleId;
		this.menuId = menuId;
	}
	
	public Long getRoleId() {
		return this.roleId;
	}
	
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public Long getMenuId() {
		return this.menuId;
	}
	
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof RoleMenuId)) {
			return false;
		}
		RoleMenuId castOther = (RoleMenuId)other;
		
		return (this.getRoleId() == castOther.getRoleId()) && (this.getMenuId() == castOther.getMenuId());
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		
		result = 37 * result + this.getRoleId().intValue();
		result = 37 * result + this.getMenuId().intValue();
		return result;
	}
	
}
