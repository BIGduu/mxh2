package net.mxh.admin.main.bean;

/**
 * @description 搜索
 * @author LuBo
 * @date 2017年7月6日
 */
public class CommonSearch {
	
	private Long id;
	
	private String name;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public CommonSearch(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
