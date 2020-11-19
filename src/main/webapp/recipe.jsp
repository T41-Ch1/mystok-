<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.util.*" import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>recipe.jsp</title>
</head>
<body>
<h1>なんでなんだああああああああああああ</h1>
<%=request.getAttribute("recipe_name")%>
<%=request.getAttribute("tukurikata")%>

<%
ArrayList<String[]> list =new ArrayList<>();
list = (ArrayList<String[]>)request.getAttribute("recipe_bunryou");
//テスト用
for (int i=0;i < list.size();i++) {
	for ( int j=0; j < 3;j++) {

		out.println(list.get(i)[j]);
	}
}
%>


</body>
</html>
