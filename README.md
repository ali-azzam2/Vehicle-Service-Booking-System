# Vehicle Service Booking System

A Spring Boot application for managing vehicle service bookings.

## Prerequisites

- Java 17 or higher
- Docker and Docker Compose
- Maven

## Running the Application

### Option 1: Using Docker Compose (Recommended)

This will start both the PostgreSQL database and the Spring Boot application:

```bash
docker-compose up
```

The application will be available at: http://localhost:8080/api

### Option 2: Running Locally

1. Start PostgreSQL:
```bash
docker-compose up db
```

2. Run the Spring Boot application:
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication
- POST `/api/auth/register` - Register a new user
- POST `/api/auth/login` - Login
- POST `/api/auth/logout` - Logout
- GET `/api/auth/current-user` - Get current user info

### Service Requests
- GET `/api/service-requests` - Get all service requests
- POST `/api/service-requests` - Create a new service request
- GET `/api/service-requests/{id}` - Get service request by ID
- PUT `/api/service-requests/{id}` - Update service request
- DELETE `/api/service-requests/{id}` - Delete service request

## Database

The application uses PostgreSQL. The database configuration is:
- Database: CarService
- Username: postgres
- Password: 1234
- Port: 5432

## Security

The application uses session-based authentication. After login, a session cookie (JSESSIONID) will be automatically sent with each request. 