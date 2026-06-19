package jp.ruly.lecture.db.mybatis.training.basic.mb02.entitty;

import lombok.Getter;

@Getter
public class Riddle {
	private Integer id;
	private String question;
	private String answer1;
	private String answer2;
	private String answer3;
	private int correctNumber;

	public Riddle(Integer id, String question, String answer1, String answer2, String answer3, Integer correctNumber) {
		if (question == null || question.isBlank())
			throw new IllegalArgumentException("問題文は必須です");
		if (correctNumber < 1 || correctNumber > 3)
			throw new IllegalArgumentException("1～3で指定してください");
		this.id = id;
		this.answer1 = answer1;
		this.answer2 = answer2;
		this.answer3 = answer3;
		this.correctNumber = correctNumber;
	}
}
