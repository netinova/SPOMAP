package Controller;

import Model.ProductCatalog;
import Model.ViewType;
import View.AuthenticationView;
import View.ShopView;
import java.util.List;

import Components.MultiViewPanel;
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

    public void searchProducts(String searchText, String searchType) {
        // Map string search type to SearchType enum
        ProductCatalog.SearchType type;
        switch (searchType.toLowerCase()) {
            case "name":
                type = ProductCatalog.SearchType.NAME;
                break;
            case "color":
                type = ProductCatalog.SearchType.COLOR;
                break;
            case "manufacturer":
                type = ProductCatalog.SearchType.MANUFACTURER;
                break;
            case "all":
            default:
                type = ProductCatalog.SearchType.ALL;
                break;
        }

        // Use optimized indexed search
        List<Product> results = model.search(searchText, type);
        shopView.displayProducts(new java.util.ArrayList<>(results));
        System.out
                .println("Search for: " + searchText + " (type: " + type + ") - Found " + results.size() + " products");
    }

    public void onUserIconClick() {
        System.out.println("clicked on Icon user");

        if (listener == null) {
            return;
        }

        if (!AppState.getInstance().isUserLoggedIn()) {
            listener.changeView(ViewType.AUTH.getViewId());
            return;
        }

        listener.changeView(ViewType.USER.getViewId());

    }

    public void onShoppingCartIconClick() {
        // System.out.println("clicked on shopping cart");

        if (listener == null) {
            return;
        }

        if (!AppState.getInstance().isUserLoggedIn()) {
            listener.changeView(ViewType.AUTH.getViewId());
            return;
        }

        listener.changeView(ViewType.SHOPPING_CART.getViewId());
    }
}
