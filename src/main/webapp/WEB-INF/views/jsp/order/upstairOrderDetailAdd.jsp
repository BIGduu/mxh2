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
                <small><a onclick="goMain()">首页</a> > 新增步梯上楼详情</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
		配送单详情:<input type="hidden" name="adminId" id="adminId" value="${adminId}" />
		<div>
		<table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="upstairsDetailTable" role="grid" aria-describedby="DataTables_Table_1_info">
	        <thead>
	            <tr role="row">
	            	<th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">操作</th>
	            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >配送人员</th>
	            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >商品名称</th>
	            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >规格</th>
	            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" >上楼运费（/每层每单位）</th>
	            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">数量</th>
	            	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending">楼层</th>
	            </tr>
	        </thead>
	        <tbody>
	        	<c:if test="${empty upstairsDetailList}"><tr><td colspan="10" align="center">暂无任何配送单详情</td></tr></c:if>
	        	<c:forEach items="${upstairsDetailList}" var="upstairsDetail">
		            <tr>
		            	<td class="text-center">
		            		<button class="btn btn-xs btn-default" type="button"  title="增加" onclick="add(this)">增加</button>
						   	<button class="btn btn-xs btn-default" type="button"  title="删除" onclick="del(this)">删除</button>
		            	</td>
		            	<td>${upstairsDetail.deliveryName}</td>
		                <td>${upstairsDetail.merchandiseName}</td>
		                <td>${upstairsDetail.specification}</td>
		                <td>${upstairsDetail.upstairsCost}</td>
		                <td class="text-center">
		                	<input type="text" name="number" value="${upstairsDetail.number}" onchange="calculateCost(this)"/>
		                	<input type="hidden" name="orderId" value="${upstairsDetail.orderId}"/>
		                	<input type="hidden" name="deliveryOrderId" value="${upstairsDetail.deliveryOrderId}"/>
		                	<input type="hidden" name="deliveryName" value="${upstairsDetail.deliveryName}"/>
		                	<input type="hidden" name="deliveryId" value="${upstairsDetail.deliveryId}"/>
		                	<input type="hidden" name="merchandiseName" value="${upstairsDetail.merchandiseName}"/>
		                	<input type="hidden" name="merchandiseId" value="${upstairsDetail.merchandiseId}"/>
		                	<input type="hidden" name="specification" value="${upstairsDetail.specification}"/>
		                	<input type="hidden" name="upstairsCost" value="${upstairsDetail.upstairsCost}"/>
		                </td>
		                <td class="text-center">
		                	<input type="text" name="floor" value="" onchange="calculateCost(this)"/>
		                </td>
		            </tr>
	            </c:forEach>
	        </tbody>
	    </table>
	    </div>
	    <p style="float: right;font-size: 20px;color: coral;">上楼费总额：<span id="totalCost"></span>元
	    
	    </p>
	<form autocomplete="off" class="form-horizontal push-10-t" action="" method="post" onsubmit="return submitDetails();" id="upstairsForm" enctype="multipart/form-data">
	   <div class = "row">
	     <div >
	       <div>
	           <div >
	              <label for="material-color-primary">上传商品图片</label>
	              	<div>
	      				<img alt="" src="" name="adImageShow" style="width: 150px;height: 150px;display: none;" />
	      				<div>
							<button onclick="deleteAdImage(this)" type="button" id="adImageDeleteButton" class="btn btn-danger btn-sm" style="display: none;">删除</button>
	      					<input style="width: 210px;height: 30px;margin: 10px; display: inline-block;" onchange="previewAdvertiseImage(this)" type= "file"  id="adImage" name="files" multiple="multiple" accept="image/jpeg,image/jpg,image/png" />
	      				</div>
	                  </div>
	              </div>
	          </div>
	      </div>
	     </div>
	    <div class="btn-group">
		     <button class="btn btn-xs btn-default" type="submit" data-toggle="tooltip" title="提交配送单详情" >提交配送单详情</button>
		</div>
	</form>
</div>

<script type="text/javascript">
var data =[];
<%
Object ob=request.getAttribute("upstairsDetailList");
%>
data= <%out.println(JSON.toJSONString(ob));%>;
var dataMap ={};
if(data){
	for(i in data){
		var deliveryOrderId = data[i].deliveryOrderId;
		var merchandiseId = data[i].merchandiseId ;
		dataMap[deliveryOrderId+"_"+merchandiseId]=data[i].number;
	}
}

function goList(page)
{
	goMenu("/admin/order/upstairOrderList", page);
}

function add(obj){
	var trObj=$(obj).parents("tr");
	trObj.after("<tr>"+$(trObj).html()+"</tr>");
	trObj.next().find("input[name='number']").val("");
	trObj.next().find("input[name='floor']").val("");
}

function del(obj){
	var trObj=$(obj).parents("tr");
	trObj.remove();
}

function calculateCost(obj){
	if(!obj.value || isNaN(obj.value)){
		alert("请输入正确的数字");
		obj.value="";
		return false;
	}
	if(obj.value<0){
		alert("请不要输入负数");
		obj.value="";
		return false;
	}
	var totalCost = 0;
	var totalNumberMap = {};
	var flag = false;
	$("#upstairsDetailTable tr").each(function(){
		var numberObj = $(this).find("input[name='number']");
		var numberVal = numberObj.val();
		var deliveryOrderIdVal = $(this).find("input[name='deliveryOrderId']").val();
		var merchandiseIdVal = $(this).find("input[name='merchandiseId']").val();
		if(numberVal){
			if(totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal]){
				totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] = totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] + parseFloat(numberVal) ;
			}else{
				totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] =  parseFloat(numberVal) ;
			}
			if(totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] && totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal]>dataMap[deliveryOrderIdVal+"_"+merchandiseIdVal]){
				flag = true;
			}
		}
		var upstairsCost = numberObj.siblings("input[name='upstairsCost']").val();
		var floorVal = $(this).find("input[name='floor']").val();
		if(numberVal && upstairsCost && floorVal){
			totalCost = totalCost + (numberVal * upstairsCost * floorVal);
		}
	});
	if(flag){
		alert("您输入的数量总和超过此配送单的这个产品最大的数量");
		obj.value="";
		return false;
	}
	$("#totalCost").html(totalCost);
}

function submitDetails(){
	var upstairDetailsArray =[];
	var totalCost = 0;
	var totalNumberMap = {};
	var flag = false;
	var orderId ;
	$("#upstairsDetailTable tr").each(function(){
		var upstairDetails={};
		var numberVal = $(this).find("input[name='number']").val();
		var deliveryOrderIdVal = $(this).find("input[name='deliveryOrderId']").val();
		var merchandiseIdVal = $(this).find("input[name='merchandiseId']").val();
		if(numberVal){
			if(totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal]){
				totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] = totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] + parseFloat(numberVal) ;
			}else{
				totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] =  parseFloat(numberVal) ;
			}
			if(totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal] && totalNumberMap[deliveryOrderIdVal+"_"+merchandiseIdVal]>dataMap[deliveryOrderIdVal+"_"+merchandiseIdVal]){
				flag = true;
			}
		}
		var upstairsCostVal = $(this).find("input[name='upstairsCost']").val();
		var floorVal = $(this).find("input[name='floor']").val();
		if(numberVal && upstairsCostVal && floorVal){
			upstairDetails.number=numberVal;
			upstairDetails.upstairsCost=upstairsCostVal;
			upstairDetails.floor=floorVal;
			upstairDetails.orderId=$(this).find("input[name='orderId']").val();
			orderId = $(this).find("input[name='orderId']").val();
			upstairDetails.deliveryOrderId=$(this).find("input[name='deliveryOrderId']").val();
			upstairDetails.deliveryName=$(this).find("input[name='deliveryName']").val();
			upstairDetails.deliveryId=$(this).find("input[name='deliveryId']").val();
			upstairDetails.merchandiseName=$(this).find("input[name='merchandiseName']").val();
			upstairDetails.merchandiseId=$(this).find("input[name='merchandiseId']").val();
			upstairDetails.specification=$(this).find("input[name='specification']").val();
			upstairDetails.upstairsCost=$(this).find("input[name='upstairsCost']").val();
			upstairDetailsArray.push(upstairDetails);
		}
	});
	if(flag){
		alert("您输入的数量总和超过此配送单的这个产品最大的数量");
		return false;
	}
	var upstairDetailsStr = JSON.stringify(upstairDetailsArray);
	var adminIdVal=$("#adminId").val();
	tipSuccess("正在提交...");
	var formData = new FormData($('#upstairsForm')[0]);
	ajaxFile({
		type: "POST",
        dataType: "json",
        url: "/admin/order/upstairImgUpload?orderId="+orderId+"&adminId=${adminId}",
		data: formData,
		timeout:5000,
		error: function() 
		{
		},
        success: function(data)
        {
        }
	});
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/order/upstairDetailSave",
		data: {
			upstairDetailsStr: upstairDetailsStr,
			adminId:adminIdVal,
			orderId:orderId
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
	return false;
}

function deleteAdImage(){
	$('img[name="adImageShow"]').eq(0).siblings("img").remove();
	$('img[name="adImageShow"]').hide();
	$('#adImage').css("display","inline-block");
	$('#adImage').val('');
	$('#adImageDeleteButton').hide();
}

function previewAdvertiseImage(file){
	var files=file.files;
	if(files && files[0]){
		var fileSize=files.length;
		$('img[name="adImageShow"]').eq(0).siblings("img").remove();
		for(var i=0;i<fileSize;i++){
			if(i!=0){
				$('img[name="adImageShow"]').eq(0).after($('img[name="adImageShow"]').eq(0).clone());
			}
		}
		for(var j =0;j< fileSize;j++){
			var fileData=files[j];
			if(!fileData || fileData=='undefined'){
				return;
			}
			if((fileData.type).indexOf("image/png")==-1 && (fileData.type).indexOf("image/jpg")==-1 && (fileData.type).indexOf("image/jpeg")==-1){
				tipFail("必须选择图片jpg或者png格式的图片");
				file.value = "";
				return;
			}
			//读取图片数据
			previewFile(fileData,j);
		}
	}
}

function previewFile(fileData,j){
	 //读取图片数据
    var reader = new FileReader();
    reader.onload = function (e) {
        var data = e.target.result;
        //加载图片获取图片真实宽度和高度
        var image = new Image();
        image.onload=function(){
           	hideTips();
           	var srcs = getObjectURL(fileData);   //获取路径
           	$('img[name="adImageShow"]').eq(j).attr("src",srcs);
           	$('img[name="adImageShow"]').eq(j).show();
       		$('#adImageDeleteButton').show();
       		// $('#adImage').hide();
        };
        image.src= data;
    };
    reader.readAsDataURL(fileData);
}

function getObjectURL(file) {
    var url = null;
    if (window.createObjectURL != undefined) {
        url = window.createObjectURL(file)
    } else if (window.URL != undefined) {
        url = window.URL.createObjectURL(file)
    } else if (window.webkitURL != undefined) {
        url = window.webkitURL.createObjectURL(file)
    }
    return url
}
</script>