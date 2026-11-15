# Chat 03: Stage 3, Happy Hour, Pricing Strategy and Demo Preparation

- **Tool:** Claude (Claude Code extension in VS Code)
- **Dates:** 6–15 Nov 2026 (SGT)
- **Stage:** 3

A selection of the chat, not the full transcript. My requests are written in my own words, keeping what I asked each time; short follow-up messages are merged into the request they belong to. Claude's replies are summarised in my own words.

---

## 1. Verifying Stages 1 and 2 before implementing Happy Hour (6 Nov)

**My request:** Before starting Stage 3, pull the latest commits and verify that all Stage 1 and Stage 2 functionality still works. Using the attached Stage 3 plan, I wanted to work on my assigned components only: `PricingPolicy`, `StandardPricing`, `HappyHour`, the pricing changes in `Waiter.takeOrder`, `Customer.recoverSatisfaction`, and the corresponding SOLID documentation and design-log updates.

I wanted to understand how the new pricing mechanism should integrate with the existing customer and waiter implementations, particularly how different pricing policies could be applied without tightly coupling the classes. I also asked for the changes to be organised into small conventional commits and for me to review what would be pushed before anything was published.

**Claude:** Checked the existing implementation before proceeding:

- All six Stage 1 and Stage 2 test scripts still produced their expected results.
- Stage 1 results remained 79.2 / 58.0 / 79.2.
- Stage 2 results remained 75.6 / 53.8 / 75.6.
- The `stage-2` tag pointed to the current `main`.
- Abhishek's combo-meal branch and Aaryan's Host branch did not modify my assigned files, so there were no overlapping changes to resolve.

I then worked on the Stage 3 implementation, covering the following components:

- **`PricingPolicy`:** Implemented a one-method interface, `applyTo(billCents)`, to define how a pricing policy modifies a customer's bill.
- **`StandardPricing`:** Implemented the normal pricing strategy, which leaves the bill unchanged.
- **`HappyHour`:** Implemented the discounted pricing strategy, applying a 20% reduction and rounding to the nearest cent using integer arithmetic: `(cents * 80 + 50) / 100`. It can only be activated once per game and remains active for two turns. Its `used` and `turnsLeft` state is private and can only be modified through `activate()` and `endTurn()`.
- **`Customer.recoverSatisfaction(points)`:** Implemented satisfaction recovery, allowing eligible customers who are still waiting for a table or food to regain 15 points, up to a maximum of 100. The method is `final`, consistent with the other customer flow methods.
- **`Waiter.takeOrder`:** Modified the method to accept a `PricingPolicy`, allowing the applicable pricing strategy to be supplied rather than hardcoded into the Waiter.

A particularly important implementation decision was the order in which discounts are applied:

`pricing.applyTo(c.calculateBill(price))`

This first calculates the customer's bill, including any customer-specific discount, and then applies the current pricing policy.

For example, a VIP customer ordering a $15.00 Burger during Happy Hour receives:

$15.00 × 0.9 × 0.8 = $10.80

This matches the example in the Stage 3 brief.

**Claude's additional feedback:** During the implementation review, Claude highlighted two small design choices for me to consider:

- `recoverSatisfaction()` returns `true` only when satisfaction actually increases. This prevents a customer already at 100 satisfaction from incorrectly displaying a recovery message.
- The previous two-argument `takeOrder` method was temporarily retained as a one-line wrapper. This allowed the existing `main` implementation to continue compiling until Aaryan's controller integration was ready to pass in the current pricing policy.

I reviewed these choices before proceeding.

For the documentation, I co-wrote the SOLID report and design log updates with Claude, covering:

- The new pricing-related classes and their responsibilities.
- Happy Hour as the clearest example of Dependency Inversion in the project.
- Where the Open-Closed Principle was successfully maintained and where there were limitations.
- Three design trade-offs associated with the implementation.

## 2. Branching, pull request and merge (6 Nov)

**My request:** Once the implementation was ready, push it as a separate feature branch and create a pull request. I wanted to merge the feature into `main` through a pull request rather than pushing the changes directly, so that the entire Happy Hour implementation could be reviewed and reverted as one unit if necessary.

**Claude:** Pushed the implementation to `feat/happy-hour`, opened pull request #6 under my account and merged it into `main`.

After the merge, it reran all six Stage 1 and Stage 2 scripts. Their results remained unchanged, confirming that the new pricing functionality had not broken the previous stages.

Claude also highlighted two integration requirements for Aaryan's controller:

- Update the controller to call `takeOrder` with the current pricing policy, then remove the temporary two-argument wrapper.
- Only print the satisfaction recovery message when `recoverSatisfaction()` returns `true`.

**What I took away:** This stage gave me more experience managing a feature through a separate Git branch and pull request. I also understood why temporary compatibility measures can be useful when multiple teammates are implementing different parts of the same system. They allow individual features to be integrated without unnecessarily breaking existing functionality while waiting for the remaining components.

## 3. Preparing my Stage 3 demonstration clips (14–15 Nov)

**My request:** I prepared a script for two demonstration clips: the first covering the Stage 3 features (Happy Hour, Host and combo meals), and the second explaining the SOLID principles and design trade-offs in our implementation.

I asked Claude to help me prepare the demonstration by opening the relevant source files in the order I intended to present them and highlighting the corresponding code lines for each part of my script. I wanted the highlighted sections to match what I would actually be explaining, rather than having to search through the code while recording.

**Claude:** First generated `victory_run.txt` from `tests/stage3_victory.txt` and confirmed the expected outcome:

- Victory
- 5 paid customers
- $81.54 revenue
- 84.6 average satisfaction

It then checked every line reference in my demonstration script against the actual source files.

Two line ranges needed correction:

- **`ComboMeal.java`:** The constructor's `super(...)` call extended to line 13 rather than line 11.
- **`Customer.java`:** The five `final` flow methods extended to line 224 rather than line 210. The original range cut off `markPaid`, which was part of the payment flow I intended to explain.

Claude corrected the highlighted ranges and opened the files in the order specified in my demonstration script. No source code was modified during this preparation.

**What I took away:** Preparing a technical demonstration requires more than having a working implementation. I need to be able to navigate the source code, explain how the different components interact and ensure that the code shown actually supports the points being presented. Checking the line references beforehand also prevents important parts of an implementation from being excluded from the demonstration.

**What I took from this chat**

Interfaces and Dependency Inversion: An interface is useful when it represents a genuine abstraction with multiple implementations. StandardPricing and HappyHour provide two concrete pricing strategies, allowing the Waiter to depend on PricingPolicy rather than a specific pricing implementation.

Strategy Pattern: The pricing logic can be changed by supplying a different policy, instead of adding more conditional pricing logic directly inside Waiter.

Order of operations: Applying the customer-specific discount before the Happy Hour discount is important because the order affects the final bill. It is a deliberate design decision that must match the requirements.

Encapsulation and state management: Happy Hour's activation state and remaining duration are kept private, with changes controlled through dedicated methods. This prevents unrelated classes from modifying its internal state directly.

Integer arithmetic: Monetary calculations should avoid floating-point inaccuracies. Applying the percentage reduction in integer cents with an explicit rounding approach helps maintain consistent billing.

Backward compatibility and integration: Retaining a temporary method wrapper allowed the feature to be merged without immediately breaking existing code while another teammate completed the controller integration.

Version control: Using a feature branch and pull request made the Happy Hour implementation easier to review and manage as a single feature.

Technical communication: Preparing the demo helped me connect the actual source code to the design principles explained in our documentation. I also learned to verify code references before recording rather than relying on outdated line numbers.

Overall: Stage 3 brought together several concepts from the earlier stages. While Stage 1 focused on implementing the core game classes and Stage 2 extended the customer hierarchy, Stage 3 introduced interchangeable pricing strategies, controlled game states and more deliberate integration between components. I gained a better understanding of how interfaces and design patterns can make a system easier to extend without modifying existing functionality unnecessarily. I also developed more experience with feature branching, pull requests, integration checks and explaining implementation decisions through a technical demonstration.