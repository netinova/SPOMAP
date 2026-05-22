import Model.Product;
import Model.ProductCatalog;

public class Main {
    public static void main(String[] args) {

        ProductCatalog products = new ProductCatalog();

        MainFrame mainFrame = new MainFrame(products);

        for (int i = 0; i < 10; i++) {
            products.addProduct(new Product("", "", "", "", 0.0));
        }

        mainFrame.setVisible(true);
    }
}
