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
.swiper-pagination-custom{color:#fff;}
        </style>
    </head>
    <body>
		<div class="weui-form-preview">
            <div class="weui-form-preview__bd">
            	<div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">送货地址</label>
                    <span class="weui-form-preview__value">${deliveryOrder.receivingAddress }</span>
                </div>
            	<div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">项目经理姓名</label>
                    <span class="weui-form-preview__value">${deliveryOrder.managerName }</span>
                </div>
            	<div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">项目经理电话</label>
                    <span class="weui-form-preview__value"><a href="tel:${deliveryOrder.managerTel }">${deliveryOrder.managerTel }</a></span>
                </div>
            	<div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">门店</label>
                    <span class="weui-form-preview__value">${deliveryOrder.storesName }</span>
                </div>
                <c:if test="${sessionScope.type == 3 }">
	            	<div class="weui-form-preview__item">
	                    <label class="weui-form-preview__label">司机姓名</label>
	                    <span class="weui-form-preview__value">${deliveryOrder.deliveryName }</span>
	                </div>
                </c:if>
            	<div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">是否上楼订单</label>
                    <span class="weui-form-preview__value">
						<c:choose>
							<c:when test="${deliveryOrder.upstairs == 1}">是</c:when>
							<c:otherwise>否</c:otherwise>
						</c:choose>
					</span>
                </div>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">订单状态</label>
                    <span class="weui-form-preview__value state">
                    	<c:choose>
							<c:when test="${deliveryOrder.state == 3 }">待确认</c:when>
		   					<c:when test="${deliveryOrder.state == 4 }">待出库</c:when>
		   					<c:when test="${deliveryOrder.state == 5 }">运送中</c:when>
			   				<c:when test="${deliveryOrder.state == 6 }">已送达</c:when>
			   				<c:when test="${deliveryOrder.state == 7 }">已完成</c:when>
			   				<c:when test="${deliveryOrder.state == 8 }">待确认</c:when>
			   				<c:when test="${deliveryOrder.state == 9 }">已拒绝</c:when>
			   				<c:when test="${deliveryOrder.state == 10 }">装车中</c:when>
						</c:choose>
					</span>
                </div>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">订单类型</label>
                    <span class="weui-form-preview__value">
						<c:choose>
		   					<c:when test="${deliveryOrder.orderType == 1 }"> 配送订单</c:when>
		   					<c:when test="${deliveryOrder.orderType == 2 }"> 退货订单</c:when>
		   					<c:when test="${deliveryOrder.orderType == 3 }"> 转货订单</c:when>
		   				</c:choose>
					</span>
                </div>
                <div class="weui-form-preview__item">
                    <label class="weui-form-preview__label">运费</label>
                    <span class="weui-form-preview__value">￥${deliveryOrder.shippingcosts }</span>
                </div>
                <div class="weui-form-preview__item borderTop">
                    <label class="weui-form-preview__label">货物</label>
                    <span class="weui-form-preview__value">数量</span>
                </div>
				<form id="changeNumber">
                <c:forEach items="${deliveryOrder.orderInfos }" var="orderInfo">
                	<div class="weui-form-preview__item">
	                    <label class="weui-form-preview__label">${orderInfo.merchandiseName }</label>
	                    <span class="weui-form-preview__value"><input name="${orderInfo.merchandiseName }" value="${orderInfo.number}"></span>
	                </div>
                </c:forEach>
				</form>
            </div>
            <div class="images">
            	<c:forEach items="${images }" var="image">
            		<img src="${image }">
            	</c:forEach>
            </div>
            <c:if test="${sessionScope.type == 2}">
	            <c:choose>
	            	<c:when test="${deliveryOrder.state == 3 }">
		            	<div class="weui-form-preview__ft">
							<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary accept" data-order-id="${deliveryOrder.id }">接受</button>
			            </div>
	            	</c:when>
	            	<c:when test="${deliveryOrder.state == 5 }">
		            	<div class="weui-form-preview__ft">
							<%-- <a class="weui-form-preview__btn weui-form-preview__btn_default refuse" href="javascript:" data-order-id= "${deliveryOrder.id }">意外</a> --%>
							<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary sentto" data-order-id= "${deliveryOrder.id }"  data-upstairs="${deliveryOrder.upstairs }">确认送达</button>
			            </div>
	            	</c:when>
	            	<c:when test="${deliveryOrder.state == 10 }">
		            	<div class="weui-form-preview__ft">
							<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary shipment" data-order-id="${deliveryOrder.id }">装车完成</button>
			            </div>
	            	</c:when>
	            </c:choose>
            </c:if>
            <c:if test="${sessionScope.type == 3}">
            	<c:choose>
	            	<c:when test="${deliveryOrder.orderType == 1 && deliveryOrder.state == 4}">
		            	<div class="weui-form-preview__ft">
							<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary accept" data-order-id="${deliveryOrder.id }">确认出库</button>
			            </div>
	            	</c:when>
	            	<c:when test="${deliveryOrder.orderType == 2 && deliveryOrder.state == 8 }">
		            	<div class="weui-form-preview__ft">
							<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary storage" data-order-id= "${deliveryOrder.id }"  data-upstairs="${deliveryOrder.upstairs }">确认入库</button>
			            </div>
	            	</c:when>
	            </c:choose>
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
   	<script src="${pageContext.request.contextPath}/assets/js/swiper.min.js"></script>
	<script src="${pageContext.request.contextPath}/assets/js/jquery-1.7.2.min.js"></script>
   	<script type="text/javascript">
    	
    	$(function() {
    		
    		var u = navigator.userAgent;  
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端  
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端  
            
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
// 					mySwiper.destroy(false);
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
    		
//     		$(".images").on("click", "img", function() {
//     			pageii = layer.open({
//     			    type: 1,
//     			    content: '',
//     			    anim: 'false',
//     			    className: 'imageShow',
//     			    style: 'background-image: url('+$(this).attr("src")+');'
// 				});
//         	});

//     		$("body").on("click", ".imageShow", function() {
//     			layer.close(pageii);
//         	});
    		
    		$("body").on("click", ".accept", function() {
        		var $loadingToast = $('#loadingToast');
        		$loadingToast.fadeIn(100);
        		
        		var $this = $(this);
        		$.ajax({
                    type: 'post',
                    url: '${pageContext.request.contextPath}/web/deliveryAccept',
                    data: {
                    	deliveryOrderId: $this.data("orderId")
                    },
                    dataType: 'json',
                    success: function(data) {
                    	if(data.status == 200) {
                    		location.reload(true);  
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

        	$("body").on("click", ".shipment", function() {
        		var $loadingToast = $('#loadingToast');
        		$loadingToast.fadeIn(100);
        		
        		var $this = $(this);
        		$.ajax({
                    type: 'post',
                    url: '${pageContext.request.contextPath}/web/shipment',
                    data: {
                    	deliveryOrderId: $this.data("orderId")
                    },
                    dataType: 'json',
                    success: function(data) {
                    	if(data.status == 200) {
                    		location.reload(true);
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
        	
        	$("body").on("click", ".refuse", function() {
        		var $this = $(this);
        		layer.open({
        			type: 1,
        			title: '请输入理由?',
        			content: '<div id="promptDiv"><input class="weui-input" type="text" value=""></div>',
        			style: 'width:80%;',
        			btn: ['确认', '取消'],
        			yes: function(index) {
						var reason = $("input", "#promptDiv").val();
      			      	layer.close(index);
      			      	if(reason == null || reason.trim() == "") {
        	        		return mytips("理由不能为空");
        	        	}
      			      	var $loadingToast = $('#loadingToast');
      			      	$loadingToast.fadeIn(100);
	              		$.ajax({
	                          type: 'post',
	                          url: '${pageContext.request.contextPath}/web/deliveryRefuse',
	                          data: {
	                          	deliveryOrderId: $this.data("orderId"),
	                          	reason: reason
	                          },
	                          dataType: 'json',
	                          success: function(data) {
	                          	if(data.status == 200) {
	                          		location.reload(true);
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

        	var $clickSentto;
        	$("body").on("click", ".sentto", function() {
        		var $this = $(this);
        		$clickSentto = $this;
        		var str = "";
        		if($this.data("upstairs") == 1) {
        			str += '<div class="weui-cells weui-cells_form">'+
								'<div class="weui-cell">'+
					                '<div class="weui-cell__hd"><label class="weui-label">楼层</label></div>'+
					                '<div class="weui-cell__bd">'+
					                    '<input class="weui-input" type="text" id="upstairs" name="upstairs" placeholder="请输入楼层" value="">'+
					                '</div>'+
				                '</div>'+
				                '<div class="weui-cell">'+
				                '<div class="weui-cell__hd"><label class="weui-label">上楼人</label></div>'+
				                '<div class="weui-cell__bd">'+
				                    '<input class="weui-input" type="text" id="upstairPersion" name="upstairPersion" placeholder="请输入上楼人" value="">'+
				                '</div>'+
			                '</div>'+
				            '</div>';
        		}
        			str += '<div class="page__bd">'+
						        	'<div class="weui-gallery" id="gallery">'+
						        		'<span class="weui-gallery__img" id="galleryImg"></span>'+
						        		'<div class="weui-gallery__opr">'+
						        			'<a href="javascript:" class="weui-gallery__del">'+
						        				'<i class="weui-icon-delete weui-icon_gallery-delete"></i>'+
						        			'</a>'+
						        		'</div>'+
						        	'</div>'+
						        	'<div class="weui-cells weui-cells_form">'+
						        		'<div class="weui-cell">'+
						        			'<div class="weui-cell__bd">'+
						        				'<div class="weui-uploader">'+
						        					'<div class="weui-uploader__hd">'+
						        						'<p class="weui-uploader__title">图片上传</p>'+
						        						'<div class="weui-uploader__info">0/9</div>'+
						        					'</div>'+
						        					'<div class="weui-uploader__bd">'+
						        						'<ul class="weui-uploader__files" id="uploaderFiles">'+
						        						'</ul>'+
						        						'<div class="weui-uploader__input-box">'+
						        							'<input id="uploaderInput" class="weui-uploader__input" type="button" accept="image/*" multiple="">'+
						        						'</div>'+
						        					'</div>'+
						        				'</div>'+
						        			'</div>'+
						        		'</div>'+
						        	'</div>'+
						        '</div>' +
						        '<a href="javascript:;" class="weui-btn weui-btn_primary" id="submitBtn">提交</a>'+
						        '<a href="javascript:;" class="weui-btn weui-btn_primary weui-btn_loading" id="submitBtnLoad" style="display:none;"><i class="weui-loading"></i>提交中</a>'+
     							'<a href="javascript:;" class="weui-btn weui-btn_default" id="cancelBtn">取消</a>';
				deliveryOrderId = $this.data("orderId");
				localIdList = [];
        		pageii = layer.open({
					type: 1,
					content: str,
					anim: 'up',
					style: 'position:fixed; left:0; top:0; width:100%; height:100%; border: none; -webkit-animation-duration: .5s; animation-duration: .5s;'
       			});
        	});
        	
        	$("body").on("click", "#submitBtn", function() {
        		var $this = $(this);
        		var $cancelBtn = $this.siblings("#cancelBtn");
        		var $submitBtnLoad = $this.siblings("#submitBtnLoad");
        		if(localIdList.length == 0) {
        			return mytips("请选择图片");
        		}
        		$this.css("display", "none");
        		$submitBtnLoad.css("display", "block");
        		$cancelBtn.css("display", "none");
        		uploadImage(0);
        	});
				
        	var serverIds = [];
        	function uploadImage(i) {
        		wx.uploadImage({
        		    localId: localIdList[i], // 需要上传的图片的本地ID，由chooseImage接口获得
        		    isShowProgressTips: 0, // 默认为1，显示进度提示
        		    success: function (res) {
        		    	serverIds.push(res.serverId);
        		    	if(serverIds.length == localIdList.length) {
        		    		$.ajax({
        	                    type: 'post',
        	                    url: '${pageContext.request.contextPath}/web/deliverySubmit',
        	                    data: {
        	                    	deliveryOrderId: deliveryOrderId,
        	                    	serverIds: serverIds.join(","),
        	                    	upstairs: $("#upstairs").val(),
        	                    	upstairPersion: $("#upstairPersion").val()
        	                    },
        	                    dataType: 'json',
        	                    success: function(data) {
        	                    	if(data.status == 200) {
        	                    		$(".state", $clickSentto.parents(".weui-form-preview")).html("已送达");
        	                    		$clickSentto.parent(".weui-form-preview__ft").remove();
        	                    		layer.close(pageii);
        	                    		deliveryOrderId = "";
        	            				localIdList = [];
        	                    		mytips("成功");
        	                    	} else {
        	                    		$this.css("display", "block");
        	                    		$submitBtnLoad.css("display", "none");
        	                    		$cancelBtn.css("display", "block");
        	                    		mytips(data.message);
        	                    	}
        	                    },
        	                    error: function() {
        	                    	$this.css("display", "block");
    	                    		$submitBtnLoad.css("display", "none");
    	                    		$cancelBtn.css("display", "block");
            	                    	mytips("网络异常请稍后再试");
            	                    }
            	                });
            		    	} else {
            		    		uploadImage(++i);
            		    	}
            		    }
            		});
        	}
        	
        	$("body").on("click", "#cancelBtn", function() {
        		layer.close(pageii);
        	})
        	
        	var localIdList = [];
        	var deliveryOrderId = "";
        	$("body").on("click", "#uploaderInput", function() {
        		wx.chooseImage({
    	    	    count: 9 - localIdList.length,
    	    	    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
    	    	    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
    	    	    success: function (res) {
    	    	        var localIds = res.localIds;
    	    	        var str = "";
    	    	        if(isiOS) {
    	    	        	for(var i =0; i< localIds.length; i++) {
	    	    	        	(function(i) {
	    	    	        		wx.getLocalImgData({
	    	    		        		localId: localIds[i], // 图片的localID
	    	    		        		success: function (res) {
	    	    		        			var localData = res.localData;
	    	    		        			str += '<li class="weui-uploader__file" style="background-image:url('+ localData +')" data-local-id="'+ localData +'"></li>';
	    	    							if(i == localIds.length -1) {
	    	    								$("#uploaderFiles").append(str);
	    	    								$(".weui-uploader__info").html(localIdList.length + "/9");
	    	    		        			}
	    	    		        		}
	    	    		        	});
	    	    	        	})(i);
	    	    	        }
    	    	        } else {
    	    	        	for(var i =0; i< localIds.length; i++) {
    							str += '<li class="weui-uploader__file" style="background-image:url('+ localIds[i] +')" data-local-id="'+ localIds[i] +'"></li>';
    							localIdList.push(localIds[i]);
    	    	        	}
        	    	        $("#uploaderFiles").append(str);
    						$(".weui-uploader__info").html(localIdList.length + "/9");
    	    	        }
    	    	    }
    	    	});
        	});
			
        	
        	var showImg;
        	var localId = "";
        	$("body").on("click", ".weui-uploader__file", function() {
        		showImg = $(this);
        		localId = $(this).data("localId");
        		$("#galleryImg").css("background-image", "url("+ localId +")");
        		$("#galleryImg").attr("style", this.getAttribute("style"));
                $("#gallery").fadeIn(100);
        	});
        	
        	$("body").on("click", "#gallery", function() {
        		$("#gallery").fadeOut(100);
            });

        	$("body").on("click", ".weui-gallery__del", function() {
        		localIdList.removeByValue(localId);
        		showImg.remove();
        		$(".weui-uploader__info").html(localIdList.length + "/9");
            });
        	
        	
        	$("body").on("click", ".library", function() {
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
        	
        	$("body").on("click", ".storage", function() {
        		var $this = $(this);
        		layer.open({
        		    content: '确定入库',
        		    btn: ['确定', '取消'],
        		    yes: function(index) {
        		    	layer.close(index);
        		    	var $loadingToast = $('#loadingToast');
        			    $loadingToast.fadeIn(100);
        			    changeNumber = $("#changeNumber").serialize();
        			    changeNumber = decodeURIComponent(changeNumber,true);
						$.ajax({
                              type: 'post',
                              url: '${pageContext.request.contextPath}/web/confirmStorage?deliveryOrderId='+$this.data("orderId"),
                              data: changeNumber || {},
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
    	</script>
</html>
