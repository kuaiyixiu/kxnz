<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
		<title>登录</title>
		<link href="lib/mui/css/mui.min.css" rel="stylesheet" />
		<link href="lib/mui/css/style.css" rel="stylesheet" />
		<script src="lib/mui/js/mui.min.js"></script>
		<script src="lib/mui/js/mui.enterfocus.js"></script>
		<script src="lib/mui/js/app.js"></script>
		<script src="lib/jquery/jquery-3.4.1.js"></script>
		<script src="lib/jquery/jquery.autocompleter.js"></script>
		<script src="lib/jquery/jquery-ui.js"></script>
		
	</head>
	<style>
		.mui-input-group {
			margin-top: 10px;
		}

		.mui-input-group:first-child {
			margin-top: 20px;
		}

		/*.mui-input-group label {
			width: 25%;
		}

		.mui-input-row label~input,
		.mui-input-row label~select,
		.mui-input-row label~textarea {
			width: 75%;
		}*/

		.mui-checkbox input[type=checkbox],
		.mui-radio input[type=radio] {
			top: 6px;
		}

		.mui-content-padded {
			margin-top: 25px;
		}

		.mui-btn {
			padding: 10px;
		}

		.link-area {
			display: block;
			margin-top: 25px;
			text-align: center;
		}

		.spliter {
			color: #bbb;
			padding: 0px 8px;
		}
	</style>

	<body>
		<header class="mui-bar mui-bar-nav">
			<h1 class="mui-title">登录</h1>
		</header>
		<div class="mui-content" style="padding-top: 30px">
			<form id='login-form' class="mui-input-group" style="margin-top: 50px	">
				<div class="mui-input-row">
					<label>账号</label>
					<input id='account' type="text" class="mui-input-clear mui-input" placeholder="请输入手机号"
						   value="18256504106">
				</div>
				<div class="mui-input-row">
					<label>密码</label>
					<input id='password' type="password" class="mui-input-password" placeholder="请输入密码"
						   value="111111">
				</div>
			</form>
			<div class="mui-content-padded" style="margin-top: 50px">
				<button id='login' type="button" class="mui-btn mui-btn-block mui-btn-primary"
						data-loading-icon="mui-spinner mui-spinner-custom"
						data-loading-text="登录中...">登录</button>
				<div class="link-area"><a id='reg'>注册账号</a> <span class="spliter">|</span> <a id='forgetPassword'>忘记密码</a>
				</div>
			</div>
		</div>
		<script>

			mui('body').on('tap','#reg',function(){
				mui.openWindow({
					url: 'wechat/community/regIndex.do'
				});
			}).on("tap", "#forgetPassword", function(){
				mui.openWindow({
					url: 'wechat/community/pwdIndex.do'
				});
			}).on("tap", "#login", function () {
				var me = this;
				mui(me).button('loading');
				mui.ajax("wechat/community/login.do", {
					data: {account:$("#account").val(), password: $("#password").val()},
					dataType: 'json',
					type: 'post',
					timeout: 10000,
					success: function (res) {
						if(res!= null){
							mui.toast(res.retMsg,{ duration:'long', type:'div'});
							if(res.retCode == "success"){
								mui.openWindow({ url: 'wechat/community.do', show:{ autoShow:false }});
								return;
							}
						}else{
							mui.alert("响应失败, 请稍后重试!");
						}
						mui(me).button('reset');
					},
					error: function (xhr, type, errorThrown) {
						mui(me).button('reset');
					}
				});
			});

		</script>
	</body>
</html>