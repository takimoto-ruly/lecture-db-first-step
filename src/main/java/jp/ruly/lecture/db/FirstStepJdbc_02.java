package jp.ruly.lecture.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirstStepJdbc_02 {

	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		String sql = "SELECT \"ユーザーID\",\"氏名\",\"メールアドレス\"FROM\"ユーザーテーブル\";";

		System.out.println("データベース接続の開始．．．");

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);) {

			System.out.println("接続完了");
			System.out.println("--------------------------------------------------");

			while (rs.next()) {
				int id = rs.getInt("ユーザーID");
				String name = rs.getString("氏名");
				String email = rs.getString("メールアドレス");

				System.out.printf("ID:%d,名前:%s,メール:%s\n", id, name, email);
			}
			System.out.println("--------------------------------------------------");
			System.out.println("データ取得完了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
