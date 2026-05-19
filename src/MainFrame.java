import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

import Components.MainPanel;
import View.SidebarView;

public class MainFrame extends JFrame {

    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(500, 300));
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(500, 300));

        SidebarView sidebarView = new SidebarView();
        MainPanel mainPanel = new MainPanel();

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
