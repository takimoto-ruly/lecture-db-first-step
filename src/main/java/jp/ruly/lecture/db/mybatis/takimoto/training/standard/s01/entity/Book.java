package jp.ruly.lecture.db.mybatis.takimoto.training.standard.s01.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
	private Integer id;
	private String title;
	private String author;
	private Integer price;

	// ID無しのコンストラクタ（新規登録時の利便性のため手動定義）
	public Book(String title, String author, Integer price) {
		this.title = title;
		this.author = author;
		this.price = price;
	}

	// デバッグ表示をさらに見やすくしたい場合は toString をオーバーライド可能
	// 未定義でも Lombok が自動で「Book(id=1, title=...)」の形式を出力してくれます
	@Override
	public String toString() {
		return String.format("ID: %d | タイトル: %s | 著者: %s | 価格: %d円", id, title, author, price);
	}
}
