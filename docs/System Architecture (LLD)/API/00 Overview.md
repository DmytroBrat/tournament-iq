---
id: 12d85716
type: Component Design
title: API component overview
description: Locate registrations, match scheduling and their shared API contracts.
tags: [lld, api, tournament]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:56:12Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:56:12Z }
---

# API component overview

```mermaid
flowchart LR
Web[Web REST client] -->|setup requests| Registrations
  Web -->|match and schedule requests| Schedule
  subgraph API["Spring Boot API"]
    Registrations[Tournament registrations]
    Schedule[Manual scheduling]
    Schedule -->|check tournament and scoped participants| Registrations
  end
  Registrations -->|JPA| Database[(PostgreSQL)]
  Schedule -->|JPA| Database
  class Registrations,Schedule required
  class Web,Database context
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  classDef context fill:#e2e8f0,stroke:#475569,color:#0f172a
```

The feature packages partition [registrations](<Tournament Registrations.md>) and [manual scheduling](<Manual Scheduling.md>). [Principles and Patterns](<Principles and Patterns.md>) covers shared error, transaction and migration boundaries.
