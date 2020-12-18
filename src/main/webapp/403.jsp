<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>403 Forbidden</title>
</head>
<body style="text-align: center;">
	<h2>403エラー</h2>
	<p><%= request.getRemoteUser() %>さんは、<br>
	このページにアクセスできるロールではありません。</p>
	<a href="top.jsp">トップへ</a><br>
</body>
</html>