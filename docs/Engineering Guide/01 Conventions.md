---
id: 028f0622
type: Guide
title: TournamentIQ engineering conventions
description: Match the project structure, API behavior, database migrations and local checks.
tags: [engineering-guide, java, typescript, flyway]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
---

# TournamentIQ engineering conventions

| Scope | Practice | Evidence | Authority |
| --- | --- | --- | --- |
| Application | Keep Java controllers, services and repositories in feature packages. Keep manual match validity in the match domain. | TournamentService and MatchAssignment | Observed structure |
| API | Bean Validation checks request fields. Report input failures with Problem Details. | NameRequest and ApiErrors | Exercised by TournamentApiIntegrationTest |
| Data | Scope registrations and matches to the tournament. Apply every schema change through Flyway and keep Hibernate in validate mode. | application.yml and V1__create_tournament_schema.sql | Database constraints and startup validation |
| Time | Use instants in Java and timestamptz in PostgreSQL. The browser supplies and displays its local time. | MatchController and dates.ts | REST and browser behavior |
| Web | Route REST through api.ts. Keep loading, empty and failure states visible in the React workspace. | App.tsx and api.ts | Observed structure |
| Dependencies | Build with Java 21 and the Maven Wrapper for backend. Use npm and the lockfile for frontend. | pom.xml, mvnw and package-lock.json | Build configuration |
| Git | Use Conventional Commits with optional scope. A new Dev feature may use feat/<local-id>-<slug>. | Baseline commit and AGENTS.md | Existing commit convention and Harness workflow |
| Guarded work | Get explicit authorization for the named commit, push, history rewrite, deployment or persistent data deletion. | AGENTS.md | Project interaction rule |

## Verify changes

Use `./mvnw test` from backend with Docker for PostgreSQL integration tests. Use `npm ci` and `npm run build` from frontend. Validate Compose with `docker compose config --quiet`. Use the repository [setup instructions](<../../README.md>) for a cross-container smoke check.
