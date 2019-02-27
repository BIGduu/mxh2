package net.mxh.admin.main.bean;

import java.io.Serializable;

/**
 * @description 下拉选项共用类
 * @author LuBo
 * @date 2017年7月6日
 */
public class CommonSelect implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;
	
	private String name;
	
	public CommonSelect(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
