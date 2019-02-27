<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 数据统计</small>
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
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="receivingAddressTitle">工地：</div>
			    	<div style="width: 65%;display: inline-block;">
			    		<input class="form-control" style="height:28px;width: 87%;" type="text" id="receivingAddress" name="receivingAddress" value=${receivingAddress }>
			    		<input type="hidden" id="managerId" name="receivingbuildingId" value= "${receivingbuildingId}" />
					</div>
		    	</div>
			</div>
			
			<div class="common-query-unit" id="searchTitleDiv">
				<div class="form-group">
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="managerIdTitle">工地负责人：</div>
			    	<div style="width: 65%;display: inline-block;">
			    		<input class="form-control" style="height:28px;width: 87%;" type="text" id="managerName" name="managerName" value=${managerName }>
			    		<input type="hidden" id="managerId" name="managerId" value= "${managerId}" />
					</div>
		    	</div>
			</div>

				<div class="common-query-unit" id="searchTitleDiv">
					<div class="form-group">
						<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">开始时间：</div>
				    	<div style="width: 65%;display: inline-block;">
				    		<input type="text" class="laydate-input" id="beginTime" name="beginTime" >
						</div>
			    	</div>
				</div>
				
				<div class="common-query-unit" id="searchTitleDiv">
					<div class="form-group">
						<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">结束时间：</div>
				    	<div style="width: 65%;display: inline-block;">
				    		<input type="text" class="laydate-input" id="endTime" name="endTime" >
						</div>
			    	</div>
				</div>
			</div>

			<div class="row">
				<div class="common-query-unit">
					<div class="form-group">
				    	<div class="col-xs-12">
							<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="查询" onclick="goList('1')">查询</button>
				        </div>
				    </div>
				</div>
				<!-- <div class="common-query-unit">
					<div class="form-group">
				    	<div class="col-xs-12">
							<button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;" data-toggle="tooltip" title="导出excel" onclick="exportExcel()">导出excel</button>
				        </div>
				    </div>
				</div> -->
			</div>
	</form>
	
	<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
       <thead>
            <tr role="row">
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">门店</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">工地负责人</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">工地</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">下单日期</th>
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">金额</th>
        		<c:forEach items="${mertchmap}" var="md">
	            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">${md.value}</th>
            	</c:forEach>
            </tr>
        </thead>
        <tbody>
        		<tr>
        		<td colspan="4">合计</td>
        		<td>${data.allPrice}</td>
        		<c:set var="merch" value="${data.merchandise}"></c:set>
        		<c:forEach items="${mertchmap}" var="md">
        		<c:set var="mdid" value="${md.key}"></c:set>
        		<td>${merch[mdid].number}</td>
        		</c:forEach>
        		</tr>
        		
        		<c:forEach items="${data.child}" var="stat1">
	        		<tr>
	        		<td rowspan="${stat1.value.rowspan + 2}">${stat1.value.storesName}</td>
	        		<td colspan="3">合计</td>
	        		<td>${stat1.value.allPrice}</td>
	        		<c:set var="merch" value="${stat1.value.merchandise}"></c:set>
	        		<c:forEach items="${mertchmap}" var="md">
	        		<c:set var="mdid" value="${md.key}"></c:set>
	        		<td>${merch[mdid].number}</td>
	        		</c:forEach>
	        		</tr>
        		
		        		<c:forEach items="${stat1.value.child}" var="stat2">
		        		<tr>
		        		<td rowspan="${stat2.value.rowspan + 1}">${stat2.value.managerName}</td>
		        		<td colspan="2">合计</td>
		        		<td>${stat2.value.allPrice}</td>
		        		<c:set var="merch" value="${stat2.value.merchandise}"></c:set>
		        		<c:forEach items="${mertchmap}" var="md">
		        		<c:set var="mdid" value="${md.key}"></c:set>
		        		<td>${merch[mdid].number}</td>
		        		</c:forEach>
		        		</tr>
        		
			        		<c:forEach items="${stat2.value.child}" var="stat3">
			        		<tr>
			        		<td rowspan="${stat3.value.rowspan}">${stat3.value.receivingAddress}</td>
			        		<td colspan="1">合计</td>
			        		<td>${stat3.value.allPrice}</td>
			        		<c:set var="merch" value="${stat3.value.merchandise}"></c:set>
			        		<c:forEach items="${mertchmap}" var="md">
			        		<c:set var="mdid" value="${md.key}"></c:set>
			        		<td>${merch[mdid].number}</td>
			        		</c:forEach>
			        		</tr>
        		
				        		<c:forEach items="${stat3.value.child}" var="stat4">
				        		<tr>
				        		<td rowspan="${stat4.value.rowspan}">${stat4.value.modificationTime}</td>
				        		<td>${stat3.value.allPrice}</td>
				        		<c:set var="merch" value="${stat3.value.merchandise}"></c:set>
				        		<c:forEach items="${mertchmap}" var="md">
				        		<c:set var="mdid" value="${md.key}"></c:set>
				        		<td>${merch[mdid].number}</td>
				        		</c:forEach>
				        		</tr>
				        		</c:forEach>
				        		
        					</c:forEach>
        					
        				</c:forEach>
        				
        		</c:forEach>
            
        </tbody>
    </table>
</div>    
<!-- END Page Content -->

<script type="text/javascript">
//覆盖goList方法，分页自动调用该方法
function goList(page)
{
	goMenu("/admin/data/datastat", page, $("#searchForm").serialize());
}

var managers =[];
<%
Object ob1=request.getAttribute("managers");
Object ob2=request.getAttribute("buildings");
%>
managers= <%out.println(JSON.toJSONString(ob1));%>
buildings= <%out.println(JSON.toJSONString(ob2));%>
$(function () {
	  $('#managerName').autocompleter({
		  	highlightMatches: true,
	        // object to local or url to remote search
	        source: managers,
	        // show hint
	        hint: true,
	        // abort source if empty field
	        empty: true,
	        // max results
	        limit: 5,
	        callback: function (value, index,item) {
	        	if(item && item.id){
	        		$("#managerId").val(item.id);
	        	}else{
	        		$("#managerId").val("");
	        	}
	        }
	    });
	  $('#managerName').blur(function(){
		  if(!$(this).val()){
			  $("#managerId").val("");
		  }
	  });
	  
	  
	  $('#receivingAddress').autocompleter({
		  	highlightMatches: true,
	        // object to local or url to remote search
	        source: buildings,
	        // show hint
	        hint: true,
	        // abort source if empty field
	        empty: true,
	        // max results
	        limit: 5,
	        callback: function (value, index,item) {
	        	if(item && item.id){
	        		$("#receivingbuildingId").val(item.id);
	        	}else{
	        		$("#receivingbuildingId").val("");
	        	}
	        }
	    });
	  $('#receivingAddress').blur(function(){
		  if(!$(this).val()){
			  $("#receivingbuildingId").val("");
		  }
	  });
	  
	});
	

laydate.render({
	elem: '#beginTime',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});

laydate.render({
	elem: '#endTime',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});
</script>