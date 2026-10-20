# SC2002 Restaurant Rush — Stage 1 Design Report
**Group Members**: Raghav, Abhishek, Aaryan, Aekas, Ayush  
**Aekas's Role**: `Staff`, `Waiter`, `Cashier`, `Table`, `GameStats`, `InvalidActionException`  
**Report Contribution**: Key Abstractions Table & SOLID Principles (Stage 1)

---

## 1. Key Abstractions Table

| Abstraction | Type | Responsibility | Principle / Pattern Reflected |
|---|---|---|---|
| **`Staff`** | Entity | Base abstract class managing turn-based action accounting (`actedThisTurn`) and action availability (`checkCanAct`). | **ISP / Encapsulation**: Shared "one task per turn" quota logic only; subclasses expose specific actions without fat interfaces. |
| **`Waiter`** | Entity | Coordinates dining room operations: seating queued customers, taking orders, and serving ready dishes. Validates preconditions before calling `markActed()`. | **SRP / Fail-Safe Validation**: Precondition validation before state mutation ensures failed choices never consume the staff turn task. |
| **`Cashier`** | Entity | Processes payments for oldest `READY_TO_PAY` customers whose `readyToPayTurn < turn`. Releases tables and dispatches financial records. | **SRP / DIP**: Encapsulates settlement logic; interacts with `Customer` (abstract) and `GameStats`. |
| **`Table`** | Entity | Represents dining tables (exactly 2 in Stage 1). Tracks occupancy status, assigns customers, and releases upon checkout. | **Encapsulation / Low Coupling**: Guards table state; throws checked exception on double-seating attempts. |
| **`GameStats`** | Entity | Aggregates game metrics across turns: revenue (cents), served count, unhappy departures, turns used, and average satisfaction. | **SRP**: Changes only when scoring rules change. Returns `OptionalDouble` (empty = `N/A`) to decouple callers from null checks. |
| **`InvalidActionException`** | Exception | Custom checked exception carrying descriptive failure rationale when an invalid manager choice is attempted. | **Robustness / Retry Loop**: Caught by `GameController` to re-prompt the manager without penalty or game crash. |
| **`Customer`** *(abstract)* | Entity | Base entity tracking the customer state machine (`WAITING` → `SEATED` → `ORDERED` → `EATING` → `READY_TO_PAY` → `PAID` / `LEFT`), satisfaction decay, and billing. | **Encapsulation / OCP**: Satisfaction has no setter and is clamped [0..100]. Open for Stage 2 `VIPCustomer` and `CriticCustomer`. |
| **`RegularCustomer`** | Entity | Concrete customer losing 8 satisfaction points per waiting turn and paying full price. | **LSP**: Adheres strictly to the `Customer` state machine and satisfaction bounds. |
| **`Order`** | Entity | Tracks kitchen cooking progress (`unitsDone`/`prepUnits`) and lifecycle (`IN_PREPARATION` → `READY` → `SERVED` → `PAID` / `CANCELLED`). | **Encapsulation**: Automatically transitions to `READY` on completion of the final preparation unit inside `advanceOneUnit()`. |
| **`MenuItem`** | Entity | Immutable descriptor of a dish: name, price in integer cents, and required preparation units. | **Immutability / Encapsulation**: All fields private final; prevents runtime pricing corruption. |
| **`Menu`** | Entity | Registry of available restaurant dishes (Salad $9.00/1u, Burger $15.00/1u, Pasta $18.00/2u). | **High Cohesion**: Decouples menu catalog management from staff and order processing. |
| **`Chef`** | Entity | Advances cooking preparation by 1 unit per turn on orders in `IN_PREPARATION` status. | **SRP**: Back-of-house food preparation only; validates order state before acting. |
| **`Money`** | Utility | Pure static helper converting integer cents to formatted dollar strings (e.g., 1500 -> `$15.00`). | **Information Hiding**: Prevents IEEE 754 floating-point rounding errors across the domain. |
| **`Restaurant`** | Entity | State container holding tables, queue, staff, menu, and stats. Lookup methods only. | **Anti-God Class**: Pure state repository; avoids embedding procedural game rules. |
| **`GameConfig`** | Constants | Holds game baseline settings: 18 turns, 2 tables, and arrival turns {1, 4, 7, 10, 13, 16}. | **Separation of Concerns**: Centralizes tuning parameters. |
| **`ArrivalSchedule`** | Control | Generates scheduled customer arrivals across 18 turns. | **SRP / OCP**: Changes only when arrival timing changes. Registers VIP/Critic in Stage 2. |
| **`Objective`** | Control | Evaluates end-of-game victory targets (>= 4 paid, >= $45.00, avg satisfaction >= 60.0). | **SRP**: Decoupled from game loop; changes only if win/loss criteria change. |
| **`GameController`** | Control | Sequences the strict 6-step turn loop (arrivals, waiter, chef, eating/payment, waiting/abandonment, results). | **Low Coupling / High Cohesion**: Sequences steps; catches `InvalidActionException` to retry. |
| **`ConsoleUI`** | Boundary | CLI input/output; rejects invalid input strings and numbers without crashing. | **SRP**: Pure presentation layer; contains no business rules. |

---

## 2. Layered Architecture & Dependencies

The project follows a 3-layer architecture (`boundary`, `control`, `entity`), maintaining strict top-down dependency flow:

```
+-------------------------------------------------------------+
|                 BOUNDARY: ConsoleUI                         |
+-------------------------------------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|         CONTROL: Main, GameController, ArrivalSchedule,     |
|                  Objective, GameConfig                      |
+-------------------------------------------------------------+
                               |
                               v
+-------------------------------------------------------------+
|    ENTITY: Staff (Waiter, Chef, Cashier), Customer, Table,  |
|            Order, Menu, MenuItem, GameStats, Restaurant     |
+-------------------------------------------------------------+
```

| Layer | Depends On | Purpose & Responsibility |
|---|---|---|
| **Boundary** (`boundary`) | `entity`, `control` (for DTOs/interfaces) | Captures user inputs, sanitizes tokens, and prints domain feedback. Contains zero business rules. |
| **Control** (`control`) | `entity`, `boundary` | Coordinates turn progression, invokes domain operations in exact order, and checks victory targets. |
| **Entity** (`entity`) | None (self-contained domain) | Encapsulates all restaurant business logic, state machines, action quotas, and physical invariants. |

---

## 3. SOLID Design Principles (Stage 1 Analysis)

### 3.1 Single Responsibility Principle (SRP)
*Every class has only one reason to change, avoiding monolithic "God" classes:*
- **`Staff` subclasses**: `Waiter` changes only if front-of-house dining flow changes (seating, order taking, serving); `Chef` changes only if cooking rules change; `Cashier` changes only if billing priority changes.
- **`GameStats`**: Changes only if metrics calculation changes. It does not dictate game turns or customer states.
- **`Objective`**: Changes only if victory thresholds change (paid count, revenue, average satisfaction).
- **`ArrivalSchedule`**: Changes only if customer arrival scheduling or customer type registration changes.
- **`ConsoleUI`**: Changes only if the console formatting or user input prompts change.

### 3.2 Open/Closed Principle (OCP)
*Classes are open for extension but closed for modification:*
- **Customer Specialization**: `Customer` is abstract. Stage 2 adds `VIPCustomer` (10% discount, 5 loss rate) and `CriticCustomer` (12 loss rate, distinct ordering rule) as new subclasses. Because `Customer.calculateBill(basePriceCents)` and `Customer.getSatisfactionLossPerTurn()` are polymorphic, `GameController`, `Waiter`, and `Cashier` require **zero modifications** and no `instanceof` type-switching chains.
- **Staff Extensibility**: In Stage 3, `Host` will extend `Staff` to seat queued customers alongside `Waiter`, reusing `Staff` action tracking without altering `Staff`, `Waiter`, or `Chef`.
- **Menu Items**: `MenuItem` can be extended by `ComboMeal` in Stage 3 using composition (holding two `MenuItem` components) while still answering `getPriceCents()` and `getPrepUnits()`.

### 3.3 Liskov Substitution Principle (LSP)
*Subclasses must be substitutable for their base types without altering program correctness:*
- `RegularCustomer` adheres strictly to the lifecycle contract of `Customer`: it follows the identical state machine (`WAITING` → `SEATED` → `ORDERED` → `EATING` → `READY_TO_PAY` → `PAID`), never skips states, and clamps satisfaction to `[0..100]`.
- All `Staff` subclasses (`Waiter`, `Chef`, `Cashier`) honor the contract of `Staff`. At the start of each turn, `GameController` invokes `startNewTurn()` uniformly across all staff members without special-casing.

### 3.4 Interface Segregation Principle (ISP)
*Clients should not be forced to depend on methods they do not use:*
- Rather than defining a single bloated "god staff interface" with `seat()`, `prepare()`, and `processNextPayment()`, `Staff` defines only the shared "one task per turn" quota lifecycle (`startNewTurn`, `hasActedThisTurn`, `markActed`, `checkCanAct`).
- Each concrete staff member exposes only its cohesive operations (`Waiter.seat`, `Chef.prepare`, `Cashier.processNextPayment`). `Chef` is never forced to provide empty or throwing implementations of seating methods.

### 3.5 Dependency Inversion Principle (DIP)
*High-level modules should depend on abstractions, not concrete implementations:*
- `GameController` and `Cashier` operate exclusively on the abstract `Customer` class rather than concrete `RegularCustomer`. Only the factory method in `ArrivalSchedule` instantiates concrete customers.
- `GameStats.getAverageSatisfaction()` returns `java.util.OptionalDouble`. This inverts the dependency on magic values (such as `-1` or `0.0`), forcing calling presentation layers to explicitly handle the empty state (`"N/A"`).

---

## 4. Key Design Decisions & Trade-offs Considered

### Decision 1: Precondition Validation Before Action Consumption
- **Design Implemented**: `Waiter.seat(c, t)`, `Waiter.takeOrder(c, menu)`, and `Waiter.serve(o, turn)` perform all validation checks (queue membership, table occupancy, order readiness) *before* mutating state and *before* invoking `markActed()`.
- **Alternative Considered**: Marking the staff member as acted upon receiving user selection, then rolling back state on failure.
- **Trade-off & Rationale**: The rollback approach is error-prone and violates atomicity. By validating upfront and throwing checked `InvalidActionException`, actions succeed atomically or fail completely. A failed choice leaves `hasActedThisTurn()` as `false`, satisfying the requirement: *"An invalid choice must explain why it failed and let the manager choose again, without using up the staff task."*

### Decision 2: Staff Role Subclasses vs. Single Staff Class with Role Enum
- **Design Implemented**: Abstract `Staff` extended by `Waiter`, `Chef`, and `Cashier`.
- **Alternative Considered**: A single `Staff` class with `enum StaffRole { WAITER, CHEF, CASHIER }` and a generic `executeTask()` method.
- **Trade-off & Rationale**: The role enum approach requires extensive type switching and forces unrelated parameters across different tasks into a single signature. Subclassing provides compile-time type safety, isolates role-specific dependencies, and makes adding the `Host` in Stage 3 a purely additive change.

### Decision 3: Integer Cents vs. Floating-Point Double for Money
- **Design Implemented**: All monetary values (`MenuItem.priceCents`, `Order.billCents`, `GameStats.revenueCents`) are strictly stored as `int` cents and formatted via `Money.format()`.
- **Alternative Considered**: Storing money as `double`.
- **Trade-off & Rationale**: Floating-point arithmetic introduces binary rounding errors (e.g., `$0.10 + $0.20 = 0.30000000000000004`). In a commercial simulation with discounts and percentages (Stage 2 VIP 10% off, Stage 3 Happy Hour 20% off), integer cents prevent accumulator drift and ensure exact financial auditing.

### Decision 4: Two-Step Dining Cycle (`EATING` → `READY_TO_PAY`)
- **Design Implemented**: Customers served at `turn` transition to `EATING`. During turn updates, if `servedTurn < currentTurn`, they transition to `READY_TO_PAY` with `readyToPayTurn = currentTurn`. The `Cashier` collects payment only when `readyToPayTurn < currentTurn`.
- **Alternative Considered**: Charging customers immediately upon food delivery.
- **Trade-off & Rationale**: Immediate charging eliminates dining realism and table contention. The two-step cycle enforces the requirement that eating takes 1 full turn and guarantees tables remain occupied until the customer pays in a subsequent turn.

---

## 5. How Player Choices Change Game Results

The base setting constraints (2 tables, 18 turns, 1 task per staff member per turn) require careful trade-off management:
1. **Seating vs. Serving Priority**: Serving a ready dish immediately halts the customer's 8 points/turn satisfaction decay (satisfaction freezes once `EATING`). Delaying service in order to seat another customer allows the seated customer's dish to wait, leaking satisfaction.
2. **Impact of Idling (Defeat Demonstration)**: In `tests/stage1_defeat.txt`, having the Waiter wait on turns 1, 2, and 3 delays Customer 1's entire dining lifecycle. This initial 3-turn delay ripples through all subsequent table reassignments. Although revenue ($51.00) and paid count (4) reach targets, the average satisfaction drops to 56.0 (below the 60.0 threshold), flipping the game from **VICTORY** to **DEFEAT**.
