package jp.ruly.lecture.db.jdbc.takimoto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstStepJdbcDelete {
	private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		// ユーザー入力を受付
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== 削除するユーザーの名前 ===");
			System.out.println("名前を入力");
			String name = scanner.nextLine();

			System.out.println("データの削除を開始");
			System.out.println("--------------------------------------------------");
			// ユーザー入力をDBに削除
			String sql = "DELETE FROM users WHERE name = ?;";
			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = con.prepareStatement(sql)) {

				// プレースホルダに値をバインド
				pstmt.setString(1, name);

				int count = pstmt.executeUpdate();

				System.out.println("--------------------------------------------------");
				System.out.println("データ削除が完了（削除件数：" + count + "）");

			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
