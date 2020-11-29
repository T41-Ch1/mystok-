<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h2>ようこそ、<%= request.getRemoteUser() %>さん</h2>
	<%
	if (request.isUserInRole("user")) {
	%>
	あなたのロールは、userです。
	<%
	}
	%>
	<br>
	<h2>ここは管理画面(JSP)です。</h2>
	このページは、認証の必要があります。<br><br>
	<a href="index.html">トップへ</a><br>
	<a href="logout.jsp">ログアウト</a>
</body>
</html>