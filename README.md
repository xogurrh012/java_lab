# prodapp (Socket + MySQL 상품 CRUD)

자바 **TCP 소켓(포트 20000)** 으로 JSON 한 줄(개행 `\n`) 단위로 요청/응답을 주고받으며, MySQL에 상품을 **등록/삭제/상세/목록** 처리하는 예제입니다.

## 주요 구성

- **Server**: `src/main/java/server/MyServer.java`
  - `ServerSocket(20000)` 대기 → 클라이언트 1명 연결 수락 후 요청 루프 처리
  - `Gson`으로 `RequestDTO` 역직렬화 후 CRUD 분기
- **Client**: `src/main/java/client/MyClient.java`
  - 콘솔 메뉴로 요청 생성 → JSON 전송 → 서버 응답 출력
- **DTO**
  - `src/main/java/dto/RequestDTO.java`: `method`, `querystring`, `body`
  - `src/main/java/dto/ResponseDTO.java`: `msg`, `body`
- **DB**
  - `src/main/java/server/DBConnection.java`: MySQL 연결 정보
  - `src/main/java/server/ProductRepository.java`: JDBC로 `product` 테이블 CRUD

## 실행 환경

- Java (JDK) 8+ 권장
- MySQL 8+ 권장
- Gradle Wrapper 포함 (`gradlew`, `gradlew.bat`)

## DB 준비

서버는 다음 DB에 연결합니다(코드 기준):

- DB URL: `jdbc:mysql://localhost:3306/productdb`
- Username: `root`
- Password: `bitc5600!`

> 위 값은 `src/main/java/server/DBConnection.java`에 하드코딩되어 있으니, 로컬 환경에 맞게 수정하세요.

### 스키마/테이블 생성 예시

```sql
CREATE DATABASE IF NOT EXISTS productdb;
USE productdb;

CREATE TABLE IF NOT EXISTS product (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  price INT NOT NULL,
  qty INT NOT NULL
);
```

## 실행 방법 (IDE 권장)

이 프로젝트는 **서버/클라이언트에 `main()`이 각각 존재**합니다.

1) **서버 먼저 실행**
- `server.MyServer` 실행
- 포트 `20000`에서 클라이언트 연결을 기다립니다.

2) **클라이언트 실행**
- `client.MyClient` 실행
- 콘솔 메뉴에서 1~5를 입력해 요청을 보냅니다.

## 통신 규칙 (프로토콜)

- 전송 단위: **JSON 문자열 1줄 + 개행(`\n`)**
- 종료: 클라이언트가 `exit\n` 전송 시 서버 종료 루프 탈출

### RequestDTO 형식

- `method`: `"get" | "post" | "delete"`
- `querystring`: 예) `{"id": 1}` (없으면 `null`)
- `body`: 예) `{"name":"pen","price":1000,"qty":3}` (없으면 `null`)

### ResponseDTO 형식

- `msg`: `"ok"` 또는 오류 메시지
- `body`: 성공 시 결과(없으면 `null`)

## 기능별 요청 예시

### 1) 상품 등록 (Create)

요청(JSON):

```json
{"method":"post","querystring":null,"body":{"name":"pen","price":1000,"qty":3}}
```

응답 예시:

```json
{"msg":"ok","body":null}
```

### 2) 상품 삭제 (Delete)

요청(JSON):

```json
{"method":"delete","querystring":{"id":1},"body":null}
```

응답 예시:

```json
{"msg":"ok","body":null}
```

### 3) 상품 상세 (Detail)

요청(JSON):

```json
{"method":"get","querystring":{"id":1},"body":null}
```

응답 예시(성공):

```json
{"msg":"ok","body":{"id":1,"name":"pen","price":1000,"qty":3}}
```

응답 예시(실패):

```json
{"msg":"id가 없습니다.","body":null}
```

### 4) 상품 전체 (List)

요청(JSON):

```json
{"method":"get","querystring":null,"body":null}
```

응답 예시:

```json
{"msg":"ok","body":[{"id":1,"name":"pen","price":1000,"qty":3}]}
```

## 참고/주의

- 서버(`MyServer`)는 현재 구조상 **클라이언트 1명 연결만 `accept()`** 한 뒤 그 연결에서 계속 처리합니다(다중 접속/멀티스레드 아님).
- 잘못된 JSON을 보내면 서버는 다음 형태로 응답합니다:
  - `{"msg":"잘못된 JSON","body":null}`
- DB 연결 실패/SQL 예외는 `RuntimeException("SQL Error")`로 처리됩니다.

