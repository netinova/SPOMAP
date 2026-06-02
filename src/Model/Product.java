package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Product {
    private String name;
    private String id;
    private String imageLoc;
    private String description;
    private double discount;
    private double price;
    private int stockQuantity;

    public Product(String name, String id, String imageLoc, String description,
            double price, double discount) {
        this.name = name;
        this.id = id;
        this.imageLoc = imageLoc;
        this.description = description;
        this.price = price;
        this.discount = discount;
    }

    public Product() {

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

    public String getImageLoc() {
        return imageLoc;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
