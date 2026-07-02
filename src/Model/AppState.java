package Model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import Model.UserLists.UserAdminList;
import Model.UserLists.UserNormalList;
import Model.UserLists.UserPrimeList;

public class AppState {

    private static AppState instance;

    private User loggedInUser;
    private ShoppingCart cart;

    public UserNormalList normalUsersList;
    public UserPrimeList primeUsersList;
    public UserAdminList adminUsersList;

    public static final String PROP_USER = "loggedInUser";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    public void addListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

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
        support.firePropertyChange(PROP_USER, null, loggedInUser);
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
