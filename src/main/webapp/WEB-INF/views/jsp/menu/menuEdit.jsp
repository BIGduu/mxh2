<%@ page import="net.mxh.entity.Admin" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick="goList(${page})" style="cursor: pointer;">菜单管理  </a> > 菜单<c:if test="${menuId != '0'}">编辑</c:if><c:if test="${menuId == '0'}">新增</c:if></small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->
这是菜单编辑页面
<!-- Page Content -->
<div class="content">
    <div class="col-md-12">
        <form autocomplete="off" class="form-horizontal push-10-t" action="" method="post" onsubmit="return menuSave();" id="menuForm">
            <div class="row">
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                	<label for="material-color-primary">菜单名称</label>
		                    <div>
		                        <input class="form-control" type="text" id="menuName" name="menuName" placeholder="" value="${menu.menuName}">
		                    </div>
		                </div>
		            </div>
            	</div>
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
            		<div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                    <label for="material-color-info">菜单级别</label>
		                    <div>
		                    	<c:if test="${menuId != '0'}">
		                    		<input type="hidden" name="menuLevel" value="${menu.menuLevel}">
		                    		${menu.menuLevel}
		                    	</c:if>
		                    	<c:if test="${menuId == '0'}">
			                        <div class="col-xs-12" style="padding-left: 0px;">
										<label class="css-input css-radio css-radio-info push-10-r">
											<input type="radio" name="menuLevel" checked value="1" onclick="changeParent(1)"><span></span> 1
										</label>
										<label class="css-input css-radio css-radio-info">
											<input type="radio" name="menuLevel" value="2" onclick="changeParent(2)"><span></span> 2
										</label>
									</div>
								</c:if>
		                    </div>
		                </div>
            		</div>
            	</div>
            </div>
            <br>
            <div class="row">
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
            		<div class="form-group" id="menuUrlDiv" style="display: none;">
               			 <div class="col-sm-10 col-xs-12">
	                		<label for="material-color-primary">菜单路径</label>
	                			<div>
	                    			<input class="form-control" type="text" id="linkUrl" name="linkUrl" placeholder="" value="${menu.linkUrl}">
	                  			 </div>    
                    	</div>
               		 </div>
            	</div>
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
           			 <div class="form-group" id="parentMenuDiv" style="display: none;">
                		 <div class="col-sm-10 col-xs-12">
                    		<div class="form-material form-material-info">
                    			<label for="material-color-info">父级菜单</label>
                    			<div>
                    				<input type="hidden" value="${menu.parentId}" id="parentId" name="parentId">
                    				<span id="parentMenuName">${menu.parentName }</span>
                        			<button type="button" class="btn btn-info btn-sm push-5-r push-10" style="margin-top:10px;" onclick="getParentRoleList()"><i class="fa fa-search"></i> 选择</button>
                        		</div>
                   			 </div>
                		</div>
            	</div>
            </div>
            </div>
             <div class="row">
           		<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
            		<div class="form-group" id="menuCssDiv">
                 		<div class="col-sm-10 col-xs-12">
                   		 	<div class="form-material form-material-primary">
                    			<label for="material-color-primary">菜单路径</label>
                    			<div>
                     				<input class="form-control" type="text" id="icon" name="icon" placeholder="" value="${menu.linkUrl}">
                       			</div>
                    		</div>
                		</div>
            		</div>
            	</div>
            </div>
            <div class="row">
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
            		<div class="form-group">
                		<div class="col-sm-10 col-xs-12">
                    		<div class="form-material form-material-primary">
                     			<label for="material-color-primary">显示顺序</label>
                     		<div>
                      			<input class="form-control" type="text" id="showOrder" name="showOrder" placeholder="0-100的正整数" value="${menu.showOrder}">
                       		</div>
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
function menuSave()
{
	var menuId = "${menuId}";
	hideTips();
	var orders = $('#showOrder').val();
	var r = /^\+?[1-9][0-9]*$/;
	if(!r.test(orders) || parseInt(orders) > 100){
		tipFail("显示排序为1-100的正整数");
		return false;
	}
	tipSuccess("正在保存，请耐心等待");
	$('#saveButton').attr("disabled",true);
	$('#cancelButton').attr("disabled",true);
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/menu/menuSave?id=${menuId}",
        data: $('#menuForm').serialize(),
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
        		if(menuId == '0')
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

function getParentRoleList(){
	loadPageDialog({
		url : "/admin/menu/menuParentList?page=1",
		title : "选择母菜单",
		data:{
			parentLevel : 1
		},
		confirm: function(object){
			if(object.selectId){
				$('#parentId').val(object.selectId);
				$('#parentMenuName').html(object.name);
			}
		}
	});
}

function changeParent(level){
	if(level == 1){
		$('#parentMenuDiv').hide();
		$('#menuUrlDiv').hide();
		$('#menuCssDiv').show();
	}
	else{
		$('#parentMenuDiv').show();
		$('#menuUrlDiv').show();
		$('#menuCssDiv').hide();
	}
}
if("0" != "${menuId}"){
	changeParent("${menu.menuLevel}");
}
</script>