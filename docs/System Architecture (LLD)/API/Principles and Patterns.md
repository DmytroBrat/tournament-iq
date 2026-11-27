---
id: 8d1d790f
type: Component Design
title: API principles and patterns
description: Apply consistent REST errors, feature services and transaction boundaries.
tags: [lld, api, tournament]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:56:12Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:56:12Z }
---

# API principles and patterns

```mermaid
flowchart LR
Request[JSON request] --> Controller[Controller and validation]
  Controller --> Service[Feature service]
  Service --> Repository[Spring Data repository]
  Controller --> Errors[Problem Details]
  Service --> Errors
  class Controller,Service,Errors required
  class Request,Repository context
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  classDef context fill:#e2e8f0,stroke:#475569,color:#0f172a
```

| Concern | Existing implementation | Evidence |
| --- | --- | --- |
| Validation | Required inputs are validated in the controller path. Cross-resource checks run in feature services. | [MatchController](../../../backend/src/main/java/com/artisyn/tournamentiq/match/api/MatchController.java), [MatchService](../../../backend/src/main/java/com/artisyn/tournamentiq/match/application/MatchService.java) |
| Errors | Problem Details carries a machine code and optional field violations. The application logs unexpected failures and returns a generic message. | [ApiErrors](../../../backend/src/main/java/com/artisyn/tournamentiq/shared/api/ApiErrors.java) |
| Transactions | List queries are read-only. Mutating operations run in write transactions. | [TournamentService](../../../backend/src/main/java/com/artisyn/tournamentiq/tournament/application/TournamentService.java), [MatchService](../../../backend/src/main/java/com/artisyn/tournamentiq/match/application/MatchService.java) |
| Time | HTTP timestamps use offsets, JVM match times use instants, database columns use timestamptz. | MatchController, [MatchAssignment](../../../backend/src/main/java/com/artisyn/tournamentiq/match/domain/MatchAssignment.java) and schema |
