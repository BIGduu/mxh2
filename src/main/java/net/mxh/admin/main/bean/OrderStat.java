package net.mxh.admin.main.bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author bigduu
 */
public class OrderStat {
	
	public OrderStat() {
		
	}
	
	public OrderStat(OrderStat orderStat) {
		this.merchandiseId = orderStat.merchandiseId;
		this.merchandiseName = orderStat.merchandiseName;
		this.number = orderStat.number;
		this.rowspan = orderStat.rowspan;
		this.allPrice = orderStat.allPrice;
		this.managerId = orderStat.managerId;
		this.managerName = orderStat.managerName;
		this.storesId = orderStat.storesId;
		this.storesName = orderStat.storesName;
		this.receivingBuildingId = orderStat.receivingBuildingId;
		this.child = orderStat.child;
		this.merchandise = orderStat.merchandise;
		this.receivingAddress = orderStat.receivingAddress;
		this.merchandise = orderStat.merchandise;
		this.modificationTime = orderStat.modificationTime;
	}
	
	@Override
	public String toString() {
		return this.number.toString() + ":" + Integer.toHexString(hashCode());
	}
	
	private String merchandiseId;
	
	private String merchandiseName;
	
	private BigInteger number = BigInteger.ZERO;
	
	private Integer rowspan = 1;
	
	public Integer getRowspan() {
		return rowspan;
	}
	
	public void setRowspan(Integer rowspan) {
		this.rowspan = rowspan;
	}
	
	private BigDecimal allPrice = new BigDecimal(0);
	
	private String managerId;
	
	private String managerName;
	
	private String storesId;
	
	private String storesName;
	
	private String receivingBuildingId;
	
	private Map<String, OrderStat> child = new HashMap<String, OrderStat>();
	
	private Map<String, OrderStat> merchandise = new HashMap<String, OrderStat>();
	
	public Map<String, OrderStat> getMerchandise() {
		return merchandise;
	}
	
	public void setMerchandise(Map<String, OrderStat> merchandise) {
		this.merchandise = new HashMap<String, OrderStat>(merchandise);
	}
	
	public Map<String, OrderStat> getChild() {
		return child;
	}
	
	public void setChild(Map<String, OrderStat> child) {
		this.child = child;
	}
	
	public String getReceivingBuildingId() {
		return receivingBuildingId;
	}
	
	public void setReceivingBuildingId(String receivingBuildingId) {
		this.receivingBuildingId = receivingBuildingId;
	}
	
	private String receivingAddress;
	
	private String modificationTime;
	
	public String getMerchandiseId() {
		return merchandiseId;
	}
	
	public void setMerchandiseId(String merchandiseId) {
		this.merchandiseId = merchandiseId;
	}
	
	public String getMerchandiseName() {
		return merchandiseName;
	}
	
	public void setMerchandiseName(String merchandiseName) {
		this.merchandiseName = merchandiseName;
	}
	
	public BigInteger getNumber() {
		return number;
	}
	
	public void setNumber(BigInteger number) {
		this.number = number;
	}
	
	public BigDecimal getAllPrice() {
		return allPrice;
	}
	
	public void setAllPrice(BigDecimal allPrice) {
		this.allPrice = allPrice;
	}
	
	public String getManagerId() {
		return managerId;
	}
	
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	
	public String getManagerName() {
		return managerName;
	}
	
	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}
	
	public String getStoresId() {
		return storesId;
	}
	
	public void setStoresId(String storesId) {
		this.storesId = storesId;
	}
	
	public String getStoresName() {
		return storesName;
	}
	
	public void setStoresName(String storesName) {
		this.storesName = storesName;
	}
	
	public String getReceivingAddress() {
		return receivingAddress;
	}
	
	public void setReceivingAddress(String receivingAddress) {
		this.receivingAddress = receivingAddress;
	}
	
	public String getModificationTime() {
		return modificationTime;
	}
	
	public void setModificationTime(String modificationTime) {
		this.modificationTime = modificationTime;
	}
	
}
