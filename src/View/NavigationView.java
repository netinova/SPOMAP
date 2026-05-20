package View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;

import Components.RoundedInputText;
import Util.ColorPalette;

public class NavigationView extends JPanel {
    public NavigationView() {
        this.setLayout(new FlowLayout());
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setPreferredSize(new Dimension(0, 50));

        // search input
        RoundedInputText searchInput = new RoundedInputText("Search", 25, 5);
        this.add(searchInput);

        JLabel IconJLabel = new JLabel();
        ImageIcon searchLogo = new ImageIcon("icons/search.png");
        Image scaledIcon = searchLogo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        searchLogo = new ImageIcon(scaledIcon);
        IconJLabel.setIcon(searchLogo);

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));

        IconJLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        IconJLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!searchInput.getText().equals("") && !searchInput.getText().equals("Search"))
                    System.out.println(searchInput.getText());// Redirect to Shop item
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.add(IconJLabel);
    }
}
