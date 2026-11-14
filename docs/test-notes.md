# Test Notes

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

## Stage 2 Test Runs

Stage 2 runs use the default command (`java -cp out control.Main`). C2 is the VIP (arrives turn
4, loses 5 per turn, 10% off) and C4 is the Critic (arrives turn 10, loses 12 per turn, always
orders the most expensive dish, Pasta). The rest are Regular. The Stage 1 scripts above still give
their old results when run with `control.Main regular`.

| Script | Result | Paid | Revenue | Avg satisfaction |
|---|---|---|---|---|
| `tests/stage2_victory.txt` | VICTORY | 5 | $73.50 | 75.6 |
| `tests/stage2_defeat.txt` | DEFEAT | 4 | $58.50 | 53.8 (fails the 60 target) |
| `tests/stage2_invalid_inputs.txt` | VICTORY | 5 | $73.50 | 75.6 |

### Stage 2 victory run (`tests/stage2_victory.txt`)

| Turn | Arrival | Waiter | Chef | What happens |
|---|---|---|---|---|
| 1 | C1 Regular | Seat C1 at T1 | Wait | |
| 2 | | Take C1's order (Salad) | Cook C1 | Salad READY |
| 3 | | Serve C1 | Wait | C1 eats (84) |
| 4 | **C2 VIP** | Seat C2 at T2 | Wait | C1 ready to pay |
| 5 | | Take C2's order (Burger, **$13.50**) | Cook C2 | Burger READY; C1 pays $9.00 |
| 6 | | Serve C2 | Wait | C2 eats (90) |
| 7 | C3 Regular | Seat C3 at T1 | Wait | C2 ready to pay |
| 8 | | Take C3's order (Pasta) | Cook C3 (1/2) | C2 pays **$13.50** |
| 9 | | Wait | Cook C3 (2/2) | Pasta READY |
| 10 | **C4 Critic** | Serve C3 | Wait | C3 eats (76); C4 waits in the queue |
| 11 | | Seat C4 at T2 | Wait | C3 ready to pay |
| 12 | | Take C4's order (**Pasta**, Critic rule) | Cook C4 (1/2) | C3 pays $18.00 |
| 13 | C5 Regular | Seat C5 at T1 | Cook C4 (2/2) | Pasta READY |
| 14 | | Serve C4 | Wait | C4 eats (52) |
| 15 | | Take C5's order (Burger) | Cook C5 | Burger READY; C4 ready to pay |
| 16 | C6 Regular | Serve C5 | Wait | C4 pays $18.00 |
| 17 | | Seat C6 at T2 | Wait | C5 ready to pay |
| 18 | | Take C6's order (Pasta) | Cook C6 (1/2) | C5 pays $15.00 |

Paid satisfactions: 84, 90 (VIP), 76, 52 (Critic), 76, so the average is 75.6. Revenue is
$9.00 + $13.50 + $18.00 + $18.00 + $15.00 = $73.50. All three targets pass.

### Stage 2 defeat run (`tests/stage2_defeat.txt`)

The same slow start as the Stage 1 defeat: the Waiter waits on turns 1 to 3. The delay ripples
through every later customer, and the Critic, who loses 12 per turn, pays at only 28.

| Turn | Arrival | Waiter | Chef | What happens |
|---|---|---|---|---|
| 1-3 | C1 Regular (turn 1) | Wait | Wait | C1 waits in the queue |
| 4 | **C2 VIP** | Seat C1 at T1 | Wait | C2 waits in the queue |
| 5 | | Take C1's order (Salad) | Cook C1 | Salad READY |
| 6 | | Serve C1 | Wait | C1 eats (60) |
| 7 | C3 Regular | Seat C2 at T2 | Wait | C1 ready to pay |
| 8 | | Take C2's order (Burger, $13.50) | Cook C2 | Burger READY; C1 pays $9.00 |
| 9 | | Serve C2 | Wait | C2 eats (75) |
| 10 | **C4 Critic** | Seat C3 at T1 | Wait | C2 ready to pay |
| 11 | | Take C3's order (Pasta) | Cook C3 (1/2) | C2 pays $13.50 |
| 12 | | Seat C4 at T2 | Cook C3 (2/2) | Pasta READY |
| 13 | C5 Regular | Serve C3 | Wait | C3 eats (52) |
| 14 | | Take C4's order (Pasta) | Cook C4 (1/2) | C3 ready to pay |
| 15 | | Wait | Cook C4 (2/2) | Pasta READY; C3 pays $18.00 |
| 16 | C6 Regular | Serve C4 | Wait | C4 eats (28) |
| 17 | | Wait | Wait | C4 ready to pay |
| 18 | | Wait | Wait | C4 pays $18.00 |

Paid satisfactions: 60, 75 (VIP), 52, 28 (Critic), so the average is 53.8. Paid count (4) and
revenue ($58.50) pass, but average satisfaction misses 60, so it's a DEFEAT. In Stage 1 the same
slow start gave 58.0; the Critic is what pulls it down to 53.8.

### Stage 2 invalid inputs (`tests/stage2_invalid_inputs.txt`)

Replays the Stage 2 victory run with 11 invalid inputs mixed in, aimed at the two new customer
types. Each one prints the reason and asks again without using up the staff member's task, so
the run still ends with exactly the victory numbers (5 paid, $73.50, 75.6). That also shows the
VIP and Critic go through the same seat, order, serve, eat and pay checks as a Regular customer:
the VIP is still billed $13.50, and the Critic still pays at 52.

| Turn | Input | Message shown |
|---|---|---|
| 4 | `vip` as the waiter action | Invalid input, Please enter a valid number only |
| 4 | Take an order from C2 (VIP, still in the queue) | Customer 2 cannot order, they are WAITING |
| 4 | Seat C2 at table `99` | Please enter a number between 1 and 2 |
| 5 | Serve order 2 (the VIP hasn't ordered yet) | There is no order for C2. |
| 5 | Chef cooks order 9 | There is no order for C9. |
| 6 | Chef cooks order 2 (the VIP's Burger was just served) | Order 2 cannot be cooked, it is SERVED |
| 10 | Seat C4 (Critic) at T1, which C3 is using | Table 1 is occupied |
| 11 | Take an order from C4 (Critic, still in the queue) | Customer 4 cannot order, they are WAITING |
| 12 | Serve order 4 (the Critic hasn't ordered yet) | There is no order for C4. |
| 13 | Serve order 4 (Pasta at 1/2) | Order 4 is not READY yet, it is IN_PREPARATION |
| 14 | Chef cooks order 4 (already served) | Order 4 cannot be cooked, it is SERVED |

## Stage 3 Test Runs

Stage 3 runs use the default command (`java -cp out control.Main`). Every turn now starts with
the Happy Hour question (only until it has been used) and the Host prompt, before the Waiter and
the Chef. The menu has 5 items: Salad, Burger, Pasta, Salad + Pasta ($24.30, 3 units) and
Burger + Salad ($21.60, 2 units). With the same food rule, C4 (Critic, most expensive item) now
orders Salad + Pasta and C5 orders Burger + Salad.

| Script | Result | Paid | Revenue | Avg satisfaction |
|---|---|---|---|---|
| `tests/stage3_victory.txt` | VICTORY | 5 | $81.54 | 84.6 |
| `tests/stage3_defeat.txt` | DEFEAT | 3 (fails the 4 target) | $40.50 (fails the $45 target) | 65.0 |
| `tests/stage3_invalid_inputs.txt` | VICTORY | 5 | $81.54 | 84.6 |
| `tests/stage3_slow_start_with_host.txt` | VICTORY | 4 | $64.80 | 72.3 |
| `tests/stage3_abandonment.txt` | DEFEAT | 0 | $0.00 | N/A |

### Stage 3 victory run (`tests/stage3_victory.txt`)

Uses all three Stage 3 features. The Host seats each new arrival on the turn they arrive, so the
Waiter can take their order in the same turn.

| Turn | Arrival | Happy Hour | Host | Waiter | Chef | What happens |
|---|---|---|---|---|---|---|
| 1 | C1 Regular | No | Seat C1 at T1 | Take C1's order (Salad) | Cook C1 | Host + Waiter in one turn; Salad READY |
| 2 | | No | Wait | Serve C1 | Wait | C1 eats (92) |
| 3 | | No | Wait | Wait | Wait | C1 ready to pay |
| 4 | **C2 VIP** | No | Seat C2 at T2 | Take C2's order (Burger, $13.50) | Cook C2 | C1 pays $9.00 |
| 5 | | No | Wait | Serve C2 | Wait | C2 eats (95) |
| 6 | | No | Wait | Wait | Wait | C2 ready to pay |
| 7 | C3 Regular | No | Seat C3 at T1 | Take C3's order (Pasta) | Cook C3 (1/2) | C2 pays $13.50 |
| 8 | | No | Wait | Wait | Cook C3 (2/2) | Pasta READY |
| 9 | | No | Wait | Serve C3 | Wait | C3 eats (84) |
| 10 | **C4 Critic** | **Yes** | Seat C4 at T2 | Take C4's order (**Salad + Pasta**, $24.30 → **$19.44**) | Cook C4 (1/3) | Happy Hour turn 1 |
| 11 | | (on) | Wait | Wait | Cook C4 (2/3) | Happy Hour turn 2: C4 recovers to 100; C3 pays $18.00 |
| 12 | | | Wait | Wait | Cook C4 (3/3) | Combo READY |
| 13 | C5 Regular | | Seat C5 at T1 | Serve C4 | Wait | C4 eats (76). The Waiter can only do one thing, so C5's order waits |
| 14 | | | Wait | Take C5's order (Burger + Salad, $21.60) | Cook C5 (1/2) | C4 ready to pay |
| 15 | | | Wait | Wait | Cook C5 (2/2) | Combo READY; C4 pays $19.44 |
| 16 | C6 Regular | | Seat C6 at T2 | Serve C5 | Wait | C5 eats (76) |
| 17 | | | Wait | Wait | Wait | C5 ready to pay |
| 18 | | | Wait | Wait | Wait | C5 pays $21.60 |

Paid satisfactions: 92, 95 (VIP), 84, 76 (Critic), 76, so the average is 84.6. Revenue is
$9.00 + $13.50 + $18.00 + $19.44 + $21.60 = $81.54. All three targets pass.

Things this run shows:
- **Combo pricing:** Salad + Pasta is 90% of $9.00 + $18.00 = $24.30, and takes 1 + 2 = 3 units.
- **Happy Hour on a combo:** $24.30 × 0.8 = $19.44, rounded to the nearest cent.
- **Happy Hour recovery:** on its second turn the Critic is WAITING_FOR_FOOD at 88 and recovers
  to 100. C3 (SERVED) gets nothing, because only customers still waiting for a table or food recover.
- **Critic and combos:** the Critic picks Salad + Pasta by himself, because it is now the most
  expensive item. `CriticCustomer` was not changed.

### Stage 3 defeat run (`tests/stage3_defeat.txt`)

Same slow start as the Stage 2 defeat (the Waiter waits on turns 1 to 3), plus two bad Stage 3
choices: Happy Hour is used on turn 1, when only C1 is here and nobody is ordering, and the Host
is never used, so the Waiter has to do all the seating as well.

| Turn | Arrival | Happy Hour | Host | Waiter | Chef | What happens |
|---|---|---|---|---|---|---|
| 1 | C1 Regular | **Yes** (wasted) | Wait | Wait | Wait | Nobody to discount |
| 2 | | (on) | Wait | Wait | Wait | C1 recovers 92 → 100, then loses it again |
| 3 | | | Wait | Wait | Wait | |
| 4 | **C2 VIP** | | Wait | Seat C1 at T1 | Wait | C2 waits in the queue |
| 5 | | | Wait | Take C1's order (Salad) | Cook C1 | Salad READY (no discount, Happy Hour is over) |
| 6 | | | Wait | Serve C1 | Wait | C1 eats (68) |
| 7 | C3 Regular | | Wait | Seat C2 at T2 | Wait | C1 ready to pay |
| 8 | | | Wait | Take C2's order (Burger, $13.50) | Cook C2 | Burger READY; C1 pays $9.00 |
| 9 | | | Wait | Serve C2 | Wait | C2 eats (75) |
| 10 | **C4 Critic** | | Wait | Seat C3 at T1 | Wait | C2 ready to pay |
| 11 | | | Wait | Take C3's order (Pasta) | Cook C3 (1/2) | C2 pays $13.50 |
| 12 | | | Wait | Seat C4 at T2 | Cook C3 (2/2) | Pasta READY |
| 13 | C5 Regular | | Wait | Serve C3 | Wait | C3 eats (52) |
| 14 | | | Wait | Take C4's order (Salad + Pasta, $24.30) | Cook C4 (1/3) | C3 ready to pay |
| 15 | | | Wait | Wait | Cook C4 (2/3) | C3 pays $18.00 |
| 16 | C6 Regular | | Wait | Seat C5 at T1 | Cook C4 (3/3) | Combo READY |
| 17 | | | Wait | Serve C4 | Wait | C4 eats (16) |
| 18 | | | Wait | Take C5's order | Cook C5 (1/2) | C4 ready to pay, but the game ends |

Only 3 customers pay (68, 75 (VIP) and 52, so the average is 65.0), and revenue is $40.50. Both
the paid target and the revenue target fail, so it's a DEFEAT.

**Which choice caused it:** in the Stage 2 defeat, the same slow start still got 4 customers to
pay. Here the Critic orders the 3-unit combo, so his food is only ready on turn 16 and he can't
pay before the game ends. The Host is the fix: `tests/stage3_slow_start_with_host.txt` keeps the
same wasted turns 1 to 3 and the same wasted Happy Hour, but lets the Host do the seating. The
Waiter gets those turns back, the Critic pays on turn 16, and the run becomes a VICTORY (4 paid,
$64.80, 72.3). The only difference between the two runs is whether the manager uses the Host.

### Stage 3 abandonment (`tests/stage3_abandonment.txt`)

Turns the hand-checked abandonment case into a script. On turn 1 the Host seats C1 at Table 1
and the Waiter takes C1's Salad order; after that every staff member waits.

| Turn | What happens |
|---|---|
| 1 | C1 seated, orders Salad (IN_PREPARATION, 0/1). C1 ends the turn at 92 |
| 2-12 | Nobody acts. C1 loses 8 a turn while WAITING_FOR_FOOD; C2 to C4 wait in the queue |
| 13 | C1 reaches 0: "C1 got fed up and left!" The order is CANCELLED, Table 1 is freed, 1 departure |
| 14 | The Chef tries order 1 and is rejected ("Order 1 cannot be cooked, it is CANCELLED"), then waits. The Chef's task is not used up |
| 18 | C4 (Critic, loses 12 a turn since turn 10) reaches 0 and leaves. 2 departures |

Result: DEFEAT with all three targets failing (0 paid, $0.00, average N/A). Revenue never
includes the abandoned order.

### Stage 3 invalid inputs (`tests/stage3_invalid_inputs.txt`)

Replays the Stage 3 victory run with 8 invalid inputs mixed in, aimed at the two new prompts.
Each one prints the reason and asks again without using up the Host's (or Waiter's) task, so the
run still ends with exactly the victory numbers (5 paid, $81.54, 84.6).

| Turn | Input | Message shown |
|---|---|---|
| 1 | `abc` at the Happy Hour prompt | Invalid input, Please enter a valid number only |
| 1 | `2` at the Happy Hour prompt | Please enter a number between 0 and 1 |
| 4 | Host seats C1 (already seated, ready to pay) | Customer 1 is not in the queue |
| 4 | Host seats C2 at T1, which C1 is using | Table 1 is occupied |
| 4 | Host seats C9 (doesn't exist) | There is no customer C9. |
| 4 | Waiter tries to seat C2, whom the Host just seated | Customer 2 is not in the queue |
| 7 | Host seats C3 at table `5` | Please enter a number between 1 and 2 |
| 10 | `yes` at the Happy Hour prompt | Invalid input, Please enter a valid number only |

After the Host has seated C2 on turn 4, the Waiter's rejected seat doesn't use up his task, and
he still takes C2's order in the same turn.

### Regression: what still works

Each Stage 1 and Stage 2 script was turned into a Stage 3 version by answering `0` (no Happy
Hour) and `0` (Host waits) at the start of every turn. With the new features unused, every
earlier result is exactly the same on the Stage 3 build.

| Script | Command | Expected (stage-1 / stage-2 tag) | Stage 3 build |
|---|---|---|---|
| `tests/stage1_victory_on_stage3.txt` | `control.Main regular` | VICTORY, $66.00, 79.2 | same |
| `tests/stage1_defeat_on_stage3.txt` | `control.Main regular` | DEFEAT, $51.00, 58.0 | same |
| `tests/stage1_invalid_inputs_on_stage3.txt` | `control.Main regular` | VICTORY, $66.00, 79.2 | same |
| `tests/stage2_victory_on_stage3.txt` | `control.Main stage2` | VICTORY, $73.50, 75.6 | same |
| `tests/stage2_defeat_on_stage3.txt` | `control.Main stage2` | DEFEAT, $58.50, 53.8 | same |
| `tests/stage2_invalid_inputs_on_stage3.txt` | `control.Main stage2` | VICTORY, $73.50, 75.6 | same |
