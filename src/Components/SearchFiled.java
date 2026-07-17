package Components;

import Util.ColorPalette;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;

import com.formdev.flatlaf.extras.FlatSVGIcon;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class SearchFiled extends JPanel {

    private int cornerRadius = 40;
    private RoundedComboBox<String> comboBox;
    private SearchBarTextInput searchInput;
    private JLabel iconLabel;
    private EventListenerList listenerList = new EventListenerList();

    public SearchFiled() {
        setupUI();
        attachEvents();
        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            attachEvents();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        setOpaque(false);

        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setBorder(new EmptyBorder(0, 10, 0, 10));

        // search input
        searchInput = new SearchBarTextInput("Search", 5);

        // search Icon
        iconLabel = new JLabel();
        File svgFile = new File("icons/search.svg");
        FlatSVGIcon searchLogo = new FlatSVGIcon(svgFile).derive(23, 23);

        if (!searchLogo.hasFound()) {
            System.err.println("SVG not found: " + svgFile.getAbsolutePath());
        }
        searchLogo.setColorFilter(new FlatSVGIcon.ColorFilter(c -> ColorPalette.getInstance().getAccentPrimary()));

        iconLabel.setIcon(searchLogo);
        iconLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // comboBox
        String[] searchOptions = { "All", "Name", "Color", "Manufacturer" };
        comboBox = new RoundedComboBox<String>(searchOptions);
        comboBox.setSelectedIndex(1);

        this.add(iconLabel);
        this.add(searchInput);
        this.add(comboBox);
    }

    private void attachEvents() {
        // Add ActionListener for live search (every character)
        searchInput.addActionListener(e -> {
            fireSearchEvent(searchInput.getText());
        });

        // Add EnterKeyListener for explicit Enter key (can trigger loseFocus)
        searchInput.setEnterKeyListener(text -> {
            fireSearchEvent(text);
            loseFocus();
            transferFocus();
        });

        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (searchInput.getText().isEmpty() && !searchInput.getText().equals("Search"))
                    System.out.println(searchInput.getText()); // Redirect to Shop item
            }
        });
    }

    public String getSelectedSearchType() {
        if (comboBox == null)
            return "All";
        return comboBox.getSelectedItem().toString();
    }

    // Standard ActionListener support
    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireSearchEvent(String searchText) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, searchText);
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    private void loseFocus() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);// soft render

        // Draw border
        g2.setColor(ColorPalette.getInstance().getBorder());
        g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();
        super.paintComponent(g);
    }
}
