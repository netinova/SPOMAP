package Components;

import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Model.Product;
import Model.ProductColor;
import Util.ColorPalette;

/**
 * A collapsible panel that shows all technical specifications of a Product,
 * including manufacturer and colors, sorted alphabetically.
 */
public class TechnicalSpecsPanel extends JPanel {

    private JPanel specsInnerPanel;
    private JLabel titleLabel;

    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font ROW_FONT = new Font("Segoe UI", Font.PLAIN, 13);

    public TechnicalSpecsPanel() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        this.setVisible(true);
        // No setPreferredSize – lets container size to content

        // Title
        titleLabel = new JLabel("TECHNICAL SPECIFICATIONS");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        this.add(titleLabel, BorderLayout.NORTH);

        // Inner grid panel (expands in CENTER)
        specsInnerPanel = new JPanel(new GridBagLayout());
        specsInnerPanel.setBackground(ColorPalette.BG_SECONDARY);
        specsInnerPanel.setBorder(BorderFactory.createLineBorder(ColorPalette.BORDER, 1));
        this.add(specsInnerPanel, BorderLayout.CENTER);
    }

    public void setProduct(Product product) {
        specsInnerPanel.removeAll();

        if (product == null) {
            setVisible(false);
            return;
        }

        TreeMap<String, String> allSpecs = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        Map<String, String> techSpecs = product.getTechnicalSpecs();
        if (techSpecs != null) {
            allSpecs.putAll(techSpecs);
        }

        String manufacturer = product.getManufacturer();
        if (manufacturer != null && !manufacturer.trim().isEmpty()) {
            allSpecs.put("Manufacturer", manufacturer.trim());
        }

        ProductColor[] colors = product.getColors();
        if (colors != null && colors.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < colors.length; i++) {
                if (i > 0)
                    sb.append(", ");
                sb.append(colors[i].name());
            }
            allSpecs.put("Color", sb.toString());
        }

        if (allSpecs.isEmpty()) {
            setVisible(false);
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally only
        gbc.insets = new Insets(0, 0, 0, 0);

        int row = 0;
        boolean oddRow = true;

        for (Map.Entry<String, String> entry : allSpecs.entrySet()) {
            JLabel keyLabel = new JLabel(entry.getKey() + ":");
            keyLabel.setFont(ROW_FONT);
            keyLabel.setForeground(ColorPalette.TEXT_PRIMARY);

            JLabel valueLabel = new JLabel(entry.getValue());
            valueLabel.setFont(ROW_FONT);
            valueLabel.setForeground(ColorPalette.TEXT_MUTED);

            Color rowColor = oddRow ? ColorPalette.BG_TERTIARY : ColorPalette.BG_SECONDARY;
            keyLabel.setOpaque(true);
            keyLabel.setBackground(rowColor);
            valueLabel.setOpaque(true);
            valueLabel.setBackground(rowColor);

            // Key column
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0.3;
            gbc.weighty = 0.0;
            keyLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
            specsInnerPanel.add(keyLabel, gbc);

            // Value column
            gbc.gridx = 1;
            gbc.weightx = 0.7;
            gbc.weighty = 0.0;
            valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            specsInnerPanel.add(valueLabel, gbc);

            oddRow = !oddRow;
            row++;
        }

        setVisible(true);
    }
}
