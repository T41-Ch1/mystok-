package pac1;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

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
		String passHashed = "";
		String salt = "";
		String sql1 = "";
		String sql2 = "";
		String role = "";

		userName = request.getParameter("username"); //入力されたデータを格納
		password = request.getParameter("password"); //入力されたデータを格納

	    HttpSession session = request.getSession(false);
	    if (session == null){
	      /* セッションが開始されずにここへ来た場合は無条件でエラー表示 */
	    	System.out.println("LoginServletへの不正アクセス");
	    	RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
			rd.forward(request, response);
			return;
	    }

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//ソルト取得SQLの組み立て
		sql1 =  "select Salt from UserTB where UserName = ?";

		//ソルト取得SQLの実行
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql1)) {
			prestmt.setString(1, userName);
			System.out.println("ソルト取得SQL:" + prestmt.toString());
			try (ResultSet rs = prestmt.executeQuery()) {
				while (rs.next()) {
					salt = rs.getString("Salt");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ユーザ名がなかったらエラー画面に遷移する
		if (Objects.equals(salt, null)) {
			RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
			rd.forward(request, response);
			return;
		}
		System.out.println("ソルト取得SQL完了");

		//passHashedの生成
		password += salt;
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(password.getBytes("utf8"));
		    passHashed = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    e.printStackTrace();
		}

		//ログイン判定SQLの組み立て
		sql2 = "select UserRoleTB.Role from UserTB inner join UserRoleTB on UserTB.UserName = UserRoleTB.UserName "
				+ "where UserTB.UserName = ? and UserTB.Password = ?";

		//ログイン判定SQLの実行
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql2)) {
			prestmt.setString(1, userName);
			System.out.println("ログイン判定SQL:" + prestmt.toString());
			prestmt.setString(2, passHashed);
			try (ResultSet rs = prestmt.executeQuery()) {
				while (rs.next()) {
					//roleは1種類の前提
					role = rs.getString("UserRoleTB.Role");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ログイン判定SQL完了");

		if (!role.equals("")) {
			System.out.println("結果:ログイン成功");
			session = request.getSession();
			session.setAttribute("auth.user", userName);
			session.setAttribute("auth.role", role);
			//元々のアクセス先URIを保存する
			String target = (String)session.getAttribute("targetURI");
			//例えば"/mystok/LoginServlet"を"LoginServlet"に削る
			target = target.substring(target.lastIndexOf("/") + 1);
			RequestDispatcher rd = request.getRequestDispatcher(target);
			rd.forward(request, response);
		} else {
			System.out.println("結果:ログイン失敗");
			RequestDispatcher rd = request.getRequestDispatcher("error.jsp");
			rd.forward(request, response);
		}
	}

}
