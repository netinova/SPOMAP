package Components;

import Util.ColorPalette;
import Util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedPanel extends JPanel {
    private int borderRadius;
    private Color BGColor;
    private Color borderColor;

    public RoundedPanel(int borderRadius, Color BGColor, Color borderColor) {
        this.borderRadius = borderRadius;
        this.BGColor = BGColor;
        this.borderColor = borderColor;
        this.setOpaque(false);
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            revalidate();
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setColor(BGColor);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), borderRadius, borderRadius));

        // Draw border
        g2.setColor(borderColor);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius));

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

}
