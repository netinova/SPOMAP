package Components;

import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class RoundedInputText extends JTextField {

    public int cornerRadius;
    private boolean activePlaceHolder;

    public RoundedInputText(String placeHolder, int size) {
        this.setFont(new Font("Arial", Font.PLAIN, 3 * size));
        this.setCaretColor(ColorPalette.TEXT_PRIMARY);
        super.setPreferredSize(new Dimension(40 * size, 5 * size));
        this.setOpaque(false);
        this.cornerRadius = size * 5;
        this.setBorder(new EmptyBorder(5, 10, 5, 5));
        this.setMargin(new Insets(5, 10, 5, 5));
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setText(placeHolder);
        activePlaceHolder = true;
        this.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (activePlaceHolder) {
                    setText("");
                    activePlaceHolder = false;
                    setForeground(ColorPalette.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeHolder);
                    activePlaceHolder = true;
                    setForeground(ColorPalette.TEXT_PLACEHOLDER);
                }
            }
        });
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setBackground(ColorPalette.BG_TERTIARY);

        // Draw border
        g2.setColor(ColorPalette.BORDER);
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

}
