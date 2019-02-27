<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 角色列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
<!-- 	<button class="btn btn-rounded btn-default btn-sm addButton" type="button" data-toggle="tooltip" title="新增角色" onclick="roleEdit('0')"><i class="fa fa-plus"></i> 新增角色</button> -->
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">角色名称</th>
            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty roleList}"><tr><td colspan="10" align="center">暂无任何角色</td></tr></c:if>
        	<c:forEach items="${roleList}" var="role">
	            <tr>
	                <td>${role.roleName}</td>
	                <td class="text-center">
	                	<c:if test="${role.roleCode != 'admin'}">
		                    <div class="btn-group">
		                        <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="编辑角色" onclick="roleEdit('${role.id}')">权限分配</button>
		                        <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="菜单分配" onclick="roleMenuList('${role.id}', '${role.roleName}')">菜单分配</button>
		                    </div>
	                    </c:if>
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
	goMenu("/admin/role/roleList", page);
}

function roleEdit(roleId)
{
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/role/roleEdit?page=${page}&roleId="+roleId,
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

function roleMenuList(roleId) {
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/role/roleMenuList?page=${page}&roleId="+roleId,
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

function confirmDelete(roleId){
	confirmDialog({
		title:"信息确认",
		content: "你是否要删除此角色，如果用户有关联此角色，则有可能看不到相应的菜单",
		confirm: function(){
			roleDelete(roleId);
		}
	});
}

function roleDelete(roleId){
	tipSuccess("正在删除...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/role/roleDelete?roleId="+roleId,
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
</script>