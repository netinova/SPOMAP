package Components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.*;

import Util.ColorPalette;

public class LogoPanel extends JPanel {

    public interface LogoListener{
        void onClickLogo();
    }
    private LogoListener listener;

    public void setListener(LogoListener listener) {
        this.listener = listener;
    }

    private JLabel logoLabel;
    RoundedButton buttonTimer = new RoundedButton("",15);


    public LogoPanel() {
        setupUI();
        createComponents();
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
    }

    private void createComponents() {

        this.add(Box.createVerticalStrut(15));

        // button Timer
        buttonTimer.setEnabled(false);
        buttonTimer.add(new LiveClockSidebar());
        buttonTimer.setAlignmentX(CENTER_ALIGNMENT);
        buttonTimer.setPreferredSize(new Dimension(200 , 40));
        this.add(buttonTimer);

        this.add(Box.createVerticalStrut(5));
        // importing and scaling icon
        ImageIcon logoIcon = new ImageIcon("icons/SPOMAP_Default_White color.png");
        Image scaledIcon = logoIcon.getImage().getScaledInstance(175, 175, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(scaledIcon);
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(logoIcon);
        iconLabel.setAlignmentX(CENTER_ALIGNMENT);

        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(listener!=null)
                    listener.onClickLogo();
                // Redirect to Home page
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });

        this.add(iconLabel);

        this.add(Box.createVerticalStrut(60));

//        this.add(logoLabel);
    }
}
