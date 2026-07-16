package Components;

import Util.ColorPalette;
import Util.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class RoundedInputPassword extends JPasswordField {
    public int cornerRadius;
    private boolean activePlaceHolder;
    private String placeHolder;

    public RoundedInputPassword(String placeHolder, int size) {
        this.setFont(new Font("Arial", Font.PLAIN, 3 * size));
        this.setEchoChar((char) 0);
        this.setCaretColor(ColorPalette.getInstance().getTextPrimary());
        super.setPreferredSize(new Dimension(40 * size, 5 * size));
        this.setOpaque(false);
        this.placeHolder = placeHolder;
        this.cornerRadius = size * 5;
        this.setBorder(new EmptyBorder(5, 10, 5, 5));
        this.setMargin(new Insets(5, 10, 5, 5));
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setText(placeHolder);
        activePlaceHolder = true;
        // char[] Password = this.getPassword();
        this.setForeground(ColorPalette.getInstance().getTextPlaceholder());
        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                if (activePlaceHolder) {
                    setText("");
                    setEchoChar('•');
                    activePlaceHolder = false;
                    setForeground(ColorPalette.getInstance().getTextPrimary());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                String password = new String(getPassword());
                if (password.isEmpty()) {
                    setEchoChar((char) 0);
                    setText(placeHolder);
                    activePlaceHolder = true;
                    setForeground(ColorPalette.getInstance().getTextPlaceholder());
                }
            }
        });
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            this.setCaretColor(ColorPalette.getInstance().getTextPrimary());
            this.setBackground(ColorPalette.getInstance().getBgSecondary());
            if (activePlaceHolder) {
                this.setForeground(ColorPalette.getInstance().getTextPlaceholder());
            } else {
                this.setForeground(ColorPalette.getInstance().getTextPrimary());
            }
            revalidate();
            repaint();
        });
    }

    public void setActivePlaceHolder(boolean activePlaceHolder) {
        this.activePlaceHolder = activePlaceHolder;
        setEchoChar((char) 0);
        setText(placeHolder);
        activePlaceHolder = true;
        setForeground(ColorPalette.getInstance().getTextPlaceholder());
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setBackground(ColorPalette.getInstance().getBgTertiary());

        // Draw border
        g2.setColor(ColorPalette.getInstance().getBorder());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }
}
