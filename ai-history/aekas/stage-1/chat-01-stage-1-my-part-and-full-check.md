# Chat 01: Stage 1, my part and full check

- **Tool:** Claude (Claude Code extension in VS Code)
- **Dates:** 28–29 Oct 2026 (SGT)
- **Stage:** 1

A selection of the chat, not the full transcript. My requests are written up in my own words,
keeping what I asked each time; short follow-up messages are merged into the request
they belong to. Claude's replies are summarised in my own words.

---

## 1. Implementing my Stage 1 classes (28 Oct)

**My request:** Using the attached Stage 1 plan, read the repository and implement
only my assigned classes (`Staff`, `Waiter`, `Cashier`, `Table`, `GameStats`) plus the key
abstractions and SOLID draft. Keep it simple, don't touch any teammate's classes, and explain
what you did.

**Claude:** Read the repo and the plan, then wrote only my classes:
- `Staff`: the shared one-task-per-turn rule. `checkCanAct()` runs first and `markActed()`
  last, so a rejected choice never uses up the task.
- `Waiter` (`seat`, `takeOrder`, `serve`) and `Cashier` (one payment per turn, earliest
  ready-to-pay first), both throwing `InvalidActionException` with a reason.
- `Table` and `GameStats`. Money is in integer cents, and the paid count comes from the list of
  paid satisfactions, so it can't drift from the average.
- The key abstractions table, the SOLID draft and design-log entries.

It compiled the project and flagged a formatting problem in the shared design log.

## 2. Committing the work (28 Oct)

**My request:** Fix the design-log formatting issue and commit my work as small
conventional commits, one per class or document. The team commits directly to `main`, so
follow that workflow.

**Claude:** Made 8 commits (for example `feat(entity): implement Waiter seat, takeOrder and
serve`, `docs: add key abstractions table and SOLID draft`) and pushed them to `main`.

## 3. Reviewing a teammate's part (29 Oct)

**My request:** Review Ayush's `ConsoleUI` against the plan.

**Claude:** Listed the issues it found. I asked it to fix them, then withdrew that request
because Ayush was already making the corrections himself. Claude discarded its local edits;
none had been pushed.

## 4. Full Stage 1 check before Stage 2 (29 Oct)

**My request:** Before we move on to Stage 2, do a thorough check of the whole
Stage 1 deliverable against the brief: code, test scripts and documentation. Then fix what's
wrong in small commits.

**Claude:** Compiled the project, ran the scripts from both Bash and PowerShell, and checked the
code against the brief. Main findings:
- Piping a script from Windows PowerShell adds an invisible byte-order mark, which shifted
  every scripted answer by one.
- The Cashier's status always showed "Ready".
- The defeat script didn't serve C5 on turn 18.
- There was no invalid-input script, no test notes, only a stub README, and no UML diagrams.
- Smaller issues: an unused getter, fields that should be `final`, and a public `Money`
  constructor.

It fixed all of this in 13 commits and re-ran everything: victory 5 / $66.00 / 79.2, defeat
4 / $51.00 / 58.0, and the invalid-input run matches the victory run.

---

## What I took from this chat

- Keep to my own files; when a teammate is fixing their part, leave it to them.
- Small conventional commits make it clear who did what and are easier to review.
- Test on the platform a TA might use: the PowerShell byte-order mark only showed up on Windows.
