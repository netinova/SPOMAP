package Components;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

import Util.ColorPalette;

public class RoundedButton extends JButton {

    private int cornerRadius;
    private boolean hasBorder = true;

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public RoundedButton(String text, int cornerRadius) {
        super(text == null ? "" : text);
        this.cornerRadius = cornerRadius;
        this.setContentAreaFilled(false); // Make background transparent
        this.setFocusPainted(false);
        this.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.setPreferredSize(new Dimension(100, 40));
        this.setForeground(ColorPalette.TEXT_PRIMARY);
        this.setBackground(ColorPalette.BG_TERTIARY);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        if (getModel().isPressed()) {
            g2.setColor(ColorPalette.BUTTON_PRESSED);
        } else if (getModel().isRollover()) {
            g2.setColor(ColorPalette.BUTTON_HOVER);
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw border

        if (hasBorder) {
            g2.setColor(ColorPalette.BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

}
