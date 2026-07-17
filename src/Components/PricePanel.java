package Components;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Util.ColorPalette;

public class PricePanel extends JPanel {

    private JLabel unitFinalLabel;
    private JLabel unitOriginalLabel;
    private JLabel discountBadge;

    private JLabel totalLabel;
    private JLabel originalTotalLabel;

    private double basePrice;
    private double discount;

    private static final Font UNIT_PRICE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private static final Font UNIT_ORIGINAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font DISCOUNT_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font TOTAL_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font ORIGINAL_TOTAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public PricePanel() {
        setupUI();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        removeAll();
        setBackground(ColorPalette.getInstance().getBgMain());
        setLayout(new BorderLayout(20, 0));
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        unitFinalLabel = new JLabel();
        unitFinalLabel.setFont(UNIT_PRICE_FONT);
        unitFinalLabel.setForeground(ColorPalette.getInstance().getAccentSuccess());
        leftPanel.add(unitFinalLabel);

        JPanel discountRow = new JPanel();
        discountRow.setLayout(new BoxLayout(discountRow, BoxLayout.X_AXIS));
        discountRow.setOpaque(false);
        discountRow.setAlignmentX(LEFT_ALIGNMENT);

        unitOriginalLabel = new JLabel();
        unitOriginalLabel.setFont(UNIT_ORIGINAL_FONT);
        unitOriginalLabel.setForeground(ColorPalette.getInstance().getTextMuted());
        discountRow.add(unitOriginalLabel);

        discountRow.add(Box.createHorizontalStrut(8));

        discountBadge = new JLabel();
        discountBadge.setFont(DISCOUNT_FONT);
        discountBadge.setForeground(ColorPalette.getInstance().getAccentWarning());
        discountRow.add(discountBadge);

        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(discountRow);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        totalLabel = new JLabel();
        totalLabel.setFont(TOTAL_FONT);
        totalLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        totalLabel.setAlignmentX(CENTER_ALIGNMENT);
        totalLabel.setHorizontalAlignment(JLabel.CENTER);

        originalTotalLabel = new JLabel();
        originalTotalLabel.setFont(ORIGINAL_TOTAL_FONT);
        originalTotalLabel.setForeground(ColorPalette.getInstance().getTextMuted());
        originalTotalLabel.setAlignmentX(CENTER_ALIGNMENT);
        originalTotalLabel.setHorizontalAlignment(JLabel.CENTER);

        centerPanel.add(totalLabel);
        centerPanel.add(Box.createVerticalStrut(4));
        centerPanel.add(originalTotalLabel);

        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
    }

    public void setPrice(double basePrice, double discount, int quantity) {
        this.basePrice = basePrice;
        this.discount = discount;
        updateQuantity(quantity);
    }

    public void updateQuantity(int quantity) {
        if (quantity <= 0)
            quantity = 1;

        double originalUnitTotal = basePrice * quantity;

        if (discount > 0) {
            double unitFinal = basePrice * (1.0 - discount / 100.0);
            double discountedTotal = unitFinal * quantity;

            unitFinalLabel.setText(String.format("$%.2f", unitFinal));
            unitOriginalLabel.setText("<html><strike>$" + String.format("%.2f", basePrice) + "</strike></html>");
            discountBadge.setText("-" + (int) discount + "%");

            totalLabel.setText(String.format("$%.2f", discountedTotal));
            originalTotalLabel
                    .setText("<html><strike>$" + String.format("%.2f", originalUnitTotal) + "</strike></html>");
        } else {
            unitFinalLabel.setText(String.format("$%.2f", basePrice));
            unitOriginalLabel.setText("");
            discountBadge.setText("");

            totalLabel.setText(String.format("$%.2f", originalUnitTotal));
            originalTotalLabel.setText("");
        }
    }

    public void clear() {
        unitFinalLabel.setText("");
        unitOriginalLabel.setText("");
        discountBadge.setText("");
        totalLabel.setText("");
        originalTotalLabel.setText("");
    }
}
