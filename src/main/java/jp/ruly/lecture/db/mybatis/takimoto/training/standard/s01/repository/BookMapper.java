package jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.repository;

import java.util.List;

import jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.entity.Book;

public interface BookMapper {
	List<Book> selectAll();

	Book selectById(int id);

	int insert(Book book);

	int update(Book book);

	int delete(int id);
}
