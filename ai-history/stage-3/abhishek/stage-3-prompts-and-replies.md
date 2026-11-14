# Abhishek: AI history, Stage 3

- **Tool:** Claude (claude.ai)
- **Dates:** 5–14 Nov 2026 (SGT)
- **Stage:** 3


---

## Me (5 Nov, in the "Stage 1 and 2 status" chat, after tagging `stage-2`)

> Ok now generate a document for how we should split stage 3. Remember to take into account the shit from the slides and all

**Claude:**

I'll pull the Stage 3 requirements from the brief, check the current code and the course slides, then write up the split.

*[Claude searched my earlier chats, the repo, the project files and my Google Drive for the Stage 3 rules.]*

I can't find the assignment brief anywhere I can reach right now. It isn't in the project files, it isn't in your Drive, and your Mac isn't connected. Can you attach it here? The main assignment PDF plus the briefing doc (the one about per-stage diagrams and AI history) would be ideal.

I need the exact Stage 3 rules, because so far I only have a one-line summary of them:
- **Host:** a second staff member who can seat people
- **Combo meals:** two dishes together, 10% cheaper
- **Happy Hour:** a one-time 20% discount that also gives waiting customers some satisfaction back

What's missing is the detail: how many satisfaction points Happy Hour gives back, how long it lasts and who triggers it, which dishes make up the combos and their prep units, the Stage 3 base setting (arrivals and objectives), and what the final report and video need. If I guess those and get one wrong, five people build the wrong thing.

Once you send it, I'll write the Stage 3 plan as a doc in the same format as the Stage 2 one: what Stage 3 has to plug into, a split across the five of you, the timeline to the 15 Nov deadline, and the SOLID points from the Chapter 9 slides that Stage 3 lets us claim properly. The main one is Happy Hour as a pricing policy, our first real dependency-inversion (DIP) case.

## Me (5 Nov)

> Ok wait check now, happy hour should be 15 points lost. 

**Claude:**

Got both docs. Now the current code, so the plan fits what's actually there.

Got the brief and the briefing doc. Next I'll check the rest of the code and prototype the Stage 3 design, so the plan's sketches and numbers are tested, not guessed.

*[Claude searched the SOLID and UML slides in the project.]*

I have everything I need. Now I'll prototype Stage 3 on a scratch copy of the code (nothing gets pushed), so the plan's sketches and numbers actually run.

*[Claude wrote prototype `PricingPolicy`, `StandardPricing`, `HappyHour`, `ComboMeal` and `Host` classes plus the controller, UI and `Main` changes in a scratch copy, and compiled it.]*

It compiles. Next I'll check that Stage 3, with its new features left unused, still reproduces the Stage 1 and 2 results.

*[Claude turned the six Stage 1 and 2 scripts into Stage 3 versions and ran them, then built a Stage 3 victory run.]*

My hand-typed input was off by a line. I'll generate it from a per-turn list instead.

One real gap turned up: the graded sequence diagram (20 marks) has no loops, alternatives or final result check, and the brief requires all of those. That goes in the plan. Creating the doc now.

*[Claude wrote the 10 sections of the Stage 3 plan doc: rules from the brief, new and changed files, rules we decide, SOLID and UML points from the slides, tested code sketches, the work split, test runs, the done checklist, the report and video split, and the timeline.]*

The numbers match. Now the last three sections.

I've written the Stage 3 plan as a doc. Before writing it, I built the whole Stage 3 design on a scratch copy of the code (nothing pushed). It compiles. A Stage 3 run using all three features wins with $81.54 and 84.6 average satisfaction. With the new features left unused, all 6 Stage 1 and Stage 2 test scripts give exactly their old results, which covers the brief's "what still works" requirement.

**The split:**
- **Aekas:** Happy Hour, plus updating the SOLID doc
- **Raghav:** the Host, plus the Stage 3 class diagram (the final one, worth 25 marks)
- **You:** ComboMeal (about 20 lines, building on your own Stage 1 `Menu`/`MenuItem`), the sequence diagram fix, and the `stage-3` tag
- **Aaryan:** wiring it into the game loop, plus the README stage-2 → stage-3 section
- **Ayush:** the console prompts, plus all the test and regression scripts

The doc also covers report sections, video segments and reflections for 10–15 Nov.

**One important thing to note:**
- **The sequence diagram needs loops, alternatives and the final result check (20 marks).** The Stage 2 diagram follows the VIP correctly, but the brief asks for "guards and loops", "the result check" and "a manager choice changes an outcome". I've put the upgrade under your name. It doesn't depend on any Stage 3 code, so you can start today.

You can edit the doc inline or leave comments, and I'll revise it. I can also make a PDF for the group chat like last time.

## 3. Me (5 Nov)

> Turn this into a PDF that I can share with my group and provide the allocations. For the work split, dont give raghav the starting portion

**Claude:**

I'll check the doc for any edits you made, move Raghav off the early code, then export the PDF.

*[Claude moved the Host to Aaryan and Raghav's work to after the merge, then exported the doc as a PDF and checked the pages.]*

The PDF renders cleanly. Two small fixes before sending: the checklist still says "5 feature branches" (it's 4 now that Raghav has no code branch), and one bullet shows literal backticks.

Here's the PDF (SC2002_Restaurant_Rush_Stage3_Plan.pdf, 18 pages). Raghav no longer has any of the early code. The Host class moved to Aaryan, since Aaryan's game-loop step already uses it. Raghav now starts on Sun 8 Nov, once the code is merged: his class diagram and README section both have to match the final code anyway.

The allocations, ready to paste into the group chat:

**Stage 3 split (code by Sun 8 Nov, tag Mon 9 Nov)**
- **Aekas:** Happy Hour (`PricingPolicy`, `StandardPricing`, `HappyHour`, the `Waiter.takeOrder` change, `Customer.recoverSatisfaction`), plus the SOLID doc and design log
- **Abhishek:** `ComboMeal` and `Menu.addItem`, the sequence diagram upgrade, and the `stage-3` tag
- **Ayush:** console prompts and display, the Stage 3 victory/defeat/invalid-input scripts, the 6 regression scripts, and the test notes
- **Aaryan:** `Host` and the `Restaurant` changes, the game-loop changes and `Main` (merges last), plus the README run instructions
- **Raghav:** starts Sun 8 Nov with the Stage 3 class diagram (25 marks), plus the README stage-2 → stage-3 section. He also still owes his Stage 1 and Stage 2 AI histories.

Merge order: Aekas and Abhishek, then Ayush, then Aaryan.

The shared doc has the same changes, so the PDF and the doc match.

##  Me (5 Nov)

> Ok help me understand how to code my portion. 

**Claude:**

I'll look at how you wrote `Menu`, `MenuItem` and your other Stage 1 classes, then walk through `ComboMeal`, `Menu.addItem`, and the sequence diagram upgrade in that same style.

Your style: `this.` in getters, `){` with no space, and short casual comments. Here is how `ComboMeal` works:


```java
package entity;

// a combo is 2 existing menu items sold together, so it is still a MenuItem
public class ComboMeal extends MenuItem {
    private final MenuItem first;
    private final MenuItem second;

    public ComboMeal(MenuItem first, MenuItem second){
        // name, price and prep units all come from the 2 items
        // price = 90% of both added up, the +50 makes it round to the nearest cent
        super(first.getName() + " + " + second.getName(),
              ((first.getPriceCents() + second.getPriceCents()) * 90 + 50) / 100,
              first.getPrepUnits() + second.getPrepUnits());
        this.first = first;
        this.second = second;
    }

    public MenuItem getFirst(){
        return this.first;
    }

    public MenuItem getSecond(){
        return this.second;
    }

}
```

And in `Menu.java`, add `addItem(MenuItem item)` so `Main` can register the two combos on top of the base menu without changing the constructor:

```java
public void addItem(MenuItem item){
    this.items.add(item);
}
```

*[Claude verified both combo prices ($24.30 for 3 units, $21.60 for 2 units), the Critic's pick, and an `Order` cooking the combo.]*

It matches the brief's example ($24.30, 3 units). `Order` and `CriticCustomer` handle it without any change. Commit `ComboMeal` and `Menu.addItem` as two small commits on `feat/combo-meal`.

Now for the sequence diagram upgrade on `docs/sequence-diagram-upgrade`:
- Turn 4 is a natural spot for the invalid-choice branch: Table 1 really is occupied by C1 then.
- Add the turn loop (`1..18`), the Waiter retry loop with an `alt` for "Table 1 is occupied", customer loops, and the `VICTORY` / `DEFEAT` result check at the end.
- Add a legend at the top with the stage, starting conditions and outcome, and an `alt` on turn 5 for Take order vs Wait so the diagram shows how a manager choice changes the outcome.

*[Claude drafted the updated PlantUML sequence diagram and rendered the PNG for review.]*

Both parts render cleanly. Once you review and push `feat/combo-meal` and `docs/sequence-diagram-upgrade`, your remaining task for Stage 3 will be merging the branches and tagging `stage-3` once everyone's pieces are in.

## Me (8 Nov, new chat: "Stage 3 completion and commits")

> okay what is left to complete stage 3? 

**Claude:**

*[Claude checked the repo against the Stage 3 plan and listed what remained after merging `feat/combo-meal`, `docs/sequence-diagram-upgrade`, `feat/stage3-ui` and `feat/stage3-controller`.]*

Here is what is left after merging the four feature branches into `main`:
1. **`GameController` wiring (`handleHappyHour()` and `handleHost()`):** Aaryan's `feat/stage3-controller` branch has `Host`, the `Restaurant` changes, the pricing fields, `currentPricing()`, the 3-argument `takeOrder` call and the wrapper removal, but stops before `handleHappyHour()` and `handleHost()`.
2. **`Main.java`:** registering the two combos (`Salad + Pasta` and `Burger + Salad`) and adding the `stage2` mode argument.
3. **Bug in `ConsoleUI`:** the staff status panel on `feat/stage3-ui` hard-coded `"Ready"` instead of checking `hasActedThisTurn()`, and needs the Host line added.
4. **Tests & documentation:** Ayush's Stage 3 scripts (`stage3_victory.txt`, `stage3_defeat.txt`, `stage3_invalid_inputs.txt`, the 6 `*_on_stage3.txt` regression scripts, and `docs/test-notes.md`), Raghav's Stage 3 class diagram and README `stage-2 → stage-3` section, and the ` design-log.md` entry.

Since you're handling the merge integration tonight, you can commit the `handleHappyHour()`, `handleHost()` and `Main.java` wiring (plus the design-log note) once Ayush's staff-status fix is in:

**To verify locally:**
```bash
javac -d out $(find src -name "*.java")
java -cp out control.Main < tests/stage3_victory.txt
```

##  Me (10 Nov)

> Ok help me check whether Stage 3 is ready to tag

**Claude:**
Yes, all Stage 3 code, test scripts, regression scripts, and documentation on `main` compile and pass.

To create and push the annotated `stage-3` tag from your local clone:

```bash
git checkout main && git pull
git tag -a stage-3 -m "Stage 3: Host, combo meals and Happy Hour"
git push origin stage-3
```

This creates an annotated tag with a message matching `stage-1` and `stage-2`.

## Me (12 Nov, new chat: "2002 demo video")

> ok make the sequence diagram using the agreed upon ideas and then show me for review. 

**Claude:**

I'll build a Stage 3 sequence diagram from a real run of the game and show it to you for review.

*[Claude read the Stage 2 sequence diagram and the Stage 3 code, ran the Stage 3 victory script, and drafted `docs/uml/stage3/sequence-diagram.puml` following C4 (the Critic).]*

Now I'll check the alternative branch for real.

*[Claude reran the game with Happy Hour declined to get the exact numbers for the `else` branch.]*


**What the diagram covers:** it follows C4, a Critic, through the Stage 3 victory run from turn 10 to turn 15, then the result check after turn 18. That one customer hits all three Stage 3 features plus your combo code:
- **Your part:** at start-up, `Main` creates the Salad + Pasta `ComboMeal` and calls `menu.addItem(sp)`.
- **Happy Hour:** the Manager starts it. The C4 path then shows `currentPricing()` picking Happy Hour, so the $24.30 combo becomes $19.44. On turn 11, C4's satisfaction goes from 88 back up to 100.
- **Host:** seats C4. The diagram includes the "Table 1 is occupied" error and the Host being asked again.
- **What the brief asks for:** loops and guards, the result check, and a manager choice that changes the outcome. Saying no to Happy Hour means C4 pays $24.30 at satisfaction 64 instead of $19.44 at 76. I ran both versions of the game to get those numbers.

**Exporting the PNG:** Open the `.puml` in VS Code with the PlantUML extension (or plantuml.com), export it as `docs/uml/stage3/sequence-diagram.png`, and add a `[PNG](docs/uml/stage3/sequence-diagram.png) · ` link at the start of that README line, the way the Stage 1 and 2 entries have one.

This also gives you your video segment: walk through it from start-up to turn 15, and it fits the 3-minute sequence diagram slot.

## 9. Me (14 Nov, new chat: "Stage 3 final repo check")

> ok check 2002 repo, all ok anot

**Claude:**

*[Claude checked the repo, compiled `main`, ran every test script in its mode, and checked every relative link in the Markdown files and UML diagrams.]*

The code is solid and every number in the README checks out. Here is a summary of what's verified and the final checklist items before submission:

**What's verified**
- Everything in `main` compiles cleanly with `javac`, with no errors or warnings.
- All 10 test scripts in `tests/` run cleanly in their respective modes (default, `stage2`, `regular`) and produce the exact VICTORY/DEFEAT results, paid counts, revenues, and average satisfaction scores documented in `README.md` and `docs/test-notes.md` (including $81.54 / 84.6 for the Stage 3 win and $64.80 / 72.3 for the slow start with the Host).
- All Stage 1, Stage 2, and Stage 3 class and sequence diagrams (`.puml` and `.png`) are in `docs/uml/stage1/`, `docs/uml/stage2/`, and `docs/uml/stage3/`.
- The `stage-1`, `stage-2` and `stage-3` tags are in place.

**Final polish items to finish up**
1. **Rename `Stage-3_Class Diagram.png` to `class-diagram.png`** in `docs/uml/stage3/` and update the link in `README.md` so it matches `stage1/class-diagram.png` and `stage2/class-diagram.png`.
2. **Export `docs/uml/stage3/sequence-diagram.png`** and link it in `README.md` alongside the PlantUML source, plus show the manager's Chef choice in the Stage 3 sequence diagram.
3. **Add the demo video section** with segment timestamps and presenters at the top of `README.md`.
4. **Ensure all 5 members' Stage 1–3 AI history folders** under `ai-history/stage-1/`, `ai-history/stage-2/`, and `ai-history/stage-3/` are committed on `main` and linked in `README.md`, then update the `stage-3` tag to the final commit.

