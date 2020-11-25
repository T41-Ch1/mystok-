package mystok;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response)
			throws ServletException, IOException {

		String[] inputData; //検索窓に入力された文字列を全角スペースで分割して順に格納する配列
		String searchMode; //検索モード 料理名検索ならryouri 食材名検索ならsyokuzaiが格納される
		final String JSP_PATH0 = "top.jsp"; //メインページのJSP
		final String JSP_PATH1 = "searchResult.jsp"; //検索結果ページのJSP
		String sql = ""; //DBに送信するためのSQL文を格納する
		String[] recipeBunryouRecord = new String[3]; //レコードに登録する食材名、分量、単位を格納する配列
		ArrayList<Integer> recipeID = new ArrayList<Integer>(); //検索結果に対応するレシピのIDを格納するリスト
		ArrayList<String> recipeTitle = new ArrayList<String>(); //検索結果に対応するレシピの名前を格納するリスト
		ArrayList<String> recipeIntro = new ArrayList<String>(); //検索結果に対応するレシピの紹介文を格納するリスト
		ArrayList<ArrayList<String[]>> recipeBunryouList = new ArrayList<>(); //検索結果に対応するレシピの食材名、分量、単位を格納するリスト

		int pageNum; //検索結果ページのページ番号
		if (Objects.equals(request.getParameter("pageNum"), null)) {
			pageNum = 1; //pageNumのパラメータがnullなら1ページ目を表示
		} else {
			pageNum = Integer.parseInt(request.getParameter("pageNum")); //そうでないなら送信されたパラメータpageNumを格納
		}
		final int DATA_PER_PAGE = 10; //1ページごとに表示する最大件数
		int recipeNum = 0; //表示するデータの件数

		//inputDataとsearchModeの準備
		String input; //入力されたデータを格納
		if (Objects.equals(request.getParameter("input"), null)) {
			input = ""; //pageNumのパラメータがnullなら1ページ目を表示
		} else {
			input = request.getParameter("input"); //そうでないなら送信されたパラメータpageNumを格納
		}
		while (input.contains("　　")) input = input.replace("　　", "　"); //スペースが連続していたら1つに圧縮
		if (input.charAt(0) == '　') input = input.substring(1); //スペースから始まっていたら削る
		if (input.length() > 0){
			inputData = input.split("　"); //inputが1文字以上なら半角スペースで分割
		} else {
			inputData = new String[0]; //inputが0文字ならinputDataは要素数0 splitで生成すると要素数が1になる
		}
		if (Objects.equals(request.getParameter("searchMode"), null)) {
			 //searchModeのパラメータがnullなら食材名検索
			 //検索結果ページの一番消費したい食材を選びなおす機能から実行されたときはここ
			searchMode = "syokuzai";
		} else {
			searchMode = request.getParameter("searchMode"); //そうでないなら送信されたパラメータsearchModeを格納
		}

		//入力文字列がない場合の処理
		if (inputData.length == 0) {
			//何も入力されなかったらトップページに遷移する処理 URLを削って入力されたときに呼ばれうる
			RequestDispatcher rd_top = request.getRequestDispatcher(JSP_PATH0);
			rd_top.forward(request, response);
			return;
		} else if (searchMode.equals("ryouri")) {
			//表示レシピ検索SQL(料理名検索)の組み立て
			sql = "select RyouriID from RyouriTB where RyouriKana in ('" + inputData[0];
			for (int i = 1; i < inputData.length; i++) {
				sql += "', '" + inputData[i];
			}
			sql += "') limit " + DATA_PER_PAGE + " offset " + DATA_PER_PAGE * (pageNum - 1);
		} else {
			//表示レシピ検索SQL(食材名検索)の組み立て
			int dataNum = 1; //入力された食材の個数を格納する(重複するものを除く)
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

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}

		//表示レシピ検索SQLの実行
		try (
				Connection conn = DriverManager.getConnection(
					"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
			while (rs.next()) {
				recipeID.add(rs.getInt("RyouriID")); //条件を満たすRyouriIDを格納
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("表示レシピ検索SQL完了");
		System.out.println("レシピID:" + Arrays.toString(recipeID.toArray()));

		if (recipeID.size() > 0) {
			//レシピ件数検索SQL
			if (searchMode.equals("ryouri")) {
				//レシピ件数検索SQL(料理名検索)の組み立て
				sql = "select count(RyouriID) from RyouriTB where RyouriKana in ('" + inputData[0];
				for (int i = 1; i < inputData.length; i++) {
					sql += "', '" + inputData[i];
				}
				sql += "')";
			} else {
				//レシピ件数検索SQL(食材名検索)の組み立て
				int dataNum = 1; //入力された食材の個数を格納する(重複するものを除く)
				sql = "select count(RyouriID) from RyouriTB where RyouriID in (select RyouriID from BunryouTB where RyouriID in (select RyouriID from BunryouTB where SyokuzaiID in (select SyokuzaiID from SyokuzaiTB where SyokuzaiKana in ('" + inputData[0];
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

			try (
					Connection conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
				while (rs.next()) {
					recipeNum = rs.getInt("count(RyouriID)"); //条件を満たすRyouriIDの件数を格納
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("レシピ件数検索SQL完了");
			System.out.println("レシピ件数:" + recipeNum);

			//レシピ概要検索SQL(料理名、紹介文)の組み立て
			sql = "select Ryourimei, Syoukai from RyouriTB where RyouriID in ('" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "') order by field(RyouriID, '" + recipeID.get(0);
			for (int i = 1; i < recipeID.size(); i++) {
				sql += "', '" + recipeID.get(i);
			}
			sql += "')";
			System.out.println(sql);

			try (
					Connection conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
				while (rs.next()) {
					recipeTitle.add(rs.getString("Ryourimei")); //条件を満たすレシピ名を格納
					recipeIntro.add(rs.getString("Syoukai")); //条件を満たすレシピ紹介文を格納
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("レシピ概要検索SQL(料理名、紹介文)完了");
			System.out.println("レシピ名:" + Arrays.toString(recipeTitle.toArray()));
			System.out.println("レシピ紹介文:" + Arrays.toString(recipeIntro.toArray()));

			//レシピ概要検索SQL(分量)の組み立て
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

			try (
					Connection conn = DriverManager.getConnection(
						"jdbc:mysql://mystok-db:3306/mystok?serverTimezone=JST","root","password");
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql)) {
				//レシピごとに必要な分量のデータが入ったArrayList<String[]> tempListを作成する
				//tempListの例:[[じゃがいも,1,個],[にんじん,2,本][牛肉,100,g]]
				ArrayList<String[]> tempList = new ArrayList<String[]>();
				boolean flag = false; //次のwhileの1回目のループの途中までfalse 残りはずっとtrue
				String pos = ""; //RyouriIDが格納される この値が次のレコードのRyouriIDと異なったらtempListをrecipeBunryouListに追加して新しいtempListを用意する
				while (rs.next()) {
					recipeBunryouRecord[0] = rs.getString("syokuzaitb.Syokuzaimei"); //条件を満たす食材名を格納
					recipeBunryouRecord[1] = rs.getString("bunryoutb.Bunryou"); //条件を満たす分量を格納
					recipeBunryouRecord[2] = rs.getString("syokuzaitb.Tanni"); //条件を満たす単位を格納
					if (flag && !pos.equals(rs.getString("BunryouTB.RyouriID"))) { //最初のレコードでなく、前回のレコードとRyouriIDが異なったら
						recipeBunryouList.add(tempList); //tempListをrecipeBunryouListに追加して
						tempList = new ArrayList<String[]>(); //新しいtempListを用意する
					}
					tempList.add(recipeBunryouRecord); //tempListにレコードを追加する
					pos = rs.getString("BunryouTB.RyouriID"); //RyouriIDを更新する
					recipeBunryouRecord = new String[3]; //新しいrecipeBunryouRecordを用意する
					flag = true; //2件目以降のwhile文はtrueで回す
				}
				recipeBunryouList.add(tempList); //最後のレコードをrecipeBunryouListに追加する
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("レシピ概要検索SQL(分量)完了");
			for (int i = 0; i < recipeBunryouList.size(); i++) {
				String buff = (i + 1) + "番目の分量:";
				for (int j = 0; j < recipeBunryouList.get(i).size(); j++) {
					buff += Arrays.toString(recipeBunryouList.get(i).get(j));
				}
				System.out.println(buff);
			}
		}

		//requestに属性を追加してJSPにフォワードする
		request.setAttribute("pageNum", pageNum);
		request.setAttribute("searchMode", searchMode);
		request.setAttribute("inputData", inputData);
		request.setAttribute("recipeID", recipeID);
		request.setAttribute("recipeNum", recipeNum);
		request.setAttribute("recipeTitle", recipeTitle);
		request.setAttribute("recipeIntro", recipeIntro);
		request.setAttribute("recipeBunryouList",recipeBunryouList);
		RequestDispatcher rd_result = request.getRequestDispatcher(JSP_PATH1);
		rd_result.forward(request, response);
	}

}
