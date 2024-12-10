# 맞춤 장학금 서비스
<사진>

## 소개
이 프로젝트는 대학생들에게 장학금 정보를 제공하고, 더 나아가 입력받은 정보를 토대로 맞춤 장학금 정보를 제공하는 서비스의 서버 코드 입니다.
장학금 정보를 제공하는데 필요한 여러 API 들이 구현되어 있습니다.

## 구현 내용
- **JWT 및 OAuth2 인증 및 권한 관리**
- **CRUD 기능**: 데이터 생성, 조회, 수정, 삭제
- **AWS S3**를 통한 파일 업로드 및 관리
- **Redis 캐싱**으로 빠른 데이터 처리
- **QueryDSL**을 활용한 동적 검색 쿼리
- **Swagger UI**로 직관적인 API 문서 제공
- **Docker Compose**를 활용한 서버와 Redis의 컨테이너 통합 관리
- **AWS EC2** 환경에서 Docker 컨테이너 배포

## API 소개
서버 url : <http://ec2-15-164-84-210.ap-northeast-2.compute.amazonaws.com:8080/swagger-ui/index.html#/>
<img width="1470" alt="image" src="https://github.com/user-attachments/assets/92dadb3a-4563-485a-9324-02083e68517e">
<img width="1470" alt="image" src="https://github.com/user-attachments/assets/f14d1ba9-e15b-40fe-a881-16d9e4375209">


## 기술 스택

| **구분**       | **기술/도구**                     |
|-----------------|----------------------------------|
| **언어**       | Java 17                          |
| **프레임워크** | Spring Boot 3.3.3                |
| **데이터베이스** | H2 (로컬 환경), AWS S3 (파일)    |
| **인증/인가**  | Spring Security, JWT, OAuth2     |
| **캐싱**       | Redis                             |
| **쿼리**       | Spring Data JPA, QueryDSL         |
| **빌드 툴**    | Gradle                            |
| **문서화**     | Swagger                           |
| **컨테이너**   | Docker, Docker Compose            |
| **배포**       | AWS EC2                           |


## 💡Why?
***개발 과정 중에 있었던 어려움이나 기술을 채택한 이유에 대해 설명한 블로그 글입니다***

- 동적쿼리를 위한 QueryDsl 사용에 대한 글: (Spring Boot에서 Querydsl 사용): <https://onetaek.tistory.com/4>
- 도커를 활용해서 EC2에 서버를 배포하기 까지: (Docker와 Docker compose): <https://onetaek.tistory.com/5>
- Spring Security 사용에 대한 글: 
