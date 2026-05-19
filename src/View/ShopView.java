package View;
import java.awt.Color;
import javax.swing.JPanel;
import Components.FlatButton;

public class ShopView extends JPanel {
    public ShopView() {
        this.setBackground(Color.CYAN);

        FlatButton button = new FlatButton("dani");
        button.addActionListener(e -> {
            System.out.println("asd");
        });

        this.add(button);
    }
}
