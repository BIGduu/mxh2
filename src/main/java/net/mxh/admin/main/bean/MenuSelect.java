package net.mxh.admin.main.bean;

public class MenuSelect {
	
	private Long menuId;
	
	private String linkUrl;
	
	private String menuName;
	
	private int selected;
	
	public Long getMenuId() {
		return menuId;
	}
	
	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	public String getLinkUrl() {
		return linkUrl;
	}
	
	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public String getMenuName() {
		return menuName;
	}
	
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
}
