package Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;

public class FlatButton extends JButton {

    private int cornerRadius = 20; // Adjust for more/less rounding

    public FlatButton(String text) {

        // super(text);
        // this.setText(text);
        // this.setBackground(Color.PINK);
        // this.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        // this.setPreferredSize(new Dimension(100, 30));
        // this.setFocusable(false);

        super(text);
        this.setContentAreaFilled(false); // Make background transparent
        this.setFocusPainted(false);
        this.setBorder(new EmptyBorder(10, 20, 10, 20));
        this.setPreferredSize(new Dimension(100, 40));
        this.setBackground(Color.RED);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        if (getModel().isPressed()) {
            g2.setColor(Color.PINK.darker());
        } else if (getModel().isRollover()) {
            g2.setColor(Color.PINK.brighter());
        } else {
            g2.setColor(getBackground());
        }

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw border
        g2.setColor(Color.BLUE);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

}
