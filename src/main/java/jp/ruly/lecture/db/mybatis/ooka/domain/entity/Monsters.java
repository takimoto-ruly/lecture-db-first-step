package jp.ruly.lecture.db.mybatis.ooka.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Monsters {
	private Integer id;
	private String name;
	private Integer hp;
	private Integer mp;
	private Integer exp;
}