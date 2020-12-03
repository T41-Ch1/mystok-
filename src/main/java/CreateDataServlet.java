package mystok;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CreateDataServlet
 */
@WebServlet("/CreateDataServlet")
public class CreateDataServlet extends HttpServlet {







	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {





		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\"/>");
		out.println("<title>SQL文追加</title>");
		out.println("</head>");
		out.println("<body>");

		for (int i=4;i<=36;i++){
			out.println("insert into RyouriTB values ("+ i +",'かれー','カレー" + i +"','test"+ i +"-1/test"+ i +"-2/test"+ i +"-3','おいしいカレー" + i + "');");
			out.println("<br>");
		}
		for (int i=4;i<=36;i++){

			out.println("insert into BunryouTB values ("+ i +",1," + (int)(Math.random()*10+1) + ");");
			out.println("<br>");
			out.println("insert into BunryouTB values ("+ i +",2,"+ (int)(Math.random()*10+1) + ");");
			out.println("<br>");
			out.println("insert into BunryouTB values ("+ i +",3," + (int)(Math.random()*10+1) + ");");;
			out.println("<br>");
		}

		out.println("</body>");
		out.println("</html>");










	}

}
