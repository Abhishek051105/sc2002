# Chat 01: Stage 3, Happy Hour and demo prep

- **Tool:** Claude (Claude Code extension in VS Code)
- **Dates:** 6–15 Nov 2026 (SGT)
- **Stage:** 3

A selection of the chat, not the full transcript. My requests are written up in my own words,
keeping what I asked each time. Claude's replies are summarised in my own words.

---

## 1. Checking Stages 1 and 2, then implementing Happy Hour (6 Nov)

**My request:** Pull the latest commits and check that Stages 1 and 2 still work. Then, using the
attached Stage 3 plan, implement my part only: `PricingPolicy`, `StandardPricing`, `HappyHour`,
the pricing change in `Waiter.takeOrder`, `Customer.recoverSatisfaction`, and the SOLID doc and
design log updates. Split it into small conventional commits, and show me what will be pushed
before pushing it.

**Claude:** Checked the earlier stages first:
- All six Stage 1 and 2 scripts still gave the expected results (79.2 / 58.0 / 79.2 and
  75.6 / 53.8 / 75.6), and the `stage-2` tag pointed at the current `main`.
- Neither of the other Stage 3 code branches (Abhishek's combos, Aaryan's Host) touched my files.

Then it wrote my part as six commits on a local branch:
- `PricingPolicy` is a one-method interface, `applyTo(billCents)`. `StandardPricing` leaves the
  bill unchanged.
- `HappyHour` takes 20% off, rounded to the nearest cent in integer cents,
  `(cents * 80 + 50) / 100`. It can be started once per game, lasts 2 turns, and its "used" and
  "turns left" values are private and only change through `activate()` and `endTurn()`.
- `Customer.recoverSatisfaction(points)` gives +15 (up to 100) to customers who are still
  waiting for a table or food, the same statuses that lose satisfaction. It is `final`, like the
  other flow methods.
- `Waiter.takeOrder` now takes a `PricingPolicy`, and the bill is
  `pricing.applyTo(c.calculateBill(price))`: the customer's own discount first, then Happy Hour.
  That gives the brief's VIP Burger example, $15.00 x 0.9 x 0.8 = $10.80.
- The SOLID doc gained the new classes, Happy Hour as our clearest dependency-inversion example,
  where open/closed held and where it didn't, and three trade-offs. The design log gained the
  matching entries.

It pointed out two small choices of its own for me to approve:
- `recoverSatisfaction` returns true only if satisfaction actually went up, so a customer
  already at 100 doesn't print "recovers to 100".
- The old two-argument `takeOrder` was kept as a one-line wrapper, so `main` kept compiling until
  Aaryan's controller passed in the current pricing policy.

## 2. Branch, pull request and merge (6 Nov)

**My request:** Push it as a branch with a pull request, then merge it into `main`, so the
feature is easy to see and to revert as one unit.

**Claude:** Pushed `feat/happy-hour`, opened and merged pull request #6 under my account, and
re-ran all six Stage 1 and 2 scripts on the merged `main` (unchanged). It noted the two points
Aaryan needed for his controller: call `takeOrder` with the current pricing policy and then
delete the wrapper, and print "recovers to N" only when `recoverSatisfaction` returns true.

## 3. Preparing my demo clips (14–15 Nov)

**My request:** Here is my script for two demo video clips: a Stage 3 feature (Happy Hour, Host
and combo meals) and SOLID with trade-offs. Open the files I talk about, in the order I show
them, and highlight the lines for each step.

**Claude:** Generated `victory_run.txt` from `tests/stage3_victory.txt` (VICTORY, 5 paid,
$81.54, average 84.6), then checked every line number in my script against the code. They all
matched, apart from two ranges that stopped too early:
- In `ComboMeal.java`, the constructor's `super(...)` call runs to line 13, not 11.
- In `Customer.java`, the five `final` steps run to line 224, not 210, so `markPaid` (the "pay"
  step) was cut off.

It opened the files for each clip in script order and highlighted those lines, without
changing any code.

---

## What I took from this chat

- An interface earns its place when there are two real implementations. `StandardPricing` and
  `HappyHour` both are, so the Waiter's dependency on `PricingPolicy` is genuine dependency
  inversion, not an interface added for the checklist.
- The order of discounts is a design decision, not a detail: customer discount first, then Happy
  Hour, which matches the brief's example.
- Keeping a temporary wrapper let my branch merge without breaking `main` before a teammate's
  branch landed.
- Checking the script against the code before recording caught two line ranges that would have
  cut off what I was explaining.
