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
        <title>订单列表</title>
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
    			<div id="orderList">
    			
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
    <script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/mescroll.min.js"></script>
   	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
   	<script type="text/javascript">
    	
    	$(function() {
    		var mescroll = new MeScroll("mescroll", {
				up: {
					clearEmptyId: "orderList",
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
    				var order = curPageData[i];
    				
    				str += '<div class="weui-form-preview">'+
    					 			'<div class="weui-form-preview__bd"  onclick="jump('+ order.id +')">'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">送货地址</label>'+
    											'<span class="weui-form-preview__value">' + order.receivingAddress + '</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">订单状态</label>'+
    										'<span class="weui-form-preview__value state">'+ getStoOrderState(order.state) +'</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">订单类型</label>'+
											'<span class="weui-form-preview__value">'+ getOrderType(order.orderType) +'</span>'+
										'</div>'+
    									'<div class="weui-form-preview__item borderTop">'+
											'<label class="weui-form-preview__label">货物</label>'+
											'<span class="weui-form-preview__value">数量</span>'+
										'</div>';
					
					for (var j = 0; j < order.orderInfos.length; j++) {
						
						var orderInfo = order.orderInfos[j];
						if(orderInfo) {
							str += '<div class="weui-form-preview__item">'+
									'<label class="weui-form-preview__label">'+orderInfo.merchandiseName+'</label>'+
									'<span class="weui-form-preview__value">'+ orderInfo.number +'</span>'+
								'</div>';
						}
					}
					str += '</div>';
					
    				if(order.orderType == 1 && order.state == 4) {
    					str += '<div class="weui-form-preview__ft">'+
									'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary library" data-order-id= "'+ order.id+'">确认出库</button>'+
								'</div>';
    				} else if(order.orderType == 2 && order.state == 6) {
    					str += '<div class="weui-form-preview__ft">'+
									'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary storage" data-order-id= "'+ order.id+'">确认入库</button>'+
								'</div>';
			    	}
    				str += '</div>';
    			}
    			$("#orderList").append(str);
    		}
        	
        	function getListDataFromNet(pageNum, pageSize, successCallback, errorCallback) {
                	$.ajax({
    	                type: 'GET',
    	                url: '${pageContext.request.contextPath}/web/outofStorage',
    	                data: {
    	                	page: pageNum,
    	                	pageSize: pageSize
    	                },
    	                dataType: 'json',
    	                success: function(data){
    	                	if(data.status= 200) {
    		                	successCallback(data.data.deliveryOrderList);
    	                	}
    	                },
    	                error: errorCallback
    	            });
    		}
        	
        	$("#orderList").on("click", ".library", function() {
        		var $this = $(this);
        		layer.open({
        		    content: '确定出库',
        		    btn: ['确定', '取消'],
        		    yes: function(index) {
        		    	layer.close(index);
        		    	var $loadingToast = $('#loadingToast');
                		$loadingToast.fadeIn(100);
                		$.ajax({
                            type: 'post',
                            url: '${pageContext.request.contextPath}/web/confirmLibrary',
                            data: {
                            	deliveryOrderId: $this.data("orderId")
                            },
                            dataType: 'json',
                            success: function(data) {
                            	if(data.status == 200) {
                            		$(".state", $this.parents(".weui-form-preview")).html("已出库");
                            		$this.parent(".weui-form-preview__ft").remove();
                            		mytips("出库成功");
                            	} else {
                            		mytips(data.message);
                            	}
                            	$loadingToast.fadeOut(100);
                            },
                            error: function() {
                            	$loadingToast.fadeOut(100);
                            	mytips("网络异常请稍后再试");
                            }
                        });
        		    }
        		});
        	});
        	
        	$("#orderList").on("click", ".storage", function() {
        		var $this = $(this);
        		layer.open({
        		    content: '确定入库',
        		    btn: ['确定', '取消'],
        		    yes: function(index) {
        		    	layer.close(index);
        		    	var $loadingToast = $('#loadingToast');
        			    $loadingToast.fadeIn(100);
                  		$.ajax({
                              type: 'post',
                              url: '${pageContext.request.contextPath}/web/confirmStorage',
                              data: {
                              	deliveryOrderId: $this.data("orderId")
                              },
                              dataType: 'json',
                              success: function(data) {
                              	if(data.status == 200) {
                              		$(".state", $this.parents(".weui-form-preview")).html("已入库");
                              		$this.parent(".weui-form-preview__ft").remove();
                              		mytips("入库成功");
                              	} else {
                              		mytips(data.message);
                              	}
                              	$loadingToast.fadeOut(100);
                              },
                              error: function() {
                              	$loadingToast.fadeOut(100);
                              	mytips("网络异常请稍后再试");
                              }
                    	});
					}
				});
        	});

    	});
    	
    	function jump(deliveryOrderId) {
    		window.location.href = "${pageContext.request.contextPath}/web/deliveryOrderDetail?deliveryOrderId=" + deliveryOrderId;
    	}
    	</script>
</html>
