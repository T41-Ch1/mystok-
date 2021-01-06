<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Objects" %>
<%@ page import="pac1.func.Util" %>
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
<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->
<%
//認証チェック
if (!Util.checkAuth(request, response)) return;
%>
<%
ArrayList<String> syokuzaikanalist = (ArrayList<String>)request.getAttribute("syokuzaikanalist"); //検索結果の複数のSyokuzaiKanaを格納する配列。
ArrayList<String> tannilist = (ArrayList<String>)request.getAttribute("tannilist"); //検索結果の複数のTanniを格納する配列。
%>
<form name="recipeRegisterForm" action="RecipeRegisterServlet" method="post" enctype="multipart/form-data" onsubmit="return false;"><!-- 誤Enterに反応しないようにする -->
	<datalist id="syokuzaikanalist">
	<%
	//syokuzaikanalistの個数分だけプルダウンの中身を並べる
	for (int i = 0; i < syokuzaikanalist.size(); i++) out.println("<option value=\"" + syokuzaikanalist.get(i) + "\">");
	%>
	</datalist>

	料理名(255文字まで):<input type="text" id="ryourimei" name="ryourimei" maxlength=255 required>
	<br>
	<!-- \u3041-\u3096は平仮名、\u3000は全角スペース、\u30fcは長音 これらの文字の組み合わせのみ許可する 正規表現で書いたのがpatternの所 -->
	料理名のふりがな(255文字まで):<input type="text" id="ryourikana" name="ryourikana" maxlength=255 required pattern="[\u3041-\u3096|\u3000|\u30fc]*">
	<br>
	作り方(改行で段落分け、8000文字まで):<br><textarea id="tukurikata" name="tukurikata" cols=50 rows=10 maxlength=8000 required></textarea>
	<br>
	紹介文(100文字まで):<input type="text" id="syoukai" name="syoukai" maxlength=100 required>
	<br>
	<div id="syokuzaicontainer">
		<div id="syokuzaifield1">
			食材1(ひらがな):<input id="syokuzai1" type="text" name="syokuzaikana1" list="syokuzaikanalist" placeholder="プルダウンメニュー" autocomplete="off" size=30 required onChange="getTanni(1)">&emsp;分量:<input type="text" id="bunryou1" name="bunryou1" size=10 required>&emsp;<span id="tanni1"></span>
		</div>
		<!-- 食材2以降を追加する部分 -->
		<div id="syokuzaifield2"></div>
	</div>
	<input type="button" value="食材を追加する" onClick="ItemField.add();" />
	<input type="button" value="食材を削除する" onClick="ItemField.remove();" />
	<br>
	画像ファイル(任意)<input type="file" id="ryouripic" name="pic" accept="image/*">
	<br>
	<input type="button" value="送信" onClick="completeCheck();"><!-- 送信ボタンをクリックしたらsubmitではなく判定を行う -->
	<input type="hidden" name="syokuzaikanalist" value="<%= syokuzaikanalist %>">
	<input type="submit" value="不可視ボタン"  style="display:none" name=submitBtn><!-- formのエラーチェック用 -->
</form>

<script>
<%
//javascriptに食材リストと単位リストを生成
out.print("var syokuzaikanalist = ['" + syokuzaikanalist.get(0));
for (int i = 1; i < syokuzaikanalist.size(); i++) {
	out.print("','" + syokuzaikanalist.get(i));
}
out.println("']");
out.print("var tannilist = ['" + tannilist.get(0));
for (int i = 1; i < tannilist.size(); i++) {
	out.print("','" + tannilist.get(i));
}
out.println("']");
%>
//食材欄に入力の変化があったとき単位欄を変更
function getTanni(index) {
	//indexは食材入力欄の番号
	if (syokuzaikanalist.indexOf(document.getElementById('syokuzai' + index).value) == -1) {
		//入力された食材がリストになかったら単位欄を空にする
		document.getElementById('tanni' + index).innerHTML = '';
	} else {
		//入力された食材がリストにあったら該当する単位を単位欄に表示する
		document.getElementById('tanni' + index).innerHTML = tannilist[syokuzaikanalist.indexOf(document.getElementById('syokuzai' + index).value)];
	}
}
//食材指定テキストボックスの増減
var ItemField = {
    currentNumber : 1,
    itemTemplate : '食材__count__(ひらがな):'
        + '<input id="syokuzai__count__" type="text" name="syokuzaikana__count__" list="syokuzaikanalist" placeholder="プルダウンメニュー" autocomplete="off" size=30 required  onChange="getTanni(__count__)">'
        + '&emsp;分量:<input type="text" id="bunryou__count__" name="bunryou__count__" size=10 required>'
        + '&emsp;<span id="tanni__count__"></span>',
    add : function () {
        this.currentNumber++;

        var field = document.getElementById('syokuzaifield' + this.currentNumber);
        var newItem = this.itemTemplate.replace(/__count__/mg, this.currentNumber); //mは複数行の入力文字列を複数行として扱う（^及び$が各行の先頭末尾にマッチする） gはグローバルサーチ。文字列全体に対してマッチングするか（無指定の場合は1度マッチングした時点で処理を終了）
        field.innerHTML = newItem;

        var nextNumber = this.currentNumber + 1;
        var new_area = document.createElement("div");
        new_area.setAttribute("id", "syokuzaifield" + nextNumber);
        field.appendChild(new_area);
    },
    remove : function () {
        if ( this.currentNumber == 1 ) { return; }

        var field = document.getElementById('syokuzaifield' + this.currentNumber);
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
  for (var i = 0; i < files.length; i++) {
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
//必須項目の入力チェック すべて正常ならexistCheck()を実行する
function completeCheck() {
	var isComp = true;
	if (document.getElementById('ryourimei').value == '') isComp = false;
	if (document.getElementById('ryourikana').value == '') isComp = false;
	if (document.getElementById('tukurikata').value == '') isComp = false;
	if (document.getElementById('syoukai').value == '') isComp = false;
	for (var i = 1; i <= ItemField.currentNumber; i++) {
		if (document.getElementById('syokuzai' + i).value == '') isComp = false;
		if (document.getElementById('bunryou' + i).value == '') isComp = false;
	}
	if (!isComp) {
		alert('入力されていない欄があります');
		return;
	}
	//ふりがなが平仮名、全角スペース、長音のみかチェックする
	var patternKana = /^[ぁ-んー　]*$/;
	if (!patternKana.test(document.getElementById('ryourikana').value)) {
		alert('ふりがなが平仮名ではありません');
		return;
	}
	//分量がdecimal(5,2)で表されるかどうかを判定する
	var patternInt = /^(0|([1-9]([0-9]{0,2})))$/; //0～999の整数
	var patternDecimal = /^(0|([1-9]([0-9]{0,2})))\.([0-9]{1,2})$/; //0.00～999.99の小数 小数部分は1～2桁
	for (var i = 1; i <= ItemField.currentNumber; i++) {
		if (!patternInt.test(document.getElementById('bunryou' + i).value) && !patternDecimal.test(document.getElementById('bunryou' + i).value)) {
			alert('分量が不正な値です');
			return;
		}
	}
	existCheck();
}
//入力された食材の正常性チェック すべて正常ならdupCheck()を実行する
function existCheck() {
	for (var i = 1; i <= ItemField.currentNumber; i++) {
		if (syokuzaikanalist.indexOf(document.getElementById('syokuzai' + i).value) == -1) {
			alert('入力された食材がデータベースに存在しません');
			return;
		}
	}
	dupCheck();
}
//入力された食材の重複チェック 重複がなければsubmitする
function dupCheck() {
	var isDup = false;
	var inputList = [];
	for (var i = 1; i <= ItemField.currentNumber; i++) {
		if (inputList.indexOf(document.getElementById('syokuzai' + i).value) == -1) {
			inputList.push(document.getElementById('syokuzai' + i).value);
		} else {
			isDup = true;
			break;
		}
	}
	if (isDup) {
		alert('入力された食材に重複があります');
		return;
	} else document.recipeRegisterForm.submit();
}
</script>

<%
if (Objects.equals(request.getAttribute("register_success"), "success")) {
%>
<script>window.confirm("献立を1件登録しました。");</script>
<%
}
%>
<jsp:include page="footer.jsp" /><!-- フッター部分 -->
</body>
</html>