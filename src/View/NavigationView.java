package View;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Components.IconNavbarLabel;
import Components.SearchFiled;
import Util.ColorPalette;

public class NavigationView extends JPanel {
    public NavigationView() {
        this.setLayout(new GridBagLayout());
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setPreferredSize(new Dimension(0, 50));
        this.setBorder(new EmptyBorder(5, 10, 5, 10));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(Box.createHorizontalGlue(), gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.add(new SearchFiled(), gbc);


        gbc.weightx = 1.0;
        this.add(Box.createHorizontalGlue(), gbc);

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        this.add(new IconNavbarLabel(), gbc);
        this.setMinimumSize(new Dimension(0, 50));

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));
    }
}

