package View;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Util.ColorPalette;

public class NavigationView extends JPanel {
    public NavigationView() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setPreferredSize(new Dimension(0, 50));
        this.setMinimumSize(new Dimension(0, 50));

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));
    }
}
