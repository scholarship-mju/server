# Step 1: Build stage
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle Wrapper 실행 권한 추가
RUN chmod +x ./gradlew

# Gradle 빌드 실행 및 빌드 결과 확인
RUN ./gradlew clean build -x test && ls build/libs

# Step 2: Runtime stage
FROM openjdk:17-jdk-slim AS runtime

WORKDIR /app

RUN apt-get update && apt-get install -y iputils-ping netcat-traditional redis-tools && rm -rf /var/lib/apt/lists/*

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar /app/scholarship.jar

# JAR 파일 권한 설정 (필요시)
RUN chmod 644 /app/scholarship.jar

# 애플리케이션 실행
CMD ["java", "-jar", "/app/scholarship.jar"]
