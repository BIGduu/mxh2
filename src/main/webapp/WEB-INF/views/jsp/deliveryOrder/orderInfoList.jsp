<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 货物列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	
	
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">商品名</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">商品数量</th>
            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty orderInfoList}"><tr><td colspan="10" align="center">暂无任何货物</td></tr></c:if>
        	<c:forEach items="${orderInfoList}" var="orderInfo">
	            <tr>
	                <td>${orderInfo.merchandiseName}</td>
	                <td>${orderInfo.number.floatValue()}</td>
	                <td class="text-center">
	                    <div class="btn-group">
							<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="备注" onclick="numberUpdate('${orderInfo.id}')">修改数量</button>
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
	goMenu("/admin/order/orderInfoList", page, $("#searchForm").serialize());
}

function orderMenuList(orderId, orderName) {
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/order/orderInfoList?page=${page}&orderId="+orderId,
		timeout:5000,
		data: {
			orderName : orderName
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


function numberUpdate(orderInfoId) {
	layer.prompt({title: '请填写实际数量'}, function(pass, index){
		layer.close(index);
		index = layer.load(1, {
			shade: [0.5,'#000'] //0.1透明度的白色背景
		});
		ajax({
			type: "POST",
	        dataType: "html",
	        url: "/admin/order/updateNumber?orderInfoId="+orderInfoId,
			timeout:5000,
			data: {
				number: pass
			},
			error: function() 
			{
				layer.close(index);
				tipFail("网络异常，请稍后再试");
			},
	        success: function(data)
	        {
	        	layer.close(index);
	        	data = JSON.parse(data);
	        	if(data.status == 200) {
	        		tipSuccess("处理成功，正在刷新页面");
	        		orderInfo("${deliveryOrderId}");
	        	} else {
	        		tipFail("网络异常，请稍后再试");
	        	}
	        }
		});
	});
}

function orderInfo(){
	ajax({
		type: "get",
        dataType: "html",
        url: "/admin/order/orderInfoList?deliveryOrderId=${deliveryOrderId}",
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