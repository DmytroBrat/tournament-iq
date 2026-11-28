---
type: Design Stack
title: TQ-001 Constraint-Based Round-Robin Schedule Generation Stack
description: Records the existing technologies that implement and verify generated match scheduling without a new runtime dependency.
tags: [design, stack, scheduling, verification]
generated: { by: process:design-solution, at: 2026-11-28T02:51:22Z }
updated: { by: process:design-solution, at: 2026-11-28T02:51:22Z }
---

# TQ-001 — Constraint-Based Round-Robin Schedule Generation Stack

Generated scheduling uses the existing Web, API and PostgreSQL stack without adding a solver, service or runtime dependency.

## Technology responsibilities

| Technology | Selected capability | Scope and compatibility | Verification |
| --- | --- | --- | --- |
| Java 21 standard library | Immutable scheduling values, Berger fixture construction, deterministic CSP backtracking, time normalization and independent validation | Pure domain code has no Spring or JPA dependency. `java.time` supplies `Instant`, `LocalDate`, `LocalTime` and `ZoneId`. | Maven unit tests exercise fixture counts, deterministic replay, search outcomes, daylight-saving rejection and schedule permutations. |
| Spring Boot 3.5.16 | Additive REST endpoints, Jakarta validation, RFC 9457 Problem Details mapping and application-service transaction boundaries | The API remains one Spring Boot application. Existing manual endpoint request, response and validation behavior stay compatible. | Spring Boot integration tests verify endpoint status, payload and rollback contracts. |
| Spring Data JPA | Pessimistic tournament-row lock, tournament-scoped reloads and batch match persistence | Generated save, manual match creation and relevant team or football-field registration writes use the same tournament-first lock order. | Testcontainers races generated, manual and registration writes against one tournament. |
| PostgreSQL 17 | Tournament-row serialization, `TIMESTAMPTZ` match persistence and atomic commit or rollback | The existing schema stores generated matches. Instants are normalized to PostgreSQL microsecond precision before comparison. No table, column, index or Flyway migration is added. | PostgreSQL 17 integration tests verify lock conflicts, all-or-nothing inserts and timestamp round trips. |
| React 19.2, TypeScript 5.9 and Vite 7 | Scheduling configuration, preview, explicit save and structured Problem Details states in the existing Web application | The same-origin REST path and browser-local match presentation remain. The client preserves the top-level problem code and repeated violation array. | The TypeScript build checks contracts; Web tests cover loading, preview, save, conflict refresh and actionable failures. |
| JUnit 5 and Testcontainers | Domain, API, persistence and concurrency evidence | Versions remain managed by the Spring Boot dependency platform in the existing Maven build. Tests use the PostgreSQL 17 container already declared by the project. | `./mvnw test` from `backend/` runs unit and integration coverage against disposable PostgreSQL. |
| Docker Compose | Local Web → API → PostgreSQL deployment | [`compose.yaml`](../../../../../../compose.yaml) keeps the current three services, health checks, ports and persistent database volume. | `docker compose up --build` verifies the unchanged deployment topology and end-to-end REST proxy path. |

The backend versions and dependencies are declared in [`backend/pom.xml`](../../../../../../backend/pom.xml). Web versions and the Node `>=22.12.0` build requirement are declared in [`frontend/package.json`](../../../../../../frontend/package.json) and its checked-in lockfile.

## Dependency and deployment constraints

The implementation adds no OR-Tools or other native solver, no external API, no paid service and no additional Docker Compose service.

| Concern | Settled consequence |
| --- | --- |
| API build | Use the Maven Wrapper with Java 21. Spring Boot continues to manage Spring, JUnit and Testcontainers compatibility. |
| Web build | Use npm with the checked-in lockfile and the declared Node engine. TypeScript must pass before Vite produces the static bundle. |
| Database migration | No migration runs for TQ-001 because the selected protocol reuses existing tournament and match rows. Flyway remains authoritative for any future schema change. |
| Configuration | Calibrated domain caps, deterministic work budget, wall-clock safety deadline and lock-wait threshold are versioned non-secret application constants produced by the prerequisite effort. No credential or external configuration source is introduced. |
| Licensing and cost | The change introduces no package, service or usage-based cost. Existing project dependency licenses and local resource ownership remain unchanged. |
| Rollback | Roll back the API and Web application images together. No database rollback is required; a generated match schedule uses the existing match representation and remains readable by the baseline schedule endpoint. |

## Acceptance checks

| Check | Required result |
| --- | --- |
| Dependency diff | Backend and Web manifests contain no solver, native runtime or external-service client added for scheduling. |
| API verification | Maven unit and integration tests pass on Java 21 with PostgreSQL 17 Testcontainers. |
| Web verification | The npm build passes with the checked-in lockfile and preserves every structured violation. |
| Deployment verification | Docker Compose builds and starts the existing three-service topology with no migration or new environment secret. |
| Compatibility verification | Existing manual match, team registration, football field registration and match-schedule tests remain green after joining the shared lock protocol. |

The integrated contracts and runtime boundaries are authoritative in [Architecture](Architecture.md). Component behavior is detailed in [Fixture and Placement Strategy](<Design - Fixture and Placement Strategy.md>), [Validation and Failure Model](<Design - Validation and Failure Model.md>) and [Preview and Persistence Protocol](<Design - Preview and Persistence Protocol.md>).
