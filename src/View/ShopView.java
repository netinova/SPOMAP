package View;

import java.awt.Color;

import javax.swing.JPanel;

import Components.RoundedButton;
import Util.ColorPalette;

public class ShopView extends JPanel {
    public ShopView() {
        this.setBackground(ColorPalette.BG_MAIN);
        this.add(new RoundedButton("button", 20));
    }
}
