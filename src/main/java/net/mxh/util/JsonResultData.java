package net.mxh.util;

import java.util.HashMap;
import java.util.Map;

public class JsonResultData {
	
	private String status;
	private String message;
	private Map<String,Object> data;
	
	private JsonResultData(){
		
	}
	
	public static JsonResultData success(){
		JsonResultData data = new JsonResultData();
		data.setStatus("200");
		data.setMessage("successfully");
		data.setData(new HashMap<String,Object>());
		return data;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	
	public void addObject(String key, Object value){
		this.data.put(key, value);
	}
	/**
	 * 此方法会把data数据清空
	 * @param message
	 * @return
	 */
	public JsonResultData turnError(String message){
		this.status = "400";
		this.message = message;
		this.data.clear();
		return this;
	}
}
