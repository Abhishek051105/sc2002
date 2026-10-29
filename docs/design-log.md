# Design Log

One entry per real choice: date, decision, why, alternative considered.
This feeds the report's "trade-offs" and "alternative design" sections.

| Date | Decision | Why | Alternative considered |
|---|---|---|---|
| 22 Oct | `ArrivalSchedule.customersArrivingAt(turn)` returns `List<Customer>` (empty if nobody arrives) instead of a single `Customer` or `null` | No null checks in the controller; more than one arrival per turn becomes a schedule change, not a controller change | Return `Customer`/`null` (draft diagram); `Optional<Customer>` |
| 22 Oct | `ConsoleUI.showFinalResult` takes the `Objective` | UI must show which targets passed/failed without re-implementing the win rule | Pass three booleans; UI re-checks thresholds itself (duplicates the rule) |
| 22 Oct | Loop over a copy of the customer list in step 5 | Customers leave mid-loop; removing from the list being iterated throws `ConcurrentModificationException` | `Iterator.remove()` |
| 23 Oct | Keep Raghav's Customer API (`seat()`, `orderTaken()`, `serve(turn)`, `finishEating(turn)`, `markPaid(turn)`, `decreaseSatisfaction()`, statuses `WAITING_FOR_FOOD`/`SERVED`) | Already written and tested by its owner; renaming gives no design gain | Rename to the draft diagram's names (`seatAt`, `placeOrder`, ...) |
| 23 Oct | Customer does not hold its Table/Order; `Table` holds the occupant, `Order` holds the customer, `Restaurant` looks them up | Matches Raghav's existing Customer; keeps links one-directional | Two-way links (Customer <-> Table/Order), simpler lookups but more coupling |
| 23 Oct | Added `chooseMenuItem(Menu)` and `calculateBill(int)` to Customer | Stage 2 VIP/Critic override these instead of the controller using instanceof (OCP) | Controller branches on customer type (violates OCP) |
| 24 Oct | GameController calls `stats.recordServed()` right after a successful `waiter.serve()` | Keeps Waiter free of scoring (SRP); the controller already sequences the step | Waiter calls `recordServed()` itself (Waiter would then depend on GameStats) |
| 24 Oct | Added `getServedTurn()` / `getReadyToPayTurn()` getters to Customer | Controller needs servedTurn to apply "finish eating the turn after being served"; Cashier needs readyToPayTurn for "pay the turn after" | Wrap `finishEating()` in try/catch (uses exceptions for normal flow, bad practice) |
| 24 Oct | Bad customer/order IDs are turned into `InvalidActionException` in the controller | Typos become a normal "try again" message instead of a crash, and no staff task is used up | Let `null` reach Waiter/Chef (NullPointerException) |
| 28 Oct | Staff methods call `checkCanAct()` first, then the role's own checks, and `markActed()` last | A rejected choice never uses up the task; "already acted" is reported before anything else | Mark acted first and undo on failure (easy to forget the undo) |
| 28 Oct | `Table.assign()` throws `IllegalStateException` if occupied, even though `Waiter` checks `isFree()` first | Manager mistakes are `InvalidActionException` (caught in Waiter); this is only a safety net for programming bugs | Trust callers and silently overwrite the occupant |
| 28 Oct | `GameStats` derives the paid count from the list of paid satisfactions instead of a separate counter | Count and average can't get out of sync | Separate `paidCount` field |
| 28 Oct | `Cashier` marks itself as acted after a payment and skips if it already acted | Uses the same one-task-per-turn rule as Waiter/Chef, so "one payment per turn" lives in `Staff` | A separate flag inside Cashier |
| 29 Oct | `Restaurant.customerLeft(c)` does the departure clean-up; `Cashier.processNextPayment(restaurant, turn)` looks up the order/table through `Restaurant` | Cashier and GameController were both searching the order and table lists, so the same logic lived in two places and both knew how Restaurant stores data | Customer holds its own Table and Order (two-way links, rejected on 23 Oct) |
| 29 Oct | `ConsoleUI.showFinalResult` gets pass/fail from `Objective` instead of re-checking 4 / $45 / 60 | Keeps the win rule in one class, as decided on 22 Oct; the UI had drifted from that | Leave the numbers in the UI (two places to edit if targets change) |
| 29 Oct | `Customer` state transitions are `final` | Every customer type must follow the same flow (brief 4.2); `final` makes the LSP claim enforced by the compiler | Rely on teammates not overriding them |
| 29 Oct | Removed unused methods (`getActiveOrders`, `getAllOrders`, `Menu.getItems/createDefault`, `hasLeft`, `hasPaid`, `getRoleName`) | Unused code clutters the class diagram, which must match the code | Keep them "just in case" |
