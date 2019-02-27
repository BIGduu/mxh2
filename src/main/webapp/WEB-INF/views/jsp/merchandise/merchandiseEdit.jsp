<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick="goList(${page})" style="cursor: pointer;">商品管理  </a> >商品<c:if test="${not empty merchandise}">编辑</c:if><c:if test="${empty merchandise}">新增</c:if></small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
    <div class="col-md-12">
        <form autocomplete="off" class="form-horizontal push-10-t" action="" method="post" onsubmit="return merchandiseSave();" id="merchandiseForm" enctype="multipart/form-data">         
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">商品名称</label>
		                   <div>
	                        	<input class="form-control" type="text" id="merchandiseName" name="merchandiseName" placeholder="" value="${merchandise.merchandiseName}" required="required" maxlength="50">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">商品货号</label>
		                   <div>
	                        	<input class="form-control" type="text" id="merchandiseCode" name="merchandiseCode" placeholder="" value="${merchandise.merchandiseCode}" required="required" maxlength="50">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">品牌</label>
		                   <div>
	                        	<input class="form-control" type="text" id="brandName" name="brandName" placeholder="" value="${merchandise.brandName}" maxlength="50">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">规格</label>
		                   <div>
	                        	<input class="form-control" type="text" id="specification" name="specification" placeholder="" value="${merchandise.specification}" required="required" maxlength="50">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">单价</label>
		                   <div>
	                        	<input class="form-control" type="text" id="unitPrice" name="unitPrice" placeholder="" value="${merchandise.unitPrice}" required="required" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">单位</label>
		                   <div>
	                        	<input class="form-control" type="text" id="unit" name="unit" placeholder="" value="${merchandise.unit}" required="required" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>

			<div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">运费</label>
		                   <div>
	                        	<input class="form-control" type="text" id="shippingCost" name="shippingCost" placeholder="" value="${merchandise.shippingCost}" required="required" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">上楼费（/每层每单位）</label>
		                   <div>
	                        	<input class="form-control" type="text" id="upstairsCost" name="upstairsCost" placeholder="" value="${merchandise.upstairsCost}" required="required" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">低于该值提醒仓管安排进货</label>
		                   <div>
	                        	<input class="form-control" type="text" id="bottomLine" name="bottomLine" placeholder="" value="${merchandise.bottomLine}" required="required">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">上传商品图片</label>
		                   <div>
		                   		<c:if test="${not empty merchandise }">
                				 	<input type="hidden" name="image" value="${merchandise.img}"/>
                					<img alt="" class="zoomify" src="${merchandise.img}" id="adImageShow" style="width: 150px;height: 150px;">&nbsp;&nbsp;<button onclick="deleteAdImage(this)" type="button" id="adImageDeleteButton" class="btn btn-danger btn-sm">删除</button>
                					<input style="width: 210px;height: 30px;margin: 10px; display: none;" type= "file" onchange="previewAdvertiseImage(this)" id="adImage" name="file" accept="image/jpeg,image/jpg,image/png">
                				</c:if>
                				<c:if test="${empty merchandise }">
                					<img alt="" src="" id="adImageShow" style="width: 150px;height: 150px;display: none;">&nbsp;&nbsp;<button onclick="deleteAdImage(this)" type="button" id="adImageDeleteButton" class="btn btn-danger btn-sm" style="display: none;">删除</button>
                					<input style="width: 210px;height: 30px;margin: 10px; display: inline-block;" onchange="previewAdvertiseImage(this)" type= "file"  id="adImage" name="file" accept="image/jpeg,image/jpg,image/png">
                				</c:if>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
            </div>
            <div class="form-group">
                <div class="col-sm-9">
                    <button class="btn btn-sm btn-primary" type="submit" id="saveButton">保存</button>
                    <button class="btn btn-sm btn-warning" type="button" id="cancelButton" onclick="goList(${page})">取消</button>
                </div>
            </div>
        </form>
	</div>
</div>    
<!-- END Page Content -->

<script type="text/javascript">
function merchandiseSave()
{
	var merchandiseId = "${merchandiseId}";
	hideTips();
	tipSuccess("正在保存，请耐心等待");
	$('#saveButton').attr("disabled",true);
	$('#cancelButton').attr("disabled",true);
	var formData = new FormData($('#merchandiseForm')[0]);
	ajaxFile({
		type: "POST",
        dataType: "json",
        url: "/admin/merchandise/merchandiseSave?id=${merchandiseId}",
        data: formData,
		timeout:5000,
		error: function(request) 
		{
			tipFail("网络异常，请稍后再试");
			$('#saveButton').attr("disabled",false);
			$('#cancelButton').attr("disabled",false);
		},
        success: function(data)
        {
        	if(data.status=='200')
       		{
        		tipSuccess("保存成功");
        		if(merchandiseId == 0)
       			{
            		setTimeout('goList(1)',1500);
       			}
        		else
       			{
        			setTimeout('goList(${page})',1500);
       			}
       		}
        	else
       		{
        		tipFail(data.message);
        		$('#saveButton').attr("disabled",false);
    			$('#cancelButton').attr("disabled",false);
       		}
        }
	});
	return false;
}

function deleteAdImage(){
	$('#adImageShow').hide();
	$('#adImage').css("display","inline-block");
	$('#adImage').val('');
	$('#adImageDeleteButton').hide();
}

function previewAdvertiseImage(file){
	if (file.files && file.files[0]){
		var fileData = file.files[0];
		if((fileData.type).indexOf("image/png")==-1 && (fileData.type).indexOf("image/jpg")==-1 && (fileData.type).indexOf("image/jpeg")==-1){
			tipFail("必须选择图片jpg或者png格式的图片");
			file.value = "";
			return;
		}
        //读取图片数据
        var reader = new FileReader();
        reader.onload = function (e) {
            var data = e.target.result;
            //加载图片获取图片真实宽度和高度
            var image = new Image();
            image.onload=function(){
               	hideTips();
               	var srcs = getObjectURL(file.files[0]);   //获取路径
           		$('#adImageShow').attr("src",srcs);
           		$('#adImageShow').show();
           		$('#adImageDeleteButton').show();
           		$('#adImage').hide();
            };
            image.src= data;
        };
        reader.readAsDataURL(fileData);
	} 
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