# SC2002 Restaurant Rush — Stage 1

This repository contains the object-oriented implementation of **Restaurant Rush** for NTU course SC2002.

## Stage 1 Overview
Stage 1 implements basic restaurant operations including:
- **Core Staff Roles**: `Staff` (abstract turn management), `Waiter` (seating, ordering, serving), `Chef` (cooking preparation units), and `Cashier` (FIFO/priority payments and table clearance).
- **Domain Entities**: `Table`, `Customer` (lifecycle & satisfaction tracking), `Order`, `MenuItem`, and `Menu`.
- **Game Metrics & Rules**: `GameStats` (revenue, served counts, unhappy departures, and `OptionalDouble` average satisfaction), 18-turn game loop, and atomic action validation via `InvalidActionException`.
- **Reproducible Test Runs**: Verified reproducible victory (5 paid customers, 0 unhappy departures) and defeat runs.

---

## Build and Run Instructions

### 1. Run Complete Validation Test Suite
Tests all staff constraints, seating validations, timing rules, tie-breaking, and the Section 3.3 walkthrough:
```bash
java -cp src src/restaurant/StaffValidationTest.java
```

### 2. Run Interactive CLI Game
Play the full 18-turn game in text mode:
```bash
java -cp src src/restaurant/RestaurantRush.java
```

### 3. Run Reproducible Victory Run
Executes the reproducible victory scenario (5 customers paid, 0 departures, $69.00 revenue):
```bash
java -cp src src/restaurant/RestaurantRush.java --victory
```

### 4. Run Reproducible Defeat Run
Executes the reproducible defeat scenario (satisfaction abandonment trigger):
```bash
java -cp src src/restaurant/RestaurantRush.java --defeat
```

---

## Documentation and Design Evidence
- Detailed design discussion, key abstractions table, and SOLID principles analysis are available in [docs/stage1_report_draft.md](docs/stage1_report_draft.md).
