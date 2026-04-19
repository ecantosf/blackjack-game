# Blackjack Game API

A reactive RESTful API for playing Blackjack, built with **Spring Boot WebFlux**, following 
**Domain-Driven Design (DDD)** and **Hexagonal Architecture** (Ports & Adapters).

[![Java Version](https://img.shields.io/badge/Java-21-blue.svg)](https://jdk.java.net/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-Reactive-green.svg)](https://spring.io/projects/spring-webflux)
[![Maven](https://img.shields.io/badge/Maven-3.8+-orange.svg)](https://maven.apache.org/)
[![MongoDB](https://img.shields.io/badge/MongoDB-7.0+-green.svg)](https://www.mongodb.com/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![R2DBC](https://img.shields.io/badge/R2DBC-Reactive-red.svg)](https://r2dbc.io/)
[![Docker](https://img.shields.io/badge/Docker-24.0+-blue.svg)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 🎯 Exercise Description

This project implements a complete Blackjack game API with a reactive, non‑blocking architecture designed to handle multiple concurrent players efficiently.

### Core Features

#### Game Management
- Create a new game session
- Retrieve current game state
- Execute game moves (HIT, STAND)
- Delete game sessions

#### Game Logic
- Automatic bust detection
- Dealer intelligent logic (stands on 17+)
- Winner determination (Blackjack, Bust, Push)
- Score updates and game statistics
- Ace value calculation (1 or 11)

#### Player Management
- Create, retrieve, list, search, rename, and delete players
- Ranking system sorted by win rate
- Player statistics (games played, wins, losses, ties)

#### Additional Features
- Input validation with meaningful error messages
- Centralized error handling (`@ControllerAdvice`)
- Fully reactive, non‑blocking design with Project Reactor
- Domain Events for asynchronous processing

---

## 🛠 Technologies

| Technology | Purpose |
|------------|---------|
| **Java 21** | Core language |
| **Spring Boot 3.2.4** | Application framework |
| **Spring WebFlux** | Reactive REST API |
| **Project Reactor** | Reactive streams (Mono/Flux) |
| **MongoDB** | Game state storage (reactive) |
| **MySQL + R2DBC** | Player data & ranking (reactive) |
| **Redis** (optional) | Reactive caching |
| **SpringDoc OpenAPI** | Swagger UI documentation |
| **Docker** | Containerization |
| **JUnit 5 + Mockito** | Testing |

---

## 📦 Requirements

- **JDK 21** (or later)
- **Maven 3.8+**
- **MongoDB 7.0+** (local or Docker)
- **MySQL 8.0+** (local or Docker)
- **Git** (for version control)
- **IntelliJ IDEA** (recommended) or any Java IDE
- **Docker & Docker Compose** (optional, for containerized databases)

---

## 📥 Installation Guide

### 1. Clone the repository

```
git clone https://github.com/ecanf/blackjack-game.git
cd blackjack-game
```

### 2. Run with Docker Compose (recommended)

```bash
docker-compose up -d --build
```

This will start:
- MongoDB on port 27017
- MySQL on port 3307
- Redis on port 6379
- Blackjack API on port 8080

### 3. Verify application is running

```bash
curl http://localhost:8080/actuator/health
```

### 4. Swagger UI

Open your browser at: http://localhost:8080/swagger-ui.html

---

## 🏗 Architecture
Hexagonal Architecture (Ports & Adapters)

┌─────────────────────────────────────────────────────────────┐
│                      INFRASTRUCTURE                         │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐          │
│  │ Controller  │  │   MongoDB   │  │    MySQL    │          │
│  │ (Incoming)  │  │  (Outgoing) │  │  (Outgoing) │          │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘          │
│         │                │                │                 │
│         ▼                ▼                ▼                 │
│  ┌─────────────────────────────────────────────────────┐    │
│  │              APPLICATION LAYER                      │    │
│  │         (Handlers: CreateGame, Play, etc.)          │    │
│  └─────────────────────────┬───────────────────────────┘    │
│                            │                                │
│                            ▼                                │
│  ┌─────────────────────────────────────────────────────┐    │
│  │                  DOMAIN LAYER                       │    │
│  │   Game (Aggregate) | Player (Entity) | Value Objects│    │
│  └─────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘

---

## 📁 Package Structure

src/main/java/cat/opteams/blackjack/
├── application/           # Use cases
│   ├── command/          # Input DTOs
│   ├── handler/          # Command/Query handlers
│   ├── mapper/           # Response mappers
│   ├── query/            # Query DTOs
│   └── validator/        # Fail-fast validation
├── domain/               # Core business logic
│   ├── event/            # Domain Events
│   ├── model/            # Aggregates, Entities, Value Objects
│   ├── port/outgoing/    # Interfaces (ports)
│   └── service/          # Domain Services
├── infrastructure/       # Adapters
│   ├── adapter/
│   │   ├── incoming/web/ # REST controllers
│   │   └── outgoing/     # MongoDB, MySQL, Redis adapters
│   ├── config/           # Security, CORS configuration
│   └── filter/           # CorrelationIdFilter
└── shared/               # Shared utilities
    └── exception/        # Exception hierarchy

---

## 📁 Project Structure

blackjack-game-api/
├── src/
│   ├── main/
│   │   ├── java/cat/opteams/blackjack/
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/cat/opteams/blackjack/
│           ├── domain/
│           ├── application/
│           ├── infrastructure/
│           ├── e2e/
│           └── testutil/
├── pom.xml
├── README.md
└── run-tests.sh / run-tests.bat

---

## 📚 API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/games` | Create a new game |
| GET | `/games/{id}` | Get game details |
| POST | `/games/{id}/play` | HIT or STAND action |
| DELETE | `/games/{id}` | Delete a game |
| PATCH | `/players/{playerId}/name` | Update player name |
| GET | `/ranking` | Get player ranking |
| GET | `/actuator/health` | Health check |

---

## 🧪 Testing

### Test Status

| Metric | Value |
|--------|-------|
| **Total tests** | 109 |
| **Passing** | 105 (96.3%) |
| **Known failures** | 4 (mock configuration issues) |

### Coverage

| Layer | Coverage | Target |
|-------|----------|--------|
| Domain | 98% | ≥ 95% ✅ |
| Application | 94% | ≥ 90% ✅ |
| Infrastructure | 87% | ≥ 80% ✅ |
| **Overall** | **92%** | ≥ 85% ✅ |

### Run tests

```bash
# All tests
mvn test
```

#### Coverage report
```mvn jacoco:report```
#### Open: target/site/jacoco/index.html

---

## 📝 License
This project is for educational purposes as part of an Advanced Spring Framework course.

---

## ‍👨‍💻 Author
Eduard Cantos Font
GitHub: @ecanf


## 🤝 Contributing
This is an educational project for learning DDD, Hexagonal Architecture, and Reactive Programming. 
Suggestions and improvements are welcome!

