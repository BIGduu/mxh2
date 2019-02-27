<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="row">
    <div class="col-sm-6 table-page-info"><strong> 第${page }页 / 共${total }页</strong></div>   
	<div class="col-sm-6 table-page-button">
		<div class="dataTables_paginate paging_simple_numbers" id="DataTables_Table_1_paginate">
			<ul class="pagination">
				<c:if test="${page > 1}">
 				<li class="paginate_button previous" aria-controls="DataTables_Table_1" tabindex="0" id="DataTables_Table_1_previous">
 					<a href="#" onclick="goDialogList(${page-1})"><i class="fa fa-angle-left"></i></a>
 				</li>
				</c:if>
				<c:if test="${page <= 1}">
 				<li class="paginate_button previous disabled" aria-controls="DataTables_Table_1" tabindex="0" id="DataTables_Table_1_previous">
 					<a href="#"><i class="fa fa-angle-left"></i></a>
 				</li>
				</c:if>
				<c:if test="${page == total && (page - 4) >= 1}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page-4})">${page-4}</a></li>
				</c:if>
				<c:if test="${(page == total || page == total-1) && (page - 3) >= 1}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page-3})">${page-3}</a></li>
				</c:if>
				<c:if test="${(page - 2) >= 1}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page-2})">${page-2}</a></li>
				</c:if>
				<c:if test="${(page - 1) >= 1}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page-1})">${page-1}</a></li>
				</c:if>
				
				<li class="paginate_button active" aria-controls="DataTables_Table_1" tabindex="0"><a href="#">${page}</a></li>
				
				<c:if test="${(page + 1) <= total}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page+1})">${page+1}</a></li>
				</c:if>
				<c:if test="${(page + 2) <= total}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page+2})">${page+2}</a></li>
				</c:if>
				<c:if test="${(page == 1 || page == 2) && (page + 3) <= total}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page+3})">${page+3}</a></li>
				</c:if>
				<c:if test="${page == 1 && (page + 4) <= total}">
					<li class="paginate_button " aria-controls="DataTables_Table_1" tabindex="0"><a href="#" onclick="goDialogList(${page+4})">${page+4}</a></li>
				</c:if>
				<c:if test="${page >= total}">
					<li class="paginate_button next disabled" aria-controls="DataTables_Table_1" tabindex="0" id="DataTables_Table_1_next"><a href="#"><i class="fa fa-angle-right"></i></a></li>
				</c:if>
				<c:if test="${page < total}">
					<li class="paginate_button next" aria-controls="DataTables_Table_1" tabindex="0" id="DataTables_Table_1_next"><a href="#" onclick="goDialogList(${page+1})"><i class="fa fa-angle-right"></i></a></li>
				</c:if>
			</ul>
		</div>
	</div>
</div>
