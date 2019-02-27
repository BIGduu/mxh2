<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-store,no-cache">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>购物车</title>
	<link href="${pageContext.request.contextPath}/assets/weixin/css/pagecss.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css">
</head>

<body>
	<section>
		<div style="width: 100%; float: left" class="deliverlist"></div>
		<div style="clear:both;height:0;overflow:hidden;"></div>
	</section>
	<div style="height: 120px;"></div>
	<section>
		<div class="card_footer">
			<div class="card_footer_pirce" style="border-top: 1px solid #eee;">
				<span style="color: rgb(33, 33, 33); padding-left: 20px;"> 总价：</span>￥<span class="all_price">0</span>
				<span class="operationMyDetailBut" onclick="cardGoPay()" style="float: right; line-height: 40px;">去结算</span>
			</div>
			<div class="card_footer_menu">
				<img src="${pageContext.request.contextPath}/assets/weixin/image/img-sc_03.png" onclick="location.href='${pageContext.request.contextPath}/web/product'">
				<img src="${pageContext.request.contextPath}/assets/weixin/image/img-card-foot_selected.png" class="foot_card" onclick="location.href='${pageContext.request.contextPath}/web/shoppingcard'" />
				<img src="${pageContext.request.contextPath}/assets/weixin/image/img-my.png" onclick="location.href='${pageContext.request.contextPath}/web/my'">
				<div class="img_foot_card_num" style="position: absolute;">0</div>
			</div>

		</div>
	</section>
	<div class="dialog_mask"></div>
	<div class="dialog_mask_msg">
		<div class="dialog_mask_msg_top"></div>
		<div class="dialog_mask_msg_operation"></div>
	</div>
	
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/jquery-1.7.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/product.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
	<script>
		$(function() {
			changeSize();
			bindShopCardList();
		});
	</script>
</body>
</html>
