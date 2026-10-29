# Stage 1 Test Notes

Every run below is reproducible: the input script in `tests/` is piped into the game, so the
same input always gives the same output. See the README for the build and run commands.

C1-C6 = customers by arrival order, T1/T2 = tables. Food rule: customer *n* orders
`menu[(n-1) % 3]`, so C1 Salad, C2 Burger, C3 Pasta, C4 Salad, C5 Burger, C6 Pasta.

## Summary

| Script | Result | Paid | Revenue | Avg satisfaction | Served |
|---|---|---|---|---|---|
| `tests/stage1_victory.txt` | VICTORY | 5 | $66.00 | 79.2 | 5 |
| `tests/stage1_defeat.txt` | DEFEAT | 4 | $51.00 | 58.0 (fails the 60 target) | 5 |
| `tests/stage1_invalid_inputs.txt` | VICTORY | 5 | $66.00 | 79.2 | 5 |

## Victory run

| Turn | Arrival | Waiter | Chef | What happens |
|---|---|---|---|---|
| 1 | C1 | Seat C1 at T1 | Wait | |
| 2 | | Take C1's order (Salad) | Cook C1 | Salad READY |
| 3 | | Serve C1 | Wait | C1 eats (satisfaction 84) |
| 4 | C2 | Seat C2 at T2 | Wait | C1 ready to pay |
| 5 | | Take C2's order (Burger) | Cook C2 | C1 pays $9.00 |
| 6 | | Serve C2 | Wait | |
| 7 | C3 | Seat C3 at T1 | Wait | C2 ready to pay |
| 8 | | Take C3's order (Pasta) | Cook C3 (1/2) | C2 pays $15.00 |
| 9 | | Wait | Cook C3 (2/2) | Pasta READY |
| 10 | C4 | Serve C3 | Wait | C3 eats (76) |
| 11 | | Seat C4 at T2 | Wait | C3 ready to pay |
| 12 | | Take C4's order (Salad) | Cook C4 | C3 pays $18.00 |
| 13 | C5 | Serve C4 | Wait | |
| 14 | | Seat C5 at T1 | Wait | C4 ready to pay |
| 15 | | Take C5's order (Burger) | Cook C5 | C4 pays $9.00 |
| 16 | C6 | Serve C5 | Wait | |
| 17 | | Seat C6 at T2 | Wait | C5 ready to pay |
| 18 | | Take C6's order (Pasta) | Cook C6 (1/2) | C5 pays $15.00 |

Paid satisfactions: 84, 84, 76, 76, 76, so the average is 79.2. All three targets pass.

## Defeat run

The waiter waits on turns 1-3, then plays the same strategy as the victory run (serve if
READY, else take an order, else seat). C1 loses 3 turns in the queue and the delay carries
through to every later customer.

| Turn | Arrival | Waiter | Chef | What happens |
|---|---|---|---|---|
| 1 | C1 | Wait | Wait | |
| 2 | | Wait | Wait | |
| 3 | | Wait | Wait | |
| 4 | C2 | Seat C1 at T1 | Wait | |
| 5 | | Take C1's order (Salad) | Cook C1 | Salad READY |
| 6 | | Serve C1 | Wait | C1 eats (60) |
| 7 | C3 | Seat C2 at T2 | Wait | C1 ready to pay |
| 8 | | Take C2's order (Burger) | Cook C2 | C1 pays $9.00 |
| 9 | | Serve C2 | Wait | C2 eats (60) |
| 10 | C4 | Seat C3 at T1 | Wait | C2 ready to pay |
| 11 | | Take C3's order (Pasta) | Cook C3 (1/2) | C2 pays $15.00 |
| 12 | | Seat C4 at T2 | Cook C3 (2/2) | Pasta READY |
| 13 | C5 | Serve C3 | Wait | C3 eats (52) |
| 14 | | Take C4's order (Salad) | Cook C4 | C3 ready to pay |
| 15 | | Serve C4 | Wait | C4 eats (60), C3 pays $18.00 |
| 16 | C6 | Seat C5 at T1 | Wait | C4 ready to pay |
| 17 | | Take C5's order (Burger) | Cook C5 | C4 pays $9.00 |
| 18 | | Serve C5 | Wait | C5 eats but cannot pay before the game ends |

Paid satisfactions: 60, 60, 52, 60, so the average is 58.0. Paid count and revenue pass,
but average satisfaction misses 60, so the game ends in DEFEAT. The only difference from the
victory strategy is the slow start, which shows how the player's choices change the result.

## Invalid inputs tested

`tests/stage1_invalid_inputs.txt` replays the victory run with these inputs mixed in. Each one
prints the reason and asks again, without using up the staff member's task, so the run still
ends with exactly the victory numbers.

| Turn | Input | Message shown |
|---|---|---|
| 1 | `abc` | Invalid input, Please enter a valid number only |
| 1 | blank line | Ignored, asks again |
| 1 | `9` as the waiter action | Please enter a number between 1 and 4 |
| 1 | Take an order from C1 (not seated) | Customer 1 cannot order, they are WAITING |
| 1 | Chef cooks order 1 (no order yet) | There is no order for C1. |
| 2 | Serve order 1 (no order yet) | There is no order for C1. |
| 3 | Chef cooks order 1 (already served) | Order 1 cannot be cooked, it is SERVED |
| 4 | Seat C2 at T1 (occupied) | Table 1 is occupied |
| 5 | Seat C1 (already seated) | Customer 1 is not in the queue |
| 7 | Table `5` | Please enter a number between 1 and 2 |
| 8 | Serve order 3 (not ordered yet) | There is no order for C3. |
| 9 | Serve order 3 (Pasta at 1/2) | Order 3 is not READY yet, it is IN_PREPARATION |
| 10 | Chef cooks order 3 (already served) | Order 3 cannot be cooked, it is SERVED |
| 11 | Seat customer 99 | There is no customer C99. |

## Other cases checked by hand

| Case | Result |
|---|---|
| Never seat anyone | C1 leaves at the end of turn 13, C2 at the end of turn 16; 2 departures, average N/A, DEFEAT with all three targets failing |
| Food READY but never served | The customer keeps losing satisfaction, leaves, the order becomes CANCELLED and the table is freed; serving or cooking that order is then rejected |
| Input script ends before turn 18 | Prints "Input ended before the game finished." and exits without a stack trace |
| Script piped from Windows PowerShell (`Get-Content ... \| java ...`) | Same results as piping from Bash or cmd |

## Notes

- The customer arriving at turn 16 can never pay before turn 18 ends, so at most 5 customers
  can pay. This is intended, not a bug.
- In the Stage 1 plan's simulation of 20,000 random games, every game where the Waiter and
  Chef never waited while a valid task existed was a victory. The base game only punishes
  idling and delays. Stage 2's Critic customer (loses 12 per turn) is what makes the
  order of choices matter.
