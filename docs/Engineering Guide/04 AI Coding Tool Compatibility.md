---
id: 48960fc2
type: Guide
title: AI coding tool compatibility
description: Find the native Codex installation artifacts and the runtime capability fallback.
tags: [engineering-guide, codex, harness]
generated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
updated: { by: artisyn-harness/4.2, at: 2026-11-26T15:52:57Z }
---

# AI coding tool compatibility

| Artifact | Codex location |
| --- | --- |
| Root instructions | `AGENTS.md` |
| Skills | `.agents/skills/<name>/SKILL.md` |
| Specialist roles | `.codex/agents/<name>.toml` |
| Settings and MCP declarations | `.codex/config.toml` |
| Role procedures | `.codex/harness-runtime/` |
| Launch state | `.harness/state/launch/` |

Use the dollar prefix for installed skills. Read the relevant procedures inside their runtime folder. Track progress in the durable run or story report and mirror it in native task tracking when the session exposes it. Respect role scopes when delegating and keep independent review visible in the report.

The setup is local. MCP declarations contain no server connections for this project. Use the marketplace plugin for a future patch operation.
