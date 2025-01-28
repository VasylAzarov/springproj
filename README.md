# SpringProj

## Introduction
SpringProj is a robust backend solution designed for e-commerce platforms. It offers seamless integration with modern tools to handle user authentication, product management, and order processing efficiently. This project was inspired by the need for a scalable and secure backend that adheres to best practices in software development.

## Features
- **Authentication**:
    - User registration and login via JWT.
    - Role-based access control.
- **Product Management**:
    - Manage products, categories, and inventory.
- **Order Management**:
    - Handle customer orders and shopping carts.
- **API Documentation**:
    - Interactive Swagger UI for exploring endpoints.

## Technology Stack
- **Spring Boot 3.4.1**: Core framework.
- **Spring Security**: For secure authentication and authorization.
- **Spring Data JPA**: Simplified database interactions.
- **MySQL**: Relational database.
- **JWT**: Token-based authentication.
- **MapStruct**: Object mapping.
- **Liquibase**: Database migrations.
- **SpringDoc OpenAPI**: API documentation.
- **Testcontainers**: Integration testing with MySQL.

## Installation

### Prerequisites
- JDK 21
- Maven
- MySQL Server

### Steps
1. Clone the repository:
   ```bash
   git clone <https://github.com/VasylAzarov/springproj.git>
   cd springproj
   ```
2. Configure the database in `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/springproj_db?createDatabaseIfNotExist=true
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -jar target/springproj-1.0-SNAPSHOT.jar
   ```
5. Access the API:
    - Base URL: `http://localhost:8080/api`
    - Swagger UI: `http://localhost:8080/api/swagger-ui.html`
    - Docker URL: `http://localhost:8081/api/`
    - Docker Swagger UI: `http://localhost:8081/api/swagger-ui.html`

## Controllers

### AuthenticationController
- **Endpoints**:
    - `/auth/registration`: Register a new user.
    - `/auth/login`: Authenticate and receive a JWT.

### BookController
- **Endpoints**:
    - Manage book-related operations.

### CategoryController
- **Endpoints**:
    - CRUD operations for product categories.

### OrderController
- **Endpoints**:
    - Manage customer orders.

### ShoppingCartController
- **Endpoints**:
    - Add, update, or remove items in the shopping cart.

## API Documentation
This project uses SpringDoc OpenAPI for API documentation. Visit the Swagger UI at `http://localhost:8080/api/swagger-ui.html` to explore the endpoints.

## Challenges
- Implementing secure JWT authentication and ensuring tokens are properly validated.
- Managing database migrations with Liquibase while maintaining data integrity.
- Configuring Testcontainers for isolated integration testing.

## Video overview
[link to loom video](https://www.loom.com/share/99573a9ff3fe4c54a6c9f1f227a0468a?sid=6e4874d7-3854-470e-8f5e-b139da6d9467)