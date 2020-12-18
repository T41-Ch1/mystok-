<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pac1.func.Util" %>
<!DOCTYPE html>
<html>

<!--head開始-->
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=11">
    <title>レシピ検索サイト　レシピコンシェル|お気に入り</title>

    <link rel="stylesheet" href="CSS/okiniiri.css" type="text/css">
  </head>
<!--head終了-->

<!--body開始-->
<body>

<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->

<%
//認証チェック
if (!Util.checkAuth(request, response)) return;
%>
<%
ArrayList<Integer> recipeID = (ArrayList<Integer>)request.getAttribute("recipeID");
ArrayList<String> ryourimei = (ArrayList<String>)request.getAttribute("ryourimei");
%>
<div id="wrap">
<h1 style=text-align:left >お気に入り</h1>
<hr color="#000000" width="100%" size="7">
<ul>
<%
for (int i=0;i<recipeID.size();i++) {
%>
<div class="pic_frame"><li><a href="RecipeServlet?recipeID=<%= recipeID.get(i) %>"><img src="Picture/RyouriPIC/ryouri<%=String.format("%06d", recipeID.get(i))%>.jpg" alt="レシピページ遷移"><p><%= ryourimei.get(i) %></p></a></li>
<img class="favo" src="images/色ハート.png" width="20" height="20"></div>
<%
}
%>
<%
//テスト用に25件並べる
for (int i=recipeID.size();i<25;i++) {
%>
<div class="pic_frame"><li><a href="RecipeServlet?recipeID=<%= recipeID.get(0) %>"><img src="Picture/RyouriPIC/ryouri<%=String.format("%06d", recipeID.get(0))%>.jpg" alt="レシピページ遷移"><p><%= ryourimei.get(0) %></p></a></li>
<img class="favo" src="images/色ハート.png" width="20" height="20"></div>
<%
}
%>
</ul>
</div>
<script>
            var pics_src = new Array("images/色ハート.png","images/無色ハート.png");
            var heart = [];
 		for (var i = 0;i < 10;i++) {
			heart.push('0');
		}

            function slideshow(index){
                heart[index] = 1 - heart[index];
                document.getElementsByClassName('favo')[index].src=pics_src[heart[index]];
            }

	const lists = Array.from(document.querySelectorAll('.pic_frame .favo'));
		console.log(lists);
	lists.forEach(li => {
  		li.addEventListener("click", e => {
    	const index = lists.findIndex(list => list === e.target);
			slideshow(index);
  		});
	});


</script>
      <div class="page-number">
        <ul class="page-list">
      <!-- ｢<<｣の表示 -->
      <li><<</li>
      <!-- ｢< 前へ｣の表示 -->
      <li>< 前へ</li>
      <!-- ｢4 5 6 7 8 9 10 11 12｣の表示 pageNumの前後4件まで -->
<li><a href="">1</a></li>
<li><a href="">2</a></li>
<li><a href="">3</a></li>
<li><a href="">4</a></li>
<li><a href="">5</a></li>
      <!-- ｢次へ >｣の表示 -->
      <li><a href="">次へ ></a></li>
      <!-- ｢>>｣の表示 -->
      <li><a href="">>></a></li>
    </ul>
    </div>

<jsp:include page="footer.jsp" /><!-- フッター部分 -->

  </body>
<!--head終了-->
</html>

