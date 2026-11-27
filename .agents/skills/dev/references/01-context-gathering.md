# Context Gathering stage

Apply `.agents/skills/dev/patterns/metrics.md`; this stage's metrics unit is
`--stage context`. Metrics start in Step 2b below, not at the end of the stage.

Routing and stage tracking live in `.agents/skills/dev/SKILL.md`. This stage
captures what the ticket actually means, once: from any approved design,
and the developer's own words. It writes that down as `0-intent.md` before any branch
or plan work starts. Read-only apart from the story folder, the report skeleton and
the intent file.

One stop in this stage: the sign-off in Step 5 on the written intent. There is no
commit here; the story folder is committed by Ceremony Step 4, after the branch
checkout, so the lifecycle's first commit lands on the feature branch. The sign-off
(`patterns/phase-sign-off.md`). It covers both the gathered context and the intent
written from it; gaps and conflicts are asked there, as open questions in the file,
not earlier. Everything planned later derives from that file.

> 🔁 **After context compaction:** re-read this file and run metrics `recover`
> before recording anything (`patterns/session-continuity.md`). Fresh vs. resume was
> already decided by the router's entry check; do not redo completed work.

## Step 1 — Resolve the local story

Use the invocation description or an existing DEV-YYYYMMDD-XXXX story identifier. Reuse a uniquely matching story; otherwise generate a collision-checked local ID. Capture the problem, the developer’s initial thoughts and relevant source paths. If the outcome or acceptance criteria are unclear, ask the developer before creating the story artifacts.

## Step 2b — Create the story folder, report skeleton and start metrics

Do this now, before the heavy reading. The story name is known at this point: the
ticket summary, or a short slug of the free-form problem.

- Glob for `docs/Stories/*{TICKET}*/` (use `*{TICKET}*` wildcard — never `[{TICKET}]*`)
- If a design folder already exists (e.g. `[{TICKET}] {name}/`), create `impl/` inside it
- Otherwise create `docs/Stories/[{TICKET}] {name}/impl/`

The folder name is **stable for the life of the ticket**. Never rename it. Phase
state lives in the `Phase` row of `2-report.md`, not in the folder name. Use only
ASCII characters in the name: no emoji, no em dash. Some scanners cannot index such
folders.

Create `impl/2-report.md` from `.agents/skills/dev/templates/report.md`. Replace the template's own meta block with the fenced `yaml` frontmatter under it, without the fences (Documentation Principles, "Templates"). Then:

- `{TICKET}` and `<Short Title>` — from the ticket / intent title
- Header table — fill Ticket and Author (`git config user.name`); Branch is `—` until
  Ceremony sets it; PR `—`; Status `🔄 In Progress`; Phase
  `🎯 Context 🔄 · 🚀 Ceremony ⏳ · 📋 Plan ⏳ · 🔨 Implement ⏳ · 🧪 Test ⏳ · 📝 Document ⏳ · 🏁 Completion ⏳`
- Leave the Progress table empty; Plan fills it

Then mark the Context stage's native task `in_progress`
(`patterns/task-tracking.md`), and initialise metrics and start the stage, reusing
the same `git config user.name` value:

```sh
node .agents/skills/dev/scripts/timing.mjs init --dir "$IMPL_DIR" --ticket "$TICKET" --author "$AUTHOR"
node .agents/skills/dev/scripts/timing.mjs stage start --dir "$IMPL_DIR" --stage context
```

Everything from here on is recorded as Context work.

## Step 3 — Check for approved design

- Glob for `docs/Stories/design/approved/*{TICKET}*/solution/Architecture.md`
  (use `*{TICKET}*` wildcard) — an approved design lives under an `approved/`
  subfolder of one of the two design-lifecycle roots (under `design/`), never directly under the
  root (still active) or its `declined/` subfolder
- If found, read the story's `design/Report.md` and the paths in its
  `selected_artifacts`. Those approved files are the spec. Do not assume a fixed
  Architecture/Design/Stack/Efforts set.

If found, tell the developer:

> "Found approved design for `{TICKET}` — reading `solution/Architecture.md` and the selected solution artifacts as the source of truth for intent and planning."

The solution files are the primary spec for the intent. Developer input and agreed acceptance criteria
supplement; they do not override the solution. If nothing is found, continue; intent
comes from the story and the developer alone.

## Step 3.5 — Verify uncertain platform/framework conventions

If the intent depends on a specific external platform, service or framework
convention (required file layout, config keys, manifest fields, API shape) and you
are not sure about it, fetch the official docs first: `WebFetch` the vendor page if
the URL is known, or `WebSearch` to find it. Skip this for things you know well
(a standard `package.json`, common language idioms). This is for cases where getting
it wrong is plausible, not a mandate to fetch docs for everything.

Cite what you consulted (the URL) in the intent's sources.

## Step 3.6 — Identify the HLD container(s) in scope

Read `docs/Target Architecture (HLD)/README.md` to find the active version and its
container index, then name the container(s) this ticket falls under. This is a
quick identification, not the deep read; that happens in Plan Step 1. The point is
to catch an obvious boundary mismatch (scope that belongs to a different container,
or spans containers unexpectedly) while it is still cheap to fix.

Include the container(s) in the intent. If the scope does not map cleanly,
say so rather than silently picking one.

## Step 3.7 — Query the operational graph for related docs

Before searching by hand, attempt
`node docs/.og/cli.mjs query --related-to <doc> --dir .` (or `search --term <text>`)
against `docs/.og/docs-operational-graph.sqlite` to surface docs already related to this ticket's
scope. This is a required (not best-effort) fallback: when the graph is missing,
stale (see `references/reindex-graph.md`'s Staleness check), or the call errors, fall back to grep/glob over `docs/` for the same
information. Either way, never let this block the stage.

## Step 4 — Write 0-intent.md

Write `impl/0-intent.md` following `.agents/skills/dev/interfaces/intent.md`.
Give it B+ frontmatter with `type: Dev Intent` and `generated`/`updated` set to
`{ by: process:dev-context, at: <UTC now> }` (Documentation Principles).
That file is an interface, not a template: it lists the questions the intent must
answer (problem, what better looks like, who is affected, out of scope, constraints,
open questions) and leaves the shape to you. Write in the originator's own words,
link every source, keep it short. No steps, no acceptance criteria, no solution;
those belong to the plan.

Write it even when the context is incomplete. Anything you could not settle goes in
as an open question, with what you found so far: a missing description or
acceptance criteria, a conflict between approved design and the requested outcome, a
boundary concern from Step 3.6, a design change the developer hinted at. The
developer answers those in the sign-off, against the written file, not against a
chat summary.

## Step 5 — Sign-off

Apply `patterns/phase-sign-off.md`. The developer confirms the written
intent, not a chat summary (`patterns/write-confirm.md`). Present:

> "Intent written to `docs/Stories/[{TICKET}] {name}/impl/0-intent.md`. In short:
>
> - **Problem**: `<one line>`
> - **Better looks like**: `<one line>`
> - **Affected**: `<users / apps / container(s)>`
> - **Out of scope**: `<one line or 'nothing stated'>`
> - **Constraints**: `<one line or 'none'>`
> - **Open questions**: `<list or 'none'>`
> - **Sources**: `<ticket, pages, design, your message>`
>
> Is this the right intent? Anything missing, wrong, or out of scope before I
> plan against it? {If there are open questions: "I need your answer on the open
> questions above before Plan."}"

`report`, then `wait start --reason approval --detail "Awaiting context sign-off"`.
**Stop and wait.** No reply, no next step. On approval, record the acceptance check
(`--name "Context sign-off"`, `--source developer`, their words as evidence); without
it `stage complete` is refused.

Rework: update `0-intent.md` with the answers and present again. Once approved, the
intent is confirmed; only the open questions may be appended later.

## Step 6 — Report

Set the `Phase` row to
`🎯 Context ✅ · 🚀 Ceremony 🔄 · 📋 Plan ⏳ · 🔨 Implement ⏳ · 🧪 Test ⏳ · 📝 Document ⏳ · 🏁 Completion ⏳`,
then metrics `report`.

## Step 7 — Close

No commit in this stage. `stage complete --stage context`. Continue to Ceremony for a
full-flow request. For direct entry stop here and say that the story folder is
uncommitted on purpose until `$dev ceremony {TICKET}` checks out the
branch and proposes the first commit.

🔁 Re-read `.agents/skills/dev/SKILL.md` and resume the router from the next stage (Ceremony).
