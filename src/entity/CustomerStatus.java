package entity;

/**
 * State machine stages for a customer.
 * Lifecycle: WAITING -> SEATED -> ORDERED -> EATING -> READY_TO_PAY -> PAID (or LEFT if abandoned).
 */
public enum CustomerStatus {
    WAITING,
    SEATED,
    ORDERED,
    EATING,
    READY_TO_PAY,
    PAID,
    LEFT
}
