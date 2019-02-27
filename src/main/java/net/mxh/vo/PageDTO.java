package net.mxh.vo;

import java.io.Serializable;
import java.util.List;

public class PageDTO<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Integer total;
	
	Integer page;
	
	Integer pageSize;
	
	List<T> list;
	
	public Integer getTotal() {
		return total;
	}
	
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getPage() {
		return page;
	}
	
	public void setPage(Integer page) {
		this.page = page;
	}
	
	public Integer getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	public List<T> getList() {
		return list;
	}
	
	public void setList(List<T> list) {
		this.list = list;
	}
	
}
