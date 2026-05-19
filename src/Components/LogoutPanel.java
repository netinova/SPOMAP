package Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LogoutPanel extends JPanel {

    private JLabel logoutLabel;

    public LogoutPanel() {
        this.setBackground(Color.YELLOW);
        this.setPreferredSize(new Dimension(0, 50));
        this.setLayout(new BorderLayout());

        logoutLabel = new JLabel("logout", SwingConstants.CENTER);
        logoutLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // importing and scaling icon
        ImageIcon logoutIcon = new ImageIcon("icons/logout.png");
        Image scaledIcon = logoutIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        logoutIcon = new ImageIcon(scaledIcon);

        logoutLabel.setIcon(logoutIcon);
        this.add(logoutLabel);
    }
}
