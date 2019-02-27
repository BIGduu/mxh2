<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick="goList(${page})" style="cursor: pointer;">角色管理  </a> > 角色<c:if test="${roleId != '0'}">编辑</c:if><c:if test="${roleId == '0'}">新增</c:if></small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
    <div class="col-md-12">
        <form autocomplete="off" class="form-horizontal push-10-t" action="" method="post" onsubmit="return roleSave();" id="roleForm">         
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">名称</label>
		                   <div>
	                        	<input class="form-control" type="text" id="roleName" name="roleName" placeholder="" value="${role.roleName}">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">可查询订单状态</label>
		                   <div class="checkbox">
		                   		<c:forEach items="${stateList }" var="stateBean">
								  	<label class="checkbox-inline">
							  			<c:choose>
						   					<c:when test="${stateBean.checked}">
						   						<input type="checkbox" id="stateList" name="stateList" value="${ stateBean.id}" checked="checked"> ${ stateBean.name} 
							   				</c:when>
						   					<c:otherwise>
						   						<input type="checkbox" id="stateList" name="stateList" value="${ stateBean.id}"> ${ stateBean.name} 
						   					</c:otherwise>
										</c:choose>
									</label>
								</c:forEach>
							</div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-6 col-md-8 col-sm-8 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">可操作订单审核状态</label>
		                   <div class="checkbox">
		                   		<c:forEach items="${checkList }" var="checBean">
								  	<label class="checkbox-inline">
							  			<c:choose>
						   					<c:when test="${checBean.checked}">
						   						<input type="checkbox" id="checkList" name="checkList" value="${ checBean.id}" checked="checked"> ${ checBean.name} 
							   				</c:when>
						   					<c:otherwise>
						   						<input type="checkbox" id="checkList" name="checkList" value="${ checBean.id}"> ${ checBean.name} 
						   					</c:otherwise>
										</c:choose>
									</label>
								</c:forEach>
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
function roleSave()
{
	var roleId = "${roleId}";
	hideTips();
	tipSuccess("正在保存，请耐心等待");
	$('#saveButton').attr("disabled",true);
	$('#cancelButton').attr("disabled",true);
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/role/roleSave?roleId=${roleId}",
        data: $('#roleForm').serialize(),
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
        		if(roleId == '0')
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