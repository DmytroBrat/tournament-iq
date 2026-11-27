# TournamentIQ

## Why

TournamentIQ is a local football tournament manager and the demonstration project for the ArtiSyn Implementation Harness. Its baseline covers tournament setup, team and field registration, manually assigned matches, and schedule viewing.

Use the Harness to carry decisions and verification through locally recorded stories. The baseline keeps match entry manual. Automatic scheduling, fixture generation, optimization and minimum-rest rules belong to separately scoped work.

## What

### Conventions

Read [Conventions](<docs/Engineering Guide/01 Conventions.md>) for evidence-backed project rules.

- Use Conventional Commits with an optional scope. When starting a feature story, the Dev lifecycle proposes `feat/<local-id>-<slug>`.
- Use the Maven Wrapper and Java 21 for the API. Use npm and the checked-in lockfile for the web application.
- Use Flyway migrations for schema changes. Use local or disposable PostgreSQL instances for experiments and checks.
- Keep credentials out of tracked files, reports and prompts. Use environment references when configuration needs a credential.
- Git commits, pushes, history rewrites, deployment and deletion of persistent data require explicit authorization for the action. Otherwise give the developer the command and target.

### Architecture

| Container | Responsibility | Stack |
| --- | --- | --- |
| Web | Tournament workspace, registration forms, and schedule | React, TypeScript, Vite, Tailwind CSS, Nginx |
| API | Feature-scoped tournament REST endpoints and match rules | Java 21, Spring Boot, Spring Data JPA, Flyway |
| PostgreSQL | Durable tournament data, registered teams and fields, and manual matches | PostgreSQL 17 |

The browser sends same-origin requests to Nginx or the local Vite proxy. The proxy forwards REST calls to Spring Boot, which uses JPA and PostgreSQL. Flyway owns schema changes and the optional synthetic seed.

Start at the [active architecture index](<docs/Target Architecture (HLD)/README.md>). Check actual code against the [implementation architecture](<docs/System Architecture (LLD)/README.md>).

The operating scope is a local demo. System Sizing in the active architecture records the inputs and measurements available for this baseline. Keep app dependencies local and display scheduled instants in the browser's time zone.

## How

### Lifecycles

Use the [lifecycle overview](<docs/Engineering Guide/Lifecycles/00 Overview.md>) to route work.

- **Development** — `$dev` gathers context, handles ceremony, plans, implements, tests, documents and completes a local story. The root scope covers all seven stages.
- **Project design** — `$design` gathers context, handles ceremony, frames, researches, evaluates, drafts a solution, runs a review gate, addresses feedback, publishes and completes. The Project design uses this repository's architecture.
- **Skill improvement** — `$evolve` captures recurring findings and uses the stages `reflect`, `triage`, `validate` and `adopt`.
- Use the stage and coordination procedures of the installed skill. Progress is recorded in its story report and mirrored to native tracking when the session exposes it.

### Agents

| Component | Owned paths | Agent | Responsibilities |
| --- | --- | --- | --- |
| Tournament workspace | `frontend/` | `@web` | React views, browser time conversion and HTTP client |
| Registrations | `backend/src/main/java/com/artisyn/tournamentiq/{tournament,team,field,shared}/` and application bootstrap | `@registrations` | Registration, scoped directories and API problem responses |
| Manual scheduling | `backend/src/main/java/com/artisyn/tournamentiq/match/` | `@schedule` | Match validation, ownership and chronology |
| Persistent store | `backend/src/main/resources/db/` | `@database` | Flyway tables, constraints and seed |
| Cross-component architecture | Architecture documents and read-only review of applicable source | `@architect` | Evidence-led design and independent architecture review |
| Permanent docs | `docs/` and in-scope README references | `@docs-writer` | Align docs with evidence and index additions |
| Getting started | Setup guidance | `@onboarding` | Guide developers through the existing local setup |

Use the component's actual ownership when choosing an agent. Compose, build configuration, and cross-feature tests belong to the orchestrator unless a task assigns a narrower scope.

### Local stories and integration

- Use `docs/Stories/` for Dev work and `docs/Stories/design/` for Project Design.
- Route from the description or local story identifier. This installation uses local records and has no MCP credentials or tracker connection.
- Start or verify the application using the [root README](README.md) and each application's README.
- For architecture lookup, use the [documentation index](docs/README.md). Search source or docs directly when the operational graph is stale or unavailable.

### Work outside a story

After a logical change outside a lifecycle, delegate documentation alignment to `@docs-writer` using the changed sources and verified behavior. Update the applicable LLD, application README and Engineering Guide when the diff changes their subject. Index the changed documentation and report an indexing failure alongside the work. Typo, formatting and comment-only changes are exempt. Use the Dev Document stage when its story has passed Test.
