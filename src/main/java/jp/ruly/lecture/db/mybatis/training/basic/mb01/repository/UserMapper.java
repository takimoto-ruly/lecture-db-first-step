package jp.ruly.lecture.db.mybatis.training.basic.mb01.repository;

import java.util.List;

import jp.ruly.lecture.db.mybatis.training.basic.mb01.entity.User;

public interface UserMapper {
	List<User> findAll();

	User findById(Integer id);

	List<User> findByConditions(String name, Integer minAge);

	int updateStatus(Integer id, String status);
}
