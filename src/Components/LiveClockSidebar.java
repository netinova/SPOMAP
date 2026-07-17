package Components;

import Util.ColorPalette;

import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.Font;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LiveClockSidebar extends JLabel {
    public LiveClockSidebar() {
        setupUI();
        Timer timer = new Timer(1000, e -> {
            updateTime();
        });
        timer.start();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        this.setOpaque(false);
        this.setText("-- : -- : --");
        setFont(new Font("Arial", Font.BOLD, 15));
        setForeground(ColorPalette.getInstance().getTextMuted());
    }
    private void updateTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatedClock = DateTimeFormatter.ofPattern("HH:mm:ss");
        setText(time.format(formatedClock));
    }
}
