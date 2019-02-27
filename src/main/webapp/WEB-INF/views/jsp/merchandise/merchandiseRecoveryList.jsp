<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 商品回收站</small>
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
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >商品名称</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >规格</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >单位</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >单价</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >品牌</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >运费单价</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >上楼运费（/每层每单位）</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">总数</th>
            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty merchandiseList}"><tr><td colspan="10" align="center">暂无任何商品</td></tr></c:if>
        	<c:forEach items="${merchandiseList}" var="merchandise">
	            <tr>
	                <td>${merchandise.merchandiseName}</td>
	                <td>${merchandise.specification}</td>
	                <td>${merchandise.unit}</td>
	                <td>${merchandise.unitPrice}</td>
	                <td>${merchandise.brandName}</td>
	                <td>${merchandise.shippingCost}</td>
	                <td>${merchandise.upstairsCost}</td>
	                <td class="clearZero">${merchandise.total}</td>
	                <td class="text-center">
	                    <div class="btn-group">
	                     	<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="还原" onclick="confirmDelete('${merchandise.id}')">还原</button>
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
	goMenu("/admin/merchandise/merchandiseRecoveryList", page);
}


function merchandiseMenuList(merchandiseId, merchandiseName) {
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/merchandise/merchandiseMenuList?page=${page}&merchandiseId="+merchandiseId,
		timeout:5000,
		data: {
			merchandiseName : merchandiseName
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

function confirmDelete(merchandiseId){
	confirmDialog({
		title:"信息确认",
		content: "你是否要还原此商品",
		confirm: function(){
			merchandiseDelete(merchandiseId);
		}
	});
}

function merchandiseDelete(merchandiseId){
	tipSuccess("正在还原...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/merchandise/merchandiseDelete?merchandiseId="+merchandiseId,
		timeout:5000,
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	if(data.status == "200"){
        		tipSuccess("删除成功，正在刷新页面");
        		goList("${page}");
        	}
        	else{
        		tipFail(data.message);
        	}
        }
	});
}

var clearZeros = $(".clearZero");
for (var i = 0; i < clearZeros.length; i++) {
	$(clearZeros[i]).html(parseFloat($(clearZeros[i]).html()));
}
</script>