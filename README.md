# User API - REST API for User Management

A Spring Boot REST API implementation based on detailed specifications for user management with JWT authentication.

## Features

- **GET /api/users/{id}** - Retrieve user by ID
- JWT-based authentication and authorization
- Input validation for path parameters
- Comprehensive error handling (400, 401, 403, 404)
- H2 in-memory database with JPA/Hibernate
- Complete test coverage

## API Specification

### Endpoint
- **URL**: `GET /api/users/{id}`
- **Method**: GET
- **Path Parameter**: `id` (Long, 0以上, required)
- **Authentication**: JWT Bearer token required

### Request Example
```bash
GET /api/users/123
Authorization: Bearer {JWT}
```

### Response Examples

#### Success (200 OK)
```json
{
  "id": 123,
  "name": "田中 太郎",
  "email": "tanaka@example.com",
  "role": "admin"
}
```

#### Error Responses
| Status | Response | Description |
|--------|----------|-------------|
| 404 | `{"error": "User not found"}` | User does not exist |
| 401 | `{"error": "Unauthorized"}` | Authentication failed |
| 403 | `{"error": "Forbidden"}` | Insufficient permissions |
| 400 | `{"error": "Invalid ID"}` | Invalid ID format or negative value |

## Database Schema

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(255),
    role VARCHAR(20)
);
```

## Running the Application

### Prerequisites
- Java 17+
- Maven 3.6+

### Start the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Generate JWT Token (for testing)
```bash
curl -X POST "http://localhost:8080/auth/token" \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "role": "admin"}'
```

### Test the API
```bash
# Get JWT token
TOKEN=$(curl -s -X POST "http://localhost:8080/auth/token" \
  -H "Content-Type: application/json" \
  -d '{"username": "testuser", "role": "admin"}' | jq -r '.token')

# Test successful request
curl -X GET "http://localhost:8080/api/users/1" \
  -H "Authorization: Bearer $TOKEN"

# Test user not found
curl -X GET "http://localhost:8080/api/users/999" \
  -H "Authorization: Bearer $TOKEN"

# Test unauthorized access
curl -X GET "http://localhost:8080/api/users/1"

# Test invalid ID
curl -X GET "http://localhost:8080/api/users/abc" \
  -H "Authorization: Bearer $TOKEN"
```

## Running Tests

```bash
mvn test
```

All tests validate the API behavior including:
- Successful user retrieval
- User not found scenarios
- Authentication and authorization
- Input validation
- Error handling

## Technology Stack

- **Spring Boot 3.2.0** - Application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database access layer
- **H2 Database** - In-memory database
- **JWT (JJWT)** - JSON Web Token implementation
- **JUnit 5** - Testing framework

## Project Structure

```
src/
├── main/
│   ├── java/com/example/userapi/
│   │   ├── controller/          # REST controllers
│   │   ├── entity/             # JPA entities
│   │   ├── exception/          # Custom exceptions and handlers
│   │   ├── repository/         # Data repositories
│   │   ├── security/           # Security configuration and JWT utilities
│   │   └── UserApiApplication.java
│   └── resources/
│       ├── application.properties
│       └── data.sql            # Sample data
└── test/
    └── java/com/example/userapi/
        └── controller/         # API tests
```