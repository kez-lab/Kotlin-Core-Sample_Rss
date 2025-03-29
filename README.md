# KotlinRPC 멀티플랫폼 뉴스 리더 앱

이 프로젝트는 Kotlin Multiplatform과 KotlinRPC를 활용하여 만든 실시간 뉴스 리더 애플리케이션입니다. RSS 피드를 통해 최신 뉴스를 표시하며, Compose Multiplatform으로 다양한 플랫폼(Android, iOS, Desktop, Web)에서 동일한 UI/UX를 제공합니다.

## 프로젝트 핵심 기능

- **KotlinRPC**: 클라이언트-서버 간 타입 안전한 Remote Procedure Call(RPC) 통신
- **Compose Multiplatform**: 단일 코드베이스로 여러 플랫폼에 UI 제공
- **Ktor Server**: 백엔드 서버 구현 및 RSS 피드 파싱
- **클린 아키텍처**: 관심사 분리를 통한 유지보수성 높은 코드 구조
- **코루틴 기반 비동기 처리**: 효율적인 네트워크 통신 및 UI 업데이트

## 프로젝트 구조

### 클라이언트 측 (composeApp)

- **ui/screen/**: 뉴스 목록 화면 및 UI 컴포넌트
- **network/**: 네트워크 통신 관련 코드 (Repository 패턴 적용)
  - `NewsRepository`: 뉴스 데이터 접근을 위한 인터페이스
  - `RpcNewsRepository`: KotlinRPC를 활용한 구현체
  - `FakeNewsRepository`: 테스트용 목(Mock) 구현체
- **model/**: 데이터 모델
- `NewsViewModel`: UI 상태 관리 및 비즈니스 로직
- `AppConfig`: 개발/배포 환경 설정

### 서버 측 (server)

- `Application.kt`: Ktor 서버 설정 및 RSS 피드 갱신 로직
- `service/NewsService`: 클라이언트에 뉴스 데이터 제공을 위한 RPC 서비스
- `model/Rss.kt`: RSS 데이터 파싱을 위한 모델 클래스

### 공통 모듈 (shared)

- `service/NewsService`: RPC 인터페이스 정의
- `model/News.kt`: 공통 데이터 모델

## 기술 스택

- **언어**: Kotlin
- **백엔드**: Ktor
- **UI 프레임워크**: Compose Multiplatform
- **통신**: KotlinRPC, WebSocket
- **비동기 처리**: Kotlin Coroutines
- **의존성 관리**: Gradle (KMM)
- **parse**: Kotlinx Serialization

## 실행 방법

### 개발 환경

```bash
# 서버 실행 (로컬 개발)
./gradlew server:run

# 안드로이드 앱 실행
./gradlew :composeApp:installDebug

# 데스크톱 앱 실행
./gradlew :composeApp:run

# 웹 앱 실행 (Kotlin/Wasm)
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

### 환경 설정

개발 환경과 프로덕션 환경 전환은 HttpClient 의 host, post 값을 수정하여 설정할 수 있습니다.

## 통합 아키텍처

이 프로젝트는 Kotlin Multiplatform을 활용한 통합 아키텍처를 구현하여 다음과 같은 이점을 제공합니다:

- **단일 언어 풀스택 개발**: Kotlin을 사용하여 Android, iOS, 웹, 데스크톱, 서버 모두를 개발
- **공통 코어 로직 공유**: 비즈니스 로직과 데이터 모델을 모든 플랫폼에서 재사용
- **타입 안전한 API 통신**: kotlinx.rpc를 통해 클라이언트-서버 간 타입 안전한 통신 구현
- **UI 코드 통합**: Compose Multiplatform으로 모든 플랫폼에서 일관된 UI/UX 제공

이 아키텍처는 "한 번 작성하고, 어디서든 실행(Write Once, Run Anywhere)"하는 이상을 Kotlin 생태계 내에서 실현합니다. 모바일 앱, 웹, 서버를 아우르는 하나의 코드베이스로, 개발 생산성과 유지보수성을 크게 향상시킵니다.

### 계층 구조

1. **프레젠테이션 계층**: Compose Multiplatform으로 모든 플랫폼 UI 통합
2. **비즈니스 로직 계층**: ViewModel과 Repository 인터페이스로 상태 관리 및 데이터 접근
3. **데이터 접근 계층**: Repository 구현체와 RPC 클라이언트로 데이터 소스 추상화
4. **네트워크 계층**: kotlinx.rpc로 타입 안전한 서버-클라이언트 통신 구현
5. **백엔드 계층**: Ktor 서버와 RPC 서비스로 데이터 제공

이 다계층 구조는 테스트 가능성을 높이고 확장성을 제공하며, 모듈 간 명확한 경계를 설정하여 코드의 안정성을 향상시킵니다.

## 확장성

이 프로젝트는 다음과 같은 방식으로 확장될 수 있습니다:

- 추가 데이터 소스 지원 (다양한 뉴스 API 통합)
- 오프라인 캐싱 구현
- 사용자 인증 및 개인화된 뉴스 피드
- 다크 모드 및 테마 지원

## 라이선스

MIT License

## 기여하기

이슈와 풀 리퀘스트는 언제나 환영합니다. 기여에 관심이 있으시다면, 먼저 이슈로 논의해주세요!