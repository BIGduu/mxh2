<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
		<meta http-equiv="Expires" content="0">
		<meta http-equiv="Pragma" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-store,no-cache">
        <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
        <title>登录</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/comment.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/weui.min.css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/need/layer.css"/>
        
        <script src="${pageContext.request.contextPath}/assets/js/plugins/layer_mobile/layer.js"></script>
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
	                <div class="weui-cell__hd"><label class="weui-label">用户名</label></div>
	                <div class="weui-cell__bd">
	                    <input class="weui-input" type="text"  id="username" name="username" placeholder="请输入用户名" value="">
	                </div>
	            </div>
	            
	            <div class="weui-cell">
	                <div class="weui-cell__hd"><label class="weui-label">密码</label></div>
	                <div class="weui-cell__bd">
	                	<input type="hidden" id="truePassword" name="login-password" value="">
	                    <input class="weui-input" type="password" id="password" name="password" placeholder="请输入密码"  value="">
	                </div>
	                <div class="weui-cell__ft">
			            <i class="weui-icon-warn"></i>
			        </div>
	            </div>
	            
	        </div>
			<div class="weui-btn-area">
			    <a class="weui-btn weui-btn_primary" href="javascript:" id="submitBtn">提交</a>
			    <a href="javascript:;" class="weui-btn weui-btn_primary weui-btn_loading" id="submitBtnLoad"><i class="weui-loading"></i>提交</a>
			</div>
    	</form>
    	<script src="${pageContext.request.contextPath}/assets/js/core/jquery.min.js"></script>
    	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
    	<script src="${pageContext.request.contextPath}/assets/js/common.js"></script>
    	<script src="${pageContext.request.contextPath}/assets/js/core/md5.js"></script>
    	<script type="text/javascript">
    	
    	$(function(){
    		
    		$("#submitBtn").on("click", function() {
    			var $this = $(this);
    			$this.css("display","none");
    			$("#submitBtnLoad").css("display","block");
    			var content = "";
    			var username = $("#username").val();
    			var password = $("#password").val();
    			if(username == "") {
    				content = "用户名不能为空";
    			}
    			if(password == "") {
    				content = "密码不能为空";
    			}
    			if(content) {
    				$this.css("display","block");
        			$("#submitBtnLoad").css("display","none");
    				return mytips(content);
    			}
    			var data = {
    					username: username,
    					password: setPassword(username, password)
    			}
    			
    			$.post("${pageContext.request.contextPath}/web/login", data, function (data) {
    				if(data.status == 200) {
    					window.location.href="${pageContext.request.contextPath}/web/my";
    				} else {
    					mytips(data.message);
    				}
    				$this.css("display","block");
        			$("#submitBtnLoad").css("display","none");
    			});
    		});
    		
    	});
    		
    	</script>
    </body>
</html>
