import Components.MultiViewPanel;
import Controller.AppController;
import Controller.SettingController;
import Model.AppState;
import Model.Product;
import Model.ProductCatalog;
import Model.ShoppingCart;
import Service.AnalyticsService;
import Service.InvoiceService;
import Model.UserLists.UserAdminList;
import Model.UserLists.UserNormalList;
import Model.UserLists.UserPrimeList;
import Service.ProductService;
import Service.UserService;
import Util.Stopwatch;
import Util.ColorPalette;
import View.AuthenticationView;
import View.InvoiceDetailView;
import View.InvoiceView;
import View.NavigationView;
import View.ProductView;
import View.SettingView;
import View.ShopView;
import View.ShoppingCartView;
import View.SidebarView;
import View.UserProfileView;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        var stopWatch = new Stopwatch();

        Locale.setDefault(Locale.US);

        ProductCatalog products = new ProductCatalog();

        InvoiceService invoiceService = new InvoiceService();
        AnalyticsService analyticsService = new AnalyticsService(invoiceService);

        AppController appController = new AppController(products, invoiceService, analyticsService);

        ShopView shopView = new ShopView(appController.getShopController(), products);
        NavigationView navigationView = new NavigationView(appController.getNavigationController());
        SidebarView sidebarView = new SidebarView(appController.getSidebarController());
        AuthenticationView authenticationView = new AuthenticationView(appController.getAuthenticationController());
        UserProfileView userProfileView = new UserProfileView(appController.getProfileController());
        ProductView productView = new ProductView(appController.getProductController(), products);
        ShoppingCartView shoppingCartView = new ShoppingCartView(appController.getShoppingCartController());
        InvoiceView invoiceView = new InvoiceView(appController.getInvoiceController());
        InvoiceDetailView invoiceDetailView = new InvoiceDetailView(appController.getInvoiceController());
        SettingView settingView = new SettingView(appController.getSettingController());

        MultiViewPanel multiViewPanel = new MultiViewPanel(shopView, authenticationView, userProfileView, productView,
                shoppingCartView, invoiceView, invoiceDetailView, settingView);

        appController.setViews(shopView, navigationView, sidebarView, multiViewPanel, authenticationView,
                userProfileView, invoiceView, invoiceDetailView);

        MainFrame mainFrame = new MainFrame(appController, sidebarView, navigationView, multiViewPanel);

        ProductCatalog temp = ProductService.loadProducts();
        for (Product product : temp.getProducts()) {
            products.addProduct(product);
        }

        products.buildIndexes();

        UserNormalList normalUsersList = UserService.loadNormalUser();
        UserPrimeList primeUsersList = UserService.loadPrimeUser();
        UserAdminList adminUsersList = UserService.loadAdminUser();
        if (primeUsersList == null)
            primeUsersList = new UserPrimeList(new ArrayList<>());
        if (normalUsersList == null)
            normalUsersList = new UserNormalList(new ArrayList<>());

        AppState.getInstance().normalUsersList = normalUsersList;
        AppState.getInstance().primeUsersList = primeUsersList;
        AppState.getInstance().adminUsersList = adminUsersList;

        analyticsService.recalculateAllAnalytics();

        mainFrame.setVisible(true);

        System.out.println("Time took to load and build data: " + stopWatch.elapsedTime());
    }
}
