package net.mxh.vo;

import java.io.Serializable;
import java.util.List;

import net.mxh.entity.TheOrder;

public class OrderPageDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Integer total;
	
	Integer page;
	
	Integer pageSize;

	Long count;



	List<TheOrder> orders;


	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

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
	
	public List<TheOrder> getOrders() {
		return orders;
	}
	
	public void setOrders(List<TheOrder> orders) {
		this.orders = orders;
	}
	
}
