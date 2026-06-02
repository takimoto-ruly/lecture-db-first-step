# lecture-db-first-step

## DDL

```
-- もしすでに同名のテーブルが存在する場合は削除する（依存関係も一緒に削除）
DROP TABLE IF EXISTS users CASCADE;

-- usersテーブルの作成（PostgreSQL用）
CREATE TABLE users (
    id SERIAL PRIMARY KEY,        -- PostgreSQLでの自動連番型
    name VARCHAR(50) NOT NULL,    -- ユーザー名
    email VARCHAR(100) UNIQUE     -- メールアドレス（重複不可）
);
```

## 初期データ

```
INSERT INTO users (name, email) VALUES ('山田 太郎', 'yamada@example.com');
INSERT INTO users (name, email) VALUES ('佐藤 美咲', 'sato@example.com');
INSERT INTO users (name, email) VALUES ('鈴木 健二', 'suzuki@example.com');
```
