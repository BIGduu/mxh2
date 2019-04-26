<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > 新增订单</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
<form class="form-horizontal" autocomplete="off" action="" method="post" onsubmit="return false;" id="addOrderForm">
	<div class="row">
		<div class="common-query-unit">
			<div class="form-group">
				<div style="line-height: 25px;display: inline-block;padding-left:15px;">门店:</div>
				<div style="width: 65%;display: inline-block;">
					<input class="form-control" style="height:28px;width: 87%;" type="text" id="storesName" name="storesName" disabled value="${storesName}" />
					<input type="hidden" id="storesId" name="storesId" disabled value="${storesId}" />
				</div>
			</div>
		</div>
		<div class="common-query-unit">
			<div class="form-group">
				<div style="line-height: 25px;display: inline-block;padding-left:15px;" >项目经理:</div>
				<div style="width: 65%;display: inline-block;">
                    <select id="managerId" name="managerId"
                            onchange="getBuildingList()">
                        <option>请选择</option>
                        <c:forEach items="${userList}" var="user">
                            <option value="${user.id}">${user.username}</option>
                        </c:forEach>
                    </select>
					<%--<input class="form-control" style="height:28px;width: 87%;" type="text" id="managerName" name="managerName" value="">--%>
					<%--<input type="hidden" id="managerId" name="managerId" value= "" />--%>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
		<div class="common-query-unit" >
			<div class="form-group">
				<div style="line-height: 25px;display: inline-block;padding-left:15px;" >收货地址:</div>
				<div style="width: 65%;display: inline-block;">
                    <select id="receivingAddress" name="receivingAddress">
                        <option>请选择</option>
                        <%--<c:forEach items="${buildingList}" var="building">--%>
                            <%--<option value="${building.buildingName}">${building.buildingName}</option>--%>
                        <%--</c:forEach>--%>
                    </select>
					<%--<input class="form-control" style="height:28px;width: 87%;" type="text" id="receivingAddress" name="receivingAddress" value="">--%>
				</div>
			</div>
		</div>
		<div class="common-query-unit" >
			<div class="form-group">
				<div style="line-height: 25px;display: inline-block;padding-left:15px;" >货物送达时间:</div>
		    	<div style="width: 60%;display: inline-block;">
		    		<input type="text" class="laydate-input" id="senttoTime" name="senttoTime" >
				</div>
	    	</div>
		</div>
	</div>
	<div class="row">
		<div class="common-query-unit">
			<div class="form-group">
				<div style="line-height: 25px;display: inline-block;padding-left:15px;" >是否步梯上楼:</div>
				<div style="width: 60%;display: inline-block;">
					<input type="radio" name="upstairs" value="0"/>否 <input type="radio" name="upstairs" value="1"/>是
				</div>
			</div>
		</div>
		<div class="common-query-unit" id="searchTitleDiv">
			<div class="form-group">
					<div style="line-height: 25px;display: inline-block;padding-left:15px;" id="managerIdTitle">订单类型：</div>
			    	<div style="width: 65%;display: inline-block;">
						<select id="orderType" name="orderType" class="js-select2"  style="width:90%;margin-top: -2px;" data-placeholder="">
			            	<option value="1">配货订单</option>
			            	<option value="2">退货订单</option>
			            </select>
		            </div>
		    </div>
		</div>
	</div>
</form>
	订单详情:
	<div>
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
            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">数量</th>
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
	                <td class="text-center">
	                	<div style="display:none">${merchandise.total}</div>
	                	<input type="text" name="number" value=""/>
	                	<input type="hidden" name="merchandiseId" value="${merchandise.id}"/>
	                </td>
	            </tr>
            </c:forEach>
        </tbody>
    </table>
    </div>
    <div class="btn-group">
	     <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="提交订单" onclick="submitOrder()">提交订单</button>
	</div>
</div>

<script type="text/javascript">
    var buildingIdArray = new Array();
    var buildingNameArray = new Array();
    var buildingManagerIdArray = new Array();

    $(function () {
        <c:forEach items="${buildingList}" var="building">
            buildingIdArray.push(${building.id});
            buildingNameArray.push("${building.buildingName}");
            buildingManagerIdArray.push(${building.managerId});
        </c:forEach>
    });

    function getBuildingList() {
        $("#receivingAddress option").remove();
        $("#receivingAddress").prepend("<option type='hidden'>请选择</option>");//添加第一个option值
        var managerId = $("#managerId").val();
        for (i = 0; i < buildingNameArray.length; i++) {
            console.log(buildingManagerIdArray[i],managerId);
            if (buildingManagerIdArray[i]==managerId) {
                $("#receivingAddress").append("<option value='" + buildingNameArray[i] + "'>" + buildingNameArray[i] + "</option>");
            }
        }
    }


function goList(page)
{
	goMenu("/admin/order/custOrderList", page);
}

function submitOrder(){
	var managerId = $("#managerId").val();
	if(!managerId||managerId==''){
		alert("请选择项目经理");
		return false;
	}
	var receivingAddress = $("#receivingAddress").val();
	if(!receivingAddress || $.trim(receivingAddress)==''){
		alert("请输入收货地址");
		return false;
	}
	var senttoTime = $("#senttoTime").val();
	if(!senttoTime){
		alert("请输入货物送达时间");
		return false;
	}
	var upstairs =$("input[name='upstairs']:checked").val();
	if(!upstairs){
		alert("请选择是否上楼");
		return false;
	}
	var orderType = $("#orderType").val();
	if(!orderType||orderType==''){
		alert("请选择订单类型");
		return false;
	}
	var orderInfoStr =[];
	$("input[name='number']").each(function(){
		var numberValue=$(this).val();
		if(numberValue && numberValue>0){
			orderInfoStr.push({
				number:numberValue,
				merchandiseId:$(this).siblings("input[name='merchandiseId']").val()
				});
		}
	});
	if(orderInfoStr.length==0){
		alert("请输入需要下单产品的数量");
		return false;
	}
	orderInfoStr = JSON.stringify(orderInfoStr);
	tipSuccess("正在提交...");
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/order/orderAdd",
		data: {
			orderInfoStr: orderInfoStr,
			receivingAddress:receivingAddress,
			upstairs:upstairs,
			managerId:managerId,
			orderType:orderType
		},
		timeout:5000,
		error: function() 
		{
			tipFail("网络异常，请稍后再试");
		},
        success: function(data)
        {
        	if(data.status == "200"){
        		tipSuccess("提交成功");
        		goList(1);
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


var data =[];
<%
Object ob=request.getAttribute("users");
%>
data= <%out.println(JSON.toJSONString(ob));%>
$(function () {
	  $('#managerName').autocompleter({
		  	highlightMatches: true,
	        // object to local or url to remote search
	        source: data,
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
	});

laydate.render({
	elem: '#senttoTime',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});
</script>