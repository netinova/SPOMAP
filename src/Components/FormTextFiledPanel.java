package Components;

import Util.ColorPalette;

import javax.swing.*;
import java.awt.*;

public class FormTextFiledPanel extends JPanel {

    public FormTextFiledPanel(String title, JComponent component) {
        setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(300,70));

        //Label
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(ColorPalette.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(Box.createVerticalStrut(5));
        this.add(label);
        this.add(Box.createVerticalStrut(5));
        // Inputs
        this.add(component);

    }
}
