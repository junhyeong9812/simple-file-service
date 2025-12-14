# Simple File Service

[![Docker](https://github.com/junhyeong9812/simple-file-service/actions/workflows/docker-publish.yml/badge.svg)](https://github.com/junhyeong9812/simple-file-service/actions/workflows/docker-publish.yml)
[![Docker Image](https://img.shields.io/badge/docker-ghcr.io%2Fjunhyeong9812%2Fsimple--file--service-blue)](https://github.com/junhyeong9812/simple-file-service/pkgs/container/simple-file-service)

[Streamix](https://github.com/junhyeong9812/Streamix) 라이브러리의 레퍼런스 구현체입니다.  
Docker 이미지로 바로 실행하여 파일 업로드/스트리밍 서비스를 사용할 수 있습니다.

---

## 🚀 Quick Start

### Docker로 실행 (H2 인메모리)

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
- **H2 Console**: http://localhost:8080/h2-console

---

## 🔌 API Endpoints

### 파일 관리 API

| Method | Endpoint | 설명 | Request | Response |
|--------|----------|------|---------|----------|
| `POST` | `/api/streamix/files` | 파일 업로드 | `multipart/form-data` | `FileResponse` |
| `GET` | `/api/streamix/files` | 파일 목록 | `?page=0&size=20` | `Page<FileResponse>` |
| `GET` | `/api/streamix/files/{id}` | 파일 정보 조회 | - | `FileResponse` |
| `DELETE` | `/api/streamix/files/{id}` | 파일 삭제 | - | `204 No Content` |

### 스트리밍 API

| Method | Endpoint | 설명 | Headers |
|--------|----------|------|---------|
| `GET` | `/api/streamix/files/{id}/stream` | 파일 스트리밍 | `Range: bytes=0-1023` |
| `GET` | `/api/streamix/files/{id}/thumbnail` | 썸네일 조회 | - |

### 대시보드 페이지

| Endpoint | 설명 |
|----------|------|
| `/streamix` | 메인 대시보드 (통계, 최근 업로드) |
| `/streamix/files` | 파일 목록 (그리드/리스트 뷰) |
| `/streamix/files/{id}` | 파일 상세/미리보기 |
| `/streamix/monitor` | 스트리밍 모니터 |

### API 사용 예시

```bash
# 파일 업로드
curl -X POST http://localhost:8080/api/streamix/files \
  -F "file=@video.mp4"

# 파일 목록 조회
curl http://localhost:8080/api/streamix/files

# 파일 스트리밍 (Range Request)
curl -H "Range: bytes=0-1023" \
  http://localhost:8080/api/streamix/files/{id}/stream

# 파일 삭제
curl -X DELETE http://localhost:8080/api/streamix/files/{id}
```

---

## 🗄️ 데이터베이스 설정

기본값은 **H2 인메모리**이며, 환경변수로 PostgreSQL, MySQL 등으로 변경 가능합니다.

### H2 (기본값 - 테스트/개발용)

```bash
docker run -d -p 8080:8080 \
  ghcr.io/junhyeong9812/simple-file-service:latest
```

### PostgreSQL

```bash
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/filedb \
  -e SPRING_DATASOURCE_DRIVER=org.postgresql.Driver \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e SPRING_JPA_DDL_AUTO=update \
  -v ./data:/app/streamix-data \
  ghcr.io/junhyeong9812/simple-file-service:latest
```

### MySQL

```bash
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/filedb \
  -e SPRING_DATASOURCE_DRIVER=com.mysql.cj.jdbc.Driver \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=secret \
  -e SPRING_JPA_DDL_AUTO=update \
  -v ./data:/app/streamix-data \
  ghcr.io/junhyeong9812/simple-file-service:latest
```

---

## 📦 Docker Compose

### 기본 (H2)

```yaml
version: '3.8'

services:
  file-service:
    image: ghcr.io/junhyeong9812/simple-file-service:latest
    ports:
      - "8080:8080"
    volumes:
      - ./data:/app/streamix-data
```

### PostgreSQL 연동

```yaml
version: '3.8'

services:
  file-service:
    image: ghcr.io/junhyeong9812/simple-file-service:latest
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/filedb
      - SPRING_DATASOURCE_DRIVER=org.postgresql.Driver
      - SPRING_DATASOURCE_USERNAME=postgres
      - SPRING_DATASOURCE_PASSWORD=secret
      - SPRING_JPA_DDL_AUTO=update
      - STREAMIX_STORAGE_BASE_PATH=/app/streamix-data
      - H2_CONSOLE_ENABLED=false
    volumes:
      - file_data:/app/streamix-data
    depends_on:
      - postgres

  postgres:
    image: postgres:16-alpine
    environment:
      - POSTGRES_DB=filedb
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=secret
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  file_data:
  postgres_data:
```

---

## ⚙️ 환경 변수

### 데이터베이스

| 변수 | 기본값 | 설명 |
|------|--------|------|
| `SPRING_DATASOURCE_URL` | `jdbc:h2:mem:streamix` | JDBC URL |
| `SPRING_DATASOURCE_DRIVER` | `org.h2.Driver` | JDBC 드라이버 |
| `SPRING_DATASOURCE_USERNAME` | `sa` | DB 사용자명 |
| `SPRING_DATASOURCE_PASSWORD` | *(empty)* | DB 비밀번호 |
| `SPRING_JPA_DDL_AUTO` | `create-drop` | DDL 전략 |
| `H2_CONSOLE_ENABLED` | `true` | H2 콘솔 활성화 |

### Streamix

| 변수 | 기본값 | 설명 |
|------|--------|------|
| `STREAMIX_STORAGE_BASE_PATH` | `./streamix-data` | 파일 저장 경로 |
| `STREAMIX_STORAGE_MAX_FILE_SIZE` | `104857600` | 최대 파일 크기 (바이트) |
| `STREAMIX_THUMBNAIL_ENABLED` | `true` | 썸네일 생성 활성화 |
| `STREAMIX_THUMBNAIL_WIDTH` | `320` | 썸네일 너비 |
| `STREAMIX_THUMBNAIL_HEIGHT` | `180` | 썸네일 높이 |
| `STREAMIX_THUMBNAIL_FFMPEG_PATH` | `ffmpeg` | FFmpeg 경로 |
| `STREAMIX_API_ENABLED` | `true` | REST API 활성화 |
| `STREAMIX_API_BASE_PATH` | `/api/streamix` | API 기본 경로 |
| `STREAMIX_DASHBOARD_ENABLED` | `true` | 대시보드 활성화 |
| `STREAMIX_DASHBOARD_PATH` | `/streamix` | 대시보드 경로 |

### 서버

| 변수 | 기본값 | 설명 |
|------|--------|------|
| `SERVER_PORT` | `8080` | 서버 포트 |
| `MAX_FILE_SIZE` | `100MB` | 업로드 최대 크기 |
| `MAX_REQUEST_SIZE` | `100MB` | 요청 최대 크기 |

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
- **H2 / PostgreSQL / MySQL** - 데이터베이스 (선택)
- **Thymeleaf** - 대시보드 UI
- **FFmpeg** - 비디오 썸네일 생성

---

## 🔗 관련 프로젝트

- [Streamix](https://github.com/junhyeong9812/Streamix) - 파일 스트리밍 Spring Boot Starter 라이브러리

---

## 📄 License

MIT License