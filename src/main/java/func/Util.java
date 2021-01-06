package pac1.func;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Util {
	public static String sanitizing(String str) {
		return str.replaceAll("&", "&amp;")
				.replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;")
				.replaceAll("\"", "&quot;")
				.replaceAll("\'", "&apos;");
	}
	public static boolean favoInfo(int recipeID, String userName) {
		boolean existsData = false;
		String sql = "";
		if (userName == null) {
			return existsData;
		}

		sql = "select * from FavoTB where UserName = ? and RyouriID = ?";
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				PreparedStatement prestmt = conn.prepareStatement(sql)) {
			prestmt.setString(1,userName);
			prestmt.setInt(2,recipeID);
			try (ResultSet rs = prestmt.executeQuery()) {
				while (rs.next()) {
					existsData = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(existsData);
		return existsData;
	}
	public static ArrayList<Boolean> favoInfo(ArrayList<Integer> recipeID, String userName) {
		ArrayList<Boolean> existsData = new ArrayList<>();
		String sql = "";
		if (userName == null) {
			for (int i = 0; i < recipeID.size();i++) {
				existsData.add(false);
			}
			return existsData;
		}

		sql = "select RyouriID from FavoTB where UserName = ? and RyouriID in (?";
		for (int i=1;i<=recipeID.size()-1;i++) {
			sql += ",?";
		}
		sql += ") group by RyouriID order by field(RyouriID,?";
		for (int i=1;i<=recipeID.size()-1;i++) {
			sql += ",?";
		}
		sql += ")";

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
			for (int i = 0; i < recipeID.size(); i++) {
				prestmt.setInt(i + 2, recipeID.get(i));
				prestmt.setInt(i + 2 + recipeID.size(), recipeID.get(i));
			}
			try (ResultSet rs = prestmt.executeQuery()) {
				ArrayList<Integer> result = new ArrayList<>();
				while (rs.next()) {
					result.add(rs.getInt("RyouriID"));
				}
				for (int i = 0; i < recipeID.size(); i++) {
					if (result.indexOf(recipeID.get(i)) == -1) {
						existsData.add(false);
					} else {
						existsData.add(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(Arrays.toString(existsData.toArray()));
		return existsData;
	}
	public static boolean existsUser(String userName) {
		String sql = "";
		sql = "select * from UserTB where UserName = ?";
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
			try (ResultSet rs = prestmt.executeQuery()) {
				while (rs.next()) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean checkAuth(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		//認証チェック
		HttpSession session = request.getSession(false);
		if (session == null) {
			//セッションが切れている場合
			session = request.getSession(true);
			session.setAttribute("targetURI", request.getRequestURI());
			System.out.println("targetURI:" + request.getRequestURI());
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.forward(request, response);
			return false;
		} else if (request.getRemoteUser() == null) {
			//ログインしていない場合
			session.setAttribute("targetURI", request.getRequestURI());
			System.out.println("targetURI:" + request.getRequestURI());
			RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
			rd.forward(request, response);
			return false;
		}
		return true;
	}
}

