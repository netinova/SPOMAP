package Model;

import java.util.ArrayList;
import java.util.List;

public class ProductService {
    private ProductCatalog catalog;

    public ProductService(ProductCatalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Search products by name/keyword
     */
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(catalog.getProducts());
        }
        return catalog.searchByName(keyword);
    }

    /**
     * Filter products by price range
     */
    public List<Product> filterByPrice(double minPrice, double maxPrice) {
        return catalog.getProducts().stream()
                .filter(p -> p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .toList();
    }

    /**
     * Filter products that are in stock
     */
    public List<Product> filterInStock() {
        return catalog.getProducts().stream()
                .filter(Product::isInStock)
                .toList();
    }

    /**
     * Get all products
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(catalog.getProducts());
    }
}
