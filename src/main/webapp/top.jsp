<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>

<!--head開始-->
  <head>
    <meta charset="utf-8" >
    <meta http-equiv="X-UA-Compatible" content="IE=11">
    <title>レシピ検索サイト　レシピコンシェル|TOP</title>
    <link rel="stylesheet" href="CSS/TOPStyle.css" type="text/css">
  </head>
<!--head終了-->

<!--body開始-->
  <body>

<jsp:include page="header.jsp" /><!-- ヘッダー部分 -->


<!--aboxが開始-->
  <div class="abox"><!--三要素構成-"abox"div２-->
      <div class ="abox-image"></div><!--野菜の背景のdiv３-->
        <h1>あなたの消費したい食材を教えてください</h1>
        <h4>特に消費したい食材からレシピを検索できます。<br>入力した食材の消費が多い順にレシピを表示します！</h4>
  <!--ラジオボタン開始-->
        <form id ="mkensaku" action="SearchResultServlet" method="get">
          <div class = "radio-font"><!--ラジオボタンのdiv４-->
            <ul class ="radiolist"><!--ラジオボタンリストのul-->
                <li>
                  <input type="radio" id = "f-option" name="searchMode" value="syokuzai" checked>
                  <label for="f-option">
                    食材名検索
                  </label>
                  <div class ="check"></div><!--ラジオボタンチェックする円のdiv５-->
                </li>
                <li>
                  <input type="radio" id="s-option" name="searchMode" value="ryouri">
                  <label for="s-option">料理名検索</label>
                  <div class ="check"></div><!--ラジオボタンチェックする円のdiv６-->
                </li>
            </ul>
          </div>

  <!--ラジオボタン終了-->

  <!--検索窓開始-->
            <!-- \u3041-\u3096は平仮名、\u3000は全角スペース、\u30fcは長音 これらの文字の組み合わせのみ許可する 正規表現で書いたのがpatternの所 -->
            <input id="mado" type="text" name="input" size=50 pattern="[\u3041-\u3096|\u3000|\u30fc]*" maxlength=50
             placeholder=" 例）じゃがいも　かれー等　【ひらがな入力のみ】" title="ひらがなで入力して下さい" required>
            <input id = "mbutton" type="submit" value="レシピ検索">
            <script>
             let form = document.getElementById('mbutton');
             form.addEventListener('submit', () => { form.disabled = true; }, false);
            </script>
          </form>
  <!--検索窓終了-->

  </div>
<!--aboxが終了-->


<!--aboxとbboxの間-->
 <div class ="about"><!--野菜の背景のdiv７-->
  <h1 class ="how">HOW　TO</h1>
 </div>


 <!--bboxが開始-->
 <div class = "bbox"><!--三要素構成-"bbox"div８-->
  <div class="bboximage"><!--野菜の背景のdiv９-->
    <img src="images/s.setumei.png"alt="サイト説明">
  </div>
 </div>
  <!--bboxが終了-->


  <!--cboxが開始-->
  <div class ="cbox"><!--三要素構成-"cbox"div１０-->
    <div class ="cboxtext"><!--野菜の背景のdiv１１-->
      <h3>レシピ検索サイト<br>  レシピコンシェル</h3>
        <p>フードコンシェルでは特に消費したい食材を指定可能。<br>
           貴方に適したレシピを提案します。</p>
    </div>
      <div class ="cboximage"><!--野菜の背景のdiv１２-->
        <br>
        <img src="images/butler.png"alt="コンシェルジュ">
      </div>
    </div>
<!--cboxが終了-->
<jsp:include page="footer.jsp" /><!-- フッター部分 -->
  </body>
<!--head終了-->
</html>

