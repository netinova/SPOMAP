package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ProductCatalog {
    private ArrayList<Product> products = new ArrayList<>();

    // Indexes for fast searching
    private Map<String, Set<Integer>> nameIndex = new ConcurrentHashMap<>();
    private Map<String, Set<Integer>> colorIndex = new ConcurrentHashMap<>();
    private Map<String, Set<Integer>> manufacturerIndex = new ConcurrentHashMap<>();

    private boolean indexesBuilt = false;

    @JsonIgnore
    private Product selectedProduct;

    @JsonIgnore
    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String PROP_PRODUCTS = "products";
    public static final String PROP_SELECTED = "selectedProduct";

    public enum SearchType {
        NAME,
        COLOR,
        MANUFACTURER,
        ALL
    }

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void addProduct(Product product) {
        products.add(product);
        indexesBuilt = false;
        support.firePropertyChange(PROP_PRODUCTS, null, products);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        indexesBuilt = false;
        support.firePropertyChange(PROP_PRODUCTS, null, products);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products); // Return copy for immutability
    }

    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
        support.firePropertyChange(PROP_SELECTED, null, product);
    }

    public Product getSelectedProduct() {
        return selectedProduct;
    }

    // Search logic

    // Build indexes for fast searching (called once after loading all products)
    public void buildIndexes() {
        if (indexesBuilt)
            return;

        nameIndex.clear();
        colorIndex.clear();
        manufacturerIndex.clear();

        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);

            // Index by name words
            if (product.getName() != null) {
                String[] nameWords = product.getName().toLowerCase().split("\\s+");
                for (String word : nameWords) {
                    if (!word.isEmpty()) {
                        nameIndex.computeIfAbsent(word, k -> new HashSet<>()).add(i);
                    }
                }
                // Also index the full name as a substring searchable term
                String lowerName = product.getName().toLowerCase();
                nameIndex.computeIfAbsent(lowerName, k -> new HashSet<>()).add(i);
            }

            // Index by colors
            if (product.getColors() != null) {
                for (ProductColor color : product.getColors()) {
                    if (color != null) {
                        String colorName = color.name().toLowerCase();
                        colorIndex.computeIfAbsent(colorName, k -> new HashSet<>()).add(i);
                    }
                }
            }

            // Index by manufacturer
            if (product.getManufacturer() != null && !product.getManufacturer().isEmpty()) {
                String lowerManufacturer = product.getManufacturer().toLowerCase();
                manufacturerIndex.computeIfAbsent(lowerManufacturer, k -> new HashSet<>()).add(i);

                // Also index individual words in manufacturer name
                String[] manufacturerWords = lowerManufacturer.split("[\\s_]+");
                for (String word : manufacturerWords) {
                    if (!word.isEmpty()) {
                        manufacturerIndex.computeIfAbsent(word, k -> new HashSet<>()).add(i);
                    }
                }
            }
        }

        indexesBuilt = true;
    }

    // Optimized search with type selection
    public List<Product> search(String keyword, SearchType searchType) {
        if (!indexesBuilt) {
            buildIndexes();
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(products);
        }

        String lowerKeyword = keyword.toLowerCase().trim();
        Set<Integer> resultIndices = new HashSet<>();

        switch (searchType) {
            case NAME:
                resultIndices = searchInName(lowerKeyword);
                break;
            case COLOR:
                resultIndices = searchInColor(lowerKeyword);
                break;
            case MANUFACTURER:
                resultIndices = searchInManufacturer(lowerKeyword);
                break;
            case ALL:
            default:
                // Search across all fields
                resultIndices.addAll(searchInName(lowerKeyword));
                resultIndices.addAll(searchInColor(lowerKeyword));
                resultIndices.addAll(searchInManufacturer(lowerKeyword));
                break;
        }

        // Convert indices back to products
        List<Product> results = new ArrayList<>();
        for (int index : resultIndices) {
            if (index >= 0 && index < products.size()) {
                results.add(products.get(index));
            }
        }

        return results;
    }

    // Helper method to search in name index
    private Set<Integer> searchInName(String keyword) {
        Set<Integer> matches = new HashSet<>();

        // Direct match
        if (nameIndex.containsKey(keyword)) {
            matches.addAll(nameIndex.get(keyword));
        }

        // Partial word match - check each word in the keyword
        String[] words = keyword.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                // Check if any indexed name contains this word
                for (Map.Entry<String, Set<Integer>> entry : nameIndex.entrySet()) {
                    if (entry.getKey().contains(word)) {
                        matches.addAll(entry.getValue());
                    }
                }
            }
        }

        return matches;
    }

    // Helper method to search in color index
    private Set<Integer> searchInColor(String keyword) {
        Set<Integer> matches = new HashSet<>();

        // Direct match
        if (colorIndex.containsKey(keyword)) {
            matches.addAll(colorIndex.get(keyword));
        }

        // Partial match
        for (Map.Entry<String, Set<Integer>> entry : colorIndex.entrySet()) {
            if (entry.getKey().contains(keyword)) {
                matches.addAll(entry.getValue());
            }
        }

        return matches;
    }

    // Helper method to search in manufacturer index
    private Set<Integer> searchInManufacturer(String keyword) {
        Set<Integer> matches = new HashSet<>();

        // Direct match
        if (manufacturerIndex.containsKey(keyword)) {
            matches.addAll(manufacturerIndex.get(keyword));
        }

        // Partial word match
        String[] words = keyword.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                for (Map.Entry<String, Set<Integer>> entry : manufacturerIndex.entrySet()) {
                    if (entry.getKey().contains(word)) {
                        matches.addAll(entry.getValue());
                    }
                }
            }
        }

        return matches;
    }

}
