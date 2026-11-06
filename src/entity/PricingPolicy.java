package entity;

/** A pricing rule applied to the bill when an order is taken. */
public interface PricingPolicy {
    /** Returns the bill after this rule, in cents. */
    int applyTo(int billCents);
}
