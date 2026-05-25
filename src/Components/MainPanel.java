package Components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import Controller.NavigationController;
import Controller.ShopController;
import Model.ProductCatalog;
import View.NavigationView;

public class MainPanel extends JPanel {

    private NavigationView navigationView;
    private MultiViewPanel multiViewPanel;

    public MainPanel(ShopController shopController, ProductCatalog productCatalog,
            NavigationController navigationController) {

        navigationView = new NavigationView(navigationController);

        multiViewPanel = new MultiViewPanel(shopController, productCatalog, navigationController);

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

        this.add(multiViewPanel, gbc);
    }
}
