package jp.ruly.lecture.db.mybatis.takimoto.domain.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import jp.ruly.lecture.db.mybatis.takimoto.domain.entity.Users;

class UsersMapperTest {

	private static SqlSessionFactory sqlSessionFactory;
	private SqlSession session;
	private UsersMapper mapper;

	// テストクラス全体の実行前に一度だけMyBatisの設定を読み込む
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		String resource = "mybatis-config.xml"; // もしテスト用DBを分ける場合は "mybatis-config-test.xml" 等
		try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		}
	}

	// 各テストケース（@Test）が実行される直前に毎回呼ばれる
	@BeforeEach
	void setUp() {
		// セッションを開く（引数を空、またはfalseにすると手動コミット＝ロールバック可能になる）
		session = sqlSessionFactory.openSession();
		// テスト対象のMapperを取得
		mapper = session.getMapper(UsersMapper.class);

		// （オプション）ここにテスト用の初期データをINSERTするクエリなどを書いても良いです
	}

	// 各テストケースが終了した直後に毎回呼ばれる
	@AfterEach
	void tearDown() {
		if (session != null) {
			// テストによるDBへの変更を実際のデータベースに反映させないため、必ずロールバックする
			session.rollback();
			session.close();
		}
	}

	@Test
	@DisplayName("selectAll()を実行し、ユーザー一覧がID昇順で正しく取得できること")
	void testSelectAll() {
		// 1. テストの実行
		List<Users> result = mapper.selectAll();

		// 2. 検証（JUnit 5の共通Assertion）
		// 結果がnullでないことの確認
		assertNotNull(result, "取得結果のListがnullになっています");

		// ※データベースにあらかじめ3件のデータ（DDLで入れたもの等）があると仮定した場合の検証例
		assertFalse(result.isEmpty(), "テーブルが空、またはデータが取得できていません");

		// 1件目のデータを取得して、Lombokのゲッターや値が正しくマッピングされているか検証
		Users firstUser = result.get(0);
		assertNotNull(firstUser.getId(), "IDがマッピングされていません");
		assertNotNull(firstUser.getName(), "Nameがマッピングされていません");

		// ID順（ORDER BY id）に並んでいるかの検証例（2件目以降がある場合）
		if (result.size() > 1) {
			assertTrue(result.get(0).getId() < result.get(1).getId(), "IDが昇順でソートされていません");
		}

		// コンソール確認用（テスト結果の視覚的確認）
		System.out.println("--- JUnit Test: selectAll() Result ---");
		result.forEach(System.out::println);
	}
}