package Controller;

import Model.Product;
import Model.ProductCatalog;
import Model.ProductService;
import View.ShopView;

public class ShopController {
    private ProductCatalog model;
    private ProductService service;

    public ShopController(ProductCatalog model) {
        this.model = model;
        this.service = new ProductService(model);
    }

    public void setView(ShopView view) {

    }

    public void handleProductClick(Product product) {
        // Business logic: select the product in the model
        model.setSelectedProduct(product);
        System.out.println("Product selected: " + product.getName() + " - Discount: " + product.getDiscount());
    }

    public ProductService getService() {
        return service;
    }

    public ProductCatalog getModel() {
        return model;
    }
}
