package Model;

/**
 * Enum representing the different views/screens in the application.
 * Centralizes view identifiers to prevent magic strings throughout the
 * codebase.
 */
public enum ViewType {
    SHOP("shopView", "Shop"),
    PRODUCT("productView", "Product Details"),
    AUTH("authView", "Authentication"),
    SHOPPING_CART("shoppingCartView", "Shopping Cart"),
    USER("userView", "User Profile"),
    INVOICE("invoiceView", "invoices display"),
    INVOICE_DETAIL("invoiceDetailView", "Invoice Details");

    private final String viewId;
    private final String displayName;

    ViewType(String viewId, String displayName) {
        this.viewId = viewId;
        this.displayName = displayName;
    }

    public String getViewId() {
        return viewId;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the ViewType from a view ID string
     */
    public static ViewType fromViewId(String viewId) {
        if (viewId == null || viewId.isEmpty()) {
            return SHOP;
        }

        for (ViewType type : values()) {
            if (type.viewId.equals(viewId)) {
                return type;
            }
        }
        return SHOP;
    }

    /**
     * Check if this view requires user authentication
     */
    public boolean requiresAuth() {
        return this == SHOPPING_CART || this == USER;
    }
}
