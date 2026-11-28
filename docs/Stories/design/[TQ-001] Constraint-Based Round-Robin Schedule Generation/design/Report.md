---
type: Design Report
title: TQ-001 Constraint-Based Round-Robin Schedule Generation
description: Tracks the project design for deterministic round-robin fixture generation and constrained schedule placement.
tags: [design, scheduling, round-robin, constraints]
generated: { by: process:design-context, at: 2026-11-27T12:10:59Z }
updated: { by: process:design-solution, at: 2026-11-28T02:36:00Z }
---

# Design Report — TQ-001: Constraint-Based Round-Robin Schedule Generation

## Context

TQ-001 is a local project ticket with no external tracker integration. It defines a design for previewing and atomically saving a deterministic single round-robin schedule for 2–12 registered teams. The design must place every unique pairing across selected tournament fields and playing windows while enforcing team overlap, field overlap, turnaround, minimum-rest, ownership, and time-window constraints. It must preserve the current manual scheduling flow, use independent validation, distinguish invalid input, proven capacity failure, and bounded-search failure, and evaluate alternatives before recommending a solution. The Design Lifecycle produces design artifacts only and does not modify application code.

## State

```yaml
current_stage: review-gate
completed_stages: [context-gathering, ceremony, frame, research, evaluate, solution]
selected_artifacts:
  - '../solution/Architecture.md'
  - '../solution/Design - Fixture and Placement Strategy.md'
  - '../solution/Design - Validation and Failure Model.md'
  - '../solution/Design - Preview and Persistence Protocol.md'
  - '../solution/Stack.md'
  - '../solution/Efforts.md'
next_action: 'Commit the approved Solution artifacts, then open the Review Gate'
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
| Phase | 🎯 Context Gathering ✅ · 🚀 Ceremony ✅ · 📋 Frame ✅ · 🔍 Research ✅ · 🏗 Evaluate ✅ · 📦 Solution ✅ · 🔎 Review 🔄 · 💬 Address Feedback ⏳ · 📤 Publish ⏳ · 🏁 Completion ⏳ |

---

## Stage Metrics

<!-- design-metrics:start -->
Source: [stat.json](stat.json). **Elapsed** = wall-clock time from the stage's first start to its completion (includes any wait). **Work** = summed observed work intervals (active effort) within that span. **Wait** = summed observed wait intervals (blocked on developer input, approval, or an external dependency) within that span. Work + Wait can read less than Elapsed when time went unclassified, or more after a retry reopened the stage. Durations are shown as `Xh Ym Zs`, rounded to the nearest second; open intervals are excluded from Elapsed/Work/Wait. Token subtotals have unavailable coverage; ticket usage is not allocated to individual units.

Observed work: 12h 22m 17s · Wait to date: 5h 47m 42s (closed 5h 47m 42s + current 0s) · Unclassified: 2m 4s

| Stage | Started (UTC) | Completed (UTC) | Elapsed | Work | Wait | Input tokens | Output tokens | Coverage | Attempts | Failed checks |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| context-gathering (completed) | 2026-11-27T12:20:36.323Z | 2026-11-27T12:23:35.617Z | 2m 59s | 1m 2s | 1m 57s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| ceremony (completed) | 2026-11-27T12:24:07.747Z | 2026-11-27T12:37:25.696Z | 13m 18s | 1m 20s | 11m 58s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| frame (completed) | 2026-11-27T12:37:49.458Z | 2026-11-27T18:09:35.927Z | 5h 31m 46s | 5m 48s | 5h 25m 58s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| research (completed) | 2026-11-27T18:10:07.589Z | 2026-11-27T18:33:17.087Z | 23m 9s | 21m 36s | 1m 34s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| evaluate (completed) | 2026-11-27T18:33:27.399Z | 2026-11-27T18:40:46.642Z | 7m 19s | 5m 12s | 2m 7s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| solution (waiting) | 2026-11-27T18:41:08.384Z | unavailable | unavailable | 11h 47m 18s | 4m 8s | unavailable | unavailable | from_tracking_start | 1 | 0 |
| review-gate (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| address-feedback (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| publish (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
| completion (pending) | unavailable | unavailable | unavailable | 0s | 0s | unavailable | unavailable | from_tracking_start | 0 | 0 |
<!-- design-metrics:end -->

## Assumptions

_From Research OQ triage (2026-11-27)._

| # | Open question | V/E | Answer / status |
| --- | ------------- | --- | --------------- |
| A-OQ1 | What is the legal finite start-time domain? | (V) | Generation inputs use an explicit tournament IANA `ZoneId`. Durations and rest use whole minutes. Candidate starts are a finite one-minute grid inside resolved daily windows. Daylight-saving gaps and ambiguous local times are rejected instead of silently selecting an offset. |
| A-OQ2 | What maximum date span, daily-window size, football-field count and candidate-count cap apply? | (E) | The exact limits remain evaluation and benchmark inputs. Every option rejects a configuration that exceeds its finite candidate bound. |
| A-OQ3 | What scope does the deterministic-output guarantee cover across application versions? | (V) | Identical normalized inputs produce an identical result within the same released application version. Identical match schedules across application versions are not promised. |
| A-OQ4 | What quantitative search limit applies, and does it use deterministic work or wall-clock time? | (E) | The exact node or work limit remains an evaluation and benchmark input. Public outcomes use deterministic work units; wall-clock time is only a safety stop. |
| A-OQ5 | Must placement be lexicographically earliest, or is any deterministic valid placement sufficient? | (E) | Any deterministic valid placement is acceptable. Lexicographic optimality and global optimization are not required. |
| A-OQ6 | How does generation handle existing manually created matches? | (V) | Generation and save apply only when the tournament has no existing matches. Existing matches produce an explicit conflict; the workflow neither merges nor overwrites them. |
| V-OQ1 | Must current manual submissions continue accepting duplicate pairings and zero turnaround? | (V) | Yes. Keep the existing single-match endpoint unchanged. Scope generated-schedule rules to preview and save; global database constraints that alter manual behavior are unsuitable. |
| V-OQ2 | May generation run when tournament matches already exist? | (V) | No. Preview and save require an empty tournament match schedule; do not merge with or overwrite manual matches. |
| V-OQ3 | Which time zone and daylight-saving policy governs dates and daily windows? | (V) | Require one explicit IANA `ZoneId`; reject invalid and ambiguous local date-times deterministically instead of choosing an offset silently. |
| V-OQ4 | What qualifies as insufficient theoretical capacity? | (E) | Unresolved evaluation input. A documented capacity calculation is a necessary lower bound only and never proves infeasibility. |
| V-OQ5 | How does exhaustive search differ from a bounded stop? | (E) | Exhausting the complete modeled space can return `PROVEN_INFEASIBLE`; stopping at a bound returns `SEARCH_LIMIT_EXHAUSTED`. |
| V-OQ6 | Must validation report every conflict or only a sufficient invalidity set? | (E) | Report all deterministic violations. The 66-match maximum bounds pairwise conflict checks and report construction. |
| V-OQ7 | What may the validator share with the generator? | (E) | Share immutable value types and timestamp normalization only; do not share generator constraint predicates or search state. |
| V-OQ8 | Are violation codes and ordering part of the REST contract? | (E) | Yes. Keep codes and ordering stable; the Web client preserves repeated violations and their machine-readable code. |
| V-OQ9 | How is generator output rejected by independent validation classified? | (E) | Return `INTERNAL_FAILURE` with sanitized structured diagnostics; do not blame user configuration or claim infeasibility. |
| P-OQ1 | Must save reject an existing match schedule, merge with it, or overwrite it? | (V) | Reject a tournament with any existing matches; do not merge or overwrite. |
| P-OQ2 | Does side-effect-free preview prohibit server-side preview storage? | (V) | Yes. This makes server-stored consumable preview nonviable unless the requirement changes. |
| P-OQ3 | Must save prove exact preview identity through a token or fingerprint? | (V) | No. Deterministic regeneration or resubmission plus transaction-time reload and revalidation is sufficient. |
| P-OQ4 | Which tournament state must save reload before persistence? | (E) | Reload relevant teams, football fields and matches; conflicting relevant state fails. |
| P-OQ5 | Which HTTP contract represents stale optimistic state? | (E) | Use `409 Conflict` with Problem Details for the current architecture; `If-Match` with `412` remains part of the optimistic-revision option if that option survives Evaluate. |
| P-OQ6 | Must previews survive browser refresh or API restart, or support sharing and audit? | (V) | No. |
| P-OQ7 | What bound applies to lock waits, deadlocks and serialization retries? | (E) | Apply a bounded policy and distinguish state conflict from internal failure; select the threshold during implementation planning and verification. |

---

## Solution Artifact Selection and Concern Scan

The selected artifact set owns every material Solution concern without adding a new runtime boundary.

| Interface | Selection | Evidence and scope |
| --- | --- | --- |
| Architecture | Selected | The new Web-to-API preview/save flow and API-to-PostgreSQL transaction change cross existing container boundaries. |
| Design | Selected | Fixture placement, independent validation, REST contracts and persistence coordination require component-level detail. The three confirmed decisions remain split into separate files. |
| Stack | Selected | The solution confirms pure Java domain logic and existing Spring Data JPA/PostgreSQL locking without adding a solver or service. |
| Efforts | Selected | Implementation spans Web, API, PostgreSQL integration tests and a prerequisite bounded-search calibration spike. |
| Focused data model or migration | Omitted | Tournament-row locking uses the existing tournament and match schema; the selected P1 protocol adds no table, column or migration. |
| Threat model or security artifact | Omitted | Authentication and external integrations are excluded, and the solution does not change a trust boundary or secret flow. |
| Deployment artifact | Omitted | The existing Web, API and PostgreSQL topology remains unchanged. |

| Concern | Classification | Resolution and evidence |
| --- | --- | --- |
| Boundaries and ownership | Material | Architecture and Design own the additive Web-to-API preview/save contracts; the API retains domain and transaction ownership. |
| User and business behavior | Material | Architecture and Design define preview followed by explicit save while preserving the manual match-schedule flow. |
| Data and state | Material | Architecture and persistence Design define stateless preview and one atomic batch match insert with no schema change. |
| Interfaces and compatibility | Material | Design defines additive REST endpoints, structured Problem Details and unchanged manual endpoint semantics. |
| Security and privacy | Not applicable | The accepted Frame excludes authentication and external integrations; no personal data, credential or trust-boundary change is introduced. |
| Reliability and recovery | Material | Architecture and Design own independent validation, transaction rollback and `409 Conflict` handling for stale or concurrent saves. |
| Performance and capacity | Material | Efforts owns a prerequisite spike that sets finite-domain caps, deterministic search budget and benchmark acceptance before generator implementation. |
| Operations and delivery | Informational | Stack and Efforts retain the existing Docker Compose runtime and add no runtime component. |
| Cost and licensing | Informational | The selected solution adds no paid service, solver dependency or new license obligation. |
| Compliance and policy | Not applicable | The local demo adds no regulated data, residency, retention or external policy requirement. |
| Evolution and migration | Material | Architecture and Design keep generated scheduling additive and compatible with the existing manual match-schedule path. |

Every material concern is assigned to a selected artifact or confirmed design decision.

---

## Solution Quality Gate

**2026-11-28 verdict: PASS.** No material findings remain.

| Check | Evidence |
| --- | --- |
| Selected artifacts | All six State-listed artifacts exist. |
| Requirement coverage | EC1–EC20 and PEC1/PEC4 map to the Architecture decision register and the three decision-specific Design files. |
| Concern ownership | Every material preliminary concern has a selected artifact or confirmed decision. |
| Certainty | Solution artifacts contain no TODO, TBD, placeholder, open-question or unresolved sections. Empty `Efforts.md` estimates are intentionally developer-owned. |
| Decision consistency | `Architecture.md` is canonical; Design, Stack and Efforts add no conflicting material decision. |
| Architect review | Pass 1 found nonempty-preview rejection and wall-clock safety classification omissions. Both were corrected; pass 2 returned `OK`. |
| Diagrams | Mermaid CLI is unavailable. Syntax-oriented inspection covered 10 fenced Mermaid blocks, diagram types and color declarations with no issue. |
| Links and formatting | Every local Markdown link resolves and `git diff --check` passes. |

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
| 2026-11-27 | Frame | 20 known requirements and 2 proposed requirements accepted; three independent decision tracks confirmed. |
| 2026-11-27 | Research | `@architect` returned 11 options across Fixture and Placement Strategy (earliest-fit greedy, deterministic CSP backtracking, embedded CP-SAT), Validation and Failure Model (generator-coupled checks, independent pure whole-schedule validator, PostgreSQL constraint-led validation, independent constraint-model replay), and Preview and Persistence Protocol (tournament row lock, optimistic schedule revision, server-stored preview, serializable save); developer confirmed the set complete. |
| 2026-11-27 | Evaluate | Recommended deterministic CSP backtracking, independent pure whole-schedule validation, and stateless preview with a tournament row lock. |
| 2026-11-28 | Solution | Selected Architecture, three decision-specific Designs, Stack and Efforts written; independent architect review OK; quality gate PASS; developer estimates total 10d. |

---

## Implementation Tickets

_Filled in during Completion after implementation tickets are created._

| Ticket | Summary | Local follow-up |
| ------ | ------- | --------------- |
