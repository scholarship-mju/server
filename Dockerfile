# OpenJDK 17 베이스 이미지 사용
FROM openjdk:17-jdk-slim AS build

# 작업 디렉토리 설정
WORKDIR /app

# JAR 파일 복사
COPY . .

RUN ./gradlew build

FROM openjdk:17-jdk-slim AS runtime

# 작업 디렉토리 설정
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/server.jar
# 애플리케이션 실행
CMD ["java", "-jar", "/app/server.jar"]