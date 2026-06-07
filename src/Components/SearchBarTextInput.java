package Components;

import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.EventListenerList;

import Model.AppState;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

public class SearchBarTextInput extends JTextField {

    private boolean activePlaceHolder;
    private EventListenerList listenerList = new EventListenerList();
    private boolean isUpdatingPlaceholder = false;

    public interface EnterKeyListener {
        void onEnterPressed(String text);
    }

    private EnterKeyListener enterKeyListener;

    public SearchBarTextInput(String placeHolder, int size) {

        this.setFont(new Font("Arial", Font.PLAIN, 3 * size));
        this.setCaretColor(ColorPalette.TEXT_PRIMARY);
        super.setPreferredSize(new Dimension(80 * size, 5 * size));
        this.setOpaque(false);
        this.setBorder(new EmptyBorder(5, 0, 5, 0));
        this.setMargin(new Insets(5, 0, 5, 0));
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setText(placeHolder);
        activePlaceHolder = true;
        this.setForeground(ColorPalette.TEXT_PLACEHOLDER);

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
                    setForeground(ColorPalette.TEXT_PRIMARY);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getText().isEmpty()) {
                    isUpdatingPlaceholder = true;
                    setText(placeHolder);
                    isUpdatingPlaceholder = false;
                    activePlaceHolder = true;
                    setForeground(ColorPalette.TEXT_PLACEHOLDER);
                }
            }

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

    private void fireSearchEvent() {
        fireActionEvent();
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
