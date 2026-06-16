package jp.ruly.lecture.db.mybatis.ooka.domain.repository;

import java.util.List;

import jp.ruly.lecture.db.mybatis.ooka.domain.entity.Users;

public interface UsersMapper {
	List<Users> selectAll();
}