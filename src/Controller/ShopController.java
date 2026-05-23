package Controller;

import Model.Product;

public class ShopController {

    public void handleProductClick(Product product) {
        System.out.println(product.getDiscount());
    }
}
