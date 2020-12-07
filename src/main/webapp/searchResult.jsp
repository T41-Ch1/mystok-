<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="java.net.URLEncoder" %>
<%
final int DATA_PER_PAGE = 10; //1ページごとに表示する最大件数
String NFOUND_ERRORMSG ="該当するレシピが見つかりませんでした。"; //検索結果がなかった場合表示するエラーメッセージ
int recipeNum = (int)(request.getAttribute("recipeNum")); //検索結果の件数
int pageNum = (int)(request.getAttribute("pageNum")); //10件ごとに表示した場合何ページ目か 1からスタートする
String searchMode = (String)(request.getAttribute("searchMode")); //検索モード 料理名検索ならryouri 食材名検索ならsyokuzaiが格納される
String[] inputData = (String[])(request.getAttribute("inputData")); //検索窓に入力された文字列をスペースで分割したもの
String input = inputData[0]; //inputDataに格納された文字列をスペースで連結したもの
for (int i = 1; i < inputData.length; i++){
	input += "　" + inputData[i];
}
ArrayList<Integer> recipeID = (ArrayList)(request.getAttribute("recipeID")); //表示するレシピのID(最大10件)
ArrayList<String> recipeTitle = (ArrayList)(request.getAttribute("recipeTitle")); //表示するレシピ名(最大10件)
ArrayList<String> recipeIntro = (ArrayList)(request.getAttribute("recipeIntro")); //表示するレシピの紹介文(最大10件)
ArrayList<ArrayList<String[]>> list =new ArrayList<>(); //表示するレシピの分量(最大10件)
list = (ArrayList<ArrayList<String[]>>)request.getAttribute("recipeBunryouList");
%>
<!DOCTYPE html>
<html>

<!--head開始-->
<head>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=11">
  <title>レシピ検索サイト　レシピコンシェル|検索結果一覧</title>
<!--
<link href="https://fonts.googleapis.com/css?family=Roboto+Slab:400,700&display=swap" rel="stylesheet">-->
  <link href="CSS/SearchResultStyle.css" rel="stylesheet">
</head>
<!--head終了-->

<!--body開始-->
<body><!--bodyにはWEBブラウザに表示させる内容を記載する-->

 <!-- header開始 -->
    <header>
      <div class="headerline">
        <ul class="nav"><!--ヘッダーの会員登録とログインのリンクリスト１-->
          <li class="logo"><!--真ん中のレシピコンシェルのロゴdiv１-->
            <a href="top.jsp"><img src="images/logo.PNG"alt="Logo"></a>
          </li>
          <li class="kaiinn"><a href="consept.html">会員登録</a></li>
          <li><a href="MENU.html">ログイン</a></li>
        </ul>
      </div>
    </header>
  <!-- header終了 -->

  <!-- mainとasaid開始 -->
  <div id="wrap" class="clearfix">
    <div class="content">
<%
//食材名検索で複数の食材が指定された場合のみ特に消費したい食材を選択し直せる枠を表示
if (searchMode.equals("syokuzai") && inputData.length > 1) {
%>
  <!--aside開始-->
      <aside>
        <h3>特に消費したい食材</h3>
        <form action="SearchResultServlet" method="get"><!--サイドバー側のラジオボタン-->
          <div class="sradio-font">
            <ul>
            <%
            for (int i = 0; i < inputData.length; i++) {
            	//選択された食材に対する検索文字列を生成しnewInputに格納する
        		String newInput = inputData[i];
        		int j;
            	for (j = 0; j < i; j++) {
            		newInput += "　" + inputData[j];
            	}
            	for (j++; j < inputData.length; j++) {
            		newInput += "　" + inputData[j];
            	}
            %>
             <li>
               <!-- パラメータinputにnewInputを格納して送信できるようにする 最初の要素にチェックを入れる -->
               <input type="radio" id="option<%= i %>" name="input" value="<%= newInput %>" <% if (i == 0) out.print("checked"); %>><label for="option<%= i %>"><%= inputData[i] %></label>
               <div class="scheck"></div>
             </li>
            <%
            }
            %>
             <li>
              <input id="narabikae" type="submit" value="並び替え">
             </li>
            </ul>
          </div>
        </form>
      </aside>
  <!--aside終了-->
<%
}
%>

  <!--main開始-->
      <main class="main">
       <article>
         <h1>検索結果
           <span> <%= recipeNum %> 件</span>
           <div class="radio-font"><!--ラジオボタンのdiv４-->
            <form action="SearchResultServlet" method="get"><!--メイン要素側のラジオボタン-->
             <ul class="radiolist"><!--ラジオボタンリストのul-->
                 <li>
                   <input type="radio" id = "f-option" name="searchMode" value="syokuzai" <% if (searchMode.equals("syokuzai")) out.print("checked"); %>><label for="f-option">食材名検索</label>
                   <div class="check"></div><!--ラジオボタンチェックする円のdiv５-->
                 </li>
                 <li>
                   <input type="radio" id ="s-option" name="searchMode" value="ryouri" <% if (!searchMode.equals("syokuzai")) out.print("checked"); %>><label for="s-option">料理名検索</label>
                   <div class="check"></div><!--ラジオボタンチェックする円のdiv６-->
                 </li>
             </ul>

           </div>
          <div class="kennsaku">
           <!-- \u3041-\u3096は平仮名、\u3000は全角スペース、\u30fcは長音 これらの文字の組み合わせのみ許可する 正規表現で書いたのがpatternの所 -->
           <input id="mado" type="text" name="input" value="<%=input%>" size=50 pattern="[\u3041-\u3096|\u3000|\u30fc]*" maxlength=50 required>
           <input id="mbutton" type="submit" value="検索">
          </div>
            </form>
        </h1>

    <!--main終了-->
     <!--レシピ情報-->
<%
if (recipeNum == 0) {
	out.println(NFOUND_ERRORMSG); //検索結果がなかった場合エラーメッセージを表示
} else {
	for (int i = 0; i < recipeID.size(); i++){
%>
         <div class="recipebox">
           <div class="recipeimage">
            <!-- レシピのIDをゼロパディングしてファイル名を生成する -->
            <img alt="<%= recipeTitle.get(i) %>" width="200" height="200" src="Picture/RyouriPIC/ryouri<%=String.format("%06d", recipeID.get(i))%>.jpg" class="recipetori">
           </div>
           <div class="racipe-text">
            <h2 class="recipetitle">
              <a class="recipititlelink" href="RecipeServlet?recipeID=<%= recipeID.get(i) %>&input=<%= URLEncoder.encode(input, "UTF-8") %>&searchMode=<%= searchMode %>"><%= recipeTitle.get(i) %></a></h2>
           <div class="material">
<%
out.println(recipeIntro.get(i) + "<br>");
out.println("材料：<br>");
for ( int j = 0; j < list.get(i).size(); j++) {
	while (list.get(i).get(j)[1].length() > 0 && list.get(i).get(j)[1].substring(list.get(i).get(j)[1].length() - 1).equals("0")) {
		list.get(i).get(j)[1] = list.get(i).get(j)[1].substring(0, list.get(i).get(j)[1].length() - 1);
	}
	if (list.get(i).get(j)[1].substring(list.get(i).get(j)[1].length() - 1).equals(".")) {
		list.get(i).get(j)[1] = list.get(i).get(j)[1].substring(0, list.get(i).get(j)[1].length() - 1);
	}
	out.println(list.get(i).get(j)[0] + " " + list.get(i).get(j)[1] + " " + list.get(i).get(j)[2] + "<br>");
}
%>
           </div>
           <div class="clear"></div>
           </div>
        </div>
<%
	}
}
%>

<%
//検索結果が11件以上ならページ送りするためのリンクを用意する
	//////////デバッグ用//////////
	recipeNum = 1000;
	//////////////////////////////
if (recipeNum > 10) {
	int pageTotal = (recipeNum - 1) / 10 + 1;
	String inputDataStr = URLEncoder.encode(inputData[0], "UTF-8");
	for (int i = 1; i < inputData.length; i++)	inputDataStr += URLEncoder.encode("　" + inputData[i], "UTF-8");
%>
      <div class="page-number">
        <ul class="page-list">
      <!-- ｢<<｣の表示 -->
      <li><%
      if (pageNum == 1) out.print("<<");
      else out.print("<a href=\"SearchResultServlet?searchMode=" + searchMode +"&input=" + inputDataStr + "&pageNum=1\"><<</a>");
      %></li>
      <!-- ｢< 前へ｣の表示 -->
      <li><%
      if (pageNum == 1) out.print("< 前へ");
      else out.print("<a href=\"SearchResultServlet?searchMode=" + searchMode +"&input=" + inputDataStr + "&pageNum=" + (pageNum - 1) + "\">< 前へ</a>");
      %></li>
      <!-- ｢4 5 6 7 8 9 10 11 12｣の表示 pageNumの前後4件まで -->
      <%
      for (int i = Math.max(1, pageNum - 4); i <= Math.min(pageNum + 4, pageTotal); i++) {
       out.print("<li><a href=\"SearchResultServlet?searchMode=" + searchMode +"&input=" + inputDataStr + "&pageNum=" + i + "\">" + i + "</a></li>");
      }
      %>
      <!-- ｢次へ >｣の表示 -->
      <li><%
      if (pageNum == pageTotal) out.print("次へ >");
      else out.print("<a href=\"SearchResultServlet?searchMode=" + searchMode +"&input=" + inputDataStr + "&pageNum=" + (pageNum + 1) + "\">次へ ></a>");
      %></li>
      <!-- ｢>>｣の表示 -->
      <li><%
      if (pageNum == pageTotal) out.print(">>");
      else out.print("<a href=\"SearchResultServlet?searchMode=" + searchMode +"&input=" + inputDataStr + "&pageNum=" + pageTotal +"\">>></a>");
      %></li>
    </ul>
    </div>
<%
}
%>
  </article>
  </main>

</div>
<!-- footer開始 -->
  <footer>
    <h4><a href="top.jsp">TOPに戻る</a></h4>
  </footer>

<!-- footer終了 -->

</div>

  <!-- wrap終了 -->

</body>
</html>
