package Components;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Util.ColorPalette;

public class StockLabel extends JPanel {

    private JLabel textLabel;
    private static final Font FONT = new Font("Segoe UI", Font.BOLD, 14);

    public StockLabel() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        textLabel = new JLabel();
        textLabel.setFont(FONT);
        textLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        this.add(textLabel, BorderLayout.WEST);
    }

    public void setStockQuantity(int quantity) {
        if (quantity > 0) {
            textLabel.setText("In Stock: " + quantity);
            textLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        } else {
            textLabel.setText("Out of Stock");
            textLabel.setForeground(ColorPalette.ACCENT_WARNING);
        }
    }

    public void clear() {
        textLabel.setText("");
    }
}
