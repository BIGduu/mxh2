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
        <title>错误</title>
    </head>
    <body>
    	<c:choose>
    		<c:when test="${not empty message }">${message }</c:when>
    		<c:otherwise>用户正在审核,请耐心等待</c:otherwise>
    	</c:choose>
    </body>
</html>
