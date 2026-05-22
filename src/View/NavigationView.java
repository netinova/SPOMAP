package View;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;

import Components.RoundedInputText;
import Components.SearchFiled;
import Util.ColorPalette;

public class NavigationView extends JPanel {
    public NavigationView() {
        this.setLayout(new FlowLayout());
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setPreferredSize(new Dimension(0, 50));
        this.add(new SearchFiled());
    }
}
