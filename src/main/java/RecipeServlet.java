package pac1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecipeServlet
 */
@WebServlet("/RecipeServlet")
public class RecipeServlet extends HttpServlet {
	String recipeID = ""; //表示するレシピのID
	String searchMode = ""; //検索窓のラジオボタンに最初からチェックを入れる方
	String input = ""; //検索窓に最初から表示させる文字列
	String recipe_name = ""; //料理名
	String tukurikata = ""; //作り方
	String[] str = new String[3]; //順に食材名、分量、単位が格納される
	final String JSP_PATH = "recipe.jsp"; //遷移先のJSP

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String[]> recipe_bunryou = new ArrayList<>(); //strの情報を順に格納する

		//recipeID, searchMode, inputの準備
		recipeID = request.getParameter("recipeID");//検索結果画面からパラメータrecipeIDをgetして変数recipeIDに代入する
		//recipeID = "2"; レシピページ単体でテストをする場合こっちに切り替える
		System.out.println("レシピID:" + request.getParameter("recipeID"));
		searchMode = request.getParameter("searchMode");//検索窓のラジオボタンに最初からチェックを入れる方を取得する
		input = request.getParameter("input"); //検索窓に最初から表示させる文字列を取得する

		//DBに接続し、recipeIDに該当するレシピ名、作り方、食材を検索する
		//SQLの組み立て
		String sql1 = "select Ryourimei, Tukurikata from RyouriTB where RyouriID = " + recipeID;
		String sql2 = "select SyokuzaiTB.Syokuzaimei, BunryouTB.Bunryou, SyokuzaiTB.Tanni from BunryouTB inner join SyokuzaiTB on BunryouTB.SyokuzaiID = SyokuzaiTB.SyokuzaiID where BunryouTB.RyouriID = " + recipeID;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//レシピ名、作り方を検索
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql1)) {

			while (rs.next()) {
				recipe_name = rs.getString("Ryourimei");
				tukurikata = rs.getString("Tukurikata");
			}
			System.out.println("レシピ詳細検索SQL(レシピ名、作り方):" + sql1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("レシピ詳細検索SQL(レシピ名、作り方)完了");
		System.out.println("レシピ名:" + recipe_name + "作り方:" + tukurikata);

		//食材名、分量、単位を検索
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql2)) {
			//必要な分量のデータが入ったArrayList recipe_bunryou1を作成する
			//レコードstrの例:[[じゃがいも,1,個],[にんじん,2,本],[牛肉,100,g]]
			while (rs.next()) {
				str[0] = rs.getString("syokuzaitb.Syokuzaimei");
				str[1] = rs.getString("bunryoutb.Bunryou");
				str[2] = rs.getString("syokuzaitb.Tanni");
				recipe_bunryou.add(str);
				str = new String[3];
			}
			System.out.println("レシピ詳細検索SQL(食材名、分量、単位):" + sql2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("レシピ詳細検索SQL(食材名、分量、単位)完了");
		for (int i = 0; i < recipe_bunryou.size(); i++) {
			String buff = (i + 1) + "番目の分量:";
			for (int j = 0; j < recipe_bunryou.size(); j++) {
				buff += recipe_bunryou.get(i)[j];
			}
			System.out.println(buff);
		}

		//requestに属性を追加してJSPにフォワードする
		request.setAttribute("searchMode",searchMode);
		request.setAttribute("input",input);
		request.setAttribute("recipe_name",recipe_name);
		request.setAttribute("tukurikata", tukurikata);
		request.setAttribute("recipeID", recipeID);
		request.setAttribute("recipe_bunryou", recipe_bunryou);
		RequestDispatcher rd = request.getRequestDispatcher(JSP_PATH);
		rd.forward(request, response);
	}
}