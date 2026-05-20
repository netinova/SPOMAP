package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Util.ColorPalette;

public class LogoPanel extends JPanel {

    private JLabel logoLabel;

    public LogoPanel() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setPreferredSize(new Dimension(0, 50));
        this.setLayout(new BorderLayout());

        logoLabel = new JLabel("SPOMAP", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setForeground(ColorPalette.TEXT_PRIMARY);

        // importing and scaling icon
        ImageIcon logoIcon = new ImageIcon("icons/shopping_cart.png");
        Image scaledIcon = logoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledIcon);

        logoLabel.setIcon(logoIcon);

        this.add(logoLabel);
    }
}
