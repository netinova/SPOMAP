package Components;

import Util.ColorPalette;

import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.EventListenerList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedInputText extends JTextField {

    public int cornerRadius;
    private boolean activePlaceHolder;
    private EventListenerList listenerList = new EventListenerList();
    private boolean isUpdatingPlaceholder = false;
    private String placeHolder;
    private Color colorBG;

    public interface EnterKeyListener {

        void onEnterPressed(String text);
    }

    private EnterKeyListener enterKeyListener;

    public RoundedInputText(String placeHolder, int size) {
        this.setFont(new Font("Arial", Font.PLAIN, 3 * size));
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
        this.setForeground(ColorPalette.getInstance().getTextPlaceholder());

        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!isUpdatingPlaceholder) {
                    fireSearchEvent();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!isUpdatingPlaceholder) {
                    fireSearchEvent();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!isUpdatingPlaceholder) {
                    fireSearchEvent();
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (enterKeyListener != null) {
                        enterKeyListener.onEnterPressed(getText());
                    }
                }
            }
        });

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (activePlaceHolder) {
                    isUpdatingPlaceholder = true;
                    setText("");
                    isUpdatingPlaceholder = false;
                    activePlaceHolder = false;
                    setForeground(ColorPalette.getInstance().getTextPrimary());
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    isUpdatingPlaceholder = true;
                    setText(placeHolder);
                    isUpdatingPlaceholder = false;
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

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    public void setEnterKeyListener(EnterKeyListener listener) {
        this.enterKeyListener = listener;
    }

    private void fireActionEvent() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getText());
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    public void setColorBG(Color colorBG) {
        this.colorBG = colorBG;
    }

    public void setActivePlaceHolder(boolean activePlaceHolder) {
        this.activePlaceHolder = activePlaceHolder;
        isUpdatingPlaceholder = true;
        setText(placeHolder);
        isUpdatingPlaceholder = false;
        setForeground(ColorPalette.getInstance().getTextPlaceholder());
    }

    private void fireSearchEvent() {
        fireActionEvent();
    }

    public void setCornerRadius(int cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw rounded background
        if (colorBG != null) {
            g2.setColor(colorBG);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        }

        // Draw border
        g2.setColor(ColorPalette.getInstance().getBorder());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();

        // Paint the text
        super.paintComponent(g);
    }

}
