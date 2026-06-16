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
        support.firePropertyChange(PROP_PRODUCTS, null, new ArrayList<>(products));
    }

    public void removeProduct(Product product) {
        products.remove(product);
        indexesBuilt = false;
        support.firePropertyChange(PROP_PRODUCTS, null, new ArrayList<>(products));
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

    // Build indexes for fast searching
    public void buildIndexes() {
        if (indexesBuilt)
            return;

        nameIndex.clear();
        colorIndex.clear();
        manufacturerIndex.clear();

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);

            // Index by name words
            if (p.getName() != null) {
                String[] nameWords = p.getName().toLowerCase().split("\\s+");
                for (String word : nameWords) {
                    if (!word.isEmpty()) {
                        nameIndex.computeIfAbsent(word, k -> new HashSet<>()).add(i);
                    }
                }
                String lowerName = p.getName().toLowerCase();
                nameIndex.computeIfAbsent(lowerName, k -> new HashSet<>()).add(i);
            }

            // Index by colors
            if (p.getColors() != null) {
                for (ProductColor color : p.getColors()) {
                    if (color != null) {
                        String colorName = color.name().toLowerCase();
                        colorIndex.computeIfAbsent(colorName, k -> new HashSet<>()).add(i);
                    }
                }
            }

            // Index by manufacturer
            if (p.getManufacturer() != null && !p.getManufacturer().isEmpty()) {
                String lowerManufacturer = p.getManufacturer().toLowerCase();
                manufacturerIndex.computeIfAbsent(lowerManufacturer, k -> new HashSet<>()).add(i);
                
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

    private Set<Integer> searchInName(String keyword) {
        Set<Integer> matches = new HashSet<>();

        if (nameIndex.containsKey(keyword)) {
            matches.addAll(nameIndex.get(keyword));
            return matches; // Early exit for exact matches
        }

        // Prefix match
        for (Map.Entry<String, Set<Integer>> entry : nameIndex.entrySet()) {
            if (entry.getKey().startsWith(keyword)) {
                matches.addAll(entry.getValue());
            }
        }

        return matches;
    }


    private Set<Integer> searchInColor(String keyword) {
        Set<Integer> matches = new HashSet<>();

        // Direct match
        if (colorIndex.containsKey(keyword)) {
            matches.addAll(colorIndex.get(keyword));
            return matches;
        }

        // Prefix match
        for (Map.Entry<String, Set<Integer>> entry : colorIndex.entrySet()) {
            if (entry.getKey().startsWith(keyword)) {
                matches.addAll(entry.getValue());
            }
        }

        return matches;
    }


    private Set<Integer> searchInManufacturer(String keyword) {
        Set<Integer> matches = new HashSet<>();

        if (manufacturerIndex.containsKey(keyword)) {
            matches.addAll(manufacturerIndex.get(keyword));
            return matches;
        }

        // Prefix match
        for (Map.Entry<String, Set<Integer>> entry : manufacturerIndex.entrySet()) {
            if (entry.getKey().startsWith(keyword)) {
                matches.addAll(entry.getValue());
            }
        }

        return matches;
    }

}
