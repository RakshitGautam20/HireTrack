# Quick Start Guide - How to Run HireTrack

## Prerequisites

Before running the application, make sure you have:

1. **Java 21** installed
   ```bash
   java -version
   ```
   Should show version 21 or higher

2. **Maven 3.6+** installed
   ```bash
   mvn -version
   ```

3. **PostgreSQL 12+** installed and running
   ```bash
   # Check if PostgreSQL is running
   psql --version
   ```

## Step-by-Step Setup

### Step 1: Start PostgreSQL

Make sure PostgreSQL is running on your system:

**Windows:**
- Check Services (services.msc) for "PostgreSQL" service
- Or use pgAdmin to start the server

**Mac/Linux:**
```bash
# Check if PostgreSQL is running
sudo systemctl status postgresql
# Or start it
sudo systemctl start postgresql
```

### Step 2: Create the Database

Connect to PostgreSQL and create the database:

```bash
# Connect to PostgreSQL (default user is usually 'postgres')
psql -U postgres

# Create the database
CREATE DATABASE hiretrack;

# Exit psql
\q
```

**Alternative using pgAdmin:**
- Open pgAdmin
- Right-click on "Databases" → Create → Database
- Name: `hiretrack`
- Click Save

### Step 3: Configure Database Connection

Edit `src/main/resources/application.properties` and update these lines if needed:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/hiretrack
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD
```

**Or set environment variables:**
```bash
# Windows (PowerShell)
$env:DATABASE_URL="jdbc:postgresql://localhost:5432/hiretrack"
$env:DATABASE_USERNAME="postgres"
$env:DATABASE_PASSWORD="your_password"

# Windows (CMD)
set DATABASE_URL=jdbc:postgresql://localhost:5432/hiretrack
set DATABASE_USERNAME=postgres
set DATABASE_PASSWORD=your_password

# Mac/Linux
export DATABASE_URL="jdbc:postgresql://localhost:5432/hiretrack"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="your_password"
```

### Step 4: Build the Project (Optional but Recommended)

```bash
# Clean and build the project
mvn clean install

# Or skip tests for faster build
mvn clean install -DskipTests
```

### Step 5: Run the Application

**Option 1: Using Maven (Recommended)**
```bash
mvn spring-boot:run
```

**Option 2: Using the JAR file**
```bash
# First build the JAR
mvn clean package

# Then run it
java -jar target/HireTrack-0.0.1-SNAPSHOT.jar
```

**Option 3: Using your IDE**
- Open the project in IntelliJ IDEA, Eclipse, or VS Code
- Find `HireTrackApplication.java` in `src/main/java/com/example/HireTrack/`
- Right-click → Run 'HireTrackApplication'

### Step 6: Verify the Application is Running

You should see output like:
```
Started HireTrackApplication in X.XXX seconds
```

The application will be available at:
- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/api/applications/all (should return empty array `[]`)

## Testing the Application

### 1. Access Swagger UI

Open your browser and go to:
```
http://localhost:8080/swagger-ui.html
```

You'll see all available endpoints with interactive documentation.

### 2. Create a User (Using Swagger UI)

1. In Swagger UI, find the **Users** section
2. Click on `POST /api/users`
3. Click "Try it out"
4. Enter the request body:
```json
{
  "email": "john.doe@example.com",
  "name": "John Doe"
}
```
5. Click "Execute"
6. You should see a 201 response with the created user

### 3. Create a Job Application

1. In Swagger UI, find the **Job Applications** section
2. Click on `POST /api/applications`
3. Click "Try it out"
4. Set `userEmail` query parameter: `john.doe@example.com`
5. Enter the request body:
```json
{
  "company": "Google",
  "position": "Software Engineer",
  "status": "APPLIED",
  "appliedDate": "2024-01-15",
  "notes": "Applied through referral"
}
```
6. Click "Execute"

### 4. Test Other Endpoints

- **Get all applications**: `GET /api/applications/all`
- **Get user's applications**: `GET /api/applications/user/john.doe@example.com`
- **Get by status**: `GET /api/applications/status/APPLIED`
- **Update status**: `PATCH /api/applications/{id}/status`

## Troubleshooting

### Port 8080 Already in Use

If you see "Port 8080 is already in use":

**Option 1: Change the port**
Edit `application.properties`:
```properties
server.port=8081
```

**Option 2: Kill the process using port 8080**

Windows:
```powershell
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Mac/Linux:
```bash
lsof -ti:8080 | xargs kill -9
```

### Database Connection Error

**Error**: `Connection refused` or `FATAL: password authentication failed`

**Solutions:**
1. Verify PostgreSQL is running
2. Check username/password in `application.properties`
3. Verify database `hiretrack` exists
4. Check PostgreSQL is listening on port 5432:
   ```bash
   # Windows
   netstat -an | findstr 5432
   
   # Mac/Linux
   netstat -an | grep 5432
   ```

### Java Version Error

**Error**: `Unsupported class file major version`

**Solution**: Make sure you have Java 21 installed:
```bash
java -version
# Should show: openjdk version "21" or higher
```

If you have multiple Java versions, set JAVA_HOME:
```bash
# Windows
set JAVA_HOME=C:\Program Files\Java\jdk-21

# Mac/Linux
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
```

### Maven Build Fails

**Error**: Dependency download issues

**Solution**: 
```bash
# Clean Maven cache and rebuild
mvn clean
mvn dependency:purge-local-repository
mvn install
```

## Next Steps

Once the application is running:

1. **Explore Swagger UI** - Test all endpoints interactively
2. **Create some test data** - Add users and applications
3. **Check the database** - Verify data is being stored:
   ```sql
   psql -U postgres -d hiretrack
   SELECT * FROM users;
   SELECT * FROM job_applications;
   ```

## Stopping the Application

Press `Ctrl + C` in the terminal where the application is running.

