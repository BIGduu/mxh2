package net.mxh.vo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.mxh.entity.OrderInfo;

public class FinalOrderExcel {
	
	private Date placeOrderTime;
	
	private String managerName;
	
	private String receivingAddress;
	
	private BigDecimal allPrice;
	
	private BigDecimal upstaircosts;
	
	private BigDecimal shippingcosts;
	
	private List<OrderInfo> orderInfos;
	
	private String remarks;
	
	public Date getPlaceOrderTime() {
		return placeOrderTime;
	}
	
	public void setPlaceOrderTime(Date placeOrderTime) {
		this.placeOrderTime = placeOrderTime;
	}
	
	public String getManagerName() {
		return managerName;
	}
	
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	public String getReceivingAddress() {
		return receivingAddress;
	}
	
	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}
	
	public BigDecimal getAllPrice() {
		return allPrice;
	}
	
	public void setAllPrice(BigDecimal allPrice) {
		this.allPrice = allPrice;
	}
	
	public BigDecimal getUpstaircosts() {
		return upstaircosts;
	}
	
	public void setUpstaircosts(BigDecimal upstaircosts) {
		this.upstaircosts = upstaircosts;
	}
	
	public BigDecimal getShippingcosts() {
		return shippingcosts;
	}
	
	public void setShippingcosts(BigDecimal shippingcosts) {
		this.shippingcosts = shippingcosts;
	}
	
	public List<OrderInfo> getOrderInfos() {
		return orderInfos;
	}
	
	public void setOrderInfos(List<OrderInfo> orderInfos) {
		this.orderInfos = orderInfos;
	}
	
	public String getRemarks() {
		return remarks;
	}
	
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
