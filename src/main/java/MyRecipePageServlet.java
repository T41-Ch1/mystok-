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
 * Servlet implementation class MyRecipePageServlet
 */
@WebServlet("/MyRecipePageServlet")
public class MyRecipePageServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {
		int pageNum = 1; //あとで修正する
		final int DATA_PER_PAGE = 25;
		final String JSP_PATH = "myrecipe.jsp";
		ArrayList<Integer> ryouriID = new ArrayList<>();
		ArrayList<String> ryourimei = new ArrayList<>();
		String userName = request.getRemoteUser();
		String sql = "select RyouriID, Ryourimei from RyouriTB where UserName = ? limit " + DATA_PER_PAGE;
		sql += " offset " + DATA_PER_PAGE * (pageNum - 1);

		//認証チェック
		if (!Util.checkAuth(request, response)) return;

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
			System.out.println("マイレシピ検索SQL:" + prestmt.toString());
			try (ResultSet rs = prestmt.executeQuery()) {


				while (rs.next()) {
					ryouriID.add(rs.getInt("RyouriID"));
					ryourimei.add(rs.getString("Ryourimei"));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("マイレシピ検索SQL完了");
		System.out.println("マイレシピID:" + Arrays.toString(ryouriID.toArray()));

		ArrayList<Boolean> favoList = new ArrayList<>();

		favoList = Util.favoInfo(ryouriID,userName);


		request.setAttribute("favoList", favoList);
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("ryouriID", ryouriID);
		request.setAttribute("ryourimei", ryourimei);
		RequestDispatcher rd_result = request.getRequestDispatcher(JSP_PATH);
		rd_result.forward(request, response);
	}

}
