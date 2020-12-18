<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>あ</title>
<link rel="stylesheet" href="CSS/TOPStyle.css" type="text/css">
</head>
<body>
<!-- header開始 -->
<%
if(request.isUserInRole("user")) {
%>
	<header>
      <div class ="headerline">
        <ul class="nav"><!--ヘッダーの会員登録とログインのリンクリスト１-->
          <li class="logo"><!--真ん中のレシピコンシェルのロゴdiv１-->
            <a href="top.jsp"><img src="images/logo.PNG"alt="Logo"></a>
          </li>
          <li class ="kaiinn"><a href="mypage.jsp"><%= request.getRemoteUser() %>さん</a></li>
          <li><a href="logout.jsp">ログアウト</a></li>
        </ul>
    </div>
    </header>
<%
} else {
%>
<header>
      <div class ="headerline">
        <ul class="nav"><!--ヘッダーの会員登録とログインのリンクリスト１-->
          <li class="logo"><!--真ん中のレシピコンシェルのロゴdiv１-->
            <a href="top.jsp"><img src="images/logo.PNG"alt="Logo"></a>
          </li>
          <li class ="kaiinn"><a href="NewUserServlet">会員登録</a></li>
          <li><a href="mypage.jsp">ログイン</a></li>
        </ul>
    </div>
    </header>
<%
}
%>

<!-- header終了 -->


</body>
</html>