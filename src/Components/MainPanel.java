package Components;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import View.NavigationView;
import View.ShopView;

public class MainPanel extends JPanel {

    public MainPanel() {
        this.setLayout(new GridBagLayout());

        NavigationView navigationPanel = new NavigationView();
        ShopView shopPanel = new ShopView();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 0;

        this.add(navigationPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;

        this.add(shopPanel, gbc);
    }

}
