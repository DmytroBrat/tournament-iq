# Completion stage

Apply `.agents/skills/dev/patterns/metrics.md`; this stage's metrics unit is
`--stage completion`. First actions: mark this stage's native task `in_progress`
(`patterns/task-tracking.md`), then `stage start --stage completion`.

Routing and stage tracking live in `.agents/skills/dev/SKILL.md`. This is the
last stage. It makes "done" unambiguous: archive the story, finalise metrics, hand
the developer the commit and the PR text, record local technical debt,
and say clearly that the ticket is finished.

Run it directly with `$dev complete {TICKET}` for any story that
reached Document but was never archived.

One stop in this stage: the combined ask in Step 4. Everything else is routine.

> 🔁 **After context compaction:** re-read this file and run metrics `recover`
> before recording anything (`patterns/session-continuity.md`). Fresh vs. resume was
> already decided by the router's entry check; do not redo completed work.

## Step 1 — Check the story is ready

`$IMPL_DIR` comes from the router's entry check.

- If it is already under `docs/Stories/archive/`, say so and continue from Step 3
  with the archived path; nothing is moved twice
- Read `2-report.md`. The `Phase` row must show Document ✅. If not, tell the
  developer which stage is still open and stop; do not archive unfinished work

Run `report`, then `quality validate --dir "$IMPL_DIR"`. If it fails, the story is
not closable: report what it rejected and stop. The fix belongs in Document.

Update the `Phase` row:

```
🎯 Context ✅ · 🚀 Ceremony ✅ · 📋 Plan ✅ · 🔨 Implement ✅ · 🧪 Test ✅ · 📝 Document ✅ · 🏁 Completion 🔄
```

## Step 2 — Archive

Move the whole story folder, keeping every artifact including `impl/stat.json`:

```sh
git mv "docs/Stories/[{TICKET}] {name}" "docs/Stories/archive/[{TICKET}] {name}"
```

Use a plain move if the folder is untracked. Never overwrite an existing archive
destination; if one exists, stop and ask. Keep the folder name exactly as it was:
ASCII only, no status marker, no emoji, no em dash. Done is recorded in the report's
Status row, not in the folder name.

Moving adds one directory level. Fix every relative Markdown link inside the moved
files that points back up to `docs/`. Check them, do not assume.

From here on, use the archived path for every metrics command.

## Step 3 — Update the report

In `2-report.md`:

- Status → `✅ Complete`
- Phase → `🎯 Context ✅ · 🚀 Ceremony ✅ · 📋 Plan ✅ · 🔨 Implement ✅ · 🧪 Test ✅ · 📝 Document ✅ · 🏁 Completion ✅`
- Add a `## Completion` section: date, archive path, and a placeholder for the PR
  title and the external actions (filled in Steps 4 and 5)

Then metrics `report`.

## Step 4 — Sign-off: archive and follow-up

Summarize the archive and any remaining local technical debt. Ask the developer to confirm the outcome. Record `report` and `wait start --reason approval --detail "Awaiting completion sign-off"`.

**Stop and wait.** On reply, `resume --activity documentation`; record `Completion sign-off` as an acceptance check with developer evidence. Keep agreed follow-up in the local ledger.

## Step 5 — Finalise metrics

```sh
node .agents/skills/dev/scripts/timing.mjs stage complete --dir "$IMPL_DIR" --stage completion
node .agents/skills/dev/scripts/timing.mjs complete --dir "$IMPL_DIR"
node .agents/skills/dev/scripts/timing.mjs report --dir "$IMPL_DIR"
```

`complete` fails if any stage or step is still open. That is the point: fix the
cause, do not work around it. For a direct-stage run on a story with partial
coverage, `complete` still records the end; that is correct.

## Step 6 — Commit, PR text, and say it is done

Nothing waits after this; the lifecycle is over. Hand the developer everything in
one message. The archive commit:

```
docs({ticket-lowercase}): archive {TICKET} and close the story
```

The PR title, Conventional Commit form, scoped to the main change:

```
{type}({scope}): {what the ticket delivered}
```

The PR body, from the report and the real evidence, not from the plan's intentions:

```markdown
## What

<2–4 lines: what changed and why, for a reviewer who has not read the ticket>

## Verification

- <check> — <result, with the report reference>
- <manual item the developer confirmed>

## Notes for reviewers

- <divergence from plan, accepted deviation, or known debt — one line each>

Story: `docs/Stories/archive/[{TICKET}] {name}/impl/2-report.md`
Closes {TICKET}
```

Pushing and opening the PR are the developer's (`git push` is guarded). When they
share the PR link, put it in the report's PR row; it can ride along in a small
follow-up commit.

Mark the Completion stage's native task `completed` now (`patterns/task-tracking.md`):
the banner shows it done and no later stage exists to do it. Check the native tracker
shows no open task for this ticket.

Then the closing message. This is the signal the team asked for; do not bury it.

> "🏁 **{TICKET} is complete.**
>
> - Archived: `docs/Stories/archive/[{TICKET}] {name}/`
> - Tech debt tickets: {keys / none}
> - Elapsed {Xh Ym} · work {Xh Ym} · wait {Xh Ym} (from `stat.json`)
>
> Remaining on your side: commit, push, open the PR with the text above.
> If anything surprised you this ticket, `$evolve reflect <one sentence>` captures it."

Direct-stage entry ends here. For a full-flow request this is also the end; there is
no further stage.
