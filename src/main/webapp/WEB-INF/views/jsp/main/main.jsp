<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<!--[if IE 9]>         <html class="ie9 no-focus"> <![endif]-->
<!--[if gt IE 9]><!--> <html class="no-focus"> <!--<![endif]-->
    <jsp:include page="head.jsp"></jsp:include>
    <body>
        <!-- Page Container -->
        <!--
            Available Classes:

            'sidebar-l'                  Left Sidebar and right Side Overlay
            'sidebar-r'                  Right Sidebar and left Side Overlay
            'sidebar-mini'               Mini hoverable Sidebar (> 991px)
            'sidebar-o'                  Visible Sidebar by default (> 991px)
            'sidebar-o-xs'               Visible Sidebar by default (< 992px)

            'side-overlay-hover'         Hoverable Side Overlay (> 991px)
            'side-overlay-o'             Visible Side Overlay by default (> 991px)

            'side-scroll'                Enables custom scrolling on Sidebar and Side Overlay instead of native scrolling (> 991px)

            'header-navbar-fixed'        Enables fixed header
        -->
        <audio id="myAudio" src="http://mxh.mingxionghuijc.com/file/ring.wav"></audio>
        <div id="page-container" class="sidebar-l sidebar-o side-scroll header-navbar-fixed">
            <!-- Side Overlay  aside.jsp-->
            <!-- END Side Overlay -->

            <jsp:include page="leftMenu.jsp"></jsp:include>
            <jsp:include page="header.jsp"></jsp:include>
            
            <jsp:include page="commonScript.jsp"></jsp:include>

            <!-- Main Container -->
            <div id="main-container" style="min-width: 1235px;">
            	<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
            	<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
                <!-- Page Header -->
                <div class="content bg-gray-lighter">
                    <div class="row items-push">
                        <div class="col-sm-7">
                            <h1 class="page-heading">
                                <small>欢迎来到明兄辉建材管理系统</small>
                            </h1>
                        </div>
                    </div>
                </div>
                <!-- END Page Header -->
               
                <!-- Page Content -->
               <div class="content">
               
               </div>    
                <!-- END Page Content -->
            </div>
            <!-- END Main Container -->
            <jsp:include page="modal.jsp"></jsp:include>
            
            <jsp:include page="foot.jsp"></jsp:include>
		</div>
		<!-- END Page Container -->
        <jsp:include page="leftMenuScript.jsp"></jsp:include>
        <script type="text/javascript">audio()</script>
    </body>
</html>
