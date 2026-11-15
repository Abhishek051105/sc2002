# Chat 02: Stage 2, Critic Customer and Design Documentation

- **Tool:** Claude (Claude Code extension in VS Code)
- **Dates:** 1–4 Nov 2026 (SGT)
- **Stage:** 2

A selection of the chat, not the full transcript. My requests are written up in my own words, keeping what I asked each time; short follow-up messages are merged into the request they belong to. Claude's replies are summarised in my own words.

---

## 1. Verifying dependencies and implementing CriticCustomer (1 Nov)

**My request:** Using the attached Stage 2 plan, first verify Raghav's implementation (`getTypeLabel()` and `VIPCustomer`), since my assigned `CriticCustomer` class depends on the existing customer structure. After understanding how the existing classes worked, I wanted to implement `CriticCustomer` and update the SOLID document and design log for Stage 2. I also wanted to ensure that the new implementation did not break any of the Stage 1 functionality and that changes were organised into small conventional commits.

**Claude:** Reviewed Raghav's implementation and confirmed that:

- `getTypeLabel()` was correctly declared as an abstract method in `Customer`.
- `VIPCustomer` correctly implemented the method, lost 5 satisfaction points per turn and received a 10% discount through `super.calculateBill()`.
- All three existing Stage 1 scripts still produced their expected results: 79.2 for victory, 58.0 for defeat and 79.2 for invalid input.

It also identified two minor style issues in Raghav's implementation: a misplaced Javadoc comment and inconsistent indentation.

I co-wrote `CriticCustomer` with Claude, working through its required behaviour:

- **Satisfaction:** Loses 12 satisfaction points per turn.
- **Menu selection:** Automatically chooses the most expensive dish available.
- **Billing:** Pays full price without receiving a discount.
- **Type label:** Returns `"Critic"`.

I also needed to ensure that the new subclass behaved consistently with the existing `Customer` abstraction and did not introduce unnecessary changes to the parent class or other subclasses.

Since the Stage 2 arrival changes had not yet been merged, the new behaviour was tested using a temporary local copy with the planned arrival list. This allowed the new class to be evaluated against the intended Stage 2 scenario without interfering with the team's ongoing integration work.

The Stage 2 victory run produced exactly the expected results from the plan:

- 5 paid customers
- $73.50 revenue
- 75.6 average satisfaction

The Critic also selected Pasta instead of Salad, confirming that the most-expensive-dish selection behaviour was working as intended.

For the documentation, I co-wrote the SOLID section updates with Claude, covering:

- **Open-Closed Principle (OCP):** Added a table showing how unchanged call sites invoke the appropriate overridden subclass methods without requiring modifications to the existing customer-handling logic.
- **Liskov Substitution Principle (LSP):** Documented the contracts that each customer subclass must preserve when substituting for the parent `Customer` class.
- **ArrivalSchedule:** Explained why the single creation `switch` is acceptable and does not undermine the overall design.
- **Rejected design:** Documented why an alternative Critic implementation based on leaving a bad review was rejected.

After reviewing the changes, I chose to push my work directly to `main`, following the team's established workflow.

## 2. Addressing team review feedback (4 Nov)

**My request:** A teammate had reviewed the Stage 2 implementation and raised several points for improvement. I wanted to go through the feedback and address the issues relevant to my assigned work only. One particularly important point was that `CriticCustomer.chooseMenuItem()` should preserve the parent's null/empty-menu validation rather than bypassing it, so that the subclass remained consistent with the Liskov Substitution Principle.

**Claude:** Checked the current repository before making changes and found that the relevant null/empty-menu check had already been committed by a teammate earlier that morning. Since the correction was already present, it left the code unchanged.

Instead, it updated the LSP section of my SOLID documentation to reflect the corrected implementation and listed the remaining review points, which belonged to other team members.

**What I took away:** I learned that code reviews are not just about identifying implementation errors but also about verifying whether the design principles documented in our report are actually upheld by the code.

In particular, overriding a parent method does not mean a subclass can freely discard the validation and behavioural guarantees established by the parent. I also learned to check the latest shared repository before making corrections, since another teammate may already have addressed the issue.

---

## What I took from this chat

- Verify dependencies before implementing new functionality. Since `CriticCustomer` depends on the existing `Customer` abstraction, I needed to understand Raghav's implementation before building on it.
- Re-run existing test scripts after introducing new functionality to ensure that Stage 2 changes do not break Stage 1 behaviour.
- Understand the difference between extending existing functionality through inheritance and modifying the parent class unnecessarily.
- The Open-Closed Principle is particularly relevant to subclassing: new customer types should be introduced through extensions and overrides rather than requiring changes throughout existing call sites.
- The Liskov Substitution Principle is not just something to describe in documentation. Subclasses must preserve the behavioural expectations of their parent classes, including important validation checks.
- Review feedback should be evaluated against the actual code and the relevant design principles, rather than applied blindly.
- Check the latest commits before modifying shared code, especially when multiple teammates are working on the same deliverable.

**Overall:** Stage 2 built on the object-oriented implementation work from Stage 1 by introducing another customer type and applying inheritance and polymorphism more deliberately. I became more conscious of how new subclasses should fit into existing abstractions without breaking their established contracts. I also gained a better understanding of how to connect the principles documented in a SOLID report to actual implementation decisions. Going into Stage 3, I want to continue writing and reasoning through the implementation myself, using Claude primarily to clarify concepts, review my decisions and help investigate issues I encounter.