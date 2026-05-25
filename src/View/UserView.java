package View;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Util.ColorPalette;

public class UserView extends JPanel {
    public UserView() {
        this.setBackground(ColorPalette.BG_MAIN);
        JLabel label = new JLabel("user View");
        label.setForeground(ColorPalette.TEXT_PRIMARY);
        this.add(label);
    }
}
