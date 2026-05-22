FitSync - A Fitness Tracking & Recommendation Backend System
Description

FitSync is a backend fitness application built using Java and Spring Boot that helps users track activities, manage fitness data, and receive personalized recommendations. The application provides secure JWT-based authentication and role-based authorization with fully documented REST APIs using Swagger/OpenAPI.

Tech Stack
Backend
Java
Spring Boot
Spring Security
JWT Authentication
Database
MySQL
Spring Data JPA
API Documentation
Swagger / OpenAPI 3
Tools
Maven
Postman
IntelliJ IDEA
Features
User Registration and Login
JWT-based Authentication & Authorization
Secure REST APIs
Activity Tracking System
Fitness Recommendation Module
Admin Protected Dashboard
Swagger/OpenAPI API Documentation
Layered Architecture using DTOs and Services
API Documentation

Swagger UI:

http://localhost:8080/swagger-ui/index.html

OpenAPI Docs:

http://localhost:8080/v3/api-docs
API Endpoints
Authentication
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/admin/dashboard
Activity Management
POST /api/activities/track
GET  /api/activities/{id}
Recommendation Module
POST /api/recommendations
GET  /api/recommendations/{id}
Sample Request & Response
POST /api/auth/login
Request
{
  "email": "user@gmail.com",
  "password": "123456"
}
Response
{
  "token": "jwt_token"
}
Security Features
JWT Token Authentication
Password Encryption
Protected APIs
Role-Based Access Control (RBAC)
Secure Admin Routes
Project Structure
src/main/java
│
├── controller/       → Handles REST API requests
├── service/          → Business logic layer
├── repository/       → Database access layer
├── model/            → Entity classes
├── dto/              → Request & Response DTOs
├── security/         → JWT & Spring Security configuration
├── config/           → Application configurations
└── exception/        → Global exception handling
Setup Instructions
1. Clone Repository
git clone https://github.com/yourusername/fitsync.git
2. Open in IDE
IntelliJ IDEA / Eclipse
3. Configure Database

Update application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/fitsync
spring.datasource.username=root
spring.datasource.password=yourpassword
4. Run Application
mvn spring-boot:run
5. Visit Swagger UI
http://localhost:8080/swagger-ui/index.html
Future Improvements
Add Docker Support
Implement Redis Caching
Add Unit & Integration Testing
Deploy using AWS / Render
Add Pagination & Filtering
Improve Recommendation Algorithm
Add Email Verification
Author
Alok Vishwakarma

GitHub:
GitHub Profile
