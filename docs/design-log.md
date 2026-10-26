# Design Log

One entry per real choice: date, decision, why, alternative considered.
This feeds the report's "trade-offs" and "alternative design" sections.

| Date | Decision | Why | Alternative considered |
|---|---|---|---|
| 22 Oct | `ArrivalSchedule.customersArrivingAt(turn)` returns `List<Customer>` (empty if nobody arrives) instead of a single `Customer` or `null` | No null checks in the controller; more than one arrival per turn becomes a schedule change, not a controller change | Return `Customer`/`null` (draft diagram); `Optional<Customer>` |
| 22 Oct | `ConsoleUI.showFinalResult` takes the `Objective` | UI must show which targets passed/failed without re-implementing the win rule | Pass three booleans; UI re-checks thresholds itself (duplicates the rule) |
| 22 Oct | Loop over a copy of the customer list in step 5 | Customers leave mid-loop; removing from the list being iterated throws `ConcurrentModificationException` | `Iterator.remove()` |
| | | | |

| 23 Oct | Keep Raghav's Customer API (`seat()`, `orderTaken()`, `serve(turn)`, `finishEating(turn)`, `markPaid(turn)`, `decreaseSatisfaction()`, statuses `WAITING_FOR_FOOD`/`SERVED`) | Already written and tested by its owner; renaming gives no design gain | Rename to the draft diagram's names (`seatAt`, `placeOrder`, ...) |
| 23 Oct | Customer does not hold its Table/Order; `Table` holds the occupant, `Order` holds the customer, `Restaurant` looks them up | Matches Raghav's existing Customer; keeps links one-directional | Two-way links (Customer <-> Table/Order), simpler lookups but more coupling |
| 23 Oct | Added `chooseMenuItem(Menu)` and `calculateBill(int)` to Customer | Stage 2 VIP/Critic override these instead of the controller using instanceof (OCP) | Controller branches on customer type (violates OCP) |
| 24 Oct | GameController calls `stats.recordServed()` right after a successful `waiter.serve()` | Keeps Waiter free of scoring (SRP); the controller already sequences the step | Waiter calls `recordServed()` itself (Waiter would then depend on GameStats) |
| 24 Oct | Added `getServedTurn()` / `getReadyToPayTurn()` getters to Customer | Controller needs servedTurn to apply "finish eating the turn after being served"; Cashier needs readyToPayTurn for "pay the turn after" | Wrap `finishEating()` in try/catch (uses exceptions for normal flow, bad practice) |
| 24 Oct | Bad customer/order IDs are turned into `InvalidActionException` in the controller | Typos become a normal "try again" message instead of a crash, and no staff task is used up | Let `null` reach Waiter/Chef (NullPointerException) |
