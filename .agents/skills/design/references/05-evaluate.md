# Evaluate stage

Apply `.agents/skills/design/patterns/metrics.md`; this stage's metrics unit is
`--stage evaluate`. First action, at Step 1: `stage start --stage evaluate`.
Follow the pattern's wait, check, and exit commands for everything after. When
this reference is used only as a correction recipe within another active
stage, retain that stage's metrics ownership.

Resume detection and stage tracking live in `.agents/skills/design/SKILL.md`.
Evaluate is the judgment phase. It maps requirements against options, surfaces
tradeoffs for integration complexity and vendor lock-in and makes a recommendation. Output: `design/02 Evaluate.md`.

> ⚠️ Follow every step in order. Do not skip, summarize, or improvise. If you are unsure, ask before deviating.

> 🔁 **Session continuity:** apply `.agents/skills/design/patterns/session-continuity.md`.

## Step 1 — Read inputs

`$DESIGN_DIR` comes from the router's Resume Detection.

Read in full:

- `design/Report.md` — restore Assumptions recorded during Research
- `design/00 Frame.md` — ECs (both known and accepted proposed)
- `design/01 Research.md` — the options

If either Frame or Research is missing, tell the developer which phase to complete first and stop.

**Multi-decision check:** if `design/00 Frame.md`'s Decisions section lists two
or more decisions confirmed at Frame's dedicated decisions sign-off (Step 2),
apply `.agents/skills/design/references/00-decision-router.md` for
the loop boundary (Steps 1b–6 loop, Steps 7–9 close once) and
`.agents/skills/design/patterns/multi-decision-split.md` for the
file-split policy. Already decided in Frame; do not re-detect or re-ask here.

---

## Step 1b — Resolve viability OQs before the matrix

Read the Open Questions table in `design/01 Research.md`. This is an
intelligent-approval checkpoint (`patterns/approval-intelligent.md`).
Auto-approval condition: no OQ classified **(V) Viability** is still unresolved.

Condition met → continue to Step 2 without asking.

Unresolved `(V)` OQs exist → this is a sign-off gate (`patterns/approval-sign-off.md`):

> "The following viability OQs from Research are still open and would block the matrix if left unresolved: [list]. Can you answer any of these now, or should I mark the affected option as conditional?"

**Stop and wait.** Record resolutions in `design/01 Research.md` before building
the matrix, or mark affected options conditional.

See `.agents/skills/design/patterns/design-oq-triage.md` for the full protocol.

---

## Step 2 — Build the Requirements × Options matrix and Tradeoffs table

**Matrix:** Rows = evaluation criteria derived from the requirements and constraints in `design/00 Frame.md` (functional requirements, NFRs, architectural constraints, operational concerns). Columns = every option from `design/01 Research.md`. Cell values: ✅ (yes) · ⚠️ (partially / with caveats) · ❌ (no). Add a brief note in any ⚠️ or ❌ cell.

> **SPIKE tickets** — the requirements listed in the ticket are often meta-level documentary requirements ("assess X", "score against 5 criteria"), not technical evaluation axes. Derive the actual evaluation criteria from the named dimensions (e.g. implementation simplicity, delivery cost, infra running cost, runtime performance, Azure stack alignment) and use those as rows.

**Tradeoffs table:** Use Integration Complexity and Vendor Lock-in columns; give a brief rationale for each rating.

---

## Step 2b — Write the draft

The matrix + tradeoffs are already the bulk of the draft content and too large to
review comfortably in the console — that review happens once, in the IDE, at the
end-of-phase sign-off (Step 7), together with the recommendation. Apply the
docs-writer authoring pattern (`.agents/skills/design/patterns/docs-writer-authoring.md`):
delegate the write of `design/02 Evaluate.md` (matrix + tradeoffs, recommendation
section left as a placeholder, using `.agents/skills/design/templates/evaluate.md` with its meta block replaced by the fenced `yaml` frontmatter under it)
to `@docs-writer` using the selected delegation procedure. Continue straight to
Step 3 — do not stop here.

---

## Step 3 — Write the Recommendation

State which option is recommended and why — explicitly referencing:

- Which ECs it satisfies best (from the matrix)
- Which tradeoffs are acceptable and why (from the tradeoffs table)
- What the disqualifying factors are for the other options

The recommendation must be falsifiable: if a stated assumption later turns out wrong, the recommendation can be revisited.

Also list the **open questions** that must be resolved before implementation begins — with a suggested owner for each.

---

## Step 4 — Fill the recommendation in 02 Evaluate.md

Apply the docs-writer authoring pattern (`.agents/skills/design/patterns/docs-writer-authoring.md`): delegate filling the recommendation placeholder (from Step 3) and the open questions with their owners to `@docs-writer` using the selected delegation procedure. Continue straight to Step 5 — do not stop here.

**Reiterate paths**, for use from Step 7's sign-off if the developer raises a concern:

- Missing option in the matrix → "Reiterate from Research, Step 2 — re-delegate to `{resolved architect agent}` with the specific gap."
- Wrong or missing EC → "Reiterate from Frame, Step 1 — re-draft the Success section."
- Tradeoff cell wrong → "Reiterate from Evaluate, Step 2 — rebuild the tradeoffs table with the correction."
- Recommendation needs revisiting after a fix → "Reiterate from Evaluate, Step 4 — restate the recommendation against the updated table."

---

## Step 6 — Save Evaluate

Save the requirements matrix, integration complexity and vendor lock-in tradeoffs, recommendation and open questions in `design/02 Evaluate.md`. Carry unresolved decisions with their owners to sign-off.

## Step 7 — Sign-off

Apply `patterns/phase-sign-off.md`. Present the recommendation, tradeoffs, and named open risks for each decision in one message. Ask the developer to approve or request changes.

`report`, then `wait start --reason approval --detail "Awaiting evaluate sign-off"`.
**Stop and wait.** On approval, record the acceptance check
(`--name "Evaluate sign-off"`, `--source developer`); without it `stage complete`
is refused.

Rework: apply the named reiterate path, present again.

## Step 8 — Report

Update `design/Report.md`:

- Phase tracker: `🎯 Context Gathering ✅ · 🚀 Ceremony ✅ · 📋 Frame ✅ · 🔍 Research ✅ · 🏗 Evaluate ✅ · 📦 Solution 🔄 · 🔎 Review ⏳ · 💬 Address Feedback ⏳ · 📤 Publish ⏳ · 🏁 Completion ⏳`
- Append to Efforts table: `| {YYYY-MM-DD} | Evaluate | Recommended <option name> |`

Then metrics `report`.

## Step 9 — Commit

Propose:

> `docs({TICKET}): evaluate, recommend <option name>`

This is a manual-operation sign-off (`patterns/approval-sign-off.md`) — the
developer runs the commit, never you. `wait start --reason user_input`.
**Stop and wait.** On the reply, check `git log`/`git status`: the commit exists
and the worktree holds nothing from this phase. If not, say what is missing and
wait again. Then `stage complete --stage evaluate`.

🔁 Re-read `.agents/skills/design/SKILL.md` and resume the router from the next stage.
