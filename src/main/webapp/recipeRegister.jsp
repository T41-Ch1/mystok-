<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.util.ArrayList" %>
    <%@ page import="java.util.Objects" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>仮の献立登録画面</title>
</head>
<body>

<!--
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
formによるアップロードファイルサイズの制限だけでは不十分なため、サーバー側でもファイルサイズをチェックする処理を追加する
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 -->

<%
ArrayList<String> syokuzaikanalist = (ArrayList<String>)request.getAttribute("syokuzaikanalist"); //検索結果の複数のSyokuzaiKanaを格納する配列。
ArrayList<String> tannilist = (ArrayList<String>)request.getAttribute("tannilist"); //検索結果の複数のTanniを格納する配列。
%>
<form action="RecipeRegisterServlet" method="post" enctype="multipart/form-data">

<datalist id="syokuzaikanalist">
 <!-- syokuzaikanalistの個数分だけ内容を並べる -->
 <%
 for (int i = 0; i < syokuzaikanalist.size(); i++) out.println("<option value=\"" + syokuzaikanalist.get(i) + "\">");
 %>
</datalist>

料理名(255文字まで):<input type="text" name="ryourimei" maxlength=255 required>
<br>
<!-- \u3041-\u3096は平仮名、\u3000は全角スペース、\u30fcは長音 これらの文字の組み合わせのみ許可する 正規表現で書いたのがpatternの所 -->
料理名のふりがな(255文字まで):<input type="text" name="ryourikana" maxlength=255 required pattern="[\u3041-\u3096|\u3000|\u30fc]*">
<br>
作り方(改行で段落分け、8000文字まで):<br><textarea name="tukurikata" cols=50 rows=10 maxlength=8000 required></textarea>
<br>
紹介文(100文字まで):<input type="text" name="syoukai" maxlength=100 required>
<br>
食材1(ひらがな):<input type="text" name="syokuzaikana1" list="syokuzaikanalist" placeholder="プルダウンメニュー" autocomplete="off" size=30 required onchange="getTanni();">
&emsp;分量:<input type="text" name="bunryou1" size=10 required>
<div id="item1tanni"></div>
<!-- 食材2以降を追加する部分 -->
<div id="item2"></div>
<input type="button" value="食材を追加する" onClick="ItemField.add();" />
<input type="button" value="食材を削除する" onClick="ItemField.remove();" />
<script type="text/javascript">
ItemField.currentNumber = 1;
ItemField.itemTemplate
    = '食材__count__(ひらがな):'
    + '<input type="text" name="syokuzaikana__count__" list="syokuzaikanalist" placeholder="プルダウンメニュー" autocomplete="off" size=30 required>'
    + '&emsp;分量:<input type="text" name="bunryou__count__" size=10 required>';
</script>
<br>
画像ファイル(任意)<input type="file" id="ryouripic" name="pic" accept="image/*">
<br>
<input type="submit" value="送信" onclick="clickBtn5()">
<input type="hidden" name="syokuzaikanalist" value="<%= syokuzaikanalist %>">
</form>

<script>
//食材指定による単位の取得
function getTanni(){
	var syokuzaikanalist = document.getElementById("syokuzaikanalist");
}
//食材指定テキストボックスの増減
var ItemField = {
    currentNumber : 1,
    itemTemplate : '食材__count__(ひらがな):'
        + '<input type="text" name="syokuzaikana__count__" list="syokuzaikanalist" placeholder="プルダウンメニュー" autocomplete="off" size="30">'
        + '&emsp;分量:<input type="text" name="bunryou__count__" size="10">',
    add : function () {
        this.currentNumber++;

        var field = document.getElementById('item' + this.currentNumber);

        var newItem = this.itemTemplate.replace(/__count__/mg, this.currentNumber); //mは複数行の入力文字列を複数行として扱う（^及び$が各行の先頭末尾にマッチする） gはグローバルサーチ。文字列全体に対してマッチングするか（無指定の場合は1度マッチングした時点で処理を終了）
        field.innerHTML = newItem;

        var nextNumber = this.currentNumber + 1;
        var new_area = document.createElement("div");
        new_area.setAttribute("id", "item" + nextNumber);
        field.appendChild(new_area);
    },
    remove : function () {
        if ( this.currentNumber == 1 ) { return; }

        var field = document.getElementById('item' + this.currentNumber);
        field.removeChild(field.lastChild);
        field.innerHTML = '';

        this.currentNumber--;
    }
}
//画像ファイルサイズ制限 1MBまで
const sizeLimit = 1024 * 1024 * 1; // 制限サイズ
const fileInput = document.getElementById('ryouripic'); // input要素
// changeイベントで呼び出す関数
const handleFileSelect = () => {
  const files = fileInput.files;
  for (let i = 0; i < files.length; i++) {
    if (files[i].size > sizeLimit) {
      // ファイルサイズが制限以上
      alert('ファイルサイズは1MB以下にしてください'); // エラーメッセージを表示
      fileInput.value = ''; // inputの中身をリセット
      return; // この時点で処理を終了する
    }
  }
}
// フィールドの値が変更された時（≒ファイル選択時）に、handleFileSelectを発火
fileInput.addEventListener('change', handleFileSelect);
</script>
<script>
/*
function clickBtn5(){
	保留
}
*/
</script>

<%
if (Objects.equals(request.getAttribute("register_success"), "success")) {
%>
<script>window.confirm("献立を1件登録しました。");</script>
<%
}
%>

</body>
</html>