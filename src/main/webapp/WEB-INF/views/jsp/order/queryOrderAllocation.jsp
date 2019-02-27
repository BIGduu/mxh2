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
	<div>
		<div>
			<c:if test="${order.state >= 2 }">
				<a style="margin: 10px" href="${pageContext.request.contextPath}/admin/order/orderDetail?deliveryOrderId=-1&orderId=${order.id}" target="_blank">打印订单</a>
			</c:if>
		</div>
		<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer">
	        <thead>
	            <tr>
	            	<th>编号</th>
			        <th>材料名称</th>
			        <th>规格</th>
			        <th>单位</th>
			        <th>单价</th>
			        <th>下单数量</th>
			        <c:if test="${not empty isShowShippingcosts }">
			        	<th>运费</th>
			        </c:if>
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
						 <c:if test="${not empty isShowShippingcosts }">
					        <td>${orderInfo.merchandise.shippingCost*2*orderInfo.number}</td>
					      </c:if>
						<td>${orderInfo.allPrice }</td>
		            </tr>
	            </c:forEach>
	        </tbody>
	    </table>
    </div>
    
    <div>
    	<c:if test="${not empty upstairsDetails}">
			<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="upstairsDetailTable" role="grid" aria-describedby="DataTables_Table_1_info">
		        <thead>
		            <tr role="row">
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >配送人员</th>
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >商品名称</th>
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >规格</th>
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >上楼运费单价（/每层每单位）</th>
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">数量</th>
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">楼层</th>
		            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">上楼费</th>
		            </tr>
		        </thead>
		        <tbody>
		        	<c:forEach items="${upstairsDetails}" var="upstairsDetail">
			            <tr>
			            	<td>${upstairsDetail.deliveryName}</td>
			                <td>${upstairsDetail.merchandiseName}</td>
			                <td>${upstairsDetail.specification}</td>
			                <td>${upstairsDetail.upstairsCost}</td>
			                <td >${upstairsDetail.number}</td>
			                <td >${upstairsDetail.floor}</td>
			                <td >${upstairsDetail.cost}</td>
			            </tr>
		            </c:forEach>
		        </tbody>
		    </table>
	    </c:if>
    </div>
    <div style="overflow: hidden;">
    	<c:if test="${not empty totalAmountAll }">
    		<p style="float: right;font-size: 20px;color: coral;">总金额合计：${totalAmountAll}元
    			<c:if test="${not empty totalShippingcosts }"><div style="font-size: 20px;color: coral;">运费合计：${totalShippingcosts}元</div></c:if>
    			<c:if test="${not empty totalUpstaircosts }"><div style="font-size: 20px;color: coral;">上楼费合计：${totalUpstaircosts}元</div></c:if>
    		</p>
    	</c:if>
    </div>
    <div>
    	<c:if test="${not empty upstairsImgs}">
    	<div>步梯上楼图片</div>
    		<c:forEach items="${upstairsImgs}" var="upstairsImg">
    		<img alt="" class="zoomify" src="${upstairsImg.img}" name="adImageShow" style="width: 150px;height: 150px;">
    		</c:forEach>
    	</c:if>
    </div>


	<div id="deliveryList">
		<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer">
			<thead>
			<tr>
				<th>配送员姓名</th>
				<th>配送材料详情</th>
				<th>订单状态</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody id="deliveryListBody">
			<c:if test="${empty deliveryOrders}"><tr><td colspan="10" align="center">暂无任何配送员</td></tr></c:if>
			<c:forEach items="${deliveryOrders}" var="deliveryOrder">
				<tr data-merchandise-id="${orderInfo.merchandiseId }" data-merchandise-name="${orderInfo.merchandiseName }">
					<td>${deliveryOrder.deliveryName}</td>
					<td>
						<c:forEach items="${deliveryOrder.orderInfos }" var="orderInfo">
							${orderInfo.merchandiseName }: <span class="clearZero">${orderInfo.number }</span>
						</c:forEach>
					</td>
					<td>
						<c:choose>
							<c:when test="${deliveryOrder.state == 3}">已分配</c:when>
							<c:when test="${deliveryOrder.state == 10}">装车中</c:when>
							<c:when test="${deliveryOrder.state == 4}">装车完成 </c:when>
							<c:when test="${deliveryOrder.state == 5}">已出库 </c:when>
							<c:when test="${deliveryOrder.state == 6}">已送达</c:when>
							<c:when test="${deliveryOrder.state == 7}">已完成</c:when>
							<c:when test="${deliveryOrder.state == 8}">已入库</c:when>
							<c:when test="${deliveryOrder.state == 9}">异常 理由：${deliveryOrder.remarks}</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
							<c:when test="${deliveryOrder.state == 3}"><button class="deliveryDel" data-delivery-order-id="${deliveryOrder.id }" >删除此配送人</button>
								<a class="btn" href="${pageContext.request.contextPath}/admin/order/orderDetail?deliveryOrderId=${deliveryOrder.id}&orderId=${order.id}" target="_blank">打印</a>
							</c:when>
							<c:when test="${deliveryOrder.state == 10}"><button class="deliveryDel" data-delivery-order-id="${deliveryOrder.id }" >删除此配送人</button>
								<a class="btn" href="${pageContext.request.contextPath}/admin/order/orderDetail?deliveryOrderId=${deliveryOrder.id}&orderId=${order.id}" target="_blank">打印</a>
							</c:when>
							<c:when test="${deliveryOrder.state == 6}"><button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看送达图片</button>
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
							<c:when test="${deliveryOrder.state == 7}"><button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button></c:when>
							<c:when test="${deliveryOrder.state == 8}"><button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button></c:when>
						</c:choose>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>

    <%--<div id="deliveryList">--%>
    	<%--<c:forEach items="${deliveryOrders}" var="deliveryOrder">--%>
    		<%--<div class="delivery">--%>
    			<%--${deliveryOrder.deliveryName }--%>
    			<%--<c:forEach items="${deliveryOrder.orderInfos }" var="orderInfo">--%>
					<%--${orderInfo.merchandiseName }: <span class="clearZero">${orderInfo.number }</span>--%>
				<%--</c:forEach>--%>
				<%----%>
				<%--订单状态:--%>
				<%--<c:choose>--%>
					<%--<c:when test="${deliveryOrder.state == 3}">已分配</c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 10}">装车中</c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 4}">装车完成</c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 5}">已出库</c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 6}">已送达--%>
						<%--<button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button>--%>
						<%--&nbsp;--%>
						<%--<c:if test="${order.orderType != 1 || (sessionScope.admin.roleId != 5 && sessionScope.admin.roleId != 6) }">--%>
							<%--<c:if test="${deliveryOrder.shippingcosts != 0}">--%>
								<%--运费--%>
								<%--<c:choose>--%>
									<%--<c:when test="${order.orderType == 2 }">${deliveryOrder.shippingcosts * 2}</c:when>--%>
									<%--<c:otherwise>${deliveryOrder.shippingcosts}</c:otherwise>--%>
								<%--</c:choose>--%>
							<%--</c:if>--%>
						<%--</c:if>--%>
						<%--&nbsp; <c:if test="${order.upstairs == 1}">上楼人:${deliveryOrder.upstairPersion} &nbsp;楼层${deliveryOrder.floor} &nbsp;上楼费${deliveryOrder.upstaircosts}</c:if>--%>
					<%--</c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 7}">已完成<button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button></c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 8}">已入库<button class="imageCheck" data-delivery-order-id="${deliveryOrder.id }" >查看图片</button></c:when>--%>
					<%--<c:when test="${deliveryOrder.state == 9}">异常 理由：${deliveryOrder.remarks} </c:when>--%>
				<%--</c:choose>--%>
				<%--<a class="btn btn-xs btn-default" href="${pageContext.request.contextPath}/admin/order/orderDetail?deliveryOrderId=${deliveryOrder.id}&orderId=${order.id}" target="_blank">打印</a>--%>
    		<%--</div>--%>
    	<%--</c:forEach>--%>
    <%--</div>--%>
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
		
	$(".zoomify").on("click",  function() {
		var $this = $(this).get(0);
		var arr=[];
		arr.push($this.src);
  			layer.photos({
				photos: imageJson(arr),
	   		    anim: 5
			});
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
</script>