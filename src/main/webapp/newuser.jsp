<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8">
<title>新規登録画面</title>
</head>
<body>
<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->
<form action="NewUserServlet" method="post">
名前<input type="text" name="name" size="20" required>
<br>
パスワード<input type="password" name="password" size="20" required>
<br>
<input type="submit" value="送信">

</form>
<jsp:include page="footer.jsp" /><!-- フッター部分 -->
</body>
</html>