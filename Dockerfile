# Step 1: Build stage
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

COPY . .

# Gradle Wrapper 실행 권한 추가
RUN chmod +x ./gradlew

# Gradle 빌드 실행
RUN ./gradlew clean build -x test && ls build/libs

# Step 2: Runtime stage
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/server.jar /app/server.jar

# 애플리케이션 실행
CMD ["java", "-jar", "/app/server.jar"]
