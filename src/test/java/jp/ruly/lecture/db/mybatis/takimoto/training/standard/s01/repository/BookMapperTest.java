package jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.entity.Book;

class BookMapperTest {

	private static SqlSessionFactory sqlSessionFactory;
	private SqlSession session;
	private BookMapper mapper;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		// テスト開始時に一度だけSqlSessionFactoryをビルド
		String resource = "mybatis-config.xml";
		try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		}
	}

	@BeforeEach
	void setUp() throws Exception {
		// 各テストの実行前にセッションを開き、マッパーを取得
		session = sqlSessionFactory.openSession();
		mapper = session.getMapper(BookMapper.class);

		// データベースの初期化スクリプト（schema.sql）を実行して状態をリセット
		Connection connection = session.getConnection();
		try (Reader reader = Resources.getResourceAsReader("schema.sql")) {
			ScriptRunner runner = new ScriptRunner(connection);
			runner.setLogWriter(null); // テストコンソールを汚さないようにログを抑制
			runner.runScript(reader);
		}
	}

	@AfterEach
	void tearDown() {
		// 各テスト終了後にセッションをクローズ（コミットしないため自動でロールバック扱い）
		if (session != null) {
			session.close();
		}
	}

	@Test
	@DisplayName("全件取得ができること")
	void testSelectAll() {
		List<Book> books = mapper.selectAll();

		assertNotNull(books, "取得結果がnullになっていません");
		assertEquals(2, books.size(), "初期データ件数は2件である必要があります");

		Book firstBook = books.get(0);
		assertEquals("Java入門", firstBook.getTitle());
		assertEquals("山田太郎", firstBook.getAuthor());
		assertEquals(2800, firstBook.getPrice());
	}

	@Test
	@DisplayName("指定したIDで1件検索ができること")
	void testSelectById_ExistingId() {
		Book book = mapper.selectById(1);

		assertNotNull(book, "存在するIDの検索結果はnullになりません");
		assertEquals(1, book.getId());
		assertEquals("Java入門", book.getTitle());
	}

	@Test
	@DisplayName("存在しないIDで検索した場合にnullが返ること")
	void testSelectById_NonExistingId() {
		Book book = mapper.selectById(999);

		assertNull(book, "存在しないIDの検索結果はnullになる必要があります");
	}

	@Test
	@DisplayName("新規登録ができること、および自動採番されたIDがモデルにセットされること")
	void testInsert() {
		Book newBook = new Book("テスト駆動開発", "ケント・ベック", 4800);

		int resultCount = mapper.insert(newBook);

		assertEquals(1, resultCount, "1件登録される必要があります");
		assertNotNull(newBook.getId(), "useGeneratedKeysにより、生成されたIDがオブジェクトに格納される必要があります");
		assertEquals(3, newBook.getId(), "3番目のデータとしてID: 3が採番されるはずです");

		// 実際に登録されたか確認
		Book fetched = mapper.selectById(newBook.getId());
		assertNotNull(fetched);
		assertEquals("テスト駆動開発", fetched.getTitle());
		assertEquals(4800, fetched.getPrice());
	}

	@Test
	@DisplayName("書籍情報が更新できること")
	void testUpdate() {
		// ID: 1のデータを取得して書き換える
		Book target = mapper.selectById(1);
		target.setTitle("Java入門 第2版");
		target.setPrice(3000);

		int resultCount = mapper.update(target);

		assertEquals(1, resultCount, "1件更新される必要があります");

		// 再取得して値が変わっているか確認
		Book updated = mapper.selectById(1);
		assertEquals("Java入門 第2版", updated.getTitle());
		assertEquals(3000, updated.getPrice());
		assertEquals("山田太郎", updated.getAuthor(), "変更していない項目は維持されていること");
	}

	@Test
	@DisplayName("指定したIDの書籍が削除できること")
	void testDelete() {
		// 事前に存在を確認
		assertNotNull(mapper.selectById(2));

		int resultCount = mapper.delete(2);

		assertEquals(1, resultCount, "1件削除される必要があります");
		// 削除後はnullになることを確認
		assertNull(mapper.selectById(2), "削除されたデータは検索できなくなります");
	}
}