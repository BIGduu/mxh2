<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 订单管理列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	<!-- 查询表单 -->
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">配送人姓名</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">配送人手机</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">货物</th>
            	<th class="text-center sorting_disabled" style="width: 100px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty deliveryOrderList}"><tr><td colspan="10" align="center">暂无任何订单</td></tr></c:if>
        	<c:forEach items="${deliveryOrderList}" var="deliveryOrder">
	            <tr>
	                <td>${deliveryOrder.deliveryName}</td>
	                <td class="hidden-xs">${deliveryOrder.deliveryTel}</td>
	                <td class="hidden-xs">
						<c:forEach items="${deliveryOrder.orderInfos }" var="orderInfo">
							${orderInfo.merchandiseName }: ${orderInfo.number.floatValue() }
						</c:forEach>
					</td>
	                <td class="text-center">
	                    <div class="btn-group">
	                     	<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="确认入库" onclick="confirmStorage('${deliveryOrder.id}')">确认入库</button>
	                     	<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="修改数量" onclick="orderInfo('${deliveryOrder.id}')">修改数量</button>
	                    </div>
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
	var selectId = $('#selectId').val();
	var searchName =$('#searchName').val();
	goMenu("/admin/deliveryOrder/storageConfirmList", page, {
		selectId : selectId,
		searchName : searchName
	});
}

function confirmStorage(deliveryOrderId) {
		ajax({
			type: "POST",
	        dataType: "html",
	        url: "/admin/deliveryOrder/confirmStorage?deliveryOrderId="+ deliveryOrderId,
			timeout:5000,
			error: function() 
			{
				tipFail("网络异常，请稍后再试");
			},
	        success: function(data)
	        {
	        	tipSuccess("修改成功，正在刷新页面");
	    		goList("${page}");
	        }
		});
}

function orderInfo(deliveryOrderId){
	ajax({
		type: "get",
        dataType: "html",
        url: "/admin/order/orderInfoList?deliveryOrderId="+deliveryOrderId,
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

</script>

