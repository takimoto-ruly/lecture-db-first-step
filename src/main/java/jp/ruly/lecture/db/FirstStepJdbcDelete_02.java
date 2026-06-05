package jp.ruly.lecture.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstStepJdbcDelete_02 {
	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("削除するアイテムの名前を入力");
			String name = scanner.nextLine();

			System.out.println("データの削除を開始");
			System.out.println("--------------------------------------------------");

			String sql = "DELETE FROM items WHERE name = ?";

			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = con.prepareStatement(sql)) {

				pstmt.setString(1, name);

				int count = pstmt.executeUpdate();

				System.out.println("--------------------------------------------------");
				System.out.println("データ削除が完了（削除件数：" + count + "件）");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}