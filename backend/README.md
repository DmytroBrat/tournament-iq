# TournamentIQ API

The Spring Boot app serves the tournament REST API, validates manual match assignments and runs Flyway on startup.

## Local development

Use Java 21 and start PostgreSQL from the repository root with `docker compose up -d --wait db`. Run `SPRING_PROFILES_ACTIVE=demo ./mvnw spring-boot:run` here. The API is available at `http://localhost:8080/api/tournaments`. Run `./mvnw test` with Docker for the PostgreSQL integration tests.

## Architecture

- [API container](<../docs/Target Architecture (HLD)/v1/Containers/API.md>)
- [Tournament Registrations](<../docs/System Architecture (LLD)/API/Tournament Registrations.md>)
- [Manual Scheduling](<../docs/System Architecture (LLD)/API/Manual Scheduling.md>)
- [Tournament Store](<../docs/System Architecture (LLD)/PostgreSQL/Tournament Store.md>) for Flyway and persistence
