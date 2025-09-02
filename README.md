# User Management API

Java REST APIユーザー管理システムです。管理者がユーザー情報を参照できる機能を提供します。

## 機能

- 管理者権限でのユーザー情報取得
- ユーザーIDによる特定ユーザー情報の取得
- 認証機能（Basic認証）
- エラーハンドリング（存在しないユーザーの場合）

## API仕様

### GET /api/users/{id}

指定されたIDのユーザー情報を取得します。

**認証**: Basic認証（管理者権限必須）

**パラメータ**:
- `id` (path): ユーザーID（数値）

**レスポンス**:
- 成功時 (200): ユーザー情報のJSON
- ユーザーが存在しない場合 (404): Not Found
- 認証失敗 (401): Unauthorized
- 権限不足 (403): Forbidden

**レスポンス例**:
```json
{
  "id": 1,
  "name": "田中太郎",
  "email": "tanaka@example.com",
  "phone": "090-1234-5678",
  "department": "開発部"
}
```

## 使用方法

### アプリケーションの起動

```bash
mvn spring-boot:run
```

### APIの呼び出し例

```bash
# 正常なケース（ユーザーID 1の情報を取得）
curl -u admin:admin123 http://localhost:8080/api/users/1

# 存在しないユーザー（404エラー）
curl -u admin:admin123 http://localhost:8080/api/users/999

# 認証なし（401エラー）
curl http://localhost:8080/api/users/1
```

### 認証情報

- ユーザー名: `admin`
- パスワード: `admin123`
- ロール: `ADMIN`

## テストの実行

```bash
mvn test
```

## 技術仕様

- Java 17
- Spring Boot 3.1.5
- Spring Security (Basic認証)
- Spring Data JPA
- H2データベース（インメモリ）
- Maven

## データベース

開発用にH2インメモリデータベースを使用しています。
初期データとして5件のサンプルユーザーが登録されています。

### サンプルデータ

1. 田中太郎 (tanaka@example.com, 開発部)
2. 佐藤花子 (sato@example.com, 営業部)
3. 鈴木一郎 (suzuki@example.com, マーケティング部)
4. 高橋美穂 (takahashi@example.com, 人事部)
5. 山田次郎 (yamada@example.com, 総務部)