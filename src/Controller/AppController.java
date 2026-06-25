package Controller;

import Model.ProductCatalog;
import View.*;
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
    private ProductController productController;
    private UserProfileController profileController;

    private MultiViewPanel multiViewPanel;

    public AppController(ProductCatalog productCatalog) {
        this.productCatalog = productCatalog;

        this.shopController = new ShopController(productCatalog);
        this.navigationController = new NavigationController(productCatalog);
        this.sidebarController = new SidebarController();
        this.authenticationController = new AuthenticationController();
        this.profileController = new UserProfileController();
        this.shoppingCartController = new ShoppingCartController(productCatalog);
        this.productController = new ProductController();
    }

    public void setViews(ShopView shopView, NavigationView navigationView,
                         SidebarView sidebarView, MultiViewPanel multiViewPanel,
                         AuthenticationView authenticationView, UserProfileView userProfileView) {

        this.multiViewPanel = multiViewPanel;

        // Set views in controllers so they can update them
        shopController.setView(shopView);
        navigationController.setShopView(shopView);
        authenticationController.setView(authenticationView);
        profileController.setView(userProfileView);

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

        profileController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });

        productController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });

        shoppingCartController.setOnChangeViewListener(viewId -> {
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

    public ProductController getProductController() {
        return productController;
    }

    public UserProfileController getProfileController() {
        return profileController;
    }
}
