package restaurant;

import java.util.OptionalDouble;

/**
 * Tracks cumulative game statistics across turns.
 * Calculates revenue, served count, unhappy departures, turns used,
 * and average satisfaction of paid customers.
 */
public class GameStats {
    private int servedCount;
    private int unhappyDepartures;
    private double revenue;
    private int paidCount;
    private int totalPaidSatisfaction;
    private int turnsUsed;

    public GameStats() {
        this.servedCount = 0;
        this.unhappyDepartures = 0;
        this.revenue = 0.0;
        this.paidCount = 0;
        this.totalPaidSatisfaction = 0;
        this.turnsUsed = 0;
    }

    /**
     * Records a successful customer payment.
     *
     * @param bill the bill amount collected
     * @param satisfaction the customer's satisfaction at time of payment
     */
    public void recordPayment(double bill, int satisfaction) {
        this.revenue += bill;
        this.totalPaidSatisfaction += satisfaction;
        this.paidCount++;
    }

    /**
     * Records a customer who has been served their order.
     */
    public void recordServed() {
        this.servedCount++;
    }

    /**
     * Records an unhappy customer who abandoned the restaurant.
     */
    public void recordUnhappyDeparture() {
        this.unhappyDepartures++;
    }

    /**
     * Records the completion of a turn.
     */
    public void recordTurn() {
        this.turnsUsed++;
    }

    public void setTurnsUsed(int turnsUsed) {
        this.turnsUsed = turnsUsed;
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    public double getRevenue() {
        return revenue;
    }

    public int getPaidCount() {
        return paidCount;
    }

    public int getTurnsUsed() {
        return turnsUsed;
    }

    /**
     * Computes the average satisfaction of paid customers.
     *
     * @return an OptionalDouble with the average satisfaction, or empty if no customers have paid yet
     */
    public OptionalDouble getAverageSatisfaction() {
        if (paidCount == 0) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of((double) totalPaidSatisfaction / paidCount);
    }

    /**
     * Formats the average satisfaction for display.
     *
     * @return formatted average (e.g., "76.00") or "N/A" if empty
     */
    public String getFormattedAverageSatisfaction() {
        OptionalDouble avg = getAverageSatisfaction();
        return avg.isPresent() ? String.format("%.2f", avg.getAsDouble()) : "N/A";
    }

    /**
     * Displays the end-of-turn or end-of-game summary matching Section 3.5.
     */
    public void displaySummary() {
        System.out.println("----------------------------------------");
        System.out.println("Turns Used:            " + turnsUsed);
        System.out.println("Served Count:          " + servedCount);
        System.out.println("Unhappy Departures:    " + unhappyDepartures);
        System.out.printf("Total Revenue:         $%.2f%n", revenue);
        System.out.println("Average Satisfaction:  " + getFormattedAverageSatisfaction());
        System.out.println("----------------------------------------");
    }
}
