package Model;

import java.awt.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ShoppingCart {

    private ArrayList<CartItem> items = new ArrayList<>();

    public static final String PROP_ITEMS = "items";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.support.addPropertyChangeListener(listener);
    }

    public void addProduct(Product product, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.increaseQuantity(quantity);
                support.firePropertyChange(PROP_ITEMS, null, items);
                return;
            }
        }

        items.add(new CartItem(product, quantity));
        support.firePropertyChange(PROP_ITEMS, null, items);
    }

    public void removeProduct(String productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        support.firePropertyChange(PROP_ITEMS, null, items);
    }

    public void setItemQuantity(String productId, int newQuantity) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(productId)) {
                if (newQuantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(newQuantity);
                }
                support.firePropertyChange(PROP_ITEMS, null, items);
                return;
            }
        }
    }

    public int getReservedQuantity(Product product) {
        int reserved = 0;
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                reserved += item.getQuantity();
            }
        }
        return reserved;
    }

    public int getAvailableQuantity(Product product) {
        return Math.max(0, product.getStockQuantity() - getReservedQuantity(product));
    }

    public ArrayList<CartItem> getItems() {
        return new ArrayList<>(items);
    }

}
