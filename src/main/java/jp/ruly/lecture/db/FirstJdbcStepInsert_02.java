package jp.ruly.lecture.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FirstJdbcStepInsert_02 {

	private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
	private static final String USER = "postgres";
	private static final String PASSWORD = "postgres";

	public static void main(String[] args) {
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("===モンスターの登録===");
			System.out.println("idを入力");
			int id = scanner.nextInt();
			scanner.nextLine();

			System.out.println("名前を入力");
			String name = scanner.nextLine();

			System.out.println("HPを入力");
			int hp = scanner.nextInt();
			scanner.nextLine();

			System.out.println("MPを入力");
			int mp = scanner.nextInt();
			scanner.nextLine();

			System.out.println("EXPを入力");
			int exp = scanner.nextInt();

			System.out.println("データの登録を開始");
			System.out.println("--------------------------------------------------");

			String sql = "INSERT INTO  monsters (id,name,hp,mp,exp) VALUES(?,?,?,?,?)";
			try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
					PreparedStatement psmt = con.prepareStatement(sql)) {

				psmt.setInt(1, id);
				psmt.setString(2, name);
				psmt.setInt(3, hp);
				psmt.setInt(4, mp);
				psmt.setInt(5, exp);

				int count = psmt.executeUpdate();

				System.out.println("--------------------------------------------------");
				System.out.println("データ登録が完了(登録件数：" + count + ")");
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
}
