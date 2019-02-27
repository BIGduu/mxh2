<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Header -->
<header id="header-navbar" class="content-mini content-mini-full">
    <!-- Header Navigation Right -->
    <ul class="nav-header pull-right">
        <li>
            <div class="btn-group">
                <button class="btn btn-default btn-image dropdown-toggle" data-toggle="dropdown" type="button" style="padding-left: 16px;">
                   	${admin.adminName}
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu dropdown-menu-right">
                    <!--<li class="dropdown-header">信息</li>
                    <li>
                        <a tabindex="-1" href="#">
                            <i class="si si-user pull-right"></i>帐号信息
                        </a>
                    </li>
                    <li class="divider"></li>-->
                    <li class="dropdown-header">操作</li>
                    <!-- 
                    <li>
                        <a tabindex="-1" href="base_pages_lock.html">
                            <i class="si si-lock pull-right"></i>锁屏帐号
                        </a>
                    </li>-->

                    <li>
                        <a tabindex="-1" href="javascript:;" onclick="passwordUpdate()">
                            <i class="si si-lock pull-right"></i>修改密码
                        </a>
                    </li>
                    <li>
                        <a tabindex="-1" href="${pageContext.request.contextPath}/admin/logout?time=${time}">
                            <i class="si si-logout pull-right"></i>安全退出
                        </a>
                    </li>
                </ul>
            </div>
        </li>
    </ul>
    <!-- END Header Navigation Right -->

    <!-- Header Navigation Left -->
    <ul class="nav-header pull-left" style="margin-left: -28px;padding: 10px 0px;">
        <li class="hidden-md hidden-lg">
            <!-- Layout API, functionality initialized in App() -> uiLayoutApi() -->
            <button class="btn btn-default" data-toggle="layout" data-action="sidebar_toggle" type="button">
                <i class="fa fa-navicon"></i>
            </button>
        </li>
        <%--<li class="hidden-xs hidden-sm">--%>
            <%--<!-- Layout API, functionality initialized in App() -> uiLayoutApi() -->--%>
            <%--<img id="scroll-img" src="${pageContext.request.contextPath}/assets/img/favicons/zoom_left.png" style="width:22px;height:16px;" --%>
            <%--onclick="changeScrollImg()" data-toggle="layout" data-action="sidebar_mini_toggle"/>--%>
        <%--</li>--%>
    </ul>
    <!-- END Header Navigation Left -->
</header>
<!-- END Header -->