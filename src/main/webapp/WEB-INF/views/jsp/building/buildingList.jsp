<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
	<div class="row items-push">
		<div class="col-sm-7">
			<h1 class="page-heading">
				<small><a onclick="goMain()">首页</a> > 工地列表</small>
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
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="receivingAddressTitle">楼盘：</div>
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

			<div class="common-query-unit">
				<div class="form-group">
					<div class="col-xs-12">
						<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="新增工地" onclick="buildingEdit('0')"><i class="fa fa-plus"></i> 新增工地</button>
					</div>
				</div>
			</div>


		</div>


	</form>


	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
		<thead>
		<tr role="row">
			<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">工地名称</th>
			<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">项目经理</th>
			<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">开始时间</th>
			<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">面积（㎡）</th>
			<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">创建时间</th>
			<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
		</tr>
		</thead>
		<tbody>
		<c:if test="${empty buildingList}"><tr><td colspan="10" align="center">暂无任何工地</td></tr></c:if>
		<c:forEach items="${buildingList}" var="building">
			<tr>
				<td>${building.buildingName}</td>
				<td>${building.managerName}</td>
				<td>${building.startTimeStr}</td>
				<td>${building.area}</td>
				<td>${building.createTimeStr}</td>
				<td class="text-center">
					<div class="btn-group">
						<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="编辑工地" onclick="buildingEdit('${building.id}')"><i class="fa fa-pencil"></i></button>
						<button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="删除工地" onclick="confirmDelete('${building.id}')"><i class="fa fa-times"></i></button>
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
        goMenu("/admin/building/buildingList", page,$("#searchForm").serialize());
    }

    function buildingEdit(buildingId, buildingName)
    {
        ajax({
            type: "get",
            dataType: "html",
            url: "/admin/building/buildingEdit?page=${page}&buildingId="+buildingId,
            timeout:5000,
            data: {
                buildingName : buildingName
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

    function confirmDelete(buildingId){
        confirmDialog({
            title:"信息确认",
            content: "你是否要删除此工地",
            confirm: function(){
                buildingDelete(buildingId);
            }
        });
    }

    function buildingDelete(buildingId){
        tipSuccess("正在删除...");
        ajax({
            type: "POST",
            dataType: "json",
            url: "/admin/building/buildingDelete?buildingId="+buildingId,
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