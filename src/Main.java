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

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        ThemeService themeService = new ThemeService();
        Theme defaultTheme = themeService.loadThemeByName("Coffee Cream");
        if (defaultTheme == null) {
            defaultTheme = Theme.defaultDark();
            themeService.saveTheme(defaultTheme);
        }
        ColorPalette.getInstance().applyTheme(defaultTheme);

        SplashScreen splash = new SplashScreen();
        splash.setVisible(true);

        SwingWorker<LoadedData, ProgressUpdate> worker = new SwingWorker<>() {
            @Override
            protected LoadedData doInBackground() throws Exception {
                var stopWatch = new Stopwatch();

                publish(new ProgressUpdate("Initializing services...", 5));
                ProductCatalog products = new ProductCatalog();
                InvoiceService invoiceService = new InvoiceService();
                AnalyticsService analyticsService = new AnalyticsService(invoiceService);
                AppController appController = new AppController(products, invoiceService, analyticsService);

                publish(new ProgressUpdate("Loading products...", 40)); // Changed to 40%
                ProductCatalog temp = ProductService.loadProducts();
                for (Product product : temp.getProducts())
                    products.addProduct(product);
                products.buildIndexes();

                publish(new ProgressUpdate("Loading users...", 70)); // Changed to 70%
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

                publish(new ProgressUpdate("Building UI...", 99));
                System.out.println("Time took to load data: " + stopWatch.elapsedTime());

                return new LoadedData(appController, products);
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
                    LoadedData data = get();

                    ShopView shopView = new ShopView(data.controller.getShopController(), data.catalog);
                    NavigationView navigationView = new NavigationView(data.controller.getNavigationController());
                    SidebarView sidebarView = new SidebarView(data.controller.getSidebarController());
                    AuthenticationView authenticationView = new AuthenticationView(
                            data.controller.getAuthenticationController());
                    UserProfileView userProfileView = new UserProfileView(data.controller.getProfileController());
                    ProductView productView = new ProductView(data.controller.getProductController(), data.catalog);
                    ShoppingCartView shoppingCartView = new ShoppingCartView(
                            data.controller.getShoppingCartController());
                    InvoiceView invoiceView = new InvoiceView(data.controller.getInvoiceController());
                    InvoiceDetailView invoiceDetailView = new InvoiceDetailView(data.controller.getInvoiceController());
                    SettingView settingView = new SettingView(data.controller.getSettingController());

                    MultiViewPanel multiViewPanel = new MultiViewPanel(shopView, authenticationView, userProfileView,
                            productView, shoppingCartView, invoiceView, invoiceDetailView, settingView);

                    data.controller.setViews(shopView, navigationView, sidebarView, multiViewPanel, authenticationView,
                            userProfileView, invoiceView, invoiceDetailView);

                    MainFrame mainFrame = new MainFrame(data.controller, sidebarView, navigationView, multiViewPanel);

                    data.catalog.updateView();

                    SwingUtilities.invokeLater(() -> {
                        mainFrame.setVisible(true);

                        SwingUtilities.invokeLater(() -> {
                            splash.dispose();
                        });
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();

    }

    private record ProgressUpdate(String status, int percent) {
    }

    private record LoadedData(AppController controller, ProductCatalog catalog) {
    }
}
