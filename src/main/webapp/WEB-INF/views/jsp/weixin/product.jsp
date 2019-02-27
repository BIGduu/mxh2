<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="Expires" content="0">
	<meta http-equiv="Pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-store,no-cache">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title></title>
	<link href="${pageContext.request.contextPath}/assets/weixin/css/pagecss.css" rel="stylesheet" />
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/weixin/css/css1.css" type="text/css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/assets/weixin/css/swipe.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css">
</head>
<body>
	<div class="content-body" id="content_product">
		<section>

			<div class="addWrap"
				style="padding: 8px 8px 0; box-sizing: border-box;">
				<div class="swipe" id="mySwipe">
				</div>
				<ul id="position">
				</ul>
			</div>

		</section>
		<section>

			<div style="width: 100%; float: left; padding-bottom: 80px" class="commodity_list"></div>

		</section>
	</div>

	<section>
		<div class="footer">
			<img src="${pageContext.request.contextPath}/assets/weixin/image/img-commodity.png" onclick="location.href='${pageContext.request.contextPath}/web/product'">
			<img src="${pageContext.request.contextPath}/assets/weixin/image/img-card-foot.png" class="foot_card" onclick="location.href='${pageContext.request.contextPath}/web/shoppingcard'">
			<img src="${pageContext.request.contextPath}/assets/weixin/image/img-my.png" onclick="location.href='${pageContext.request.contextPath}/web/my'">
			<div class="img_foot_card_num" style="position: absolute; top: 0">0</div>
		</div>
	</section>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/jquery-1.7.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/menuswiper.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/swipe.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/product.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
	<script>
		$(function() {
			bindCommodityType();
			changeSize();
			bindCommodityAd();
			bindCommodityList();
		});
</script>
</body>

</html>
