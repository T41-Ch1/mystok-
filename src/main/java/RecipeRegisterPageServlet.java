package pac1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RecipeRegisterPageServlet
 */
@WebServlet("/RecipeRegisterPageServlet")
public class RecipeRegisterPageServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ArrayList<String> syokuzaikanalist = new ArrayList<>();
		ArrayList<String> tannilist = new ArrayList<>();
		final String JSP_PATH = "recipeRegister.jsp";

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//食材名取得SQL
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/j2a1b?serverTimezone=JST","root","password");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select SyokuzaiKana, Tanni from SyokuzaiTB")) {
			while (rs.next()) {
				syokuzaikanalist.add(rs.getString("SyokuzaiKana")); //条件を満たすSyokuzaiKanaを格納
				tannilist.add(rs.getString("Tanni")); //条件を満たすTanniを格納
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("食材名取得SQL完了");
		System.out.println("食材件数:" + syokuzaikanalist.size());

		//requestに属性を追加してJSPにフォワードする
		request.setAttribute("syokuzaikanalist", syokuzaikanalist);
		request.setAttribute("tannilist", tannilist);
		if (Objects.equals(request.getAttribute("register_success"), "success")) request.setAttribute("register_success", "success");
		RequestDispatcher rd_result = request.getRequestDispatcher(JSP_PATH);
		rd_result.forward(request, response);
	}

	@Override
	//PostメソッドでRecipeRegisterServletから遷移されるためdoGetを実行するようにする
	public void doPost (HttpServletRequest request, HttpServletResponse response)	throws IOException, ServletException {
		this.doGet(request, response);
	}

}
