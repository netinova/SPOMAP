package Components;

import Util.ColorPalette;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Font;

public class LiveJLabelNumber extends JLabel {
    private int target;
    private double targetDouble=0;
    private int current = 0;
    private Timer timer;
    private String defaultString="";

    public LiveJLabelNumber(String text){
        this(Integer.parseInt(text));
    }

    public LiveJLabelNumber(int target) {
        this.target = target;
        setupUI();
        startCounting();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        setOpaque(false);
        setFont(new Font("Arial", Font.PLAIN, 13));
        setForeground(ColorPalette.getInstance().getTextPrimary());
        setText("0");
    }

    public void setTarget(int target) {
        this.target = target;
        startCounting();
    }
    public void setTarget(double target) {
        this.targetDouble = target;
        this.target = (int) target;
        startCounting();
    }

    public void setDefaultString(String defaultString) {
        this.defaultString = defaultString;
    }

    public void startCounting() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        current = 0;
        setText(defaultString+"0");

        if (target <= 0) return;

        //calculate steps
        int steps = Math.min(target, 30);
        int increment = target / steps;
        int delay = 1700 / steps;

        timer = new Timer(delay, e -> {
            current += increment;
            if (current >= target) {
                current = target;
                if (targetDouble==0)
                    setText(String.format("%s%d",defaultString,target));
                else
                    setText(String.format("%s%.2f",defaultString,targetDouble));
                timer.stop();
            } else {
                setText(String.format("%s%d",defaultString,current));
            }
        });
        timer.start();
    }
}
