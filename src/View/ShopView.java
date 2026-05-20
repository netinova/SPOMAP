package View;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Components.RoundedButton;
import Util.ColorPalette;

public class ShopView extends JPanel {
    public ShopView() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.add(new RoundedButton("button", 20));

        // border
        Border line = BorderFactory.createLineBorder(ColorPalette.BORDER);
        Border etched = BorderFactory.createEtchedBorder();
        this.setBorder(BorderFactory.createCompoundBorder(line, etched));
    }
}
