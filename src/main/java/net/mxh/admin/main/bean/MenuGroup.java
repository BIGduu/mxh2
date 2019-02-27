package net.mxh.admin.main.bean;

import java.util.ArrayList;
import java.util.List;

import net.mxh.entity.Menu;

public class MenuGroup {
	
	private String name;
	
	private String menuCss;
	
	private List<Menu> menus = new ArrayList<Menu>();
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Menu> getMenus() {
		return menus;
	}
	
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	
	public String getMenuCss() {
		return menuCss;
	}
	
	public void setMenuCss(String menuCss) {
		this.menuCss = menuCss;
	}
	
}
