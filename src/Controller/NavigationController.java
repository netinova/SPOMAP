package Controller;

import Components.MultiViewPanel;
import Model.ProductCatalog;
import Model.ProductService;
import View.ShopView;
import java.util.List;
import Model.Product;

public class NavigationController {

    public interface onChangeViewListener {
        void changeView(String viewId);
    }

    private onChangeViewListener listener;

    @SuppressWarnings("unused")
    private ProductCatalog model;

    private ProductService service;
    private ShopView shopView;

    public NavigationController(ProductCatalog model) {
        this.model = model;
        this.service = new ProductService(model);
    }

    public void setOnChangeViewListener(onChangeViewListener listener) {
        this.listener = listener;
    }

    public void setShopView(ShopView shopView) {
        this.shopView = shopView;
    }

    public void searchProducts(String searchString) {
        // Delegate to ProductService for business logic
        List<Product> results = service.searchProducts(searchString);
        shopView.displayProducts(new java.util.ArrayList<>(results));
        System.out.println("Search for: " + searchString + " - Found " + results.size() + " products");
    }

    public void onUserIconClick() {
        System.out.println("clicked on Icon user");

        if (listener != null) {
            listener.changeView(MultiViewPanel.USER_VIEW);
        }
    }
}
