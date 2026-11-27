# Stage reference router

Resume detection, how approvals work, the Phase tracker, and stage tasks live in
[SKILL.md](../SKILL.md). Each row below is loaded only when its stage runs.

| Stage             | Reference                                          | Produces                                                                                                                                                                                       |
| ----------------- | -------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Context Gathering | [01-context-gathering.md](01-context-gathering.md) | Ticket/local ID resolved, track/subfolder resolved; story folder + `design/Report.md` created                                                                         |
| Ceremony          | [02-ceremony.md](02-ceremony.md)                   | Confirmed branch                                                                                                                                                          |
| Frame             | [03-frame.md](03-frame.md)                         | `design/00 Frame.md`                                                                                                                                                                           |
| Research          | [04-research.md](04-research.md)                   | `design/01 Research.md` (or per-decision variants), Assumptions recorded                                                                                                                       |
| Evaluate          | [05-evaluate.md](05-evaluate.md)                   | `design/02 Evaluate.md`, recommendation and open questions                                                                                                                               |
| Solution          | [06-solution.md](06-solution.md)                   | Selected solution artifacts recorded in `design/Report.md`; solution quality gate                                                                                            |
| Review Gate       | [07-review-gate.md](07-review-gate.md)             | Draft MR opened, `design/Report.md` Gate recorded, parked for approval                                                                                                                         |
| Address Feedback  | [08-address-feedback.md](08-address-feedback.md)   | Confirms the approval outcome; if concerns, reiteration applied and re-approval recorded; if declined, routes straight to Completion; if approved outright, skips itself and routes to Publish |
| Publish           | [09-publish.md](09-publish.md)                     | Permanent docs updated — skipped entirely for a declined design                                                                                                                                |
| Completion        | [10-completion.md](10-completion.md)               | Implementation tickets published, story archived (`approved/` or `declined/`), commit + PR handed over                                                                         |

Context Gathering and Ceremony are separate stages, mirroring the `dev` skill:
Context Gathering only reads and resolves (local ID and architecture subfolder);
Ceremony is the only stage that checks out a branch.
Frame then writes the framing content using what Context Gathering resolved.

Design-lifecycle artifacts live under `{stories root}` — active at
`{stories root}[{TICKET}] {name}/`, archived at
`{stories root}approved/[{TICKET}] {name}/` or
`{stories root}declined/[{TICKET}] {name}/`. `{stories root}`
is `docs/Stories/design/`. Read the resolved paths from the design report.

Shared protocols used across these stages live in [patterns/](../patterns/). Lifecycle
templates live in [templates/](../templates/); solution artifacts use the adaptive
contracts in [interfaces/](../interfaces/).

[00-decision-router.md](00-decision-router.md) is not a stage — it has no row
above and no entry in the Phase tracker. It's the shared loop mechanics
Research, Evaluate, and Solution each apply when Frame confirmed two or more
decisions: which steps loop per decision, and which close once regardless of
how many.
