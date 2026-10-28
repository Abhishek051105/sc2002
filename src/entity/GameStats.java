package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

/** Changes only if scoring changes (SRP). */
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

    /** Empty until the first payment (UI shows N/A). */
    public OptionalDouble getAverageSatisfaction() {
        return paidSatisfactions.stream().mapToInt(Integer::intValue).average();
    }
}
