package restaurant;

/**
 * Lifecycle states of a customer in the restaurant.
 */
public enum CustomerState {
    WAITING,
    SEATED,
    ORDERED,
    SERVED,
    READY_TO_PAY,
    PAID,
    LEFT_UNHAPPY
}
