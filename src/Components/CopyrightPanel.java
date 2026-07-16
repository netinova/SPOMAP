package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Util.ColorPalette;
import Util.UIUtils;

public class CopyrightPanel extends JPanel {

    private JLabel logoutLabel;

    public CopyrightPanel() {
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
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setPreferredSize(new Dimension(0, 50));
        this.setLayout(new BorderLayout());

        logoutLabel = new JLabel("SPOMAP ©", SwingConstants.CENTER);
        logoutLabel.setFont(new Font("Arial", Font.BOLD, 10));
        logoutLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
        logoutLabel.setForeground(ColorPalette.getInstance().getTextMuted());

        this.add(logoutLabel);
    }
}
