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
    public void recordPayment(int billCents, int satisfaction) { throw new UnsupportedOperationException("TODO"); }
    public void recordServed() { throw new UnsupportedOperationException("TODO"); }
    public void recordDeparture() { throw new UnsupportedOperationException("TODO"); }

    public int getRevenueCents() { throw new UnsupportedOperationException("TODO"); }
    public int getPaidCount() { throw new UnsupportedOperationException("TODO"); }
    public int getServedCount() { throw new UnsupportedOperationException("TODO"); }
    public int getUnhappyDepartures() { throw new UnsupportedOperationException("TODO"); }
    /** Empty until the first payment (UI shows N/A). */
    public OptionalDouble getAverageSatisfaction() { throw new UnsupportedOperationException("TODO"); }
}
