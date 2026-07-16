import Components.MultiViewPanel;
import Components.SplashScreen;
import Controller.AppController;
import Model.AppState;
import Model.Product;
import Model.ProductCatalog;
import Service.AnalyticsService;
import Service.InvoiceService;
import Service.ThemeService;
import Model.Theme;
import Model.UserLists.UserAdminList;
import Model.UserLists.UserNormalList;
import Model.UserLists.UserPrimeList;
import Service.ProductService;
import Service.UserService;
import Util.ColorPalette;
import Util.Stopwatch;
import View.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {

    private record ProgressUpdate(String status, int percent) {
    }

    public static void main(String[] args) {

        Locale.setDefault(Locale.US);

        ThemeService themeService = new ThemeService();
        Theme defaultTheme = themeService.loadThemeByName("default dark");
        if (defaultTheme == null) {
            defaultTheme = Theme.defaultDark();
            themeService.saveTheme(defaultTheme);
        }
        ColorPalette.getInstance().applyTheme(defaultTheme);

        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        SwingWorker<MainFrame, ProgressUpdate> worker = new SwingWorker<>() {
            @Override
            protected MainFrame doInBackground() throws Exception {
                var stopWatch = new Stopwatch();

                publish(new ProgressUpdate("Initializing services...", 5));
                ProductCatalog products = new ProductCatalog();

                InvoiceService invoiceService = new InvoiceService();
                AnalyticsService analyticsService = new AnalyticsService(invoiceService);
                AppController appController = new AppController(products, invoiceService, analyticsService);

                publish(new ProgressUpdate("Building views...", 20));
                ShopView shopView = new ShopView(appController.getShopController(), products);
                NavigationView navigationView = new NavigationView(appController.getNavigationController());
                SidebarView sidebarView = new SidebarView(appController.getSidebarController());
                AuthenticationView authenticationView = new AuthenticationView(
                        appController.getAuthenticationController());
                UserProfileView userProfileView = new UserProfileView(appController.getProfileController());
                ProductView productView = new ProductView(appController.getProductController(), products);
                ShoppingCartView shoppingCartView = new ShoppingCartView(appController.getShoppingCartController());
                InvoiceView invoiceView = new InvoiceView(appController.getInvoiceController());
                InvoiceDetailView invoiceDetailView = new InvoiceDetailView(appController.getInvoiceController());
                SettingView settingView = new SettingView(appController.getSettingController());

                MultiViewPanel multiViewPanel = new MultiViewPanel(shopView, authenticationView, userProfileView,
                        productView,
                        shoppingCartView, invoiceView, invoiceDetailView, settingView);

                appController.setViews(shopView, navigationView, sidebarView, multiViewPanel, authenticationView,
                        userProfileView, invoiceView, invoiceDetailView);

                publish(new ProgressUpdate("Creating main window...", 40));
                MainFrame mainFrame = new MainFrame(appController, sidebarView, navigationView, multiViewPanel);

                publish(new ProgressUpdate("Loading products...", 55));
                ProductCatalog temp = ProductService.loadProducts();
                for (Product product : temp.getProducts())
                    products.addProduct(product);
                products.buildIndexes();

                publish(new ProgressUpdate("Loading users...", 75));
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

                publish(new ProgressUpdate("Calculating analytics...", 90));
                analyticsService.recalculateAllAnalytics();

                publish(new ProgressUpdate("Done!", 100));
                System.out.println("Time took to load and build data: " + stopWatch.elapsedTime());

                return mainFrame;
            }

            @Override
            protected void process(List<ProgressUpdate> proses) {
                ProgressUpdate latest = proses.getLast();
                splash.setStatus(latest.status());
                splash.setProgress(latest.percent());
            }

            @Override
            protected void done() {
                try {
                    MainFrame frame = get();
                    Timer timer = new Timer(500, e -> {
                        splash.dispose();
                        frame.setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }
}
