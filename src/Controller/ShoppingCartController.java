package Controller;

import Model.AppState;
import Model.CartItem;
import Model.Invoice;
import Model.ProductCatalog;
import Model.ShoppingCart;
import Model.User;
import Model.ViewType;
import Service.InvoiceService;

public class ShoppingCartController {

    private InvoiceService invoiceService;
    private ProductCatalog model;
    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public ShoppingCartController(ProductCatalog model, InvoiceService invoiceService) {
        this.model = model;
        this.invoiceService = invoiceService;
    }

    public void handleItemCardClick(CartItem cartItem) {
        if (listener == null)
            return;
        model.setSelectedProduct(cartItem.getProduct());
        listener.changeView(ViewType.PRODUCT.getViewId());
    }

    public void handleIncreaseQuantity(CartItem cartItem) {
        ShoppingCart cart = AppState.getInstance().getCart();
        if (cart == null)
            return;

        int newQty = cartItem.getQuantity() + 1;
        int maxStock = cartItem.getProduct().getStockQuantity();
        if (newQty > maxStock) {
            newQty = maxStock;
        }
        cart.setItemQuantity(cartItem.getProduct().getId(), newQty);
    }

    public void handleDecreaseQuantity(CartItem cartItem) {
        ShoppingCart cart = AppState.getInstance().getCart();
        if (cart == null)
            return;

        int newQty = cartItem.getQuantity() - 1;
        if (newQty >= 0) {
            cart.setItemQuantity(cartItem.getProduct().getId(), newQty);
        }
    }

    public void handleRemoveItem(CartItem cartItem) {
        ShoppingCart cart = AppState.getInstance().getCart();
        if (cart == null)
            return;
        cart.removeProduct(cartItem.getProduct().getId());
    }

    public void handleCheckout() {
        ShoppingCart cart = AppState.getInstance().getCart();
        User user = AppState.getInstance().getLoggedInUser();
        if (cart == null || user == null)
            return;

        double finalTotal = 0;
        for (CartItem item : cart.getItems()) {
            double price = item.getProduct().getPrice();
            double discount = item.getProduct().getDiscount();
            double unitFinal = price * (1 - discount / 100);
            finalTotal += unitFinal * item.getQuantity();
        }

        if (finalTotal <= 0) {

            return;
        }

        if (user.canPurchase(finalTotal)) {

            // Success

            for (CartItem item : cart.getItems()) {
                item.getProduct().reduceStock(item.getQuantity());
            }
            user.deductBalance(finalTotal);

            Invoice invoice = Invoice.fromCart(cart, user);
            invoiceService.addInvoice(invoice);

            cart.clear();
        } else {
            System.out.println("Insufficient funds!");
        }
    }
}
