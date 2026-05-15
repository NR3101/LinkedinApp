# LinkedIn App — Microservices Backend

A LinkedIn-inspired social platform backend built with **Spring Boot microservices**, featuring user authentication with JWT and post management with likes. Each service owns its own PostgreSQL database.

> 🚧 **This project is under active development.**

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5 |
| Database | PostgreSQL (per-service) |
| ORM | Spring Data JPA / Hibernate |
| Auth | JWT (jjwt 0.13) + BCrypt |
| Build | Maven |
| Containerization | Docker Compose |
| Mapping | ModelMapper |
| Validation | Jakarta Bean Validation |

---

## Getting Started

### Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **Docker & Docker Compose**

### 1. Clone & start databases

```bash
git clone https://github.com/<your-username>/LinkedInApp.git
cd LinkedInApp
docker compose up -d
```

### 2. Set environment variables

```bash
export JWT_SECRET_KEY="your-secret-key-here"
```

### 3. Run the services

```bash
# User Service
cd userService && ./mvnw spring-boot:run

# Posts Service (separate terminal)
cd postsService && ./mvnw spring-boot:run
```

---

## Project Structure

```
LinkedInApp/
├── docker-compose.yml
├── userService/          # User & Auth microservice  (:9020)
│   ├── src/main/java/
│   ├── src/main/resources/application.yaml
│   └── pom.xml
├── postsService/         # Posts & Likes microservice (:9010)
│   ├── src/main/java/
│   ├── src/main/resources/application.yaml
│   └── pom.xml
└── README.md
```

---

## License

This project is for educational purposes.
