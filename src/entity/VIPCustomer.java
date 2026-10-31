package entity;

public class VIPCustomer extends Customer {

    public VIPCustomer(int customerId, int arrivalTurn) {
        super(customerId, arrivalTurn);
    }

    @Override
    public int getSatisfactionDecay() {
        return 5;
    }

    @Override
    public int calculateBill(int basePriceCents) {
        int fullPrice = super.calculateBill(basePriceCents);
        return fullPrice * 90 / 100;
    }

    @Override
    public String getTypeLabel() {
        return "VIP";
    }
}

