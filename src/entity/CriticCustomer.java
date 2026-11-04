package entity;

public class CriticCustomer extends Customer {

    public CriticCustomer(int customerId, int arrivalTurn) {
        super(customerId, arrivalTurn);
    }

    @Override
    public int getSatisfactionDecay() {
        return 12;
    }

    /** Always the most expensive dish; on a tie the earlier menu item wins. */
    @Override
    public MenuItem chooseMenuItem(Menu menu) {
        // same checks as Customer.chooseMenuItem(), so a Critic fails the same way (LSP)
        if (menu == null) {
            throw new IllegalArgumentException("Menu cannot be null.");
        }
        if (menu.size() == 0) {
            throw new IllegalArgumentException("Menu cannot be empty.");
        }

        MenuItem mostExpensive = menu.getItem(0);
        for (int i = 1; i < menu.size(); i++) {
            if (menu.getItem(i).getPriceCents() > mostExpensive.getPriceCents()) {
                mostExpensive = menu.getItem(i);
            }
        }
        return mostExpensive;
    }

    @Override
    public String getTypeLabel() {
        return "Critic";
    }
}
