<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 库存列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	<button class="btn btn-rounded btn-default btn-sm addButton" type="button" data-toggle="tooltip" title="新增库存" onclick="storageEdit('0')"><i class="fa fa-plus"></i> 新增库存</button>
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">商品名称</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">数量</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">总价</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">是否支付</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">创建时间</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">类型</th>
            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty storageList}"><tr><td colspan="10" align="center">暂无任何库存</td></tr></c:if>
        	<c:forEach items="${storageList}" var="storage">
	            <tr>
	                <td>${storage.merchandiseName}</td>
	                <td  class="clearZero">${storage.number}</td>
	                <td>${storage.totalPrice}</td>
	                <td>
		                <label class="css-input switch switch-success switch-sm" onmousedown="storageIsPay(${storage.isPay},'${storage.id}')">
	                    
		                    <c:if test="${storage.isPay==1}">
		                    	<input type="checkbox" checked="checked">
		                    </c:if>
	                        <c:if test="${storage.isPay==0}">
	                         	<input type="checkbox">
	                        </c:if>
	                        <span></span> <span id="staus"><c:if test="${storage.isPay==1}"></c:if><c:if test="${storage.isPay==0}"></c:if></span>
	                    </label>
	                </td>
	                <td>${storage.createTimeStr}</td>
	                <td>
		                <c:forEach items="${typeList}" var="type">
		                	<c:if test="${type.id == storage.type }">${ type.name }</c:if>
		                </c:forEach>
	                </td>
	                <td class="text-center">
	                    <div class="btn-group">
	                        <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="编辑库存" onclick="storageEdit('${storage.id}')"><i class="fa fa-pencil"></i></button>
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
	goMenu("/admin/storage/storageList", page);
}

function storageEdit(storageId, storageName)
{
	ajax({
		type: "get",
        dataType: "html",
        url: "/admin/storage/storageEdit?page=${page}&storageId="+storageId,
		timeout:5000,
		data: {
			storageName : storageName
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

function storageMenuList(storageId, storageName) {
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/storage/storageMenuList?page=${page}&storageId="+storageId,
		timeout:5000,
		data: {
			storageName : storageName
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

function storageIsPay(isPay,storageId){
	var content = "你是否要设为未支付?";
	if(isPay == 0){
		content = "你是否要设为已支付?";
	}
	confirmDialog({
		title:"信息确认",
		content: content,
		confirm: function(){
			storageIsPayAdjust(isPay,storageId);
		}
	});
}

function storageIsPayAdjust(isPay,storageId){
	ajax({
		type: "post",
        dataType: "json",
        url: "/admin/storage/isPay?isPay="+isPay+"&storageId="+storageId,
		timeout:5000,
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	if(data.status == "200"){
        		tipSuccess("修改成功，正在刷新页面");
        		goList("${page}");
        	}
        	else{
        		tipFail(data.message);
        	}
        }
	});
}

function confirmDelete(storageId){
	confirmDialog({
		title:"信息确认",
		content: "你是否要删除此库存",
		confirm: function(){
			storageDelete(storageId);
		}
	});
}

function storageDelete(storageId){
	tipSuccess("正在删除...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/storage/storageDelete?storageId="+storageId,
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