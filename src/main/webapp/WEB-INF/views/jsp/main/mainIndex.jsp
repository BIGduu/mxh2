<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${isShow == 1 }">
<div class="content" style="min-width:1100px">
	<table border="1"  style="width: 500px;text-align: center; margin-top: 10px;">
		<caption>材料明细</caption>
		<tr><td>材料</td><td>接单</td><td>出库</td><td>退货</td><td>进货</td><td>库存</td></tr>
			<c:forEach items="${list }" var="map">
				<tr>
					<td>${map.merchandiseName }</td>
					<td class="clearZero">${map.orderSum }</td>
					<td class="clearZero">${map.outSum }</td>
					<td class="clearZero">${map.returnSum }</td>
					<td class="clearZero">${map.purchaseSum }</td>
					<td class="clearZero">${map.total }</td>
				</tr>
			</c:forEach>
                       </table>
                       
                       <table border="1"  style="width: 500px;text-align: center; margin-top: 10px;">
                       	<caption>应付账款明细</caption>
                       	<tr><td>材料</td><td>数量</td><td>金额</td></tr>
			<c:forEach items="${storages }" var="map">
				<tr>
					<td>${map.merchandiseName }</td>
					<td><span class="clearZero">${map.billingQuantitySum }</span>${map.billingUnit }</td>
					<td class="clearZero">${map.purchaseAmountSum }</td>
				</tr>
			</c:forEach>
                       </table>
                       
                       <table border="1"  style="width: 500px;text-align: center; margin-top: 10px;">
                       	<caption>各店应收款明细</caption>
                       	<tr><td>分店</td><td>金额</td></tr>
			<c:forEach items="${receivables }" var="map">
				<tr><td>${map.storesName }</td><td>${map.allPrice }</td></tr>
			</c:forEach>
                       </table>
                       
                       <table border="1"  style="width: 500px;text-align: center; margin-top: 10px;">
                       	<caption>运费明细</caption>
                       	<tr><td>司机</td><td>运费</td></tr>
			<c:forEach items="${freights }" var="map">
				<tr><td>${map.deliveryName }</td><td>${map.shippingcosts }</td></tr>
			</c:forEach>
                       </table>
                       
</div>
                      </c:if>
<script>
$(function() {
	var clearZeros = $(".clearZero");
	for (var i = 0; i < clearZeros.length; i++) {
		$(clearZeros[i]).html(parseFloat($(clearZeros[i]).html()));
	}
	
})
</script>