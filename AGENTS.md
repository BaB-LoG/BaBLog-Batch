# Repository Guidelines (BaBLog-Batch)

## Project Structure & Module Organization
- `src/main/java/com/ssafy/bablog`: Spring Batch 엔트리(`BaBLogBatchApplication.java`)와 배치 계층(`batch/*`)이 위치합니다.
- 배치 실행은 `batch/config/BatchJobConfig.java`와 `batch/scheduler/BatchJobScheduler.java`를 기준으로 합니다.
- Core 모듈의 도메인/서비스를 재사용하므로 비즈니스 로직은 주로 Core에 존재합니다.

## Build, Test, and Development Commands
- `./mvnw clean package`: 배치 애플리케이션 빌드.
- `./mvnw spring-boot:run`: 로컬 배치 실행(스케줄러 동작 포함).
- `./mvnw test`: 배치 테스트 실행.

## Batch Jobs & Scheduling
- 잡 구성: `dailyMealInitJob`, `dailyReportJob`, `weeklyReportJob`.
- 스케줄러:
  - 일일 식단 생성: 00:00 (당일 기준)
  - 일간 리포트 생성: 00:05 (전날 기준)
  - 주간 리포트 생성: 월요일 00:15 (전주 월~일 기준)
- 대상 사용자: `MemberIdProvider`가 전체 회원 ID를 조회.
- 실패 처리: `batch_failure_log` 테이블에 실패 내역 기록.

## Data & Configuration
- 설정 파일: `src/main/resources/application.yml` (gitignore에 포함될 수 있음).
- Mapper XML: `src/main/resources/mapper/BatchFailureLogMapper.xml`.
- 배치는 Core의 MyBatis/서비스에 의존하므로 DB 스키마는 `BaBLog-BE/src/main/resources/static/DDL.sql`를 참고합니다.

## Coding Style & Naming Conventions
- 표준 Spring 관례(클래스 PascalCase, 메서드 camelCase).
- 들여쓰기 4칸, 탭 사용 금지.
- Tasklet은 `tasklet/`, 배치 서비스는 `batch/service/`에 배치합니다.

## Testing Guidelines
- 배치 테스트는 JUnit 5를 사용합니다.
- 잡/스텝 단위 테스트는 `spring-batch-test`를 사용해 최소 범위로 유지합니다.

## Security & Configuration Tips
- 배치 설정에 DB/AI 키 등이 포함될 수 있으니 커밋/공유 시 주의하세요.
