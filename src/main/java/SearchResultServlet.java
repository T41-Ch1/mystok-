package pac1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SearchResultServlet
 */
@WebServlet("/SearchResultServlet")
public class SearchResultServlet extends HttpServlet {

	//static final String URL="jdbc:mysql://localhost/j2a1_gradedb?useSSL=false&serverTimezone=JST";

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		String[] inputData;
		String searchMode;
		String sql = "";
		//###################################################################################
		String[] recipeBunryouRecord = new String[3];
		ArrayList<Integer> recipeID = new ArrayList<Integer>();
		ArrayList<String> recipeTitle = new ArrayList<String>();
		//###################################################################################
		//ArrayList<String> recipeText = new ArrayList<String>();
		//###################################################################################
		ArrayList<ArrayList<String[]>> recipeBunryouList = new ArrayList<>();

		int pageNum;
		if (Objects.equals(request.getParameter("pageNum"), null)) {
			pageNum = 1;
		} else {
			pageNum = Integer.parseInt(request.getParameter("pageNum"));
		}
		final int DATA_PER_PAGE = 10;
		int recipeNum = 0;
		String input = request.getParameter("input"); //入力されたデータを格納
		input = input.replace("　", " "); //全角スペースを半角スペースに置換
		while (input.contains("  ")) input = input.replace("  ", " "); //スペースが連続していたら1つに圧縮
		while (input.length() > 0 && input.charAt(0) == ' ') input = input.substring(1); //スペースから始まっていたら削る
		if (input.length() > 0){
			inputData = input.split("\\s"); //inputが1文字以上なら半角スペースで分割
		} else {
			inputData = new String[0]; //inputが0文字ならinputDataは要素数0 splitで生成すると要素数が1になる
		}

		if (Objects.equals(request.getParameter("searchMode"), null)) {
			searchMode = "syokuzai";
		} else {
			searchMode = request.getParameter("searchMode");
		}

		if (inputData.length == 0) {
			//トップページに遷移する処理 今はテストページに遷移している 後で修正
			RequestDispatcher rd_top = request.getRequestDispatcher("top.jsp");
			rd_top.forward(request, response);
			return;
		} else if (searchMode.equals("ryouri")) {
			//料理名テーブルから検索
			sql = "select RyouriID from RyourimeiTB where RyouriKana in ('" + inputData[0];
			for (int i = 1; i < inputData.length; i++) {
				sql += "', '" + inputData[i];
			}
			sql += "') limit " + DATA_PER_PAGE + " offset " + DATA_PER_PAGE * (pageNum - 1);
		} else {
			//食材名テーブルから検索
			int dataNum = 1;
			sql = "select RyouriID from BunryouTB where RyouriID in (select RyouriID from BunryouTB where SyokuzaiID in (select SyokuzaiID from SyokuzaiTB where SyokuzaiKana in ('" + inputData[0];
			for (int i = 1; i < inputData.length; i++) {
				//すでに追加されたデータではないものを追加する
				boolean dataExists = false;
				for (int j = 0; j < i; j++) {
					if (inputData[i].equals(inputData[j])) {
						dataExists = true;
					}
				}
				if (!dataExists) {
					dataNum++;
					sql += "', '" + inputData[i];
				}
			}
			sql += "')) group by RyouriID having count(RyouriID) = " + dataNum + ") and SyokuzaiID = (select SyokuzaiID from SyokuzaiTB where SyokuzaiKana = '" + inputData[0];
			sql += "') order by Bunryou desc limit " + DATA_PER_PAGE + " offset " + DATA_PER_PAGE * (pageNum - 1);
		}
		System.out.println(sql);

		//表示レシピ検索SQLの実行
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
			System.out.println("connection success");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				recipeID.add(rs.getInt("RyouriID"));
			}

		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if (rs!=null) {
				try {
					rs.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (stmt != null) {
				try {
					stmt.close();
				}catch (SQLException sqlEX) {
				}
			}

			if (conn != null) {

				try {
					conn.close();
				}catch (SQLException sqlEX) {
				}
			}
		}
		System.out.println("レシピID:" + Arrays.toString(recipeID.toArray()));

		if (recipeID.size() > 0) {
			//レシピ件数検索SQL
			if (searchMode.equals("ryouri")) {
				//料理名テーブルから検索
				sql = "select count(RyouriID) from RyourimeiTB where RyouriKana in ('" + inputData[0];
				for (int i = 1; i < inputData.length; i++) {
					sql += "', '" + inputData[i];
				}
				sql += "')";
			} else {
				//食材名テーブルから検索
				int dataNum = 1;
				sql = "select count(RyouriID) from RyourimeiTB where RyouriID in (select RyouriID from BunryouTB where RyouriID in (select RyouriID from BunryouTB where SyokuzaiID in (select SyokuzaiID from SyokuzaiTB where SyokuzaiKana in ('" + inputData[0];
				for (int i = 1; i < inputData.length; i++) {
					//すでに追加されたデータではないものを追加する
					boolean dataExists = false;
					for (int j = 0; j < i; j++) {
						if (inputData[i].equals(inputData[j])) {
							dataExists = true;
						}
					}
					if (!dataExists) {
						dataNum++;
						sql += "', '" + inputData[i];
					}
				}
				sql += "')) group by RyouriID having count(RyouriID) = " + dataNum + ") and SyokuzaiID = (select SyokuzaiID from SyokuzaiTB where SyokuzaiKana = '" + inputData[0] + "') order by Bunryou desc);";
			}
			System.out.println(sql);

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
				System.out.println("connection success");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					recipeNum = rs.getInt("count(RyouriID)");
				}

			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (rs!=null) {
					try {
						rs.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (stmt != null) {
					try {
						stmt.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (conn != null) {

					try {
						conn.close();
					}catch (SQLException sqlEX) {
					}
				}
			}
			System.out.println("レシピ件数:" + recipeNum);

			//レシピ概要検索SQL(料理名)
			sql = "select Ryourimei from RyourimeiTB where RyouriID in ('" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "') order by field(RyouriID, '" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "')";
			System.out.println(sql);

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
				System.out.println("connection success");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					recipeTitle.add(rs.getString("Ryourimei"));
				}

			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (rs!=null) {
					try {
						rs.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (stmt != null) {
					try {
						stmt.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (conn != null) {

					try {
						conn.close();
					}catch (SQLException sqlEX) {
					}
				}
			}
			System.out.println("レシピ名:" + Arrays.toString(recipeTitle.toArray()));
			//###################################################################################
			/*
			//レシピ概要検索SQL(作り方)
			sql = "select Tukurikata from TukurikataTB where RyouriID in ('" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "')";
			System.out.println(sql);

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
				System.out.println("connection success");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					recipeText.add(rs.getString("Tukurikata"));
				}

			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (rs!=null) {
					try {
						rs.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (stmt != null) {
					try {
						stmt.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (conn != null) {

					try {
						conn.close();
					}catch (SQLException sqlEX) {
					}
				}
			}
			//###################################################################################
			*/
			//###################################################################################start
			sql = "select BunryouTB.RyouriID, SyokuzaiTB.Syokuzaimei, BunryouTB.Bunryou, SyokuzaiTB.Tanni from BunryouTB inner join SyokuzaiTB on BunryouTB.SyokuzaiID = SyokuzaiTB.SyokuzaiID where BunryouTB.RyouriID in ('" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "') order by field(BunryouTB.RyouriID, '" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "'), BunryouTB.SyokuzaiID;";
			System.out.println(sql);

			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
			//[[じゃいも,1,個],[にんじん,2,本][牛肉,100,g]]のような感じのArrayList recipe_bunryou1を作成する
				ArrayList<String[]> tempList = new ArrayList<String[]>();
				boolean flag = false;
				String pos = "";
				while (rs.next()) {
					recipeBunryouRecord[0] = rs.getString("syokuzaitb.Syokuzaimei");
					recipeBunryouRecord[1] = rs.getString("bunryoutb.Bunryou");
					recipeBunryouRecord[2] = rs.getString("syokuzaitb.Tanni");
					if (flag) {
						if (!pos.equals(rs.getString("BunryouTB.RyouriID"))) {
							recipeBunryouList.add(tempList);
							tempList = new ArrayList<String[]>();
						}
					} else {
						tempList = new ArrayList<String[]>();
					}
					tempList.add(recipeBunryouRecord);
					pos = rs.getString("BunryouTB.RyouriID");
					recipeBunryouRecord = new String[3];
					flag = true;
				}
				recipeBunryouList.add(tempList);
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				if (rs!=null) {
					try {
						rs.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (stmt != null) {
					try {
						stmt.close();
					}catch (SQLException sqlEX) {
					}
				}

				if (conn != null) {
					try {
						conn.close();
					}catch (SQLException sqlEX) {
					}
				}
			}
			System.out.println("レシピ概要:" + Arrays.toString(recipeBunryouList.toArray()));
			//###################################################################################end
			//###################################################################################
			/*
			for (int i = 0; i < recipeText.size(); i++) {
				System.out.println(recipeText.get(i));
			}
			//###################################################################################
			*/
		}

		request.setAttribute("pageNum", pageNum);
		request.setAttribute("searchMode", searchMode);
		request.setAttribute("inputData", inputData);
		request.setAttribute("recipeID", recipeID);
		request.setAttribute("recipeNum", recipeNum);
		request.setAttribute("recipeTitle", recipeTitle);
		//###################################################################################
		//request.setAttribute("recipeText", recipeText);
		request.setAttribute("recipeBunryouList",recipeBunryouList);
		RequestDispatcher rd_result = request.getRequestDispatcher("searchResult.jsp");
		rd_result.forward(request, response);
	}

}
//###################################################################################
