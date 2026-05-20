package Components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LogoPanel extends JPanel {

    private JLabel logoLabel;

    public LogoPanel() {
        this.setBackground(Color.ORANGE);
        this.setPreferredSize(new Dimension(0, 50));
        this.setLayout(new BorderLayout());

        logoLabel = new JLabel("SPOMAP", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));

        // importing and scaling icon
        ImageIcon logoIcon = new ImageIcon("icons/shopping_cart.png");
        Image scaledIcon = logoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledIcon);

        logoLabel.setIcon(logoIcon);


        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("2");// Redirect to Home page
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.add(logoLabel);
    }
}
