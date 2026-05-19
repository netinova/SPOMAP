package View;

import java.awt.Color;

import javax.swing.JPanel;

import Components.RoundedButton;

public class ShopView extends JPanel {
    public ShopView() {
        this.setBackground(Color.CYAN);
        this.add(new RoundedButton("button", 20));
    }
}
