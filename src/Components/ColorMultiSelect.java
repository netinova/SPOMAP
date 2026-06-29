package Components;

import Model.ProductColor;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class ColorMultiSelect extends JPanel {

    private final RoundedPanel displayField;
    private final JLabel displayLabel;
    private final JWindow dropdownWindow;
    private final RoundedPanel dropdownPanel;
    private final Set<ProductColor> selectedColors = new LinkedHashSet<>();
    private final Map<ProductColor, JPanel> rowMap = new LinkedHashMap<>();
    private boolean dropdownOpen = false;

    public ColorMultiSelect() {
        setOpaque(false);
        setLayout(new BorderLayout());

        displayField = new RoundedPanel(30,ColorPalette.BG_SECONDARY, ColorPalette.BORDER);
        displayField.setBorder(new EmptyBorder(6, 14, 6, 10));
        displayField.setPreferredSize(new Dimension(0, 40));
        displayField.setCursor(new Cursor(Cursor.HAND_CURSOR));

        displayLabel = new JLabel("Select colors...");
        displayLabel.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        displayField.add(displayLabel, BorderLayout.CENTER);

        add(displayField, BorderLayout.CENTER);

        //color window
        dropdownWindow = new JWindow();
        dropdownWindow.setBackground(new Color(0, 0, 0, 0));
        dropdownWindow.getContentPane().setBackground(new Color(0, 0, 0, 0));

        dropdownPanel = new RoundedPanel(30,ColorPalette.BG_TERTIARY,ColorPalette.BORDER);
        dropdownPanel.setLayout(new BoxLayout(dropdownPanel, BoxLayout.Y_AXIS));
        dropdownPanel.setBorder(new EmptyBorder(8, 10, 8, 10));

        // color rows
        for (ProductColor color : ProductColor.values()) {
            JPanel row = createRow(color);
            dropdownPanel.add(row);
            dropdownPanel.add(Box.createVerticalStrut(8));
            rowMap.put(color, row);
        }

        //scroll pane
        JScrollPane scrollPane = new JScrollPane(dropdownPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        JScrollBar verticalBar = scrollPane.getVerticalScrollBar();
        styleScrollBar(verticalBar);

        dropdownWindow.add(scrollPane);

        // handle window listener
        displayField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (dropdownOpen) closeDropdown();
                else openDropdown();
            }
        });

        // Close when clicked outside
        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEventListener) event -> {
            if (dropdownOpen && event instanceof MouseEvent me) {
                if (me.getID() == MouseEvent.MOUSE_PRESSED) {
                    Point p = me.getLocationOnScreen();
                    Rectangle bounds = new Rectangle(
                            dropdownWindow.getLocationOnScreen().x,
                            dropdownWindow.getLocationOnScreen().y,
                            dropdownWindow.getWidth(),
                            dropdownWindow.getHeight()
                    );
                    if (!bounds.contains(p))
                        closeDropdown();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
    }

    private JPanel createRow(ProductColor color) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(ColorPalette.BG_TERTIARY);
        row.setBorder(new EmptyBorder(4, 4, 4, 4));
        row.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));

        //left side
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        left.setOpaque(false);

        JPanel colorDot = getJPanel(color);

        JLabel nameLabel = new JLabel(color.name());
        nameLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        left.add(colorDot);
        left.add(nameLabel);
        row.add(left, BorderLayout.WEST);

        //right side(selection)
        JLabel checkmark = new JLabel("");
        checkmark.setForeground(ColorPalette.SELECTION_BG);
        checkmark.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        checkmark.setBorder(new EmptyBorder(0, 0, 0, 0));
        row.add(checkmark, BorderLayout.EAST);
        row.putClientProperty("checkmark", checkmark);

        // Click to toggle
        row.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleColor(color);
            }
        });

        return row;
    }

    private JPanel getJPanel(ProductColor color) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color.getProductColor());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(ColorPalette.BORDER);
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(15, 15));
        return panel;
    }

    private void toggleColor(ProductColor color) {
        if (selectedColors.contains(color)) { //search color in list
            selectedColors.remove(color);
        } else {
            selectedColors.add(color);
        }
        updateRows();
        updateDisplay();
    }

    private void updateRows() {
        for (Map.Entry<ProductColor, JPanel> entry : rowMap.entrySet()) {
            ProductColor color = entry.getKey();
            JPanel row = entry.getValue();
            JLabel checkmark = (JLabel) row.getClientProperty("checkmark");
            boolean isSelected = selectedColors.contains(color);
            checkmark.setText((isSelected)? "◄" : "");
            // Highlight selected row with a subtle background
            row.setBackground(isSelected ? ColorPalette.BG_SECONDARY : ColorPalette.BG_TERTIARY);
        }
        dropdownPanel.repaint();
    }

    private void updateDisplay() {
        if (selectedColors.isEmpty()) {
            displayLabel.setText("Select colors...");
            displayLabel.setForeground(ColorPalette.TEXT_PLACEHOLDER);
        } else {
            List<String> names = new ArrayList<>();
            for (ProductColor color : selectedColors)
                names.add(color.name());
            displayLabel.setText(String.join(", ", names));
            displayLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        }
    }

    private void openDropdown() {
        Point loc = displayField.getLocationOnScreen();
        dropdownWindow.setLocation(loc.x, loc.y + displayField.getHeight() + 4);
        dropdownWindow.setSize(displayField.getWidth(), 220);
        dropdownWindow.setVisible(true);
        dropdownOpen = true;
        updateRows(); // sync selection state on open
    }

    private void closeDropdown() {
        dropdownWindow.setVisible(false);
        dropdownOpen = false;
    }

    public ProductColor[] getSelectedColors() {
        return selectedColors.toArray(new ProductColor[0]);
    }

    public void resetPanel() {
        selectedColors.clear();
        rowMap.clear();//TODO:check have problem or not
        updateRows();
        updateDisplay();
    }
    private void styleScrollBar(JScrollBar bar) {

        bar.setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.trackColor = ColorPalette.BG_MAIN;
                this.thumbColor = ColorPalette.BG_TERTIARY;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                if (thumbBounds.isEmpty() || !scrollbar.isEnabled())
                    return;

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Round the corners of the thumb
                int arc = 8;
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width - 1, thumbBounds.height - 1, arc, arc);

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(trackColor);
                g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
                g2.dispose();
            }
        });
        bar.setPreferredSize(new Dimension(8, 0));
        bar.setUnitIncrement(16);

    }
}