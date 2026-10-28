# Key Abstractions and SOLID (Stage 1 draft)

Draft for the report's "Design considerations" section. Every claim points at a
real class or method in the Stage 1 code. Update this when Stage 2/3 land.

## Key abstractions

| Abstraction | Type | Responsibility | Principle / Pattern |
|---|---|---|---|
| `ConsoleUI` | Boundary | Shows state, reads manager input, prints the reason text the domain gives back | SRP: no game rules, changes only if the display changes |
| `Main` | Control | Wires the objects together and calls `run()` | |
| `GameController` | Control | Runs turns 1-18 and calls the 6 steps in order; catches `InvalidActionException` and re-prompts | Low coupling: works with `Customer`, never a subclass (no `instanceof`) |
| `ArrivalSchedule` | Control | Decides who arrives on which turn and creates the customer | The only place a concrete customer is created |
| `Objective` | Control | `isMet(stats)` checks the 3 win targets | SRP: changes only if the win rule changes |
| `GameConfig` | Constants | 18 turns, 2 tables, arrival turns, targets | |
| `Restaurant` | Entity | Holds tables, queue, customers, orders, menu, staff, stats; lookups only | Composition (owns tables, menu, staff, stats); hands out unmodifiable lists |
| `Customer` *(abstract)* | Entity | Own satisfaction and status, state transitions, food choice, bill | Encapsulation (satisfaction has no setter); OCP hook for VIP/Critic |
| `RegularCustomer` | Entity | Satisfaction decay of 8 per turn | Inheritance for a real "is-a" |
| `Menu` / `MenuItem` | Entity | Fixed menu; item = name, price in cents, prep units | |
| `Order` | Entity | Locks the bill at creation, tracks units cooked, moves itself to READY | Encapsulation ("tell, don't ask": `advanceOneUnit()`) |
| `Staff` *(abstract)* | Entity | The shared "one task per turn" rule | Template for `Waiter`, `Chef`, `Cashier` |
| `Waiter` | Entity | `seat()`, `takeOrder()`, `serve()`: validates, then acts | Validate-then-act; throws `InvalidActionException` with a reason |
| `Chef` | Entity | `prepare(order)`: cooks one unit | |
| `Cashier` | Entity | `processNextPayment(...)`: oldest ready customer pays | |
| `Table` | Entity | Free/occupied, `assign()`, `release()` | |
| `GameStats` | Entity | Revenue (cents), satisfaction at payment, served count, departures, average | SRP: changes only if scoring changes |
| `InvalidActionException` | Exception | Carries the reason a manager choice was rejected | Custom checked exception |
| `CustomerStatus`, `OrderStatus`, `WaiterTask` | Enum | Allowed states / menu choices | |
| `Money` | Utility | `format(cents)` gives `$15.00` | |

## SOLID

**Single Responsibility.** Each class has one reason to change. `GameStats`
only records and averages scores; `Objective` only judges them against the
targets; `ConsoleUI` only displays. For example, `GameStats` does not decide
whether the player won, so changing the win rule never touches it.

**Open/Closed.** `Customer` is abstract, and the behaviour that differs by type
is in overridable methods: `getSatisfactionDecay()`, `chooseMenuItem(Menu)` and
`calculateBill(int)`. `GameController` and `Waiter` only call these through
`Customer`, so Stage 2's `VIPCustomer` and `CriticCustomer` can be added as new
files plus a registration in `ArrivalSchedule`, with no type checks in the turn
loop.

**Liskov Substitution.** The state machine (`seat()` -> `orderTaken()` ->
`serve()` -> `finishEating()` -> `markPaid()`) and the 0..100 clamp live in
`Customer`, and subclasses do not override them. Any subclass can therefore be
used wherever a `Customer` is expected without the controller knowing.

**Interface Segregation** (modest claim). There is no single staff contract
with `seat()`, `prepare()` and `processNextPayment()`. `Staff` holds only the
shared turn rule (`startNewTurn()`, `hasActedThisTurn()`, `checkCanAct()`,
`markActed()`), and each role exposes only its own actions. This gets stronger
when `Host` arrives in Stage 3.

**Dependency Inversion** (limited). `GameController`, `Waiter` and `Cashier`
depend on the abstract `Customer`, never `RegularCustomer`; only
`ArrivalSchedule` names a concrete customer class. We do not claim more:
the controller still uses the concrete `ConsoleUI` and staff classes, and we
have not added interfaces where there is only one implementation.

## Encapsulation examples (for the class-diagram notes)

- `Staff.actedThisTurn` is private; subclasses can only set it through
  `markActed()`, which they call *after* their checks pass. A rejected choice
  therefore never uses up the staff member's task.
- `Table.occupant` changes only through `assign()` / `release()`, and
  `assign()` refuses an occupied table.
- `GameStats` exposes no setters; the paid count is the size of the recorded
  satisfactions list, so the count and the average can't drift apart.
