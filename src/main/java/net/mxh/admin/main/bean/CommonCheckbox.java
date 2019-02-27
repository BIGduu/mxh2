package net.mxh.admin.main.bean;

/**
 * @description 多选框共用类
 * @author LuBo
 * @date 2017年7月6日
 */
public class CommonCheckbox {
	
	private Integer id;
	
	private String name;
	
	private Boolean checked;
	
	public CommonCheckbox(Integer id, String name, Boolean checked) {
		super();
		this.id = id;
		this.name = name;
		this.checked = checked;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getChecked() {
		return checked;
	}
	
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}
}
