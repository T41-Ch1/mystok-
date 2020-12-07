<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>レシピ検索サイト　レシピコンシェル|認証失敗画面</title>
</head>
<body>
	<p><%= request.getRemoteUser() %>さんは、<br>
	このページにアクセスできるロールではありません。</p>
	ログインできません。<br>
	ユーザ名 または パスワードに誤りがあります。<br>
	<a href="top.jsp">トップへ</a>
</body>
</html>