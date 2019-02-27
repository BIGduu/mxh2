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
        <title>库存列表</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mescroll.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css">
        <style type="text/css">
        	#orderList .weui-form-preview {
        		margin: 10px 0;
        	}
        	#loadingToast {
        		display: none;
        	}
        	.borderTop {
        		border-top: 1px solid #666666;
        	}
        </style>
    </head>
    <body>
    	<div id = "mescroll" class="mescroll">
    		<div class="mescroll-bounce">
    			<div id="storageList">
    			
    			</div>
    		</div>
    	</div>
    	
    	<div id="loadingToast">
	        <div class="weui-mask_transparent"></div>
	        <div class="weui-toast">
	            <i class="weui-loading weui-icon_toast"></i>
	            <p class="weui-toast__content">数据加载中</p>
	        </div>
	    </div>
    </body>
    
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/mescroll.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
   	
   	<script type="text/javascript">
    	
    	$(function() {
    		var mescroll = new MeScroll("mescroll", {
				up: {
					clearEmptyId: "storageList",
					callback: getListData
				}
			});
    		
    		function getListData(page) {
    			page.size = 20;
    			getListDataFromNet(page.num, page.size, function(curPageData) {
    				
					mescroll.endSuccess(curPageData.length);
					//设置列表数据,因为配置了emptyClearId,第一页会清空dataList的数据,所以setListData应该写在最后;
					setListData(curPageData);
				}, function(){
					//联网失败的回调,隐藏下拉刷新和上拉加载的状态;
	                mescroll.endErr();
				});
    		}
    		function setListData(curPageData) {
    			var str = "";
    			for (var i = 0; i < curPageData.length; i++) {
    				var storage = curPageData[i];
    				
    				str += '<div class="weui-form-preview">'+
    					 			'<div class="weui-form-preview__bd">'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">商品名称</label>'+
    											'<span class="weui-form-preview__value">' + storage.merchandiseName + '</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">数量</label>'+
    										'<span class="weui-form-preview__value state">'+ storage.number +'</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">总价</label>'+
											'<span class="weui-form-preview__value">￥'+ storage.totalPrice +'</span>'+
										'</div>'+
    									'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">是否支付</label>'+
											'<span class="weui-form-preview__value">'+ isPay(storage.isPay) +'</span>'+
										'</div>'+
    									'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">类型</label>'+
											'<span class="weui-form-preview__value">'+ storageType(storage.type) +'</span>'+
										'</div>'+
    									'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">创建时间</label>'+
											'<span class="weui-form-preview__value">'+ storage.createTimeStr +'</span>'+
										'</div>'+
									'</div>'+
								'</div>';
    			}
    			$("#storageList").append(str);
    		}
        	
        	function getListDataFromNet(pageNum, pageSize, successCallback, errorCallback) {
                	$.ajax({
    	                type: 'GET',
    	                url: '${pageContext.request.contextPath}/web/storageList',
    	                data: {
    	                	page: pageNum,
    	                	pageSize: pageSize
    	                },
    	                dataType: 'json',
    	                success: function(data){
    	                	if(data.status= 200) {
    		                	successCallback(data.data.storages);
    	                	}
    	                },
    	                error: errorCallback
    	            });
    		}
        	
    	});
    	
    	</script>
</html>
