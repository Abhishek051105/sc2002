# Chat 01: Stage 2, arrivals, README and review

- **Tool:** Claude (claude.ai, Claude Code session in the cloud, linked to my Mac's clone of the repo)
- **Dates:** Sun 1 Nov 2026 11:08 to Wed 4 Nov 2026 14:30 (SGT)
- **Stage:** 2

A selection of the chat, not the full transcript. My prompts are copied word for word (typos
included); Claude's replies are summarised. Claude could read and compile the files in my local
clone, but I ran every git command myself, and every commit is authored by me only.

---

## 1. Me (1 Nov, 11:08), described, with the Stage 2 plan PDF attached

I gave Claude the project setup (repo, layout, team split, Stage 1 results, deadlines) and my
Stage 2 tasks from the plan: the `CustomerType` enum, `GameConfig` arrival lists,
`ArrivalSchedule`'s creation switch, the `regular` argument in `Main`, the README
stage-1 → stage-2 section, and reviewing Ayush's ConsoleUI PR. I asked it to review code I type
rather than write it, teach the exam concepts the code touches, and use confidence tags.

**Claude:** Checked where `ARRIVAL_TURNS` was used (only `GameConfig`, `ArrivalSchedule` and
`Main`, so `GameController` stays untouched). It noticed my local `main` was missing Raghav's and
Aekas's classes, so I had to pull before branching. It also listed the traps ahead: keeping
`nextCustomerId` at 1, guarding `args` before reading it, and `Integer == int` versus
`Integer == Integer`.

## 2. Me (1 Nov, 11:09), pasting my terminal

I ran the git commands from my home folder and got `fatal: not a git repository`.

**Claude:** Git looks for `.git` in the current folder, so I needed to `cd` into the repo first.
After I did, the pull brought in the VIP and Critic classes. Claude compiled the fresh `main` and
re-ran the Stage 1 scripts (79.2 / 58.0) as a baseline before I changed anything.

## 3. Me (1 Nov, 11:13–11:19)

> wait give me step by step instructions an teach me in the process asw

> wait no i'm lost sorry according to the pdf what is my task n stuff

> yea jsut liek we were doing, you explain what it is doing and build a skeleton for me. i replace the todo poriton with my code

**Claude:** Summarised my six tasks as a table and the order they depend on each other
(enum → config → `ArrivalSchedule` → `Main`). From then on it put a skeleton file with `TODO`
comments in my repo for each step and explained the idea before I typed.

## 4. Me (1 Nov, 11:25–11:27), on `CustomerType`

> can you hwo and what to do for customer type

> done

**Claude:** Explained the file line by line. An enum is a closed list of values, so a typo
like `CustomerType.VPI` is a compile error where a String `"VPI"` would only fail at runtime, and
nobody can call `new CustomerType()`. My file compiled. It then gave me a `GameConfig` skeleton
and explained why `Set` becomes `List` (a set has no positions, but turn *i* has to line up with
type *i*).

## 5. Me (1 Nov, 11:36), answering its question about `final` and `List.of`

> final so it's immutable you can't change it. List.of noit sure

**Claude:** Corrected me: `final` stops the variable being pointed at a different list (a
compile error), while `List.of` makes the list's contents unchangeable (an exception at runtime).
`final` on its own is not immutability.

## 6. Me (1 Nov, 11:37–11:39)

> done check

> let's move on. how many total taska btw

**Claude:** Found my bug: I had declared `ARRIVAL_TYPES` twice instead of naming the second list
`ALL_REGULAR`. My order of types was right (VIP at index 1, Critic at index 3). The other compile
error, in `Main`, was expected until `ArrivalSchedule` changed. It also pointed out I had deleted
the existing class comment on `GameConfig`. Then it gave the `ArrivalSchedule` skeleton and
explained why `createCustomer()` returns `Customer` and why one creation switch is allowed
under OCP.

## 7. Me (1 Nov, 16:17–16:20)

> okay can you check please

> let's stop here for now. gimme a nice mesage to send to my grpmates to update them

> wait what do i do when i open the repo on github

**Claude:** Confirmed my commit and push (author only me, no co-author line, exactly four files,
no untouched class in the diff), drafted the group message, and walked me through opening the PR.

## 8. Me (2 Nov, 15:34), pasting Ayush's messages

> Ayush:
> hi ive done my part and sent pull req to @aaryan_10_0
>
> Ayush:
> maybe just merge the pull requests no need to review

**Claude:** Advised against merging unreviewed and checked both PRs instead. Mine was already
merged. Ayush's ConsoleUI change had been pushed straight to `main` and looked fine (type labels,
bill, no `instanceof`), but his `stage2_defeat.txt` broke at turn 15 and ran out of input, and
he had added an unused constructor with a hardcoded 18. It drafted the review and a message to him.

## 9. Me (2 Nov, 15:37–15:46)

> wait but realistically is my part done>

> can you do the readme part right now

> no so tell me step by step first what to do with ayush then the readme part

**Claude:** Said my code was done but the review and README weren't. It wrote the README Stage 2
rules section and the stage-1 → stage-2 section (new files, changed files with reasons, the
unchanged list, what still works), checking each claim against the code on `main`. I committed it
on my own branch and opened a PR. From my screenshot it noticed my review had gone in as a plain
comment rather than "Request changes".

## 10. Me (4 Nov, 14:27–14:30)

> okay now check wha'ts up there have been changes

> okay can you generate the AI history ofc keeping in mind the guidelines and requirements plsu we haven;t done anyhting illegal or prohibited i;m sure

**Claude:** Confirmed that Ayush had fixed both review points and everything was merged. On
`main` all five scripts give the expected results (Stage 2: 73.50 / 75.6 and 58.50 / 53.8;
Stage 1 in regular mode: 79.2 / 58.0 / 79.2), and none of the twelve untouched classes changed
since `stage-1`. Still open: the `stage-2` tag and my AI history. It then drafted these files in
the same format as Abhishek's and Aekas's, which I checked before committing.

---

## What I took from this chat

- Pull before branching. "Up to date with origin/main" only means up to date with the last fetch.
- `final` is not immutability: `final` fixes the variable, `List.of` fixes the contents.
- Calling a method that returns something doesn't store the result. My loop built each customer
  and then lost it.
- Guard before access: `args.length > 0 && args[0].equals(...)`, in that order.
- Only commit my own files. Stripping comments from `GameController` would have broken our
  "untouched" claim.
- Review even when a teammate says not to. The defeat script would have gone into the tag broken.
