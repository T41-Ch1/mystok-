package mystok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		//変数
		String userName;
		String password;
		String sql = "";
		boolean hantei = false;

		userName = request.getParameter("username"); //入力されたデータを格納
		password = request.getParameter("input2"); //入力されたデータを格納

		//ログイン判定SQLの組み立て
		sql = "select count(UserID) from UserTB where UserName = '" + "?" + "' and Password ='" + "?" + "'";
		System.out.println(sql);

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//ログイン判定SQLの実行
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://127.0.0.1:3306/mystok?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql)) {
			prestmt.setString(1, userName);
			prestmt.setString(2, password);
			try (ResultSet rs = prestmt.executeQuery()) {
				ArrayList<Integer> count_userid = new ArrayList<Integer>(); //検索に合致するUserIDの件数を格納する 原則要素数は1で値は0か1
				while (rs.next()) {
					count_userid.add(rs.getInt("count(UserID)"));
				}
				if (count_userid.get(0) == 1) {
					hantei = true;
				} else {
					hantei = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ログイン判定SQL完了");
		System.out.println("結果:" + hantei);

		if (hantei){

			HttpSession session=request.getSession();
			session.setAttribute("userName",userName);
			session.setAttribute("password",password);
			RequestDispatcher rd = request.getRequestDispatcher("logintest.jsp");
			rd.forward(request, response);


		} else {
			RequestDispatcher rd = request.getRequestDispatcher("logintest.jsp");
			rd.forward(request, response);


		}












	}

}
