import java.util.Random;

import Controller.NavigationController;
import Controller.ShopController;
import Model.Product;
import Model.ProductCatalog;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);

        ProductCatalog products = new ProductCatalog();
        ShopController shopController = new ShopController();

        NavigationController navigationController = new NavigationController();

        MainFrame mainFrame = new MainFrame(shopController, products ,navigationController);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            products.addProduct(new Product("name", "", "", "", 0.0, random.nextDouble(0.0, 10.0)));
        }

        products.addProduct(new Product("name", "", "", "", 0.0, 0.0));

        mainFrame.setVisible(true);
    }
}
