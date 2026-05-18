import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class MainPanel extends JPanel {

    public MainPanel() {
        this.setLayout(new GridBagLayout());

        NavigationPanel navigationPanel = new NavigationPanel();
        ShopPanel shopPanel = new ShopPanel();

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
