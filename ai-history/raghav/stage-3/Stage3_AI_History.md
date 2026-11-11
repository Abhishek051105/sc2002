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

I also learned that adding a new feature does not always mean creating a completely independent class. The
