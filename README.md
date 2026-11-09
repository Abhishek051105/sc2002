# SC2002 Restaurant Rush

Console restaurant-management game for SC2002. Group: Raghav, Abhishek, Aaryan, Aekas, Ayush.

You manage a restaurant for 18 turns. Each turn the Host, the Waiter and the Chef can do one
task each, and the Cashier takes one payment automatically. Once per game you can also start a
Happy Hour. You win by ending turn 18 with **at least 4
paid customers, at least $45.00 revenue and an average satisfaction of at least 60**.

## Build and run

Requires JDK 17 or later. Run from the repository root.

**Git Bash / macOS / Linux**
```
javac -d out $(find src -name "*.java")
java -cp out control.Main
```

**Windows PowerShell**
```
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
java -cp out control.Main
```

| Command | Setting |
|---|---|
| `java -cp out control.Main` | Stage 3: Regular, VIP and Critic customers, the two combos on the menu, Host and Happy Hour |
| `java -cp out control.Main stage2` | Stage 2 menu (no combos). Used to re-run the Stage 2 scripts |
| `java -cp out control.Main regular` | Stage 1: six Regular customers, no combos. Used to re-run the Stage 1 scripts |

The Host and Happy Hour prompts are shown in every mode. Answering `0` to both gives exactly the
Stage 1 or Stage 2 game.

## Reproducible test runs

Each script in `tests/` is piped in as the manager's input:

```
java -cp out control.Main < tests/stage1_victory.txt                  # Bash or cmd
Get-Content tests\stage1_victory.txt | java -cp out control.Main      # PowerShell
```

The Stage 1 scripts are run with the `regular` argument, and the Stage 2 ones with `stage2`:

```
java -cp out control.Main regular < tests/stage1_victory_on_stage3.txt
Get-Content tests\stage1_victory_on_stage3.txt | java -cp out control.Main regular
```

| Script | Command | Expected result |
|---|---|---|
| `tests/stage3_victory.txt` | `control.Main` | VICTORY: 5 paid, $81.54, avg 84.6 |
| `tests/stage3_defeat.txt` | `control.Main` | DEFEAT: 3 paid, $40.50, avg 65.0 (paid and revenue targets fail) |
| `tests/stage3_invalid_inputs.txt` | `control.Main` | The Stage 3 victory run with invalid input mixed in; same result as the Stage 3 victory run |
| `tests/stage3_slow_start_with_host.txt` | `control.Main` | The defeat's slow start, but using the Host: VICTORY, 4 paid, $64.80, avg 72.3 |
| `tests/stage2_*_on_stage3.txt` | `control.Main stage2` | Same results as the Stage 2 scripts below |
| `tests/stage1_*_on_stage3.txt` | `control.Main regular` | Same results as the Stage 1 scripts below |

The scripts below are kept from the earlier tags. They have no answers for the Host and Happy
Hour prompts, so they only run on the `stage-1` and `stage-2` builds; the `*_on_stage3.txt`
versions are the same runs for this build.

| Script | Command (on its own tag) | Expected result |
|---|---|---|
| `tests/stage2_victory.txt` | `control.Main` | VICTORY: 5 paid, $73.50, avg 75.6 |
| `tests/stage2_defeat.txt` | `control.Main` | DEFEAT: 4 paid, $58.50, avg 53.8 (satisfaction target fails) |
| `tests/stage2_invalid_inputs.txt` | `control.Main` | The Stage 2 victory run with invalid input mixed in; same result as the Stage 2 victory run |
| `tests/stage1_victory.txt` | `control.Main regular` | VICTORY: 5 paid, $66.00, avg 79.2 |
| `tests/stage1_defeat.txt` | `control.Main regular` | DEFEAT: 4 paid, $51.00, avg 58.0 (satisfaction target fails) |
| `tests/stage1_invalid_inputs.txt` | `control.Main regular` | The victory run with invalid input mixed in; same result as the victory run |

Turn-by-turn tables and the list of invalid inputs tested are in
[docs/test-notes.md](docs/test-notes.md).

## How to play

Each turn shows the queue, the tables and the staff, then asks:

- **Happy Hour** (until it has been used): `1` to start it now, `0` not yet.
- **Host:** the customer to seat (then the table), or `0` to wait.
- **Waiter:** `1` Seat, `2` Take order, `3` Serve, `4` Wait. Customers are picked by number
  (C3 is `3`) and tables by number.
- **Chef:** the order number to cook, or `0` to wait. An order's number is its customer's number.

An invalid choice (for example an occupied table or a dish that isn't ready) prints the reason
and asks again without using up that staff member's task. Letters, blank lines and
out-of-range numbers are also asked again.

## Rules and decisions (Stage 1)

| Topic | Decision |
|---|---|
| Arrivals | One Regular customer at turns 1, 4, 7, 10, 13 and 16, starting at satisfaction 100 |
| Menu | Salad $9.00 (1 unit), Burger $15.00 (1 unit), Pasta $18.00 (2 units) |
| Food choice | Customer *n* orders `menu[(n-1) % 3]`: Salad, Burger, Pasta, Salad, Burger, Pasta. Shown once the order is taken |
| Turn order | Arrivals, start-of-turn display, Waiter, Chef, payment and eating, waiting, end-of-turn display |
| Chef after Waiter | The Chef can cook an order taken this turn; a dish finished this turn can only be served next turn |
| Satisfaction | Customers in the queue, seated, or waiting for food (even if it is READY) lose 8 per turn, down to 0. At 0 they leave and any unfinished order is cancelled. Eating, ready-to-pay and paid customers lose nothing |
| Eating and paying | A customer finishes eating the turn after being served and pays the turn after that |
| Cashier | One payment per turn: earliest ready-to-pay turn first, ties to the lower customer number |
| Served count | Dishes served; paid customers are counted separately |
| Average satisfaction | Recorded at payment; shown as N/A until someone pays |
| Money | Stored as integer cents, shown as dollars with 2 decimals |
| Game end | Only after turn 18; there is no early ending |

The customer arriving at turn 16 can never pay before the game ends, so at most 5 customers can
pay. This is intended.

## Rules and decisions (Stage 2)

Everything in the Stage 1 table still applies. Stage 2 adds two customer types:

| Customer | Loses per waiting turn | Bill | Special behaviour |
|---|---|---|---|
| Regular | 8 | Full price | None |
| VIP | 5 | 10% off (`price * 90 / 100` in cents: $8.10, $13.50, $16.20) | None |
| Critic | 12 | Full price | Always orders the most expensive dish on the menu (Pasta) |

| Topic | Decision |
|---|---|
| Arrivals | Still 6 arrivals at turns 1, 4, 7, 10, 13, 16. Arrival 2 (turn 4) is the VIP, arrival 4 (turn 10) is the Critic, the rest are Regular |
| Food choice | Unchanged: customer *n* orders `menu[(n-1) % 3]`, except the Critic, who overrides it with the most expensive dish (C4 orders Pasta instead of Salad) |
| Display | The queue and table lines show each customer's type, and an order shows its bill (for example `Seated C2 VIP ... \| Order: Burger $13.50`) |
| Bill | Worked out once, when the order is taken, and locked into the order |

## Project layout

```
src/boundary   ConsoleUI (input and output only)
src/control    Main, GameController, ArrivalSchedule, Objective, GameConfig
src/entity     Restaurant, customers, staff, tables, menu, orders, stats
tests/         Input scripts for reproducible runs
docs/          Design log, key abstractions and SOLID notes, test notes, UML
```

## Stages

Repository: https://github.com/Abhishek051105/sc2002

| Stage | Tag | What changed | Class diagram |
|---|---|---|---|
| 1 | [`stage-1`](https://github.com/Abhishek051105/sc2002/tree/stage-1) | Base game with Regular customers only | [stage1/class-diagram.png](docs/uml/stage1/class-diagram.png) |
| 2 | [`stage-2`](https://github.com/Abhishek051105/sc2002/tree/stage-2) | Adds VIP and Critic customers | [stage2/class-diagram.png](docs/uml/stage2/class-diagram.png) |
| 3 | `stage-3` | To be added | To be added |

### stage-1 → stage-2

All changes: [compare stage-1...stage-2](https://github.com/Abhishek051105/sc2002/compare/stage-1...stage-2)

Stage 2 was built mostly as new files. Two new customer types were added without changing
`GameController` or any staff class: each customer object decides its own satisfaction loss,
bill and dish, and the rest of the game just calls those methods.

**New files**

| File | Responsibility |
|---|---|
| `entity/VIPCustomer.java` | Extends `Customer`: loses 5 per turn, `calculateBill` gives 10% off |
| `entity/CriticCustomer.java` | Extends `Customer`: loses 12 per turn, `chooseMenuItem` picks the most expensive dish |
| `entity/CustomerType.java` | Enum `REGULAR, VIP, CRITIC`, used only to describe the arrival list |
| `tests/stage2_victory.txt`, `tests/stage2_defeat.txt` | Reproducible Stage 2 runs |

**Changed files**

| File | Change | Reason |
|---|---|---|
| `entity/Customer` | Added abstract `getTypeLabel()` | The UI must show "VIP" or "Critic" without `instanceof`, so each subclass names itself (polymorphism) |
| `entity/RegularCustomer` | Added `getTypeLabel()` returning "Regular" | Required by the new abstract method |
| `control/GameConfig` | `ARRIVAL_TURNS` is now a `List`; added `ARRIVAL_TYPES` and `ALL_REGULAR` | Which arrival is which type is configuration, not logic. A `List` keeps positions, so turn *i* and type *i* line up |
| `control/ArrivalSchedule` | Takes the turns and types; `createCustomer()` builds the matching subclass | The one place allowed to name concrete customer classes, so adding a type changes one method |
| `control/Main` | Optional `regular` argument uses `ALL_REGULAR` | Lets the Stage 1 scripts run on the Stage 2 build, which is our "what still works" evidence |
| `boundary/ConsoleUI` | Shows the type label and the locked bill in the queue and table lines | So the player can see who is VIP or Critic and the discounted price |

**Unchanged:** `GameController`, `Waiter`, `Chef`, `Cashier`, `Order`, `Menu`, `MenuItem`,
`Restaurant`, `Table`, `GameStats`, `Objective`, `Staff`.

**What still works:** the seat, order, serve, eat and pay flow is the same for every customer
type, and the three Stage 1 scripts give exactly their old results when run with `regular`
(79.2, 58.0, 79.2).

## Design documents

- Stage 1 class diagram: [PNG](docs/uml/stage1/class-diagram.png) · [PlantUML source](docs/uml/stage1/class-diagram.puml)
- Stage 1 sequence diagram (one turn, for reference): [PNG](docs/uml/stage1/sequence-diagram.png) · [PlantUML source](docs/uml/stage1/sequence-diagram.puml)
- Stage 2 class diagram (new and changed classes coloured): [PNG](docs/uml/stage2/class-diagram.png) · [PlantUML source](docs/uml/stage2/class-diagram.puml)
- Stage 2 sequence diagram (a VIP from arrival to paying the discounted $13.50): [PNG](docs/uml/stage2/sequence-diagram.png) · [PlantUML source](docs/uml/stage2/sequence-diagram.puml)
- [Key abstractions and SOLID](docs/key-abstractions-and-solid.md)
- [Design log](docs/design-log.md)
- [Test notes (Stage 1 and Stage 2)](docs/test-notes.md)

## AI history

Each member keeps their AI chats for the project, in order, under `ai-history/` on their own branch.

| Member | Branch | Stage 1 | Stage 2 |
|---|---|---|---|
| Abhishek | [`abhishek`](https://github.com/Abhishek051105/sc2002/tree/abhishek/ai-history) | [ai-history/stage-1](https://github.com/Abhishek051105/sc2002/tree/abhishek/ai-history/stage-1) | [ai-history/stage-2](https://github.com/Abhishek051105/sc2002/tree/abhishek/ai-history/stage-2) |
| Aaryan | [`aaryan`](https://github.com/Abhishek051105/sc2002/tree/aaryan/ai-history) | [ai-history/stage-1](https://github.com/Abhishek051105/sc2002/tree/aaryan/ai-history/stage-1) | [ai-history/stage-2](https://github.com/Abhishek051105/sc2002/tree/aaryan/ai-history/stage-2) |
| Aekas | [`aekas`](https://github.com/Abhishek051105/sc2002/tree/aekas/ai-history) | [ai-history/stage-1](https://github.com/Abhishek051105/sc2002/tree/aekas/ai-history/stage-1) | [ai-history/stage-2](https://github.com/Abhishek051105/sc2002/tree/aekas/ai-history/stage-2) |
| Ayush | to be added | to be added | to be added |
| Raghav | to be added | to be added | to be added |
