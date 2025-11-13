# API Examples

## User Management

### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "name": "John Doe"
  }'
```

### Get All Users

```bash
curl http://localhost:8080/api/users
```

### Get User by Email

```bash
curl http://localhost:8080/api/users/john.doe@example.com
```

### Update User

```bash
curl -X PUT http://localhost:8080/api/users/john.doe@example.com \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "name": "John Smith"
  }'
```

### Delete User

```bash
curl -X DELETE http://localhost:8080/api/users/john.doe@example.com
```

## Job Application Management

**Note:** All job application endpoints require a `userEmail` query parameter.

### Create a Job Application

```bash
curl -X POST "http://localhost:8080/api/applications?userEmail=john.doe@example.com" \
  -H "Content-Type: application/json" \
  -d '{
    "company": "Google",
    "position": "Software Engineer",
    "status": "APPLIED",
    "appliedDate": "2024-01-15",
    "notes": "Applied through referral. Great company culture."
  }'
```

**Note:** Status must be one of: `APPLIED`, `INTERVIEW`, `REJECTED`, `OFFERED`, `HIRED`

### Get All Applications for a User

```bash
curl "http://localhost:8080/api/applications?userEmail=john.doe@example.com"
```

### Get Applications by Status

```bash
curl "http://localhost:8080/api/applications?userEmail=john.doe@example.com&status=APPLIED"
```

**Note:** Status values: `APPLIED`, `INTERVIEW`, `REJECTED`, `OFFERED`, `HIRED`

### Search Applications

```bash
curl "http://localhost:8080/api/applications?userEmail=john.doe@example.com&search=Google"
```

### Get Application by ID

```bash
curl "http://localhost:8080/api/applications/550e8400-e29b-41d4-a716-446655440000?userEmail=john.doe@example.com"
```

**Note:** ID is now a UUID instead of a number

### Update Application

```bash
curl -X PUT "http://localhost:8080/api/applications/550e8400-e29b-41d4-a716-446655440000?userEmail=john.doe@example.com" \
  -H "Content-Type: application/json" \
  -d '{
    "company": "Google",
    "position": "Software Engineer",
    "status": "INTERVIEW",
    "appliedDate": "2024-01-15",
    "notes": "Interview scheduled for next week"
  }'
```

### Delete Application

```bash
curl -X DELETE "http://localhost:8080/api/applications/550e8400-e29b-41d4-a716-446655440000?userEmail=john.doe@example.com"
```

## Example Responses

### User Response

```json
{
  "email": "john.doe@example.com",
  "name": "John Doe",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

### Job Application Response

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "company": "Google",
  "position": "Software Engineer",
  "status": "APPLIED",
  "appliedDate": "2024-01-15",
  "notes": "Applied through referral. Great company culture.",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00",
  "user": {
    "email": "john.doe@example.com",
    "name": "John Doe",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

**Status Values:**
- `APPLIED` - Application submitted
- `INTERVIEW` - Interview scheduled/in progress
- `REJECTED` - Application rejected
- `OFFERED` - Job offer received
- `HIRED` - Offer accepted (hired)

