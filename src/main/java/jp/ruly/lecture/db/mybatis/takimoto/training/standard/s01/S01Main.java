package jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.entity.Book;
import jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.repository.BookMapper;

public class S01Main {
	public static void main(String[] args) throws Exception {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("\n--- 書籍管理システム (Lombok対応版) ---");
			System.out.println("1: 全件表示 | 2: 1件検索 | 3: 新規登録 | 4: 更新 | 5: 削除 | 0: 終了");
			System.out.print("メニュー番号を入力してください: ");
			int menu = scanner.nextInt();
			scanner.nextLine(); // 改行クリア

			if (menu == 0) {
				System.out.println("アプリケーションを終了します。");
				break;
			}

			try (SqlSession session = sqlSessionFactory.openSession()) {
				BookMapper mapper = session.getMapper(BookMapper.class);

				switch (menu) {
				case 1: // 全件表示
					List<Book> books = mapper.selectAll();
					if (books.isEmpty()) {
						System.out.println("データがありません。");
					} else {
						books.forEach(System.out::println);
					}
					break;

				case 2: // 1件検索
					System.out.print("検索するID: ");
					int searchId = scanner.nextInt();
					Book foundBook = mapper.selectById(searchId);
					if (foundBook != null) {
						System.out.println("【検索結果】 " + foundBook);
					} else {
						System.out.println("該当するIDの書籍は見つかりませんでした。");
					}
					break;

				case 3: // 新規登録
					System.out.print("タイトル: ");
					String title = scanner.nextLine();
					System.out.print("著者: ");
					String author = scanner.nextLine();
					System.out.print("価格: ");
					int price = scanner.nextInt();

					// 手動追加したコンストラクタを利用
					Book newBook = new Book(title, author, price);
					int insertCount = mapper.insert(newBook);
					session.commit();

					// Lombokが生成したgetId()が正常に動作します
					System.out.println(insertCount + " 件の書籍を登録しました。(新ID: " + newBook.getId() + ")");
					break;

				case 4: // 更新
					System.out.print("更新するID: ");
					int updateId = scanner.nextInt();
					scanner.nextLine();

					Book targetBook = mapper.selectById(updateId);
					if (targetBook == null) {
						System.out.println("該当する書籍がありません。");
						break;
					}

					System.out.print("新しいタイトル[" + targetBook.getTitle() + "]: ");
					String newTitle = scanner.nextLine();
					System.out.print("新しい著者[" + targetBook.getAuthor() + "]: ");
					String newAuthor = scanner.nextLine();
					System.out.print("新しい価格[" + targetBook.getPrice() + "]: ");
					int newPrice = scanner.nextInt();

					// Lombokが自動生成したSetterで値を更新
					targetBook.setTitle(newTitle);
					targetBook.setAuthor(newAuthor);
					targetBook.setPrice(newPrice);

					int updateCount = mapper.update(targetBook);
					session.commit();
					System.out.println(updateCount + " 件の書籍情報を更新しました。");
					break;

				case 5: // 削除
					System.out.print("削除するID: ");
					int deleteId = scanner.nextInt();

					int deleteCount = mapper.delete(deleteId);
					session.commit();

					if (deleteCount > 0) {
						System.out.println(deleteCount + " 件の書籍を削除しました。");
					} else {
						System.out.println("該当するIDの書籍がありませんでした。");
					}
					break;

				default:
					System.out.println("無効な番号です。0〜5を選択してください。");
				}
			} catch (Exception e) {
				System.err.println("エラーが発生しました。ロールバックします。");
				e.printStackTrace();
			}
		}
		scanner.close();
	}
}
