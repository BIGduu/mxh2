<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 

<style type="text/css">
.zoom_div{ padding-left: 42%;background-color: #f8f8f8;height: 34px;line-height: 34px; }
</style>

<!-- Sidebar -->
<nav id="sidebar" style="border-right: 1px solid #e6e6e6;">
    <!-- Sidebar Scroll Container -->
    <div id="sidebar-scroll">
        <!-- Sidebar Content -->
        <!-- Adding .sidebar-mini-hide to an element will hide it when the sidebar is in mini mode -->
        <div class="sidebar-content">
            <!-- Side Header -->
            <div class="side-header side-content bg-white-op" style="padding-left: 8px;">
                <!-- Layout API, functionality initialized in App() -> uiLayoutApi() -->
                <button class="btn btn-link text-gray pull-right hidden-md hidden-lg" type="button" data-toggle="layout" data-action="sidebar_close">
                    <i class="fa fa-times"></i>
                </button>
                <a class="h5" href="#" onmouseover="this.style.color='#646464'" onclick="goMenu('/admin/mainIndex',1)">
                    <i class="fa text-primary"><img src="${pageContext.request.contextPath}/assets/img/favicons/favicon.png" style="width: 42px;height: 42px;"></i> 
                    <span class="h4 sidebar-mini-hide" style="vertical-align: middle">明兄辉建材</span>
                </a>
            </div>
            <div id="zoom-div" class="zoom_div hidden-md-zoom">
                <img id="scroll-img" src="${pageContext.request.contextPath}/assets/img/favicons/zoom_left.png" style="width:16px;height:14px;"
                     onclick="changeScrollImg()" data-toggle="layout" data-action="sidebar_mini_toggle"/>
            </div>
            <!-- END Side Header -->

            <!-- Side Content -->
            <div class="side-content">
                <ul class="nav-main">
                	<c:forEach items="${admin.menuGroups}" var="menuGroup">
	                	<li>
	                		<a class="nav-submenu" data-toggle="nav-submenu" href="#"><i class="${menuGroup.menuCss}"></i><span class="sidebar-mini-hide">${menuGroup.name}</span></a> 
	                		<c:forEach items="${menuGroup.menus}" var="menu">
	                			<ul>
		                        	<li>
		                                <a href="#" data-toggle="left" data-action="sidebar_close_mini" data-url="${menu.linkUrl }">${menu.menuName}</a>
		                            </li>
	                        	</ul>
	                		</c:forEach>
	                	</li>
                	</c:forEach>
                </ul>
            </div>
            <!-- END Side Content -->
        </div>
        <!-- Sidebar Content -->
    </div>
    <!-- END Sidebar Scroll Container -->
</nav>
<!-- END Sidebar -->
<script type="text/javascript">
    /**
     * 切换展开和收缩菜单的图标样式
     */
    function changeScrollImg() {
        var imgSrc = $("#scroll-img").attr("src");
        imgSrc = imgSrc.substring(imgSrc.lastIndexOf("/") + 1, imgSrc.lastIndexOf("."));
        if (imgSrc == "zoom_left") {
            $("#scroll-img").attr("src", "${pageContext.request.contextPath}/assets/img/favicons/zoom_right.png");
        } else {
            $("#scroll-img").attr("src", "${pageContext.request.contextPath}/assets/img/favicons/zoom_left.png");
        }
    }
</script>