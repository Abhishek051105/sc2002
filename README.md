# SC2002 Restaurant Rush

Console restaurant-management game for SC2002. Group: Raghav, Abhishek, Aaryan, Aekas, Ayush.

You manage a restaurant for 18 turns. Each turn the Waiter and the Chef can do one task each,
and the Cashier takes one payment automatically. You win by ending turn 18 with **at least 4
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

## Reproducible test runs

Each script in `tests/` is piped in as the manager's input:

```
java -cp out control.Main < tests/stage1_victory.txt                  # Bash or cmd
Get-Content tests\stage1_victory.txt | java -cp out control.Main      # PowerShell
```

| Script | Expected result |
|---|---|
| `tests/stage1_victory.txt` | VICTORY: 5 paid, $66.00, avg 79.2 |
| `tests/stage1_defeat.txt` | DEFEAT: 4 paid, $51.00, avg 58.0 (satisfaction target fails) |
| `tests/stage1_invalid_inputs.txt` | The victory run with invalid input mixed in; same result as the victory run |

Turn-by-turn tables and the list of invalid inputs tested are in
[docs/test-notes.md](docs/test-notes.md).

## How to play

Each turn shows the queue, the tables and the staff, then asks:

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
| 2 | `stage-2` | To be added | To be added |
| 3 | `stage-3` | To be added | To be added |

Stage comparisons (new files, changed files with reasons) will be added here for
stage-1 → stage-2 and stage-2 → stage-3.

## Design documents

- Stage 1 class diagram: [PNG](docs/uml/stage1/class-diagram.png) · [PlantUML source](docs/uml/stage1/class-diagram.puml)
- Stage 1 sequence diagram (one turn, for reference): [PNG](docs/uml/stage1/sequence-diagram.png) · [PlantUML source](docs/uml/stage1/sequence-diagram.puml)
- [Key abstractions and SOLID](docs/key-abstractions-and-solid.md)
- [Design log](docs/design-log.md)
- [Stage 1 test notes](docs/test-notes.md)

## AI history

Each member keeps their full AI chats, in order, on their own branch under `ai-history/`.

| Member | Branch | Stage 1 |
|---|---|---|
| Abhishek | [`abhishek`](https://github.com/Abhishek051105/sc2002/tree/abhishek/ai-history) | [ai-history/stage-1](https://github.com/Abhishek051105/sc2002/tree/abhishek/ai-history/stage-1) |
| Aaryan | to be added | to be added |
| Aekas | to be added | to be added |
| Ayush | to be added | to be added |
| Raghav | to be added | to be added |
