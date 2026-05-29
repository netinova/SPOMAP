package Controller;

import Model.ProductCatalog;
import View.ShopView;
import View.NavigationView;
import View.SidebarView;
import Components.MultiViewPanel;

/**
 * AppController is the main orchestrator for the application.
 * It coordinates between all controllers and manages view switching.
 */
public class AppController {
    private ProductCatalog model;

    private ShopController shopController;
    private NavigationController navigationController;
    private SidebarController sidebarController;

    private MultiViewPanel multiViewPanel;

    public AppController(ProductCatalog model) {
        this.model = model;

        this.shopController = new ShopController(model);
        this.navigationController = new NavigationController(model);
        this.sidebarController = new SidebarController();
    }

    public void setViews(ShopView shopView, NavigationView navigationView,
            SidebarView sidebarView, MultiViewPanel multiViewPanel) {
        this.multiViewPanel = multiViewPanel;

        // Set views in controllers so they can update them
        shopController.setView(shopView);
        navigationController.setShopView(shopView);

        // Set up view switching listeners
        navigationController.setOnChangeViewListener(viewId -> {
            this.multiViewPanel.switchView(viewId);
        });

        sidebarController.setOnChangeViewListener(viewId -> {
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

    public ProductCatalog getModel() {
        return model;
    }
}
