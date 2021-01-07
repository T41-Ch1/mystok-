package mystok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mystok.func.Util;

/**
 * Servlet implementation class RecipeDeleteServlet
 */
@WebServlet("/RecipeDeleteServlet")
public class RecipeDeleteServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getRemoteUser();
		int ryouriID = 37;//あとで修正する
		String sql = "delete from RyouriTB where RyouriID = ? and UserName = ?";

		//認証チェック
		if (!Util.checkAuth(request, response)) return;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/mystok?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql)) {
			prestmt.setInt(1,ryouriID);
			prestmt.setString(2,userName);
			System.out.println("レシピ削除SQL:" + prestmt.toString());
			prestmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("レシピ削除SQL完了");
	}

}
