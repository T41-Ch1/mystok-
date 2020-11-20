package pac1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	//String recipeID = "1";
	String recipeID = "";
	String txt = "";
	String recipe_name = "";
	String tukurikata = "";
	String[] str = new String[3];
	final String JSP_PATH = "recipe.jsp";
	String syokuzai_name = "";
	String bunryou = "";
	String tanni = "";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ArrayList<String[]> recipe_bunryou1 = new ArrayList<>();
		//検索結果画面からパラメータrecipeIDをgetして変数recipeIDに代入する

		recipeID = request.getParameter("recipeID");
		System.out.println(request.getParameter("recipeID"));
		//あとで↓を↑に修正する
		//recipeID = "2"; 修正した レシピページ単体でテストをする場合こっちに切り替える
		txt = request.getParameter("txt");

		//DBに接続し、recipeIDで検索して、レシピ名を取得し、変数recipe_nameに代入する
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		String sql1 = "select Ryourimei from RyourimeiTB where RyouriID = " + recipeID;
		String sql2 = "select Tukurikata from TukurikataTB where RyouriID = " + recipeID;
		String sql3 = "select SyokuzaiTB.Syokuzaimei,BunryouTB.Bunryou,SyokuzaiTB.Tanni from BunryouTB inner join SyokuzaiTB on BunryouTB.SyokuzaiID = SyokuzaiTB.SyokuzaiID where BunryouTB.RyouriID = " + recipeID;


		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql1);

			while (rs.next()) {
				recipe_name = rs.getString("Ryourimei");
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (rs!=null) {
				try {
					rs.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				}catch (SQLException sqlEX) {
				}
			}
		}


		//DBに接続し、recipeIDで検索して、作り方を取得し、変数tukurikataに代入する
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql2);

			while (rs.next()) {
				tukurikata = rs.getString("Tukurikata");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (rs!=null) {
				try {
					rs.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				}catch (SQLException sqlEX) {
				}
			}
		}

		//DBに接続し、recipeIDで検索して、食材名、分量、単位を取得して、ArrayList recipe_bunryou1に代入する

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql3);
		//[[じゃいも,1,個],[にんじん,2,本][牛肉,100,g]]のような感じのArrayList recipe_bunryou1を作成する
			while (rs.next()) {

				str[0] = rs.getString("syokuzaitb.Syokuzaimei");
				str[1] = rs.getString("bunryoutb.Bunryou");
				str[2] = rs.getString("syokuzaitb.Tanni");
				recipe_bunryou1.add(str);
				str = new String[3];
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (rs!=null) {
				try {
					rs.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (conn != null) {
				try {
					conn.close();
				}catch (SQLException sqlEX) {
				}
			}
		}

		//recipe_name = "さんぷるねーむ";
		//tukurikata = "さんぷる作り方";
		//System.out.println(recipe_name);
		//レシピページに必要なパラメータをそれぞれセットして、recipe.jspに転送する
		request.setAttribute("txt",txt);
		request.setAttribute("recipe_name",recipe_name);
		request.setAttribute("tukurikata", tukurikata);
		request.setAttribute("recipeID", recipeID);
		//食材名、分量、単位は配列に格納したままrecipe.jspに転送する
		request.setAttribute("recipe_bunryou", recipe_bunryou1);

		RequestDispatcher rd = request.getRequestDispatcher(JSP_PATH);
		rd.forward(request, response);


		//テスト用コード
		/*
		if (tukurikata.equals("")) {
			System.out.println("tukurikata empty");
		}

		if (recipe_name.equals("")) {
			System.out.println("recipe_name empty");
		}

		System.out.println(tukurikata);
		System.out.println(recipe_name);
		System.out.println(str.length);
		System.out.println(str[0]);
		System.out.println(recipe_bunryou1.get(0)[0]);
		System.out.println(recipe_bunryou1.get(0)[1]);
		System.out.println(recipe_bunryou1.get(0)[2]);
		System.out.println("aaaaaaaaaaaaaaaaaaaaaaaa");
		*/
	}
}

//転送先jspのパスは適当
