package Components;

import Util.ColorPalette;

import javax.swing.JProgressBar;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class RoundedProgressBar extends JProgressBar {

    private int cornerRadius;
    private Color trackColor = ColorPalette.getInstance().getBgTertiary();
    private Color fillColor = ColorPalette.getInstance().getAccentPrimary();
    private Color textColor = ColorPalette.getInstance().getTextPrimary();

    public RoundedProgressBar(int cornerRadius) {
        super(0, 100);
        this.cornerRadius = cornerRadius;

        setOpaque(false);
        setBorderPainted(false);
        setStringPainted(true);
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setForeground(fillColor);
        setBackground(trackColor);
    }

    public void setTrackColor(Color trackColor) {
        this.trackColor = trackColor;
        repaint();
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
        repaint();
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        repaint();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // BG
        g2.setColor(trackColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius));

        // Fill
        double percent = (getValue() - getMinimum()) / (double) (getMaximum() - getMinimum());
        int fillWidth = (int) Math.round(width * percent);

        if (fillWidth > 0) {
            Shape oldClip = g2.getClip();
            g2.clip(new RoundRectangle2D.Double(0, 0, width, height, cornerRadius, cornerRadius));
            g2.setColor(fillColor);
            g2.fillRect(0, 0, fillWidth, height);
            g2.setClip(oldClip);
        }

        // Border
        g2.setColor(ColorPalette.getInstance().getBorder());
        g2.draw(new RoundRectangle2D.Double(0, 0, width - 1, height - 1, cornerRadius, cornerRadius));

        // Percent text
        if (isStringPainted()) {
            String text = getString() != null ? getString() : (int) (percent * 100) + "%";
            g2.setFont(getFont());
            g2.setColor(textColor);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            int textX = (width - textWidth) / 2;
            int textY = (height + fm.getAscent() - fm.getDescent()) / 2;
            g2.drawString(text, textX, textY);
        }

        g2.dispose();
    }
}