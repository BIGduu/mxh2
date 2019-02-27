<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 9]>         
<html class="ie9 no-focus"> <![endif]-->
<!--[if gt IE 9]> -->
<html class="no-focus">
<!--<![endif]-->
<jsp:include page="head.jsp"></jsp:include>
<jsp:include page="commonScript.jsp"></jsp:include>

<link href="${pageContext.request.contextPath}/assets/css/base/bootstrap.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/base/bootstrap-responsive.min.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/base/style.css" rel="stylesheet">
<link href="${pageContext.request.contextPath}/assets/css/base/style-responsive.css" rel="stylesheet">

<body onload="judgetBrower();">
<!-- Login Content -->
<div class="container-fluid-full" id="alldiv" style="display: none">
	<div class="row-fluid">
		<div class="row-fluid">
			<div id="loginboxid" class="login-box">
				<form method="post" action="" autocomplete="off" onsubmit="return goLogin()">
					<h2 id="yhdl">用户登录</h2>
					<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
                    <div class="col-sm-12 col-lg-12 alert alert-error alert-dismissable" style="display: none;" id="failTips"></div>
                    
					<div class="input-prepend" id="Username" title="Username">
						<i class="user-icon  user" id="Usernamei"></i>
						<div style="position: absolute; margin: auto; top: 0; left: 18.75%; bottom: 0; background-color: #fff; width: 1.7px; height: 32px;" id="xian1"></div>
						<input style="background-color: transparent; border: 0; height: 40px; width: 72%; color: #fff; font-size: 16px; margin: 4px 0px;"
							autocomplete="off" name="usercode" id="usercode" type="text"
							placeholder="请输入用户名"  value=""/>
					</div>

					<div class="input-prepend" id="Password" title="Password">
						<i class="pass-icon pass" id="Passwordi"></i>
						<div
							style="position: absolute; margin: auto; top: 0; left: 18.75%; bottom: 0; background-color: #fff; width: 2px; height: 32px;"
							id="xian2"></div>
						<input type="hidden" id="truePassword" name="login-password"
							value=""> <input
							style="background-color: transparent; border: 0; height: 40px; width: 72%; color: #fff; font-size: 16px; margin: 4px 0px;"
							autocomplete="off" name="password" id="passwordp"
							type="password" placeholder="输入密码"  value=""/>
					</div>

					<div class="button-login" id="divbutton">
						<button type="submit" id="submitid"
							style="background-color: #69A3D1; height: 48px; width: 100%; border-radius: 8px; border: none; font-size: 18px; color: #fff">登录</button>
					</div>
				</form>

			</div>
			<!--/span-->
		</div>
		<!--/row-->
	</div>
	<!--/.fluid-container-->
</div>
<!--/fluid-row-->
<!-- END Login Content -->

<!-- Page JS Plugins -->
<script src="${pageContext.request.contextPath}/assets/js/plugins/jquery-validation/jquery.validate.min.js"></script>
<!-- Page JS Code -->
<script src="${pageContext.request.contextPath}/assets/js/core/md5.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/login/init_login_page.js"></script>

<script type="text/javascript">
function judgetBrower() {
	var agent = navigator.userAgent.toLowerCase(); //得到浏览器机构名
	var regStr_ie = /msie [\d.]+;/gi;
	var flag = true;
	//IE
	if (agent.indexOf("msie") > 0) {
		var browser = agent.match(regStr_ie);
		var version = (browser + "").replace(/[^0-9.]/ig, "");
		if (version <= 9.0) {
			flag = false;
			window.location.href = "${pageContext.request.contextPath}/navigator";
		}
	}
}

function goLogin() {
	if (!setPassword()) {
		return false;
	}
	$('#submitid').attr("disabled", true);
	hideTips();
	ajax({
		type : "POST",
		dataType : "json",
		url : "/admin/login",
		data : {
			adminName : $('#usercode').val(),
			password : $('#truePassword').val(),
			validate : $('#validate').val()
		},
		timeout : 6000,
		error : function(request) {
			tipFail("网络异常，请稍后再试");
			$('#submitid').attr("disabled", false);
		},
		success : function(data) {
			if (data.status == '200') {
				if(data.data.audio) {
					audio();
				}
				tipSuccess("登录成功，正在跳转页面");
				window.location.href = "${pageContext.request.contextPath}/admin/main";
			} else {
				tipFail(data.message);
				$('#validate').val('');
				$('#submitid').attr("disabled", false);
			}
		}
	});
	return false;
}


/**
 * 更换验证码图片
 */
function changeImage() {
	var img = document.getElementById("validateImage");
	img.style.backgroundImage = "url('${pageContext.request.contextPath}/admin/imageValidate?"
			+ new Date().getTime() + "')";
}

function setPassword() {
	var username = $('#usercode').val();
	var usernameNum = username.length;
	if (usernameNum <= 0) {
		tipFail("用户名不能为空");
		return false;
	}
	var oldPassword = $('#passwordp').val();
	var oldNum = oldPassword.length;
	if (oldNum <= 0) {
		tipFail("密码不能为空");
		return false;
	}
	else {
		var oldnumhex = oldNum.toString(16);
		newPassword = hex_md5(username + oldPassword) + oldnumhex;
		$('#truePassword').val(newPassword);
		return true;
	}
}
</script>
</body>
</html>