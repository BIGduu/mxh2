<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>订单详情</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mescroll.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/weixin/css/swiper.min.css"/>
        <script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
        <style type="text/css">
        	.images {
        		overflow: hidden;
        	}
        	
        	.images img {
	        	float: left;
			    margin-right: 9px;
			    margin-bottom: 9px;
			    width: 79px;
			    height: 79px;
		    }
		    
		    .imageShow {
		    	position:fixed; left:0; top:0; width:100%; height:100%; border: none; -webkit-animation-duration: .5s; animation-duration: .5s;
				background-position: center;
			    background-repeat: no-repeat;
			    background-size: contain;
			    background-color: rgba(255,255,255,0.1);
		    }
		    
		     .borderTop {
        		border-top: 1px solid #e6e6e6;
        	}
        	.swiper-container {
			    width: 100%;
			    height: 100%;
			}
        	.swiper-wrap-none{
				position:fixed;
				top:0;
				left:0;
				bottom:0;
				width:100%;
				z-index:10000;
				background-color:rgba(0,0,0,0.7);
				display:none;
			} 
			.swiper-slide{
				background-repeat:no-repeat;
				background-size:contain;
				background-position:center;
				overflow:hidden;
			}
			.weui-form-preview__label, .weui-form-preview__bd{
				color:#000!important;
			}
        </style>
    </head>
    <body>
		<div class="weui-form-preview">
            <div class="weui-form-preview__bd">
            	<div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">送货地址</label>
                    <span class="weui-form-preview__value">${order.receivingAddress }</span>
                </div>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">订单状态</label>
                    <span class="weui-form-preview__value state">
                    	<c:choose>
							<c:when test="${order.state == 1 }">未审核</c:when>
		   					<c:when test="${order.state == 2 }">审核通过</c:when>
		   					<c:when test="${order.state == 0 }">异常</c:when>
			   				<c:when test="${order.state == 6 }">已送达</c:when>
			   				<c:when test="${order.state == 7 }">已完成</c:when>
						</c:choose>
					</span>
                </div>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">订单类型</label>
                    <span class="weui-form-preview__value">
						<c:choose>
		   					<c:when test="${order.orderType == 1 }"> 配送订单</c:when>
		   					<c:when test="${order.orderType == 2 }"> 退货订单</c:when>
		   					<c:when test="${order.orderType == 3 }"> 转货订单</c:when>
		   				</c:choose>
					</span>
                </div>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">创建时间</label>
                    <span class="weui-form-preview__value">${order.placeOrderTimeStr }</span>
                </div>
                <c:if test="${order.orderType != 1 }">
	                <div class="weui-form-preview__item">
	                    <label class="weui-form-preview__label">运费</label>
	                    <span class="weui-form-preview__value">￥${order.shippingcosts }</span>
	                </div>
                </c:if>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">步梯上楼费</label>
                    <span class="weui-form-preview__value">￥${order.upstaircosts }</span>
                </div>
                <div class="weui-form-preview__item borderTop">
                    <label class="weui-form-preview__label">货物</label>
                    <span class="weui-form-preview__value">数量</span>
                </div>
                <c:forEach items="${orderInfos }" var="orderInfo">
                	<div class="weui-form-preview__item">
	                    <label class="weui-form-preview__label">${orderInfo.merchandiseName }</label>
	                    <span class="weui-form-preview__value clearZero">${orderInfo.number }</span>
	                </div>
                </c:forEach>
            </div>
            <div class="images">
            	<c:forEach items="${images }" var="image">
            		<img src="${image }">
            	</c:forEach>
            </div>
            <c:if test="${order.state == 1 }">
            	<div class="weui-form-preview__ft">
					<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary cancel" data-order-id= "${order.id }">取消</button>
	            </div>
            </c:if>
            <c:if test="${order.state == 6 }">
            	<div class="weui-form-preview__ft">
	                <a class="weui-form-preview__btn weui-form-preview__btn_default abnormal" href="javascript:" data-order-id= "${order.id }">异常</a>
					<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary confirm" data-order-id= "${order.id }">确认</button>
	            </div>
            </c:if>
        </div>
        <section class="swiper-wrap-none">
			<div class="swiper-container">
			    <div class="swiper-wrapper">
			    <c:forEach items="${images }" var="img">
				    	<div class="swiper-slide" style="background-image:url(${img })"></div>
	            	</c:forEach>
			    </div>
			    <!-- 如果需要分页器 -->
			    <div class="swiper-pagination"></div>
			</div>
	</section>
    </body>
    <script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/mescroll.min.js"></script>
   	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
   	<script src="${pageContext.request.contextPath}/assets/weixin/js/swiper.min.js"></script>
   	<script type="text/javascript">
    	
    	$(function() {
    		var pageii;
    		$(".images img").click(function() {
    			var index = $(this).index();
    			$(".swiper-wrap-none").show();
				var mySwiper = new Swiper ('.swiper-container', {
				    direction: 'horizontal', 
				    observer:true,
				    initialSlide: index,
				    // 如果需要分页器
				    pagination: '.swiper-pagination',
				    paginationType : 'custom',
					paginationCustomRender: function (swiper, current, total) {
					    return current + ' / ' + total;
					}
				});
				$(".swiper-wrap-none").bind('click',function() {
					$(this).hide();
					//mySwiper.destroy(false);
				});  
    		})
    		
    		$(".img").click(function() {
    			var str = '<div class="weui-gallery" id="gallery">' + 
	    						'<span class="weui-gallery__img" id="galleryImg" style="background-image:url('+ $(this).src +')"></span>'+
	    					'</div>';
    			pageii = layer.open({
    				type: 1,
    				content: str,
    				anim: 'up',
    				style: 'position:fixed; left:0; top:0; width:100%; height:100%; border: none; -webkit-animation-duration: .5s; animation-duration: .5s;'
       			});
    		});
    		
    		$("body").on("click", "#gallery", function() {
    			layer.close(pageii);
    		});
    		
    		$("body").on("click", ".confirm", function() {
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
	                		mytips("成功");
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
    		
    		$("body").on("click", ".cancel", function() {
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
        	                		mytips("取消成功");
        	                		history.back();
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
                                    mytips("异常已提交");
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
                );
            });
    		
    		var pageii;
    		$(".images").on("click", "img", function() {
    			pageii = layer.open({
    			    type: 1,
    			    content: '',
    			    anim: 'false',
    			    className: 'imageShow',
    			    style: 'background-image: url('+$(this).attr("src")+');'
				});
        	});

    		$("body").on("click", ".imageShow", function() {
    			layer.close(pageii);
        	});
    	});
    	
    	</script>
</html>
