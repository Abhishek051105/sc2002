# Abhishek: AI history, Stage 1

- **Tool:** Claude (claude.ai)
- **Dates:** 20–29 Oct 2026 (SGT)
- **Stage:** 1


| Chat | Dates (SGT) | Prompts in this file |
|---|---|---|
| SC2002 restaurant project stage 1 | 20–29 Oct | prompts 1–5 and 9 |
| Merging branch into main | 27–28 Oct | prompts 6–8 |

---

## Me (20 Oct)

> Hi, we want to start working on our SC2002 group project, which is the assignment on the restaurant. so let us following the rubric they want the thing done, now one thing to note is that they r very particular about design principles. so the thing here is that pls refer to the slides and break down the shit for us. this is a sample grp report of my friends from last year but this is on a different topic. so rmb, the rubric goes stage wise so we will also go stage wise. we are 5 ppl in a group; raghav, abhishek, aaryan, aekas and ayush. So what do we do for stage 1. also help for uml and all the shit there too

**Claude:**

I'll read the assignment brief, your friends' report, and the design-principles slides first, then put together the Stage 1 breakdown.

*[Claude read the brief, the sample report and the Chapter 6 and 9 slides, simulated the turn rules in a script, drew the Stage 1 class and sequence diagrams in PlantUML and wrote a Stage 1 plan doc.]*

Base rules check out: I simulated the turn sequence and both a winning and a losing run are reproducible. Now I'll draw the Stage 1 UML.

The diagrams look right. Next I'll put the full Stage 1 plan into a doc your group can share.

The Stage 1 plan is in the doc, along with draft class and sequence diagrams. The editable PlantUML files and PNGs are attached too.

**What Stage 1 needs:**
- **What to build:** a full playable game with Regular customers only, tagged `stage-1`. That means Waiter, Chef, Cashier, 2 tables, orders, the 6-step turn, and both endings.
- **Proof it works:** one victory run and one defeat run that anyone can repeat.
- **What to leave out:** nothing from Stage 2 or 3.

**The design decision that matters most:** set up Stage 1 so Stage 2 and 3 are mostly new files.
- Make `Customer` abstract, with the satisfaction loss rate and the bill as methods each customer type can override. Then VIP and Critic are just new subclasses, and the turn controller never checks customer types. That check is exactly what the brief bans.
- Make `MenuItem` a class that `ComboMeal` can extend later.
- Lock the bill in at the moment the order is taken. Happy Hour depends on that.

**The rest of the doc covers:**
- How each SOLID principle from the Chapter 9 slides shows up in this code, and where it doesn't.
- About 22 classes, sorted into entity, control and boundary.
- Seven open rules to agree on before coding.
- A split for the five of you. Each person owns some code plus one UML or report piece, so everyone has a part to explain in the video.
- Git habits, since the commit history is graded.

**Things I checked by running the rules in a quick simulation:**
- **Victory run:** 5 customers pay, $66.00 revenue, average satisfaction 79.2.
- **Defeat run:** the waiter waits on turns 1–3 and then plays the same way. Satisfaction drops to an average of 58, so it's a defeat.
- **The base game is lenient.** In 20,000 random games where the Waiter and Chef never chose to wait when they had a valid task, every game was a win. Only idling causes a loss. Put that in the report. In Stage 2, the Critic is what makes the order you serve people in really matter.

**Two things to watch:**
- **Diagram notes:** your friends' report left the explanatory notes off its class diagram. Your brief requires them, so don't copy that.
- **AI declaration:** since you're using me, say so in each person's GAI form.

**Next step:** have your kickoff meeting, agree on the method names in the class diagram, and start building. Once you've chosen the Critic's behaviour, I can do the same breakdown for Stage 2.

Files:
- stage1_class.puml
- stage1_sequence.puml
- stage1_class.png
- stage1_sequence.png

## Me (20 Oct)

> Ok for stage 1, split the work amongst the 5 of us

**Claude:**

*[Claude added a detailed task list for each person to Section 7 of the Stage 1 plan doc.]*

I've added a task list for each person to Section 7 of the doc. Here's the split:

| Who | Codes | Also owns |
| --- | --- | --- |
| **Raghav** | `Customer`, `RegularCustomer`, `CustomerStatus`: satisfaction, losing points while waiting, leaving | Class diagram |
| **Aaryan** | `Menu`, `MenuItem`, `Order`, `OrderStatus`, `Chef`, `Money` | Sequence diagram |
| **Aekas** | `Staff`, `Waiter`, `Cashier`, `Table`, `GameStats`, `InvalidActionException` | Key-abstractions table and SOLID write-up |
| **Abhishek** | `GameController`, `Restaurant`, `ArrivalSchedule`, `Objective`, `GameConfig`, `Main`, plus putting it all together | Design log and architecture section |
| **Ayush** | `ConsoleUI` (display and input checking) | Repo setup, test scripts, README, `stage-1` tag |

How it works:
- **Shared skeleton first.** Before anyone codes, Abhishek pushes every class with its exact method names and empty bodies. Then all 5 of you can work at the same time against the same method names, and nobody waits on anyone. It takes about an hour, using the class diagram.
- **Each task list says when it's done.** For example, Raghav's part is done when it reproduces the brief's 92 → 84 → 76 example. Abhishek's is done when the victory run ends at 5 paid, $66.00, average satisfaction 79.2.
- **Reviewers go in a loop:** Raghav reviews Aekas, Aekas reviews Aaryan, Aaryan reviews Raghav, Abhishek reviews Ayush, and Ayush reviews Abhishek. Every pull request gets one review before it goes into `main`.
- **Order id = customer id.** Each customer only ever has one order, so this keeps the input prompts and test scripts simple.
- **Everyone has a part to explain in the video.** That covers the brief's rule that each member presents.

The workload isn't perfectly even: Abhishek's integration part and Aekas's are the heaviest. If someone finishes early, they should help with testing invalid inputs.

What's the Stage 1 deadline? If you tell me, I can add dates to each task.

##  Me (27 Oct)

> I want u to review my code to ensure it follow my simplistic style. 
**Claude:**

Got it, I'll match your style: no static factory, no copying lists, no read-only views, just plain fields, a constructor and simple `if` checks. Rewriting and retesting now.

*[Claude rewrote `Menu`, `MenuItem`, `Order` and `Chef` in the simpler style, recompiled and reran the kitchen test.]*

I've rewritten all 5 files in your style, and every check still passes. The new versions are attached and replace the ones I sent before.

**What's simpler now**
- **`Menu`:** your version exactly. The constructor adds Salad, Burger and Pasta, plus `getItem()` and `size()`. I dropped `createDefault()`, the list copying and `getItems()`. Nothing else in the repo used `getItems()`.
- **`MenuItem`:** your version plus two simple `if` checks (price above 0, units at least 1). I left out `final` to keep it plain.
- **`Money`:** your version as-is.
- **`Order`:** plain fields, a constructor and getters. Each of the 4 status methods is just "if the status is wrong, throw; otherwise change it."
- **`Chef`:** check, check, cook, then `markActed()` last.

**One thing to tell Aaryan:** his `Restaurant` calls `Menu.createDefault()`, which no longer exists. He needs to change one line in `Restaurant.java`:
```java
menu = Menu.createDefault();   →   menu = new Menu();
```
Either he does it, or you do it in your pull request and say so in the description. I tested with that change.

**Then**
1. `cd sc2002`, then `git pull`, then `git checkout -b feat/kitchen`.
2. Paste the 5 files into `src/entity/` and `KitchenTest.java` into `tests/`.
3. Commit in small steps, push, and open a pull request.

`Chef` will only run once Aekas fills in `Staff`. Your code compiles and is ready for when he does.

Files:
- Money.java
- MenuItem.java
- Menu.java
- Order.java
- Chef.java
- KitchenTest.java

## Me (27 Oct, 20:55, new chat: "Merging branch into main")

> Hi I just pushed my Menu, MenuItem, Money, Order and Chef code, can you check if anything breaks when integrating with main?

**Claude:**

I'll compare your changes against the rest of the codebase on `main` and check if everything compiles together.

*[Claude checked the method calls across `src/entity/` and `src/control/` against the new kitchen classes.]*

Your logic in `Money`, `MenuItem`, `Menu`, `Order` and `Chef` is clean, but there are two build issues when compiling against `Restaurant.java` on `main`:

1. **`Menu.createDefault()` and `getItems()` are missing.** Your `Menu` constructor sets up the three default items directly, which is fine, but `Restaurant.java` (from Aaryan's skeleton) still calls `Menu.createDefault()`, and the skeleton contract includes `getItems()`.
2. **There's a stray `src/restaurant/Cashier.java`.** It's in the old `restaurant` package that everything was moved out of, references `CustomerState` instead of `CustomerStatus`, and duplicates `src/entity/Cashier.java`.

To fix both without changing your teammates' files:
- Add `public static Menu createDefault() { return new Menu(); }` and an unmodifiable `getItems()` method to `Menu.java`
- Remove the stray `src/restaurant/Cashier.java`

Want me to show the exact lines to add to `Menu.java`?

## Me (27 Oct, 20:58)

> yes show me

**Claude:**

Add `Collections` and `List` to your imports in `src/entity/Menu.java`, and add these two methods below `size()`:

```java
public static Menu createDefault() {
    return new Menu();
}

public List<MenuItem> getItems() {
    return Collections.unmodifiableList(items);
}
```

Then delete the stray file and commit the fix:

```bash
git rm src/restaurant/Cashier.java
git add src/entity/Menu.java
git commit -m "fix(entity): restore Menu.createDefault() and getItems(); remove the stray restaurant/Cashier"
git push origin main
```

With those two methods restored and the duplicate `Cashier.java` removed, the full project compiles cleanly.


## Me (29 Oct, back in the Stage 1 chat)

> Can you check whether everything for Stage 1 is ready to tag, and how we should organise the Stage 1 UML diagrams and README links so Stage 2 doesn't overwrite them?

**Claude:**

*[Claude checked the codebase, test scripts, and documentation against the Stage 1 brief.]*

Everything for Stage 1 is in place and passing:
- **Code & compilation:** All 23 classes in `src/boundary`, `src/control`, and `src/entity` compile cleanly with `javac`.
- **Reproducible test scripts:**
  - `tests/stage1_victory.txt` → VICTORY: 5 paid, $66.00 revenue, 79.2 average satisfaction.
  - `tests/stage1_defeat.txt` → DEFEAT: 4 paid, $51.00 revenue, 58.0 average satisfaction.
  - `tests/stage1_invalid_inputs.txt` → Matches the victory run (5 paid, $66.00, 79.2) after rejecting invalid inputs without consuming staff turns.
- **Design documentation:** `docs/key-abstractions-and-solid.md`, `docs/design-log.md`, and `docs/test-notes.md` are complete.

**Organising the Stage 1 UML diagrams and README before tagging:**

Move the Stage 1 PlantUML sources and rendered PNGs into `docs/uml/stage1/` (`class-diagram.puml`, `class-diagram.png`, `sequence-diagram.puml`, `sequence-diagram.png`). That way Stage 2 and Stage 3 can add `docs/uml/stage2/` and `docs/uml/stage3/` without overwriting the Stage 1 diagrams (since the brief requires one class diagram per stage matching that stage's code).

In `README.md`, add:
1. A **Stages** table linking to the `stage-1` tag and `docs/uml/stage1/class-diagram.png`.
2. A **Design documents** section linking to the Stage 1 class and sequence diagrams (PNG and PlantUML source), `docs/key-abstractions-and-solid.md`, `docs/design-log.md`, and `docs/test-notes.md`.
3. An **AI history** section so each member's Stage 1–3 AI chats are linked in one table.

Once you commit the diagram move and `README.md` update, tag `stage-1` and push:

```bash
git tag -a stage-1 -m "Stage 1: basic restaurant operation"
git push origin main stage-1
```
