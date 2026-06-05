package jp.ruly.lecture.db.jdbc.takimoto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstStepJdbcInsert {
	private static final String URL = "jdbc:postgresql://localhost:5432/mydb";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		// ユーザー入力を受付
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("=== ユーザー登録 ===");
			System.out.println("名前を入力");
			String name = scanner.nextLine();

			System.out.println("メールアドレスを入力");
			System.out.println("データの登録を開始");

			String email = scanner.nextLine();

			System.out.println("--------------------------------------------------");
			// ユーザー入力をDBに登録
			String sql = "INSERT INTO users (name, email) VALUES(?, ?)";
			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = con.prepareStatement(sql)) {

				// プレースホルダに値をバインド
				pstmt.setString(1, name);
				pstmt.setString(2, email);

				int count = pstmt.executeUpdate();

				System.out.println("--------------------------------------------------");
				System.out.println("データ登録が完了（登録件数：" + count + "）");

			} catch (SQLException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}
	}
}
