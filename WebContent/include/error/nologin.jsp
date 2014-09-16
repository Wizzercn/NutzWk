<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${base}/include/css/index.css" rel="stylesheet" type="text/css"> 
<script type='text/javascript'>
	var CONTEXTPATH = '${base}';
</script>
<script type='text/javascript' src='${base}/include/js/main.js'></script> 
<title>用户登陆已失效</title>
<script type="text/javascript">
    $(document).ready(function(){
    	top.Dialog.confirm("用户登陆已失效，请重新登陆！",function(){
			window.top.location.href="${base}/private/login";
		},function(){
			window.top.location.href="${base}/private/login";
		});
		
	});
</script>
</head>
<body style="overflow:hidden">
&nbsp;
&nbsp;
&nbsp;
&nbsp;
</body>
</html>