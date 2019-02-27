<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a> > <a onclick="goList(${page})" style="cursor: pointer;">库存管理  </a> >库存<c:if test="${not empty storage}">编辑</c:if><c:if test="${empty storage}">新增</c:if></small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">
    <div class="col-md-12">
        <form autocomplete="off" class="form-horizontal push-10-t" action="" method="post" onsubmit="return storageSave();" id="storageForm">
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">商品名称</label>
		                   <div>
		                   		<select class="form-control" style="height:35px;" id="merchandiseId" name="merchandiseId"  data-placeholder="" required="required">
		                  			<c:forEach items="${merchandises }" var="merchandise">
		                  				<c:choose>
						   					<c:when test="${storage.merchandiseId == merchandise.id }"> 
						   						<option value="${merchandise.id }" selected="selected">${merchandise.merchandiseName}</option>
							   				</c:when>
						   					<c:otherwise> 
						   						<option value="${merchandise.id }">${merchandise.merchandiseName}</option>
						   					</c:otherwise>
										</c:choose>
		                  			</c:forEach>
		                  		</select>
	                        </div>
	                    </div>
	                </div>
	            </div>

	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">数量</label>
		                   <div>
	                        	<input class="form-control" type="text" id="number" name="number" placeholder="" value="${storage.number}" required="required" max="100000">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>

            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">总价</label>
		                   <div>
	                        	<input class="form-control" type="text" id="totalPrice" name="totalPrice" placeholder="" value="${storage.totalPrice}" required="required" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>

	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">是否支付</label>
		                   <div>
		                  		<select class="form-control" style="height:35px;" id="isPay" name="isPay"  data-placeholder="" required="required">
			                  		<c:choose>
					   					<c:when test="${storage.isPay == 0 }"> 
					   						<option value="0" selected="selected">未支付</option>
				                  			<option value="1" >支付</option> 
						   				</c:when>
						   				<c:when test="${storage.isPay == 1}"> 
					   						<option value="0">未支付</option>
				                  			<option value="1" selected="selected">支付</option> 
						   				</c:when>
					   					<c:otherwise> 
					   						<option value="0">未支付 </option>
				                  			<option value="1">支付</option> 
					   					</c:otherwise>
									</c:choose>
		                  		</select>
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">库存类型</label>
		                   <div>
		                  		<select class="form-control" style="height:35px;" id="type" name="type"  data-placeholder="" required="required">
		                  			<c:forEach items="${typeList }" var="type">
		                  				<c:choose>
						   					<c:when test="${storage.type == type.id }"> 
						   						<option value="${type.id }" selected="selected">${type.name}</option>
							   				</c:when>
						   					<c:otherwise> 
						   						<option value="${type.id }">${type.name} </option>
						   					</c:otherwise>
										</c:choose>
		                  			</c:forEach>
		                  		</select>
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">送货单位</label>
		                   <div>
		                  		<input class="form-control" type="text" id="deliveryCompany" name="deliveryCompany" placeholder="" value="${storage.deliveryCompany}" maxlength="50">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">进货单号</label>
		                   <div>
		                  		<input class="form-control" type="text" id="receiptNumber" name="receiptNumber" placeholder="" value="${storage.receiptNumber}" maxlength="25">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">结算数量</label>
		                   <div>
		                  		<input class="form-control" type="text" id="billingQuantity" name="billingQuantity" placeholder="" value="${storage.billingQuantity}" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">结算单位</label>
		                   <div>
		                  		<input class="form-control" type="text" id="billingUnit" name="billingUnit" placeholder="" value="${storage.billingUnit}" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">进货单价</label>
		                   <div>
		                  		<input class="form-control" type="text" id="purchasePrice" name="purchasePrice" placeholder="" value="${storage.purchasePrice}" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">运费单价</label>
		                   <div>
		                  		<input class="form-control" type="text" id="shippingPrice" name="shippingPrice" placeholder="" value="${storage.shippingPrice}" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">货款付款日期</label>
		                   <div>
		                  		<input class="form-control laydate-input" type="text" id="purchaseDate" name="purchaseDateStr" placeholder="" value="${storage.purchaseDate}">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">货款收款人姓名</label>
		                   <div>
		                  		<input class="form-control" type="text" id="purchaseName" name="purchaseName" placeholder="" value="${storage.purchaseName}" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">运费收款日期</label>
		                   <div>
		                  		<input class="form-control laydate-input" type="text" id="shippingDate" name="shippingDateStr" placeholder="" value="${storage.shippingDate}">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class = "row">
	           <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">运费收款人姓名</label>
		                   <div>
		                  		<input class="form-control" type="text" id="shippingName" name="shippingName" placeholder="" value="${storage.shippingName}" maxlength="10">
	                        </div>
	                    </div>
	                </div>
	            </div>
	            
	            <div class="col-lg-4 col-md-6 col-sm-6 col-xs-12">
		            <div class="form-group">
		                <div class="col-sm-10 col-xs-12">
		                   <label for="material-color-primary">备注</label>
		                   <div>
		                  		<input class="form-control" type="text" id="remarks" name="remarks" placeholder="" value="${storage.remarks}">
	                        </div>
	                    </div>
	                </div>
	            </div>
            </div>
            
            <div class="form-group">
                <div class="col-sm-9">
                    <button class="btn btn-sm btn-primary" type="submit" id="saveButton">保存</button>
                    <button class="btn btn-sm btn-warning" type="button" id="cancelButton" onclick="goList(${page})">取消</button>
                </div>
            </div>
        </form>
	</div>
</div>    
<!-- END Page Content -->

<script type="text/javascript">
function storageSave()
{
	var storageId = "${storageId}";
	hideTips();
	tipSuccess("正在保存，请耐心等待");
	$('#saveButton').attr("disabled",true);
	$('#cancelButton').attr("disabled",true);
	ajax({
		type: "POST",
        dataType: "json",
        url: "/admin/storage/storageSave?id=${storageId}",
        data: $('#storageForm').serialize(),
		timeout:5000,
		error: function(request) 
		{
			tipFail("网络异常，请稍后再试");
			$('#saveButton').attr("disabled",false);
			$('#cancelButton').attr("disabled",false);
		},
        success: function(data)
        {
        	if(data.status=='200')
       		{
        		tipSuccess("保存成功");
        		if(storageId == 0)
       			{
            		setTimeout('goList(1)',1500);
       			}
        		else
       			{
        			setTimeout('goList(${page})',1500);
       			}
       		}
        	else
       		{
        		tipFail(data.message);
        		$('#saveButton').attr("disabled",false);
    			$('#cancelButton').attr("disabled",false);
       		}
        }
	});
	return false;
}


laydate.render({
	elem: '#shippingDate',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});

laydate.render({
	elem: '#purchaseDate',
	type: 'datetime',
	format: 'yyyy-MM-dd HH:mm:ss'
});
</script>