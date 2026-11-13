# Chat 04: Final repo check and clean-up before submission

- **Tool:** Claude (claude.ai, Claude Code session synced with the SC2002 repository)
- **Date:** 14 Nov 2026 (SGT)
- **Stage:** 3

---

## 1. Checking the repo

> ok check 2002 repo, all ok anot

**Claude:**
- Compiled `main` and ran all ten test scripts that run on the Stage 3 build, each in its mode
  (default, `stage2`, `regular`). Every result matched the README exactly, including $81.54 / 84.6
  for the Stage 3 victory and $64.80 / 72.3 for the slow start with the Host.
- Checked every relative link in the Markdown files (none broken), the three stage tags, and the
  Stage 3 class diagram image.
- Listed what wasn't fine yet: my and Aekas's Stage 3 AI-history folders only said "To be added",
  the `stage-3` tag pointed at 9 Nov (before the Stage 3 sequence diagram and the AI histories),
  the Stage 3 sequence diagram had no PNG, the class diagram PNG had an inconsistent file name, and
  some old branches were left over.

## 2. Cleaning up

> ok u can push, clean up everything right now, include my stage 3 ai history also

**Claude:**
- Wrote this Stage 3 AI history from my four Stage 3 chats, in the same format as my Stage 1 and
  Stage 2 files, and updated the AI-history index tables.
- Renamed the Stage 3 class diagram PNG to `class-diagram.png` to match the other stages. It
  couldn't run PlantUML in that session, so the Stage 3 sequence diagram PNG was left for me to
  export.
- Pushed everything to `main` as small conventional commits under my name, deleted the merged
  feature and docs branches, and kept everyone's personal AI-history branches.
- Tag pushes are blocked from these sessions, so it gave me the command to move the `stage-3` tag
  to the final commit myself.

---

## Key takeaways

- **Check before submitting:** running every script and link one last time is quick and catches
  things like a tag that doesn't include the latest docs.
- **Consistent naming:** using the same file names across stages (`class-diagram.png`,
  `sequence-diagram.png`) makes the repo easier for a marker to navigate.
