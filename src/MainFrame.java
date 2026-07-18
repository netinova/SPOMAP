import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import Components.MainPanel;
import Components.MultiViewPanel;
import Controller.AppController;
import View.NavigationView;
import View.SidebarView;
import Util.ColorPalette;
import Util.UIUtils;

public class MainFrame extends JFrame {

    private SidebarView sidebarView;
    private MainPanel mainPanel;
    private ImageIcon iconProgram;

    public MainFrame(AppController appController, SidebarView sidebarView,
            NavigationView navigationView, MultiViewPanel multiViewPanel) {

        this.sidebarView = sidebarView;
        this.mainPanel = new MainPanel(appController, navigationView, multiViewPanel);

        iconProgram = UIUtils.loadAndScaleSVG("icons/logo_svg/SPOMAP_BGblack_LogoWhite1000x1000.svg",
                30, 30,
                null);
        this.setIconImage(iconProgram.getImage());

        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            iconProgram = UIUtils.loadAndScaleSVG("icons/logo_svg/SPOMAP_BGblack_LogoWhite1000x1000.svg",
                    30, 30,
                    null);
            this.setIconImage(iconProgram.getImage());
        });
    }

    private void setupUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("SPOMAP");

        this.setSize(new Dimension(500, 300));
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(1100, 700));

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
