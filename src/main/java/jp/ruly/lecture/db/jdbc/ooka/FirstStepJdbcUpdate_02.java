package jp.ruly.lecture.db.jdbc.ooka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstStepJdbcUpdate_02 {

	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("アイテム更新");
			System.out.println("アイテムの名前を入力");
			String name = scanner.nextLine();

			System.out.println("アイテムの金額を入力");
			int price = scanner.nextInt();

			System.out.println("データの更新を開始");
			System.out.println("--------------------------------------------------");

			String sql = "UPDATE items SET name = ?, price = ? WHERE id = 1";
			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
				PreparedStatement pstmt = con.prepareStatement(sql);

				pstmt.setString(1, name);
				pstmt.setInt(2, price);

				int count = pstmt.executeUpdate();
				System.out.println("データの更新完了（更新件数：" + count + "件）");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}