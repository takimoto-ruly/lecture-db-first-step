package jp.ruly.lecture.db.mybatis.training.basic.mb01;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jp.ruly.lecture.db.mybatis.training.basic.mb01.entity.User;
import jp.ruly.lecture.db.mybatis.training.basic.mb01.repository.UserMapper;

public class MyBatisStepByStepTest {

	private static SqlSessionFactory sqlSessionFactory;
	private SqlSession sqlSession;
	private UserMapper mapper;

	@BeforeAll
	static void setUpSchema() throws Exception {
		// 1. MyBatisの設定ファイルを読み込み、SessionFactoryを生成
		String resource = "mybatis-config.xml";
		try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		}

		// 2. テスト用H2データベースにテーブルを作成 (初回のみ)
		try (SqlSession session = sqlSessionFactory.openSession();
				Connection conn = session.getConnection();
				Statement stmt = conn.createStatement()) {
			stmt.execute("CREATE TABLE IF NOT EXISTS \"user\" (" +
					"id SELEAL PRIMARY KEY, " +
					"name VARCHAR(50), " +
					"age INT, " +
					"status VARCHAR(20))");
		}
	}

	@BeforeEach
	void setUpTestData() throws Exception {
		// テストごとに個別のセッションを開き、Mapperを取得
		sqlSession = sqlSessionFactory.openSession(true); // オートコミット有効
		mapper = sqlSession.getMapper(UserMapper.class);

		// DBUnitを使ってデータを初期化 (CLEAN_INSERT)
		Connection connection = sqlSession.getConnection();
		IDatabaseConnection dbunitConnection = new DatabaseConnection(connection);
		dbunitConnection.getConfig().setProperty(
				org.dbunit.database.DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "\"?\"");

		try (InputStream is = getClass().getClassLoader().getResourceAsStream("dataset.xml")) {
			var dataSet = new FlatXmlDataSetBuilder().build(is);
			DatabaseOperation.CLEAN_INSERT.execute(dbunitConnection, dataSet);
		}
	}

	@AfterEach
	void tearDown() {
		if (sqlSession != null) {
			sqlSession.close();
		}
	}

	// ==========================================
	// 第1問：基本のSELECTテスト
	// ==========================================

	@Test
	@DisplayName("第1問: ユーザーが全件取得できること")
	void testFindAll() {
		List<User> users = mapper.findAll();
		assertEquals(3, users.size(), "初期データは3件であるはずです");
		assertEquals("Yamada", users.get(0).getName());
	}

	@Test
	@DisplayName("第1問: 存在するIDで1件検索ができること")
	void testFindByIdSuccess() {
		User user = mapper.findById(2);
		assertNotNull(user);
		assertEquals("Tanaka", user.getName());
		assertEquals(25, user.getAge());
	}

	@Test
	@DisplayName("第1問: 存在しないIDで検索したときnullが返ること")
	void testFindByIdNotFound() {
		User user = mapper.findById(999);
		assertNull(user, "存在しないIDの場合はnullが返る仕様です");
	}

	// ==========================================
	// 第2問：条件付き検索（動起SELECT）のテスト
	// ==========================================

	@Test
	@DisplayName("第2問: 名前の一部を指定して動的検索ができること")
	void testFindByConditionsNameLike() {
		// "a" を含むのは Yamada(18) と Tanaka(25)
		List<User> users = mapper.findByConditions("a", null);
		assertEquals(2, users.size());
	}

	@Test
	@DisplayName("第2問: 最低年齢を指定して動的検索ができること")
	void testFindByConditionsMinAge() {
		// 25歳以上は Tanaka(25) と Suzuki(30)
		List<User> users = mapper.findByConditions(null, 25);
		assertEquals(2, users.size());
	}

	@Test
	@DisplayName("第2問: 名前と最低年齢の両方を指定して複合検索ができること")
	void testFindByConditionsBoth() {
		// "a" を含み、かつ25歳以上 ➔ Tanaka(25) のみ
		List<User> users = mapper.findByConditions("a", 25);
		assertEquals(1, users.size());
		assertEquals("Tanaka", users.get(0).getName());
	}

	@Test
	@DisplayName("第2問: 条件を何も指定しない場合、全件が取得されること")
	void testFindByConditionsNone() {
		List<User> users = mapper.findByConditions(null, null);
		assertEquals(3, users.size(), "条件なしの場合は全件取得になるべきです");
	}

	// ==========================================
	// 第3問：UPDATE処理とビジネスルールのテスト
	// ==========================================

	@Test
	@DisplayName("第3問: 20歳以上のユーザーのステータス更新が成功すること")
	void testUpdateStatusSuccess() {
		// Tanakaは25歳なので条件を満たす
		int updatedCount = mapper.updateStatus(2, "SUSPENDED");
		assertEquals(1, updatedCount, "更新が成功したため1が返るべきです");

		// 反映確認
		User updatedUser = mapper.findById(2);
		assertEquals("SUSPENDED", updatedUser.getStatus());
	}

	@Test
	@DisplayName("第3問: 20歳未満のユーザーを更新しようとしたとき、更新されず0が返ること")
	void testUpdateStatusRowLockedByAge() {
		// Yamadaは18歳なので、SQLのWHERE句ではじかれて更新されないはず
		int updatedCount = mapper.updateStatus(1, "SUSPENDED");
		assertEquals(0, updatedCount, "年齢条件を満たさないため更新件数は0であるべきです");

		// データベース側が変わっていないことを確認
		User nonUpdatedUser = mapper.findById(1);
		assertEquals("ACTIVE", nonUpdatedUser.getStatus(), "ステータスはACTIVEのままであるべきです");
	}
}