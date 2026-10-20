package control;

import entity.GameStats;
import java.util.OptionalDouble;

/**
 * Evaluates game victory criteria after turn 18.
 * Victory requires:
 * 1. At least 4 paid customers
 * 2. At least $45.00 revenue (4500 cents)
 * 3. Average satisfaction of paid customers at least 60.0
 */
public class Objective {
    public static final int TARGET_PAID = 4;
    public static final int TARGET_REVENUE_CENTS = 4500;
    public static final double TARGET_AVG_SATISFACTION = 60.0;

    /**
     * Checks if all 3 victory targets are met.
     */
    public boolean isMet(GameStats stats) {
        if (stats == null) {
            return false;
        }

        boolean paidMet = stats.getPaidCount() >= TARGET_PAID;
        boolean revenueMet = stats.getRevenueCents() >= TARGET_REVENUE_CENTS;
        OptionalDouble avg = stats.getAverageSatisfaction();
        boolean satisfactionMet = avg.isPresent() && avg.getAsDouble() >= TARGET_AVG_SATISFACTION;

        return paidMet && revenueMet && satisfactionMet;
    }

    public boolean isPaidMet(GameStats stats) {
        return stats != null && stats.getPaidCount() >= TARGET_PAID;
    }

    public boolean isRevenueMet(GameStats stats) {
        return stats != null && stats.getRevenueCents() >= TARGET_REVENUE_CENTS;
    }

    public boolean isSatisfactionMet(GameStats stats) {
        if (stats == null) return false;
        OptionalDouble avg = stats.getAverageSatisfaction();
        return avg.isPresent() && avg.getAsDouble() >= TARGET_AVG_SATISFACTION;
    }
}
