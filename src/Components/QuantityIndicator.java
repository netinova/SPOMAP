package Components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JComponent;

import Util.ColorPalette;

public class QuantityIndicator extends JComponent {

    private static final Font QTY_FONT = new Font("Segoe UI", Font.BOLD, 16);

    private int radius;
    private int quantity;

    public QuantityIndicator(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    public void setQuantity(int q) {
        this.quantity = q;
        repaint();
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(ColorPalette.BG_TERTIARY);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.setColor(ColorPalette.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);

        g2.setFont(QTY_FONT);
        g2.setColor(ColorPalette.TEXT_PRIMARY);
        String text = String.valueOf(quantity);
        int textWidth = g2.getFontMetrics().stringWidth(text);
        int textHeight = g2.getFontMetrics().getAscent();
        g2.drawString(text,
                (getWidth() - textWidth) / 2,
                (getHeight() + textHeight) / 2 - 2);
        g2.dispose();
    }
}
