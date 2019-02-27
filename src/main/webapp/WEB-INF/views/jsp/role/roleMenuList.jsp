<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a>  > <a onclick="goList(${page})" style="cursor: pointer;">角色列表  </a> > ${roleName} > 菜单分配</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
	<button class="btn btn-rounded btn-default btn-sm addButton" type="button" data-toggle="tooltip" title="保存" onclick="roleMenuSave(${page})"><i class="fa fa-plus"></i> 保存</button>
	<button class="btn btn-rounded btn-default btn-sm addButton" type="button" data-toggle="tooltip" title="返回角色列表" onclick="goList(${page})" style="margin-left: 10px;"><i class="fa fa-reply"></i> 返回角色列表</button>
	<table class="table table-bordered table-striped js-table-checkable dataTable no-footer table-hover" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="text-center sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 50px;">是否分配</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单名称</th>
            	<th class="sorting_disabled hidden-xs" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单路径</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty menuSelectList}"><tr><td colspan="10" align="center">无任何菜单可分配</td></tr></c:if>
        	<c:forEach items="${menuSelectList}" var="menuSelect">
	            <tr>
	                <td class="text-center">
		         	 	<label class="css-input css-checkbox css-checkbox-primary" style="margin-top:0px;margin-bottom:0px;">
		         	 		<c:if test="${menuSelect.selected == 1}">
								<input type="checkbox" name="menuSelected" value="${menuSelect.menuId}" checked="checked"><span></span>
		         	 		</c:if>
		         	 		<c:if test="${menuSelect.selected == 0}">
								<input type="checkbox" name="menuSelected" value="${menuSelect.menuId}"><span></span>
		         	 		</c:if>
						</label>
	                </td>
	                <td>${menuSelect.menuName}</td>
	                <td class="hidden-xs">${menuSelect.linkUrl}</td>
	            </tr>
            </c:forEach>
        </tbody>
    </table>
</div>    
<!-- END Page Content -->
<script type="text/javascript">
function roleMenuSave(page){
	var roleId = "${roleId}"
	var ids = "";
	$('input[name="menuSelected"]:checked').each(function(){
		ids += $(this).val()+","
	});
	tipSuccess("正在保存...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/role/roleMenuSave",
		timeout:6000,
		data:{
			roleId : roleId,
			ids : ids
		},
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	if(data.status == "200"){
        		tipSuccess("保存成功");
        	}
        	else{
        		tipFail(data.message);
        	}
        }
	});
}
</script>
<script type="text/javascript">
$(function(){App.initHelpers('table-tools');});
</script>