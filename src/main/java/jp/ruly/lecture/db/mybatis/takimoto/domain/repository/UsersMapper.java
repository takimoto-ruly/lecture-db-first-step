package jp.ruly.lecture.db.mybatis.takimoto.domain.repository;

import java.util.List;

import jp.ruly.lecture.db.mybatis.takimoto.domain.entity.Users;

public interface UsersMapper {
	List<Users> selectAll();
}
