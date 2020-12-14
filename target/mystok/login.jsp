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

<!-- header開始 -->
    <header>
      <div class ="headerline">
      <ul class="nav"><!--ヘッダーの会員登録とログインのリンクリスト１-->
        <li class="logo"><!--真ん中のレシピコンシェルのロゴdiv１-->
          <a href="top.jsp"><img src="images/logo.PNG"alt="Logo"></a>
        </li>
        <li class ="kaiinn"><a href="consept.html">会員登録</a></li>
        <li><a href="MENU.html">ログイン</a></li>
      </ul>
    </div>
    </header>
<!-- header終了 -->

<!-- ここからログインの枠+入力フォーム等の入力 -->
<div class="form-wrapper">
  <h1>ログイン</h1>
  <form action="j_security_check" method="post">
    <div class="form-item">
      <label for="email"></label><!-- autocomplete=off これ入れたら検索候補をなくせる -->
      <input type="text" name="j_username" required placeholder="　アカウント名を入力"></input>
    </div>
    <div class="form-item">
      <label for="password"></label>
      <input type="password" name="j_password" required placeholder="　パスワード"></input>
    </div>
    <div class="button-panel">
      <input type="submit" class="button" title="Sign In" value="ログイン"></input>
    </div>
  </form>
  <div class="form-footer">
    <p><a href="#">新規会員登録はこちら</a></p>
    <p><a href="#">ここはもしかしたら使うかも</a></p>
  </div>
</div>
<!-- ここからログインの枠+入力フォーム等の入力(終了) -->

  <!-- wrap終了 -->

<!-- footer開始 -->
  <footer>
  <h4><a href="top.jsp">TOPに戻る</a></h4>
  </footer>

<!-- footer終了 -->

</body>
</html>