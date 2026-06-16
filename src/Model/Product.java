package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Product implements Purchasable {
    private String name;
    private String id;

    private String thumbnail;
    private String[] productImages;

    private String description;
    private double discount;
    private double price;

    private ProductColor[] colors;
    private String manufacturer;

    private Map<String, String> technicalSpecs; // {"Mass": "250g", "Color": "Black", "Size": "M"}

    @JsonIgnore
    private int stockQuantity;

    public Product(String name, String id, String thumbnail, String description,
            double price, double discount, String[] productImages) {
        this.name = name;
        this.id = id;
        this.thumbnail = thumbnail;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.technicalSpecs = new HashMap<>();
        this.productImages = productImages != null ? productImages.clone() : new String[0];

        generateRandomStockQuantity();
    }

    public Product() {
        this.technicalSpecs = new HashMap<>();
        this.productImages = new String[0];

        generateRandomStockQuantity();
    }

    public ProductColor[] getColors() {
        return colors;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    private void generateRandomStockQuantity() {
        Random random = new Random();
        this.stockQuantity = random.nextInt(1, 50);
    }

    public String[] getProductImages() {
        return productImages != null ? productImages.clone() : new String[0];
    }

    public void setProductImages(String[] productImages) {
        this.productImages = productImages;
    }

    @JsonIgnore
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stockQuantity) {
            stockQuantity -= quantity;
        }
    }

    public double getDiscount() {
        return discount;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Map<String, String> getTechnicalSpecs() {
        return technicalSpecs;
    }

    public void setTechnicalSpecs(Map<String, String> technicalSpecs) {
        this.technicalSpecs = technicalSpecs;
    }

    public void addTechnicalSpec(String key, String value) {
        this.technicalSpecs.put(key, value);
    }
}
