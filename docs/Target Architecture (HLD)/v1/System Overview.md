---
id: 77376e1f
type: System Overview
title: TournamentIQ system overview
description: Show the runtime boundaries and data flow of the local tournament manager.
tags: [hld, architecture, tournament]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:57:16Z }
updated: { by: process:design-publish, at: 2026-11-28T07:59:31Z }
---

# TournamentIQ system overview

```mermaid
flowchart LR
User[Organizer in browser] -->|configure, preview, save or enter manually| Web
  subgraph System["TournamentIQ"]
    Web[Web UI and Nginx<br/>tournament workspace]
    Api[Spring Boot REST API<br/>scheduling rules and transactions]
    Db[(PostgreSQL 17<br/>tournament state and serialization)]
    Web -->|REST JSON: setup, preview and save| Api
    Api -->|JPA and SQL: reads, tournament lock and atomic writes| Db
  end
  class Web,Api,Db required
  class User context
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  classDef context fill:#e2e8f0,stroke:#475569,color:#0f172a
```

| Container | Runtime boundary | Primary responsibility |
| --- | --- | --- |
| [Web](<Containers/Web.md>) | Static React application and request proxy | Tournament setup, manual match entry, generated-schedule configuration, preview and explicit save |
| [API](<Containers/API.md>) | One Spring Boot process | REST, exact fixture enumeration, deterministic bounded placement, independent validation and transaction ownership |
| [PostgreSQL](<Containers/PostgreSQL.md>) | One database service | Persistent tournament state, chronological match queries and tournament-row serialization |

An organizer creates or selects a tournament and registers teams and football fields. Manual scheduling submits one explicit matchup. Generated scheduling sends one configuration for a read-only preview, then explicitly submits the complete proposal for save. The API reloads tournament state under the tournament-row lock, validates the complete match schedule independently and inserts every generated match in one transaction. Workspace requests return the match schedule chronologically, and the Web displays persisted instants in the browser's time zone.

The baseline is a local Docker Compose demonstration. Compose starts PostgreSQL, waits for the API, then starts Web. Flyway initializes the schema and optionally seeds the demo. See [Compose](<../../../compose.yaml>), [the LLD index](<../../System Architecture (LLD)/README.md>) and [System Sizing](<System Sizing.md>).
