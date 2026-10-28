package entity;

import java.util.ArrayList;
import java.util.List;

public class GameStats {
    private int revenueCents;
    private final List<Integer> paidSatisfactions = new ArrayList<>();
    private int servedCount;
    private int unhappyDepartures;

    /** Satisfaction is recorded at the moment of payment. */
    public void recordPayment(int billCents, int satisfaction) {
        revenueCents += billCents;
        paidSatisfactions.add(satisfaction);
    }

    public void recordServed() {
        servedCount++;
    }

    public void recordDeparture() {
        unhappyDepartures++;
    }

    public int getRevenueCents() {
        return revenueCents;
    }

    public int getPaidCount() {
        return paidSatisfactions.size();
    }

    public int getServedCount() {
        return servedCount;
    }

    public int getUnhappyDepartures() {
        return unhappyDepartures;
    }

    /** Average satisfaction of paid customers. Check getPaidCount() > 0 first (the UI shows N/A). */
    public double getAverageSatisfaction() {
        int total = 0;
        for (int s : paidSatisfactions) {
            total += s;
        }
        return (double) total / paidSatisfactions.size();
    }
}
