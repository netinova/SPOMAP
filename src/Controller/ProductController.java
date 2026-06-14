package Controller;

import Components.MultiViewPanel;
import Model.AppState;
import Model.CartItem;
import Model.Product;
import Model.ShoppingCart;

public class ProductController {

    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public void handleAddToCart(Product product, int quantity) {

        if (listener == null) {
            return;
        }

        if (!AppState.getInstance().isUserLoggedIn()) {
            listener.changeView(MultiViewPanel.AUTH_VIEW);
            return;
        }

        ShoppingCart shoppingCart = AppState.getInstance().getCart();

        shoppingCart.addProduct(product, quantity);

        for (CartItem item : shoppingCart.getItems()) {
            System.out.println(item.getProduct().getName() + " " + item.getQuantity());
        }

    }

}
