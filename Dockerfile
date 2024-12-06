# OpenJDK 17 베이스 이미지 사용
FROM openjdk:17-jdk-slim AS build

# 작업 디렉토리 설정
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle Wrapper 실행 권한 추가
RUN chmod +x ./gradlew

# 프로젝트 빌드
RUN ./gradlew clean build -x test

FROM openjdk:17-jdk-slim AS runtime

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar /app/server.jar

# 애플리케이션 실행
CMD ["java", "-jar", "/app/server.jar"]
