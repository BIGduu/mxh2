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
        		border-top: 1px solid #e6e6e6;
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
    <script src="${pageContext.request.contextPath}/assets/js/mescroll.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
   	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
   	<script type="text/javascript">
    	
    	$(function() {
    		
    		var u = navigator.userAgent;  
            var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1; //android终端  
            var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端  
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
    										'<label class="weui-form-preview__label">项目经理姓名</label>'+
    											'<span class="weui-form-preview__value">' + order.managerName + '</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">项目经理电话</label>'+
    											'<span class="weui-form-preview__value">' + order.managerTel + '</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">门店</label>'+
    											'<span class="weui-form-preview__value">' + order.storesName + '</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
    										'<label class="weui-form-preview__label">订单状态</label>'+
    										'<span class="weui-form-preview__value state">'+ getDelOrderState(order.state) +'</span>'+
    									'</div>'+
    									'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">订单类型</label>'+
											'<span class="weui-form-preview__value">'+ getOrderType(order.orderType) +'</span>'+
										'</div>'+
										'<div class="weui-form-preview__item">'+
											'<label class="weui-form-preview__label">运费</label>'+
											'<span class="weui-form-preview__value">￥'+ order.shippingcosts+'</span>'+
										'</div>';
					
					str += '</div>';
    				if(order.state == 3) {
    					str += '<div class="weui-form-preview__ft">'+
									'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary accept" data-order-id= "'+ order.id+'">接受</button>'+
								'</div>';
    				} else if(order.state == 5) {
    					str += '<div class="weui-form-preview__ft">'+
									/* '<a class="weui-form-preview__btn weui-form-preview__btn_default refuse" href="javascript:" data-order-id= "'+ order.id+'">意外</a>'+ */
									'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary sentto" data-order-id= "'+ order.id+'" data-upstairs='+order.upstairs+'>确认送达</button>'+
								'</div>';
			    	} else if(order.state == 10) {
    					str += '<div class="weui-form-preview__ft">'+
									'<button type="submit" class="weui-form-preview__btn weui-form-preview__btn_primary shipment" data-order-id= "'+ order.id+'">装车完成</button>'+
								'</div>';
    				}
    				str += '</div>';
    			}
    			$("#orderList").append(str);
    		}
        	
        	function getListDataFromNet(pageNum, pageSize, successCallback, errorCallback) {
                	$.ajax({
    	                type: 'GET',
    	                url: '${pageContext.request.contextPath}/web/deliveryOrderList',
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
        	
        	$("#orderList").on("click", ".accept", function() {
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
                    		$(".state", $this.parents(".weui-form-preview")).html("装货中");
                    		$this.parent(".weui-form-preview__ft").remove();
                    		mytips("接受成功");
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

        	$("#orderList").on("click", ".shipment", function() {
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
                    		$(".state", $this.parents(".weui-form-preview")).html("待出库");
                    		$this.parent(".weui-form-preview__ft").remove();
                    		mytips("装货完成，等待仓管确认出库");
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
        	
        	$("#orderList").on("click", ".refuse", function() {
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
	                          		$(".state", $this.parents(".weui-form-preview")).html("意外");
	                          		$this.parent(".weui-form-preview__ft").remove();
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
        			}
        		});
        	});

        	var pageii,$clickSentto;
        	$("#orderList").on("click", ".sentto", function() {
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
    	    	    sizeType: ['original', 'compressed'],
    	    	    sourceType: ['album', 'camera'],
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
	    	    		        			localIdList.push(localIds[i]);
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
    	});
    	
    	function jump(deliveryOrderId) {
    		window.location.href = "${pageContext.request.contextPath}/web/deliveryOrderDetail?deliveryOrderId=" + deliveryOrderId;
    	}
    	
    	</script>
</html>
