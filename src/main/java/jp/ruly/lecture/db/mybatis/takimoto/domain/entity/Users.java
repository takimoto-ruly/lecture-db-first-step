package jp.ruly.lecture.db.mybatis.takimoto.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Users {
	private Integer id;
	private String name;
	private String email;
}
