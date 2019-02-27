<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="${pageContext.request.contextPath}/assets/css/jquery.searchableSelect.css" rel="stylesheet" type="text/css">
<script src="${pageContext.request.contextPath}/assets/js/jquery-1.7.2.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/jquery.searchableSelect.js"></script>

<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick="goList(${page})" style="cursor: pointer;">工地管理  </a> >工地<c:if test="${not empty building}">编辑</c:if><c:if test="${empty building}">新增</c:if></small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
    <div class="col-md-12">
        <form autocomplete="off" class="form-horizontal push-10-t" action="" method="post" onsubmit="return buildingSave();" id="buildingForm">
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">工地名称</label>
		                   <div>
		                  		<input class="form-control" type="text" id="buildingName" name="buildingName" placeholder="" value="${building.buildingName}" maxlength="25">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">工地负责人</label>
	                        <div>
		                   		<select class="form-control" style="height:35px;" id="userId" name="managerId"  data-placeholder="" required="required">
		                  			<c:forEach items="${users }" var="user">
		                  				<c:choose>
						   					<c:when test="${building.managerId == user.id }">
						   						<option value="${user.id }" selected="selected">${user.username}</option>
							   				</c:when>
						   					<c:otherwise>
						   						<option value="${user.id }">${user.username}</option>
						   					</c:otherwise>
										</c:choose>
		                  			</c:forEach>
									<script>
                                        $(function(){
                                            $('#userId').searchableSelect();
                                        });
									</script>
		                  		</select>
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">开工日期</label>
		                   <div>
		                   	<input type="text" class="laydate-input" id="startTimeStr" name="startTimeStr" value="${building.startTimeStr}">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">面积（㎡）</label>
		                   <div>
		                  		<input class="form-control" type="number" id="area" name="area" placeholder="" value="${building.area}" maxlength="4">
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
function buildingSave()
{
	var buildingId = "${buildingId}";
	hideTips();
	tipSuccess("正在保存，请耐心等待");
	$('#saveButton').attr("disabled",true);
	$('#cancelButton').attr("disabled",true);
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/building/buildingSave?id=${buildingId}",
        data: $('#buildingForm').serialize(),
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
        		if(buildingId == 0)
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

laydate.render({
	elem: '#startTimeStr',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});
</script>