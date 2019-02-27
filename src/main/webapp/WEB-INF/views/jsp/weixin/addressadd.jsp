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
	<title>提交订单</title>
	<link href="${pageContext.request.contextPath}/assets/weixin/css/pagecss.css" rel="stylesheet">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css">
</head>
<body>

	<form id="orderForm" action="" method="post" >
		<div class="weui-cells weui-cells_form">
			<div class="weui-cell weui-cell_select weui-cell_select-after">
				<div class="weui-cell__hd">
				    <label for="orderType" class="weui-label">订单类型</label>
				</div>
				<div class="weui-cell__bd">
			    	<select class="weui-select" id="orderType" name="orderType">
		            	<option value="1">配货订单</option>
		            	<option value="2">退货订单</option>
		            </select>
			    </div>
			</div>
		
        	<div class="weui-cell">
                <div class="weui-cell__hd"><label class="weui-label" for="receivingAddress">送货地址</label></div>
                <div class="weui-cell__bd">
                    <input class="weui-input" type="text" readonly="readonly" id="receivingAddress" name="receivingAddress" placeholder="请选择工地" value="">
                    <input type="hidden" readonly="readonly" id="receivingBuildingId" name="receivingBuildingId" value="">
                </div>
                <div class="weui-cell__ft">
                    <a id="showIOSActionSheet" class="weui-vcode-btn" href="javascript:;">选择工地</a>
                </div>
            </div>
            
            <div class="weui-cell weui-cell_switch">
                <div class="weui-cell__bd">是否步梯上楼</div>
                <div class="weui-cell__ft">
                    <input class="weui-switch" name="upstairs" type="checkbox">
                </div>
            </div>
		</div>
		<div class="weui-btn-area">
		    <a class="weui-btn weui-btn_primary" href="javascript:" id="submitBtn" onclick="submitOrder(this)">提交</a>
		    <a href="javascript:;" class="weui-btn weui-btn_primary weui-btn_loading" id="submitBtnLoad" style="display:none;"><i class="weui-loading"></i>提交</a>
		</div>
		
		<div class="weui-mask" id="iosMask"></div>
		<div class="weui-actionsheet" id="iosActionsheet">
            <div class="weui-actionsheet__menu">
            	<c:forEach items="${ buildingList}" var="building">
            		<div class="weui-actionsheet__cell address" data-buildingid="${building.id}">${ building.buildingName}</div>
            	</c:forEach>
            </div>
            <div class="weui-actionsheet__action">
                <div class="weui-actionsheet__cell" id="iosActionsheetCancel">取消</div>
            </div>
        </div>
    </form>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/jquery-1.7.1.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/weixin/js/product.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>

	<script type="text/javascript">
		var $iosActionsheet = $('#iosActionsheet');
	    var $iosMask = $('#iosMask');
	    $iosMask.fadeOut();
	    
	    function hideActionSheet() {
	        $iosActionsheet.removeClass('weui-actionsheet_toggle');
	        $iosMask.fadeOut(200);
	    }
	
	    $iosMask.on('click', hideActionSheet);
	    $('#iosActionsheetCancel').on('click', hideActionSheet);
	    $("#showIOSActionSheet").on("click", function(){
	        $iosActionsheet.addClass('weui-actionsheet_toggle');
	        $iosMask.fadeIn(200);
	    });
	    
	    $(".address").on("click",function() {
	    	$("#receivingAddress").val($(this).html());
	    	$("#receivingBuildingId").val($(this).data("buildingid"));
	    	hideActionSheet();
	    });

	</script>
</body>
</html>
