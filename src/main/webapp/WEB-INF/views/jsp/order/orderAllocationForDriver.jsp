<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick="goList(${page})" style="cursor: pointer;">订单管理  </a> >订单分配</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer">
        <thead>
            <tr>
            	<th>编号</th>
		        <th>材料名称</th>
		        <th>规格</th>
		        <th>单位</th>
		        <th>单价</th>
		        <th>下单数量</th>
		        <th>金额</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty orderInfos}"><tr><td colspan="10" align="center">暂无任何订单</td></tr></c:if>
        	<c:forEach items="${orderInfos}" var="orderInfo">
	            <tr data-merchandise-id="${orderInfo.merchandiseId }" data-merchandise-name="${orderInfo.merchandiseName }">
					<td>${orderInfo.merchandise.merchandiseCode }</td>
					<td>${orderInfo.merchandiseName }</td>
					<td>${orderInfo.merchandise.specification }</td>
					<td>${orderInfo.merchandise.unit }</td>
					<td>${orderInfo.merchandise.unitPrice }</td>
					<td class="clearZero">${orderInfo.number }</td>
					<td>${orderInfo.allPrice }</td>
	            </tr>
            </c:forEach>
        </tbody>
    </table>
    <div style="overflow: hidden;">
    	<c:if test="${not empty totalAmount }">
    		<p style="float: right;font-size: 20px;color: coral;">总金额：${totalAmount}元
    		</p>
    	</c:if>
    </div>
    <c:if test="${order.state == 2 }">
    	<button id="deliveryAdd" type="button">新增配送员</button>
    </c:if>
    <div id="deliveryList">
    	<c:forEach items="${deliveryOrders}" var="deliveryOrder">
    		<div class="delivery">
    			${deliveryOrder.deliveryName }
    			<c:forEach items="${deliveryOrder.orderInfos }" var="orderInfo">
					${orderInfo.merchandiseName }: <span class="clearZero">${orderInfo.number }</span>
				</c:forEach>
				
				订单状态:
				<c:choose>
					<c:when test="${deliveryOrder.state == 3}">已分配<button class="deliveryDel" data-delivery-order-id="${deliveryOrder.id }" >删除</button></c:when>
					<c:when test="${deliveryOrder.state == 10}">装车中<button class="deliveryDel" data-delivery-order-id="${deliveryOrder.id }" >删除</button></c:when>
					<c:when test="${deliveryOrder.state == 4}">装车完成<button class="deliveryDel" data-delivery-order-id="${deliveryOrder.id }" >删除</button></c:when>
					<c:when test="${deliveryOrder.state == 5}">已出库</c:when>
					<c:when test="${deliveryOrder.state == 6}">已送达
						<button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button>
						&nbsp;
						<c:if test="${order.orderType != 1 || (sessionScope.admin.roleId != 5 && sessionScope.admin.roleId != 6) }">
							<c:if test="${deliveryOrder.shippingcosts != 0}">
								运费
								<c:choose>
									<c:when test="${order.orderType == 2 }">${deliveryOrder.shippingcosts * 2}</c:when>
									<c:otherwise>${deliveryOrder.shippingcosts}</c:otherwise>
								</c:choose>
							</c:if>
						</c:if>
						&nbsp; <c:if test="${order.upstairs == 1}">上楼人:${deliveryOrder.upstairPersion} &nbsp;楼层${deliveryOrder.floor} &nbsp;上楼费${deliveryOrder.upstaircosts}</c:if>
					</c:when>
					<c:when test="${deliveryOrder.state == 7}">已完成<button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button></c:when>
					<c:when test="${deliveryOrder.state == 8}">已入库<button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button></c:when>
					<c:when test="${deliveryOrder.state == 9}">异常 理由：${deliveryOrder.remarks} <button class="deliveryDel" data-delivery-order-id="${deliveryOrder.id }" >删除</button></c:when>
				</c:choose>
				<a class="btn btn-xs btn-default" href="${pageContext.request.contextPath}/admin/order/orderDetail?deliveryOrderId=${deliveryOrder.id}" target="_blank">打印</a>
    		</div>
    	</c:forEach>
    </div>
</div>    
<!-- END Page Content -->

<script type="text/javascript">
$(function(){
	

	var deliveryList;
	var orderId = "${orderId}";
	ajax({
		type: "get",
	    dataType: "json",
	    url: "/admin/order/deliveryList",
	    data: {},
		timeout:5000,
		error: function(request) {
			tipFail("网络异常，请稍后再试");
		},
	    success: function(data) {
	    	if(data.status=='200') {
	    		deliveryList = data.data.userCheckboxs;
	   		}
	    }
	});
	
	var merchandises = [];
	var trs = $("tbody tr");
	for (var i = 0; i < trs.length; i++) {
		var merchandise = {};
		merchandise.merchandiseId = $(trs[i]).data("merchandiseId");
		merchandise.merchandiseName = $(trs[i]).data("merchandiseName");
		merchandises.push(merchandise);
	}

	$("#deliveryAdd").click(function() {
		var str = '<div class="delivery"><select class="deliveryName">';
		for (var i = 0; i < deliveryList.length; i++) {
			str += '<option value="' + deliveryList[i].id+'">'+deliveryList[i].username+'</option>';
		}
		str += "</select>"
		for (var i = 0; i < merchandises.length; i++) {
			str += merchandises[i].merchandiseName + ':<input type="number" value="" class="submitInput" data-merchandise-id="'+merchandises[i].merchandiseId +'" data-merchandise-name="'+merchandises[i].merchandiseName +'">'
		}
		str += '<button class="deliverySave">保存</button><button class="deliveryCal">取消</button></div>';
		$("#deliveryList").append(str);
	});
	
	$("#deliveryList").on("click", ".deliverySave", function() {
		var delivery = $(this).parents(".delivery");
		if("${order.orderType}" != 1) {
			layer.confirm('是否收取运费？', {
					btn: ['收','不收'] //按钮
				}, function(index) {
					layer.close(index);
					deliverySave(delivery, 1);
				}, function(index) {
					layer.close(index);
					deliverySave(delivery, 0);
				});
		} else {
			deliverySave(delivery, 1);
		}

	});
	
	function deliverySave(delivery, isShippingcosts) {
		var $deliverySelect = $(".deliveryName option:selected", delivery);
		var $submitInputs = $(".submitInput", delivery);
		var deliveryId = $deliverySelect.val();
		var deliveryName = $deliverySelect.text();
		var merchandiseList = [];
		for (var i = 0; i < $submitInputs.length; i++) {
			if($submitInputs[i].value) {
				var merchandise = {};
				merchandise.merchandiseId = $submitInputs[i].dataset.merchandiseId;
				merchandise.merchandiseName = $submitInputs[i].dataset.merchandiseName;
				merchandise.number = $submitInputs[i].value;
				merchandiseList.push(merchandise);
			}
		}
		ajax({
			type: "post",
		    dataType: "json",
		    url: "/admin/order/deliveryOrderSave",
		    data: {
		    	orderId: orderId,
		    	deliveryId: deliveryId,
		    	orderInfoStr: JSON.stringify(merchandiseList),
		    	isShippingcosts: isShippingcosts
		    },
			timeout:5000,
			error: function(request) {
				tipFail("网络异常，请稍后再试");
			},
		    success: function(data) {
		    	if(data.status=='200') {
		    		tipSuccess("保存成功");
		    		var str = '<div class="delivery">' + deliveryName;
		    		for (var i = 0; i < merchandiseList.length; i++) {
		    			str += merchandiseList[i].merchandiseName + ":" + parseFloat(merchandiseList[i].number);
					}
		    		str +=  '<button class="deliveryDel" data-delivery-order-id="' + data.data.deliveryOrderId+ '" >删除</button>订单状态：已分配</div>';
		    		$("#deliveryList").append(str);
		    		delivery.remove();
		   		} else {
		   			tipFail(data.message);
		   		}
		    }
		});
	}
	
/*  	$("#deliveryList").on("click", ".deliveryDel", function() {
		var $this = $(this);
		
		ajax({
			type: "post",
		    dataType: "json",
		    url: "/admin/order/deliveryOrderDel",
		    data: {
		    	deliveryOrderId: $this.data("deliveryOrderId")
		    },
			timeout:5000,
			error: function(request) {
				tipFail("网络异常，请稍后再试");
			},
		    success: function(data) {
		    	if(data.status=='200') {
		    		tipSuccess("删除成功");
		    		$this.parent(".delivery").remove();
		   		} else {
		   			tipFail("网络异常，请稍后再试");
		   		}
		    }
		});
		
	});  */
	
	$("#deliveryList").on("click", ".deliveryCal", function() {
		$(this).parent(".delivery").remove();
	});
	
	$("#deliveryList").on("click", ".imageCheck", function() {
		var $this = $(this);
		ajax({
			type: "get",
		    dataType: "json",
		    url: "/admin/order/images",
		    data: {
		    	deliveryOrderId: $this.data("deliveryOrderId")
		    },
			timeout:5000,
			error: function(request) {
				tipFail("网络异常，请稍后再试");
			},
		    success: function(data) {
		    	if(data.status==200) {
		    		if(data.data.images.length > 0) {
		    			layer.photos({
							photos: imageJson(data.data.images),
			    		    anim: 5
						});
		    		} else {
		    			mytips("没有图片");
		    		}
		   		} else {
		   			tipFail("网络异常，请稍后再试");
		   		}
		    }
		});
	});
	
	function imageJson(images) {
		var json = {
			"title": ""
		}
		var arr = [];
		for (var i = 0; i < images.length; i++) {
			var image = {};
			image.src = images[i];
			arr.push(image);
		}
		json.data = arr;
		return json;
	}
	
	var clearZeros = $(".clearZero");
	for (var i = 0; i < clearZeros.length; i++) {
		$(clearZeros[i]).html(parseFloat($(clearZeros[i]).html()));
	}
})
</script>