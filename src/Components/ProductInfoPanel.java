package Components;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import Util.ColorPalette;

public class ProductInfoPanel extends JPanel {

    private JLabel nameLabel;
    private JTextArea descriptionArea; // using JTextArea for better text wrapping

    private static final Font NAME_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font DESC_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final int DESCRIPTION_MAX_WIDTH = 500;

    public ProductInfoPanel() {
        setupUI();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        removeAll();
        this.setBackground(ColorPalette.getInstance().getBgMain());
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        nameLabel = new JLabel("Product Name");
        nameLabel.setFont(NAME_FONT);
        nameLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        nameLabel.setAlignmentX(LEFT_ALIGNMENT);
        this.add(nameLabel);

        this.add(Box.createVerticalStrut(8));

        descriptionArea = new JTextArea("Product description...");
        descriptionArea.setFont(DESC_FONT);
        descriptionArea.setForeground(ColorPalette.getInstance().getTextMuted());
        descriptionArea.setBackground(ColorPalette.getInstance().getBgMain());
        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setAlignmentX(LEFT_ALIGNMENT);
        descriptionArea.setBorder(null);
        descriptionArea.setMaximumSize(new Dimension(DESCRIPTION_MAX_WIDTH,
                Integer.MAX_VALUE));
        descriptionArea.setPreferredSize(new Dimension(DESCRIPTION_MAX_WIDTH, 50));

        this.add(descriptionArea);
    }

    public void updateInfo(String name, String description) {
        nameLabel.setText(name.isEmpty() ? " " : name);
        descriptionArea.setText(description == null ? "" : description);
    }
}
