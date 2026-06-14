package Controller;

import Components.MultiViewPanel;
import Model.ProductCatalog;
import View.AuthenticationView;
import View.ShopView;
import java.util.List;

import Model.AppState;
import Model.Product;

public class NavigationController {

    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    private ProductCatalog model;

    private ShopView shopView;

    public NavigationController(ProductCatalog model) {
        this.model = model;
    }

    public void setShopView(ShopView shopView) {
        this.shopView = shopView;
    }

    public void searchProducts(String searchString) {
        // Delegate to ProductService for business logic
        List<Product> results = model.searchByName(searchString);
        shopView.displayProducts(new java.util.ArrayList<>(results));
        System.out.println("Search for: " + searchString + " - Found " + results.size() + " products");
    }

    public void onUserIconClick() {
        System.out.println("clicked on Icon user");

        if (listener == null) {
            return;
        }

        if (!AppState.getInstance().isUserLoggedIn()) {
            listener.changeView(MultiViewPanel.AUTH_VIEW);
            return;
        }

        listener.changeView(MultiViewPanel.USER_VIEW);
    }

    public void onShoppingCartIconClick() {
        System.out.println("clicked on shopping cart");

        if (listener == null) {
            return;
        }

        if (!AppState.getInstance().isUserLoggedIn()) {
            listener.changeView(MultiViewPanel.AUTH_VIEW);
            return;
        }

        listener.changeView(MultiViewPanel.SHOPPING_CART_VIEW);
    }
}
