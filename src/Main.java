import java.util.Random;

import Controller.ShopController;
import Model.Product;
import Model.ProductCatalog;

public class Main {
    public static void main(String[] args) {

        ProductCatalog products = new ProductCatalog();
        ShopController shopController = new ShopController();

        MainFrame mainFrame = new MainFrame(shopController, products);

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            products.addProduct(new Product("name", "", "", "", 0.0, random.nextDouble(0.0, 10.0)));
        }

        products.addProduct(new Product("name", "", "", "", 0.0, 0.0));

        mainFrame.setVisible(true);
    }
}
