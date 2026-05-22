package Components;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Util.ColorPalette;

public class ProductCard extends JPanel {
    private ImageIcon productImage;
    private JLabel productName;
    private JLabel productPrice;

    public ProductCard(String productImageLoc, String productName, double productPrice) {

        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER));
        this.setPreferredSize(new Dimension(200, 230));
    }
}
