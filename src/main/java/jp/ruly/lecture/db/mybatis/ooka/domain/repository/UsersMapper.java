package jp.ruly.lecture.db.mybatis.ooka.domain.repository;

import java.util.List;

public interface UsersMapper {
	List<jp.ruly.lecture.db.mybatis.ooka.domain.entity.Users> selectAll();
}