---
id: 4871e58d
type: Guide
title: Documents flow
description: Trace approved design and verified code into the architecture and engineering guide.
tags: [engineering-guide, architecture, lifecycle]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
---

# Documents flow

```mermaid
flowchart LR
  Design[Approved project design] --> HLD[Active HLD]
  HLD --> Dev[Local Dev story]
  Code[Verified code] --> LLD[Current LLD]
  Dev --> Code
  LLD --> Guide[Shared engineering rules]
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  class HLD,LLD,Guide required
```

Publish accepted design intent through the Design Publish stage. Record verified implementation using the Dev Document stage. Update the component docs and navigation when the project's behavior changes. Index the resulting documents in the operational graph.

Use [Documentation Structure](<../Documentation/02 Structure.md>) to choose the owning tier.
