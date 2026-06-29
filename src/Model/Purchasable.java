package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Interface for items that can be added to a shopping cart.
 * Provides common methods for cart operations.
 */
public interface Purchasable {

    /**
     * Get the unique identifier of the product
     */
    String getId();

    /**
     * Get the name of the product
     */
    String getName();

    /**
     * Get the base price of the product
     */
    double getPrice();

    /**
     * Get the discount percentage (0-100)
     */
    double getDiscount();

    /**
     * Get the final price after applying discount
     */

    @JsonIgnore
    default double getFinalPrice() {
        return getPrice() * (1 - getDiscount() / 100);
    }

    /**
     * Check if the product is in stock
     */
    boolean isInStock();

    /**
     * Get the current stock quantity
     */
    int getStockQuantity();

    /**
     * Reduce stock by the specified quantity
     */
    void reduceStock(int quantity);
}
