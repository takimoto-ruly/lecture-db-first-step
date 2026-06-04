package jp.ruly.lecture.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstJdbcStepInsert_01 {

	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("===ユーザー登録===");
			System.out.println("ユーザーIDを入力");
			int id = scanner.nextInt();
			scanner.nextLine();

			System.out.println("名前を入力");
			String name = scanner.nextLine();

			System.out.println("メールアドレスを入力");
			String email = scanner.nextLine();

			System.out.println("データの登録を開始");

			
			String sql = "INSERT INTO \"ユーザーテーブル\" (\"ユーザーID\",\"氏名\",\"メールアドレス\") VALUES(?, ?, ?)";
			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement pstmt = con.prepareStatement(sql)) {

				pstmt.setInt(1, id);
				pstmt.setString(2, name);
				pstmt.setString(3, email);

				int count = pstmt.executeUpdate();

				System.out.println("--------------------------------------------------");
				System.out.println("データ登録が完了(登録件数：" + count + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}