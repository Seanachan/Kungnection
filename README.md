# Kungnection Backend

A Spring Boot + MySQL backend for a chat application, featuring JWT authentication, user/channel management, and friend chat functionality.

## Features
- User registration and login (JWT-based authentication)
- Channel creation, joining, and membership management
- Friend system with one-on-one chat rooms
- Message sending and retrieval (channels and friend chats)
- RESTful API endpoints
- Integration and controller tests

## Tech Stack
- Java 17
- Spring Boot 3.2.5
- MySQL 8
- Gradle (Kotlin DSL)
- Lombok
- JJWT (JSON Web Token)

## Getting Started

### Prerequisites
- Java 17+
- Docker (for MySQL, or install MySQL manually)
- Gradle (or use the included `./gradlew` wrapper)

### 1. Start MySQL (Docker)
You can start a MySQL container with the provided Gradle task:

```sh
./gradlew startMysql
```

Or manually:
```sh
docker run --name kungnection -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=kungnection -p 3306:3306 -d mysql:latest
```

### 2. Configure Database
Edit `src/main/resources/application.properties` if you need to change DB credentials.

### 3. Initialize Schema & Data
The app will auto-run `schema.sql` and `data.sql` on startup. Test users are included.

### 4. Build & Run
```sh
./gradlew bootRun
```

### 5. Run Tests
```sh
./gradlew test
```

## API Testing
- Use Postman or curl to test endpoints (see controller classes for routes)
- Example login:
```sh
curl -X POST -H "Content-Type: application/json" -d '{"username":"tester1","password":"testpassword"}' http://localhost:8080/auth/login
```

## Project Structure
- `src/main/java/org/kungnection/` - Main Java source
- `src/main/resources/` - Config, schema, and seed data
- `src/test/java/org/kungnection/` - Tests

## Useful Gradle Tasks
- `./gradlew bootRun` - Run the Spring Boot app
- `./gradlew test` - Run all tests
- `./gradlew startMysql` - Start MySQL via Docker

## Notes
- Lombok is used for model boilerplate; enable annotation processing in your IDE.
- JWT secret and DB credentials are for development only—change for production.

## License
MIT
