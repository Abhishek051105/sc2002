# Chat 01: Stage 2, Critic and docs

- **Tool:** Claude (Claude Code extension in VS Code)
- **Dates:** 1–4 Nov 2026 (SGT)
- **Stage:** 2

A selection of the chat, not the full transcript. My prompts are **paraphrased** into
clearer wording, keeping what I asked each time. Claude's replies are summarised in my own words.

---

## 1. Verifying the dependency, then implementing the Critic (1 Nov)

**My request (paraphrased):** Using the attached Stage 2 plan, first verify Raghav's part
(`getTypeLabel()` and `VIPCustomer`), since my class depends on it. Then implement my part,
`CriticCustomer`, and update the SOLID doc and design log for Stage 2, as small conventional
commits.

**Claude:** Checked Raghav's part:
- It compiles, `getTypeLabel()` is abstract in `Customer`, and VIP loses 5 per turn and gets
  10% off through `super.calculateBill()`.
- The three Stage 1 scripts still give 79.2 / 58.0 / 79.2.
- Two style issues: a misplaced javadoc and inconsistent indentation.

Then it wrote `CriticCustomer` (loses 12 per turn, always orders the most expensive dish, full
price, label "Critic"). The arrival changes weren't merged yet, so it tested on a throwaway local
copy with the planned arrival list. The Stage 2 victory run gave exactly the plan's
5 paid / $73.50 / 75.6, with the Critic ordering Pasta instead of Salad.

The SOLID doc gained:
- an OCP table showing which subclass method each unchanged call site reaches
- the LSP contract each subclass keeps
- why the one creation `switch` in `ArrivalSchedule` is acceptable
- the rejected "bad review" Critic design

I chose to push to `main`, following the team's workflow.

## 2. Acting on team review feedback (4 Nov)

**My request (paraphrased):** A teammate reviewed Stage 2 and raised a list of issues. Address
the ones in my part only, including the point that `CriticCustomer.chooseMenuItem()` should keep
the parent's null/empty-menu check for LSP.

**Claude:** Found the check had already been committed by a teammate that morning, so it left the
code alone. It updated the LSP section of my SOLID doc to match, and listed the remaining items,
which belong to other members.

---

## What I took from this chat

- Verify the part mine depends on before building on it, and re-run the old scripts to show
  nothing broke.
- An override should keep the parent's checks, or the LSP claim in our report isn't true. A
  teammate's review caught that.
- Check what's already pushed before fixing something; it may already be done.
