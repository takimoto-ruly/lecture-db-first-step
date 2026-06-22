package jp.ruly.lecture.db.mybatis.training.basic.mb02.repository;

import java.util.List;

import jp.ruly.lecture.db.mybatis.training.basic.mb02.entitty.Riddle;

public interface RiddleMapper {
	List<Riddle> selectAll();

	void insert(Riddle riddle);
}