package Controller;

import Model.CartItem;
import Model.ShoppingCart;

public class ShoppingCartController {

    @SuppressWarnings("unused")
    private ShoppingCart shoppingCart;

    public ShoppingCartController(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void handleItemCardClick(CartItem cartItem) {
        System.out.println("Clicked on ItemCard!");
    }
}
