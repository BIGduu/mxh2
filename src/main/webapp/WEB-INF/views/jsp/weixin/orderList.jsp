<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>订单列表</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mescroll.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css"/>
        <script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
        <style type="text/css">
        	#orderList .weui-form-preview {
        		margin: 10px 0;
        	}
        	#loadingToast {
        		display: none;
        	}
        	.weui-form-preview__label, .weui-form-preview__bd{
				color:#000!important;
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
    <script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/mescroll.min.js"></script>
   	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/plugins/layer/layer.js"></script>
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
					setListData(curPageData);
				}, function(){
	                mescroll.endErr();
				});
    		}
    		function setListData(curPageData){
    			var str = "";
    			for (var i = 0; i < curPageData.length; i++) {
    				var order = curPageData[i];
    				
    				str += '<div class="weui-form-preview">'+
    					 			'<div class="weui-form-preview__bd" onclick="jump('+ order.id +')">'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">订单编号</label>'+
    											'<span class="weui-form-preview__value">' + order.orderCode + '</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">订单状态</label>'+
    										'<span class="weui-form-preview__value state">'+ getManOrderState(order.state) +'</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">创建时间</label>'+
    										'<span class="weui-form-preview__value">'+ order.placeOrderTimeStr +'</span>'+
    									'</div>';
					if(order.orderType != 1) {
						str += '<div class="weui-form-preview__item">'+
										'<label class="weui-form-preview__label">运费</label>'+
										'<span class="weui-form-preview__value">￥'+ order.shippingcosts +'</span>'+
									'</div>';
					}
					str += '</div>';
					if(order.state == 1) {
    					str += '<div class="weui-form-preview__ft">'+
						'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary cancel" data-order-id= "'+ order.id+'">取消</button>'+
					'</div>'
    				}
    				if(order.state == 6) {
    					str += '<div class="weui-form-preview__ft">'+
						'<a class="weui-form-preview__btn weui-form-preview__btn_default abnormal" href="javascript:" data-order-id= "'+ order.id+'">异常</a>'+
						'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary confirm" data-order-id= "'+ order.id+'">确认</button>'+
					'</div>'
    				}
    				str += '</div>';
    			}
    			$("#orderList").append(str);
    		}
        	
        	function getListDataFromNet(pageNum, pageSize, successCallback, errorCallback) {
                	$.ajax({
    	                type: 'GET',
    	                url: '${pageContext.request.contextPath}/web/orderList',
    	                data: {
    	                	page: pageNum,
    	                	pageSize: pageSize,
    	                	type: "${type}"
    	                },
    	                dataType: 'json',
    	                success: function(data){
    	                	if(data.status= 200) {
    		                	successCallback(data.data.orderList);
    	                	}
    	                },
    	                error: errorCallback
    	            });
    		}
    	});
    	
    	$("#orderList").on("click", ".confirm", function() {
    		var $loadingToast = $('#loadingToast');
    		$loadingToast.fadeIn(100);

    		var $this = $(this);
    		$.ajax({
                type: 'post',
                url: '${pageContext.request.contextPath}/web/confirmOrder',
                data: {
                	orderId: $this.data("orderId")
                },
                dataType: 'json',
                success: function(data){
                	if(data.status == 200) {
                		$this.parents(".weui-form-preview").remove();
                		mytips("修改成功");
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
    	});
    	
    	$("#orderList").on("click", ".cancel", function() {
    		var $this = $(this);
    		layer.open({
    		    content: '您确定要取消订单吗？',
    		    btn: ['确定', '取消'],
    		    yes: function(index){
    		    	layer.close(index);
    		    	var $loadingToast = $('#loadingToast');
    	    		$loadingToast.fadeIn(100);

    	    		$.ajax({
    	                type: 'post',
    	                url: '${pageContext.request.contextPath}/web/cancelOrder',
    	                data: {
    	                	orderId: $this.data("orderId")
    	                },
    	                dataType: 'json',
    	                success: function(data) {
    	                	if(data.status == 200) {
    	                		$this.parents(".weui-form-preview").remove();
    	                		mytips("取消成功");
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

    	$("#orderList").on("click", ".abnormal", function() {
            var $this = $(this);
            layer.prompt(
                {title:"填写异常原因",formType:2}, function (abnormalReason,index) {
                    layer.close(index);
                    var $loadingToast = $('#loadingToast');
                    $loadingToast.fadeIn(100);
                    console.log($this.data("orderId"))
                    $.ajax({
                        type: 'post',
                        url: '${pageContext.request.contextPath}/web/abnormalOrder',
                        data: {
                            orderId: $this.data("orderId"),
                            abnormalReason: abnormalReason
                        },
                        dataType: 'json',
                        success: function(data) {
                            if(data.status == 200) {
                                $(".state", $this.parents(".weui-form-preview")).html("异常");
                                $this.parent(".weui-form-preview__ft").remove();
                                $loadingToast.fadeOut(100);
                                mytips("异常已提交");
                            } else {
                                $loadingToast.fadeOut(100);
                                mytips(data.message);
                            }

                        },
                        error: function() {
                            $loadingToast.fadeOut(100);
                            mytips("网络异常请稍后再试");
                        }
                    });

                }
            );
    	});
    	
    	function jump(orderId) {
    		window.location.href = "${pageContext.request.contextPath}/web/orderDetail?orderId=" + orderId;
    	}
    	</script>
</html>
