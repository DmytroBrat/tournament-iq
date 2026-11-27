---
id: 88115664
type: Guide
title: Project Design lifecycle
description: Use the ten Design stages to evaluate changes to TournamentIQ architecture.
tags: [engineering-guide, design, lifecycle]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
---

# Project Design lifecycle

```mermaid
flowchart LR
  Context --> Ceremony --> Frame --> Research --> Evaluate --> Solution
  Solution --> Review --> Feedback --> Publish --> Completion
  classDef required fill:#dbeafe,stroke:#1d4ed8,color:#172554
  class Frame,Evaluate,Solution,Review required
```

Invoke `$design` with the design question or a local design ID. Context Gathering creates the durable record under the project Design stories root. Capture actual source paths, decisions, tradeoffs and the approved destination there.

Record the real review outcome and any addressed feedback before publishing. Publish confirmed decisions into the active project [architecture](<../../Target Architecture (HLD)/README.md>). Completion records the local implementation handoff. Start `$dev` for separately scoped code work.
