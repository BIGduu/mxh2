package net.mxh.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @description 角色菜单对应表
 * @author ZhongHan
 * @date 2017年8月24日
 */
@Entity
@Table(name = "role_menu")
public class RoleMenu {
	
	@EmbeddedId
	private RoleMenuId id;
	
	public RoleMenu() {
	}
	
	public RoleMenu(RoleMenuId id) {
		this.id = id;
	}
	
	public RoleMenuId getId() {
		return this.id;
	}
	
	public void setId(RoleMenuId id) {
		this.id = id;
	}
	
}
