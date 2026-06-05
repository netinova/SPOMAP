import java.util.Random;

import Controller.AppController;
import Model.Product;
import Model.ProductCatalog;
import View.NavigationView;
import View.ShopView;
import View.SidebarView;
import View.AuthenticationView;
import Components.MultiViewPanel;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        ProductCatalog products = new ProductCatalog();
        AppController appController = new AppController(products);

        ShopView shopView = new ShopView(appController.getShopController(), products);
        NavigationView navigationView = new NavigationView(appController.getNavigationController());
        SidebarView sidebarView = new SidebarView(appController.getSidebarController());
        AuthenticationView authenticationView = new AuthenticationView(appController.getAuthenticationController());

        MultiViewPanel multiViewPanel = new MultiViewPanel(shopView, authenticationView);

        appController.setViews(shopView, navigationView, sidebarView, multiViewPanel, authenticationView);

        MainFrame mainFrame = new MainFrame(appController, sidebarView, navigationView, multiViewPanel);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            products.addProduct(
                    new Product("Product " + (i + 1), "id_" + i, "", "", 0.0, random.nextDouble(0.0, 10.0)));
        }
        products.addProduct(new Product("name", "", "", "", 0.0, 0.0));

        mainFrame.setVisible(true);
    }
}
