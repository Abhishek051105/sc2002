# SC2002 Restaurant Rush — Stage 1 Design Report Draft

**Course**: SC2002 Object-Oriented Design & Programming  
**Component**: Stage 1 Core Staff & Table Architecture  
**Deliverable**: Key Abstractions Table & SOLID Principles Analysis  

---

## 1. Key Abstractions and Responsibilities

The system follows the **Boundary-Control-Entity (BCE)** architectural pattern to cleanly decouple user interaction, game flow coordination, domain entities, and error handling.

| Abstraction | Pattern Role | Core Responsibilities | Key Collaborators | Invariants & Encapsulation |
|---|---|---|---|---|
| **`Staff`** *(abstract)* | Entity | Base abstraction for all restaurant staff members. Tracks turn-based action accounting (`actedThisTurn`) and validates action quotas. | Subclasses (`Waiter`, `Chef`, `Cashier`, `Host`) | At most one completed task per staff member per turn. Action state cannot be bypassed. |
| **`Waiter`** | Entity | Handles front-of-house customer interactions: seating queued customers, taking dining orders, and serving prepared dishes. | `Customer`, `Table`, `Order`, `Menu`, `InvalidActionException` | Validates preconditions before calling `markActed()`. Failed choices never consume staff turn quota. |
| **`Cashier`** | Entity | Processes customer payments according to FIFO/priority timing (`readyToPayTurn < turn`), releases occupied tables, and dispatches financial records. | `Customer`, `GameStats`, `Table` | Cannot charge before `READY_TO_PAY`. Charges earliest eligible customer; ties broken by lower ID. |
| **`Chef`** | Entity | Handles back-of-house kitchen preparation by advancing order preparation units by 1 per turn based on manager priority. | `Order`, `Staff`, `InvalidActionException` | Prepares at most 1 unit per turn. Cannot cook orders that are already `READY` or `SERVED`. |
| **`Table`** | Entity | Manages dining table state, tracks customer occupancy, and guards against double-seating. | `Customer`, `InvalidActionException` | Table remains occupied continuously until the seated customer pays or leaves. |
| **`GameStats`** | Entity | Centralized aggregator for game outcomes: revenue, served count, unhappy departures, turns used, and average satisfaction. | `Cashier`, `GameController` | Revenue strictly excludes unpaid or abandoned orders. Average satisfaction returns `OptionalDouble.empty()` when 0 customers paid (displaying `N/A`). |
| **`Customer`** | Entity | Represents the diner lifecycle (`WAITING` → `SEATED` → `ORDERED` → `SERVED` → `READY_TO_PAY` → `PAID` / `LEFT_UNHAPPY`). Encapsulates satisfaction decay and eating timing. | `Table`, `Order`, `MenuItem`, `Menu` | Satisfaction drops by 8/turn while waiting/seated/ordered. Drops cease immediately once `SERVED`. Eating takes 1 turn. |
| **`Order`** | Entity | Tracks kitchen progress (`completedUnits` / `requiredUnits`) and status (`PENDING` → `COOKING` → `READY` → `SERVED`). | `Customer`, `MenuItem`, `OrderStatus` | Status automatically transitions to `READY` when `completedUnits >= requiredUnits`. |
| **`MenuItem`** | Entity | Immutable descriptor of a dish, encapsulating its name, base price, and required preparation units. | `Menu`, `Order` | Immutable attributes prevent runtime pricing corruption. |
| **`Menu`** | Entity | Registry of available restaurant menu items (Burger, Pasta, Salad). | `MenuItem` | Returns unmodifiable collection of items. |
| **`GameController`** | Control | Orchestrates turn sequence, schedules arrivals across 18 turns, handles manager command dispatch, and evaluates win/defeat conditions. | `Waiter`, `Chef`, `Cashier`, `Table`, `GameStats` | Enforces 18-turn game boundary, 2-unhappy-departure defeat threshold, and max 5 payable customers constraint. |
| **`RestaurantRush`** | Boundary | CLI user interface presenting turn statuses, menus, and choices. Catches `InvalidActionException` to allow retry. | `GameController`, `Scanner` | Captures manager input and provides interactive as well as reproducible script execution (`--victory`, `--defeat`). |
| **`InvalidActionException`** | Exception | Domain exception thrown upon rule violations (e.g., seating at occupied table, taking order when not seated, serving unready food). | All staff and entity classes | Preserves failure rationale; enables clean retry loops without state corruption. |

---

## 2. SOLID Design Principles Analysis

The implementation strictly demonstrates and adheres to SOLID principles in areas genuinely reflected in the codebase:

### 2.1 Single Responsibility Principle (SRP)
Each class in the design has a single, well-defined reason to change:
- **`Waiter`**: Focuses exclusively on dining room service operations (`seat`, `takeOrder`, `serve`). It does not cook food (delegated to `Chef`), does not manage financial bookkeeping (delegated to `Cashier` and `GameStats`), and does not track table occupancy internally (delegated to `Table`).
- **`Cashier`**: Focuses solely on financial settlement and table clearance for customers who have completed eating. It does not decide manager priority or customer dining flow.
- **`Table`**: Has the sole responsibility of tracking whether a physical seat is free or occupied and maintaining reference to the currently seated customer.
- **`GameStats`**: Serves as a pure metrics aggregator. It does not dictate game flow or customer lifecycle, only accumulating metrics when notified by actors.
- **`Staff`**: Abstracts turn-action accounting (`startNewTurn`, `hasActedThisTurn`, `markActed`, `checkCanAct`). Subclasses do not duplicate turn flag management.

### 2.2 Open/Closed Principle (OCP)
The system is open for extension but closed for modification:
- **Extending Staff Roles**: In Stage 3, when the `Host` role is introduced to seat queued customers, it extends `Staff` seamlessly without modifying `Staff`, `Waiter`, or `Chef`.
- **Customer Specialization & Dynamic Pricing**: `Customer.calculateBill(MenuItem item)` returns `item.getPrice()` by default for `RegularCustomer`. In Stage 2, `VIPCustomer` overrides `calculateBill(item)` to apply a 10% discount, and `CriticCustomer` overrides order preferences or reaction rules. Neither `Waiter.takeOrder`, `Cashier.processNextPayment`, nor the turn loop require modifications or `instanceof` checks.
- **Menu Item Extensibility**: `MenuItem` allows Stage 3 `ComboMeal` composition without altering the `Order` or `Chef` preparation mechanics.

### 2.3 Liskov Substitution Principle (LSP)
Subclasses can substitute their parent classes without violating system contracts or behavioral expectations:
- `Waiter`, `Chef`, and `Cashier` all inherit from `Staff`. Any collection or loop operating on `Staff` (such as `startNewTurn()` at the start of each round in `GameController`) can invoke methods polymorphically without needing downcasting or special casing.
- The contract of `Customer` guarantees that any derived customer type in Stage 2 (`VIPCustomer`, `CriticCustomer`) adheres to the state lifecycle (`WAITING` → `SEATED` → `ORDERED` → `SERVED` → `READY_TO_PAY` → `PAID`). Substitution never breaks table assignment, order placement, or cashier collection.

### 2.4 Interface Segregation Principle (ISP)
Rather than forcing broad interfaces with unused methods, classes expose cohesive, client-specific APIs:
- `Table` exposes only what is needed for seating management: `isFree()`, `assign(c)`, `release()`, and `getTableNo()`. Clients do not see culinary or financial operations on a table.
- `Staff` exposes only action quota checks and turn resets. Waiter-specific actions (`seat`, `takeOrder`, `serve`) are isolated strictly to `Waiter`, preventing `Chef` from being forced to implement seating methods or throw `UnsupportedOperationException`.

### 2.5 Dependency Inversion Principle (DIP)
- High-level orchestrators (`GameController`) depend on abstract role abstractions and cohesive domain models rather than low-level tightly coupled procedural scripts.
- `GameStats.getAverageSatisfaction()` returns `java.util.OptionalDouble`. This inverts the dependency on null checks or magic numbers (e.g. returning `-1` or `0.0`), allowing boundary classes (`RestaurantRush`) to handle the presence or absence of payments safely and declaratively.

---

## 3. Trade-offs and Alternative Designs Considered

### Alternative 1: Monolithic Staff Class vs. Role-Based Inheritance Hierarchy
- **Alternative Considered**: Having a single concrete `Staff` class with an `enum Role { WAITER, CHEF, CASHIER }` containing a large switch statement in an `executeAction()` method.
- **Trade-off & Rationale**: The monolithic approach was rejected because it violates SRP and OCP. Adding a new role (like the `Host` in Stage 3) would require modifying existing methods and risk regressions. The subclass hierarchy (`Staff` → `Waiter`, `Chef`, `Cashier`) encapsulates role-specific behavior, validates preconditions cleanly, and ensures strong compile-time type safety.

### Alternative 2: Boolean Return Codes vs. Custom Checked Exception (`InvalidActionException`)
- **Alternative Considered**: Having methods return `boolean` (e.g., `boolean seat(Customer c, Table t)`) to indicate success or failure.
- **Trade-off & Rationale**: Return codes fail to convey *why* an action failed (e.g., distinguishing between "Customer is not in queue" vs. "Table is occupied" vs. "Waiter already acted"). By throwing `InvalidActionException` before state modification and before `markActed()`, we guarantee:
  1. Atomic state transitions: either the action succeeds completely or no state changes.
  2. The staff action quota is strictly preserved on failure, fulfilling the core rule: *"An invalid choice must explain why it failed and let the manager choose again, without using up the staff task."*

### Alternative 3: Immediate Cashier Billing on Serving vs. 2-Step Eating Lifecycle (`READY_TO_PAY`)
- **Alternative Considered**: Billing customers immediately upon `serve()`.
- **Trade-off & Rationale**: Immediate billing violates dining realism and the project rule: *"A customer cannot eat before being served. The Cashier cannot charge before READY_TO_PAY."* Implementing the explicit `READY_TO_PAY` transition with timing condition `readyToPayTurn < turn` ensures customers spend exactly 1 turn eating, preventing tables from being prematurely cleared and creating realistic restaurant resource contention.

---

## 4. How Player Choices Impact Game Outcomes

The player acts as the restaurant manager directing staff attention under strict resource constraints:
1. **Dining Room Priority (Waiter)**: Because the Waiter can perform at most **one** task per turn, the player must balance:
   - Seating a customer to prevent queue abandonment (saving 8 satisfaction loss/turn in queue, though seated customers still decay until served).
   - Taking orders to keep the Chef's pipeline active.
   - Serving completed dishes immediately to stop customer satisfaction decay (once `SERVED`, decay drops to 0).
2. **Kitchen Priority (Chef)**: The player selects which dish the Chef works on. Prioritizing faster dishes (Salad: 1 unit) can quickly satisfy waiting customers, whereas prioritizing heavier dishes (Pasta: 2 units) clears high-revenue orders.
3. **Action Preservation Guarantee**: If a manager makes an invalid decision (e.g. attempting to seat a customer at an occupied table), the game throws `InvalidActionException` with an explanatory message (`"Table 1 is occupied"`), leaving the Waiter's action unspent (`hasActedThisTurn() == false`) and prompting the player to re-select without penalty.
