# DỰ ÁN DEVOPS – JAVA SPRING BOOT

## 1. Giới thiệu dự án

Dự án này xây dựng một ứng dụng REST API quản lý công việc đơn giản bằng Java Spring Boot, đồng thời triển khai quy trình DevOps từ đóng gói ứng dụng, kiểm thử tự động, kiểm tra bảo mật đến giám sát và phân tích lỗi.

**Mục tiêu của dự án:**
- Đóng gói ứng dụng Java bằng Docker.
- Quản lý các container bằng Docker Compose.
- Tự động hóa quy trình kiểm thử và kiểm tra bảo mật bằng GitHub Actions.
- Phát hiện và xử lý lỗ hổng bảo mật bằng Trivy.
- Thu thập logs, metrics và traces để hỗ trợ debugging.
- Phân tích nguyên nhân gốc rễ và đề xuất biện pháp phòng ngừa lỗi.

## 2. Công nghệ sử dụng

| Thành phần | Công nghệ |
|---|---|
| Ngôn ngữ lập trình | Java 21 |
| Framework | Spring Boot 4.1.2 |
| Công cụ build | Apache Maven |
| Kiểm thử | JUnit 5 |
| Đóng gói ứng dụng | Docker |
| Quản lý container | Docker Compose |
| Tích hợp liên tục (CI) | GitHub Actions |
| Kiểm tra bảo mật | Trivy |
| Thu thập logs | SLF4J, Logback |
| Giám sát metrics | Spring Boot Actuator, Micrometer |
| Distributed Tracing | Micrometer Tracing, Zipkin |
| Quản lý mã nguồn | Git và GitHub |

## 3. Chức năng ứng dụng

Ứng dụng cung cấp các REST API cơ bản:

| Phương thức | Endpoint | Chức năng |
|---|---|---|
| GET | `/tasks` | Lấy danh sách công việc |
| POST | `/tasks` | Thêm một công việc mới |
| GET | `/actuator/health` | Kiểm tra trạng thái ứng dụng |
| GET | `/actuator/prometheus` | Cung cấp metrics giám sát |

Ví dụ lấy danh sách công việc:

```bash
curl http://localhost:8082/tasks
```

Thêm công việc mới:

```bash
curl -X POST http://localhost:8082/tasks \
  -H "Content-Type: text/plain" \
  -d "Learn DevOps"
```

**Lưu ý:** Dữ liệu công việc hiện được lưu trong bộ nhớ RAM của ứng dụng và sẽ mất khi container khởi động lại.

## 4. Cấu trúc dự án

```text
devops-lab/
├── .github/
│   └── workflows/
│       └── ci.yml
├── src/
│   ├── main/
│   └── test/
├── evidence/
│   ├── app-logs.txt
│   └── metrics.txt
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── mvnw
├── RCA.md
└── README.md
```

## 5. Hướng dẫn cài đặt và chạy ứng dụng

### 5.1. Yêu cầu

Máy tính cần có Git, Java 21, Docker Desktop và kết nối Internet để tải các dependency cần thiết.

### 5.2. Tải mã nguồn

```bash
git clone https://github.com/yahan2001/devops-lab.git
cd devops-lab
```

### 5.3. Build và kiểm thử ứng dụng

```bash
chmod +x mvnw
./mvnw clean package
```

Lệnh trên sẽ biên dịch ứng dụng, chạy các bài kiểm thử và đóng gói thành file JAR.

### 5.4. Khởi động các container

```bash
docker compose up -d --build
```

Kiểm tra:

```bash
docker compose ps
```

Ứng dụng Spring Boot sử dụng cổng `8082`, còn Zipkin sử dụng cổng `9411`.

### 5.5. Kiểm tra ứng dụng

```bash
curl http://localhost:8082/actuator/health
```

Nếu ứng dụng hoạt động bình thường, kết quả sẽ chứa:

```json
{
  "status": "UP"
}
```

## 6. Quy trình CI/CD với GitHub Actions

Dự án sử dụng GitHub Actions để tự động chạy quy trình kiểm tra mỗi khi có sự kiện push hoặc pull request.

Quy trình gồm hai job chính:

### 6.1. JUnit Test Gate

- Tải mã nguồn từ GitHub.
- Thiết lập môi trường Java 21.
- Biên dịch và chạy kiểm thử Maven/JUnit.
- Lưu kết quả kiểm thử.
- Dừng quy trình tiếp theo nếu kiểm thử thất bại.

### 6.2. Docker Security Gate

- Đóng gói ứng dụng Spring Boot.
- Build Docker image.
- Quét lỗ hổng bảo mật bằng Trivy.
- Chặn image nếu phát hiện lỗ hổng thuộc ngưỡng chính sách bảo mật.

Chính sách quét được cấu hình cho mức độ HIGH và CRITICAL, bỏ qua những lỗ hổng chưa có bản sửa.

Xem kết quả thực tế tại:

[GitHub Actions – DevOps Lab](https://github.com/yahan2001/devops-lab/actions)

## 7. Giám sát và hỗ trợ debugging

### 7.1. Logs

Ứng dụng sử dụng SLF4J và Logback để ghi nhận các hoạt động và lỗi.

Xem logs:

```bash
docker compose logs app
```

Bằng chứng đã được lưu tại `evidence/app-logs.txt`.

### 7.2. Metrics

Spring Boot Actuator và Micrometer cung cấp các metrics như:

- Mức sử dụng bộ nhớ JVM.
- Số luồng Java.
- Thời gian xử lý Garbage Collection.
- Mức sử dụng CPU.
- Thời gian hoạt động của ứng dụng.

Truy cập:

http://localhost:8082/actuator/prometheus

Bằng chứng: `evidence/metrics.txt`.

### 7.3. Distributed Tracing

Ứng dụng sử dụng Micrometer Tracing để gửi dữ liệu trace đến Zipkin.

Truy cập giao diện:

http://localhost:9411

Các HTTP request đã được kiểm chứng:

- `GET /tasks`
- `POST /tasks`
- `GET /actuator/health`

Zipkin hiển thị thông tin trace, span và thời gian xử lý request.

## 8. Sự cố bảo mật và phân tích nguyên nhân gốc rễ

Trong quá trình triển khai CI, Trivy phát hiện bốn lỗ hổng mức HIGH.

### 8.1. Nguyên nhân

Docker base image ban đầu:

```dockerfile
FROM eclipse-temurin:21-jre
```

chứa file thực thi `/usr/bin/pebble`, được biên dịch bằng Go và sử dụng thư viện chuẩn Go phiên bản `v1.27.1` có các lỗ hổng đã được ghi nhận.

### 8.2. Cách khắc phục

Chuyển Docker base image sang phiên bản dựa trên Ubuntu 24.04 LTS:

```dockerfile
FROM eclipse-temurin:21-jre-noble
```

Sau đó build lại ứng dụng và quét bảo mật.

### 8.3. Kết quả

- Docker image được build thành công.
- File thực thi Pebble có lỗ hổng không còn trong image mới.
- Trivy không phát hiện lỗ hổng HIGH/CRITICAL có bản vá theo chính sách quét.
- Các lượt chạy GitHub Actions sau khi khắc phục đều thành công.

### 8.4. Biện pháp phòng ngừa

- Thường xuyên cập nhật Docker base image.
- Duy trì kiểm thử và kiểm tra bảo mật tự động trong CI.
- Không bỏ qua lỗi bảo mật chỉ để pipeline thành công.
- Lưu trữ logs và kết quả quét phục vụ phân tích sự cố.
- Kiểm tra lại toàn bộ ứng dụng sau mỗi lần cập nhật.

Xem báo cáo chi tiết tại [RCA.md](RCA.md).

## 9. Kết quả thực hiện

Dự án đã hoàn thành các nội dung chính:

- Xây dựng REST API Java Spring Boot.
- Đóng gói và chạy ứng dụng trong Docker.
- Quản lý dịch vụ bằng Docker Compose.
- Kiểm thử tự động bằng JUnit.
- Thiết lập GitHub Actions với test gate và security gate.
- Phát hiện và khắc phục lỗ hổng Docker image.
- Thu thập logs, metrics và traces.
- Viết báo cáo phân tích nguyên nhân gốc rễ (RCA).

Các bằng chứng triển khai có trong thư mục [evidence](evidence/) và lịch sử chạy GitHub Actions.

## 10. Kết luận

Dự án giúp thực hành quy trình DevOps với Java Spring Boot, từ phát triển ứng dụng, kiểm thử tự động, đóng gói container đến kiểm soát bảo mật và giám sát vận hành.

Thông qua sự cố Trivy, dự án cũng minh họa cách sử dụng bằng chứng kỹ thuật để xác định nguyên nhân, đưa ra biện pháp khắc phục và xác minh hiệu quả của giải pháp.

**Mã nguồn:** [GitHub – Java Spring Boot DevOps Lab](https://github.com/yahan2001/devops-lab)