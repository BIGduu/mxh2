<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-store,no-cache">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>订单</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css"/>
        <script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
        <style type="text/css">
        	.merchandise {
        		position: relative;
        		margin: 15px 5px;
        		padding-left: 100px;
        		height: 100px;
        		font-size: 12px;
        		border-bottom: 1px solid #000;
        	}
        	
        	.merchandise img{
        		position: absolute;
			    top: 0;
			    left: 0;
			    width: 80px;
			    height: 80px;
			    border-radius: 6px;
			    
			}
			
			.merchandise p{
        		overflow: hidden;
        		margin-bottom: 5px;
        		height: 20px;
        		line-height: 20px;
			}
			
			.merchandise p span{
        		float: left;
        		display: block;
        		width: 50%;
			}

			.merchandise .price {
        		float: right;
        		width: 50%;
        		height: 20px;
			}
			
			.merchandise .price span {
        		width: 20px;
        		height: 20px;
        		display: inline-block;
        		border: 1px solid #000;
        		box-sizing: border-box;
        		text-align: center;
				line-height: 15px;
				margin: 0 3px;        	
				float: right;
				font-size: 16px;
				cursor: pointer;
			}
			
			.merchandise input {
        		width: 40px;
        		height: 20px;
        		line-height: 20px;
        		outline: none;
        		float: right;
        		box-sizing: border-box;
				text-align: center;
				
			}
			
			#footer-but{
			    position: fixed;
			    left: 0;
			    right: 0;
			    bottom: 0;
			    max-width: 768px;
			    margin: 0 auto;
			    height: 50px;
			    border-top: 1px solid #e8e8e8;
			    padding: 0px 12px;
			    box-sizing: border-box;
			    background-color: #fff;
			    z-index: 10000;
    		}

			#footer-but p{
			    height: 50px;
			    line-height: 50px;
    		}
    		
    		.payment-but {
			    display: block;
			    width: 130px;
			    height: 50px;
			    line-height: 50px;
			    text-align: center;
			    position: absolute;
			    top: 0;
			    right: 0;
			    color: #fff;
			    font-size: 16px;
			    background-color: #ff6600;
			}
        </style>
    </head>
    <body>
    	<div class="merchandiseList">
    		<c:forEach items="${ merchandiseList}" var="merchandise">
	    		<div class="merchandise">
	    			<p><span>名称:${merchandise.merchandiseName }</span><span>品牌:${merchandise.brandName }</span></p>
	    			<p><span>规格:${merchandise.specification }</span><span>单位:${merchandise.unit }</span></p>
	    			<p><span>单价:${merchandise.unitPrice }</span></p>
	    			<div class="price">数量 <span class="add">+</span><input class="number" type="number" data-price="${merchandise.unitPrice }" data-merchandise-id="${merchandise.id }" value="0"><span class="subtract">-</span></div>
	    		</div>
    		</c:forEach>
    	</div>
    	
    	<footer id="footer-but">
			<div class="footer-wrap">
				<p>总计:<span id="totalAmount">0元</span></p>
				<a href="javascript:;" class="payment-but">提交</a>
			</div>
		</footer>
    </body>
    
    <script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
   	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
   	<script type="text/javascript">
    	
    	$(function(){
    		
			$(".add").on("click", function() {
				var $input = $(this).siblings("input");
				var val = $input.val() || 0;
				$input.val(++val);
				calculation()
			});
			
			$(".subtract").on("click", function() {
				var $input = $(this).siblings("input");
				var val = $input.val() || 0;
				if(val > 0) {
					$input.val(--val);
				}
				calculation()
			});
			
			$(".number").keyup(function(){
				calculation();
			});
			
			$(".payment-but").click(function() {
				var $numbers = $(".number");
				layer.open({
        			type: 1,
        			title: '请输入送货地址',
        			content: '<div id="promptDiv"><input class="weui-input" type="text" value=""></div>',
        			style: 'width:80%;',
        			btn: ['确认', '取消'],
        			yes: function(index) {
						var receivingAddress = $("input", "#promptDiv").val();
      			      	layer.close(index);
      			      	if(receivingAddress == null || receivingAddress.trim() == "") {
        	        		return mytips("送货地址不能为空");
        	        	}
      			      	var orderInfos = [];
	      				for (var i = 0; i < $numbers.length; i++) {
	      					if($numbers[i].value && $numbers[i].dataset.price) {
	      						var orderInfo = {};
	      						orderInfo.merchandiseId = $numbers[i].dataset.merchandiseId;
	      						orderInfo.number = $numbers[i].value;
	      						orderInfos.push(orderInfo);
	      					}
	      				}
	      				if(orderInfos.length == 0) {
	      					return mytips("请先选择商品");
	      				}
	      				$.post("${pageContext.request.contextPath}/web/orderAdd", {receivingAddress:receivingAddress, orderInfoStr: JSON.stringify(orderInfos)}, function() {
	      					window.location.href="${pageContext.request.contextPath}/web/orderListPage?type=1";
	      				});
        			}
				});
			});
			
			function calculation() {
				var $numbers = $(".number");
				var totalAmount = 0;
				for (var i = 0; i < $numbers.length; i++) {
					if($numbers[i].value && $numbers[i].dataset.price) {
						totalAmount += parseInt($numbers[i].value) * $numbers[i].dataset.price;
					}
				}
				$("#totalAmount").html(totalAmount.toFixed(2) + "元");
			}
    	});
    	</script>
</html>
