<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" import="java.io.*" %>
<!DOCTYPE html>
<html>
<head><!--headにはPCが参照する情報を記入する　例：タイトルや読み込む関連ファイル -->
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=11">
<title>レシピ検索サイト　レシピコンシェル｜レシピ</title>
<link href="CSS/RecipePageStyle.css" rel="stylesheet">
</head>
<body id="photograph"><!--bodyにはWEBブラウザに表示させる内容を記載する-->


  <!-- header開始 -->
      <header>
        <div class ="headerline">
        <ul class="nav"><!--ヘッダーの会員登録とログインのリンクリスト１-->
          <li class="logo"><!--真ん中のレシピコンシェルのロゴdiv１-->
            <a href="test.html"><img src="images/logo.PNG"alt="Logo"></a>
          </li>
          <li class ="kaiinn"><a href="consept.html">会員登録</a></li>
          <li><a href="MENU.html">ログイン</a></li>
        </ul>
      </div>
      </header>
  <!-- header終了 -->

<%
String recipe_name = (String)request.getAttribute("recipe_name");
String tukurikata = (String)request.getAttribute("tukurikata");
ArrayList<String[]> bunryouList =new ArrayList<>();
bunryouList = (ArrayList<String[]>)request.getAttribute("recipe_bunryou");
String recipeID = (String)(request.getAttribute("recipeID"));

%>


<!-- wrap開始 -->
  <div id="wrap" class="clearfix">
    <div class="content">
      <div class="main">
  <!--ラジオボタン開始-->
    <form>
    <div class ="radio-font"><!--ラジオボタンのdiv４-->
      <ul class ="radiolist"><!--ラジオボタンリストのul-->
        <li>
          <input type="radio" id ="f-option" name="searchMode" value="syokuzai" checked><label for="f-option">食材名検索</label>
          <div class ="check"><!--ラジオボタンチェックする円のdiv５-->
          </div>
        </li>
        <li>
          <input type="radio" id ="s-option" name="searchMode" value="ryouri"><label for ="s-option">料理名検索</label>
          <div class ="check">
          </div><!--ラジオボタンチェックする円のdiv６-->
        </li>
      </ul>
    </div>
  <!--ラジオボタン終了-->

  <!--検索窓開始-->
      <input id ="mado" type="text" name="input" size=50 pattern="[\u3041-\u3096]*" maxlength=50 placeholder=" 例）じゃがいも　カレー等"  required>
      <input id ="mbutton" type="submit" value="レシピ検索" onclick="func1()">
        <script>
          function func1() {
              document.getElementById("mbutton").disabled = true;
          }
        </script>
    </form>
  <!--検索窓終了-->


<!--料理名（タイトル）-->
 <p><font size="7">　<%= recipe_name %></font></p>
<!--料理名（タイトル）終了-->



<!-- 料理の写真 -->
<img src="Picture/RyouriPIC/ryouri<%=String.format("%06d", Integer.parseInt(recipeID))%>.jpg" alt="写真" width="45%" height="450" border="1" align="left" class="recipetori">



<!--写真右側の必要材料入力開始-->
<div style=" padding:10px; border-radius: 10px; border: 3px dotted #ffb6c1;width:300px;margin-left:auto;margin-right:100px;">
	<h2 style="text-align:center">必要な材料</h2><!-- 必要な材料を書く場所 -->

	<%
	for (int i = 0; i < bunryouList.size(); i++) {
		//個数のデータの右端にあるゼロを消す　整数の場合小数点も消す
		while (bunryouList.get(i)[1].length() > 0 && bunryouList.get(i)[1].substring(bunryouList.get(i)[1].length() - 1).equals("0")) {
			bunryouList.get(i)[1] = bunryouList.get(i)[1].substring(0, bunryouList.get(i)[1].length() - 1);
		}
		if (bunryouList.get(i)[1].substring(bunryouList.get(i)[1].length() - 1).equals(".")) {
			bunryouList.get(i)[1] = bunryouList.get(i)[1].substring(0, bunryouList.get(i)[1].length() - 1);
		}
		out.println("<DIV style=\"text-align:left;\">");
		out.println("<DIV style=\"text-align:right;float:right;\">" + bunryouList.get(i)[1] + " " + bunryouList.get(i)[2] + "</DIV>　★" + bunryouList.get(i)[0] + "</div>");
	}
	%>
</div>

<p style="clear:both;">
　
</p>



<section>

	<h1 style="text-align:center">レシピ</h1>

<%
String[] tukurikataSplit;
tukurikataSplit = tukurikata.split("/");
for (int i = 0; i < tukurikataSplit.length; i++) {
	out.println("<p> <h2 style=\"justify\">" + (i + 1) + ".　" + tukurikataSplit[i] + "</h2></p>");
}

%>

</section>






      </div>

    </div>
  </div>
  <!-- wrap終了 -->






  <!-- footer開始 -->
  <footer>
  <div class="top">
      <a href="TOP.html">TOPへ戻る</a>
    </div>
  </footer>
  <!-- footer終了 -->

</body>
</html>