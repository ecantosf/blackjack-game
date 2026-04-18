# Blackjack Game API

A reactive RESTful API for playing Blackjack, built with **Spring Boot WebFlux**, following **Domain-Driven Design (DDD)** and **Hexagonal Architecture** (Ports & Adapters).

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

## 📋 Table of Contents

- [Exercise Description](#exercise-description)
- [Technologies Used](#technologies-used)
- [Requirements](#requirements)
- [Installation Guide](#installation-guide)
- [Execution Guide](#execution-guide)
- [Deployment](#deployment)
- [API Documentation](#api-documentation)
- [Architecture](#architecture)
- [Testing](#testing)

---

## 🎯 Exercise Description

This project implements a complete Blackjack game API with a reactive, non‑blocking architecture designed to handle multiple concurrent players efficiently.

### Core Features

#### Game Management
- Create, retrieve, list, search, rename, and delete players
- Ranking system sorted by win rate
- Player statistics (games played, wins, losses, ties)

#### Game Logic
- Automatic bust detection
- Dealer intelligent logic (stands on 17+)
- Winner determination (Blackjack, Bust, Push)
- Score updates and game statistics
- Ace value calculation (1 or 11)

#### Additional Features
- Input validation with meaningful error messages
- Centralized error handling (@ControllerAdvice)
- Fully reactive, non‑blocking design with Project Reactor
- Domain Events for asynchron

#### Testing
Test Type	Coverage	Tools
- Unit Tests: Domain & Application layers (JUnit 5, Mockito, AssertJ)
- Integration Tests: Repositories & Controllers	Test (containers, WebTestClient)
Architecture Tests	Hexagonal constraints	ArchUnit

Key Testing Practices
- Reactive Testing: StepVerifier for testing Mono/Flux streams
- Testcontainers: Real MongoDB and MySQL containers for integration tests
- Test Data Builders: GameTestBuilder, PlayerTestBuilder for flexible test data
- Isolation: Each test runs with clean database state
- Naming Convention: shouldDoSomethingWhenCondition() pattern

---

## 🛠 Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java 17** | Core programming language |
| **Spring Boot 3.2.4** | Application framework |
| **Spring WebFlux** | Reactive REST API |
| **Project Reactor** | Reactive streams (Mono/Flux) |
| **Spring Data Reactive MongoDB** | Reactive game state persistence |
| **Spring Data R2DBC** | Reactive MySQL access for player data |
| **MySQL 8.0** | Player ranking and statistics |
| **MongoDB 7.0** | Game state storage |
| **Redis Reactive** | Reactive caching for ranking queries |
| **Spring Security** | CORS configuration and security headers |
| **SpringDoc OpenAPI** | Swagger UI automatic API documentation |
| **Lombok** | Boilerplate code reduction |
| **Docker & Docker Compose** | Containerization and multi-service orchestration |
| **JUnit 5 + Mockito** | Unit testing with mocks |
| **StepVerifier** | Reactive streams testing |
| **Testcontainers** | Integration testing with real databases |
| **Maven** | Build automation and dependency management |
| **IntelliJ IDEA Community Edition** | Development IDE |
| **Git & GitHub** | Version control and repository hosting |
| **GitHub Actions** | CI/CD pipeline automation |
| **Render.com** | Cloud deployment platform |

---

## 📦 Requirements

Before you begin, ensure you have the following installed:

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

```bash
git clone https://github.com/ecanf/blackjack-game.git
cd blackjack-game
```

---

## 🤝 Contributing
This is an educational project for learning DDD, Hexagonal Architecture, and Reactive Programming. Suggestions and improvements are welcome!

---

## 📝 License
This project is for educational purposes.

---

## ‍💻 Author
Eduard Cantos Font