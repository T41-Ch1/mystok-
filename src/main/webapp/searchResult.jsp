<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="javax.servlet.http.HttpServletRequest" %>
<%@ page import="javax.servlet.http.HttpServletResponse" %>
<%@ page import="java.net.URLEncoder" %>
<%
final int DATA_PER_PAGE = 10;
String NFOUND_ERRORMSG ="該当するレシピが見つかりませんでした。";
int recipeNum = (int)(request.getAttribute("recipeNum"));
int pageNum = (int)(request.getAttribute("pageNum"));
String searchMode = (String)(request.getAttribute("searchMode"));
String[] inputData = (String[])(request.getAttribute("inputData"));
String txt = inputData[0];
for (int i = 1; i < inputData.length; i++){
	txt += "　" + inputData[i];
}
ArrayList<Integer> recipeID = (ArrayList)(request.getAttribute("recipeID"));
ArrayList<String> recipeTitle = (ArrayList)(request.getAttribute("recipeTitle"));
//###################################################################################
ArrayList<ArrayList<String[]>> list =new ArrayList<>();
//###################################################################################
//ArrayList<String> recipeText = (ArrayList)(request.getAttribute("recipeText"));
//###################################################################################
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
            <a href="test.html"><img src="images/logo.PNG"alt="Logo"></a>
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
if (searchMode.equals("syokuzai") && inputData.length > 1) {
%>
  <!--aside開始-->
      <aside>
        <h3>特に消費したい食材</h3>
        <form action="SearchResultServlet?searchMode=syokuzai" method="get"><!--サイドバー側のラジオボタン-->
          <div class="sradio-font">
            <ul>
            <%
            for (int i = 0; i < inputData.length; i++) {
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
                   <input type="radio" id = "f-option" name="searchMode" value="syokuzai" checked><label for="f-option">食材名検索</label>
                   <div class="check"></div><!--ラジオボタンチェックする円のdiv５-->
                 </li>
                 <li>
                   <input type="radio" id ="s-option" name="searchMode" value="ryouri"><label for="s-option">料理名検索</label>
                   <div class="check"></div><!--ラジオボタンチェックする円のdiv６-->
                 </li>
             </ul>

           </div>
          <div class="kennsaku">
           <input id="mado" type="text" name="input" value="<%=txt%>" size=50 pattern="[\u3041-\u3096|\u3000|\u30fc]*" maxlength=50 required>
           <input id="mbutton" type="submit" value="検索">
          </div>
            </form>
        </h1>

    <!--mein終了-->
     <!--レシピ情報-->
<%
if (recipeNum == 0) {
	out.println(NFOUND_ERRORMSG);
} else {
	for (int i = 0; i < recipeID.size(); i++){
%>
         <div class="recipebox">
           <div class="recipeimage">
            <img alt="<%= recipeTitle.get(i) %>" width="200" height="200" src="Picture/RyouriPIC/ryouri<%=String.format("%06d", recipeID.get(i))%>.jpg" class="recipetori">
           </div>
           <div class="racipe-text">
            <h2 class="recipetitle">
              <a class="recipititlelink" href="RecipeServlet?recipeID=<%= recipeID.get(i) %>&txt=<%= URLEncoder.encode(txt, "UTF-8") %>"><%= recipeTitle.get(i) %></a></h2>
           <div class="material">
<%
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
/*
材料の個数や単位を表示させる場合のコード
for ( int j = 1; j < list.get(i).size(); j++) {
	while (list.get(i).get(j)[1].length() > 0 && list.get(i).get(j)[1].substring(list.get(i).get(j)[1].length() - 1).equals("0")) {
		list.get(i).get(j)[1] = list.get(i).get(j)[1].substring(0, list.get(i).get(j)[1].length() - 1);
	}
	if (list.get(i).get(j)[1].substring(list.get(i).get(j)[1].length() - 1).equals(".")) {
		list.get(i).get(j)[1] = list.get(i).get(j)[1].substring(0, list.get(i).get(j)[1].length() - 1);
	}
	out.println(list.get(i).get(j)[0] + " " + list.get(i).get(j)[1] + " " + list.get(i).get(j)[2] + "<br>");
}
*/
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
if (recipeNum > 10) {
	int pageTotal = recipeNum / 10 + 1;
%>
      <div class="page-number">
        <ul class="page-list">
      <li><<</li><li><</li><li>前へ</li>
<%
for (int i = 1; i <= pageTotal; i++) {
	String buf = "<li><a href=\"SearchResultServlet?searchMode=" + searchMode +"&input=";
	for (int j = 0; j < inputData.length; j++)	{
		if (j == 0) {
			buf += URLEncoder.encode(inputData[j], "UTF-8");
		} else {
			buf += URLEncoder.encode("　" + inputData[j], "UTF-8");
		}
	}
	buf += "&pageNum=" + i + "\">" + i + "</a></li>";
	out.print(buf);
}
%>
      <li>次へ</li><li>></li><li>>></li>
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
    <h4>TOPに戻る</h4>
  </footer>

<!-- footer終了 -->

</div>

  <!-- wrap終了 -->


</body>
</html>
