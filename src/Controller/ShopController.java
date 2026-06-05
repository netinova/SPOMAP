package Controller;

import Components.MultiViewPanel;
import Model.Product;
import Model.ProductCatalog;
import View.ShopView;

public class ShopController {
    private ProductCatalog model;

    private OnChangeViewListener listener;

    public void setOnChangeViewListener(OnChangeViewListener listener) {
        this.listener = listener;
    }

    public ShopController(ProductCatalog model) {
        this.model = model;
    }

    public void setView(ShopView view) {

    }

    public void handleProductClick(Product product) {

        model.setSelectedProduct(product);

        if (listener != null) {
            listener.changeView(MultiViewPanel.PRODUCT_VIEW);
        }

    }
}
