# Chat 01: Stage 3 plan, work split and my ComboMeal

- **Tool:** Claude (claude.ai, Claude Code session synced with the SC2002 repository)
- **Date:** 5 Nov 2026 (SGT)
- **Stage:** 3
- **Note:** this chat started by closing off Stage 2 (checking what was left and tagging `stage-2`).
  Only the Stage 3 part is recorded here.

---

## 1. Planning Stage 3 and splitting the work

> Ok now generate a document for how we should split stage 3. Remember to take into account the shit from the slides and all

**Claude:**
- Asked for the assignment brief and the briefing doc, since it only had a one-line summary of the
  Stage 3 rules. Once my Mac was connected it read both from my Downloads folder, plus the current
  code and the Chapter 9 SOLID and UML class diagram slides in the project.
- Prototyped the whole Stage 3 design on a scratch copy of the code (nothing pushed) so the plan
  was tested, not guessed: `Host extends Staff`, `ComboMeal extends MenuItem`, a `PricingPolicy`
  interface with `StandardPricing` and `HappyHour`, `Customer.recoverSatisfaction()` and
  `Menu.addItem()`.
- Checked the prototype: it compiled, a Stage 3 run using all three features won ($81.54,
  average 84.6), and with the new features unused all six Stage 1 and Stage 2 scripts gave their
  old results.
- Wrote the Stage 3 plan as a doc: what Stage 3 plugs into, the rules we had to decide (turn
  order, combo choice, rounding, a `stage2` regression mode), which SOLID points the slides let us
  claim honestly (Happy Hour as our first real DIP case, `ComboMeal` as OCP and LSP), tested code
  sketches, who does what, test and regression evidence, and the timeline to 15 Nov.
- Flagged that the Stage 2 sequence diagram had no loops, guards or result check, which the
  brief asks for, and put the upgrade under my name.

## 2. PDF for the group, and moving Raghav off the early code

> Turn this into a PDF that I can share with my group and provide the allocations. For the work split, dont give raghav the starting portion

**Claude:**
- Moved the `Host` class to Aaryan (his game-loop step already uses it) and moved Raghav's parts
  (Stage 3 class diagram, README stage-2 → stage-3 section) to after the code merge, since both
  have to match the final code anyway.
- Exported the plan as an 18-page PDF, checked the pages rendered properly, fixed two small
  mistakes it found ("5 feature branches" → 4, and a stray code mark), and re-exported it.
- Gave me the allocations as a short list to paste into the group chat, with the merge order:
  Aekas and me, then Ayush, then Aaryan.

## 3. Doing my portion: ComboMeal and Menu.addItem

> Ok help me settle my portion. As said earlier, make the code simple, following my style and also when u commit, dont spam commit. Commit little by little

**Claude:**
- Read my Stage 1 `Menu` and `MenuItem` first to match my style (`this.` in getters, `){` with no
  space, short casual comments).
- Added `Menu.addItem(item)` so combos can go on top of the base menu without editing the
  constructor.
- Wrote `ComboMeal extends MenuItem`: the name, price (90% of both items, rounded to the nearest
  cent with `(cents * 90 + 50) / 100`) and prep units (both added) are worked out once in the
  constructor, and it keeps its two items.
- Compiled it and checked Salad + Pasta comes out at $24.30 and 3 units.
- Put the work on two branches (`feat/combo-meal`, `docs/sequence-diagram-upgrade`) with two small
  commits each, under my name with no AI lines, and gave me the PR links.

---

## Key takeaways

- **Plan against the brief, not memory:** reading the actual brief and briefing doc changed
  several details (Happy Hour recovery points, rounding, the sequence diagram requirements).
- **Prototype before splitting work:** testing the design first meant everyone got method
  signatures that already worked together, so nobody waited on anybody to start writing code.
- **Inheritance pays off in later stages:** because a `ComboMeal` is a `MenuItem`, `Order`, `Chef`
  and `CriticCustomer` needed no changes at all (LSP and OCP in practice).
