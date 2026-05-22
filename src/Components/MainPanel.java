package Components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import Model.ProductCatalog;
import View.NavigationView;
import View.ShopView;

public class MainPanel extends JPanel {

    private NavigationView navigationView;
    private ShopView shopView;

    public MainPanel(ProductCatalog productCatalog) {

        shopView = new ShopView(productCatalog);

        navigationView = new NavigationView();

        setupUI();
    }

    private void setupUI() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0;

        this.add(navigationView, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;

        this.add(shopView, gbc);
    }

}
