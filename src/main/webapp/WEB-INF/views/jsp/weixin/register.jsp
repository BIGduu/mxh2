<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-store,no-cache">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>用户注册</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css">
        <style type="text/css">
        	#submitBtnLoad {
        		display: none;
        	}
        </style>
    </head>
    <body>
    	<form id="registerForm" action="" method="post">
	        <div class="weui-cells weui-cells_form">
	        	<div class="weui-cell">
	                <div class="weui-cell__hd"><label class="weui-label">姓名</label></div>
	                <div class="weui-cell__bd">
	                    <input class="weui-input" type="text"  id="username" name="username" placeholder="请输入姓名" maxlength="10" value="">
	                </div>
	            </div>
	            
	            <div class="weui-cell">
	                <div class="weui-cell__hd">
	                    <label class="weui-label">手机号</label>
	                </div>
	                <div class="weui-cell__bd">
	                    <input class="weui-input" type="tel" id="telephone" name="telephone" placeholder="请输入手机号" maxlength="20" value="">
	                </div>
	                <div class="weui-cell__ft">
			            <i class="weui-icon-warn"></i>
			        </div>
	            </div>
	            
	            <div class="weui-cell weui-cell_select weui-cell_select-after">
		            <div class="weui-cell__hd"> <label class="weui-label">部门</label></div>
			        <div class="weui-cell__bd">
			            <select class="weui-select" id="department" name="departmentId">
			            	<c:forEach items="${departmentList }" var="department">
			            		<option value="${department.id }">${department.departmentName }</option>
			            	</c:forEach>
			            </select>
			        </div>
			    </div>
			    
	            <div class="weui-cell weui-cell_select weui-cell_select-after stores">
	            	<div class="weui-cell__hd"> <label class="weui-label">门店</label></div>
			        <div class="weui-cell__bd">
			            <select class="weui-select" id="stores" name="storesId">
			            	<c:forEach items="${storesList }" var="stores">
			            		<option value="${stores.id }">${stores.storesName }</option>
			            	</c:forEach>
			            </select>
			        </div>
			    </div>

	        </div>
			<div class="weui-btn-area">
			    <a class="weui-btn weui-btn_primary" href="javascript:" id="submitBtn">提交</a>
			    <a href="javascript:;" class="weui-btn weui-btn_primary weui-btn_loading" id="submitBtnLoad"><i class="weui-loading"></i>提交</a>
			    <a href="${pageContext.request.contextPath}/web/login" class="weui-btn weui-btn_default">仓管登录</a>
			</div>
    	</form>
    	
    	<script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
    	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
		<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
    	<script type="text/javascript">
    	
    	$(function(){
    		
    		$("#submitBtn").on("click", function() {
    			var $this = $(this);
    			$this.css("display","none");
    			$("#submitBtnLoad").css("display","block");
    			var content = "";
    			if($("#username").val() == "") {
    				content = "用户名不能为空";
    			}
    			if($("#telephone").val() == "") {
    				content = "手机号不能为空";
    			} else if(!isMobile($("#telephone").val())) {
    				content = "请输入正确的手机格式";
    			}
    			if(content) {
    				$this.css("display","block");
        			$("#submitBtnLoad").css("display","none");
    				return mytips(content);
    			}
    			var formData = $("#registerForm").serialize();
    			var departmentId = $("#department").val();
    			formData += "&departmentName=" + $("#department option:selected").text();
    			if(departmentId == 1) {
	    			formData += "&storesName=" + $("#stores option:selected").text();
    			}
    			
    			$.post("${pageContext.request.contextPath}/web/register", formData, function (data) {
    				if(data.status == 200) {
    					window.location.href="${pageContext.request.contextPath}/web/my";
    				} else {
    					mytips(data.message);
    				}
    				$this.css("display","block");
        			$("#submitBtnLoad").css("display","none");
    			});
    		});
    		
    		$("#department").on("change", function() {
				console.log("change");
				if($(this).val() == 2) {
					$(".stores").css("display", "none");
				} else {
					$(".stores").css("display", "");
				}
			});
    	});
    		
    	</script>
    </body>
</html>
