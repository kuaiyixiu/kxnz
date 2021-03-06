<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE html>
<html class="ui-page-login">
<base href="<%=basePath%>">
<head>
    <meta charset="utf-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <title>视频播放页</title>
    <link href="lib/mui/css/mui.min.css" rel="stylesheet"/>
    <link href="lib/mui/css/mui-icons-extra.css" rel="stylesheet"/>
    <link href="lib/mui/css/style.css" rel="stylesheet"/>
    <script src="lib/mui/js/mui.min.js"></script>
    <script src="http://res2.wx.qq.com/open/js/jweixin-1.4.0.js"></script>
    <script src="lib/jquery/jquery-3.4.1.js"></script>
    <link rel="stylesheet" href="lib/admin/wechatfonts/style.css">
    <style>
        div {
            font-size: 15px;
        }

        .mui-table-view-cell:after {
            left: 0;
        }

        .mui-table-view li a div {
            font-size: 14px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            margin-left: 5px;
            margin-right: 5px;
        }

        .empty-tips {
            display: block;
            margin: calc(50% - 50px) auto;
            width: 180px;
            height: auto;
        }

        .float-left {
            float: left;
        }

        .float-right {
            float: right;
        }

        .clear-float {
            clear: both;
        }

        .car-video-player {
            height: 200px;
            width: 100%;
            background: black;
            position: relative;
        }

        .car-video-player .car-video-watermark {
            position: absolute;
            right: 10px;
            top: 15px;
            float: right;
            color: white;
            font-size: 13px;
            z-index: 1000;
        }

        .car-video-body {
            height: calc(100% - 200px);
            width: 100%;
            overflow-x: hidden;
            overflow-y: auto;
        }

        .car-video-body > div {
            padding: 5px 15px;
            background-color: white;
        }

        .car-video-body .bottom-line {
            margin-left: -15px;
            margin-right: -15px;
            padding-left: 15px;
            padding-right: 15px;
            border-bottom: 1px solid #e1e1e1;
        }

        .car-video-body .car-video-li-title {
            padding: 5px 15px;
        }

        .car-video-body .mui-icon, .car-video-body .mui-icon-extra {
            font-size: 12px;
            min-width: 45px;
            margin-right: 5px;
            max-width: 60px;
            color: #9e9e9e;
        }

        .car-video-body .mui-icon:before, .car-video-body .mui-icon-extra:before {
            margin-right: 2px;
        }

        .car-video-operation {
            padding-top: 10px;
        }

        .car-video-operation .mui-icon, .car-video-operation .mui-icon-extra {
            font-size: 15px;
        }

        .car-video-describe {
            margin-top: 20px;
        }

        .car-video-describe .car-video-li-describe {
            padding: 8px 15px;
            font-size: 12px;
            color: #9e9e9e;
        }

        .car-video-describe .car-video-author {
            padding-top: 10px;
            padding-bottom: 10px;
            height: 60px;
        }

        .car-video-describe div, .car-video-describe span {
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .car-video-describe .car-video-author .car-video-author-name {
            color: #ffb150;
            font-size: 16px;
            max-width: 150px;
        }

        .car-video-describe .car-video-author .car-video-author-photo {
            height: 40px;
            width: 40px;
            margin-right: 5px;
            border-radius: 50%;
            border: 1px solid #b7b7b7;
        }

        .car-video-describe .car-video-author .car-video-author-role {
            color: #878787;
            font-size: 12px;
        }

        .car-video-describe .author-other-case {
            background-color: #ffb150;
            border-radius: 3px;
            border: 1px solid #cecece;
            color: white;
            padding: 3px 10px;
            height: 30px;
            margin-top: 8px;
            font-size: 13px;
        }

        .car-video-recommend {
            margin-top: 8px;

        }

        .car-video-comment {
            margin-top: 8px;

        }

        .car-video-recommend .mui-table-view-cell .mui-media-body {
            margin-left: -15px;
            margin-right: -15px;
            height: 75px;
        }

        .car-video-recommend .mui-table-view:after {
            background-color: transparent;
        }

        .car-video-recommend-image {
            width: 120px;
            height: 100%;
            float: left;
            border: 1px solid #bdbdbd;
            border-radius: 2px;
        }

        .car-video-recommend-body {
            height: 100%;
            width: calc(100% - 130px);
            margin: 0 5px;
        }

        .car-video-recommend .mui-navigate-right:after, .mui-push-right:after {
            right: 0px;
        }

        .car-video-recommend-body .car-video-recommend-title {
            font-weight: bold;
            font-size: 14px;
            height: 25px;
            line-height: 25px;
        }

        .car-video-recommend-body .car-video-recommend-describe {
            font-weight: normal;
            font-size: 12px;
            color: #bababa;
            height: 25px;
            line-height: 25px;
        }

        .car-video-recommend-body .car-video-recommend-other div {
            font-weight: normal;
            font-size: 11px;
            color: #9e9e9e;
            height: 25px;
            line-height: 35px;
        }

        .member-0 .car-video-recommend-title:before {
            content: "免费";
            color: white;
            background-color: #e1e1e1;
            padding: 3px 6px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: normal;
            margin-right: 4px;
        }

        .member-1 .car-video-recommend-title:before {
            content: "VIP";
            color: white;
            background-color: #ffbf48;
            padding: 3px 6px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: normal;
            margin-right: 4px;
        }

        .member-2 .car-video-recommend-title:before {
            content: "付费";
            color: white;
            background-color: #70df5a;
            padding: 3px 6px;
            border-radius: 3px;
            font-size: 12px;
            font-weight: normal;
            margin-right: 4px;
        }

        #car-video-mask{
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            background-color: black;
            text-align: center;
            padding-top: 50px;
            z-index: 100;
            color: #d8d8d8;
        }
        #car-video-mask .mui-icon-info{
            font-size: 15px;
            color: #d8d8d8;
            font-weight: bold;
        }
        #car-video-mask p:before{
            margin-right: 3px;
            font-size: 16px;
        }
        #car-video-mask .car-mask-tips{
            font-size: 13px;
            color: #e1e1e1;
            margin-top: 15px;
            font-family: '华文楷体', "Helvetica", "Arial", "sans-serif";
        }
        #car-video-mask button{
            padding: 6px 12px;
            border-radius: 15px;
            margin-top: 6px;
            font-weight: bold;
            font-family: '华文楷体', "Helvetica", "Arial", "sans-serif";
        }
        #car-video-mask button:last-child{
            margin-left: 15px;
        }
        .mui-popup-button.mui-popup-button-bold{
            font-weight: normal;
        }
    </style>
</head>
<body>
<header class="mui-bar mui-bar-nav">
    <a class="mui-action-back mui-icon mui-icon-left-nav mui-pull-left"></a>
    <h1 class="mui-title">${video.title}</h1>
</header>
<c:if test="${empty video}">
    <img class="empty-tips" src="image/no_datas.png"/>
</c:if>
<c:if test="${not empty video}">
    <div class="mui-content">
        <div class="car-video-player video" id="J_prismPlayer">
            <c:if test="${not empty autoplay}">
                <video muted width="100%" height="100%" ref="video" controls autoplay="autoplay" preload="auto"
                       webkit-playsinline=“false” playsinline=“false”
                       id="videoPlayer" controlslist="nodownload noremoteplayback noremote"
                       oncontextmenu="return false">
                    <source src="${video.dirUrl}" type="video/MP4">
                </video>
            </c:if>
            <c:if test="${empty autoplay}">
                <div id="car-video-mask">
                    <p class="mui-icon mui-icon-info">您尚未购买当前视频, 无法观看</p>
                    <div>
                        <button class="car-mask-to-play mui-btn mui-btn-warning"
                                data-value="${video.charge}" data-id="${video.id}" data-type="${orderType}">购买当前视频</button>
                        <button class="car-mask-to-vip mui-btn mui-btn-warning">成为会员</button>
                    </div>
                    <p class="car-mask-tips">会员可观看所有付费视频</p>
                </div>
            </c:if>
        </div>
        <div class="car-video-body">
            <div class="car-video-title" style="margin-top: 3px">${video.title}</div>
            <div class="car-video-info">
                <span class="mui-icon mui-icon-navigate">${video.browseNum}</span>
                <span class="mui-icon-extra mui-icon-extra-heart">${video.browseNum}</span>
            </div>
            <div class="car-video-operation">
                <span class="mui-icon mui-icon-chatboxes">2热评</span>
                <span class="float-right mui-icon mui-icon-redo" style="margin-right: 0px;">分享</span>
                <span class="float-right mui-icon-extra mui-icon-extra-heart">收藏</span>
            </div>
            <%--简介--%>
            <div class="car-video-describe">
                <div class="car-video-li-title bottom-line">案例简介</div>
                <div class="car-video-li-describe bottom-line">${video.describe}</div>
                <div class="car-video-author">
                    <img class="float-left car-video-author-photo" src=""
                         onerror="this.onerror='';this.src='image/no_picture.jpg'"/>
                    <div class="float-left">
                        <div class="car-video-author-name">${video.authorName}</div>
                        <div class="car-video-author-role"><span style="margin-right: 10px;max-width: 70px">系统管理员</span><span>粉丝量: 36</span>
                        </div>
                    </div>
                    <a class="float-right author-other-case">更多课程案例</a>
                    <div class="clear-float"></div>
                </div>
            </div>
            <%--推荐--%>
            <div class="car-video-recommend">
                <div class="car-video-li-title bottom-line">相关推荐</div>
                <ul id="other-recommends" class="mui-table-view" style="margin-top: 0">
                    <li class="mui-table-view-cell mui-media">
                        <a href="wechat/carVideo/toVideoPlayer.do?videoId=${item.id}">
                            <div class="mui-media-body mui-navigate-right">
                                <img class="car-video-recommend-image float-left" src="mobile/shop/getImage.do?path=${video.coverUrl}" onerror="this.onerror='';this.src='image/no_picture.jpg'"/>
                                <div class="car-video-recommend-body float-right member-${video.member}">
                                    <div class="car-video-recommend-title">${video.title}</div>
                                    <div class="car-video-recommend-describe">${video.describe}</div>
                                    <div class="car-video-recommend-other">
                                        <div class="car-video-recommend-browse float-left">
                                            <span class="mui-icon mui-icon-navigate">${video.browseNum}</span></div>
                                        <div class="car-video-recommend-collect float-left">
                                            <span class="mui-icon-extra mui-icon-extra-heart">${video.collectNum}</span>
                                        </div>
                                        <div class="car-video-recommend-time float-right"><fmt:formatDate type="date" value="${video.createTime}"/></div>
                                    </div>
                                </div>
                                <div class="clear-float"></div>
                            </div>
                        </a>
                    </li>
                </ul>
            </div>
            <%--评论--%>
            <%--<div class="car-video-comment" id="car-video-comment">
                <div class="car-video-li-title bottom-line">评论</div>
                <ul id="user-comments" class="mui-table-view" style="margin-top: 0">
                    <li class="mui-table-view-cell mui-media">

                    </li>
                </ul>
            </div>--%>
        </div>
    </div>
</c:if>
</body>
<script type="text/javascript">
    var wechatPayParam = {
        "appId": '',     //公众号名称,由商户传入
        "timeStamp": '',         //时间戳,自1970年以来的秒数
        "nonceStr": '', //随机串
        "package": '',
        "signType": '', //微信签名方式：
        "paySign": '' //微信签名
    };
    mui.init();

    mui(document.body).on("tap", '#car-video-mask .car-mask-to-play', function () {
        var money = this.getAttribute("data-value");
        var viewId = this.getAttribute("data-id");
        var orderType = this.getAttribute("data-type");
        mui.confirm('购买视频案例-连途社区<div style="color:orange;font-size: 20px;margin-top: 8px">￥'+money+'</div>', '确认付款', ['取消', '立即支付'], function(e) {
            if (e.index == 1) {
                var param = {"payAmount": money, "viewId": viewId, "orderType": orderType, "payType": "1" };
                mui.post('wechat/community/pay/orders.do',param, function (data) {
                    if (data.retCode == "success"){
                        wechatPayParam = data.retData;
                        if (typeof WeixinJSBridge == "undefined") {
                            if (document.addEventListener) {
                                document.addEventListener('WeixinJSBridgeReady',
                                    onBridgeReady, false);
                            } else if (document.attachEvent) {
                                document.attachEvent('WeixinJSBridgeReady',
                                    onBridgeReady);
                                document.attachEvent('onWeixinJSBridgeReady',
                                    onBridgeReady);
                            }
                        } else {
                            onBridgeReady();
                        }
                    }else{
                        mui.alert("充值失败");
                    }
                });
            }
        })
    }).on('tap', '#car-video-mask .car-mask-to-vip', function () {
        mui.openWindow({
            url: "wechat/community/wechatPay.do",
            id: "wechatPay",
        })
    })


    
    function onBridgeReady(){
        WeixinJSBridge.invoke( 'getBrandWCPayRequest', wechatPayParam,
            function(res){
                if(res.err_msg == "get_brand_wcpay_request:ok" ) {
                    console.log('支付成功');
                    window.location.reload();
                    //支付成功后跳转的页面
                }else if(res.err_msg == "get_brand_wcpay_request:cancel"){
                    console.log('支付取消');
                }else if(res.err_msg == "get_brand_wcpay_request:fail"){
                    console.log('支付失败');
                    WeixinJSBridge.call('closeWindow');
                } //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回ok,但并不保证它绝对可靠。
            });
    }
</script>
</html>

