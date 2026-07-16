package Components;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Util.ColorPalette;
import Util.UIUtils;

public class StockLabel extends JPanel {

    private JLabel textLabel;
    private static final Font FONT = new Font("Segoe UI", Font.BOLD, 14);

    public StockLabel() {
        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        this.setLayout(new BorderLayout());
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        textLabel = new JLabel();
        textLabel.setFont(FONT);
        textLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        this.add(textLabel, BorderLayout.WEST);
    }

    public void setStockQuantity(int stockQuantity, int availableQuantity) {
        if (stockQuantity > 0) {
            if (availableQuantity < stockQuantity) {
                textLabel.setText(
                        String.format("In Stock: %d  |  Available for Cart: %d", stockQuantity, availableQuantity));
            } else {
                textLabel.setText("In Stock: " + stockQuantity);
            }
            textLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        } else {
            textLabel.setText("Out of Stock");
            textLabel.setForeground(ColorPalette.getInstance().getAccentWarning());
        }
    }

    public void clear() {
        textLabel.setText("");
    }
}
