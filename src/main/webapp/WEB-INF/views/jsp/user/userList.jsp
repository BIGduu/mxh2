<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 用户管理列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

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
						<input class="form-control" style="height:28px;width: 87%;" type="text" id="searchName" name="searchName" placeholder="" value=${searchName }>
			        </div>
			    </div>
			</div>
			
			<c:if test="${storesList.size() > 0}">
				<div class="common-query-unit" id="searchTitleDiv">
					<div class="form-group">
						<div style="line-height: 25px;display: inline-block;padding-left:15px;">门店：</div>
				    	<div style="width: 65%;display: inline-block;">
			    			<select class="js-select2" name="storesId" style="width:90%;margin-top: -2px;" data-placeholder="">
						      	<option value="">请选择：</option>
		                    	<c:forEach items="${storesList }" var="stores">
	                    			<c:choose>
					   					<c:when test="${stores.id == storesId }"> 
					   						<option value="${stores.id }" selected="selected">${stores.name }</option>
						   				</c:when>
					   					<c:otherwise> 
					   						<option value="${stores.id }">${stores.name }</option>
					   					</c:otherwise>
									</c:choose>
		                        	
		                    	</c:forEach>
	                       	</select>
						</div>
			    	</div>
				</div>
			</c:if>
			
			<div class="common-query-unit">
				<div class="form-group">
			    	<div class="col-xs-12">
						<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="查询" onclick="goList('1')">查询</button>
			        </div>
			    </div>
			</div>
		</div>
	</form>
	
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">编号</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">姓名</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">注册日期</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">手机</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">部门</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">门店</th>
            	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 70px;">状态</th>
            	<th class="text-center sorting_disabled" style="width: 100px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
            </tr>
        </thead>
        <tbody>
        	<c:if test="${empty userList}"><tr><td colspan="10" align="center">暂无任何用户</td></tr></c:if>
        	<c:forEach items="${userList}" var="user">
	            <tr>
	                <td style="word-break:break-all;">${user.id}</td>
	                <td>${user.username}</td>
	                <td class="hidden-xs">${user.createTimeStr}</td>
	                <td class="hidden-xs">${user.telephone }</td>
	                <td class="hidden-xs">${user.departmentName}</td>
	                <td class="hidden-xs">${user.storesName}</td>
	                <td class="hidden-xs">
                		<c:choose>
                			<c:when test="${user.status == 1}">未审核</c:when>
                			<c:when test="${user.status == 2}">审核未通过</c:when>
                			<c:when test="${user.status == 3}">审核通过</c:when>
                			<c:when test="${user.status == 4}">注销</c:when>
                		</c:choose>
					</td>
	                <td class="text-center">
	                    <div class="btn-group">
	                    	<c:if test="${user.status ==  1}"> 
	                    		<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="通过" onclick="userEdit('${user.id}', '3')">通过</button>
	                    		<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="拒绝" onclick="userEdit('${user.id}', '2')">拒绝</button>
	                    	</c:if>
	                    	
	                    	<c:if test="${not empty user.isSign && user.isSign == 0}"> 
	                    		<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="补签" onclick="sign(${user.id})">补签</button>
	                    	</c:if>
	                     	<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="删除" onclick="confirmDelete('${user.id}')"><i class="fa fa-times"></i></button>
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
	goMenu("/admin/user/userList", page, {
		selectId : selectId,
		searchName : searchName
	});
}

function userEdit(userId, status)
{
	ajax({
		type: "POST",
        dataType: "html",
        url: "/admin/user/userEdit?status="+ status +"&userId="+userId,
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

function confirmDelete(userId,userLevel){
	confirmDialog({
		title:"信息确认",
		content: "你确认要注销此用户？注销后无法再恢复此用户！！！",
		confirm: function(){
			userDelete(userId,userLevel);
		}
	});
}

function userDelete(userId,userLevel){
	tipSuccess("正在注销...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/user/userDelete",
		timeout:5000,
		data:{
			userId : userId
		},
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	if(data.status == "200"){
        		tipSuccess("注销成功，正在刷新页面");
        		goList("${page}");
        	}
        	else{
        		tipFail(data.message);
        	}
        }
	});
}

function sign(userId){
	tipSuccess("正在补签...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/user/sign",
		timeout:5000,
		data:{
			userId : userId
		},
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	if(data.status == "200"){
        		tipSuccess("补签成功，正在刷新页面");
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
		$('#searchNameTitle').html('用户名称：')
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