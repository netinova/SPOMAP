package Components;

import Util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LiveClockSidebar extends JLabel {
    public LiveClockSidebar() {

        this.setOpaque(false);

        setFont(new Font("Arial",Font.BOLD,15));
        setForeground(ColorPalette.TEXT_MUTED);

        Timer timer = new Timer(1000, e->{
            updateTime();
        });
           timer.start();
    }
    private void updateTime(){
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatedClock = DateTimeFormatter.ofPattern("HH:mm:ss");
        setText(time.format(formatedClock));
    }
}
