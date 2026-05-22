package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class ProductCatalog {
    private ArrayList<Product> products = new ArrayList<>();
    private Product selectedProduct;
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String PROP_PRODUCTS = "products";
    public static final String PROP_SELECTED = "selectedProduct";

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addProduct(Product product) {
        products.add(product);
        support.firePropertyChange(PROP_PRODUCTS, null, products);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        support.firePropertyChange(PROP_PRODUCTS, null, products);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products); // Return copy for immutability
    }

    public void setSelectedProduct(Product product) {
        Product old = this.selectedProduct;
        this.selectedProduct = product;
        support.firePropertyChange(PROP_SELECTED, old, product);
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

}
