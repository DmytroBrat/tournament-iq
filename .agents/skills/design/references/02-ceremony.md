# Ceremony stage

Apply `.agents/skills/design/patterns/metrics.md`; this stage's metrics
unit is `--stage ceremony`. First action: `stage start --stage ceremony`.

Resume detection and stage tracking live in `.agents/skills/design/SKILL.md`.
This stage is the official start of work: assign and transition the ticket via
content — that's Frame's job.

Two stops in this stage: the branch checkout in Step 2, which is the phase
sign-off, and the commit in Step 4. Step 1 is an intelligent checkpoint
(`patterns/approval-intelligent.md`).

> 🔁 **Session continuity:** apply `.agents/skills/design/patterns/session-continuity.md`.

## Step 2 — Sign-off: the feature branch

Apply `patterns/phase-sign-off.md`. If you are already on the feature branch for
this ticket, say so; that is a fact from Git, not a decision, and the sign-off
is just "we're on `{branch}`, continuing". Otherwise the branch is where all the
work lands, so the developer confirms it. Checkout is their operation; never run
`git checkout`/`git switch` yourself. Propose the exact command:

> "Ticket is {assigned / transitioned / unchanged}. Please check out the feature
> branch for this work:
>
> ```bash
> git checkout -b docs/{ticket-lowercase}-{slug}
> ```
>
> or tell me a different name. Let me know once you're on it."

If the worktree has unrelated changes, say so in the same message instead of
proposing a switch that would conflict. Never discard or stash the developer's
changes. On the reply, check the branch with Git before continuing; if it is
not the one agreed, say so and wait again. Then record the acceptance check
(`--name "Ceremony sign-off"`, `--source developer`, evidence: the branch name
they confirmed); without it `stage complete` is refused. If you were already on
the branch, record it with `--source self` and the branch name; that is the one
sign-off the developer gave implicitly, by being there.

## Step 3 — Report

Set the `Branch` row to the actual branch and the `Phase` row to
`🎯 Context Gathering ✅ · 🚀 Ceremony ✅ · 📋 Frame 🔄 · 🔍 Research ⏳ · 🏗 Evaluate ⏳ · 📦 Solution ⏳ · 🔎 Review ⏳ · 💬 Address Feedback ⏳ · 📤 Publish ⏳ · 🏁 Completion ⏳`.
Add a one-line note if the ticket was auto-assigned. Then metrics `report`.

## Step 4 — Commit

The lifecycle's first commit, on the branch just checked out. Propose
`docs({TICKET}): gather context and record branch` covering
`{stories root}[{TICKET}] {name}/design/` (Report.md, stat.json) and this stage's
row. If the story folder was already committed earlier (a resumed design), propose
only `docs({TICKET}): record the story branch`.

This is a manual-operation sign-off (`patterns/approval-sign-off.md`) — the
developer runs the commit, never you. `wait start --reason user_input`.
**Stop and wait.** On the reply, check `git log`/`git status`: the commit exists
and the worktree holds nothing from this phase. If not, say what is missing and
wait again. Then `stage complete --stage ceremony`. Tell the developer which
branch you're on and continue to Frame for full-flow entry.

🔁 Re-read `.agents/skills/design/SKILL.md` and resume the router from the next stage (Frame).
