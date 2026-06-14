package Controller;

import Components.MultiViewPanel;
import Model.AppState;
import Model.CartItem;
import Model.ProductCatalog;
import Model.ShoppingCart;

public class ShoppingCartController {

    private ProductCatalog model;
    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public ShoppingCartController(ProductCatalog model) {
        this.model = model;
    }

    public void handleItemCardClick(CartItem cartItem) {
        if (listener == null)
            return;
        model.setSelectedProduct(cartItem.getProduct());
        listener.changeView(MultiViewPanel.PRODUCT_VIEW);
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
}
