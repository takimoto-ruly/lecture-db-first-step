package jp.ruly.lecture.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirstStepJdbc_01 {

	private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		String sql = "SELECT id, name, email FROM users order by id;";

		System.out.println("データベース接続の開始...");

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);) {

			System.out.print("接続完了");
			System.out.print("--------------------------------------------------");

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String email = rs.getString("email");

				System.out.printf("ID:%d,名前:%s,メール:%s\n", id, name, email);
			}
			System.out.print("--------------------------------------------------");
			System.out.print("データ取得完了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
