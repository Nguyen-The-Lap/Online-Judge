# ByteForge Judge

Nền tảng chấm bài trực tuyến fullstack (Java + Vue) được thiết kế như một repository GitHub hoàn chỉnh.

## Kiến trúc

- **Backend**: Spring Boot 3, Java 21, Spring Security (JWT), JPA, Flyway, MapStruct.
- **Frontend**: Vue 3 + Vite + TypeScript + Pinia + TailwindCSS.
- **Judge Engine**: `JudgeService` + `NaiveJudgeExecutor` mô phỏng xử lý bất đồng bộ, dễ nâng cấp thành sandbox thực tế.

Chi tiết xem `docs/architecture.md`.

## Yêu cầu hệ thống

- Java 21, Maven 3.9+
- Node.js 18+, pnpm/npm/yarn

## Hướng dẫn chạy

```bash
# Backend
cd backend
mvn spring-boot:run

# Frontend (tab khác)
cd frontend
npm install
npm run dev
```

Ứng dụng frontend chạy tại `http://localhost:5173`, backend API tại `http://localhost:8080/api`.

## Cấu hình môi trường

- Mặc định sử dụng H2 in-memory cho phát triển.
- Đổi sang PostgreSQL bằng cách thiết lập biến môi trường:

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/byteforge
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=secret
```

## API chính

- `POST /api/auth/register|login`: xác thực JWT.
- `GET /api/problems`, `GET /api/problems/{slug}`, `POST /api/problems`: quản lý bài toán (role `ADMIN`/`PROBLEM_SETTER`).
- `POST /api/submissions`: gửi bài (cần đăng nhập).
- `GET /api/submissions`, `GET /api/submissions/{id}`: xem bài nộp của bản thân.
- `GET /api/system/stats`: số liệu tổng quan (public).
- `GET /api/admin/stats`: thống kê dành cho admin.

## Seed dữ liệu

- `DataInitializer` tự động tạo các ngôn ngữ Java, Python, C++ khi khởi động lần đầu.
- Tạo tài khoản admin bằng cách đăng ký và cập nhật role trực tiếp trong DB (hoặc mở rộng `AppUserService`).

## Kiểm thử

```bash
cd backend
mvn test
```

## Lộ trình mở rộng

- Thêm worker chấm bài thực tế (Docker sandbox, RabbitMQ).
- Realtime status qua WebSocket/SSE.
- Module contest, ranking, editorial.

---

Made with ♥ for competitive programming teams.

