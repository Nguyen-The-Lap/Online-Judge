# ByteForge Judge – Kiến trúc hệ thống

## Tổng quan

ByteForge Judge là nền tảng chấm bài trực tuyến fullstack gồm:

- **Backend**: Spring Boot 3, RESTful API, JWT Authentication, JPA + Flyway.
- **Frontend**: Vue 3 (Vite + TypeScript + Pinia + Tailwind).
- **Judge Engine**: Mô phỏng asynchronous execution bằng thread pool, dễ dàng thay thế bằng worker thật.

## Sơ đồ logic cao cấp

```
[Frontend Vue] --axios--> [Spring Boot API] --JPA--> [Database]
                                      \
                                       \--> [JudgeService] --Executor--> [JudgeExecutor]
```

## Thành phần backend

- `config/`
  - `SecurityConfig`: cấu hình Spring Security + JWT.
  - `AsyncConfig`: thread pool chuyên xử lý chấm bài.
  - `DataInitializer`: seed dữ liệu ngôn ngữ mặc định.
  - `JwtProperties`: bind cấu hình JWT.
- `domain/`: entity + enum (User, Problem, Submission,...).
- `repository/`: Spring Data JPA repositories.
- `service/`:
  - `AppUserService`, `ProblemService`, `SubmissionService`, `ProgrammingLanguageService`, `CurrentUserService`.
  - Mapper MapStruct chuyển entity -> DTO.
- `judge/`:
  - `JudgeService`: hàng đợi và lifecycle.
  - `executor/NaiveJudgeExecutor`: giả lập kết quả trên testcases (có thể thay bằng real sandbox).
  - `model/`: DTO nội bộ cho kết quả chấm.
- `web/rest/`: Auth, Problems, Submissions, Admin, System public stats, User profile.
- `web/security/`: JWT filter + user detail.

### Luồng chấm bài

1. Người dùng gửi submission -> `SubmissionService.createSubmission`.
2. Submission được set trạng thái `QUEUED` rồi đưa cho `JudgeService.enqueue`.
3. `JudgeService` chạy async bằng `@Async("judgeExecutor")`, load testcase và gọi `JudgeExecutor`.
4. Kết quả được lưu lại vào bảng `submission_testcase_result`.

## Frontend

- `src/router`: định tuyến, guards dựa trên Pinia store.
- `src/stores/useAuthStore.ts`: quản lý JWT, profile, lưu LocalStorage.
- `src/views`: Home, Auth, Problems, Problem detail + submission, Submission list/detail, Admin dashboard.
- `src/components`: User menu, ProblemCard, SubmissionTable.
- `src/lib/http.ts`: axios instance + helper gắn token.
- Tailwind custom style + các lớp tiện ích `btn-primary`, `card`.

## Database & Migration

- Sử dụng Flyway (`V1__init_schema.sql`) tạo bảng user, problem, testcase, submission.
- Mặc định chạy H2 in-memory (dev). Có thể đổi sang PostgreSQL bằng `application.yml`.

## Test & CI

- `ByteForgeJudgeApplicationTests` đảm bảo context load.
- Hướng dẫn mở rộng: sử dụng Testcontainers (đã khai báo dependency) cho integration test.

## Hướng dẫn mở rộng

- Thay `NaiveJudgeExecutor` bằng worker thực tế (Docker sandbox, gRPC,...).
- Bổ sung bảng contest, scoreboard, thảo luận.
- Tích hợp WebSocket để push trạng thái chấm real-time (`spring-boot-starter-websocket` đã add).
- Kết nối RabbitMQ (đã add starter AMQP) để phân tán job judge.

