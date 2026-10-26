package entity;

/** Lifecycle of an order. Transitions only happen inside Order. */
public enum OrderStatus {
    IN_PREPARATION, READY, SERVED, PAID, CANCELLED
}
