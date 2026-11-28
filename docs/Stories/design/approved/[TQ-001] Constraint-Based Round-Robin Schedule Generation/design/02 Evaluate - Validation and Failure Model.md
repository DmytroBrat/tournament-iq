---
type: Design Evaluation
title: TQ-001 Validation and Failure Model Evaluation
description: Evaluates whole-match-schedule validation and failure-classification options against the accepted TQ-001 criteria.
tags: [design, scheduling, validation, failures]
generated: { by: process:design-evaluate, at: 2026-11-27T18:35:11Z }
updated: { by: process:design-evaluate, at: 2026-11-27T18:37:11Z }
---

# TQ-001 — Validation and Failure Model Evaluate

_Produced by the Evaluate phase. Records tradeoffs and the recommended option._

## Requirements × Options Matrix

The matrix evaluates each option as the primary validator for the complete proposed match schedule. It uses the accepted [Frame](<00 Frame.md>), confirmed [Research](<01 Research - Validation and Failure Model.md>), and assumptions in the [Design Report](Report.md).

✅ satisfies · ⚠️ partially or with the stated caveat · ❌ does not satisfy

| Evaluation criterion | A. Generator-coupled checks | B. Pure whole-schedule validator | C. PostgreSQL constraints | D. Constraint-model replay |
| --- | --- | --- | --- | --- |
| EC3: tournament ownership, distinct teams and exact fixture set | ⚠️ Can check partial assignments, but needs separate complete-set assertions. | ✅ Validates ownership and the canonical fixture multiset over the complete input. | ⚠️ Enforces ownership and duplicate-pair constraints after schema changes, but not fixture completeness. | ⚠️ Needs separate imperative checks for ownership, missing fixtures and duplicate pairings. |
| EC4: no team overlap | ⚠️ Correct only when every candidate is checked against both chronological neighbours or all assignments. | ✅ Rebuilds every team timeline before checking overlaps. | ✅ Participant occupancy ranges can enforce the represented rule after persistence. | ✅ A correctly quantified fixed-assignment model detects team overlap. |
| EC5: no football-field overlap and five-minute turnaround | ⚠️ Correct only with order-independent predecessor and successor checks. | ✅ Rebuilds football-field timelines and applies the fixed turnaround. | ✅ Exclusion ranges can enforce the represented interval rule after persistence. | ✅ A correctly encoded fixed-assignment model checks overlap and turnaround. |
| EC6: minimum rest uses previous match end to next match start | ⚠️ Generator predicates can implement end-to-start rest, but append-order assumptions can miss a neighbour. | ✅ Sorts each team timeline and compares `next.start` with `previous.end + minimumRest`. | ⚠️ Expanded occupancy ranges can represent rest, but configurable rest complicates persisted rows. | ✅ Explicit ordering constraints can encode end-to-start rest. |
| EC7 and PEC1: dates, daily windows, time zone and daylight-saving rules | ⚠️ Can validate generator candidates, but does not independently validate arbitrary input. | ✅ Validates every normalized match against resolved windows and the confirmed `ZoneId` policy. | ❌ Ordinary row constraints do not validate the complete configured daily-window policy. | ✅ Can encode resolved windows, with time normalization performed before model construction. |
| EC10: independent validation of the complete preview | ❌ Reuses generator state, predicates and ordering assumptions. | ✅ Consumes the complete preview without generator search state or constraint predicates. | ❌ Protects only persisted, relationally represented invariants and cannot validate a side-effect-free preview alone. | ⚠️ Independent only when the replay model is separately encoded; imperative checks remain necessary. |
| EC11: validation is correct for non-chronological input | ⚠️ Input order can evade a latest-match-only implementation. | ✅ Canonicalizes the complete input and independently sorts each resource timeline. | ⚠️ Represented constraints are insertion-order independent, but completeness and window rules remain unchecked. | ✅ Set-based fixed assignments are independent of arrival order when every cross-resource constraint is encoded. |
| EC14 and PEC4: actionable, machine-readable violations | ⚠️ Search-oriented fail-fast checks do not naturally return a complete, stable violation list. | ✅ Returns all structured violations in a deterministic public order. | ❌ Constraint failures do not provide a complete multi-violation report for preview. | ⚠️ Solver status is machine-readable, but fixture-level explanations require duplicate diagnostics or unsatisfiable-core machinery. |
| EC15–EC16: capacity, infeasibility, exhaustion and internal failure remain distinct | ⚠️ Coupled checks can reject a candidate but cannot independently classify the final match schedule or prove global infeasibility. | ✅ Keeps `SCHEDULE_INVALID` separate from generation outcomes and maps rejected generator output to `INTERNAL_FAILURE`. | ❌ Database rejection alone cannot classify configuration, capacity, search exhaustion or proven infeasibility. | ⚠️ A fixed preview can be proven invalid, but that result does not prove the generation problem infeasible. |
| EC18: domain logic stays Spring/JPA-free with no unjustified dependency or infrastructure | ✅ Adds no dependency. | ✅ Pure Java validation adds no dependency, schema or infrastructure. | ⚠️ Adds schema structures and can require the PostgreSQL-specific `btree_gist` extension. | ❌ Adds a solver and potentially native runtime artifacts without a demonstrated need for validating at most 66 fixed matches. |
| EC19: deterministic automated verification | ⚠️ Generator and validator defects can share the same predicates and ordering assumptions. | ✅ Supports permutation, mutation and property-style unit tests without a database. | ⚠️ Requires PostgreSQL integration tests and still needs another validator for complete preview coverage. | ⚠️ Requires solver/model tests and separate tests for actionable imperative diagnostics. |
| EC20: unchanged manual scheduling behavior | ✅ Can remain scoped to generated scheduling, although it does not satisfy independent validation. | ✅ Runs only in preview and generated save paths; the manual endpoint keeps its current rules. | ⚠️ Global exclusion and uniqueness constraints can change accepted manual writes unless additional generated-schedule scoping is introduced. | ✅ A separate replay validator does not change the manual endpoint, but adds an unrelated solver dependency. |
| Bounded cost for 1–66 matches | ✅ Incremental checks are bounded, but completeness remains deficient. | ✅ Complete pair and timeline checks are bounded at this scale. | ⚠️ Represented database checks are bounded, but incomplete coverage requires a second mechanism. | ⚠️ Bounded solving is possible, but a solver is disproportionate for a fixed match schedule of this size. |

## Tradeoffs

The principal tradeoff is implementation independence versus additional database or solver machinery.

| Dimension | A. Generator-coupled checks | B. Pure whole-schedule validator | C. PostgreSQL constraints | D. Constraint-model replay |
| --- | --- | --- | --- | --- |
| Integration complexity | Low — checks stay inside generator code, but a second validator is still required to satisfy EC10. | Medium — adds a separate pure validator, stable violation model and invariant tests. | High — requires new occupancy/schema design, Flyway migrations, error translation and a second preview validator. | High — requires solver integration, a separately encoded model and imperative diagnostic checks. |
| Vendor lock-in | Low — pure Java, but generator and validation logic remain coupled. | None — pure Java domain logic and standard REST Problem Details. | Medium — relies on PostgreSQL range/exclusion semantics and potentially `btree_gist`. | High — validation depends on a solver API, status model and native-runtime packaging. |

## Recommendation

**Recommended option: Option B — Independent Pure Whole-Schedule Validator**

Option B satisfies the complete validation boundary without adding schema, infrastructure or a solver dependency. It canonicalizes the full input, validates the EC3 fixture set and ownership rules, and independently sorts every team and football-field timeline for EC4–EC7. That order makes EC6 explicitly end-to-start and makes EC11 insensitive to non-chronological input. The resolved `ZoneId` and daylight-saving policy supplies PEC1's time context.

The validator returns every PEC4 violation in a deterministic public order. It keeps invalid match schedules separate from EC15–EC16 generation outcomes, and maps rejected generator output to `INTERNAL_FAILURE`. The pure Java implementation remains independent of Spring and JPA for EC18, has bounded evaluation cost for at most 66 matches, supports EC19 permutation and mutation tests, and leaves the EC20 manual endpoint unchanged.

Some constraint semantics also exist in generator pruning. That duplication is deliberate: independent validation protects against a generator predicate or search-state defect. Shared invariant test vectors keep the implementations aligned, but the validator does not share generator constraint predicates or search state.

The other options have disqualifying gaps:

| Option | Disqualifying factor |
| --- | --- |
| A. Generator-coupled checks | Fails EC10 because it shares generator state and predicates; append-order implementations can also fail EC11. |
| C. PostgreSQL constraints | Cannot validate a complete side-effect-free preview, adds PostgreSQL-specific schema machinery, and can change existing manual writes. |
| D. Constraint-model replay | Adds a solver and native-runtime cost for a fixed schedule of at most 66 matches, provides weak actionable diagnostics, and cannot treat fixed-preview infeasibility as proof that generation is infeasible. |

This recommendation is falsifiable. Revisit it if an approved future constraint cannot be expressed by independent imperative evaluation, or if measured validation at 66 matches exceeds an approved execution budget.

**Open questions to resolve before implementation:**

| # | Question | Owner | Impact if unresolved |
| --- | --- | --- | --- |
| V-E1 | What necessary theoretical-capacity formula is exposed, and which insufficiency cases does it explicitly not prove? | Architect + Backend | Blocks the correct `INSUFFICIENT_THEORETICAL_CAPACITY` contract. |
| V-E2 | What stable violation codes, ordering and payload fields form the REST and Web contract? | API + Web | Blocks deterministic Problem Details and actionable UI messages. |
| V-E3 | Which microsecond-normalization, daylight-saving, permutation and mutation cases form the shared invariant test corpus? | Backend + QA | Blocks EC19 acceptance evidence and generator-validator alignment. |
