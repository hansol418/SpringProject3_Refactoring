# 🛠 ToolWithMe (Web)

공구 이미지 업로드 → AI 분류(ResNet50) → 공구 설명/사용법 + 관련 영상 제공  
Spring Boot(웹/인증/서비스) + Django(DRF 추론) 분리형 이중 서버 아키텍처 기반 AI 분석 웹 서비스

---

## 📋 프로젝트 개요 (Project Overview)

ToolWithMe(Web)는 공구 이미지를 기반으로 공구를 분류하고, 사용자에게 공구 설명/사용법 및 관련 영상을 제공하는 웹 서비스입니다.  
서비스 서버(Spring Boot)와 AI 추론 서버(Django)를 분리 구성하여, 웹 기능(로그인/커뮤니티/마이페이지)과 AI 추론을 독립적으로 운영할 수 있도록 설계했습니다.

---

## ✨ 주요 기능 (Key Features)

### 🔐 로그인/권한 (Authentication)
- JWT 기반 인증 (Access/Refresh Token 발급 및 검증)
- 폼 로그인 + OAuth2 소셜 로그인(카카오) 기반 로그인 흐름
- 인증 필요 페이지 접근 제어 (예: `/users/**`, `/main`)

### 🧠 이미지 분석 (AI Classification)
- 이미지 업로드 후 Django 추론 서버로 분석 요청
- 분류 결과(JSON) 수신 후 아래 정보를 함께 반환:
  - 예측 공구 라벨
  - 공구 설명 (DB 조회)
  - 관련 YouTube 영상 URL
- Spring Boot: `POST /classify` (multipart 이미지 업로드)
- Django: `POST /api/classify/` (ResNet50 추론 후 JSON 응답)

### 🧰 공구 상세/검색
- 예측된 공구명 기반으로 DB에서 설명/사용법 제공

### 🗣 커뮤니티
- 게시글 CRUD / 댓글 CRUD

### 👤 마이페이지
- 회원정보 수정 / 비밀번호 변경 / 계정 탈퇴

---

## 🧩 아키텍처 (Architecture)

### ✅ 분리형 2-Server 구조

**Spring Boot (Main Web Server)**
- 인증(JWT/소셜 로그인), 페이지 렌더링(Thymeleaf), 커뮤니티/마이페이지
- 이미지 업로드 수신 → Django로 전달 → 결과 파싱 → DB 조회/영상 매핑 → 응답

**Django (AI Inference Server)**
- DRF 기반 API
- PyTorch ResNet50 모델 로드 후 추론
- 예측 라벨/확률을 JSON으로 반환

---

## 🛠️ 기술 스택 (Tech Stack)

| 카테고리 | 기술 | 비고 |
|----------|------|------|
| Language | Java, Python, HTML/CSS, JavaScript | |
| Backend | Spring Boot | Java 17 |
| Template | Thymeleaf + Layout Dialect | |
| Security | Spring Security, JWT | Access/Refresh |
| Social Login | OAuth2 Client | Kakao |
| AI Inference | Django REST Framework, PyTorch | ResNet50 |
| DB | MariaDB | JPA 기반 |

---

## 🚀 설치 및 실행 (Installation & Execution)

> 아래는 **로컬 실행** 기준입니다. (Spring ↔ Django 연동)

### 사전 요구사항
- Java 17
- Gradle
- Python 3.10+ 권장
- MariaDB

### 🐍 Django 추론 서버 실행

**1. 의존성 설치**
```bash
pip install django djangorestframework torch torchvision pillow numpy
```

**2. 모델 가중치 준비**

`views.py`에 아래 경로로 가중치를 로드합니다.
```
model_weight_save_path = "DjangoProject3/resnet50_epoch_50_thisreal.pth"
```
해당 `.pth` 파일을 프로젝트 경로에 맞게 배치하거나, `model_weight_save_path`를 실제 경로로 수정하세요.

**3. 서버 실행**
```bash
python manage.py runserver 0.0.0.0:8000
```

---

## 🔗 연동 포인트 (Spring ↔ Django)

Spring Boot의 `ImageClassifyController`에서 Django API를 호출합니다.

| 항목 | 내용 |
|------|------|
| Spring Endpoint | `POST /classify` |
| Django Endpoint | `POST http://localhost:8000/api/classify/` |
| multipart field name | `image` |

> ⚠️ 현재 Spring 코드에는 Django URL이 `localhost:8000`으로 하드코딩되어 있습니다.  
> 배포/환경 분리를 위해서는 환경변수 또는 properties로 분리하는 구성이 권장됩니다.

---

## 🧪 API 동작 개요 (핵심 플로우)

1. 사용자가 이미지 업로드 (`/classify`)
2. Spring Boot가 Django로 이미지 전송 (`/api/classify/`)
3. Django가 ResNet50 추론 후 JSON 응답 반환

```json
{
  "predicted_class_index": 4,
  "predicted_class_label": "드라이버",
  "confidence": 0.83,
  "class_confidences": { "...": 0.01 }
}
```

4. Spring Boot가 예측 라벨로 DB 조회 (tool description)
5. 라벨에 따른 YouTube URL 매핑 후 최종 응답

```json
{
  "predictedLabel": "드라이버",
  "description": "공구 설명 ...",
  "videoUrl": "https://www.youtube.com/embed/...."
}
```

---

## 🐛 트러블슈팅 (Troubleshooting)

### 1) `/classify` 호출 시 500 / 응답 없음
- Django 서버가 8000 포트로 실행 중인지 확인
- Spring 코드의 Django URL이 로컬 주소(`http://localhost:8000/api/classify/`)인지 확인

### 2) 예측 라벨은 나오는데 description이 없음
- `tool` 테이블에 `tool_name`과 `description` 데이터가 존재하는지 확인
- 예측 라벨과 DB의 `tool_name`이 완전히 동일한 문자열인지 확인  
  (예: `"버니어캘리퍼스"` vs `"버니어 캘리퍼스"`처럼 공백 차이)

### 3) 모델 파일 로드 오류
- `.pth` 경로 및 파일 존재 여부 확인
- CPU 환경이면 `map_location=torch.device('cpu')` 설정 유지

---

## ☁️ AWS 배포 (Deployment)

ToolWithMe(Web)는 AWS 환경에 배포하여 운영했습니다.

👉 [AWS 배포 링크](http://52.79.99.241:8080/users/login)

---

## 👤 담당 역할 (My Contribution)

| 기능 | 기여도 |
|------|--------|
| JWT 기반 인증 및 소셜 로그인 기반 사용자 로그인 기능 구현 | 100% |
| 공구 상세/커뮤니티/마이페이지 등 사용자 중심 기능 페이지 개발 | 100% |
| Selenium 기반 이미지 데이터 자동 수집 및 전처리 | 100% |
| Django REST API 기반 실시간 이미지 분류 기능 구현 | 100% |
| Spring Boot ↔ Django HTTP 통신 기반 분석 결과 연동 | 100% |
| PyTorch ResNet 기반 이미지 학습/평가 참여 | 80% |
| AWS EC2 기반 외부 서버 배포 참여 | 80% |

