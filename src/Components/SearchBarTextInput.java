package Components;

import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.RoundRectangle2D;

public class SearchBarTextInput extends JTextField {
    private int cornerRadius;
    private String placeHolder;
    private boolean activePlaceHolder;

    public SearchBarTextInput(String placeHolder ,int size) {
        this.setFont(new Font("Arial",Font.PLAIN,3*size));
        this.setCaretColor(ColorPalette.TEXT_PRIMARY);
        super.setPreferredSize(new Dimension(80*size,5*size));
        this.setOpaque(false);
        this.cornerRadius = size*5;
        this.placeHolder=placeHolder;
        this.setBorder(new EmptyBorder(5,0,5,0));
        this.setMargin(new Insets(5, 0, 5, 0));
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setText(placeHolder);
        activePlaceHolder=true;
        this.setForeground(ColorPalette.TEXT_PLACEHOLDER);

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if(activePlaceHolder)
                {
                    setText("");
                    activePlaceHolder=false;
                    setForeground(ColorPalette.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    setText(placeHolder);
                    activePlaceHolder=true;
                    setForeground(ColorPalette.TEXT_PLACEHOLDER);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        g2.setBackground(ColorPalette.BG_TERTIARY);

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

}

