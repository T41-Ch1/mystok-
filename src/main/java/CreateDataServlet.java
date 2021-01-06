package pac1;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;

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

		//UserTBのデータ
		String password = "password";
		String salt;
		String passHashed = "";
		salt = getSalt();
		salt = "a77e286e1f5bfac1";
		passHashed = getSHA256(password, salt);
		out.println("insert into UserTB values ('admin','" + passHashed + "','" + salt + "');");
		out.println("<br>");
		salt = getSalt();
		passHashed = getSHA256(password, salt);
		out.println("insert into UserTB values ('aaa','" + passHashed + "','" + salt + "');");
		out.println("<br>");
		salt = getSalt();
		passHashed = getSHA256(password, salt);
		out.println("insert into UserTB values ('bbb','" + passHashed + "','" + salt + "');");
		out.println("<br>");
		salt = getSalt();
		passHashed = getSHA256(password, salt);
		out.println("insert into UserTB values ('ccc','" + passHashed + "','" + salt + "');");
		out.println("<br>");

		//UserRoleTBのデータ
		out.println("insert into UserRoleTB values ('admin','user');");
		out.println("<br>");
		out.println("insert into UserRoleTB values ('aaa','user');");
		out.println("<br>");
		out.println("insert into UserRoleTB values ('bbb','user');");
		out.println("<br>");
		out.println("insert into UserRoleTB values ('ccc','user');");
		out.println("<br>");

		//SyokuzaiTBのデータ
		out.println("insert into SyokuzaiTB values (1,'じゃがいも','じゃがいも','個');");
		out.println("<br>");
		out.println("insert into SyokuzaiTB values (2,'にんじん','にんじん','本');");
		out.println("<br>");
		out.println("insert into SyokuzaiTB values (3,'ぎゅうにく','牛肉','g');");
		out.println("<br>");
		out.println("insert into SyokuzaiTB values (4,'たまねぎ','玉ねぎ','個');");
		out.println("<br>");

		//RyouriTBのデータ
		out.println("insert into RyouriTB values (1,'かれー','カレー','test1-1/test1-2/test1-3','おいしいカレー','admin');");
		out.println("<br>");
		out.println("insert into RyouriTB values (2,'にくじゃが','肉じゃが','test2-1/test2-2/test2-3','おいしい肉じゃが','admin');");
		out.println("<br>");
		out.println("insert into RyouriTB values (3,'ぎゅうどん','牛丼','test3-1/test3-2/test3-3','おいしい牛丼','admin');");
		out.println("<br>");
		for (int i=4;i<=36;i++){
			out.println("insert into RyouriTB values ("+ i +",'かれー','カレー" + i +"','test"+ i +"-1/test"+ i +"-2/test"+ i +"-3','おいしいカレー" + i + "','admin');");
			out.println("<br>");
		}
		out.println("insert into RyouriTB values (37,'かれー','aaaのカレー37','test37-1/test37-2/test37-3','aaaのおいしいカレー37','aaa'); ");
		out.println("<br>");
		out.println("insert into RyouriTB values (38,'かれー','aaaのカレー38','test38-1/test38-2/test38-3','aaaのおいしいカレー38','aaa');");
		out.println("<br>");

		//BunryouTBのデータ
		out.println("insert into BunryouTB values (1,1,3);");
		out.println("<br>");
		out.println("insert into BunryouTB values (1,2,4);");
		out.println("<br>");
		out.println("insert into BunryouTB values (1,3,3);");
		out.println("<br>");
		out.println("insert into BunryouTB values (2,1,3);");
		out.println("<br>");
		out.println("insert into BunryouTB values (2,2,4);");
		out.println("<br>");
		out.println("insert into BunryouTB values (2,3,3);");
		out.println("<br>");
		out.println("insert into BunryouTB values (3,1,3);");
		out.println("<br>");
		out.println("insert into BunryouTB values (3,4,4);");
		out.println("<br>");
		for (int i=4;i<=36;i++){
			out.println("insert into BunryouTB values ("+ i +",1," + (int)(Math.random()*10+1) + ");");
			out.println("<br>");
			out.println("insert into BunryouTB values ("+ i +",2,"+ (int)(Math.random()*10+1) + ");");
			out.println("<br>");
			out.println("insert into BunryouTB values ("+ i +",3," + (int)(Math.random()*10+1) + ");");;
			out.println("<br>");
		}

		//FavoTBのデータ
		out.println("insert into FavoTB values ('aaa',13,'2019-10-04 15:25:07');");
		out.println("<br>");
		out.println("insert into FavoTB values ('bbb',3,'2019-10-04 15:26:07');");
		out.println("<br>");
		out.println("insert into FavoTB values ('ccc',3,'2019-10-04 15:27:07');");
		out.println("<br>");
		out.println("insert into FavoTB values ('aaa',3,'2019-10-04 15:28:07');");
		out.println("<br>");

		out.println("</body>");
		out.println("</html>");
	}

	protected String getSalt() {
		String salt = "";
		for (int j = 0; j < 16; j++) {
			salt += String.format("%x", (int)(Math.random() * 16));
		}
		return salt;
	}

	protected String getSHA256(String password, String salt) {
		password += salt;
		String passHashed = "";
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(password.getBytes("utf8"));
		    passHashed = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return passHashed;
	}

}
