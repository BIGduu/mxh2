<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript">

function tipSuccess(message)
{
	$('#successTips').html(message);
	$('#successTips').show();
	$('#failTips').hide();
}

function tipFail(message)
{
	$('#failTips').html(message);
	$('#failTips').show();
	$('#successTips').hide();
}

function hideTips()
{
	$('#successTips').hide();
	$('#failTips').hide();
}

function hideTipsLater(time)
{
	setTimeout('hideTips()',time);
}

function goList(page)
{
	
}

function goMain(){
	goMenu("/admin/mainIndex",1);
}

function goMenu(url, page, data)
{
    tipFail("正在处理数据,请耐心等待!(可能持续数十秒)")
	ajax({
		type: 'post',
		dataType: 'html',
		url: url+"?page="+page,
		timeout: 50000,
		data: data || {},
        error: function(){
			hideTips();
			tipFail("网络异常，请稍后再试");
		},
		success: function(data){
			$('#main-container').html(data);
		}
	});
}

function ajax(object)
{
	var ajaxObject = {
		cache: false,
		type: object.type,
        dataType: 'html',
        url: "${pageContext.request.contextPath}"+object.url,
        data: object.data,
		async: true,
		timeout: object.timeout || 10000,
		beforeSend: function(request) {
            request.setRequestHeader("MyAjax", "1");
        },
		error: object.error,
        success: function(data)
        {
        	if(data.substring(0,1)=='{'){
        		var dataObject = JSON.parse(data);
    			if(dataObject.status == "999999"){
    				window.location.href = "${pageContext.request.contextPath}/admin/login";
    			}
    			else{
            		if(object.dataType == 'json'){
            			object.success(dataObject);
                	}
            		else{
            			object.success(data);
            		}
    			}
        	}
        	else{
        		object.success(data);
        	}
        }
	};
	$.ajax(ajaxObject);
}

function ajaxFile(object)
{
	var ajaxObject = {
		processData: false,
		contentType: false,
		cache: false,
		type: object.type,
        dataType: 'html',
        url: "${pageContext.request.contextPath}"+object.url,
        data: object.data,
		async: true,
		timeout: object.timeout || 10000,
		beforeSend: function(request) {
            request.setRequestHeader("MyAjax", "1");
        },
		error: object.error,
        success: function(data)
        {
        	if(data.substring(0,1)=='{'){
        		var dataObject = JSON.parse(data);
    			if(dataObject.status == "999999"){
    				window.location.href = "${pageContext.request.contextPath}/admin/login";
    			}
    			else{
            		if(object.dataType == 'json'){
            			object.success(dataObject);
                	}
            		else{
            			object.success(data);
            		}
    			}
        	}
        	else{
        		object.success(data);
        	}
        }
	};
	$.ajax(ajaxObject);
}
	
function confirmDialog(object){
	$('#modal-common').modal('hide');
	$('#modal-common-title').html(object.title);
	$('#modal-common-content').html(object.content);
	$('#modal-common-confirm').off('click');
	$('#modal-common-confirm').one('click',function(){
		$('#modal-common').modal('hide');
		$('#modal-common-title').html('');
		$('#modal-common-content').html('');
		object.confirm();
	});
	$('#modal-common-confirm').show();
	if(object.confirmShow != 'undefined' && object.confirmShow == false){
		$('#modal-common-confirm').hide();
	}
	$('#modal-common-cancel').off('click');
	if(object.cancel){
		$('#modal-common-cancel').one('click',function(){
			object.cancel();
		});
	}
	$('#modal-common-cancel').show();
	if(object.cancelShow != 'undefined' && object.cancelShow == false){
		$('#modal-common-cancel').hide();
	}
	$('#modal-common-close').off('click');
	$('#modal-common').off('click');
	if(object.close){
		$('#modal-common-close').one('click',function(){
			object.close();
		});
		$('#modal-common').one('click', function(){
			object.close();
		});
	}
	$('#modal-common').modal('show');
}
	
function loadPageDialog(object){
	$('#modal-large').modal('hide');
	$('#modal-large-title').html(object.title);
	$('#modal-large-content').html('');
	tipSuccessDialog("正在加载...");
	ajax({
		type: 'post',
		dataType: 'html',
		url: object.url,
		timeout: 10000,
		data: object.data || {},
		error: function(){
			tipFailDialog("网络异常，请稍后再试");
		},
		success: function(data){
			hideTipsDialog();
			$('#modal-large-content').html(data);
		}
	});
	$('#modal-large-confirm').off('click');
	$('#modal-large-confirm').one('click',function(){
		$('#modal-large').modal('hide');
		$('#modal-large-title').html('');
		onDialogConfirm(object.confirm);
	});
	$('#modal-large-confirm').show();
	if(object.confirmShow != 'undefined' && object.confirmShow == false){
		$('#modal-large-confirm').hide();
	}
	$('#modal-large-cancel').off('click');
	if(object.cancel){
		$('#modal-large-cancel').one('click',function(){
			object.cancel();
		});
	}
	$('#modal-large-cancel').show();
	if(object.cancelShow != 'undefined' && object.cancelShow == false){
		$('#modal-large-cancel').hide();
	}
	$('#modal-large-close').off('click');
	$('#modal-large').off('click');
	if(object.close){
		$('#modal-large-close').one('click',function(){
			object.close();
		});
		$('#modal-large').one('click', function(){
			object.close();
		});
	}
	$('#modal-large').modal('show');
}

function tipSuccessDialog(message)
{
	$('#successDialogTips').html(message);
	$('#successDialogTips').show();
	$('#failDialogTips').hide();
}

function tipFailDialog(message)
{
	$('#failDialogTips').html(message);
	$('#failDialogTips').show();
	$('#successDialogTips').hide();
}

function hideTipsDialog()
{
	$('#successDialogTips').hide();
	$('#failDialogTips').hide();
}

function goMenuDialog(url, page, data)
{
	ajax({
		type: 'post',
		dataType: 'html',
		url: url+"?page="+page,
		timeout: 5000,
		data: data || {},		
		error: function(){
			hideTipsDialog();
			tipFailDialog("网络异常，请稍后再试");
		},
		success: function(data){
			$('#modal-large-content').html(data);
		}
	});
}
var myAudio = document.getElementById("myAudio");
function audio() {
	if (typeof(EventSource)!=="undefined") {
	    var source=new EventSource("${pageContext.request.contextPath}/admin/audio");
	    source.onmessage = function(event) {
			if(event.data == 1)	 {
				myAudio.currentTime = 0;
				myAudio.play();
			} else if (event.data == 0) {
				myAudio.pause();
			} else {
				source.close();
			}
	    }; 
	    
	    source.addEventListener('error', function(e) {  
	    	source.close();
        }, false);
	} else{  
		tipFail("此浏览器不支持server-send，请使用新版本的谷歌浏览器");  
	}
}

function setPassword(password) {
	var username = "${admin.adminName}";
	var usernameNum = username.length;
	
	var oldNum = password.length;
	var oldnumhex = oldNum.toString(16);
	newPassword = hex_md5(username + password) + oldnumhex;
	return newPassword;
}

function passwordUpdate() {
	layer.prompt({title: '请输入原密码', formType: 1}, function(pass, index){
		layer.close(index);
		index = layer.load(1, { shade: [0.5,'#000'] });
		pass = setPassword(pass);
		ajax({
			type: "POST",
	        dataType: "json",
	        url: "/admin/admin/passwordVerify",
			timeout:5000,
			data: {
				password: pass
			},
			error: function()  {
				layer.close(index);
				layer.alert('网络异常，请稍后再试');
			},
	        success: function(data)  {
	        	layer.close(index);
	        	if(data.status == 200) {
	        		layer.prompt({title: '请输入新的密码', formType: 1}, function(text, index) {
	        			text = setPassword(text);
	        			layer.close(index);
	        			index = layer.load(1, { shade: [0.5,'#000'] });
	        			ajax({
	        				type: "POST",
	        		        dataType: "html",
	        		        url: "/admin/admin/password",
	        				timeout:5000,
	        				data: {
	        					password: pass,
	        					passwordNew: text
	        				},
	        				error: function()  {
	        					layer.close(index);
	        					layer.alert('网络异常，请稍后再试');
	        				},
	        		        success: function(data)
	        		        {
	        		        	layer.close(index);
	        		        	if(data.status == 200) {
	        		        		layer.alert('修改成功');
	        		        	} else {
	        		        		layer.alert('网络异常，请稍后再试');
	        		        	}
	        		        }
	        			});
	        		});
	        	} else {
	        		layer.alert(data.message);
	        	}
	        }
		});
	});
	
}
$(function() {
	var clearZeros = $(".clearZero");
	for (var i = 0; i < clearZeros.length; i++) {
		$(clearZeros[i]).html(parseFloat($(clearZeros[i]).html()));
	}
	
})

</script>