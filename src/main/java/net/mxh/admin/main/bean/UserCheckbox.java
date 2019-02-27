package net.mxh.admin.main.bean;

public class UserCheckbox {
	
	private Long id;
	
	private String username;
	
	private String telephone;
	
	private Integer state;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getTelephone() {
		return telephone;
	}
	
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public Integer getState() {
		return state;
	}
	
	public void setState(Integer state) {
		this.state = state;
	}
	
	public UserCheckbox(Long id, String username, String telephone, Integer state) {
		super();
		this.id = id;
		this.username = username;
		this.telephone = telephone;
		this.state = state;
	}
	
}
