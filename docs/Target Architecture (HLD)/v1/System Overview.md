---
id: 77376e1f
type: System Overview
title: TournamentIQ system overview
description: Show the runtime boundaries and data flow of the local tournament manager.
tags: [hld, architecture, tournament]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:57:16Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:57:16Z }
---

# TournamentIQ system overview

```mermaid
flowchart LR
User[Organizer in browser] -->|HTML and same-origin requests| Web
  subgraph System["TournamentIQ"]
    Web[Web UI and Nginx]
    Api[Spring Boot REST API]
    Db[(PostgreSQL 17)]
    Web -->|REST JSON| Api
    Api -->|JPA and SQL| Db
  end
  class Web,Api,Db required
  class User context
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  classDef context fill:#e2e8f0,stroke:#475569,color:#0f172a
```

| Container | Runtime boundary | Primary responsibility |
| --- | --- | --- |
| [Web](<Containers/Web.md>) | Static React application and request proxy | Tournament selection, registration and match entry |
| [API](<Containers/API.md>) | One Spring Boot process | Feature-scoped REST, domain validation and transactions |
| [PostgreSQL](<Containers/PostgreSQL.md>) | One database service | Persistent tournament assignments and chronological queries |

An organizer creates or selects a tournament, registers teams and fields, then submits an explicit matchup and its time range. The API validates the assignment against the tournament and writes it transactionally. Workspace requests return the schedule in chronological order. The Web interface displays instants in the browser's time zone.

The baseline is a local Docker Compose demonstration. Compose starts PostgreSQL, waits for the API, then starts Web. Flyway initializes the schema and optionally seeds the demo. See [Compose](<../../../compose.yaml>), [the LLD index](<../../System Architecture (LLD)/README.md>) and [System Sizing](<System Sizing.md>).
