package Controller;

import Model.ProductCatalog;
import Model.ShoppingCart;
import View.ShopView;
import View.AuthenticationView;
import View.NavigationView;
import View.SidebarView;
import Components.MultiViewPanel;

/**
 * AppController is the main orchestrator for the application.
 * It coordinates between all controllers and manages view switching.
 */
public class AppController {
    private ProductCatalog productCatalog;

    private ShopController shopController;
    private NavigationController navigationController;
    private SidebarController sidebarController;
    private AuthenticationController authenticationController;
    private ShoppingCartController shoppingCartController;

    private MultiViewPanel multiViewPanel;

    public AppController(ProductCatalog productCatalog, ShoppingCart shoppingCart) {
        this.productCatalog = productCatalog;

        this.shopController = new ShopController(productCatalog);
        this.navigationController = new NavigationController(productCatalog);
        this.sidebarController = new SidebarController();
        this.authenticationController = new AuthenticationController();
        this.shoppingCartController = new ShoppingCartController(shoppingCart);
    }

    public void setViews(ShopView shopView, NavigationView navigationView,
            SidebarView sidebarView, MultiViewPanel multiViewPanel,
            AuthenticationView authenticationView) {
                
        this.multiViewPanel = multiViewPanel;

        // Set views in controllers so they can update them
        shopController.setView(shopView);
        navigationController.setShopView(shopView);
        authenticationController.setView(authenticationView);

        // Set up view switching listeners
        navigationController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });

        sidebarController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });

        shopController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });

        authenticationController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });
    }

    // Getters for controllers (so other components can access them)
    public ShopController getShopController() {
        return shopController;
    }

    public NavigationController getNavigationController() {
        return navigationController;
    }

    public SidebarController getSidebarController() {
        return sidebarController;
    }

    public AuthenticationController getAuthenticationController() {
        return authenticationController;
    }

    public ShoppingCartController getShoppingCartController() {
        return shoppingCartController;
    }

    public ProductCatalog getModel() {
        return productCatalog;
    }
}
