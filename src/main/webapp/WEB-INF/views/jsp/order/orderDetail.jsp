<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>长沙明兄辉建材</title>
        <link rel="shortcut icon" href="${pageContext.request.contextPath}/assets/img/favicons/favicon.png">
		<style>
			th,td,p,h2{text-align:center}
			tr{height:35px}
			
			table{border-collapse:collapse;border-spacing:0;border-left:1px solid #888;border-top:1px solid #888;background:#efefef; max-width: 1000px; margin: 0 auto;}
			th,td{border-right:1px solid #888;border-bottom:1px solid #888;padding:5px 15px;}
			th{font-weight:bold;background:#ccc;}
			.printBtn{margin: 0 auto; display: block; margin-top: 10px;}
			@media print{
			    .printBtn{
			        display:none;
			    }
			}
		</style>
	</head>
<body>
	<h2>长沙明兄辉建材<c:choose><c:when test="${order.orderType == 1 }">出库单</c:when><c:otherwise>退货单</c:otherwise></c:choose> ${number }</h2>
	<p>收货人：${order.managerName } ${order.managerTel } 送货地址:${order.receivingAddress }</p>
	<div style="clear: both">&nbsp;</div>

	<!--表格形式展示-->
	<table border="1" style="text-align: center; width: 100%;">
		<thead>
			<tr>
				<th>编号</th>
				<th>材料名称</th>
				<th>规格/型号/颜色</th>
				<th>单位</th>
				<th>数量</th>
				<th>备注</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orderInfos}" var="orderInfo">
		            <tr>
						<td>${orderInfo.merchandise.merchandiseCode }</td>
						<td>${orderInfo.merchandiseName }</td>
						<td>${orderInfo.merchandise.specification }</td>
						<td>${orderInfo.merchandise.unit }</td>
						<td class="clearZero">${orderInfo.number }</td>
						<td></td>
		            </tr>
	            </c:forEach>
	            <tr>
					<td colspan="6">送货人:${deliveryName }</td>
				</tr>
		</tbody>
	</table>
	<button class="printBtn" type="button" onclick="javascript:window.print();">打印</button>
	<script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
	<script type="text/javascript">
	var clearZeros = $(".clearZero");
	for (var i = 0; i < clearZeros.length; i++) {
		$(clearZeros[i]).html(parseFloat($(clearZeros[i]).html()));
	}
	</script>
</body>
</html>