package mystok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mystok.func.Util;

/**
 * Servlet implementation class FavoPageServlet
 */
@WebServlet("/FavoPageServlet")
public class FavoPageServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getRemoteUser();
		ArrayList<Integer> recipeID = new ArrayList<>();
		ArrayList<String> ryourimei = new ArrayList<>();
		String JSP_PATH = "okiniiri.jsp";
		String sql = "";

		//認証チェック
		if (!Util.checkAuth(request, response)) return;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Favoレシピ検索SQLの組み立て
		sql = "select RyouriTB.RyouriID, RyouriTB.Ryourimei from FavoTB inner join RyouriTB on "
			  +"FavoTB.RyouriID = RyouriTB.RyouriID where FavoTB.UserName = ?";
		//Favoレシピ検索SQLの実行
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/mystok?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql)) {

			prestmt.setString(1, userName);
			System.out.println("Favoレシピ検索SQL:" + prestmt.toString());
			try (ResultSet rs = prestmt.executeQuery()) {
				while (rs.next()) {
					recipeID.add(rs.getInt("RyouriID"));
					ryourimei.add(rs.getString("Ryourimei"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Favoレシピ検索SQL完了");
		System.out.println("料理ID:" + Arrays.toString(recipeID.toArray()));
		System.out.println("料理名:" + Arrays.toString(ryourimei.toArray()));

		request.setAttribute("recipeID",recipeID);
		request.setAttribute("ryourimei",ryourimei);
		RequestDispatcher rd_result = request.getRequestDispatcher(JSP_PATH);
		rd_result.forward(request, response);

	}

}
