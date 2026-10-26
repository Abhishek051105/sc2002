package control;

import entity.GameStats;

public class Objective {
    private final int minPaidCustomers;
    private final int minRevenueCents;
    private final int minAvgSatisfaction;

    public Objective(int minPaidCustomers, int minRevenueCents, int minAvgSatisfaction) {
        this.minPaidCustomers = minPaidCustomers;
        this.minRevenueCents = minRevenueCents;
        this.minAvgSatisfaction = minAvgSatisfaction;
    }


    public boolean isMet(GameStats stats) { 
        return paidTargetMet(stats) && revenueTargetMet(stats) && satisfactionTargetMet(stats);
    }

    public boolean paidTargetMet(GameStats stats) {
        return minPaidCustomers <= stats.getPaidCount();
    }
    public boolean revenueTargetMet(GameStats stats) { 
        return minRevenueCents <= stats.getRevenueCents(); 
    }
    public boolean satisfactionTargetMet(GameStats stats) { 
        return stats.getPaidCount() > 0 && minAvgSatisfaction <= stats.getAverageSatisfaction();
        
    }

    public int getMinPaidCustomers() {
        return minPaidCustomers;
    }
    public int getMinRevenueCents() { 
        return minRevenueCents;    
    }
    public int getMinAvgSatisfaction() { 
        return minAvgSatisfaction;
    }
}
