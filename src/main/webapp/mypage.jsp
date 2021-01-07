<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="mystok.func.Util" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>テスト用マイページ</title>
</head>
<body>
<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->
	<%
	//認証チェック
	if (!Util.checkAuth(request, response)) return;
	%>
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
	<a href="top.jsp">トップへ</a><br>
	<form method="post" name="form1" action="MyRecipePageServlet">
	<a href="javascript:form1.submit()">マイレシピページ</a>
	</form>
	<form method="post" name="form2" action="FavoPageServlet">
	<a href="javascript:form2.submit()">お気に入りページ</a>
	</form>
	<form method="post" name="form3" action="RecipeRegisterPageServlet">
	<a href="javascript:form3.submit()">献立登録ページ</a>
	</form>
	<a href="logout.jsp">ログアウト</a>
<jsp:include page="footer.jsp" /><!-- フッター部分 -->
</body>
</html>
