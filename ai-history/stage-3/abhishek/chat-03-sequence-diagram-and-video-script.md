# Chat 03: Completion check, Stage 3 sequence diagram and the video script

- **Tool:** Claude (claude.ai, Claude Code session synced with the SC2002 repository)
- **Dates:** 12–14 Nov 2026 (SGT)
- **Stage:** 3

---

## 1. Where the project stood

> ok what about completion, what are we at right now

**Claude:**
- Built the `stage-3` tag fresh and ran every Stage 3 and regression script: all passed, and every
  feature branch was merged.
- Listed what was left: most members' Stage 3 AI history, the video, the report, reflections and
  declarations.
- Pointed out there was no Stage 3 sequence diagram (the upgraded one was in `docs/uml/stage2/`
  and followed a VIP).

## 2. The Stage 3 sequence diagram

> ok make the sequence diagram and push the shit for now

**Claude:**
- Read the Stage 3 code and traced a real run (`tests/stage3_victory.txt`) so every message in the
  diagram maps to an actual method call.
- Followed C4, the Critic, from turn 10 to turn 15 plus the result check after turn 18. That one
  customer goes through all three Stage 3 features and my combo code: `Main` creating the
  Salad + Pasta `ComboMeal` and calling `menu.addItem`, the Host seating C4 (including the "Table 1
  is occupied" error and the retry), `currentPricing()` handing the Waiter Happy Hour so $24.30
  becomes $19.44, and C4 recovering from 88 to 100 on turn 11.
- Added what the brief asks for: loops and guards (`loop`, `alt`, `opt`), the result check with an
  `alt` for VICTORY or DEFEAT, and a manager choice that changes the outcome. It ran the game
  without Happy Hour to get the real alternative numbers: C4 would pay $24.30 at 64 instead of
  $19.44 at 76.
- Checked the blocks balance, then pushed it on `docs/stage3-sequence-diagram` as two small commits
  under my name (the diagram, then the README link) and gave me the PR link.
- Couldn't render the PNG in that session, so told me to export it myself.

## 3. Video script for each person

> gimme a script and what slide to show for each person

> turn into a html page that they can all see, so its very easily demarcated

**Claude:**
- Pulled the numbers it needed from the repo (diff stats between tags, the defeat and invalid-input
  runs, the README sections) and ran the `stage-3` build so every figure in the script was real.
- Made an HTML page with a colour per person, a running-order bar for the 15 minutes, and for each
  person what to have open plus a timed "say / show on screen" table. My parts were segment 3
  (READY → SERVED → payment, following C4) and the 3-minute sequence diagram walkthrough.
- Added draft closing lines for everyone to rewrite, and a README timestamps block to paste in
  after editing.
- Reminded me the Stage 3 sequence diagram PNG still didn't exist and that we needed to pick an
  editor.

---

## Key takeaways

- **Sequence diagrams should trace a real run:** taking every message from an actual execution
  means each arrow maps 1:1 to a method in the code, and the numbers in the notes are correct.
- **Show the alternative, not just the happy path:** putting the Happy Hour "no" branch in an `alt`
  with its real outcome shows how a manager choice changes the result.
- **One customer can cover a whole stage:** following the Critic through turns 10 to 15 shows the
  Host, Happy Hour, the combo, polymorphic satisfaction decay and payment in one diagram.
