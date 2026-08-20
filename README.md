# Habitude -  An AI-Powered Habit Tracking App

## Description

A backend-based habit tracking and activity management system built using Java and Spring Boot. The application provides secure REST APIs for managing users, tracking habit and  activities, and generating recommendations using JWT-based authentication and Spring Security.

## Tech Stack

* Backend: Java, Spring Boot, Spring Security, JWT
* Database: PostgreSQL, Spring Data JPA
* Tools: Postman, Maven, IntelliJ IDEA, Docker

## Features

* User registration and login
* JWT-based authentication and authorization
* Secure REST APIs
* Activity tracking management
* Personalized recommendations
* Role-based access control using Spring Security
* Admin dashboard APIs
* User activity history management

## API Endpoints

### Authentication

* POST /api/auth/register
* POST /api/auth/login
* GET /api/auth/admin/dashboard

### Activities

* POST /api/activities/track
* GET /api/activities/{id}

### Recommendations

* GET /api/recommendations/{userId}

## Sample Request & Response

### POST /api/auth/login

Request:
```json
{
  "email": "test@gmail.com",
  "password": "123456"
}
