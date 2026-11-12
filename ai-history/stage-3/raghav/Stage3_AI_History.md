# SC2002 Restaurant Rush – Stage 3 AI History

## Chat 01: Understanding the Stage 3 Implementation and Design

- **Tool:** ChatGPT
- **Date:** 6 November 2026 (SGT)
- **Stage:** 3

This discussion focused on understanding how Stage 3 extended our existing Stage 2 implementation. I wanted to understand the purpose of the new classes and how they fitted into the existing design, rather than treating them as isolated additions.

**Me:**

We are now working on Stage 3 of our restaurant project. I want to understand how the new features fit into our existing code. We have Host, ComboMeal and Happy Hour. Can you explain how these should work with the existing classes and what design principles are involved? Keep the explanation simple because I need to be able to explain the design myself.

**ChatGPT:**

It explained that Stage 3 was an extension of Stage 2, not a complete redesign.

The three main additions were:

- **Host:** A new staff member who can seat one waiting customer per turn.
- **ComboMeal:** A new type of menu item containing two existing menu items, with a combined preparation time and a discounted price.
- **Happy Hour:** A feature that provides a 20% discount for two turns and allows waiting customers to recover satisfaction.

It also explained the purpose of `PricingPolicy`, `StandardPricing` and `HappyHour`. Instead of making the Waiter directly depend on Happy Hour, the Waiter uses the pricing interface. This allows different pricing rules to be used without changing the Waiter's main ordering logic.

**Me:**

So why do we need an interface here? Wouldn't it be simpler to directly use the HappyHour class?

**ChatGPT:**

It explained that directly depending on Happy Hour would make the Waiter more tightly connected to one particular pricing implementation.

Using `PricingPolicy` separates the general pricing rule from its actual implementation. Both normal pricing and Happy Hour pricing can follow the same interface.

**My Reflection:**

This helped me understand the Dependency Inversion Principle in a practical way. Previously, I mostly understood design principles as definitions that had to be remembered for examinations. Seeing how `PricingPolicy` was used in our project made the idea clearer.

I also learned that adding a new feature does not always mean creating a completely independent class. The important part is understanding how it interacts with existing components and whether those interactions introduce unnecessary dependencies.

Another thing I realised was that a design decision should be explainable. It is not enough to say that we used an interface because it follows SOLID. We should be able to explain what problem the interface solves.

---

## Chat 02: Reviewing and Correcting the Stage 3 UML Class Diagram

- **Tool:** ChatGPT
- **Date:** 11 November 2026 (SGT)
- **Stage:** 3

By this point, the Stage 3 implementation had been merged, and I was working on my assigned UML class diagram. My responsibility was to ensure that the final diagram accurately represented the implementation.

**Me:**

I shared the current `GameController` class from my PlantUML diagram:

```plantuml
class GameController <<control>> #FFF2C2 {
    - currentTurn : int
    + GameController(restaurant : Restaurant, ui : ConsoleUI, schedule : ArrivalSchedule, objective : Objective)
    + run() : void
    - playTurn() : void
    - handleArrivals() : void
    - handleHappyHour() : void
    - handleHost() : void
    - handleWaiter() : void
    - handleChef() : void
    - handleEatingAndPayment() : void
    - handleEndOfTurnWaiting() : void
    - currentPricing() : PricingPolicy
    - getCustomerOrThrow(id : int) : Customer
    - getOrderOrThrow(id : int) : Order
}
```

**Me:**

This is the current GameController. Is anything missing?

**ChatGPT:**

It compared the UML representation with the Java implementation and identified two missing attributes:

- `happyHour : HappyHour`
- `standardPricing : PricingPolicy`

Although the corresponding composition relationships were already represented in the diagram, the actual attributes were missing from the `GameController` class.

**Me:**

Okay, add and give the complete GameController.

**ChatGPT:**

It provided the complete updated PlantUML class with the following attributes added:

```plantuml
class GameController <<control>> #FFF2C2 {
    - currentTurn : int
    - happyHour : HappyHour
    - standardPricing : PricingPolicy

    + GameController(restaurant : Restaurant, ui : ConsoleUI, schedule : ArrivalSchedule, objective : Objective)
    + run() : void
    - playTurn() : void
    - handleArrivals() : void
    - handleHappyHour() : void
    - handleHost() : void
    - handleWaiter() : void
    - handleChef() : void
    - handleEatingAndPayment() : void
    - handleEndOfTurnWaiting() : void
    - currentPricing() : PricingPolicy
    - getCustomerOrThrow(id : int) : Customer
    - getOrderOrThrow(id : int) : Order
}
```

It also reminded me to retain the existing composition relationships.

**My Reflection:**

This was a small correction, but it made me reconsider how carefully I had been checking my UML diagram.

Initially, I was mainly concerned about whether all the classes and methods were present. I had overlooked two attributes that were actually part of the implementation.

I realised that a UML diagram is not simply a drawing of the system's overall structure. It must accurately represent the attributes, methods, visibility and relationships found in the actual code.

I also understood that attributes and relationships communicate different information. The attributes show that `GameController` maintains references to these objects, while the composition relationships explain their ownership.

The biggest lesson for me was that documentation should be validated against the implementation. A diagram can look complete and still contain important omissions.

---

## Chat 03: Understanding Stage Changes and Preparing the Final Documentation

- **Tool:** ChatGPT
- **Date:** 11 November 2026 (SGT)
- **Stage:** 3

After working on the UML correction, I wanted to make sure I understood the remaining responsibilities assigned to me. My work was not limited to producing the final Stage 3 diagram. I also had to explain how the system evolved across all three stages.

**Me:**

I have to complete my Stage 3 documentation and contribute to the final report. Can you help me understand what I should explain about the changes between Stage 1, Stage 2 and Stage 3? I don't want to just list the new classes. I want to explain why the changes were made.

**ChatGPT:**

It explained that the change analysis should distinguish between three categories:

1. **New classes:** Classes introduced to support new functionality.
2. **Modified classes:** Existing classes changed to accommodate the new requirements.
3. **Unchanged classes:** Classes that continued to work without modification.

It also explained that each change should have a reason. For example, adding Host required changes to the restaurant's staffing and turn-management logic. Adding ComboMeal introduced a new type of menu item while allowing existing menu and order functionality to be reused.

**Me:**

How should I explain the evolution of the UML diagrams in the final report?

**ChatGPT:**

It suggested presenting the three class diagrams in chronological order and explaining the important design changes between them.

Instead of describing every class individually, I should focus on how the architecture developed, what new responsibilities were introduced and how the original design was extended.

**My Reflection:**

This made me realise that the three UML diagrams should not be treated as three unrelated deliverables. Together, they tell the story of how our software developed.

Stage 1 was about establishing the basic restaurant simulation. Stage 2 introduced additional customer behaviour and pricing requirements. Stage 3 expanded the system further through Host, ComboMeal and Happy Hour.

I also understood that an important part of software engineering is knowing what not to change. Some existing classes could continue to work because the original design was flexible enough to support the new features.

This helped me appreciate the value of inheritance, interfaces and encapsulation beyond simply using them to satisfy design requirements.

---

## Overall Reflection

Looking back at Stage 3, I think my understanding of software design changed from focusing mainly on individual classes to looking at the system as a whole.

During Stage 1, I was more concerned with understanding my assigned implementation and getting the program to work. By Stage 3, my responsibilities required me to examine the relationships between classes, compare different versions of the system and ensure that the documentation accurately represented the final implementation.

One important thing I learned was that software development does not end when the code compiles or the required features work. We also need to communicate our design decisions clearly, justify changes and make sure that the documentation remains consistent with the code.

The correction to `GameController` was a good example. It was only two missing attributes, but identifying them required me to look beyond the appearance of the diagram and compare it with the actual implementation.

More importantly, I learned to question my own work instead of assuming that something was correct simply because I had completed it. I believe this is one of the most useful lessons I will take away from this project.
