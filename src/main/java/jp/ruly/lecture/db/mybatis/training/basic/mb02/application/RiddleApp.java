package jp.ruly.lecture.db.mybatis.training.basic.mb02.application;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import jp.ruly.lecture.db.mybatis.training.basic.mb02.entitty.Riddle;
import jp.ruly.lecture.db.mybatis.training.basic.mb02.repository.RiddleMapper;

public class RiddleApp {
	public void run() {
		Scanner scanner = new Scanner(System.in);

		try (InputStream inputStream = Resources.getResourceAsStream("ooka-mybatis-config.xml")) {
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

			try (SqlSession session = sqlSessionFactory.openSession()) {
				RiddleMapper mapper = session.getMapper(RiddleMapper.class);

				System.out.println("1.なぞなぞに挑戦する / 2.新しいなぞなぞを追加する / 3.終了する");
				int choice = scanner.nextInt();
				if (1 == choice) {
					question(scanner, mapper);
				} else if (3 == choice) {
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void question(Scanner scanner, RiddleMapper mapper) {
		List<Riddle> riddles = mapper.selectAll();
		if (riddles.isEmpty()) {
			System.out.println("問題がありません");
			return;
		}
		Riddle question = riddles.get(0);
		this.printQuestion(question);
		int userChoice = this.getUserChoice(scanner);

		if (userChoice == question.getCorrectNumber()) {
			System.out.println("正解です");

		} else {
			System.out.println("不正解です");
		}
	}

	public void printQuestion(Riddle question) {
		System.out.println("【なぞなぞクイズ！】");
		System.out.println(question.getQuestion());
		System.out.println("1:" + question.getAnswer1());
		System.out.println("2:" + question.getAnswer2());
		System.out.println("3:" + question.getAnswer3());
	}

	public int getUserChoice(Scanner scanner) {
		System.out.println("正解番号を入力してください(1から3)");
		while (true) {
			try {
				int input = scanner.nextInt();
				if (input < 1 || input > 3) {
					throw new IllegalArgumentException();
				}

				return input;
			} catch (Exception e) {
				System.out.println("1から3の数字を入力してください");
			}
		}
	}

	public static void main(String[] args) {
		RiddleApp app = new RiddleApp();
		app.run();
	}
}
