package entity;

/**
 * A pricing rule applied to a bill when an order is taken.
 * The Waiter depends on this interface, never on a concrete rule, so
 * Happy Hour can change the price without the Waiter knowing about it.
 */
public interface PricingPolicy {
    /** Returns the bill after this rule, in cents. */
    int applyTo(int billCents);
}
