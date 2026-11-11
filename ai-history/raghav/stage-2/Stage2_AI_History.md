
# AI Interaction History: SC2002 Restaurant Simulation - Stage 2

- **Repository:** `sc2002`
- **Contributor:** Raghav
- **Stage:** 2
- **Tools Used:** Microsoft Copilot
- **Overview:** Documents AI-assisted discussions related to Stage 2 customer-type extensions, Open-Closed Principle compliance, UML updates, implementation planning, and Git workflow management.

---

# Chat 01: Stage 2 Customer Extension Design & OOP Review

- **Tool:** Microsoft Copilot
- **Date:** 31 Oct 2026 (SGT)
- **Scope:** Understanding Stage 2 requirements, identifying assigned responsibilities, implementing VIP customer support, updating UML diagrams, and ensuring Open-Closed Principle compliance.

---

## 1. Understanding My Stage 2 Responsibilities

### Me (31 Oct)

> This file contains the assignment details and other details. We have completed Stage 1. Now we are moving to Stage 2. Go through the document in detail. I also need to provide AI chat evidence and we must exactly complete it as per the instructions. The other document is the Stage 2 Plan. I am Raghav. Tell me what I have to do and how I must do it.

### Copilot

Reviewed the Stage 2 assignment requirements together with the team's implementation plan and identified the work specifically assigned to me.

The discussion focused on four main responsibilities:

- Customer type abstraction support
- VIP customer implementation
- Stage 2 UML updates
- AI interaction documentation

The conversation clarified that the primary purpose of Stage 2 was not simply adding new classes, but demonstrating object-oriented design principles. In particular, Copilot explained that the assignment expected new customer behaviour to be introduced through inheritance and polymorphism rather than modifying existing controller logic.

This helped me understand that Stage 2 is largely a design-extension exercise intended to demonstrate maintainability and extensibility.

---

## 2. Determining the Required Code Changes

### Me (31 Oct)

> Tell me exactly what code changes I should do.

### Copilot

Analyzed the planned Stage 2 architecture and proposed introducing a common abstraction for customer-type identification.

The recommendation was to add an abstract method to the `Customer` class:

```java
public abstract String getTypeLabel();
```

and implement it in `RegularCustomer`:

```java
@Override
public String getTypeLabel() {
    return "Regular";
}
```

A significant portion of the discussion focused on why this approach is preferable to repeatedly checking customer types using conditional logic or multiple `instanceof` checks.

The explanation reinforced how polymorphism allows future customer types to be added with minimal impact on existing classes.

---

## 3. Designing the VIPCustomer Extension

### Me (31 Oct)

> How should I implement VIPCustomer?

### Copilot

Walked through the behavioural differences between a regular customer and a VIP customer based on the Stage 2 specification.

The discussion identified three key changes:

- VIP customers lose satisfaction more slowly.
- VIP customers receive a 10% discount on their bill.
- VIP customers continue to follow the normal order and service workflow.

Rather than modifying existing game logic, Copilot recommended implementing a dedicated subclass that overrides only the methods responsible for these behavioural differences.

The explanation demonstrated how inheritance can be used to extend functionality without affecting unrelated parts of the system.

---

## 4. Understanding Design Constraints

### Me (31 Oct)

> What files should I avoid touching?

### Copilot

Reviewed the Stage 2 implementation strategy and emphasized keeping modifications localized to customer-related classes.

The discussion highlighted that introducing VIP customers should not require changes to major simulation components such as:

- `GameController`
- `Waiter`
- `Chef`
- `Cashier`
- `Restaurant`
- Order-related classes

unless absolutely necessary.

The reasoning was that excessive modifications to existing classes would weaken the project's demonstration of the Open-Closed Principle.

This helped me focus on extending behaviour through inheritance rather than altering established functionality.

---

## 5. Updating the UML Diagram

### Me (31 Oct)

> What changes should be reflected in the Stage 2 class diagram?

### Copilot

Explained that the UML diagram should visually demonstrate how the design evolved from Stage 1.

The discussion included:

- `Customer` remaining an abstract superclass.
- `RegularCustomer`, `VIPCustomer`, and `CriticCustomer` being represented as subclasses.
- Inheritance relationships introduced in Stage 2.
- Polymorphic behaviour through overridden methods.
- Extension without modification of existing controller logic.

The conversation reinforced that UML diagrams are not only documentation but also evidence of applying object-oriented design principles.

---

## 6. Git Workflow and Branch Management

### Me (31 Oct)

> Tell me how I should pull and push it to GitHub.

### Copilot

Provided a recommended workflow for contributing Stage 2 changes safely.

The discussion covered:

1. Pulling the latest updates from `main`.
2. Creating a dedicated feature branch.
3. Implementing customer-related changes.
4. Committing related changes with meaningful commit messages.
5. Pushing the branch to GitHub.
6. Creating a pull request before merging.

The conversation also explained the importance of regularly syncing with the latest version of `main` to reduce merge conflicts and maintain a cleaner integration process.

---

# Key Takeaways & OOP Learnings

## Open-Closed Principle (OCP)

Stage 2 reinforced the idea that software should remain open for extension but closed for modification. New customer types should be introduced through subclassing and method overriding rather than repeatedly changing existing controller logic.

## Polymorphism Over Type Checking

Customer-specific behaviour can be handled through polymorphic methods instead of relying on chains of `instanceof` checks. This approach improves maintainability and scalability.

## Focused Class Responsibilities

Only the behaviour that differs between customer types should be overridden. Shared functionality should remain within the base `Customer` class to avoid duplication.

## UML as Design Evidence

The Stage 2 UML updates serve as evidence that the system was extended through inheritance and polymorphism while preserving the original architecture.

## Git Workflow Discipline

Using dedicated feature branches and pull requests helps isolate changes, simplifies review, and reduces integration issues when multiple team members are working concurrently.

---

**End of AI Interaction History - Stage 2**
