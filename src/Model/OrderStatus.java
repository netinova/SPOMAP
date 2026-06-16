package Model;

/**
 * Enum representing the status of an order in the system.
 */
public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PROCESSING("Processing"),
    CANCELLED("Cancelled"),
    REFUNDED("Refunded");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Check if the order is in an active state (not cancelled or delivered)
     */
    public boolean isActive() {
        return this == PENDING || this == CONFIRMED || this == PROCESSING;
    }

    /**
     * Check if the order can be cancelled
     */
    public boolean canBeCancelled() {
        return this == PENDING || this == CONFIRMED;
    }
}
