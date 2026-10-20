package entity;

import java.util.OptionalDouble;

/**
 * Aggregates game metrics across turns.
 * Revenue is tracked in integer cents.
 * Average satisfaction is returned as an OptionalDouble (empty until the first payment).
 */
public class GameStats {
    private int revenueCents;
    private int paidCount;
    private int totalPaidSatisfaction;
    private int servedCount;
    private int departureCount;
    private int turnsUsed;

    public GameStats() {
        this.revenueCents = 0;
        this.paidCount = 0;
        this.totalPaidSatisfaction = 0;
        this.servedCount = 0;
        this.departureCount = 0;
        this.turnsUsed = 0;
    }

    /**
     * Records a customer payment.
     *
     * @param billCents the collected bill in cents
     * @param satisfaction the customer's satisfaction at time of payment
     */
    public void recordPayment(int billCents, int satisfaction) {
        this.revenueCents += billCents;
        this.totalPaidSatisfaction += satisfaction;
        this.paidCount++;
    }

    /**
     * Records an order served to a customer.
     */
    public void recordServed() {
        this.servedCount++;
    }

    /**
     * Records an unhappy customer departure.
     */
    public void recordDeparture() {
        this.departureCount++;
    }

    /**
     * Records turn progression.
     */
    public void recordTurn() {
        this.turnsUsed++;
    }

    public void setTurnsUsed(int turnsUsed) {
        this.turnsUsed = turnsUsed;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public double getRevenueDollars() {
        return revenueCents / 100.0;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getDepartureCount() {
        return departureCount;
    }

    public int getTurnsUsed() {
        return turnsUsed;
    }

    /**
     * Returns the average satisfaction of paid customers.
     * Empty OptionalDouble means no customers have paid yet (displayed as N/A).
     */
    public OptionalDouble getAverageSatisfaction() {
        if (paidCount == 0) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of((double) totalPaidSatisfaction / paidCount);
    }

    /**
     * Formats average satisfaction for display. Returns "N/A" if empty.
     */
    public String getFormattedAverageSatisfaction() {
        OptionalDouble avg = getAverageSatisfaction();
        return avg.isPresent() ? String.format("%.1f", avg.getAsDouble()) : "N/A";
    }

    /**
     * Formats the revenue in standard dollars format.
     */
    public String getFormattedRevenue() {
        return Money.format(revenueCents);
    }
}
