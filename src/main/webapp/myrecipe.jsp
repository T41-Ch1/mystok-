<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="pac1.func.Util" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8">
<title>マイレシピページ</title>
</head>
<body>
<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->
<%
//認証チェック
if (!Util.checkAuth(request, response)) return;
%>
<%
final int DATA_PER_PAGE = 25;
ArrayList<Integer> ryouriID = (ArrayList)(request.getAttribute("ryouriID")); //
ArrayList<String> ryourimei = (ArrayList)(request.getAttribute("ryourimei")); //
ArrayList<Boolean> favoList = (ArrayList)(request.getAttribute("favoList")); //
int pageNum = (int)(request.getAttribute("pageNum")); //
for (int i = 0;i < ryouriID.size();i++) {
	out.println(ryourimei.get(i) + "<br>");
}
%>
<jsp:include page="footer.jsp" /><!-- フッター部分 -->
</body>
</html>