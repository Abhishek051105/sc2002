# AI Interaction History: SC2002 Restaurant Simulation — Stage 1

- **Repository:** `sc2002`
- **Contributor:** Abhishek
- **Stage:** 1
- **Tools Used:** Claude (claude.ai / Claude Code CLI session linked to repository workspace)
- **Overview:** Documents AI collaboration across Stage 1 architectural decomposition, modular work division, component encapsulation, pre-merge build verification, and Git history sanitization.

---

## Chat 01: Stage 1 Architecture, Domain Design & Work Breakdown

- **Tool:** Claude (Claude Project workspace initialized with project brief, rubric, and OOP lecture slides)
- **Dates:** 20 Oct 2026 – 29 Oct 2026 (SGT)
- **Original Chat Reference:** `328f5934-5f5e-474d-955f-404789a4b35b`
- **Scope:** Architectural decomposition, modular work allocation, component encapsulation, and milestone validation.

### 1. Architectural Scoping & Modular Allocation (20 Oct, 15:03)

> We are starting our SC2002 group project (Restaurant Simulation). Based on the course specification, grading rubric, and design principles covered in lectures, provide a structured breakdown for Stage 1. Organize classes using the Boundary-Control-Entity (BCE) pattern, align SOLID principles to specific components, and draft a 5-person modular work allocation across our team (Raghav, Abhishek, Aaryan, Aekas, and Ayush). Include draft PlantUML outlines for initial class and sequence diagrams.

**Claude:**
- Analyzed the project brief and reference architecture to formulate the Stage 1 roadmap:
  - Mapped core BCE responsibilities and explicitly linked SOLID principles to candidate domain classes.
  - Outlined turn-execution flow and boundary input handling.
  - Generated draft PlantUML structural and sequence representations.
  - Established a 5-person modular division of labor:
    - **Abhishek:** `Menu`, `MenuItem`, `Order`, `OrderStatus`, `Chef`, `Money` + Sequence Diagram.
    - **Raghav / Aekas:** Customer lifecycle, state transitions, and billing logic.
    - **Aaryan / Ayush:** `GameController`, simulation turn orchestration, and CLI boundary handling.
  - Calculated baseline simulation runs for victory and defeat test benchmarks.

### 2. Component Design & Encapsulation Review (27 Oct, 20:42)

> Review the implementation for my assigned module (`Menu`, `MenuItem`, `Order`, `OrderStatus`, `Chef`, `Money`). Ensure the design avoids over-engineering; prioritize readable OOP principles, clean getters/setters, robust encapsulation, and straightforward state checks without unnecessary enterprise boilerplate. Verify any interface dependencies with `Restaurant.java`.

**Claude:**
- Refactored assigned core domain classes to emphasize clean object-oriented fundamentals:
  - Enforced private field isolation and explicit public getter/accessor contracts.
  - Utilized direct state-checking methods over complex abstract wrappers or premature design patterns.
- Verified test suite execution against baseline scenarios.
- Identified an interface dependency in `Restaurant.java` for Aaryan to adjust method signatures to match the streamlined constructor.

### 3. Stage 1 Milestone Audit (29 Oct)

> Audit our repository status against the official Stage 1 submission checklist. Confirm whether all core deliverables, simulation test runs, and documentation artifacts are complete before tagging the release.

**Claude:**
- Validated completed Stage 1 requirements:
  - Game logic execution strictly meeting the stage specification.
  - Verified victory, defeat, and invalid-input execution benchmark runs.
  - Verified README documentation and Stage 1 class diagram integration.
- Outlined pending tasks: Pushing final commits, tagging the `stage-1` Git milestone, and compiling individual AI interaction logs.

### 4. Stage 2 Transition & Forward Planning (29 Oct)

> Generate the architectural transition document for Stage 2. Account for upcoming requirements (VIP and Critic customer extensions, modified arrival rates, and discounted billing) and provide a proposed work split adhering to the minimal-modification constraint.

**Claude:**
- Outlined upcoming Stage 2 simulation mechanics:
  - `CriticCustomer` selection logic (prioritizing highest-priced menu items).
  - Explicit turn arrivals: Turn 4 (`VIPCustomer`) and Turn 10 (`CriticCustomer`).
  - Discounted billing calculations: `price * 90 / 100` (integer-cent arithmetic).
- Established baseline expected results for Stage 2 victory and defeat scenarios.
- Assigned Stage 2 individual components: VIP sequence diagram construction and `stage-2` tag management.

---

## Chat 02: Branch Integration & Version Control Hygiene

- **Tool:** Claude Code (CLI session linked to local Mac development environment)
- **Dates:** 27 Oct 2026 – 28 Oct 2026 (SGT)
- **Original Chat Reference:** `8cc97713-af80-4ee6-9f0f-b9079dad5909`
- **Scope:** Branch compilation verification, merge staging, and Git commit metadata cleaning.

### 1. Pre-Merge Verification & Compilation Fix (27 Oct, 20:55)

> Check the feature branch `abhishek` against `main` before merging. Ensure all local implementations for the menu and order classes are intact, verify compilation across the entire project, and stage the integration safely.

**Claude:**
- Verified branch tracking: `main` had not diverged, allowing a clean fast-forward merge.
- Ran a pre-merge compilation audit and flagged two blocking integration issues:
  - A compilation error in `Restaurant.java` due to a call to `Menu.createDefault()`, which was removed in the refactor.
  - A stray legacy package file (`src/restaurant/Cashier.java`) committed unintentionally.
- Resolved both issues by restoring the required accessor methods and removing the redundant package artifact while preserving domain logic.
- Compiled the entire project clean and executed the local fast-forward merge on `main`.

### 2. Git Commit Hygiene & Metadata Sanitization (28 Oct, 09:54)

> Standardize the recent commit on my branch. Remove automated CLI co-author and AI bot trailer metadata from the commit message to maintain clean repository history, while ensuring the tree diff remains untouched.

**Claude:**
- Verified no new remote commits had been pushed to `main`.
- Amended the commit message to strip bot attribution and session metadata, preserving an author-clean audit log.
- Confirmed the commit SHA diff remained identical and provided the safe `git push --force-with-lease` command to update the remote branch.

---

## Stage 1 Technical Takeaways & OOP Learnings

- **Monetary Representation:** Storing currency as integer cents rather than floating-point primitives (`float` / `double`) avoids cumulative IEEE-754 precision errors during bill calculations.
- **Defensive Encapsulation:** Enforcing class invariants through private attributes and explicit state-guard methods ensures objects maintain valid runtime states without exposing mutable internals.
- **Pre-Merge Compilation Discipline:** Fast-forward merges should never occur blindly without a clean local compile check; removing legacy package artifacts and aligning constructor signatures early prevents broken integration states on `main`.
