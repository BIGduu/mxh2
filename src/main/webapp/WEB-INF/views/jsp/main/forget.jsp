<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!--[if IE 9]>         <html class="ie9 no-focus"> <![endif]-->
<!--[if gt IE 9]><!--> <html class="no-focus"> <!--<![endif]-->
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/jsp/main/head.jsp"></jsp:include>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/jsp/main/commonScript.jsp"></jsp:include>
    <body>
        <!-- Reminder Content -->
        <div class="content overflow-hidden">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3 col-lg-4 col-lg-offset-4">
                    <!-- Reminder Block -->
                    <div class="block block-themed animated fadeIn">
                        <div class="block-header bg-primary">
                            <ul class="block-options">
                                <li>
                                    <a href="${pageContext.request.contextPath}/admin/login" data-toggle="tooltip" data-placement="left"><i class="si si-login"></i> 返回登录</a>
                                </li>
                            </ul>
                            <h3 class="block-title">密码重置</h3>
                        </div>
                        
                        <div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
                        <div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
                        
                        <div class="block-content block-content-full block-content-narrow">
                            <!-- Reminder Title -->
                            <h1 class="h2 font-w600 push-30-t push-5">明兄辉建材</h1>
                            <p>请提供你的登录账户，我们会发送一封邮件给你邮箱</p>
                            <!-- END Reminder Title -->

                            <!-- Reminder Form -->
                            <!-- jQuery Validation (.js-validation-reminder class is initialized in js/pages/base_pages_reminder.js) -->
                            <!-- For more examples you can check out https://github.com/jzaefferer/jquery-validation -->
                            <form class="js-validation-reminder form-horizontal push-30-t push-50" action="base_pages_reminder.html" method="post">
                                <div class="form-group">
                                    <div class="col-xs-12">
                                        <div class="form-material form-material-primary floating">
                                            <input class="form-control" id="reminder-email" name="reminder-email">
                                            <label for="reminder-email">账号</label>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-xs-12 col-sm-6 col-md-5">
                                        <button class="btn btn-block btn-primary" type="button" onclick="forgetSend()"><i class="si si-envelope-open pull-right"></i> 发送邮件</button>
                                    </div>
                                </div>
                            </form>
                            <!-- END Reminder Form -->
                        </div>
                    </div>
                    <!-- END Reminder Block -->
                </div>
            </div>
        </div>
        <!-- END Reminder Content -->

        <!-- Reminder Footer -->
        <div class="push-10-t text-center animated fadeInUp">
            <small class="text-muted font-w600"><span class="js-year-copy"></span> &copy; 明兄辉建材</small>
        </div>
        <!-- END Reminder Footer -->
        
        <script type="text/javascript">
        function forgetSend(){
        	tipFail("此功能暂时未开放");
        }
        </script>
    </body>
</html>