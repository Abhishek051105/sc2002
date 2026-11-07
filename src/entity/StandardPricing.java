package entity;

/** Normal pricing: the bill is unchanged. */
public class StandardPricing implements PricingPolicy {
    @Override
    public int applyTo(int billCents) {
        return billCents;
    }
}
