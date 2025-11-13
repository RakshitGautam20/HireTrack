# HireTrack - Job Application Tracker API

A RESTful backend API for tracking job applications, built with Spring Boot and PostgreSQL.

## Features

- ✅ User management with email as unique identifier
- ✅ Create, read, update, and delete job applications
- ✅ UUID-based application identifiers
- ✅ Status tracking with enum values (APPLIED, INTERVIEW, REJECTED, OFFERED, HIRED)
- ✅ Filter applications by user and status
- ✅ Pagination support for users and applications
- ✅ CSV export for user's job applications
- ✅ Daily email reminders for 'APPLIED' applications (cron job)
- ✅ Automatic timestamp management (createdAt, updatedAt)
- ✅ Swagger UI for interactive API documentation
- ✅ PostgreSQL database (supports Supabase, Render, or local)

## Quick Start

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL database 

### Setup

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd HireTrack
   ```

2. **Configure environment variables**
   
   Copy `app.env` and set your database credentials:
   
   **PowerShell:**
   ```powershell
   $env:DATABASE_URL="jdbc:postgresql://your-host:5432/your-database?sslmode=require"
   $env:DATABASE_USERNAME="your_username"
   $env:DATABASE_PASSWORD="your_password"
   ```
   
   **Or set them in your IDE's run configuration.**

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access Swagger UI**
   - Open: https://amusing-embrace-production.up.railway.app/swagger-ui
   - Interactive API documentation and testing

## API Endpoints

### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users (paginated) |
| GET | `/api/users/{email}` | Get user by email |
| POST | `/api/users` | Create new user |
| PUT | `/api/users/{email}` | Update user |
| DELETE | `/api/users/{email}` | Delete user |

### Job Application Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/applications/all` | Get all applications (paginated) |
| GET | `/api/applications/user/{userEmail}` | Get all applications for a user |
| GET | `/api/applications/status/{status}` | Get all applications by status |
| GET | `/api/applications/user/{userEmail}/status/{status}` | Get user applications by status |
| POST | `/api/applications?userEmail={email}` | Create application for a user |
| PATCH | `/api/applications/{id}/status?status={status}&userEmail={email}` | Update application status |
| DELETE | `/api/applications/{id}?userEmail={email}` | Delete application |
| GET | `/api/applications/user/{userEmail}/export/csv` | Export user applications as CSV |

**Status Values:** `APPLIED`, `INTERVIEW`, `REJECTED`, `OFFERED`, `HIRED`

### Example Requests

**Create User:**
POST /api/users
```json
{
  "email": "john.doe@example.com",
  "name": "John Doe"
}
```

**Create Application:**
POST /api/applications?userEmail=john.doe@example.com

```json
{
  "company": "Google",
  "position": "Software Engineer",
  "status": "APPLIED",
  "appliedDate": "2024-01-15",
  "notes": "Great company culture"
}
```

**Update Application Status:**
```
PATCH /api/applications/{uuid}/status?status=INTERVIEW&userEmail=john.doe@example.com
```

## Technology Stack

- **Spring Boot 3.5.8** - Java framework
- **Spring Data JPA** - Database abstraction
- **PostgreSQL** - Relational database
- **SpringDoc OpenAPI** - API documentation
- **Lombok** - Reduces boilerplate code
- **Maven** - Build tool

## Documentation

- **Swagger UI**: https://amusing-embrace-production.up.railway.app/swagger-ui
- **API Docs**: https://amusing-embrace-production.up.railway.app/v3/api-docs

## License

This project is open source and available under the MIT License.
