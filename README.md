# 맞춤 장학금 서비스
## 소개
이 프로젝트는 청년들에게 지원되는 장학금의 수가 많고 다양한 것에 비해 장학금 정보를 찾기가 어렵고 자신이 조건이 되는 것에도 불구하고 정보의 부족으로
장학금을 받지 못하는 청년들이 많다고 생각되어 시작하게 된 프로젝트입니다. 단 한사람이라도 이 사이트를 통해서 장학금을 정보를 얻고 장학금을 받을 수 있도록
돕는 것이 이 프로젝트의 목적입니다.

<img width="1455" alt="image" src="https://github.com/user-attachments/assets/070f010a-0ba2-45ee-bf48-9f4710a558d6" />

## 구현 내용
- 로그인 : OAuth2 사용
- 인증/인가 : JWT & Spring Security 사용
- 맞춤(추천) 장학금 : QueryDsl -> AI 활용
- 배포 : Docker & AWS EC2
- CI/CD : Github Actions

## 💡Why?
***개발 과정 중에 있었던 어려움이나 기술을 채택한 이유에 대해 설명한 블로그 글입니다***

- 동적쿼리를 위한 QueryDsl 사용에 대한 글: (Spring Boot에서 Querydsl 사용): <https://onetaek.tistory.com/4>
- 도커를 활용해서 EC2에 서버를 배포하기 까지: (Docker와 Docker compose): <https://onetaek.tistory.com/5>
- 조회수 성능 최적화 by Redis: <https://onetaek.tistory.com/18>
- 레디스 백업 방식에 대한 고민 : <https://onetaek.tistory.com/19>
- 레디스 백업 환경 설정 : <https://onetaek.tistory.com/21>

## API 소개
처음 프로젝트 시작 후 협업할 때는 스웨거를 통해 명세를 했지만 이후 혼자서 프로젝트를 진행하기 때문에 명세는 잠시 중단했습니다.
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



