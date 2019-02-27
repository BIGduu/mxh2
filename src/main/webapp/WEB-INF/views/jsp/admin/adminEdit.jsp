<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick = "goList(${page})" style="cursor: pointer;">后台用户管理列表  </a> > 用户<c:if test="${adminId != '0'}">编辑</c:if><c:if test="${adminId == '0'}">新增</c:if></small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
    <div class="col-md-12">
        <form autocomplete="off" data-ajax='false' class="form-horizontal push-10-t" action="" method="post" onsubmit="return adminSave();" id="adminForm">
        	<input type="hidden" value="${adminId}" id="adminId" name="adminId">
        	<c:if test="${ empty adminId || adminId == 0}"> 
	           <div class="row">
	            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
			            <div class="form-group">
			                <div class="col-sm-10 col-xs-12">
			                	<label for="material-color-primary">登录账号</label>
			                    <div> 
	                                <input class="form-control" style="height:35px;" type="text"  id="adminName" name="adminName" placeholder="" value="${adminEdit.adminName}">
	                         	</div>
			                </div>
			            </div>
	            	</div>
	            	
	            	
		            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
				            <div class="form-group">
				                <div class="col-sm-10 col-xs-12">
				                	<label for="material-color-primary">密码</label>
				                    <div>
				                    	<input class="form-control" style="height:35px;" type="text" id="password" name="password" placeholder="" value="">
		                             </div>
				                </div>
				            </div>
		            	</div>
	            </div>
            </c:if>
            <div class="row">
                <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                	<label for="material-color-primary">名称</label>
		                    <div> 
		                       <input class="form-control" style="height:35px;" type="text" id="adminLoginName" name="username" placeholder="" value="${adminEdit.username}">
				             </div>
		                </div>
		            </div>
            	</div>
            	
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
			            <div class="form-group">
			                <div class="col-sm-10 col-xs-12">
			                	<label for="material-color-primary">可查看门店</label>
			                    <div>
			                    	<select class="form-control" style="height:35px;" id="storesId" name="storesId" >
			                    	<option value="">全部</option>
										<c:forEach items="${storesList }" var="stores">
											<c:choose>
												<c:when test="${stores.id ==  adminEdit.storesId}"><option value="${stores.id }" selected="selected">${stores.storesName }</option></c:when>
												<c:otherwise><option value="${stores.id }">${stores.storesName }</option></c:otherwise>
											</c:choose>
											
										</c:forEach>
									</select>
	                             </div>
			                </div>
			            </div>
	            	</div>
            </div>
            <div class="row">
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                	<label for="material-color-primary">联系电话</label>
		                    <div>
                                <input class="form-control" style="height:35px;" type="text"  id="telephone" name="telephone" placeholder="" value="${adminEdit.telephone}">
                         	</div>
		                </div>
		            </div>
            	</div>
            	
            	<div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                	<label for="material-color-primary">角色</label>
		                    <div>
		                    	<select class="form-control" style="height:35px;" id="title" name="roleId" >
									<c:forEach items="${roles }" var="role">
										<c:choose>
											<c:when test="${role.id ==  adminEdit.roleId}"><option value="${role.id }" selected="selected">${role.roleName }</option></c:when>
											<c:otherwise><option value="${role.id }">${role.roleName }</option></c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
                             </div>
		                </div>
		            </div>
            	</div>
            	
            </div>             
 
              <br>
            <div class="form-group" >
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
function adminSave()
{
	var adminId = "${adminId}";
	hideTips();
	tipSuccess("正在保存，请耐心等待");
	$('#saveButton').attr("disabled",true);
	$('#cancelButton').attr("disabled",true);
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/admin/adminSave",
        data: $('#adminForm').serialize(),
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
        		if(adminId == '0')
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
</script>