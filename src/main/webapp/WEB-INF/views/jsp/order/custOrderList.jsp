<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 客户材料部新增订单列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	
	<form class="form-horizontal" autocomplete="off" action="" method="post" onsubmit="return false;" id="searchForm">
		<div class="row">
			<div class="common-query-unit" id="searchTitleDiv">
				<div class="form-group">
					<div
						style="line-height: 25px; display: inline-block; padding-left: 15px;"
						id="orderCodeTitle">订单编号：</div>
					<div style="width: 65%; display: inline-block;">
						<input class="form-control" style="height: 28px; width: 87%;"
							type="text" id="orderCode" name="orderCode" value=${orderCode }>
					</div>
				</div>
			</div>

			<div class="common-query-unit" id="searchTitleDiv">
				<div class="form-group">
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="receivingAddressTitle">工地：</div>
			    	<div style="width: 65%;display: inline-block;">
			    		<input class="form-control" style="height:28px;width: 87%;" type="text" id="receivingAddress" name="receivingAddress" value=${receivingAddress }>
					</div>
		    	</div>
			</div>
			
			<div class="common-query-unit" id="searchTitleDiv">
				<div class="form-group">
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="managerIdTitle">项目经理：</div>
			    	<div style="width: 65%;display: inline-block;">
			    		<input class="form-control" style="height:28px;width: 87%;" type="text" id="managerName" name="managerName" value=${managerName }>
			    		<input type="hidden" id="managerId" name="managerId" value= "${managerId}" />
					</div>
		    	</div>
			</div>
		</div>
		<div class="row">
			<div class="common-query-unit" id="searchTitleDiv">
					<div class="form-group">
						<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">开始时间：</div>
				    	<div style="width: 65%;display: inline-block;">
				    		<input type="text" class="laydate-input" id="beginTime" name="beginTime" >
						</div>
			    	</div>
				</div>
				
				<div class="common-query-unit" id="searchTitleDiv">
					<div class="form-group">
						<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">结束时间：</div>
				    	<div style="width: 65%;display: inline-block;">
				    		<input type="text" class="laydate-input" id="endTime" name="endTime" >
						</div>
			    	</div>
				</div>
			</div>

			<div class="row">
				<div class="common-query-unit">
					<div class="form-group">
				    	<div class="col-xs-12">
							<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="查询" onclick="goList('1')">查询</button>
				        </div>
				    </div>
				</div>
				<div class="common-query-unit">
					<div class="form-group">
				    	<div class="col-xs-12">
							<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="新增订单" onclick="addOrder()">新增订单</button>
				        </div>
				    </div>
				</div>
			</div>
	</form>
	
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">门店</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">下单时间</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">项目经理</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">项目经理电话</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">配送人</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">收货地址</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">订单备注</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">订单状态</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">订单类型</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">是否上楼</th>
            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty orderList}"><tr><td colspan="13" align="center">暂无任何订单</td></tr></c:if>
        	<c:forEach items="${orderList}" var="order">
	            <tr>
	                <td>${order.storesName}</td>
	                <td>${order.placeOrderTimeStr}</td>
	                <td>${order.managerName}</td>
	                <td>${order.managerTel}</td>
	                <td>${order.deliveryNames}</td>
	                <td>${order.receivingAddress}</td>
	                <td>
		                <c:if test="${sessionScope.admin.roleId == 2 || sessionScope.admin.roleId == 4}">
		                	<input type="text" value="${order.remarks}" onchange="changeRemarks('${order.id}',this.value);"/>
		                </c:if>
		                <c:if test="${sessionScope.admin.roleId != 2 && sessionScope.admin.roleId != 4}">
		                	${order.remarks}
		                </c:if>
	                </td>
	                <td>
	                	<c:choose>
		   					<c:when test="${order.state == 1 }"> 未审核</c:when>
		   					<c:when test="${order.state == 2 }"> 审核通过</c:when>
		   					<c:when test="${order.state == 0 }"> 异常</c:when>
		   					<c:when test="${order.state == 5 }"> 已出库待司机送达</c:when>
		   					<c:when test="${order.state == 6 }"> 已送达</c:when>
			   				<c:when test="${order.state == 7 }">已完成</c:when>
						</c:choose>
					</td>
					<td>
						<c:choose>
		   					<c:when test="${order.orderType == 1 }"> 配送订单</c:when>
		   					<c:when test="${order.orderType == 2 }"> 退货订单</c:when>
		   					<c:when test="${order.orderType == 3 }"> 转货订单</c:when>
		   				</c:choose>
					</td>
					<td>
						<c:choose>
		   					<c:when test="${order.upstairs == 1 }"> 是</c:when>
		   					<c:otherwise>否</c:otherwise>
		   				</c:choose>
					</td>
	                <td class="text-center">
	                    <div class="btn-group">
							<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="订单详情" onclick="orderDetail('${order.id}')">订单详情</button>
	                    </div >
	                </td>
	            </tr>
            </c:forEach>
        </tbody>
    </table>
    <jsp:include page="../main/tablePage.jsp"></jsp:include>
</div>    
<!-- END Page Content -->

<script type="text/javascript">
//覆盖goList方法，分页自动调用该方法
function goList(page)
{
	goMenu("/admin/order/custOrderList", page, $("#searchForm").serialize());
}

var data =[];
<%
Object ob=request.getAttribute("users");
%>
data= <%out.println(JSON.toJSONString(ob));%>
$(function () {
	  $('#managerName').autocompleter({
		  	highlightMatches: true,
	        // object to local or url to remote search
	        source: data,
	        // show hint
	        hint: true,
	        // abort source if empty field
	        empty: true,
	        // max results
	        limit: 5,
	        callback: function (value, index,item) {
	        	if(item && item.id){
	        		$("#managerId").val(item.id);
	        	}else{
	        		$("#managerId").val("");
	        	}
	        }
	    });
	  $('#managerName').blur(function(){
		  if(!$(this).val()){
			  $("#managerId").val("");
		  }
	  });
	});

function orderDetail(orderId)
{
	ajax({
		type: "get",
        dataType: "html",
        url: "/admin/order/queryAllocationPage?page=${page}&orderId="+orderId,
		timeout:5000,
		data: {},
		error: function() {
			tipFail("网络异常，请稍后再试");
		},
        success: function(data) {
        	$('#main-container').html(data);
        }
	});
}

function addOrder(){
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/order/custOrderAdd",
		timeout:5000,
		data: {
		},
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	$('#main-container').html(data);
        }
	});
}

laydate.render({
	elem: '#beginTime',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});

laydate.render({
	elem: '#endTime',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});

</script>