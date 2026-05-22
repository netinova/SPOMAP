package Model;

import javax.swing.ImageIcon;

public class Product {
    private String productName;
    private String productId;
    private ImageIcon productImage;
    private String productDescription;
    private double price;
    private int stockQuantity;

    public Product(String productName, String productId, ImageIcon productImage, String productDescription,
            double price) {
        this.productName = productName;
        this.productId = productId;
        this.productImage = productImage;
        this.productDescription = productDescription;
        this.price = price;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void reduceStock(int quantity) {
        if (quantity <= stockQuantity) {
            stockQuantity -= quantity;
        }
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductId() {
        return productId;
    }

    public ImageIcon getProductImage() {
        return productImage;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public double getPrice() {
        return price;
    }
}
