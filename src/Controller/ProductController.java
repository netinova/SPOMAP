package Controller;

import Model.AppState;
import Model.CartItem;
import Model.Product;
import Model.ShoppingCart;
import Model.ViewType;

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
            listener.changeView(ViewType.AUTH.getViewId());
            return;
        }

        ShoppingCart shoppingCart = AppState.getInstance().getCart();

        int available = shoppingCart.getAvailableQuantity(product);
        int actualQuantity = Math.min(quantity, available);
        if (actualQuantity <= 0) {
            return;
        }

        shoppingCart.addProduct(product, actualQuantity);

        for (CartItem item : shoppingCart.getItems()) {
            System.out.println(item.getProduct().getName() + " " + item.getQuantity());
        }

    }

}
