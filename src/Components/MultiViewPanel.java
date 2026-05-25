package Components;

import java.awt.CardLayout;

import javax.swing.JPanel;

import Controller.ShopController;
import Model.ProductCatalog;
import View.ShopView;

public class MultiViewPanel extends JPanel {

    private ShopView shopView;
    private CardLayout cardLayout;

    public MultiViewPanel(ShopController shopController, ProductCatalog productCatalog) {

        shopView = new ShopView(shopController, productCatalog);

        setupUI();

    }

    private void setupUI() {
        cardLayout = new CardLayout();
        this.setLayout(cardLayout);

        this.add(shopView, "shopView");
    }

}
