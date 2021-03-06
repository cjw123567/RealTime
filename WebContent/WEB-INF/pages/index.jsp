<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" import="java.util.*,java.sql.*"
	pageEncoding="utf-8"%>
<%@ page contentType="text/html;charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head id="Head1">
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<title>加班自動拋轉系統</title> <!-- Icons -->
	<c:url value="/resources/assets/css/icons.css" var="assetsIconsCSS"/>
	<c:url value="/resources/assets/css/bootstrap.css" var="bootstrapCSS"/>
    <c:url value="/resources/assets/css/plugins.css" var="pluginsCSS"/>
    <c:url value="/resources/assets/css/main.css?version=${resourceVersion}" var="mainCSS"/>
    <c:url value="/resources/css/default.css" var="defaultCSS"/>
    <c:url value="/resources/css/themes/default/easyui.css" var="easyuiCSS"/> 
    <c:url value="/resources/css/themes/icon.css" var="themesIconCSS"/>        
    
	<link href="${assetsIconsCSS}" rel="stylesheet" />
	<!-- Bootstrap stylesheets (included template modifications) -->
	<link href="${bootstrapCSS}" rel="stylesheet"/>
	<!-- Plugins stylesheets (all plugin custom css) -->
	<link href="${pluginsCSS}" rel="stylesheet" />
	<!-- Main stylesheets (template main css file) -->
	<link href="${mainCSS}" rel="stylesheet" />
	<link href="${defaultCSS}" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="${easyuiCSS}" />
	<link rel="stylesheet" type="text/css" href="${themesIconCSS}" />
	
	<style>
		#loginOut {
			color: #ffffff;
		}
	</style>
	
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
	<noscript>
		<div
			style="position: absolute; z-index: 100000; height: 2046px; top: 0px; left: 0px; width: 100%; background: white; text-align: center;">
			<img src="images/noscript.gif" alt='抱歉，请开启脚本支持！' />
		</div>
	</noscript>
	<div region="north" split="true" border="false"
		style="overflow: hidden; height: 30px; background: #75b9e6;">

		<i class="im-windows8 text-logo-element animated bounceIn"></i><span
			class="text-logo">FOX</span><span class="text-slogan">LINK</span> <span
			style="float: right; padding-right: 20px;" class="head">欢迎${username}
			<a href="#" id="loginOut">安全退出</a>
		</span>
	</div>
	<div region="south" split="true"
		style="height: 30px; background: #D2E0F2;">
		<div class="footer">By 林東平 Tel:#36908,劉峰君 Tel:#31007,陳健文 Tel:#31013</div>
	</div>
	<div region="west" split="true" title="导航菜单" style="width: 180px;"
		id="west">
		<div class="easyui-accordion" fit="true" border="false">
			<!--  导航内容 -->

		</div>

	</div>
	<div id="mainPanle" region="center"
		style="background: #eee; overflow-y: hidden">
		<div id="tabs" class="easyui-tabs" fit="true" border="false">
			<div title="欢迎使用" style="padding: 20px; overflow: hidden;" id="home">
				<h1>歡迎來到加班自動拋轉系統</h1>
				<h1 style="color:red;">根據人資規定，所有刷卡記錄只允許查詢近三個月的記錄</h1>
				<h1 style="color:red;">加班拋轉系統請使用<img src="./resources/images/chromeicon.jpg" alt='谷歌瀏覽器圖標！'/>谷歌瀏覽器拋轉，請不要使用<img src="./resources/images/ieicon.png" alt='ie瀏覽器圖標！'/>IE，<img src="./resources/images/firefoxicon.jpg" alt='火狐瀏覽器圖標！'/>火狐等瀏覽器。如沒有谷歌瀏覽器請自行點擊下方藍色字體下載相對應的谷歌瀏覽器。否則出現報加班的問題請自行用聯絡單修改，或者聯繫林東平36908，陳健文31013，劉峰君31007進行谷歌瀏覽器的安裝。</h1>
				<div>
				<a style="font-size: 50px;color: blue;" href="./Utils/DownLoadChrome.do?filename=32" download="64.0.3282.140_chrome32_stable_windows_installer.exe"  target="_blank">點擊下載32位谷歌瀏覽器</a>
				</div>
				<div>
				<a style="font-size: 50px;color: blue;" href="./Utils/DownLoadChrome.do?filename=64" download="chromebeta_x64-v68.0.3440.33.exe"  target="_blank">點擊下載64位谷歌瀏覽器</a>
				</div>
				<div>
				<a style="font-size: 50px;color: blue;" href="./resources/ppt/實時工時系統USERGUIDE.pptx" download="實時工時系統USERGUIDE.pptx"  target="_blank">實時工時SOP下載</a>
				</div>
				<div>
				<a style="font-size: 50px;color: blue;" href="./resources/ppt/實時工時車間門禁管理.pptx" download="實時工時車間門禁管理.pptx"  target="_blank">車間門禁管理SOP下載</a>
				</div>
			    
			 <!-- ./resources/ppt/實時工時系統USERGUIDE.pptx -->
			</div>
		</div>
	</div>

	<div id="mm" class="easyui-menu" style="width: 150px;">
		<div id="mm-tabclose">关闭</div>
		<div id="mm-tabcloseall">全部关闭</div>
		<div id="mm-tabcloseother">除此之外全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-tabcloseright">当前页右侧全部关闭</div>
		<div id="mm-tabcloseleft">当前页左侧全部关闭</div>
		<div class="menu-sep"></div>
		<div id="mm-exit">退出</div>
	</div>	    	   
</body>
        <c:url value="/resources/js/jquery/jquery-1.4.2.min.js" var="jqueryJS"/>
        <c:url value="/resources/js/jquery.easyui.pack.js" var="easyuiJS"/>
        <c:url value="/resources/js/Project/sideBarMenu.js?version=${resourceVersion}" var="sideBarMenuJS"/> 
         <c:url value="/resources/js/Project/AjaxCheckSession.js?version=${resourceVersion}" var="AjaxCheckSessionJS"/>       
        <c:url value="/resources/js/outlook2.js?version=${resourceVersion}" var="outlook2JS"/>

	    <script type="text/javascript" src="${jqueryJS}"></script>
	    <script type="text/javascript" src="${easyuiJS}"></script>
	    <script type="text/javascript" src='${AjaxCheckSessionJS}'></script>
	    <script type="text/javascript" src='${sideBarMenuJS}'></script>
	    <script type="text/javascript" src='${outlook2JS}'></script>
</html>