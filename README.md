# HireTrack - Job Application Tracker API

A RESTful backend API for tracking job applications, built with Spring Boot and PostgreSQL.

## Features

- ✅ User management with email as unique identifier
- ✅ Create, read, update, and delete job applications
- ✅ User-specific job application tracking (multi-tenant support)
- ✅ UUID-based application identifiers
- ✅ Status enum with predefined values (APPLIED, INTERVIEW, REJECTED, OFFERED, HIRED)
- ✅ Search applications by company, position, or keyword
- ✅ Filter applications by status
- ✅ Track essential details: company, position, status, applied date, notes
- ✅ PostgreSQL database for reliable data storage
- ✅ **Swagger UI** for interactive API documentation and testing

## API Endpoints

### User Management

#### Get All Users
```
GET /api/users
```

#### Get User by Email
```
GET /api/users/{email}
```

#### Create User
```
POST /api/users
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "name": "John Doe"
}
```

#### Update User
```
PUT /api/users/{email}
Content-Type: application/json

{
  "email": "john.doe@example.com",
  "name": "John Smith"
}
```

#### Delete User
```
DELETE /api/users/{email}
```

### Job Application Management

**Note:** All job application endpoints require a `userEmail` query parameter to identify the user.

#### Get All Applications
```
GET /api/applications?userEmail={email}
```

Query Parameters:
- `userEmail` (required): User's email address
- `status` (optional): Filter by status (`APPLIED`, `INTERVIEW`, `REJECTED`, `OFFERED`, `HIRED`)
- `search` (optional): Search by company or position keyword

Example:
```
GET /api/applications?userEmail=john.doe@example.com&status=APPLIED
GET /api/applications?userEmail=john.doe@example.com&search=Google
```

#### Get Application by ID
```
GET /api/applications/{uuid}?userEmail={email}
```

**Note:** Application ID is a UUID (e.g., `550e8400-e29b-41d4-a716-446655440000`)

#### Create Application
```
POST /api/applications?userEmail={email}
Content-Type: application/json

{
  "company": "Google",
  "position": "Software Engineer",
  "status": "APPLIED",
  "appliedDate": "2024-01-15",
  "notes": "Great company culture"
}
```

**Status Values:** `APPLIED`, `INTERVIEW`, `REJECTED`, `OFFERED`, `HIRED`

#### Update Application
```
PUT /api/applications/{uuid}?userEmail={email}
Content-Type: application/json

{
  "company": "Google",
  "position": "Software Engineer",
  "status": "INTERVIEW",
  "appliedDate": "2024-01-15",
  "notes": "Interview scheduled"
}
```

#### Delete Application
```
DELETE /api/applications/{uuid}?userEmail={email}
```

**Note:** Application ID is now a UUID instead of a numeric ID.

## Local Development

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL 12+

### Setup

1. **Clone the repository**
   ```bash
   git clone <your-repo-url>
   cd HireTrack
   ```

2. **Create PostgreSQL database**
   ```sql
   CREATE DATABASE hiretrack;
   ```

3. **Configure database connection**
   
   Update `src/main/resources/application.properties` or set environment variables:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/hiretrack
   spring.datasource.username=postgres
   spring.datasource.password=your_password
   ```

4. **Run the application**
   
   **Using Maven:**
   ```bash
   mvn spring-boot:run
   ```
   
   **Or build and run the JAR:**
   ```bash
   mvn clean package
   java -jar target/HireTrack-0.0.1-SNAPSHOT.jar
   ```
   
   **Or run from your IDE:**
   - Open `HireTrackApplication.java`
   - Right-click → Run 'HireTrackApplication'

   The API will be available at `http://localhost:8080`

5. **Verify the application is running**
   
   You should see: `Started HireTrackApplication in X.XXX seconds`
   
   Test the API:
   - Open browser: http://localhost:8080/swagger-ui.html
   - Or test endpoint: http://localhost:8080/api/applications/all

6. **Access Swagger UI**
   
   Once the application is running, you can access the interactive API documentation at:
   - **Swagger UI**: http://localhost:8080/swagger-ui.html
   - **OpenAPI JSON**: http://localhost:8080/v3/api-docs
   
   Swagger UI provides an interactive interface where you can:
   - View all available endpoints
   - See request/response schemas
   - Test API endpoints directly from the browser
   - View example requests and responses

**For detailed setup instructions, see [QUICK_START.md](QUICK_START.md)**

## Deployment on Render

### Step 1: Prepare for Deployment

1. **Create a `render.yaml` file** (optional, for easier setup):
   ```yaml
   services:
     - type: web
       name: hiretrack-api
       env: java
       buildCommand: mvn clean package -DskipTests
       startCommand: java -jar target/HireTrack-0.0.1-SNAPSHOT.jar
       envVars:
         - key: DATABASE_URL
           fromDatabase:
             name: hiretrack-db
             property: connectionString
         - key: DATABASE_USERNAME
           fromDatabase:
             name: hiretrack-db
             property: user
         - key: DATABASE_PASSWORD
           fromDatabase:
             name: hiretrack-db
             property: password
         - key: PORT
           value: 8080
         - key: JAVA_OPTS
           value: -Xmx512m

   databases:
     - name: hiretrack-db
       databaseName: hiretrack
       user: hiretrack_user
   ```

2. **Update application.properties for Render**
   
   The application is already configured to use environment variables:
   - `DATABASE_URL` - Full PostgreSQL connection URL
   - `DATABASE_USERNAME` - Database username
   - `DATABASE_PASSWORD` - Database password
   - `PORT` - Server port (Render sets this automatically)

### Step 2: Deploy on Render

1. **Create a Render account** at [render.com](https://render.com)

2. **Create a PostgreSQL Database**
   - Go to Dashboard → New → PostgreSQL
   - Name it `hiretrack-db`
   - Note the connection details

3. **Create a Web Service**
   - Go to Dashboard → New → Web Service
   - Connect your GitHub repository
   - Configure:
     - **Name**: `hiretrack-api`
     - **Environment**: `Java`
     - **Build Command**: `mvn clean package -DskipTests`
     - **Start Command**: `java -jar target/HireTrack-0.0.1-SNAPSHOT.jar`
     - **Plan**: Free (or choose a paid plan)

4. **Set Environment Variables**
   - `DATABASE_URL`: Your PostgreSQL connection string
     ```
     jdbc:postgresql://<host>:<port>/<database>?sslmode=require
     ```
   - `DATABASE_USERNAME`: Your database username
   - `DATABASE_PASSWORD`: Your database password
   - `PORT`: `8080` (or leave empty, Render sets this)
   - `JAVA_OPTS`: `-Xmx512m` (optional, for memory management)

5. **Deploy**
   - Click "Create Web Service"
   - Render will build and deploy your application
   - Your API will be available at `https://hiretrack-api.onrender.com` (or your custom domain)

### Step 3: Test the API

Once deployed, test your API:
```bash
curl https://your-app.onrender.com/api/applications
```

## Project Structure

```
src/main/java/com/example/HireTrack/
├── HireTrackApplication.java          # Main application class
├── controller/
│   ├── JobApplicationController.java  # Job application REST API endpoints
│   └── UserController.java            # User management REST API endpoints
├── service/
│   ├── JobApplicationService.java    # Job application business logic
│   └── UserService.java               # User management business logic
├── repository/
│   ├── JobApplicationRepository.java  # Job application data access layer
│   └── UserRepository.java           # User data access layer
├── model/
│   ├── JobApplication.java           # Job application entity
│   └── User.java                     # User entity (email as unique ID)
└── exception/
    └── GlobalExceptionHandler.java    # Exception handling
```

## Technology Stack

- **Spring Boot 3.5.8** - Java framework
- **Spring Data JPA** - Database abstraction
- **PostgreSQL** - Relational database
- **Lombok** - Reduces boilerplate code
- **Maven** - Build tool

## License

This project is open source and available under the MIT License.

