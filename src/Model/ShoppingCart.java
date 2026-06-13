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

}
