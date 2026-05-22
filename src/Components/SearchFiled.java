package Components;

import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;

public class SearchFiled extends JPanel {

    private int cornerRadius=45;

    public SearchFiled(){
        setOpaque(false);

        this.setBackground(ColorPalette.BG_SECONDARY);

        // search input
        SearchBarTextInput searchInput = new SearchBarTextInput("Search", 5);

        // search Icon
        JLabel IconJLabel = new JLabel();
        ImageIcon searchLogo = new ImageIcon("icons/search.png");
        Image scaledIcon = searchLogo.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        searchLogo = new ImageIcon(scaledIcon);
        IconJLabel.setIcon(searchLogo);
        IconJLabel.setBorder(new EmptyBorder(0,0,0,0));

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
                if (searchInput.getText().isEmpty() && !searchInput.getText().equals("Search"))
                    System.out.println(searchInput.getText());// Redirect to Shop item
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        // comboBox
        String[] searchOption = {"Free Search", "Color" , "Creator Name"};
        JComboBox<String> searchType = new JComboBox<String>(searchOption);
        searchType.setUI(new BasicComboBoxUI() {
             @Override
             protected JButton createArrowButton() {
                 JButton button = new JButton("▼");
                 button.setForeground(ColorPalette.TEXT_PRIMARY);
                 button.setBorder(null);
                 button.setContentAreaFilled(false);
                 return button;
             }
        });
        searchType.setBackground(ColorPalette.BG_SECONDARY);
        searchType.setForeground(ColorPalette.TEXT_PRIMARY);
        searchType.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchType.setOpaque(false);
        searchType.setBorder(null);
        searchType.setFocusable(false);

        this.add(IconJLabel);
        this.add(searchInput);
        this.add(searchType);
    }
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);//soft render

        // Draw border
        g2.setColor(ColorPalette.BORDER);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }
}