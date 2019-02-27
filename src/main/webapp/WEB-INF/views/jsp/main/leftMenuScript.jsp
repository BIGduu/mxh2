<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
$('[data-action="sidebar_close_mini"]').each(function(index, element){
	$(element).click(function(){
		var url = $(element).data('url');
		if(url && url != ''){
			goMenu(url, 1);
		}
	});
});
</script>
