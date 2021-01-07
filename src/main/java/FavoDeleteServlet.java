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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class FavoDeleteServlet
 */
@WebServlet("/FavoDeleteServlet")
public class FavoDeleteServlet extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userName = request.getRemoteUser(); //ユーザ名 ログイン中でなければnullが格納される
		int recipeID = Integer.parseInt(request.getParameter("recipeID"));
		String sql = "delete from FavoTB where UserName = ? and RyouriID = ?";
		HttpSession session = request.getSession();

		//認証チェック
		//if (!Util.checkAuth(request, response)) return; とするとログイン中ではない時ログインページに飛ぶ
		if (session == null || request.getRemoteUser() == null) {
			session.setAttribute("favoDelete", false);
			System.out.println("ログアウト状態でFavoされました");
			return;
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql)) {

			prestmt.setString(1, userName);
			prestmt.setInt(2, recipeID);
			System.out.println("Favo削除SQL:" + prestmt.toString());
			prestmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Favo削除SQL完了");
		session.setAttribute("favoDelete", true);
	}

}
