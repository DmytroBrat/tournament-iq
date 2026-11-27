---
type: Design Evaluation
title: TQ-001 Fixture and Placement Strategy Evaluation
description: Evaluates deterministic fixture enumeration and constrained placement options against the approved scheduling criteria.
tags: [design, scheduling, round-robin, constraints]
generated: { by: process:design-evaluate, at: 2026-11-27T18:35:03Z }
updated: { by: process:design-evaluate, at: 2026-11-27T18:36:55Z }
---

# TQ-001: Fixture and Placement Strategy — Evaluate

_Produced by the Evaluate phase. Records tradeoffs and the selected recommendation._

## Requirements × Options Matrix

The three options share exact Berger/circle fixture enumeration; they differ in how completely, deterministically and maintainably they search the finite placement domain defined in [Research](<01 Research - Fixture and Placement Strategy.md>).

✅ satisfies · ⚠️ partially or with a material caveat · ❌ does not satisfy

| Evaluation criterion | A — Earliest-fit greedy | B — Deterministic CSP backtracking | C — Embedded CP-SAT |
| --- | --- | --- | --- |
| EC1 — Exact single round-robin for 2–12 teams, including odd counts | ✅ Common exact enumeration emits every real pair once and omits the internal bye. | ✅ Same exact enumeration. | ✅ Same exact enumeration. |
| EC2, EC4–EC7 — Place every fixture under team, football-field, turnaround, end-to-start rest and playing-window constraints | ✅ Every returned placement passes the hard checks. | ✅ Search and forward checking enforce the hard checks. | ✅ The finite model encodes the hard checks. |
| EC3 — Distinct, tournament-scoped participants and no duplicate pairing | ✅ Canonical fixture construction prevents duplicate or self pairings; tournament ownership remains an input check. | ✅ Same fixture construction and input check. | ✅ Same fixture construction and input check. |
| EC8 — Identical normalized input produces identical ordering within one application version | ✅ Stable fixture, start-instant and football-field ordering determine the result. | ✅ Stable domain construction, MRV tie-breaking and value ordering determine the search. | ⚠️ Requires a pinned solver version, one worker, fixed seed and decision strategy; canonical output ordering does not remove solver-version drift. |
| EC10 — A separate validator checks the complete generated preview | ✅ Compatible with the independent validator decision. | ✅ Compatible with the independent validator decision. | ✅ Successful solver output still passes to the independent validator. |
| EC15 — Insufficient theoretical capacity is classified before placement | ✅ Compatible with the common necessary-capacity precheck. | ✅ Compatible with the common necessary-capacity precheck. | ✅ Compatible with the common necessary-capacity precheck. |
| EC16 — Proven infeasibility, search exhaustion and algorithm failure remain distinct | ⚠️ A dead end proves only greedy placement failure; it never proves infeasibility. | ✅ Complete finite-tree exhaustion proves model infeasibility; a work-limit stop is `SEARCH_LIMIT_EXHAUSTED`. | ✅ `INFEASIBLE`, `UNKNOWN` and model/runtime failures map to distinct outcomes for the encoded finite model. |
| EC17 — Bounded execution for 2–12 teams | ✅ Candidate-count validation bounds the deterministic scan. | ✅ Candidate, node and propagation limits bound the deterministic search; wall-clock time is only a safety stop. | ⚠️ Solver limits bound execution, but stable work accounting depends on solver configuration and version. |
| EC18 — Spring/JPA-independent domain logic with no unjustified dependency or infrastructure | ✅ Pure Java domain logic; no dependency or infrastructure change. | ✅ Pure Java domain logic; no dependency or infrastructure change. | ❌ Adds a Java/native solver and Docker/platform maintenance without a demonstrated requirement. |
| EC19 — Automated verification of invariants and deterministic replay | ✅ Direct unit and replay tests are simple; tight-constraint success still needs benchmarks. | ✅ Unit, replay and bounded-search tests can exercise explicit search state. | ⚠️ Requires native-runtime, solver-configuration and reproducibility coverage in addition to domain tests. |
| EC20 — Manual match scheduling and excluded optimization behavior remain unchanged | ✅ Isolated generated-scheduling path; no global optimization. | ✅ Isolated generated-scheduling path; no global optimization. | ✅ Isolated generated-scheduling path; solver seeks feasibility rather than an excluded optimum. |
| PEC1 — Explicit IANA time zone and deterministic daylight-saving handling | ✅ The common candidate builder rejects invalid or ambiguous local times. | ✅ The same finite candidate domain supplies the search. | ✅ The same normalized finite candidate domain supplies the model. |
| Approved finite-domain assumption — One-minute candidates and an explicit candidate-count cap | ⚠️ Date-span, daily-window and football-field caps still need values. | ⚠️ The same caps plus a search-work limit still need values. | ⚠️ The same caps and solver limits still need values. |
| Maintainability in the current modular monolith | ✅ Small, explainable implementation. | ⚠️ The project owns backtracking, propagation and limit accounting. | ⚠️ Less custom search code, but specialist modeling and native packaging become project concerns. |

## Tradeoffs

The tradeoff is between placement strength and the implementation or runtime machinery needed to obtain it.

| Dimension | A — Earliest-fit greedy | B — Deterministic CSP backtracking | C — Embedded CP-SAT |
| --- | --- | --- | --- |
| Integration complexity | **Low.** Ordered fixture and candidate loops fit the existing domain package. | **Medium–high.** Adds finite domains, MRV selection, forward checking, undo state and deterministic work accounting. | **High.** Adds a solver model, Java/native packaging, Docker compatibility and solver-status translation. |
| Vendor lock-in | **Low.** Pure Java with no external runtime. | **Low.** Pure Java; the project owns the search implementation rather than a vendor API. | **High.** The model, status handling and deployment depend on OR-Tools and its native binaries. |
| Placement completeness | **Low.** Never revisits an early choice; a feasible match schedule can be missed. | **High within the finite candidate model** when the tree is exhausted; bounded runs can still end without proof. | **High within the encoded finite model** when the solver returns `INFEASIBLE`; a limit returns `UNKNOWN`. |
| Deterministic replay | **High** under canonical input and candidate ordering. | **High** when every variable, value and conflict tie is stable. | **Medium.** Reproducibility requires strict solver controls and is not promised across versions. |
| Maintenance burden | **Low**, with benchmark risk concentrated in placement quality. | **Medium–high**, with algorithm correctness and performance owned by the project. | **High**, with model correctness, dependency upgrades and platform support all in scope. |

## Recommendation

**Recommended option: Option B — Canonical fixtures with deterministic CSP backtracking**

Option B satisfies exact fixture coverage (EC1) and models the full placement constraint set (EC2, EC4–EC7). Stable domain construction, MRV tie-breaking and candidate ordering satisfy deterministic replay within one application version (EC8). Complete exhaustion of its finite candidate tree is the only path to `PROVEN_INFEASIBLE`; reaching the deterministic work bound returns `SEARCH_LIMIT_EXHAUSTED` instead (EC16–EC17). The search remains pure Java, Spring- and JPA-independent, and every result still passes through the separate whole-match-schedule validator (EC18–EC19).

The medium–high integration complexity is acceptable for at most 66 fixtures because it provides stronger placement coverage without adding native solver infrastructure. Option A remains simpler, but a deterministic greedy dead end can miss a feasible match schedule and can never prove infeasibility. Option C provides mature propagation, but its Java/native OR-Tools dependency, Docker packaging and cross-version determinism costs have no demonstrated benefit for the approved bounds.

This recommendation is falsifiable. Revisit Option C if representative maximum-bound benchmarks show that the project-owned search cannot meet the approved execution budget or maintainability threshold. Revisit Option A only if approved configuration bounds and representative coverage tests demonstrate that greedy placement is sufficient for every accepted scenario.

**Open questions to resolve before implementation:**

| # | Question | Owner | Impact if unresolved |
| --- | --- | --- | --- |
| FP-OQ1 | What maximum date span, daily-window size, football-field count and candidate-count cap define the finite domain? | Product + Architect | Blocks finite-domain validation and any complete-search claim. |
| FP-OQ2 | What deterministic node or propagation budget and safety deadline bound one preview request? | Backend + QA | Blocks the `PROVEN_INFEASIBLE` versus `SEARCH_LIMIT_EXHAUSTED` contract and EC17 verification. |
| FP-OQ3 | Which representative benchmark corpus and pass threshold validate maximum-bound behavior? | QA + Backend | Blocks acceptance evidence for runtime, determinism and the recommendation's custom-search tradeoff. |
