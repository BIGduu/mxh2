<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-store,no-cache">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>我</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css">
	<link href="${pageContext.request.contextPath}/assets/weixin/css/pagecss.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css"/>
</head>
<body>

	<div class="my_top">
		<div class="my_top_detail">
			<c:if test="${sessionScope.type == 1 || sessionScope.type == 2}">
				<div class="my_top_detail_name">姓名:${sessionScope.user.username }</div>
				<div class="my_top_detail_name">电话:${sessionScope.user.telephone }</div>
				<div class="my_top_detail_name">部门:${sessionScope.user.departmentName }</div>
				<div class="my_top_detail_name">门店:${sessionScope.user.storesName }</div>
			</c:if>
			<c:if test="${sessionScope.type == 2}">
				<div class="my_top_detail_name"  style="padding-bottom: 10px;">
					<c:if test="${sessionScope.sign == 0}">
						<a href="javascript:;" id="sign" class="weui-btn weui-btn_mini weui-btn_primary">签到</a>
					</c:if>
					<c:if test="${sessionScope.sign == 1}">
						<a href="javascript:;" class="weui-btn weui-btn_mini weui-btn_primary weui-btn_disabled">已签到</a>
					</c:if>
				</div>
			</c:if>
			<c:if test="${sessionScope.type == 3}">
				<div class="my_top_detail_name">姓名:${sessionScope.admin.username }</div>
				<div class="my_top_detail_name">电话:${sessionScope.admin.telephone }</div>
				<c:if test="${ not empty sessionScope.admin.storesName }">
					<div class="my_top_detail_name">门店:${sessionScope.admin.storesName }</div>
				</c:if>
			</c:if>
		</div>
	</div>
	
	<c:choose>
		<c:when test="${sessionScope.type == 1 }">
			<div class="myitem_row">
				<div class="item_row" onclick="location.href='/mxh/web/orderListPage?type=1'">
					<img src="${pageContext.request.contextPath}/assets/weixin/image/order.png" class="item_row_left_img">
					<span class="item_row_title">我的订单</span>
					<img src="${pageContext.request.contextPath}/assets/weixin/image/img-right_arrow.png" class="item_row_right_img">
				</div>
			</div>
		
			<div class="myitem_row">
				<div class="item_row" onclick="location.href='/mxh/web/orderListPage?type=2'">
					<img src="${pageContext.request.contextPath}/assets/weixin/image/order.png" class="item_row_left_img" />
					<span class="item_row_title">历史订单</span>
					<img src="${pageContext.request.contextPath}/assets/weixin/image/img-right_arrow.png" class="item_row_right_img">
				</div>
			</div>
		</c:when>
		<c:when test="${sessionScope.type == 2 }">
			<div class="myitem_row">
				<div class="item_row" onclick="location.href='/mxh/web/deliveryOrderListPage'">
					<img src="${pageContext.request.contextPath}/assets/weixin/image/order.png" class="item_row_left_img">
					<span class="item_row_title">送货订单</span>
					<img src="${pageContext.request.contextPath}/assets/weixin/image/img-right_arrow.png" class="item_row_right_img">
				</div>
			</div>
		</c:when>
		
		<c:when test="${sessionScope.type == 3 }">
			<div class="myitem_row">
				<div class="item_row" onclick="location.href='/mxh/web/outofStoragePage'">
					<img src="${pageContext.request.contextPath}/assets/weixin/image/order.png" class="item_row_left_img">
					<span class="item_row_title">出入库管理</span>
					<img src="${pageContext.request.contextPath}/assets/weixin/image/img-right_arrow.png" class="item_row_right_img">
				</div>
			</div>
			<div class="myitem_row">
				<div class="item_row" onclick="location.href='/mxh/web/storageListPage'">
					<img src="${pageContext.request.contextPath}/assets/weixin/image/order.png" class="item_row_left_img">
					<span class="item_row_title">库存管理</span>
					<img src="${pageContext.request.contextPath}/assets/weixin/image/img-right_arrow.png" class="item_row_right_img">
				</div>
			</div>
		</c:when>
	</c:choose>
	
	<div class="myitem_row">
		<div class="item_row">
			<img src="${pageContext.request.contextPath}/assets/weixin/image/phone.png" class="item_row_left_img" /><span
				class="item_row_title">客服电话</span> <a href="tel:13308408517"><span class="item_row_phone">13308408517</span></a>
		</div>
	</div>
	
	<c:if test="${sessionScope.type == 3 }">
		<div class="weui-btn-area">
		    <a href="${pageContext.request.contextPath}/web/logout" class="weui-btn weui-btn_default">注销登录</a>
		</div>
	</c:if>

	<c:if test="${sessionScope.type == 1 }">
		<section>
			<div class="footer">
				<img src="${pageContext.request.contextPath}/assets/weixin/image/img-sc_03.png" onclick="location.href='${pageContext.request.contextPath}/web/product'">
				<img src="${pageContext.request.contextPath}/assets/weixin/image/img-card-foot.png" class="foot_card" onclick="location.href='${pageContext.request.contextPath}/web/shoppingcard'">
				<img src="${pageContext.request.contextPath}/assets/weixin/image/myred.png" onclick="location.href='location.href='${pageContext.request.contextPath}/web/my'" />
				<div class="img_foot_card_num" style="position: absolute;"></div>
			</div>
		</section>
	</c:if>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/jquery-1.7.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/product.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
	<script>
		$(function() {
			changeSize();
			getCount();
			$("#sign").click(function() {
				var $this = $(this);
				if(!$this.hasClass("weui-btn_disabled")) {
					$.post("/mxh/web/signin",{}, function(data){
						if(data.status == 200) {
							$this.removeAttr("id");
							$this.addClass("weui-btn_disabled");
							$this.html("已签到");
							mytips("签到成功");
						} else {
							mytips(data.message);
						}
					});
				}
			});
		});
	</script>
</body>
</html>
