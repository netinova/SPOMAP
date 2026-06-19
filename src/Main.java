import java.util.Random;

import View.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import Controller.AppController;
import Model.AppState;
import Model.Product;
import Model.ProductCatalog;
import Model.ShoppingCart;
import Components.MultiViewPanel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);

        ProductCatalog products = new ProductCatalog();

        AppController appController = new AppController(products);

        ShopView shopView = new ShopView(appController.getShopController(), products);
        NavigationView navigationView = new NavigationView(appController.getNavigationController());
        SidebarView sidebarView = new SidebarView(appController.getSidebarController());
        AuthenticationView authenticationView = new AuthenticationView(appController.getAuthenticationController());
        UserProfileView userProfileView = new UserProfileView();
        ProductView productView = new ProductView(appController.getProductController(), products);
        ShoppingCartView shoppingCartView = new ShoppingCartView(appController.getShoppingCartController());

        MultiViewPanel multiViewPanel = new MultiViewPanel(shopView, authenticationView, userProfileView,productView, shoppingCartView);

        appController.setViews(shopView, navigationView, sidebarView, multiViewPanel, authenticationView);

        MainFrame mainFrame = new MainFrame(appController, sidebarView, navigationView, multiViewPanel);

        ObjectMapper objectMapper = new ObjectMapper();

        ProductCatalog temp = objectMapper.readValue(new File("database/products.json"), ProductCatalog.class);
        for (Product product : temp.getProducts()) {
            products.addProduct(product);
        }

        products.buildIndexes();

        mainFrame.setVisible(true);
    }
}
