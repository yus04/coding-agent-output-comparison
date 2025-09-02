# User Management REST API

REST API for user management with JWT authentication based on the provided specifications.

## Features

- **Endpoint:** `GET /api/users/{id}`
- **Authentication:** JWT token required
- **Authorization:** Admin role only
- **Response:** JSON with user data (id, name, email, role)
- **Error Handling:** Proper HTTP status codes (200, 401, 403, 404)

## Technical Stack

- **Framework:** Spring Boot 3.1.5
- **Java Version:** 17
- **Security:** JWT tokens with HMAC-SHA384 signing
- **Database:** H2 in-memory
- **ORM:** JPA/Hibernate

## Quick Start

### 1. Build and Run

```bash
# Build and run the application
mvn spring-boot:run
```

The API will start on `http://localhost:8080`

### 2. Generate JWT Tokens

```bash
# Generate test tokens
mvn spring-boot:run -Dspring-boot.run.profiles=token-gen
```

This will output admin and user tokens for testing.

### 3. Test API Endpoints

```bash
# Test with admin token (replace {admin-token} with actual token)
curl -H "Authorization: Bearer {admin-token}" http://localhost:8080/api/users/1

# Test without token (should return 403)
curl http://localhost:8080/api/users/1

# Test with non-existent user (should return 404)
curl -H "Authorization: Bearer {admin-token}" http://localhost:8080/api/users/999
```

## API Documentation

### GET /api/users/{id}

Retrieve user by ID (Admin only).

**Parameters:**
- `id` (path) - User ID (Long, required)

**Headers:**
- `Authorization: Bearer {jwt-token}` (required)

**Responses:**

- **200 OK** - User found and returned
```json
{
  "id": 1,
  "name": "Admin User",
  "email": "admin@example.com", 
  "role": "ADMIN"
}
```

- **401 Unauthorized** - Missing or invalid JWT token
- **403 Forbidden** - Insufficient permissions (non-admin user)
- **404 Not Found** - User not found
```json
{
  "error": "Not Found",
  "message": "User not found with id: 999",
  "timestamp": "2025-09-02T07:00:00.000",
  "status": 404
}
```

## Test Data

The application automatically loads test data:

1. **ID: 1** - Admin User (admin@example.com, ADMIN)
2. **ID: 2** - Regular User (user@example.com, USER)  
3. **ID: 3** - Test User (test@example.com, USER)

## Database Console

For development, H2 console is available at: `http://localhost:8080/h2-console`

- **JDBC URL:** `jdbc:h2:mem:testdb`
- **Username:** `sa`
- **Password:** (empty)