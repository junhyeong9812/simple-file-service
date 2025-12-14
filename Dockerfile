# Build stage
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /build

# Gradle wrapper 복사
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

# 의존성 캐싱을 위해 먼저 복사
COPY build.gradle settings.gradle ./

# 소스 복사 및 빌드
COPY src src
RUN ./gradlew build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:25-jre

WORKDIR /app

# FFmpeg 설치 (비디오 썸네일용)
RUN apt-get update && \
    apt-get install -y --no-install-recommends ffmpeg && \
    rm -rf /var/lib/apt/lists/*

# JAR 파일 복사
COPY --from=builder /build/build/libs/*.jar app.jar

# 데이터 디렉토리 생성
RUN mkdir -p /app/streamix-data

# 환경 변수 기본값
ENV STREAMIX_STORAGE_BASE_PATH=/app/streamix-data
ENV STREAMIX_THUMBNAIL_FFMPEG_PATH=/usr/bin/ffmpeg

# 포트 노출
EXPOSE 8080

# 헬스체크
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 실행
ENTRYPOINT ["java", "-jar", "app.jar"]