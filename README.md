# SC2002 Restaurant Rush — Stage 1

**Team**: Raghav, Abhishek, Aaryan, Aekas, Ayush  
**Tag**: `stage-1`

---

## 1. Stage 1 Overview
Stage 1 implements basic restaurant operations with Regular customers and 3 menu items:
- **Salad**: $9.00 (900 cents), 1 prep unit
- **Burger**: $15.00 (1500 cents), 1 prep unit
- **Pasta**: $18.00 (1800 cents), 2 prep units
- **Physical Layout**: 2 Tables, Waiting Queue, Staff (`Waiter`, `Chef`, `Cashier`), `GameStats`.
- **Turn Sequence**: 18 turns, arrivals at turns 1, 4, 7, 10, 13, and 16.
- **Victory Condition**: After turn 18, at least 4 paid customers, at least $45.00 revenue, and average satisfaction of paid customers at least 60.0.

---

## 2. Package Architecture
The codebase follows the 3-layer architecture (`boundary`, `control`, `entity`):
- `src/entity/`: Domain models (`Staff`, `Waiter`, `Chef`, `Cashier`, `Table`, `Customer`, `RegularCustomer`, `Order`, `Menu`, `MenuItem`, `Money`, `GameStats`, `InvalidActionException`, `CustomerStatus`, `OrderStatus`, `WaiterTask`, `Restaurant`).
- `src/control/`: Workflow orchestration (`Main`, `GameController`, `ArrivalSchedule`, `Objective`, `GameConfig`, `AekasValidationTest`).
- `src/boundary/`: Input/output presentation (`ConsoleUI`).
- `tests/`: Automated test input scripts (`stage1_victory.txt`, `stage1_defeat.txt`).
- `docs/`: Design discussion and report drafts (`docs/stage1_report_draft.md`).

---

## 3. Build & Run Instructions

### A. Run Aekas's Role Unit & Validation Tests
Validates staff turn accounting, seating preconditions, order taking, ready-dish serving, cashier priority & tie-breaking, table lifecycle, and the Section 3.3 customer trace:
```bash
java -cp src src/control/AekasValidationTest.java
```

### B. Run Reproducible Victory Run
Reproduces the base setting victory run (5 paid customers, $66.00 revenue, 79.2 average satisfaction, 0 unhappy departures):
```bash
java -cp src src/control/Main.java < tests/stage1_victory.txt
```

### C. Run Reproducible Defeat Run
Demonstrates how player choices change results (Waiter idles on turns 1-3; results in 4 paid customers, $51.00 revenue, 56.0 average satisfaction, failing the 60.0 target):
```bash
java -cp src src/control/Main.java < tests/stage1_defeat.txt
```

### D. Interactive Play Mode
Play the full 18-turn game in text mode:
```bash
java -cp src src/control/Main.java
```

---

## 4. Documentation & Design Evidence
- The complete **Key Abstractions Table** and **SOLID Design Principles Analysis** for Stage 1 are documented in [`docs/stage1_report_draft.md`](docs/stage1_report_draft.md).
