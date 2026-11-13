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
- Renamed the Stage 3 class diagram PNG to `class-diagram.png` to match the other stages.
- Pushed everything to `main` as small conventional commits under my name, and kept everyone's
  personal AI-history branches. Branch deletion and tag pushes are blocked from these sessions,
  so it gave me the commands, and I moved the `stage-3` tag myself.

## 3. The Stage 3 sequence diagram PNG

> wait how did we do for previous sequence diagrams? follow the same method

> Just do it

**Claude:**
- Checked how the Stage 1 and 2 PNGs were made: rendered from their `.puml` with PlantUML, saved
  next to the source and committed with a README link.
- Rendered `docs/uml/stage3/sequence-diagram.png` the same way, checked the whole diagram was
  there (start-up to the VICTORY/DEFEAT check), and pushed it with the README link as two small
  commits.

---

## Key takeaways

- **Check before submitting:** running every script and link one last time is quick and catches
  things like a tag that doesn't include the latest docs.
- **Consistent naming:** using the same file names across stages (`class-diagram.png`,
  `sequence-diagram.png`) makes the repo easier for a marker to navigate.
