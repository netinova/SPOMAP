import Components.MultiViewPanel;
import Controller.AppController;
import Model.AppState;
import Model.Product;
import Model.ProductCatalog;
import Service.InvoiceService;
import Model.UserLists.UserAdminList;
import Model.UserLists.UserNormalList;
import Model.UserLists.UserPrimeList;
import Service.ProductService;
import Service.UserService;
import View.AuthenticationView;
import View.NavigationView;
import View.ProductView;
import View.ShopView;
import View.ShoppingCartView;
import View.SidebarView;
import View.UserProfileView;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException {
        Locale.setDefault(Locale.US);

        ProductCatalog products = new ProductCatalog();

        InvoiceService invoiceService = new InvoiceService();

        AppController appController = new AppController(products, invoiceService);

        ShopView shopView = new ShopView(appController.getShopController(), products);
        NavigationView navigationView = new NavigationView(appController.getNavigationController());
        SidebarView sidebarView = new SidebarView(appController.getSidebarController());
        AuthenticationView authenticationView = new AuthenticationView(appController.getAuthenticationController());
        UserProfileView userProfileView = new UserProfileView(appController.getProfileController());
        ProductView productView = new ProductView(appController.getProductController(), products);
        ShoppingCartView shoppingCartView = new ShoppingCartView(appController.getShoppingCartController());

        MultiViewPanel multiViewPanel = new MultiViewPanel(shopView, authenticationView, userProfileView, productView,
                shoppingCartView);

        appController.setViews(shopView, navigationView, sidebarView, multiViewPanel, authenticationView,
                userProfileView);

        MainFrame mainFrame = new MainFrame(appController, sidebarView, navigationView, multiViewPanel);

        ProductCatalog temp = ProductService.loadProducts();
        for (Product product : temp.getProducts()) {
            products.addProduct(product);
        }

        products.buildIndexes();

        UserNormalList normalUsersList = UserService.loadNormalUser();
        UserPrimeList primeUsersList = UserService.loadPrimeUser();
        UserAdminList adminUsersList = UserService.loadAdminUser();
        if (primeUsersList==null)
            primeUsersList = new UserPrimeList(new ArrayList<>());
        if (normalUsersList==null)
            normalUsersList = new UserNormalList(new ArrayList<>());

        AppState.getInstance().normalUsersList = normalUsersList;
        AppState.getInstance().primeUsersList = primeUsersList;
        AppState.getInstance().adminUsersList = adminUsersList;

        mainFrame.setVisible(true);
    }
}
