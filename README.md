# Ticket System API

Full Stack application developed as part of a Full Stack Developer technical challenge.

The system allows managing technical support tickets.

---

# Tech Stack

## Backend

- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven

## Frontend

- HTML
- CSS
- JavaScript (Vanilla)
- Fetch API

## Testing

- JUnit 5
- Mockito
- MockMvc

## Documentation

- Swagger / OpenAPI
- Postman Collection

---

## Prerequisites

Before running the project locally you should have:

- JDK 17 installed and JAVA_HOME set
- Maven (or use the bundled wrapper scripts)
- Git to clone the repository

The instructions below assume a Windows shell; use the equivalent commands on macOS/Linux.

---

# Backend Structure

## Project Structure

```text
backend/ticket-api
└── src/main/java/com/christianpires/ticket_api
    ├── controller
    │   └── TicketController
    ├── service
    │   └── TicketService
    ├── repository
    │   └── TicketRepository
    ├── entity
    │   └── Ticket
    ├── dto
    │   ├── CreateTicketRequest
    │   ├── UpdateTicketStatusRequest
    │   └── TicketResponse
    ├── enums
    │   └── TicketStatus
    ├── mapper
    │   └── TicketMapper
    ├── exception
    │   ├── ApiErrorResponse
    │   ├── TicketNotFoundException
    │   └── GlobalExceptionHandler
    ├── config
    │   └── OpenApiConfig
    └── TicketApiApplication
```

---

## Test Structure

```text
src/test/java/com/christianpires/ticket_api
├── controller
│   └── TicketControllerTest
├── service
│   └── TicketServiceTest
├── mapper
│   └── TicketMapperTest
├── exception
│   ├── GlobalExceptionHandlerTest
│   └── TicketNotFoundExceptionTest
├── config
│   └── OpenApiConfigTest
└── TicketApiApplicationTests
```

---

# Frontend Structure

```text
frontend
├── index.html
├── css
│   └── styles.css
└── js
    ├── api.js
    └── app.js
```

---

# Running the Backend

Navigate to:

```bash
cd backend/ticket-api
```

Run the application:

```bash
mvnw.cmd spring-boot:run
```

The API will start at:

```bash
http://localhost:8080
```

---

# Running the Frontend

After starting the backend API, open the frontend.

Navigate to:

```bash
cd frontend
```

Open the file:

```bash
index.html
```

You can open it directly in the browser or use a local server (recommended).

If using VS Code, install the `Live Server` extension and run:

Right click → `Open with Live Server`

The frontend will consume the API at:

```bash
http://localhost:8080
```

---

# API Documentation

Swagger UI is available at:

```bash
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON specification:

```bash
http://localhost:8080/v3/api-docs
```

---

# API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST   | /tickets | Create a new ticket |
| GET    | /tickets | List all tickets |
| GET    | /tickets/{id} | Get ticket details |
| PUT    | /tickets/{id} | Update ticket status |
| DELETE | /tickets/{id} | Delete a ticket |

# Example Request

Create Ticket

```bash
POST /tickets
```

```json
{
  "title": "Printer issue",
  "description": "Printer not working"
}
```

Response example:

```json
{
  "id": 1,
  "title": "Printer issue",
  "description": "Printer not working",
  "status": "OPEN",
  "createdAt": "2026-03-10T17:20:00"
}
```

---

# Running Tests

Run all unit tests:

```bash
mvnw.cmd test
```

Or on Git Bash:

```bash
./mvnw test
```

Tests include:

- Service layer tests
- Controller tests using MockMvc
- Mapper tests
- Exception handler tests
- Application context test

---

# Postman Collection

A Postman collection is included to test the API endpoints.

Import the collection located at:

```bash
docs/postman/ticket-system-api.postman_collection.json
```

Set the base URL:

```bash
http://localhost:8080
```

The collection contains requests for:

- Creating tickets
- Listing tickets
- Retrieving a ticket by ID
- Updating ticket status
- Deleting tickets

---

# Author

Christian Pires
