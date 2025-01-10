# Wirebarley Coding Test Project

## 송금 서비스 코딩테스트 과제

### 과제 설명
- 본 과제는 계좌 간 송금 시스템을 설계하고 구현하는 것입니다.
- 아래에 명시된 API 요구사항을 기반으로 송금 서비스의 핵심 기능을 개발해주세요.
- 아키텍처는 자유롭게 선택 가능하며, **JPA**를 사용하여 데이터베이스와의 연동을 구현해야 합니다.
- 테이블 설계 및 API 설계는 자유롭게 하되, 요구사항에 맞게 설계해주세요.
- Docker Compose를 사용해 애플리케이션을 실행할 수 있는 환경을 제공해주세요.

## 과제 사용 기술
- Java 17
- Spring Boot & JPA (Hibernate)
- Docker 및 Docker Compose
- RESTful API 설계 및 구현
- 
### 외부 라이브러리 및 오픈소스 사용 가능 (사용 목적 명시)
- jwt - jwtToken을 사용하기 위해 사용
- gson - DB와 통신하는 entity 값을 구분하여 요청에 따른 requestDTO,responseDTO의 필요한 값만 사용하기 위해 사용
- lombok - DTO, entity 등 코드 자동화를 위해 사용
- springdoc-openapi - API 문서 작성을 위해 사용

## DB - ERD 
### DB 정보 - postgresql 14
- IP : localhost
- PORT : 5423
- DB : bankdb
- ID : bank
- pw : bank123!
### TABLE 목록
- MEMBER : 회원 테이블
- COMMON_CDE : 공통 코드 테이블
- ACCOUNT : 계좌 테이블
- PRODUCT : 상품 테이블
- TRADE : 거래 테이블
![Alt Text](/bank_erd.png)

## 실행 방법
### Docker 실행 방법
- docker-compose.yml, Dockerfile 을 사용하여 Docker 를 실행 할 수 있게 작성 해 두 었습니다.
```bash
# docker-실행 명령어 
docker-compose up --build
```
### Jwt Token 발행 및 사용 방법
- Security 를 사용하여 특정 URL 제외 하고 TOKEN 값이 필수 입니다.
- API 테스트 하실 경우 Token을 발행 하여 사용하여 주시기 바랍니다.
1. Token 발행
`[POST] http://localhost:8080/api/auth/login-jwt` 로 요청 후, <br>
응답 값 중 `token`을 헤더의 `Authorization`에 추가하여 사용합니다.

#### 요청 예제
```json
{
    "userId": "user",
    "password": "user123!"
}
```

#### 응답 예제
```json
{
    "rtCode": 200,
    "rtMsg": "The request was successfully processed.",
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJhZGRyZXNzIjoi7ISc7Jq4IOq4iOyynOq1rCDrlJTsp4DthLjroZw56ri4IDY4IiwiYXV0aG9yaXphdGlvbkNvZGUiOiJVU0VSIiwibmFtZSI6IuyCrOyaqeyekCIsImJyaXRoRGF0ZSI6IjIwMjUwMTAxIiwidHlwZSI6IkF1dGhvcml6YXRpb24iLCJ1c2VySWQiOiJ1c2VyIiwidGVsTm8iOiIwMDAwMDAwMDAwMCIsInVzZVN0YXR1cyI6IkNNTVVTRTAwMSIsInN1YiI6IkF1dGhvcml6YXRpb24iLCJpYXQiOjE3MzY0ODg0ODksImV4cCI6MTczNzgwMjQ4OX0.4-o1z7qS5B-bQ7DRxEM6-dpe4qERMQhorfQ-1r24T5O_ghu2SmtS5YQFaU-hpbwA9nQn1AUuJycTzYuTwZU3dQ"
}
```