---
id: 15bcf2b3
type: Component Design
title: PostgreSQL principles and patterns
description: Maintain Flyway-owned schema, scoped relationships and reproducible demo data.
tags: [lld, postgresql, tournament]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:56:12Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:56:12Z }
---

# PostgreSQL principles and patterns

```mermaid
flowchart LR
Migration[Versioned migration] --> Schema[(Tournament schema)]
  Profile[Demo profile] --> Seed[Fixed fixture rows]
  Seed --> Schema
  Api[API writes] --> Schema
  class Migration,Schema,Seed required
  class Profile,Api context
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  classDef context fill:#e2e8f0,stroke:#475569,color:#0f172a
```

| Concern | Existing behavior | Evidence |
| --- | --- | --- |
| Schema authority | Flyway executes versioned SQL. Hibernate validates at startup. | [V1 migration](<../../../backend/src/main/resources/db/migration/V1__create_tournament_schema.sql>), [configuration](<../../../backend/src/main/resources/application.yml>) |
| Cross-tournament references | Composite foreign keys keep the match and referenced teams and field in the same tournament. | V1 migration |
| Schedule reads | The index starts with tournament, start time and UUID. The API query orders by start and UUID. | V1 migration, [MatchRepository](<../../../backend/src/main/java/com/artisyn/tournamentiq/match/persistence/MatchRepository.java>) |
| Synthetic fixtures | The repeatable seed uses fixed values and conflict-safe insertion when the demo profile enables it. | [Demo seed](<../../../backend/src/main/resources/db/demo/R__seed_demo_tournaments.sql>) |

See [Tournament Store](<Tournament Store.md>) for the data model.
