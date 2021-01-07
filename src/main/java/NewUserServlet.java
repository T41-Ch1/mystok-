package mystok;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class NewUserServlet
 */
@WebServlet("/NewUserServlet")
public class NewUserServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String sql1 = "insert into UserTB values (?, ?, ?)";
		String sql2 = "insert into UserRoleTB values (?,'user')";

		//64bitのソルトを生成しパスワードと結合
		String salt = "";
		for (int j = 0; j < 16; j++) {
			salt += String.format("%x", (int)(Math.random() * 16));
		}
		password += salt;

		//パスワードをSHA-256でハッシュ化
		String passHashed = "";
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(password.getBytes("utf8"));
		    passHashed = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		System.out.println("passHashed:" + passHashed);

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql1)) {
			prestmt.setString(1,name);
			prestmt.setString(2,passHashed);
			prestmt.setString(3,salt);
			System.out.println("会員登録SQL:" + prestmt.toString());
			prestmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("会員登録SQL完了");

		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql2)) {
			prestmt.setString(1,name);
			System.out.println("ロール登録SQL:" + prestmt.toString());
			prestmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("ロール登録SQL完了");

		RequestDispatcher rd_result = request.getRequestDispatcher("top.jsp");
		rd_result.forward(request, response);


	}

}
