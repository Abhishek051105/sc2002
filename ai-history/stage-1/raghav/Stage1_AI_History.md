# AI Interaction History: SC2002 Restaurant Rush – Stage 1

- **Repository:** `sc2002`
- **Contributor:** Raghav
- **Stage:** 1
- **Tools Used:** Microsoft Copilot (project discussion and design assistance)
- **Overview:** Documents how AI was used to understand customer lifecycle design, state transitions, encapsulation, satisfaction management, UML design considerations, and preparation for future Stage 2 extensions.

---

# Chat 01: Customer Design, State Management & Future Extensibility

- **Tool:** Microsoft Copilot
- **Dates:** 22 Oct 2026 – 24 Oct 2026 (SGT)
- **Scope:** Customer lifecycle design, satisfaction handling, object-oriented design principles, and Stage 2 extensibility planning.

---

## 1. Understanding My Responsibility

### Me (22 Oct)

I'm doing `Customer.java` and `RegularCustomer.java` for our project. Looking at the design document, I'm not sure what responsibilities should belong inside `Customer` and what should be handled elsewhere. Can you break it down?

### Copilot

The response first reviewed the Stage 1 design and highlighted that `Customer` was responsible for managing its own state, satisfaction level and lifecycle rather than restaurant operations.

It explained that actions such as seating, ordering, serving food and payment affect the customer, but the actual orchestration should be performed by the `Waiter`, `Cashier` and `GameController`.

The discussion focused on preventing `Customer` from becoming a "God Class". Instead of allowing many classes to directly modify customer data, `Customer` should encapsulate its own state transitions and validate them internally.

The explanation also connected this to the **Single Responsibility Principle (SRP)**, where `Customer` only needs to change when customer behaviour changes, not when restaurant operations change.

[Reference: SC2002 Restaurant Rush Stage 1 Plan](https://onedrive.live.com/?cid=3e3662d46ee50933&id=3E3662D46EE50933!s3612c459dd244863b44b871b3d4c7c94)

---

## 2. Customer Lifecycle and State Transitions

### Me (22 Oct)

The plan mentions lots of states like `WAITING` and `READY_TO_PAY`. How do I actually implement this without making a mess?

### Copilot

The response walked through the entire customer lifecycle from arrival to payment.

It explained the intended progression:

```text
WAITING
   |
   v
SEATED
   |
   v
WAITING_FOR_FOOD
   |
   v
SERVED
   |
   v
READY_TO_PAY
   |
   v
PAID
```

We discussed creating dedicated methods for each transition instead of allowing the status field to be changed directly.

Examples included:

```java
seat()
orderTaken()
serve(turn)
finishEating(turn)
markPaid(turn)
```

The response also explained that every transition should verify that the customer is currently in the correct state. Invalid transitions should throw an exception because they indicate a programming error rather than an invalid player action.

This discussion helped me understand how state machines can be implemented using object-oriented design rather than large chains of conditional statements.

---

## 3. Satisfaction Handling and Encapsulation

### Me (23 Oct)

The satisfaction part seems simple, but I don't want to over-complicate it. What's the cleanest way to do it?

### Copilot

The discussion focused on keeping the implementation simple while still following the design principles from the lectures.

Instead of exposing a public setter such as:

```java
setSatisfaction(...)
```

the response suggested keeping satisfaction private and providing one controlled method responsible for updating it.

We discussed using:

```java
Math.max(0, satisfaction - getSatisfactionDecay())
```

to ensure satisfaction can never become negative.

The response also explained why this approach improves encapsulation. Other objects don't need to know how satisfaction is calculated. They simply request the customer to update itself at the end of a turn.

This was one of the concepts that helped me connect lecture theory to an actual class implementation.

---

## 4. Designing for Stage 2 Before Stage 2 Exists

### Me (24 Oct)

Right now we're only doing `RegularCustomer`. Do I have to care about VIP and Critic already?

### Copilot

Initially I thought Stage 2 was a completely separate problem, but the response highlighted that the Stage 1 design document already expects future extensions to be considered.

The response reviewed the proposed Stage 2 requirements and pointed out that VIP and Critic customers should ideally be added as new subclasses rather than forcing modifications throughout the entire system.

We discussed adding extension hooks inside `Customer`:

```java
chooseMenuItem()

calculateBill()
```

For Stage 1 these would simply provide default behaviour, but future customer types could override them.

The conversation introduced the **Open/Closed Principle (OCP)** in a practical way. Rather than memorising a SOLID definition, I could see how designing the base class correctly now would reduce work later.

[Reference: SC2002 Restaurant Rush Stage 1 Plan](https://onedrive.live.com/?cid=3e3662d46ee50933&id=3E3662D46EE50933!s3612c459dd244863b44b871b3d4c7c94)

---

## 5. Reviewing My Existing Customer Implementation

### Me (24 Oct)

My teammates want to keep my current code and build around it. Is there anything else I should change before we start integrating?

### Copilot

The response compared my implementation against the responsibilities assigned to `Customer` in the Stage 1 plan. It concluded that my lifecycle design was largely compatible with the project architecture but recommended introducing the Stage 2 extension hooks before integration.

We discussed whether the code should be redesigned entirely to match the original document.

Instead, the recommendation was to preserve the existing lifecycle implementation:

```java
seat()
orderTaken()
serve()
finishEating()
markPaid()
```

while adding:

```java
chooseMenuItem()
calculateBill()
```

This would minimise disruption to teammates while still supporting future customer subclasses.

The discussion reinforced the idea that good design is not always about rewriting code. Sometimes the better design decision is to preserve a working implementation and add carefully chosen extension points.

---

## 6. Reviewing RegularCustomer

### Me (24 Oct)

This is my `RegularCustomer` class. Is this enough?

```java
public class RegularCustomer extends Customer {

    public RegularCustomer(int customerId, int arrivalTurn) {
        super(customerId, arrivalTurn);
    }

    @Override
    public int getSatisfactionDecay() {
        return 8;
    }
}
```

### Copilot

The response reviewed the implementation and confirmed that it correctly fulfilled the Stage 1 requirements.

We discussed why `RegularCustomer` should stay minimal and inherit most behaviour from `Customer` rather than duplicating logic.

The conversation also reinforced how inheritance works in practice. Since `Customer` already handled satisfaction, status tracking and lifecycle methods, `RegularCustomer` only needed to provide its specific decay rate.

This helped me understand how abstraction and inheritance reduce duplication while keeping behaviour consistent across subclasses.

---

# What I Took From This Chat

Throughout these discussions I learned:

- How to design and implement a customer lifecycle using object-oriented state transitions.
- Why encapsulation is important for protecting customer state and satisfaction values.
- How exceptions can be used to prevent invalid state transitions.
- How inheritance allows specialised customers to reuse common behaviour.
- How the Open/Closed Principle can be applied to real code rather than treated as a theory-only concept.
- How to prepare a Stage 1 design so that Stage 2 changes can be introduced through new subclasses instead of modifying existing controller logic.
- How my `Customer` implementation influenced the UML class diagram and broader system architecture for the project.

---

**End of AI Interaction History – Stage 1**
