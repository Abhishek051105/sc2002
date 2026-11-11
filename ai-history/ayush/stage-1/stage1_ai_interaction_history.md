# AI Interaction History: SC2002 Restaurant Simulation — Stage 1

- **Repository:** `sc2002`
- **Contributor:** Ayush
- **Stage:** 1
- **Tools Used:** Claude (claude.ai)
- **Overview:** Documents AI collaboration across Stage 1 role analysis, boundary layer (ConsoleUI) planning, skeleton-phase design rationale, automated test script preparation, and defeat scenario debugging.

---

## Chat 01: Stage 1 Role Analysis & Task Planning

- **Tool:** Claude (claude.ai)
- **Dates:** 28 October 2026

### 1. Reviewing Assigned Responsibilities (24 October 2026, 17:53)

**Prompt:**

> analyze this from the stage 1 document, and what is my task at hand now? How should I go about completing this task?

**Response:**

Based on Section 7 of the Stage 1 Plan, your role (Ayush) covers three main areas: Boundary Code (ConsoleUI), Testing & Documentation, and Repository & Video Deliverables.

#### 1. Code Deliverables

**ConsoleUI (Boundary Layer)**

You own the boundary class responsible for pure input and output. It must contain no game rules (it never decides whether an action is valid; it only presents state, captures input, and displays messages/errors returned by domain and control objects).

**Input Handling (`readInt(min, max)`):**

- Robust parsing: rejects non-numeric characters (letters, symbols), whitespace, and numbers outside `[min, max]` without crashing.
- Re-prompts the user until valid input is given.
- Clean exit handling when an input stream terminates (e.g., catching `NoSuchElementException` when running automated piped test scripts).

**Start-of-Turn Display (`showStartOfTurn`):**

- Prints turn number, current revenue formatted in dollars/cents, and average satisfaction of paid customers (displays `N/A` if no one has paid yet).
- Displays the waiting queue with customer satisfaction levels.
- Displays the 2 tables: occupancy, seated customer ID, satisfaction, and order status.
- Displays available staff tasks for the turn.

**Manager Decision Prompts:**

- Prompts for Waiter actions (`askWaiterTask`: `SEAT`, `TAKE_ORDER`, `SERVE`, `WAIT`).
- Parameter prompts: customer selection (`askCustomerId`), table assignment (`askTableNo`), and Chef target (`askOrderIdOrWait`, where `0` = Wait).

**Feedback & End-of-Turn/Game Displays:**

- `showError(String message)`: displays failure reasons when `InvalidActionException` is caught.
- `showEndOfTurn`: displays dishes served count, unhappy departures, current revenue, average satisfaction, and elapsed turns.
- `showFinalResult`: displays VICTORY or DEFEAT along with the specific breakdown of which of the three targets passed or failed.

#### 2. Testing & Repository Deliverables

**Repository Setup & Governance:**

- Set up GitHub repo with folder structure (`src/boundary`, `src/control`, `src/entity`, `docs/uml`, `tests`).
- Configure collaborator access and branch protection on `main` (requiring at least one PR review).

**Code Review:**

- Designated peer reviewer for Aaryan's pull requests (Control & Integration: `GameController`, `ArrivalSchedule`, `Objective`, `Restaurant`, `Main`).

**Automated Reproducible Test Scripts:**

- Create `tests/stage1_victory.txt` simulating the turn-by-turn inputs to reproduce the 5 paid, $66.00, 79.2 satisfaction victory run.
- Create `tests/stage1_defeat.txt` reproducing the defeat run (turns 1–3 wait delay, ending in 4 paid, $51.00, 58.0 satisfaction).
- Document a checklist of invalid-input test cases (e.g., seating at an occupied table, serving before READY, entering letters).

**README & Git Tagging:**

- Maintain the root `README.md` with compilation/build instructions and terminal execution commands (e.g., piping test files via `java -cp out Main < tests/...`).
- Verify that the project compiles and passes acceptance tests independently, then tag the commit as `stage-1`.

#### 3. Video Presentation Segments

For the group video submission, you are assigned to demonstrate:

- **Game launch:** launching the game cleanly from the terminal using the submitted project setup.
- **Replication runs:** executing the reproducible victory and defeat test scripts.
- **Tag & Repo verification:** presenting the Git tag comparison and clean `stage-1` state.

When you are ready to tackle one of these components (e.g., scaffolding ConsoleUI, preparing input validation, or drafting test scripts), let me know which area you want to begin with.

---

## Chat 02: Skeleton Phase Design Rationale

- **Tool:** Claude (claude.ai)
- **Dates:** 28 October 2026 

### 1. Purpose of UnsupportedOperationException (28 October 2026, 18:47)

**Prompt:**

> what is the significance of the unsupportedoperationception

**Response:**

In Java, `UnsupportedOperationException` is a standard runtime (unchecked) exception used to indicate that the requested method is not supported or has not been implemented.

In the context of your group project and software development, it holds two major significances:

#### 1. The "Step 0 Skeleton" Significance (Parallel Team Development)

In Section 7 of your group's Stage 1 plan under Step 0, Aaryan committed every class with exact method signatures and placed `throw new UnsupportedOperationException()` inside placeholder method bodies.

- **Enables Continuous Compilation:** Java requires non-void methods to return a value (e.g., an `int` or an object reference). Throwing `UnsupportedOperationException` satisfies the Java compiler without having to return arbitrary or misleading dummy data.
- **Decouples Teammates (No Blockers):** It allowed all five teammates to pull the repository and code against real signatures from day one without waiting for each other to finish their underlying implementations.
- **Explicit Failure over Silent Bugs:** If someone calls a teammate's method that hasn't been implemented yet, the program immediately halts with an explicit exception rather than failing silently with `null` or `0`.

#### 2. The Object-Oriented Design & Java API Significance (LSP & Immutability)

In standard Java and OOP design, `UnsupportedOperationException` is commonly used to protect immutable collections or denote optional interface methods:

- **Guarding Encapsulated State:** In your project, methods like `restaurant.getWaitingQueue()` return unmodifiable lists (`Collections.unmodifiableList(...)`) to prevent other classes from altering internal data directly. If external code attempts to modify that list (e.g., `queue.add(...)` or `queue.remove(...)`), the underlying Java wrapper immediately throws `UnsupportedOperationException`.
- **Liskov Substitution Principle (LSP) Indicator:** In OOP theory (covered in Chapter 9 of your course), throwing `UnsupportedOperationException` in a subclass because it cannot fulfill a parent class contract is often a design smell indicating an LSP violation. However, in your codebase, it was used strictly as a temporary implementation placeholder during the skeleton phase.

---

## Chat 03: Post-ConsoleUI Next Steps

- **Tool:** Claude (claude.ai)
- **Dates:** 28 October 2026

### 1. Planning the Automated Test Scripts (28 October 2026, 21:27)

**Prompt:**

> done with commiting consoleUI, whats our next task

**Response:**

Your next task is **Task B: Creating the automated test script files in `tests/`**.

Once your PR is merged, your repository needs two reproducible, piped input scripts so that you, your teammates, and the course TA can verify the exact Victory and Defeat scenarios specified in Section 8.

#### What to Build Next

**`tests/stage1_victory.txt`:**

- A line-by-line sequence of numeric inputs simulating turns 1 to 18.
- It must execute the step-by-step strategy from Section 8 and yield: 5 paid, $66.00 revenue, 79.2 average satisfaction, and VICTORY.

**`tests/stage1_defeat.txt`:**

- An input sequence where the Waiter waits on turns 1, 2, and 3, then executes normal service.
- It demonstrates delay cascade, yielding: 4 paid, $51.00 revenue, 58.0 average satisfaction, and DEFEAT.

#### How Input Numbers Map in Each Turn

Whenever a turn runs, `GameController` calls your `ConsoleUI` methods in order:

- **Waiter action:** `1` (Seat), `2` (Take Order), `3` (Serve), or `4` (Wait).
  - If `1` (Seat): next line is Customer ID (e.g., `1`), followed by Table Number (e.g., `1`).
  - If `2` (Take Order): next line is Customer ID (e.g., `1`).
  - If `3` (Serve): next line is Order ID (which equals Customer ID, e.g., `1`).
  - If `4` (Wait): no follow-up line needed.
- **Chef action:** Order ID to cook (e.g., `1`), or `0` for Wait.

We can now construct the exact line-by-line contents for `tests/stage1_victory.txt`. Let me know if you would like the full input file generated.

---

## Chat 04: Defeat Scenario Test Script Debugging

- **Tool:** Claude (claude.ai)
- **Dates:** 29 October 2026 

### 1. Correcting the Defeat Input Sequence (29 October 2026, 11:03)

**Prompt:**

> in the current stage1_defeat.txt, there is a problem in the sequence after step 12, when there is a customer waiting to be seated but the waiter just waits, can you check the problem against the required scenario mentioned in stage 1 doc to fix the error and find any others if you can

**Response:**

In the Stage 1 Plan document under Section 8 (Defeat Table), the actions for Turns 11 through 13 differ slightly from how the inputs were originally translated:

**Turn 11 vs 12 Chef cooking:**

- In Turn 10, Customer 3 is seated at Table 1.
- In Turn 11, Waiter takes C3's order (Burger, which requires 2 units of prep time). The Chef starts cooking C3 (`3`).
- In Turn 12, Burger still needs its 2nd unit of prep. The Chef cooks C3 (`3`) again to finish it.

**The Waiter issue:** In Turn 12, Table 2 has become free because C2 finished eating and left, and Customer 4 is waiting in the queue. In the plan document table, the Waiter seats Customer 4 at Table 2 (`1, 4, 2`) during Turn 12, rather than waiting.

**Turn 13 serving vs order taking:** In Turn 13, Burger (C3) is ready to serve. Waiter serves C3 (`3, 3`), while Chef cooks C4 (`4`) because C4's order was taken or ready.

**Turn 17–18 final actions:** The intentional delay causing defeat in the spec is solely the Turn 1, 2, and 3 Waiter waits (`4`). After Turn 3, the Waiter and Chef should act efficiently so that customers move through the pipeline and pay, but leave with lower satisfaction scores due to the initial delay.

#### Correct Turn-by-Turn Input Sequence (Turns 1 to 18)

| Turn | Waiter | Chef |
|------|--------|------|
| 1 | Wait (`4`) | Wait (`0`) |
| 2 | Wait (`4`) | Wait (`0`) |
| 3 | Wait (`4`) | Wait (`0`) |
| 4 | Seat C1 at T1 (`1, 1, 1`) | Wait (`0`) |
| 5 | Take C1 Order (`2, 1`) | Cook C1 (`1`) |
| 6 | Serve C1 (`3, 1`) | Wait (`0`) |
| 7 | Seat C2 at T2 (`1, 2, 2`) | Wait (`0`) |
| 8 | Take C2 Order (`2, 2`) | Cook C2 (`2`) |
| 9 | Serve C2 (`3, 2`) | Wait (`0`) |
| 10 | Seat C3 at T1 (`1, 3, 1`) | Wait (`0`) |
| 11 | Take C3 Order (`2, 3`) | Cook C3 (`3`) |
| 12 | Seat C4 at T2 (`1, 4, 2`) | Cook C3 (`3`) (Burger finishes cooking) |
| 13 | Serve C3 (`3, 3`) | Wait (`0`) |
| 14 | Take C4 Order (`2, 4`) | Cook C4 (`4`) |
| 15 | Serve C4 (`3, 4`) | Wait (`0`) |
| 16 | Seat C5 at T1 (`1, 5, 1`) | Wait (`0`) |
| 17 | Take C5 Order (`2, 5`) | Cook C5 (`5`) |
| 18 | Wait (`4`) | Wait (`0`) |

#### Correct `tests/stage1_defeat.txt` Content

Replace the contents of `tests/stage1_defeat.txt` with:

```plaintext
4
0
4
0
4
0
1
1
1
0
2
1
1
3
1
0
1
2
2
0
2
2
2
3
2
0
1
3
1
0
2
3
3
1
4
2
3
3
3
0
2
4
4
3
4
0
1
5
1
0
2
5
5
4
0
```
