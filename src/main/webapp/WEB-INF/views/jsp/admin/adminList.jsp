<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 后台用户管理列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	<button class="btn btn-rounded btn-default btn-sm addButton" type="button" data-toggle="tooltip" title="新增用户" onclick="adminEdit('0')"><i class="fa fa-plus"></i> 新增用户</button>
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">登录账号</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-label="Name: activate to sort column ascending" style="width: 100px;">用户名</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-label="Email: activate to sort column ascending" style="width: 100px;">门店</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-label="Email: activate to sort column ascending" style="width: 80px;">电话</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-label="Email: activate to sort column ascending" style="width: 80px;">角色</th>
            	<th class="text-center sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-label="Email: activate to sort column ascending" style="width: 80px;">是否启用</th>
            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
	        </tr>
        </thead>
        <tbody>
        	<c:if test="${empty adminList}"><tr><td colspan="10" align="center">暂无任何管理员</td></tr></c:if>
        	<c:forEach items="${adminList}" var="admin">
	            <tr>
	                <td>${admin.adminName}</td>
	                <td class="hidden-xs">${admin.username}</td>
	                 <td class="hidden-xs">${admin.storesName}</td>
	                 <td class="hidden-xs">${admin.telephone}</td>
	                 <td class="hidden-xs">${admin.roleName}</td>
	                <td class="text-center">
	                 <label class="css-input switch switch-success switch-sm" onmousedown="adminEnable(${admin.isUse},'${admin.adminId}')">
                    
	                    <c:if test="${admin.isUse==1}">
	                    	<input type="checkbox" checked="checked">
	                    </c:if>
                        <c:if test="${admin.isUse==0}">
                         	<input type="checkbox">
                        </c:if>
                        <span></span> <span id="staus"><c:if test="${admin.isUse==1}"></c:if><c:if test="${admin.isUse==0}"></c:if></span>
                    </label>
	                </td>
	                <td class="text-center">
	                    <div class="btn-group">
	                    	<c:if test="${admin.adminName != 'admin' }">
	                        	<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="编辑管理员" onclick="adminEdit('${admin.adminId}')">编辑</button>
	                        </c:if>
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
	goMenu("/admin/admin/adminList", page);
}

function adminEnable(oldEnabled,adminId){
	var content = "你是否需要禁用此用户?";
	if(oldEnabled == 0){
		content = "你是否需要启用此用户?";
	}
	confirmDialog({
		title:"信息确认",
		content: content,
		confirm: function(){
			adminEnableAdjust(oldEnabled,adminId);
		}
	});
}

function adminEnableAdjust(oldEnabled,adminId){
	ajax({
		type: "GET",
        dataType: "json",
        url: "/admin/admin/adminEnable?isUse="+oldEnabled+"&adminId="+adminId,
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

function adminEdit(adminId)
{
	ajax({
		type: "GET",
        dataType: "html",
        url: "/admin/admin/adminEdit?page=${page}&adminId="+adminId,
		timeout:5000,
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

function adminRoleList(adminId, adminName,title){
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/admin/adminRoleList?page=${page}&adminId="+adminId,
		timeout:5000,
		data: {
			adminName : adminName,
			title : title
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
</script>
