package jp.ruly.lecture.db.jdbc.ooka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FirstStepJdbc_03 {

	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		String sql = "SELECT id,name,hp,mp,exp FROM monsters order by id";

		System.out.println("データベースの接続開始");

		try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(sql);) {

			System.out.println("接続完了");
			System.out.println("---------------------------------------");

			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				int hp = rs.getInt("hp");
				int mp = rs.getInt("mp");
				int exe = rs.getInt("exp");

				System.out.printf("id:%d,name:%s,hp:%d,mp:%d.exp%d", id, name, hp, mp, exe);
			}
			System.out.println("---------------------------------------");
			System.out.println("データ取得完了");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
