# TournamentIQ

TournamentIQ is a small football tournament manager for the ArtiSyn Implementation Harness demonstration. Create and view tournaments, register teams and fields, and enter matches manually to build a chronological schedule.

## Run with Docker Compose

Install Docker with the Compose plugin, then run this command from the repository root.

```sh
docker compose up --build
```

Open **http://localhost:3000**. The REST API is also available at **http://localhost:8080/api/tournaments**. Once the database and API are ready, Compose starts the web server.

Compose enables the `demo` profile by default. Riverside Cup 2026 has eight fictional teams, three fields, and eight fixed matches on 28–29 November 2026. Westbrook Friendly starts empty. Flyway inserts the same IDs and times into a fresh demo database, and Compose keeps changes in a named PostgreSQL volume. Run `docker compose down` to stop the application and keep the data.

To start with an empty database, use a fresh Compose project or volume and set `SPRING_PROFILES_ACTIVE=default` before running Compose. Changing the profile on an existing database preserves previously seeded records.

## Local development

Use Java 21, Node.js 22.12+ and npm. The included Maven Wrapper downloads the pinned Maven version.

From the repository root, start PostgreSQL.

```sh
docker compose up -d --wait db
```

From `backend/`, start the API.

```sh
SPRING_PROFILES_ACTIVE=demo ./mvnw spring-boot:run
```

In a second terminal, from `frontend/`, start Vite.

```sh
npm ci
npm run dev
```

Open **http://localhost:5173**. Vite proxies API requests to port 8080. The backend defaults to the Compose database on localhost. Set `DATABASE_URL`, `DATABASE_USER`, and `DATABASE_PASSWORD` to use another PostgreSQL instance. The local backend can start with an empty database by omitting the `demo` profile.

## API

Tournament, team, and field creation take a JSON object with a `name` of up to 120 characters. A match takes `homeTeamId`, `awayTeamId`, `fieldId`, `startsAt`, and `endsAt`. Provide times with an explicit UTC offset, for example `2026-11-28T09:00:00Z`. The API returns UTC times and the UI displays them in the browser’s local time zone.

| Method | Endpoint | Purpose |
| --- | --- | --- |
| GET, POST | `/api/tournaments` | List or create tournaments |
| GET | `/api/tournaments/{tournamentId}` | Get a tournament |
| GET, POST | `/api/tournaments/{tournamentId}/teams` | List or register teams |
| GET, POST | `/api/tournaments/{tournamentId}/fields` | List or register fields |
| POST | `/api/tournaments/{tournamentId}/matches` | Create a match |
| GET | `/api/tournaments/{tournamentId}/schedule` | List matches by start time |

The API checks that teams differ, that the selected teams and field belong to the tournament, and that the end follows the start. Error responses use `application/problem+json` with a machine-readable `code` and an `errors` array of field messages where applicable.

## Structure and tests

- `backend/` contains a Spring Boot modular monolith organized around tournaments, teams, fields, and matches. Feature packages contain API, application, and persistence code, with match rules in the match domain package.
- `backend/src/main/resources/db/` contains Flyway schema migrations and the profile-controlled demo seed. Hibernate validates the resulting schema.
- `frontend/` contains the React, TypeScript, Vite, and Tailwind CSS interface.

Run `./mvnw test` in `backend/` for the JUnit 5 unit and REST integration tests. Integration tests require Docker and start their own PostgreSQL instance through Testcontainers. Run `npm run build` in `frontend/` to type-check and build the web app.

## Harness and architecture

Use the [documentation index](docs/README.md) for architecture, component references and engineering conventions. In Codex, start `$dev` with an implementation task, `$design` with a design question, or `$evolve reflect` with a reusable process observation.
