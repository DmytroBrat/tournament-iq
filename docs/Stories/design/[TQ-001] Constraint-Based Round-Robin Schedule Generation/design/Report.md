---
type: Design Report
title: TQ-001 Constraint-Based Round-Robin Schedule Generation
description: Tracks the project design for deterministic round-robin fixture generation and constrained schedule placement.
tags: [design, scheduling, round-robin, constraints]
generated: { by: process:design-context, at: 2026-11-27T12:10:59Z }
updated: { by: process:design-ceremony, at: 2026-11-27T12:34:42Z }
---

# Design Report — TQ-001: Constraint-Based Round-Robin Schedule Generation

## Context

TQ-001 is a local project ticket with no external tracker integration. It defines a design for previewing and atomically saving a deterministic single round-robin schedule for 2–12 registered teams. The design must place every unique pairing across selected tournament fields and playing windows while enforcing team overlap, field overlap, turnaround, minimum-rest, ownership, and time-window constraints. It must preserve the current manual scheduling flow, use independent validation, distinguish invalid input, proven capacity failure, and bounded-search failure, and evaluate alternatives before recommending a solution. The Design Lifecycle produces design artifacts only and does not modify application code.

## State

```yaml
current_stage: frame
completed_stages: [context-gathering, ceremony]
selected_artifacts: []
next_action: 'Commit Context Gathering and Ceremony artifacts, then begin Frame'
context_fingerprint: ''
```

## Evidence

| Source | Read at stage | Relevant decision or constraint | Reference/version |
| ------ | ------------- | ------------------------------- | ----------------- |
| `requirements/TQ-001.md` | context-gathering | Defines 2–12 team single round-robin generation, preview/save flow, hard constraints, deterministic bounded execution, independent validation, atomic persistence, acceptance criteria, and explicit exclusions. | Developer-provided TQ-001 working copy, read 2026-11-27 |
| `AGENTS.md` | context-gathering | Treats automatic scheduling, fixture generation, optimization, and minimum-rest rules as separately scoped work and routes design through the project lifecycle. | Current project instructions |
| `docs/Target Architecture (HLD)/README.md` | context-gathering | Resolves `v1` as the active target architecture version. | Active version v1 |
| `docs/Target Architecture (HLD)/v1/System Overview.md` | context-gathering | Establishes the Web → API → PostgreSQL boundaries and the current explicit-assignment schedule flow. | HLD v1 |
| `docs/Target Architecture (HLD)/v1/Containers/Web.md` | context-gathering | Web owns organizer interaction and browser state; API owns durable tournament data and write rules. | HLD v1 |
| `docs/Target Architecture (HLD)/v1/Containers/API.md` | context-gathering | API owns tournament rules and transactions; automatic generation and minimum-rest rules are explicitly reserved for separately scoped work. | HLD v1 |
| `docs/Target Architecture (HLD)/v1/Containers/PostgreSQL.md` | context-gathering | API owns transactions while PostgreSQL provides durable match storage and integrity constraints. | HLD v1 |
| `docs/Target Architecture (HLD)/v1/System Sizing.md` | context-gathering | Current sizing is limited to the local demo and has no execution-time or capacity target; TQ-001 must provide its own bounded conditions. | HLD v1 |
| `docs/System Architecture (LLD)/API/Manual Scheduling.md` | context-gathering | Current implementation persists explicit match assignments and reads schedules chronologically; generated scheduling is not implemented. | Current LLD |
| `docs/Engineering Guide/01 Conventions.md` | context-gathering | Requires feature packaging, domain-owned match rules, Problem Details, tournament scoping, Flyway migrations, instant-based time handling, and existing local verification commands. | Current Engineering Guide |
| Developer design instruction | context-gathering | Confirms a project-level local ticket, `requirements/TQ-001.md` as business authority, alternative evaluation, compatibility review, approval gates, and no application implementation during Design. | Developer confirmation, 2026-11-27 |
| Developer architecture-review instruction | ceremony | Requires scrutiny of completeness versus heuristics, deterministic ordering, end-to-start rest, out-of-order insertion validation, independent validation, atomic and concurrent persistence, and infrastructure restraint. | Developer confirmation, 2026-11-27 |

| Field | Value |
| ----- | ----- |
| Track | Project |
| Source docs | `docs/Target Architecture (HLD)/` |
| Dest docs | `docs/Target Architecture (HLD)/v1/` |
| Subfolder | System-level design spanning `Containers/Web.md`, `Containers/API.md`, `Containers/PostgreSQL.md`, and `System Sizing.md` |
| Architect agent | `@architect` |
| Implementation note | Use `$dev` in this repository after the design is approved. |
| Stories root | `docs/Stories/design/` |
| Task tracking | `design/Report.md` because native `update_plan` is not exposed in this session. |

## Status

| Field | Value |
| ----- | ----- |
| Ticket | TQ-001 |
| Branch | `docs/tq-001-constraint-round-robin-scheduling` |
| Status | 🔄 In Progress |
| Phase | 🎯 Context Gathering ✅ · 🚀 Ceremony ✅ · 📋 Frame 🔄 · 🔍 Research ⏳ · 🏗 Evaluate ⏳ · 📦 Solution ⏳ · 🔎 Review ⏳ · 💬 Address Feedback ⏳ · 📤 Publish ⏳ · 🏁 Completion ⏳ |

---

## Stage Metrics

<!-- design-metrics:start -->
Source: [stat.json](stat.json). **Elapsed** = wall-clock time from the stage's first start to its completion (includes any wait). **Work** = summed observed work intervals (active effort) within that span. **Wait** = summed observed wait intervals (blocked on developer input, approval, or an external dependency) within that span. Work + Wait can read less than Elapsed when time went unclassified, or more after a retry reopened the stage. Durations are shown as `Xh Ym Zs`, rounded to the nearest second; open intervals are excluded from Elapsed/Work/Wait. Token subtotals have unavailable coverage; ticket usage is not allocated to individual units.

Observed work: 1m 23s · Wait to date: 11m 57s (closed 11m 57s + current 0s) · Unclassified: 1m 27s

| Stage | Started (UTC) | Completed (UTC) | Elapsed | Work | Wait | Input tokens | Output tokens | Coverage | Attempts | Failed checks |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| context-gathering (completed) | 2026-11-27T12:20:36.323Z | 2026-11-27T12:23:35.617Z | 2m 59s | 1m 2s | 1m 57s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| ceremony (active) | 2026-11-27T12:24:07.747Z | unavailable | unavailable | 20s | 10m 0s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| frame (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| research (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| evaluate (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| solution (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| review-gate (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| address-feedback (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| publish (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| completion (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
<!-- design-metrics:end -->

## Assumptions

_Filled in during Research OQ triage. One row per open question answered or classified._

| # | Open question | V/E | Answer / status |
| - | ------------- | --- | --------------- |

---

## Gate

- Approver: —
- Draft MR: —
- Approved: — _(a date once approved, or `declined`)_
- Decline reason: — _(filled in by Address Feedback only if declined)_

---

## Efforts

| Date | Phase | Notes |
| ---- | ----- | ----- |
| 2026-11-27 | Context Gathering | Resolved TQ-001 as a cross-container Project design against active HLD v1 and current manual scheduling behavior. |
| 2026-11-27 | Ceremony | Local ticket checkpoint auto-approved: TQ-001 was supplied by the developer, no external tracker is configured, and assignment or transition is not applicable. |

---

## Implementation Tickets

_Filled in during Completion after implementation tickets are created._

| Ticket | Summary | Local follow-up |
| ------ | ------- | --------------- |
