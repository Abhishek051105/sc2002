# Key Abstractions and SOLID (Stage 3)

Draft for the report's "Design considerations" section. Every claim points at a
real class or method in the code. Updated for Stage 3 (Host, ComboMeal and
Happy Hour); the Stage 2 sections below still hold.

## Key abstractions

| Abstraction | Type | Responsibility | Principle / Pattern |
|---|---|---|---|
| `ConsoleUI` | Boundary | Shows state, reads manager input, prints the reason text the domain gives back | SRP: no game rules, changes only if the display changes |
| `Main` | Control | Wires the objects together and calls `run()` | |
| `GameController` | Control | Runs turns 1-18 and calls the steps in order (Stage 3 adds the Happy Hour and Host steps); catches `InvalidActionException` and re-prompts; picks the pricing policy for the turn | Low coupling: works with `Customer`, never a subclass (no `instanceof`) |
| `ArrivalSchedule` | Control | Decides who arrives on which turn and creates a customer of the right type | The only place a concrete customer class is named |
| `Objective` | Control | `isMet(stats)` checks the 3 win targets | SRP: changes only if the win rule changes |
| `GameConfig` | Constants | 18 turns, 2 tables, arrival turns and arrival types, targets | Which arrival is which type is configuration, not logic |
| `Restaurant` | Entity | Holds tables, queue, customers, orders, menu, staff (now including the `Host`), stats; lookups plus `customerLeft(c)` clean-up | Composition (owns tables, menu, staff, stats); hands out unmodifiable lists |
| `Customer` *(abstract)* | Entity | Own satisfaction and status, state transitions, food choice, bill, type label; satisfaction goes down while waiting and up during Happy Hour | Encapsulation (no setter, only `decreaseSatisfaction()` / `recoverSatisfaction()`); OCP hooks for VIP/Critic |
| `RegularCustomer` | Entity | Decay 8 per turn, full price, default food rule, label "Regular" | Inheritance for a real "is-a" |
| `VIPCustomer` | Entity | Decay 5 per turn, 10% off the bill, label "VIP" | Polymorphism: overrides `calculateBill()` and calls `super` first (LSP) |
| `CriticCustomer` | Entity | Decay 12 per turn, always orders the most expensive dish, label "Critic" | Polymorphism: overrides `chooseMenuItem()` |
| `Menu` / `MenuItem` | Entity | Base menu plus `addItem()` for combos; item = name, price in cents, prep units | |
| `ComboMeal` | Entity | A `MenuItem` made of two existing items: units = sum, price = 90% of the total, rounded to the nearest cent | Inheritance (is-a `MenuItem`, so LSP) plus aggregation of its two parts |
| `Order` | Entity | Locks the bill at creation, tracks units cooked, moves itself to READY | Encapsulation ("tell, don't ask": `advanceOneUnit()`) |
| `Staff` *(abstract)* | Entity | The shared "one task per turn" rule | Template for `Waiter`, `Chef`, `Cashier`, `Host` |
| `Waiter` | Entity | `seat()`, `takeOrder(c, menu, pricing)`, `serve()`: validates, then acts | Validate-then-act; depends on `PricingPolicy`, not on Happy Hour (DIP) |
| `Host` | Entity | `seat()`: seats one queued customer per turn, on top of the Waiter's task | Reuses the `Staff` turn rule; only seating staff have `seat()` (ISP) |
| `Chef` | Entity | `prepare(order)`: cooks one unit | |
| `Cashier` | Entity | `processNextPayment(restaurant, turn)`: oldest ready customer pays | Asks `Restaurant` for the order/table instead of searching lists itself (low coupling) |
| `Table` | Entity | Free/occupied, `assign()`, `release()` | |
| `GameStats` | Entity | Revenue (cents), satisfaction at payment, served count, departures, average | SRP: changes only if scoring changes |
| `PricingPolicy` *(interface)* | Entity | `applyTo(billCents)`: the price rule used when an order is taken | DIP: the abstraction the Waiter depends on |
| `StandardPricing` | Entity | Returns the bill unchanged | One of two real implementations |
| `HappyHour` | Entity | 20% off, rounded to the nearest cent; tracks "used once" and the turns left | Polymorphism through `PricingPolicy`; encapsulation (state changes only through `activate()` / `endTurn()`) |
| `InvalidActionException` | Exception | Carries the reason a manager choice was rejected | Custom checked exception |
| `CustomerStatus`, `OrderStatus`, `WaiterTask` | Enum | Allowed states / menu choices | |
| `CustomerType` | Enum | `REGULAR`, `VIP`, `CRITIC`: describes the arrival list only | Never used for behaviour; behaviour comes from the subclass |
| `Money` | Utility | `format(cents)` gives `$15.00` | |

## SOLID

**Single Responsibility.** Each class has one reason to change. `GameStats`
only records and averages scores; `Objective` only judges them against the
targets; `ConsoleUI` only displays. For example, `GameStats` does not decide
whether the player won, so changing the win rule never touches it.

In Stage 3, `HappyHour` owns only Happy Hour's state and its price rule, and
the +15 recovery lives in `Customer.recoverSatisfaction()`, next to the decay
rule it mirrors, because `Customer` owns the satisfaction value (information
expert). The Happy Hour and Host prompts live in `ConsoleUI`. The honest limit:
`GameController` grows, because it now runs two more steps; each step is kept
in its own small method.

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

**Stage 3: where OCP held and where it didn't.** `ComboMeal` is our
"Triangle" (OCP slide 26): a new kind of menu item, added with no changes to
`Order`, `Chef` or `CriticCustomer`. `Order.advanceOneUnit()` already asks
`item.getPrepUnits()`, so a 3-unit combo cooks in 3 turns, and the Critic
already compares `getPriceCents()`, so he now picks Salad + Pasta by himself
because it is the most expensive item. Host and Happy Hour are different: they
are new *turn rules*, not new types, so they did need new steps in
`GameController`, and `Waiter.takeOrder` gained a pricing parameter. OCP
doesn't promise that new rules come for free; these are deliberate, explained
changes (brief 4.4), listed in the README's stage-2 -> stage-3 section.

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

Stage 3 keeps the same rule for the new classes:

- A `ComboMeal` goes everywhere a `MenuItem` does (`Order`, the Critic's price
  comparison, the UI) and keeps its contract: a name, a non-negative price
  and a positive number of prep units.
- `Host` inherits `checkCanAct()` and `markActed()` from `Staff`, so it keeps
  the one-task-per-turn contract, and a rejected choice doesn't use its task.
- `recoverSatisfaction()` is `final` like the other flow methods, so no
  customer type can change who recovers or lift satisfaction above 100.
- `StandardPricing` and `HappyHour` both return a non-negative bill no larger
  than the one they were given, so the Waiter can use either one.

**Interface Segregation** (modest claim). There is no single staff contract
with `seat()`, `prepare()` and `processNextPayment()`. `Staff` holds only the
shared turn rule (`startNewTurn()`, `hasActedThisTurn()`, `checkCanAct()`,
`markActed()`), and each role exposes only its own actions. In Stage 3,
`seat()` lives on `Host` and `Waiter` only, so `Chef` and `Cashier` never see
it, and `PricingPolicy` has a single method. The cost: `Host` repeats the
Waiter's two seating checks (see the trade-offs below).

**Dependency Inversion.** `GameController`, `Waiter`, `Cashier` and
`ConsoleUI` depend on the abstract `Customer`, never on `RegularCustomer`,
`VIPCustomer` or `CriticCustomer`; only `ArrivalSchedule` names a concrete
customer class. That is why two new types needed no changes to the
classes that use customers.

Stage 3 adds our first genuine DIP, with the same shape as the slides'
`OrderService -> Notifier <- EmailNotifier` example (slides 41, 45):

```
Waiter.takeOrder  ---->  PricingPolicy  <|----  StandardPricing
 (high-level rule)        (interface)    <|----  HappyHour
```

`Waiter.takeOrder(c, menu, pricing)` locks the bill with
`pricing.applyTo(c.calculateBill(price))`. It never names `HappyHour`, so the
Waiter doesn't know which discount is running, and the "VIP first, then Happy
Hour" order ($15.00 x 0.9 x 0.8 = $10.80) comes from this one line. This is not
a checklist interface: both implementations are used in any game where Happy
Hour is switched on. What we don't claim: `GameController` still chooses which
policy to pass (`currentPricing()`), and it still uses the concrete
`ConsoleUI` and staff classes.

**Dependency Injection (not a SOLID principle, but used).** `Main` creates the
`Restaurant`, `ConsoleUI`, `ArrivalSchedule` and `Objective` and passes them into
the `GameController` constructor; the controller never calls `new` on them.
As the Chapter 9 slides point out, this is DI but not DIP, because the
injected types are concrete. The pricing policy is injected differently: it
is passed into `takeOrder` each time (method injection) rather than given to
the Waiter's constructor, because the policy can change from turn to turn.

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
- The same lock makes Happy Hour safe: an order taken during Happy Hour keeps
  its 20% off after it ends, and orders taken before it started keep their
  full price.
- `HappyHour.used` and `turnsLeft` are private and change only through
  `activate()` (refused a second time) and `endTurn()` (never below 0), so
  Happy Hour can't be restarted or made to last longer from outside.

## Alternative considered (Stage 2)

The Critic could have **reacted to service**, for example by leaving a bad
review that lowers every other customer's satisfaction when the Critic leaves
unhappy. We rejected it: it needs a new hook on `Customer` plus changes to
`GameController` to apply the effect, which breaks the "no turn-logic changes"
result above for little gameplay gain. Ordering the most expensive dish uses
the existing `chooseMenuItem()` hook, and it still makes the Critic matter:
Pasta takes 2 cooking units, and the Critic loses 12 satisfaction per turn
while waiting, so neglecting the Critic costs the most.

## Trade-offs and alternatives (Stage 3)

1. **Host repeats the Waiter's seating checks.** *Alternative:* an abstract
   `SeatingStaff extends Staff` with one `seat()`, extended by both `Waiter`
   and `Host`. That removes the duplicate, but it changes the superclass of
   `Waiter`, a stable Stage 1 class, to save about 6 lines. We kept the small
   duplicate. This is the alternative design we discuss in the report.
2. **`HappyHour` holds both its state and its price rule.** *Alternative:* a
   separate `HappyHourPricing` and `HappyHourTimer`. That is cleaner SRP, but
   two classes for one 2-turn feature.
3. **The pricing policy is passed into `takeOrder`, not stored in the
   Waiter.** Method injection instead of constructor injection, because the
   policy changes from turn to turn; storing it would need a setter on the
   Waiter that the controller must remember to call every turn.
