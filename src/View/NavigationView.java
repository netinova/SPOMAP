package View;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import Util.ColorPalette;

public class NavigationView extends JPanel {
    public NavigationView() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setPreferredSize(new Dimension(0, 50));
    }
}
