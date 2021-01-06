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
<body><!--bodyにはWEBブラウザに表示させる内容を記載する-->

<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->

<%
String recipe_name = (String)request.getAttribute("recipe_name"); //表示するレシピ名
String tukurikata = (String)request.getAttribute("tukurikata"); //表示するレシピの作り方
ArrayList<String[]> bunryouList =new ArrayList<>(); //表示するレシピの分量
bunryouList = (ArrayList<String[]>)request.getAttribute("recipe_bunryou");
int recipeID = (int)(request.getAttribute("recipeID")); //表示するレシピのID
boolean favo; //お気に入り登録されているかどうか
if (Objects.equals(request.getAttribute("favo"), null)) {
	favo = false;
} else {
	favo = (boolean)request.getAttribute("favo");
}
String searchMode; //検索窓のラジオボタンに最初からチェックを入れる方
if (Objects.equals(request.getAttribute("searchMode"), null)) {
	searchMode = "syokuzai";
} else {
	searchMode = (String)request.getAttribute("searchMode");
}
String input; //検索窓に表示する文字列
if (Objects.equals(request.getAttribute("input"), null)) {
	input = "";
} else {
	input = (String)request.getAttribute("input");
}
%>

<!-- wrap開始 -->
  <div id="wrap" class="clearfix">
    <div class="content">
      <div class="main">
  <!--ラジオボタン開始-->
    <form action="SearchResultServlet" method="get">
    <div class ="radio-font"><!--ラジオボタンのdiv４-->
      <ul class ="radiolist"><!--ラジオボタンリストのul-->
        <li>
          <input type="radio" id ="f-option" name="searchMode" value="syokuzai" <% if (searchMode.equals("syokuzai")) out.print("checked"); %>><label for="f-option">食材名検索</label>
          <div class ="check"><!--ラジオボタンチェックする円のdiv５-->
          </div>
        </li>
        <li>
          <input type="radio" id ="s-option" name="searchMode" value="ryouri" <% if (!searchMode.equals("syokuzai")) out.print("checked"); %>><label for ="s-option">料理名検索</label>
          <div class ="check">
          </div><!--ラジオボタンチェックする円のdiv６-->
        </li>
      </ul>
    </div>
  <!--ラジオボタン終了-->

  <!--検索窓開始-->
  <!-- \u3041-\u3096は平仮名、\u3000は全角スペース、\u30fcは長音 これらの文字の組み合わせのみ許可する 正規表現で書いたのがpatternの所 -->
      <input id="mado" type="text" name="input" value="<%= input %>" size=50 pattern="[\u3041-\u3096|\u3000|\u30fc]*" maxlength=50 placeholder=" 例）じゃがいも　かれー等　【ひらがな入力のみ】" required>
      <input id ="mbutton" type="submit" value="レシピ検索" onclick="func1()">
      <script>
       //二度押し防止機能
       function func1() {
        document.mkensaku.submit();
        document.getElementById('mbutton').disabled = true;
       }
      </script>
    </form>
  <!--検索窓終了-->

<!--料理名（タイトル）-->
<!--<span><a href="xxxx.html"><img src="images/無色ハート.png"
   alt="お気に入りボタン" width="40" height="40" class="heart"></a></span>
   <span><p><font size="7">肉じゃが</font>

 </p>
-->

<div class ="titlebox">
  <div class ="title"><%= recipe_name %></div>
  <div class ="iconbutton">
<%
String tabeta = "";
if (favo) tabeta = "aceat.png";
else tabeta = "bceat.png";
%>
<a href="xxxx.html" id="eat"><img src="images/<%= tabeta %>" alt="今日食べたボタン" width="35" height="35"></a>
<%
String heart = "";
if (favo) heart = "pink_heart.png";
else heart = "clear_heart.png";
%>
<a href="javascript:favobutton()" id="heart"><img src="images/<%= heart %>" alt="お気に入りボタン" width="35" height="35"></a>
  </div>
</div>
<iframe id="cFrame" width=0 height=0></iframe>
<script>
var favo = <%= favo %>;
function favobutton() {
	console.log(/SESS\w*ID=([^;]+)/i.test(document.cookie) ? RegExp.$1 : false);
	if (favo) {
		document.getElementById('cFrame').src = 'FavoDeleteServlet';
		if (window.sessionStorage.getItem('favoDelete')) {
			heart.innerHTML='<img src="images/clear_heart.png" alt="お気に入りボタン" width="35" height="35">';
			favo = !favo;
		} else {
			alert('ログインしてから押してください');
		}
		return false;
	} else {
		document.getElementById('cFrame').src = 'FavoInsertServlet';
		if (window.sessionStorage.getItem('favoInsert')) {
			heart.innerHTML='<img src="images/pink_heart.png" alt="お気に入りボタン" width="35" height="35">';
			favo = !favo;
		} else {
			alert('ログインしてから押してください');
		}
		return false;
	}
}
</script>


<!--料理名（タイトル）終了-->

<!-- 料理の写真 -->
<!-- レシピのIDをゼロパディングしてファイル名を生成する -->
<img src="images/RyouriPIC/ryouri<%= String.format("%06d", recipeID) %>.jpg" alt="写真" width="45%" height="450" border="1" align="left" class="recipetori">

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
//作り方を｢/｣で分割する
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

<jsp:include page="footer.jsp" /><!-- フッター部分 -->

</body>
</html>