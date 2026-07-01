package Model;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import Util.LocalDateTimeDeserializer;
import Util.LocalDateTimeSerializer;

public class Invoice {
    private String invoiceId;
    private String userId;
    private ArrayList<InvoiceItem> items;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime InvoiceDate;

    private InvoiceStatus status;

    public Invoice(String invoiceId, String userId,
            ArrayList<InvoiceItem> items, LocalDateTime invoiceDate,
            InvoiceStatus status) {
        this.invoiceId = invoiceId;
        this.userId = userId;
        this.items = items;
        InvoiceDate = invoiceDate;
        this.status = status;
    }

    public Invoice() {
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<InvoiceItem> getItems() {
        return items;
    }

    public LocalDateTime getInvoiceDate() {
        return InvoiceDate;
    }

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setItems(ArrayList<InvoiceItem> items) {
        this.items = items;
    }

    public void setInvoiceDate(LocalDateTime invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public double getFinalPrice() {
        double finalPrice = 0;

        for (InvoiceItem item : items) {
            finalPrice += item.getTotalPrice();
        }

        return finalPrice;
    }

    @JsonIgnore
    public double getRawPrice() {
        double rawPrice = 0;

        for (InvoiceItem item : items) {
            rawPrice += item.getUnitPrice();
        }

        return rawPrice;
    }

    public static Invoice fromCart(ShoppingCart cart, User user) {
        ArrayList<InvoiceItem> invoiceItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            Product p = cartItem.getProduct();
            invoiceItems.add(new InvoiceItem(
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    cartItem.getQuantity(),
                    p.getDiscount()));
        }
        return new Invoice(
                "INV_" + System.currentTimeMillis(),
                user.getUserId(),
                invoiceItems,
                LocalDateTime.now(),
                InvoiceStatus.Paid);
    }
}
