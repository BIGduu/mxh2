<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
<div class="col-sm-12 col-lg-12 alert alert-success alert-dismissable" style="display: none;" id="successTips"></div>
<div class="col-sm-12 col-lg-12 alert alert-danger alert-dismissable" style="display: none;" id="failTips"></div>
<!-- Page Header -->
<div class="content bg-gray-lighter">
    <div class="row items-push">
        <div class="col-sm-7">
            <h1 class="page-heading">
                <small><a onclick="goMain()">首页</a>已完成订单列表</small>
            </h1>
        </div>
    </div>
</div>
<!-- END Page Header -->

<!-- Page Content -->
<div class="content">

    <form class="form-horizontal" autocomplete="off" action="" method="post" onsubmit="return false;" id="searchForm">
        <div class="row">
            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div
                            style="line-height: 25px; display: inline-block; padding-left: 15px;"
                            id="orderCodeTitle">订单编号：
                    </div>
                    <div style="width: 65%; display: inline-block;">
                        <input class="form-control" style="height: 28px; width: 87%;"
                               type="text" id="orderCode" name="orderCode" value=${orderCode }>
                    </div>
                </div>
            </div>

            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;" id="receivingAddressTitle">
                        楼盘：
                    </div>
                    <div style="width: 65%;display: inline-block;">
                        <input class="form-control" style="height:28px;width: 87%;" type="text" id="receivingAddress"
                               name="receivingAddress" value=${receivingAddress }>
                    </div>
                </div>
            </div>

            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;" id="managerIdTitle">项目经理：
                    </div>
                    <div style="width: 65%;display: inline-block;">
                        <input class="form-control" style="height:28px;width: 87%;" type="text" id="managerName"
                               name="managerName" value=${managerName }>
                        <input type="hidden" id="managerId" name="managerId" value="${managerId}"/>
                    </div>
                </div>
            </div>

            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;" id="managerIdTitle">司机：
                    </div>
                    <div style="width: 65%;display: inline-block;">
                        <select class="js-select2" name="deliveryId" style="width:90%;margin-top: -2px;"
                                data-placeholder="">
                            <option value="">请选择：</option>
                            <c:forEach items="${deliveryList }" var="delivery">
                                <c:choose>
                                    <c:when test="${delivery.id == deliveryId }">
                                        <option value="${delivery.id }" selected="selected">${delivery.name }</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="${delivery.id }">${delivery.name }</option>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">开始时间：</div>
                    <div style="width: 65%;display: inline-block;">
                        <input type="text" class="laydate-input" id="beginTime" name="beginTime">
                    </div>
                </div>
            </div>

            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">结束时间：</div>
                    <div style="width: 65%;display: inline-block;">
                        <input type="text" class="laydate-input" id="endTime" name="endTime">
                    </div>
                </div>
            </div>
            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;">门店：</div>
                    <div style="width: 65%;display: inline-block;">
                        <select class="js-select2" name="storesId" style="width:90%;margin-top: -2px;"
                                data-placeholder="">
                            <option value="">请选择：</option>
                            <c:if test="${storesList!=null && storesList.size() > 0}">
                                <c:forEach items="${storesList }" var="stores">
                                    <c:choose>
                                        <c:when test="${stores.id == storesId }">
                                            <option value="${stores.id }" selected="selected">${stores.name }</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${stores.id }">${stores.name }</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>
                </div>
            </div>

            <div class="common-query-unit" id="searchTitleDiv">
                <div class="form-group">
                    <div style="line-height: 25px;display: inline-block;padding-left:15px;" id="stateTitle">订单状态：</div>
                    <div style="width: 65%;display: inline-block;">
                        <select class="js-select2" name="state" style="width:90%;margin-top: -2px;" data-placeholder="">
                            <option value="" selected="selected">请选择：</option>
                            <option value="0">异常</option>
                            <option value="1">未审核</option>
                            <option value="2">已审核</option>
                            <option value="6">已送达</option>
                            <option value="7">已完成</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="common-query-unit">
                <div class="form-group">
                    <div class="col-xs-12">
                        <button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;"
                                data-toggle="tooltip" title="查询" onclick="goList('1')">查询
                        </button>
                    </div>
                </div>
            </div>
            <div class="common-query-unit">
                <div class="form-group">
                    <div class="col-xs-12">
                        <button class="btn btn-minw btn-rounded btn-default" type="button" style="margin-top: -2px;"
                                data-toggle="tooltip" title="导出excel" onclick="exportExcel()">导出excel
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <table class="table table-bordered table-striped js-dataTable-simple dataTable no-footer" id="DataTables_Table_1"
           role="grid" aria-describedby="DataTables_Table_1_info">
        <thead>
        <tr role="row">
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">门店
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">下单时间
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">项目经理
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">项目经理电话
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">配送人
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">收货地址
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">订单备注
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">订单状态
            </th>
            <th class="sorting_disabled" tabindex="0" aria-controls="DataTables_Table_1" rowspan="1" colspan="1"
                aria-sort="ascending" aria-label=": activate to sort column descending" style="width: 100px;">订单类型
            </th>
            <th class="text-center sorting_disabled" style="width: 150px;" rowspan="1" colspan="1" aria-label="Actions">
                操作
            </th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty orderList}">
            <tr>
                <td colspan="13" align="center">暂无任何订单</td>
            </tr>
        </c:if>
        <c:forEach items="${orderList}" var="order">
            <tr>
                <td>${order.storesName}</td>
                <td>${order.placeOrderTimeStr}</td>
                <td>${order.managerName}</td>
                <td>${order.managerTel}</td>
                <td>${order.deliveryNames}</td>
                <td>${order.receivingAddress}</td>
                <td>${order.remarks}</td>
                <td>
                    <c:choose>
                        <c:when test="${order.state == 1 }"> 未审核</c:when>
                        <c:when test="${order.state == 2 }"> 审核通过</c:when>
                        <c:when test="${order.state == 0 }"> 异常</c:when>
                        <c:when test="${order.state == 5 }"> 已出库待司机送达</c:when>
                        <c:when test="${order.state == 6 }"> 已送达</c:when>
                        <c:when test="${order.state == 7 }">
                            <c:choose>
                                <c:when test="${order.checkState == 0 }">未提交对账</c:when>
                                <c:when test="${order.checkState == 1 }"> 客户材料部审核成功</c:when>
                                <c:when test="${order.checkState == 2 }"> 客户财务部审核成功</c:when>
                                <c:when test="${order.checkState == 3 }"> 对账完成</c:when>
                            </c:choose>
                        </c:when>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${order.orderType == 1 }"> 配送订单</c:when>
                        <c:when test="${order.orderType == 2 }"> 退货订单</c:when>
                        <c:when test="${order.orderType == 3 }"> 转货订单</c:when>
                    </c:choose>
                </td>
                <td class="text-center">
                    <div class="btn-group">

                        <c:choose>
                            <c:when test="${order.state == 7 }">
                                <c:choose>
                                    <c:when test="${order.checkState == 0 }">
                                        <c:forEach items="${checkState }" var="check">
                                            <c:if test="${order.checkState == check }">
                                                <button class="btn btn-xs btn-default" type="button" title="提交对账"
                                                        onclick="checkSubmit(${order.id})">提交对账
                                                </button>
                                            </c:if>
                                        </c:forEach>
                                    </c:when>
                                    <c:when test="${order.checkState == 1 }">
                                        <c:forEach items="${checkState }" var="check">
                                            <c:if test="${order.checkState == check }">
                                                <button class="btn btn-xs btn-default" type="button" title="审核成功"
                                                        onclick="checkExamine(${order.id})">审核成功
                                                </button>
                                            </c:if>
                                        </c:forEach>
                                    </c:when>
                                    <c:when test="${order.checkState == 2 }">
                                        <c:forEach items="${checkState }" var="check">
                                            <c:if test="${order.checkState == check }">
                                                <button class="btn btn-xs btn-default" type="button" title="审核成功"
                                                        onclick="check(${order.id})">审核成功
                                                </button>
                                            </c:if>
                                        </c:forEach>
                                    </c:when>
                                </c:choose>
                            </c:when>
                        </c:choose>
                        <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="订单详情"
                                onclick="orderAllocation('${order.id}')">订单详情
                        </button>
                        <c:if test="${sessionScope.admin.roleId == 2}">
                            <button class="btn btn-xs btn-default" type="button" data-toggle="tooltip" title="备注"
                                    onclick="orderRemark('${order.id}')">备注
                            </button>
                        </c:if>
                    </div>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <jsp:include page="../main/tablePage.jsp"></jsp:include>
</div>
<div style="display:none">
    <form id="exportForm" name="exportForm" action="" method="post" target="exportIframe">
    </form>
    <iframe id="exportIframe" name="exportIframe" style="display:none"></iframe>
</div>
<!-- END Page Content -->

<script type="text/javascript">
    //覆盖goList方法，分页自动调用该方法
    function goList(page) {
        goMenu("/admin/order/orderListFinal", page, $("#searchForm").serialize());
    }

    function exportExcel() {
        var exportFormObj = document.getElementById("exportForm");
        exportFormObj.action = "/mxh/admin/order/exportOrderListFinal?" + $("#searchForm").serialize();
        exportFormObj.submit();
    }

    var data = [];
    <%
    Object ob=request.getAttribute("users");
    %>
    data = <%out.println(JSON.toJSONString(ob));%>
        $(function () {
            $('#managerName').autocompleter({
                highlightMatches: true,
                // object to local or url to remote search
                source: data,
                // show hint
                hint: true,
                // abort source if empty field
                empty: true,
                // max results
                limit: 5,
                callback: function (value, index, item) {
                    if (item && item.id) {
                        $("#managerId").val(item.id);
                    } else {
                        $("#managerId").val("");
                    }
                }
            });
            $('#managerName').blur(function () {
                if (!$(this).val()) {
                    $("#managerId").val("");
                }
            });
        });

    function orderMenuList(orderId, orderName) {
        ajax({
            type: "POST",
            dataType: "html",
            url: "/admin/order/orderListFinal?page=${page}&orderId=" + orderId,
            timeout: 5000,
            data: {
                orderName: orderName
            },
            error: function () {
                tipFail("网络异常，请稍后再试");
            },
            success: function (data) {
                $('#main-container').html(data);
            }
        });
    }

    function confirmExamine(orderId) {
        confirmDialog({
            title: "信息确认",
            content: "你是否通过此订单？",
            confirm: function () {
                examine(orderId);
            }
        });
    }

    function examine(orderId) {
        ajax({
            type: "POST",
            dataType: "html",
            url: "/admin/order/examine?orderId=" + orderId,
            timeout: 5000,
            data: {},
            error: function () {
                tipFail("网络异常，请稍后再试");
            },
            success: function (data) {
                data = JSON.parse(data);
                if (data.status == "200") {
                    if (data.data.audio == 0) {
                        myAudio.pause();
                    }
                    tipSuccess("审核成功，正在刷新页面");
                    goList("${page}");
                } else {
                    tipFail(data.message);
                }
            }
        });
    }

    function orderAllocation(orderId) {
        ajax({
            type: "get",
            dataType: "html",
            url: "/admin/order/queryAllocationPage?page=${page}&orderId=" + orderId,
            timeout: 5000,
            data: {},
            error: function () {
                tipFail("网络异常，请稍后再试");
            },
            success: function (data) {
                $('#main-container').html(data);
            }
        });
    }

    function confirmAbnormal(orderId) {
        confirmDialog({
            title: "信息确认",
            content: "异常是否已处理？",
            confirm: function () {
                orderAbnormal(orderId);
            }
        });
    }

    function orderAbnormal(orderId) {
        ajax({
            type: "POST",
            dataType: "html",
            url: "/admin/order/abnormal?orderId=" + orderId,
            timeout: 5000,
            data: {},
            error: function () {
                tipFail("网络异常，请稍后再试");
            },
            success: function (data) {
                data = JSON.parse(data);
                if (data.status == 200) {
                    tipSuccess("处理成功，正在刷新页面");
                    goList("${page}");
                } else {

                    tipFail(data.message);
                }
            }
        });
    }

    function confirmAbnormal(orderId) {
        confirmDialog({
            title: "信息确认",
            content: "异常是否已处理？",
            confirm: function () {
                orderAbnormal(orderId);
            }
        });
    }

    function checkSubmit(orderId) {
        confirmDialog({
            title: "信息确认",
            content: "是否提交对账？",
            confirm: function () {
                ajax({
                    type: "POST",
                    dataType: "html",
                    url: "/admin/order/checkSubmit?orderId=" + orderId,
                    timeout: 5000,
                    data: {},
                    error: function () {
                        tipFail("网络异常，请稍后再试");
                    },
                    success: function (data) {
                        data = JSON.parse(data);
                        if (data.status == 200) {
                            tipSuccess("处理成功，正在刷新页面");
                            goList("${page}");
                        } else {

                            tipFail(data.message);
                        }
                    }
                });
            }
        });
    }

    function checkExamine(orderId) {
        confirmDialog({
            title: "信息确认",
            content: "是否审核成功？",
            confirm: function () {
                ajax({
                    type: "POST",
                    dataType: "html",
                    url: "/admin/order/checkExamine?orderId=" + orderId,
                    timeout: 5000,
                    data: {},
                    error: function () {
                        tipFail("网络异常，请稍后再试");
                    },
                    success: function (data) {
                        data = JSON.parse(data);
                        if (data.status == 200) {
                            tipSuccess("处理成功，正在刷新页面");
                            goList("${page}");
                        } else {

                            tipFail(data.message);
                        }
                    }
                });
            }
        });
    }

    function check(orderId) {
        confirmDialog({
            title: "信息确认",
            content: "是否审核成功？",
            confirm: function () {
                ajax({
                    type: "POST",
                    dataType: "html",
                    url: "/admin/order/check?orderId=" + orderId,
                    timeout: 5000,
                    data: {},
                    error: function () {
                        tipFail("网络异常，请稍后再试");
                    },
                    success: function (data) {
                        data = JSON.parse(data);
                        if (data.status == 200) {
                            tipSuccess("处理成功，正在刷新页面");
                            goList("${page}");
                        } else {

                            tipFail(data.message);
                        }
                    }
                });
            }
        });
    }

    function orderDetail(orderId) {
        ajax({
            type: "POST",
            dataType: "html",
            url: "/admin/order/orderDetail?orderId=" + orderId,
            timeout: 5000,
            data: {},
            error: function () {
                tipFail("网络异常，请稍后再试");
            },
            success: function (data) {
                $('#main-container').html(data);
            }
        });
    }

    function orderRemark(orderId) {
        layer.prompt({title: '请填写备注', formType: 2}, function (pass, index) {
            layer.close(index);
            index = layer.load(1, {
                shade: [0.5, '#000'] //0.1透明度的白色背景
            });
            ajax({
                type: "POST",
                dataType: "html",
                url: "/admin/order/orderRemark?orderId=" + orderId,
                timeout: 5000,
                data: {
                    remarks: remarks
                },
                error: function () {
                    layer.close(index);
                    tipFail("网络异常，请稍后再试");
                },
                success: function (data) {
                    layer.close(index);
                    if (data.status == 200) {
                        tipSuccess("处理成功，正在刷新页面");
                        goList("${page}");
                    } else {
                        tipFail("网络异常，请稍后再试");
                    }
                }
            });
        });
    }

    laydate.render({
        elem: '#beginTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

    laydate.render({
        elem: '#endTime',
        type: 'datetime',
        format: 'yyyy-MM-dd HH:mm:ss'
    });

</script>