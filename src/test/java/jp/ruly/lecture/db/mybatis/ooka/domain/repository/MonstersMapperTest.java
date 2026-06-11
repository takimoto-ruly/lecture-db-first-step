package jp.ruly.lecture.db.mybatis.ooka.domain.repository;

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

import jp.ruly.lecture.db.mybatis.ooka.domain.entity.Monsters;

public class MonstersMapperTest {
	private static SqlSessionFactory sqlSessionFactory;
	private SqlSession session;
	private MonstersMapper mapper;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		String resouce = "ooka-mybatis-config.xml";
		try (InputStream inputStream = Resources.getResourceAsStream(resouce)) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		}
	}

	@BeforeEach
	void setUp() {
		session = sqlSessionFactory.openSession();
		mapper = session.getMapper(MonstersMapper.class);
	}

	@AfterEach
	void tearDown() {
		if (session != null) {
			session.rollback();
			session.close();
		}
	}

	@Test
	@DisplayName("selectAll()を実行し、ユーザー一覧がID昇順で正しく取得できること")
	void testSelectAll() {
		List<Monsters> result = mapper.selectAll();

		assertNotNull(result, "取得結果のListがnullになっています");

		assertFalse(result.isEmpty(), "テーブルが空、またはデータが取得できていません");

		Monsters firstMonster = result.get(0);
		assertNotNull(firstMonster.getId(), "IDがマッピングされていません");
		assertNotNull(firstMonster.getName(), "Nameがマッピングされていません");

		if (result.size() > 1) {
			assertTrue(result.get(0).getId() < result.get(1).getId(), "IDが昇順でソートされていません");
		}

		System.out.println("--- JUnit Test: selectAll() Result ---");
		result.forEach(System.out::println);
	}
}