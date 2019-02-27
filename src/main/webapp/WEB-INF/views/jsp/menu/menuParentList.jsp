<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<table class="table table-bordered table-striped js-table-checkable dataTable no-footer table-hover table-condensed" id="DataTables_Table_1" role="grid" aria-describedby="DataTables_Table_1_info">
    这是菜单父list
    <thead>
        <tr role="row">
           	<th class="text-center sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 50px;">选择</th>
           	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单名称</th>
        	<th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单路径</th>
        	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单级别</th>
        	<th class="hidden-xs sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1" aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">菜单样式</th>
        </tr>
    </thead>
    <tbody>
    	<c:if test="${empty menuList}"><tr><td colspan="10" align="center">暂无任何菜单</td></tr></c:if>
    	<c:forEach items="${menuList}" var="menu">
         <tr>
         	<td class="text-center">
				<input type="hidden" id="menuName_${menu.id}" value="${menu.menuName}">
         	 	<label class="css-input css-radio css-radio-primary" style="margin-top:0px;margin-bottom:0px;">
					<input type="radio" name="selectItem" value="${menu.id}"><span></span>
				</label>
			</td>
             <td>${menu.menuName}</td>
             <td>${menu.linkUrl}</td>
             <td class="hidden-xs">${menu.menuLevel}</td>
             <td class="hidden-xs"><i class="${menu.icon}"></i></td>
         </tr>
        </c:forEach>
    </tbody>
</table>
<jsp:include page="../main/tableDialogPage.jsp"></jsp:include>
<!-- END Page Content -->

<script type="text/javascript">
//覆盖goDialogList方法，弹出出来的分页自动调用该方法
function goDialogList(page)
{
	goMenuDialog("/admin/menu/menuParentList", page, {
		parentLevel : "${parentLevel}"
	});
}
//确认方法自动调用，在这方法里传给调用者用的东西给它，dialogComfirm(object)
function onDialogConfirm(callback){
	var selectId = $("input[name='selectItem']:checked").val();
	var name = $('#menuName_'+selectId).val();
	callback({
		selectId : selectId,
		name : name
	});
}
</script>
<script type="text/javascript">
$(function(){App.initHelpers('table-tools');});
</script>