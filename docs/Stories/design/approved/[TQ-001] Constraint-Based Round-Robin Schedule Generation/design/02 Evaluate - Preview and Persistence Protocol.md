---
type: Design Evaluation
title: TQ-001 Preview and Persistence Protocol Evaluation
description: Evaluates preview and atomic persistence protocols against TournamentIQ concurrency and compatibility requirements.
tags: [design, scheduling, preview, persistence, concurrency]
generated: { by: process:design-evaluate, at: 2026-11-27T18:35:20Z }
updated: { by: process:design-evaluate, at: 2026-11-27T18:37:20Z }
---

# TQ-001: Preview and Persistence Protocol — Evaluate

_Produced by the Evaluate phase. Records tradeoffs and the recommended persistence protocol._

The evaluation compares the four protocols from [Research](<01 Research - Preview and Persistence Protocol.md>) against the accepted [Frame](<00 Frame.md>) and confirmed Research assumptions in [Report](Report.md#assumptions).

## Requirements × Options Matrix

The protocol must keep preview side-effect free and serialize every match-schedule mutation without changing the existing manual scheduling contract.

✅ satisfies · ⚠️ partially or with a stated condition · ❌ does not satisfy

| Requirement | P1: Tournament row lock | P2: Schedule revision | P3: Stored preview | P4: Serializable save |
| --- | --- | --- | --- | --- |
| EC9: Return preview and validation without persistence | ✅ Stateless; inserts no match or preview record | ✅ Stateless; returns a revision with the response | ❌ Persists a preview record despite the confirmed no-preview-persistence rule | ✅ Stateless; inserts nothing during preview |
| EC10: Independently validate the complete match schedule before save | ✅ Runs the whole-match-schedule validator before the first insert | ✅ Same independent validation after the revision claim | ✅ Revalidates the stored proposal; stored validation alone is insufficient | ✅ Runs independent validation inside the serializable transaction |
| EC12: Save all matches atomically | ✅ One outer transaction covers lock, reload, validation and all inserts | ✅ One transaction covers the revision claim, reload, validation and inserts | ✅ One transaction covers consumption, validation and inserts | ✅ One serializable transaction covers validation and inserts |
| EC13: Prevent stale or concurrent combination and overwrite | ⚠️ Satisfies only when manual match creation and every relevant mutation acquire the same tournament lock in one lock order | ✅ An atomic revision claim rejects competing saves when every invalidating mutation advances the revision | ⚠️ Preview consumption prevents reuse, but distinct previews and manual writes still need a tournament lock or revision | ⚠️ Satisfies only when every competing writer uses compatible serializable transactions and state predicates |
| Confirmed existing-match policy | ✅ Reloads matches under the lock and rejects a nonempty match schedule | ✅ Reloads matches after the revision claim and rejects a nonempty match schedule | ✅ Checks current matches while consuming the preview | ✅ Predicate-reads matches and rejects a nonempty match schedule |
| Confirmed transaction-time state check | ✅ Reloads relevant teams, football fields and matches before validation and insert | ✅ Reloads the same state after claiming the expected revision | ✅ Reloads current state rather than trusting the stored snapshot | ✅ Reloads all relevant state in the serializable transaction |
| Confirmed preview identity policy | ✅ Requires no preview token; accepts deterministic regeneration or resubmission | ⚠️ Adds revision identity although exact preview identity is not required | ❌ Adds a preview UUID and consumption lifecycle that the confirmed requirement excludes | ✅ Requires no preview token; accepts regeneration or resubmission |
| API: REST Problem Details and current `409 Conflict` contract | ✅ Maps existing matches and state conflicts to stable `409` codes | ⚠️ Supports `409`, but ETag form introduces `If-Match` and `412` semantics | ⚠️ Adds preview-not-found, expired and consumed response states | ⚠️ Adds serialization-abort and bounded-retry outcomes to the conflict contract |
| EC18: Preserve the modular monolith without unnecessary infrastructure or dependencies | ✅ Uses existing Spring Data JPA and PostgreSQL; adds no schema or dependency | ⚠️ Adds a Flyway revision column or state table plus API/client coordination | ❌ Adds preview schema, retention and cleanup without a confirmed use case | ⚠️ Adds no schema or dependency, but introduces stricter isolation and retry machinery |
| EC20: Preserve manual scheduling behavior | ⚠️ Keeps request semantics, but manual match creation and relevant registrations must join the lock protocol | ⚠️ Keeps request semantics, but every relevant manual mutation must advance the revision | ⚠️ Keeps manual requests separate, but they still need the additional concurrency protocol | ⚠️ Requires competing manual writers to use compatible serializable transactions and predicates |
| EC19: Verify side effects, atomicity and concurrency | ✅ Direct Testcontainers race verifies that one tournament-locked save wins and the other receives `409` | ✅ Revision races are directly testable, including missing mutation increments | ⚠️ Requires expiry, consumption, cleanup and cross-preview tests beyond the accepted scope | ⚠️ Requires PostgreSQL-specific serialization and retry tests; non-participating writers remain a risk |
| Bounded conflict handling | ⚠️ Lock wait and deadlock thresholds remain implementation and test inputs | ⚠️ Optimistic conflict handling is bounded, but state-claim timing needs definition | ⚠️ Lock waits, expiry and cleanup bounds require separate policies | ⚠️ Serialization retry count and request-budget accounting remain open inputs |

## Tradeoffs

P1 has the smallest state model; P2–P4 add coordination mechanisms whose benefit depends on requirements that are not currently approved.

| Dimension | P1: Tournament row lock | P2: Schedule revision | P3: Stored preview | P4: Serializable save |
| --- | --- | --- | --- | --- |
| Integration complexity | **Medium.** Add one tournament lock query, one shared writer protocol, batch save and conflict mapping. | **Medium–high.** Add Flyway state, atomic claims, client revision handling and increments on every invalidating mutation. | **High.** Add preview entities, expiry, cleanup, consumption and a second tournament concurrency mechanism. | **High.** Add isolation configuration, complete predicate reads, failure translation and bounded whole-transaction retries. |
| Vendor lock-in | **Low.** Uses standard JPA locking over the existing PostgreSQL transaction boundary. | **Medium.** Couples API preconditions and mutation behavior to a project-wide revision model and PostgreSQL atomic claims. | **High.** Couples API behavior and operations to a server-owned preview lifecycle and PostgreSQL schema. | **High.** Correctness and retry behavior depend directly on PostgreSQL serializable-isolation semantics. |
| Added persistent state | None | Tournament schedule revision or state row | Preview payload, snapshot, status and expiry | None |
| Contention model | Same-tournament writers wait at one explicit row lock | Competing stale writers fail the revision claim | Preview and tournament rows require coordinated locks | PostgreSQL aborts unsafe transactions; the API retries or returns a conflict |
| Main implementation risk | A writer that omits the shared tournament lock leaves a race | A mutation that omits revision advancement leaves a stale preview undetected | Lifecycle state grows without a confirmed resume, sharing or audit requirement | Default-isolation or incomplete-predicate writers leave the serialization guarantee incomplete |

## Recommendation

**Recommended option: P1 — Stateless Preview with Tournament Row Lock**

P1 satisfies EC9, EC10, EC12, EC13 and EC18 with the smallest state model. Preview inserts no records. Save uses one outer transaction to acquire the tournament lock, reload relevant teams, football fields and matches, reject a nonempty match schedule, independently validate the complete resubmitted preview, and insert every match atomically. State conflicts return `409 Conflict` Problem Details.

EC13 depends on one enforceable condition: generated save, manual match creation and every relevant team or football-field registration mutation acquire the same tournament lock in a canonical order. This changes internal coordination without changing the manual endpoint's request semantics, satisfying EC20. A two-writer Testcontainers race can prove the required atomic winner-and-conflict behavior for EC19.

The medium lock-protocol integration cost is lower than introducing new persistent coordination state:

- P2 adds a Flyway-managed revision, client preconditions and revision advancement to every invalidating mutation. Exact preview identity is not required, so this state has no demonstrated benefit.
- P3 violates the confirmed no-preview-persistence rule and adds preview expiry, cleanup and consumption behavior.
- P4 adds PostgreSQL serializable-abort translation and bounded whole-transaction retries. Writers that do not join its isolation and predicate protocol still leave a concurrency gap.

The recommendation is falsifiable. Re-evaluate P2 if measured same-tournament lock contention exceeds an approved target or a future requirement demands durable, explicit preview freshness.

**Open questions to resolve before implementation:**

| # | Question | Owner | Impact if unresolved |
| --- | --- | --- | --- |
| P-EQ1 | Which match-schedule-invalidating mutations participate in the tournament lock protocol, and what canonical lock order do they use? | Backend architect | Blocks the EC13 concurrency guarantee. |
| P-EQ2 | Does save accept the complete preview response as its payload, and what exact reload and transaction-time validation sequence follows? | API and Web owners | Blocks the preview/save endpoint contract. |
| P-EQ3 | What lock timeout and deadlock threshold applies, and which bounded `409` Problem Details codes represent those outcomes? | Backend and QA owners | Blocks the failure contract and concurrency acceptance tests. |
