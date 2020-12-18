<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<!--head開始-->
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=11">
    <title>レシピ検索サイト　レシピコンシェル|TOP</title>
<link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,700&display=swap" rel="stylesheet">
<link href="CSS/login.css" rel="stylesheet">

    <!--フォント-->
<link href="CSS/menyustylerecipi.css" rel="stylesheet">

  </head>
<!--head終了-->

<!--body開始-->
  <body>

<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->

<!-- ここからログインの枠+入力フォーム等の入力 -->
<div class="form-wrapper">
  <h1>ログイン</h1>
  <form action="LoginServlet" method="post">
    <input type="hidden" name="targetURI" value="<%= (String)session.getAttribute("targetURI") %>"></input>
    <div class="form-item">
      <label for="username"></label><!-- autocomplete=off これ入れたら検索候補をなくせる -->
      <input type="text" name="username" required placeholder="　アカウント名を入力"></input>
    </div>
    <div class="form-item">
      <label for="password"></label>
      <input type="password" name="password" required placeholder="　パスワード"></input>
    </div>
    <div class="button-panel">
      <input type="submit" class="button" title="Sign In" value="ログイン"></input>
    </div>
  </form>
  <div class="form-footer">
    <p><a href="newuser.jsp">新規会員登録はこちら</a></p>
    <p><a href="#">ここはもしかしたら使うかも</a></p>
  </div>
</div>
<!-- ここからログインの枠+入力フォーム等の入力(終了) -->

  <!-- wrap終了 -->

<jsp:include page="footer.jsp" /><!-- フッター部分 -->

</body>
</html>