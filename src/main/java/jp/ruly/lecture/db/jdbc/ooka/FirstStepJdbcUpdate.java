package jp.ruly.lecture.db.jdbc.ooka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstStepJdbcUpdate {
	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("ユーザーの更新");
			System.out.println("名前の入力");
			String name = scanner.nextLine();

			System.out.println("メールアドレスを入力");
			System.out.println("データの更新を開始");
			String email = scanner.nextLine();

			System.out.println("--------------------------------------------------");

			String sql = "UPDATE \"ユーザーテーブル\" SET \"氏名\" = ?, \"メールアドレス\" = ? WHERE \"ユーザーID\" = 1";
			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = con.prepareStatement(sql)) {

				pstmt.setString(1, name);
				pstmt.setString(2, email);

				int count = pstmt.executeUpdate();

				System.out.println("--------------------------------------------------");
				System.out.println("データ更新が完了（更新件数：" + count + "）");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}