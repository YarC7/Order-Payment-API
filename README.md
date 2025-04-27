# Spring Payment API

## Overview

Spring Payment API là một dự án Spring Boot cung cấp các REST API để quản lý đơn hàng (Orders) và thanh toán (Payments).

Spring Payment API is a Spring Boot project providing REST APIs for managing Orders and Payments.

---

## Getting Started

### Prerequisites

- Java 17+
- Maven (hoặc sử dụng script `mvnw` đi kèm / or use the included `mvnw` script)

### Run the Application

```sh
./mvnw spring-boot:run
```
hoặc / or
```sh
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại / The application will run at:  
[http://localhost:8080](http://localhost:8080)

---

## API Documentation

### Orders

- **Create Order**
  - `POST /api/orders`
  - Request body:
    ```json
    {
      "customerName": "John Doe",
      "customerEmail": "john.doe@example.com",
      "amount": 299.99,
      "currency": "USD"
    }
    ```
- **Get Order By ID**
  - `GET /api/orders/{id}`

### Payments

- **Process Bank Payment**
  - `POST /api/payments`
  - Request body:
    ```json
    {
      "orderId": 1,
      "paymentMethod": "BANK",
      "bankName": "Example Bank",
      "accountNumber": "12345678"
    }
    ```
- **Process Cash Payment**
  - `POST /api/payments`
  - Request body:
    ```json
    {
      "orderId": 1,
      "paymentMethod": "CASH",
      "receiptNumber": "RCPT-123456"
    }
    ```
- **Get Payment By ID**
  - `GET /api/payments/{paymentId}`

- **Get Payments By Order ID**
  - `GET /api/payments/order/{orderId}`

- **Update Payment Status**
  - `PATCH /api/payments/{paymentId}/status?status=COMPLETED`

---

## Testing with Postman

- Import file `postman_collection.json` vào Postman để thử các API mẫu.
- Import the `postman_collection.json` file into Postman to try sample APIs.

---

## Notes

- Ứng dụng sử dụng H2 (in-memory) database mặc định, dữ liệu sẽ mất khi tắt ứng dụng.
- The application uses H2 (in-memory) database by default, data will be lost when the app stops.
