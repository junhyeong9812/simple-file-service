# Simple File Service

[![Docker](https://github.com/junhyeong9812/simple-file-service/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/junhyeong9812/simple-file-service/actions/workflows/docker-publish.yml)
[![Docker Image](https://img.shields.io/badge/docker-ghcr.io%2Fjunhyeong9812%2Fsimple--file--service-blue)](https://github.com/junhyeong9812/simple-file-service/pkgs/container/simple-file-service)

[Streamix](https://github.com/junhyeong9812/Streamix) 라이브러리의 레퍼런스 구현체입니다.  
Docker 이미지로 바로 실행하여 파일 업로드/스트리밍 서비스를 사용할 수 있습니다.

---

## 🚀 Quick Start

### Docker로 실행

```bash
docker pull ghcr.io/junhyeong9812/simple-file-service:latest

docker run -d \
  -p 8080:8080 \
  -v ./data:/app/streamix-data \
  --name file-service \
  ghcr.io/junhyeong9812/simple-file-service:latest
```

### 접속

- **대시보드**: http://localhost:8080/streamix
- **API**: http://localhost:8080/api/streamix/files

---

## 📦 Docker Compose

```yaml
version: '3.8'

services:
  file-service:
    image: ghcr.io/junhyeong9812/simple-file-service:latest
    ports:
      - "8080:8080"
    volumes:
      - ./data:/app/streamix-data
    environment:
      - STREAMIX_STORAGE_BASE_PATH=/app/streamix-data
      - STREAMIX_STORAGE_MAX_FILE_SIZE=524288000  # 500MB
```

---

## ⚙️ 환경 변수

| 변수 | 기본값 | 설명 |
|------|--------|------|
| `STREAMIX_STORAGE_BASE_PATH` | `./streamix-data` | 파일 저장 경로 |
| `STREAMIX_STORAGE_MAX_FILE_SIZE` | `104857600` | 최대 파일 크기 (바이트) |
| `STREAMIX_THUMBNAIL_ENABLED` | `true` | 썸네일 생성 활성화 |
| `STREAMIX_THUMBNAIL_WIDTH` | `320` | 썸네일 너비 |
| `STREAMIX_THUMBNAIL_HEIGHT` | `180` | 썸네일 높이 |
| `STREAMIX_API_ENABLED` | `true` | REST API 활성화 |
| `STREAMIX_API_BASE_PATH` | `/api/streamix` | API 기본 경로 |
| `STREAMIX_DASHBOARD_ENABLED` | `true` | 대시보드 활성화 |
| `STREAMIX_DASHBOARD_PATH` | `/streamix` | 대시보드 경로 |

---

## 🔌 API Endpoints

### 파일 관리

| Method | Endpoint | 설명 |
|--------|----------|------|
| `POST` | `/api/streamix/files` | 파일 업로드 |
| `GET` | `/api/streamix/files` | 파일 목록 조회 |
| `GET` | `/api/streamix/files/{id}` | 파일 정보 조회 |
| `DELETE` | `/api/streamix/files/{id}` | 파일 삭제 |

### 스트리밍

| Method | Endpoint | 설명 |
|--------|----------|------|
| `GET` | `/api/streamix/files/{id}/stream` | 파일 스트리밍 (Range 지원) |
| `GET` | `/api/streamix/files/{id}/thumbnail` | 썸네일 조회 |

### 사용 예시

```bash
# 파일 업로드
curl -X POST http://localhost:8080/api/streamix/files \
  -F "file=@video.mp4"

# 파일 목록 조회
curl http://localhost:8080/api/streamix/files

# 파일 스트리밍 (Range Request)
curl -H "Range: bytes=0-1023" \
  http://localhost:8080/api/streamix/files/{id}/stream
```

---

## 🏗️ 직접 빌드

### 요구사항

- Java 25+
- Gradle 8.x+

### 빌드 & 실행

```bash
git clone https://github.com/junhyeong9812/simple-file-service.git
cd simple-file-service

./gradlew build
./gradlew bootRun
```

### Docker 이미지 빌드

```bash
docker build -t simple-file-service .
docker run -p 8080:8080 simple-file-service
```

---

## 📚 기술 스택

- **Java 25**
- **Spring Boot 4.0**
- **Streamix 1.0.6** - 파일 스트리밍 라이브러리
- **H2 Database** - 인메모리 데이터베이스
- **Thymeleaf** - 대시보드 UI

---

## 🔗 관련 프로젝트

- [Streamix](https://github.com/junhyeong9812/Streamix) - 파일 스트리밍 Spring Boot Starter 라이브러리

---

## 📄 License

MIT License