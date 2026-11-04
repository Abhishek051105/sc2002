# Key Abstractions and SOLID (Stage 2)

Draft for the report's "Design considerations" section. Every claim points at a
real class or method in the code. Updated for Stage 2 (VIP and Critic customers);
update again when Stage 3 lands.

## Key abstractions

| Abstraction | Type | Responsibility | Principle / Pattern |
|---|---|---|---|
| `ConsoleUI` | Boundary | Shows state, reads manager input, prints the reason text the domain gives back | SRP: no game rules, changes only if the display changes |
| `Main` | Control | Wires the objects together and calls `run()` | |
| `GameController` | Control | Runs turns 1-18 and calls the 6 steps in order; catches `InvalidActionException` and re-prompts | Low coupling: works with `Customer`, never a subclass (no `instanceof`) |
| `ArrivalSchedule` | Control | Decides who arrives on which turn and creates a customer of the right type | The only place a concrete customer class is named |
| `Objective` | Control | `isMet(stats)` checks the 3 win targets | SRP: changes only if the win rule changes |
| `GameConfig` | Constants | 18 turns, 2 tables, arrival turns and arrival types, targets | Which arrival is which type is configuration, not logic |
| `Restaurant` | Entity | Holds tables, queue, customers, orders, menu, staff, stats; lookups plus `customerLeft(c)` clean-up | Composition (owns tables, menu, staff, stats); hands out unmodifiable lists |
| `Customer` *(abstract)* | Entity | Own satisfaction and status, state transitions, food choice, bill, type label | Encapsulation (satisfaction has no setter); OCP hooks for VIP/Critic |
| `RegularCustomer` | Entity | Decay 8 per turn, full price, default food rule, label "Regular" | Inheritance for a real "is-a" |
| `VIPCustomer` | Entity | Decay 5 per turn, 10% off the bill, label "VIP" | Polymorphism: overrides `calculateBill()` and calls `super` first (LSP) |
| `CriticCustomer` | Entity | Decay 12 per turn, always orders the most expensive dish, label "Critic" | Polymorphism: overrides `chooseMenuItem()` |
| `Menu` / `MenuItem` | Entity | Fixed menu; item = name, price in cents, prep units | |
| `Order` | Entity | Locks the bill at creation, tracks units cooked, moves itself to READY | Encapsulation ("tell, don't ask": `advanceOneUnit()`) |
| `Staff` *(abstract)* | Entity | The shared "one task per turn" rule | Template for `Waiter`, `Chef`, `Cashier` |
| `Waiter` | Entity | `seat()`, `takeOrder()`, `serve()`: validates, then acts | Validate-then-act; throws `InvalidActionException` with a reason |
| `Chef` | Entity | `prepare(order)`: cooks one unit | |
| `Cashier` | Entity | `processNextPayment(restaurant, turn)`: oldest ready customer pays | Asks `Restaurant` for the order/table instead of searching lists itself (low coupling) |
| `Table` | Entity | Free/occupied, `assign()`, `release()` | |
| `GameStats` | Entity | Revenue (cents), satisfaction at payment, served count, departures, average | SRP: changes only if scoring changes |
| `InvalidActionException` | Exception | Carries the reason a manager choice was rejected | Custom checked exception |
| `CustomerStatus`, `OrderStatus`, `WaiterTask` | Enum | Allowed states / menu choices | |
| `CustomerType` | Enum | `REGULAR`, `VIP`, `CRITIC`: describes the arrival list only | Never used for behaviour; behaviour comes from the subclass |
| `Money` | Utility | `format(cents)` gives `$15.00` | |

## SOLID

**Single Responsibility.** Each class has one reason to change. `GameStats`
only records and averages scores; `Objective` only judges them against the
targets; `ConsoleUI` only displays. For example, `GameStats` does not decide
whether the player won, so changing the win rule never touches it.

**Open/Closed.** `Customer` is abstract, and the behaviour that differs by type
is in overridable methods: `getSatisfactionDecay()`, `chooseMenuItem(Menu)` and
`calculateBill(int)`. `GameController` and `Waiter` only call these through
`Customer`. Stage 2 proved it: `VIPCustomer` and `CriticCustomer` were added as
new files, and `GameController`, `Waiter`, `Chef`, `Cashier`, `Order`, `Menu`,
`MenuItem`, `Restaurant`, `Table`, `GameStats`, `Objective` and `Staff` were not
edited at all. Each type-specific decision reaches the right subclass through
dynamic dispatch:

| Call site (unchanged since Stage 1) | Method | Regular | VIP | Critic |
|---|---|---|---|---|
| `GameController` step 5 | `getSatisfactionDecay()` | 8 | 5 | 12 |
| `Waiter.takeOrder` | `chooseMenuItem(menu)` | `menu[(n-1) % 3]` | `menu[(n-1) % 3]` | most expensive (Pasta) |
| `Waiter.takeOrder` | `calculateBill(price)` | full price | 90% of price | full price |
| `ConsoleUI` | `getTypeLabel()` | "Regular" | "VIP" | "Critic" |

The only changes to existing classes were adding the abstract `getTypeLabel()`
to `Customer` (so the UI can name the type without `instanceof`), the arrival
list in `GameConfig`/`ArrivalSchedule`, the optional `regular` argument in
`Main`, and the extra label and bill in `ConsoleUI`. The full diff is
https://github.com/Abhishek051105/sc2002/compare/stage-1...stage-2.

**Where the concrete classes are named.** `ArrivalSchedule` has one small
`switch` on `CustomerType` that calls `new VIPCustomer(...)`,
`new CriticCustomer(...)` or `new RegularCustomer(...)`. This is a type check,
but it is not the kind the brief bans: some code always has to choose which
class to create (the OCP slides: "some code still creates/selects Triangle").
Keeping it in exactly one method means a Stage 3 customer type would touch
only that method and `GameConfig`, never the turn loop or the staff.

**Liskov Substitution.** The state machine (`seat()` -> `orderTaken()` ->
`serve()` -> `finishEating()` -> `markPaid()`) and the 0..100 clamp live in
`Customer`, and those methods are `final`, so subclasses cannot override them.
Any subclass can therefore be used wherever a `Customer` is expected without
the controller knowing. Subclasses may only change the decay, food choice,
bill and label, and they keep the parent's contract ("don't demand more, don't
promise less"):

- `VIPCustomer.calculateBill()` calls `super.calculateBill()` first, so it
  rejects a negative price exactly like the parent, and then returns a
  smaller, still non-negative amount.
- `CriticCustomer.chooseMenuItem()` rejects a null or empty menu with the
  same `IllegalArgumentException` as the parent, and still returns an item
  from the menu it was given; it only picks a different one.
- Every subclass decay is positive, so satisfaction still only goes down
  while waiting and the clamp at 0 still applies.

Because of this, the Stage 1 scripts give exactly their old results on the
Stage 2 build when run with `java -cp out control.Main regular`
(79.2 / 58.0 / 79.2).

**Interface Segregation** (modest claim). There is no single staff contract
with `seat()`, `prepare()` and `processNextPayment()`. `Staff` holds only the
shared turn rule (`startNewTurn()`, `hasActedThisTurn()`, `checkCanAct()`,
`markActed()`), and each role exposes only its own actions. This gets stronger
when `Host` arrives in Stage 3.

**Dependency Inversion** (limited). `GameController`, `Waiter`, `Cashier` and
`ConsoleUI` depend on the abstract `Customer`, never on `RegularCustomer`,
`VIPCustomer` or `CriticCustomer`; only `ArrivalSchedule` names a concrete
customer class. That is why two new types needed no changes to the
classes that use customers. We do not claim more:
the controller still uses the concrete `ConsoleUI` and staff classes, and we
have not added interfaces where there is only one implementation.

**Dependency Injection (not a SOLID principle, but used).** `Main` creates the
`Restaurant`, `ConsoleUI`, `ArrivalSchedule` and `Objective` and passes them into
the `GameController` constructor; the controller never calls `new` on them.
As the Chapter 9 slides point out, this is DI but not DIP, because the
injected types are concrete. A genuine DIP case is expected in Stage 3
(a pricing policy for Happy Hour).

## Encapsulation examples (for the class-diagram notes)

- `Staff.actedThisTurn` is private; subclasses can only set it through
  `markActed()`, which they call *after* their checks pass. A rejected choice
  therefore never uses up the staff member's task.
- `Table.occupant` changes only through `assign()` / `release()`, and
  `assign()` refuses an occupied table.
- `GameStats` exposes no setters; the paid count is the size of the recorded
  satisfactions list, so the count and the average can't drift apart.
- The VIP discount is applied once, inside `VIPCustomer.calculateBill()`, when
  the order is taken. `Order` then locks the bill in a `final` field, so the
  `Cashier` records the discounted amount without knowing a VIP exists.

## Alternative considered (Stage 2)

The Critic could have **reacted to service**, for example by leaving a bad
review that lowers every other customer's satisfaction when the Critic leaves
unhappy. We rejected it: it needs a new hook on `Customer` plus changes to
`GameController` to apply the effect, which breaks the "no turn-logic changes"
result above for little gameplay gain. Ordering the most expensive dish uses
the existing `chooseMenuItem()` hook, and it still makes the Critic matter:
Pasta takes 2 cooking units, and the Critic loses 12 satisfaction per turn
while waiting, so neglecting the Critic costs the most.
