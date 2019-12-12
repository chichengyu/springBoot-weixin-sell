<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>订单列表</title>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.0.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <div class="row clearfix">
        <div class="col-md-12 column">
            <table class="table table-bordered">
                <thead>
                <tr>
                    <th>编号</th>
                    <th>买家</th>
                    <th>金额</th>
                    <th>d订单状态</th>
                    <th>支付状态</th>
                    <th>创建时间</th>
                    <th colspan="2">操作</th>
                </tr>
                </thead>
                <tbody>
                <#if orderList??>
                    <#list orderList.content as order>
                        <tr>
                            <td>${order.orderId!}</td>
                            <td>${order.buyerName!}</td>
                            <td>${order.orderAmount!}</td>
                            <td>${order.orderStatusEnum.message!}</td>
                            <td>${order.payStatusEnum.message!}</td>
                            <td>${order.createTime!}</td>
                            <td>详情</td>
                            <td>
                                <#if order.orderStatusEnum.message == "新订单">
                                    <a href="/sell/seller/order/cancel?orderId=${order.orderId}">取消</a>
                                </#if>
                            </td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
            <ul class="pagination pull-right">
                <#if (orderList.number+1) lte 1>
                    <li class="disabled"><a href="#">上一页</a></li>
                <#else>
                    <li><a href="/sell/seller/order/list?page=${orderList.number}">上一页</a></li>
                </#if>
                <#-- freemarker语法  0 到 3 并小于3 -->
                <#list 0..<orderList.totalPages as index>
                    <#if orderList.number == index>
                        <li class="disabled"><a href="/sell/seller/order/list?page=${index+1}">${index + 1}</a></li>
                    <#else>
                        <li><a href="/sell/seller/order/list?page=${index+1}">${index + 1}</a></li>
                    </#if>
                </#list>
                <#if (orderList.number+1) gte orderList.totalPages>
                    <li class="disabled"><a href="#">下一页</a></li>
                <#else>
                    <li><a href="/sell/seller/order/list?page=${orderList.number+1+1}">下一页</a></li>
                </#if>
            </ul>
        </div>
    </div>
</div>
<script>
    /************ websocket前端 ************/
    var websocket = null;
    if('WebSocket' in window) {
        websocket = new WebSocket('ws://localhost:8080/sell/webSocket');
    }else {
        alert('该浏览器不支持websocket!');
    }

    websocket.onopen = function (event) {
        console.log('建立连接');
    }

    websocket.onclose = function (event) {
        console.log('连接关闭');
    }

    websocket.onmessage = function (event) {
        console.log('收到消息:' + event.data)
        //弹窗提醒, 播放音乐
        $('#myModal').modal('show');

        document.getElementById('notice').play();
    }

    websocket.onerror = function () {
        alert('websocket通信发生错误！');
    }
    // 窗口关闭事件，关闭窗口的时候关闭 websocket事件
    window.onbeforeunload = function () {
        websocket.close();
    }
</script>
</body>
</html>