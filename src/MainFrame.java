import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.*;

import Components.MainPanel;
import Controller.NavigationController;
import Controller.ShopController;
import Controller.SidebarController;
import Model.ProductCatalog;
import View.SidebarView;

public class MainFrame extends JFrame {

    private SidebarView sidebarView;
    private MainPanel mainPanel;
    ImageIcon iconProgram = new ImageIcon("icons/SPOMAP_BGblack_LogoWhite1000x1000.png");

    public MainFrame(ShopController shopController, ProductCatalog productCatalog,
            NavigationController navigationController) {

        sidebarView = new SidebarView(new SidebarController());
        mainPanel = new MainPanel(shopController, productCatalog, navigationController);

        setupUI();
    }

    private void setupUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SPOMAP");
        this.setIconImage(iconProgram.getImage());
        this.setSize(new Dimension(500, 300));
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(1000, 600));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0;
        gbc.weighty = 1;

        this.add(sidebarView, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;

        this.add(mainPanel, gbc);
    }
}
