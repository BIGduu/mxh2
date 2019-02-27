<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 菜单管理列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->
这是菜单list
<!-- Page Content -->
<div class="content">
	<!-- 查询表单 -->
	<form class="form-horizontal" autocomplete="off" action="" method="post" onsubmit="return false;">
		<div class="row">
			<div class="common-query-unit">
				<div class="form-group">
					<div class="common-query-label">筛选条件：</div>
			    	<div class="common-query-input">
		        		<select class="js-select2" id="selectId" name="selectId" style="width:90%;" data-placeholder="" data-placeholder="" onchange="changeMenuSelect()">
							<c:forEach items="${selectList}" var="select">
								<c:if test="${selectId == select.id}">
									<option value="${select.id }" selected="selected">${select.name}</option>
								</c:if>
								<c:if test="${selectId != select.id}">
									<option value="${select.id }">${select.name}</option>
								</c:if>
							</c:forEach>
						</select>
			        </div>
			    </div>
			</div>
			<div class="common-query-unit" id="searchNameDiv">
				<div class="form-group">
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="searchNameTitle"></div>
			    	<div style="width: 65%;display: inline-block;">
						<input class="form-control" style="height:28px;width: 87%;" type="text" id="searchName" name="searchName" placeholder="" value=${searchName}>
			        </div>
			    </div>
			</div>
			<div class="common-query-unit">
				<div class="form-group">
			    	<div class="col-xs-12">
						<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="查询" onclick="goList('1')">查询</button>
			        </div>
			    </div>
			</div>
		</div>
	</form>
	
	<button class="btn btn-rounded btn-default btn-sm addButton" type="button" data-toggle="tooltip" title="新增菜单" onclick="menuEdit('0')"><i class="fa fa-plus"></i> 新增菜单</button>
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单名称</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单路径</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">菜单级别</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">菜单样式</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">显示顺序</th>
            	<th class="text-center sorting_disabled" style="width: 100px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty menuList}"><tr><td colspan="10" align="center">暂无任何菜单</td></tr></c:if>
        	<c:forEach items="${menuList}" var="menu">
	            <tr>
	                <td>${menu.menuName}</td>
	                <td style="word-break:break-all;">${menu.linkUrl}</td>
	                <td class="hidden-xs">${menu.menuLevel}</td>
	                <td class="hidden-xs"><i class="${menu.icon}"></i></td>
	                <td class="hidden-xs">${menu.showOrder}</td>
	                <td class="text-center">
	                    <div class="btn-group">
	                        <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="编辑菜单" onclick="menuEdit('${menu.id}')"><i class="fa fa-pencil"></i></button>
	                     	<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="删除" onclick="confirmDelete('${menu.id}', '${menu.menuLevel}')"><i class="fa fa-times"></i></button>
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
	goMenu("/admin/menu/menuList", page, {
		selectId : selectId,
		searchName : searchName
	});
}

function menuEdit(menuId)
{
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/menu/menuEdit?page=${page}&menuId="+menuId,
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

function confirmDelete(menuId,menuLevel){
	confirmDialog({
		title:"信息确认",
		content: "你是否要删除此菜单？",
		confirm: function(){
			menuDelete(menuId,menuLevel);
		}
	});
}

function menuDelete(menuId,menuLevel){
	tipSuccess("正在删除...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/menu/menuDelete",
		timeout:5000,
		data:{
			menuId : menuId,
			menuLevel : menuLevel
		},
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

function changeMenuSelect() {
	if ($('#selectId').val() == '0') {
		$('#searchNameDiv').hide();
	} else if ($('#selectId').val() == '1') {
		$('#searchNameDiv').show();
		$('#searchNameTitle').html('菜单名称：')
	}
}
changeMenuSelect();
</script>

<!-- Page JS Code -->
<script>
$(function () {
    // Init page helpers (BS Datepicker + BS Colorpicker + Select2 + Masked Input + Tags Inputs plugins)
    App.initHelpers(['datepicker', 'colorpicker', 'select2', 'masked-inputs', 'tags-inputs']);
});
</script>