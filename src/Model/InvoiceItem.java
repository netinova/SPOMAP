package Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class InvoiceItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;
    private double discount;

    public InvoiceItem(String productId, String productName, double unitPrice, int quantity, double discount) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public InvoiceItem() {

    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getDiscount() {
        return discount;
    }

    @JsonIgnore
    public double getTotalPrice() {
        double totalPrice;
        totalPrice = unitPrice * (100 - discount) / 100.0;
        totalPrice *= quantity;
        return totalPrice;
    }

}
