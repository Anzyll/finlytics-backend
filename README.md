# 📊 Finlytics Backend – Finance Data Processing API

A role-based backend system for managing financial records and providing analytics for a company-wide finance dashboard.

This project demonstrates backend design, data modeling, access control, and API development using Spring Boot.

---

## 🚀 Live Demo

* 📘 Swagger Docs: `https://finlytics-backend-production.up.railway.app/swagger-ui/index.html#/`

---

## 🧠 Overview

This backend powers a **centralized finance dashboard** where multiple users interact with shared financial data based on their roles.

It supports:

* Financial record management
* Role-based access control
* Dashboard analytics
* Filtering and search capabilities

---

## 🏗️ Tech Stack

* Java 21
* Spring Boot
* Spring Security (JWT)
* PostgreSQL
* JPA / Hibernate
* Swagger (OpenAPI)
* Lombok

---

## 🔐 Authentication & Authorization

JWT-based authentication with role-based access control.

### Roles:

* **ADMIN**

  * Full access (create, update, delete records, manage users)

* **ANALYST**

  * Can view records and access detailed analytics

* **VIEWER**

  * Can only view dashboard summaries (no access to detailed records)

---

## 📦 Features

### 1. User & Auth

* User registration and login
* JWT token generation
* Secure API access

---

### 2. Financial Records

* Create, update, delete records (ADMIN only)
* View records (ADMIN, ANALYST)
* Fields:

  * amount
  * type (INCOME / EXPENSE)
  * category
  * date
  * notes

---

### 3. Filtering & Search

* Filter by:

  * type
  * category
  * date range
* Search across:

  * notes
  * category name
* Pagination support

---

### 4. Dashboard APIs

* Total income
* Total expense
* Net balance
* Category-wise summary
* Top expense categories
* Recent transactions

---

### 5. Access Control

Role-based access is enforced using Spring Security:

* ADMIN → full access to all operations
* ANALYST → read access to records and analytics
* VIEWER → limited access to dashboard summaries only

Access is implemented using:

* `@PreAuthorize` annotations
* Role-based endpoint restrictions

---

### 6. Error Handling

* Centralized exception handling using `@ControllerAdvice`
* Custom `AppException` for business logic errors
* Consistent API error responses:

```json
{
  "errorCode": "VALIDATION_ERROR",
  "message": "Validation failed",
  "status": 400,
  "timestamp": "..."
}
```

---

### 7. Audit Logging

Tracks critical actions:

* CREATE
* UPDATE
* DELETE

---

## 📂 API Endpoints (Sample)

### Auth

* `POST /api/auth/register`
* `POST /api/auth/login`

---

### Records

* `GET /api/records` (ADMIN, ANALYST)
* `POST /api/records` (ADMIN)
* `PATCH /api/records/{id}` (ADMIN)
* `DELETE /api/records/{id}` (ADMIN)

---

### Dashboard

* `/api/dashboard/summary` (ADMIN, ANALYST, VIEWER)
* `/api/dashboard/recent` (ADMIN, ANALYST)
* `/api/dashboard/insights` (ADMIN, ANALYST)
* `/api/dashboard/category` (ADMIN, ANALYST, VIEWER)


---

## 🧠 System Design Overview

### 🏗️ Architecture

```text
Controller → Service → Repository → Database
```

* **Controller Layer** → Handles requests and validation
* **Service Layer** → Business logic and access control
* **Repository Layer** → Data access via JPA
* **Database** → PostgreSQL

---

### 🔐 Access Control Strategy

The system uses Role-Based Access Control (RBAC):

* Fine-grained permissions enforced at API level
* Sensitive operations restricted to ADMIN
* Data visibility differs between ANALYST and VIEWER

This ensures:

* Principle of least privilege
* Clear separation of responsibilities

---

### 📊 Data Flow (Example: Fetch Records)

```text
Client Request
   ↓
Controller (validation)
   ↓
Service (business logic)
   ↓
Repository (query execution)
   ↓
Database
   ↓
DTO Response
```

---

### 🔍 Filtering Strategy

* Dynamic filtering using optional query parameters
* JPQL query supports:

  * Conditional filters (`:param IS NULL OR ...`)
  * Case-insensitive search
  * Pagination and sorting

---

### 📦 Data Modeling

**User**

* id, email, password, role, status

**FinancialRecord**

* id, amount, type, category, date, notes

**Category**

* id, name

Relationship:

* FinancialRecord → Many-to-One → Category

---

### ⚡ Performance Considerations

* Pagination limits large responses
* Indexed fields (date, category)
* Lightweight DTO mapping

---

## ⚙️ Setup Instructions

### 1. Clone the repo

```bash
git clone https://github.com/your-username/finlytics-backend.git
cd finlytics-backend
```

---

### 2. Configure environment

Update `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/finlytics_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

---

### 3. Run the application

```bash
./mvnw spring-boot:run
```

---

## 🧪 Assumptions

* All users access shared financial records
* Role-based restrictions define capabilities
* Categories are pre-defined or managed separately

---

## 📈 Future Improvements

* Refresh token implementation
* Caching (Redis)
* Rate limiting
* Unit & integration tests

---

## 📌 Key Highlights

* Clean layered architecture
* Strong separation of concerns
* Role-based access control (RBAC)
* Centralized error handling
* Scalable filtering logic

---

## 👨‍💻 Author

**Muhammed Anzil**
Backend Developer
