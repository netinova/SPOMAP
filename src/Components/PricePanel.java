package Components;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Util.ColorPalette;

public class PricePanel extends JPanel {

    private JLabel finalPriceLabel;
    private JLabel originalPriceLabel;
    private JLabel discountLabel;

    private double price;
    private double discount;

    public void updateQuantity(int quantity) {
        setPrice(price, discount, quantity);
    }

    private static final Font FINAL_PRICE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font ORIGINAL_PRICE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    private static final Font DISCOUNT_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public PricePanel() {
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Final price
        finalPriceLabel = new JLabel();
        finalPriceLabel.setFont(FINAL_PRICE_FONT);
        finalPriceLabel.setForeground(ColorPalette.ACCENT_SUCCESS);
        finalPriceLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(finalPriceLabel);

        // Row for original price and discount
        JPanel discountRow = new JPanel();
        discountRow.setLayout(new BoxLayout(discountRow, BoxLayout.X_AXIS));
        discountRow.setOpaque(false);
        discountRow.setAlignmentX(LEFT_ALIGNMENT);

        originalPriceLabel = new JLabel();
        originalPriceLabel.setFont(ORIGINAL_PRICE_FONT);
        originalPriceLabel.setForeground(ColorPalette.TEXT_MUTED);
        discountRow.add(originalPriceLabel);

        discountRow.add(Box.createHorizontalStrut(8));

        discountLabel = new JLabel();
        discountLabel.setFont(DISCOUNT_FONT);
        discountLabel.setForeground(ColorPalette.ACCENT_WARNING);
        discountRow.add(discountLabel);

        add(Box.createVerticalStrut(4));
        add(discountRow);
    }

    public void setPrice(double basePrice, double discount, int quantity) {

        this.price = basePrice;
        this.discount = discount;

        if (this.discount > 0) {
            double finalPrice = this.price * (1.0 - this.discount / 100.0) * quantity;
            finalPriceLabel.setText(String.format("$%.2f", finalPrice));
            originalPriceLabel
                    .setText("<html><strike>$" + String.format("%.2f", this.price * quantity) + "</strike></html>");
            discountLabel.setText("-" + (int) this.discount + "%");
        } else {
            finalPriceLabel.setText(String.format("$%.2f", this.price * quantity));
            originalPriceLabel.setText("");
            discountLabel.setText("");
        }
    }

    /** Clear the display (show nothing). */
    public void clear() {
        finalPriceLabel.setText("");
        originalPriceLabel.setText("");
        discountLabel.setText("");
    }
}
