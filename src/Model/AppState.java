package Model;

public class AppState {

    private static AppState instance;

    private User loggedInUser;
    private ShoppingCart cart;

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart currentShoppingCart) {
        this.cart = currentShoppingCart;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User loggedInUser) {
        this.loggedInUser = loggedInUser;
    }

    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }

    private AppState() {
    }

    public static AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }

        return instance;
    }
}
