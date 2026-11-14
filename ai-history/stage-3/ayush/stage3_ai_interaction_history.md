# AI Interaction History: SC2002 Restaurant Simulation — Stage 3

- **Repository:** `sc2002`
- **Contributor:** Ayush
- **Stage:** 3
- **Tools Used:** Claude (claude.ai)
- **Overview:** Documents AI collaboration across Stage 3 role analysis, ConsoleUI updates for Happy Hour and Host prompts, test script and regression suite planning, documentation and report responsibilities, and opening the Stage 3 UI pull request.

---

## Chat 01: Stage 3 Plan Analysis & Responsibilities

- **Tool:** Claude (claude.ai)
- **Dates:** 9 November 2026

### 1. Breaking Down Assigned Stage 3 Deliverables (9 November 2026, 17:34)

**Prompt:**

> this is stage 3, explain my part in this

**Response:**

Based on the Stage 3 Plan document, here is the complete breakdown of your specific responsibilities, deliverables, branch assignments, and deadlines:

#### 1. Code Deliverable (Branch: `feat/stage3-ui`)

You own the boundary / presentation layer updates in `ConsoleUI.java`. Your job is strictly input and display handling without embedding game rules.

**`askStartHappyHour()`:** Prompts the player at the start of the turn:

```java
System.out.print("Start Happy Hour now? It can be used once and lasts 2 turns (1 = yes, 0 = no): ");
return readInt(0, 1) == 1;
```

**`showHappyHourStatus(int turnsLeft)`:** Displays active Happy Hour status:

```java
System.out.println("*** HAPPY HOUR ON: 20% off new orders, " + turnsLeft + " turn(s) left including this one ***");
```

**`askHostCustomerIdOrWait()`:** Prompts for the Host action:

```java
System.out.println("");
System.out.println("--- Host Action ---");
System.out.print("Customer to seat (0 = wait): C");
return readInt(0, 999);
```

**`showStartOfTurn()`:** Add the Host to the staff status list display (`Host : Ready/Acted`).

*(Merge order: You PR your branch after Aekas and Abhishek finish entity classes, but before Aaryan merges `GameController` and wires the game together.)*

#### 2. Test Scripts & Verification Deliverables

Once `main` is merged with the new features, you create and verify 9 test scripts.

**`tests/stage3_victory.txt`:**

- Implements the Section 7.1 prototype sequence.
- Uses Host, Happy Hour, and Combo Meals to achieve VICTORY ($81.54 revenue, 5 paid, 84.6 avg satisfaction).

**`tests/stage3_defeat.txt`:**

- Designed by you.
- Must demonstrate how specific player choices trigger a loss (e.g., wasting Happy Hour on Turn 1 with an empty queue, or delaying serving the Critic at Turn 13 so satisfaction crashes below 60).

**`tests/stage3_invalid_inputs.txt`:**

Must test at least:

- Host attempts to seat an already seated customer.
- Host attempts to seat someone at an occupied table.
- Host inputs an invalid/non-existent customer ID (e.g., C9).
- Malformed/junk inputs (`abc`, out-of-range `2`) at the Happy Hour prompt.
- Waiter attempts to seat a customer already seated by the Host in the same turn.

**6 Regression Scripts (`tests/*_on_stage3.txt`):**

Adapt the Stage 1 & Stage 2 scripts by prepending `0` (Happy Hour = No) and `0` (Host = Wait) to every turn. Test them against the Stage 3 build to prove backward compatibility:

| Script | Mode | Expected Result |
|--------|------|-----------------|
| `stage1_victory_on_stage3.txt` | regular | VICTORY, $66.00, 79.2 |
| `stage1_defeat_on_stage3.txt` | regular | DEFEAT, $51.00, 58.0 |
| `stage1_invalid_inputs_on_stage3.txt` | regular | VICTORY, $66.00, 79.2 |
| `stage2_victory_on_stage3.txt` | stage2 | VICTORY, $73.50, 75.6 |
| `stage2_defeat_on_stage3.txt` | stage2 | DEFEAT, $58.50, 53.8 |
| `stage2_invalid_inputs_on_stage3.txt` | stage2 | VICTORY, $73.50, 75.6 |

#### 3. Documentation Deliverables

**`docs/test-notes.md`:**

- Add the complete Stage 3 section with full turn-by-turn tables for `stage3_victory` and `stage3_defeat`.
- Document the rejected actions and recovery paths for `stage3_invalid_inputs`.

**Regression Evidence:**

- Provide the verified Section 7.4 regression table to Raghav for inclusion in the README.

**AI History:**

- Upload your Stage 1 and Stage 2 chat histories to an `ayush` branch under `ai-history/stage-1/` and `ai-history/stage-2/`.
- Track and upload this Stage 3 chat under `ai-history/stage-3/`.

#### 4. Report & Video Presentation (Post-Tag)

**Report Section (PDF):**

- Write the Functional Evidence section covering victory, defeat, invalid input runs, regression proofs, and analysis of how player choices directly determine outcomes.
- Write your individual reflection subsection (what you learned, application to design decisions, challenges encountered, and solutions).

**15-Minute Video Recording:**

- **Gameplay Segments 4 & 6:** Walk through VIP/Critic behavior and the Victory vs. Defeat outcomes.
- **Individual Contribution (~25s):** Summarize your personal learning and technical contributions.

#### Key Deadlines from Plan

| Date | Milestone |
|------|-----------|
| Sun 8 Nov | Feature code merged into `main`; Stage 3 test scripts and regression suite verified and passing. |
| Mon 9 Nov | Documentation updated; `stage-3` tag pushed. |
| Tue 10 – Thu 12 Nov | Draft report section, reflections, and record video clips. |
| Sun 15 Nov (11:59 PM) | Final submission. |

---

## Chat 02: Opening the Stage 3 UI Pull Request

- **Tool:** Claude (claude.ai)
- **Dates:** 9 November 2026

### 1. Creating the Pull Request for `feat/stage3-ui` (9 November 2026, 23:13)

**Prompt:**

> what to do in step 4

**Response:**

To open your Pull Request for Step 4:

1. Go to the GitHub repository in your browser: [https://github.com/Abhishek051105/sc2002](https://github.com/Abhishek051105/sc2002)
2. You will see a yellow banner at the top saying: *feat/stage3-ui had recent pushes ... [Compare & pull request]*
3. Click **Compare & pull request**.
4. Set the base branch to `main` and the compare branch to `feat/stage3-ui`.
5. Fill in the PR details:

   **Title:** `feat(ui): add Stage 3 Happy Hour and Host prompts`

   **Description:**

   ````markdown
   ### Summary of Changes
   - Added `askStartHappyHour()` to prompt for 2-turn Happy Hour activation at turn start.
   - Added `showHappyHourStatus(int turnsLeft)` to display remaining active turns.
   - Added `askHostCustomerIdOrWait()` for the Host seating action.
   - Updated start-of-turn staff display to include Host availability.

   ### Verification
   - Clean compilation with `javac -d bin -sourcepath src src/control/Main.java`.
   ````

6. Click **Create pull request**.
7. Once opened, notify the team (or Aaryan) on chat that `feat/stage3-ui` is ready for review and merge so Aaryan can wire it into `GameController`.
