package Components;

import Model.AppState;
import Model.UserType;
import Util.ColorPalette;
import Util.Validator.ValidationResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Invoice search panel with live filtering capabilities.
 * Contains search bars for invoice ID and user ID (user ID only for admin),
 * plus a date picker for filtering by date.
 * All searches happen live without buttons.
 */
public class InvoiceSearchPanel extends JPanel {

    private SimpleSearchField invoiceIdSearch;
    private SimpleSearchField userIdSearch;
    private RoundedInputText dateFromInputPanel;
    private RoundedInputText dateToInputPanel;
    private FormTextFiledPanel dateFromPanel;
    private FormTextFiledPanel dateToPanel;

    private EventListenerList listenerList = new EventListenerList();
    private boolean isAdmin;

    public InvoiceSearchPanel() {
        this.isAdmin = false;
        setupUI();
    }

    public interface ValidationListener {

        ValidationResult onValidation(String value);
    }

    private ValidationListener listener;

    public void addValidationListener(ValidationListener listener) {
        this.listener = listener;
    }

    private void setupUI() {
        setOpaque(false);
        setBackground(ColorPalette.BG_MAIN);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 15, 10, 15));
        setPreferredSize(new Dimension(800, 100)); // slightly taller for error labels
        setMinimumSize(new Dimension(600, 100));

        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        JPanel searchContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        searchContainer.setOpaque(false);
        searchContainer.setAlignmentY(Component.CENTER_ALIGNMENT); // vertical centring
        searchContainer.setBorder(BorderFactory.createEmptyBorder(19, 0, 0, 0));

        invoiceIdSearch = new SimpleSearchField("Search by Invoice ID");
        invoiceIdSearch.addActionListener(e -> fireSearchEvent());
        searchContainer.add(invoiceIdSearch);

        if (isAdmin) {
            userIdSearch = new SimpleSearchField("Search by User ID");
            userIdSearch.addActionListener(e -> fireSearchEvent());
            searchContainer.add(userIdSearch);
        }

        // ----- 2. Date inputs container (fixed width, centred) -----
        JPanel dateContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        dateContainer.setOpaque(false);
        dateContainer.setAlignmentY(Component.CENTER_ALIGNMENT);

        dateFromInputPanel = new RoundedInputText("YYYY/MM/DD", 4);
        dateFromInputPanel.setPreferredSize(new Dimension(200, 50));
        dateFromPanel = new FormTextFiledPanel("From", dateFromInputPanel, null);
        dateFromPanel.setPreferredSize(new Dimension(200, 75));
        dateFromInputPanel.addActionListener(e -> {
            if (listener != null) { // guard against null before listener is attached
                var result = listener.onValidation(e.getActionCommand());
                if (!result.isValid())
                    dateFromPanel.setError(result.getErrorMessage());
                else
                    dateFromPanel.clearError();
            }
        });
        dateContainer.add(dateFromPanel);

        dateToInputPanel = new RoundedInputText("YYYY/MM/DD", 4);
        dateToInputPanel.setPreferredSize(new Dimension(200, 50));
        dateToPanel = new FormTextFiledPanel("To", dateToInputPanel, null);
        dateToPanel.setPreferredSize(new Dimension(200, 75));
        dateToInputPanel.addActionListener(e -> {
            if (listener != null) {
                var result = listener.onValidation(e.getActionCommand());
                if (!result.isValid())
                    dateToPanel.setError(result.getErrorMessage());
                else
                    dateToPanel.clearError();
            }
        });
        dateContainer.add(dateToPanel);

        // ----- Assemble the row with glue for centring -----
        mainPanel.add(Box.createHorizontalGlue());
        mainPanel.add(searchContainer);
        mainPanel.add(Box.createHorizontalStrut(30)); // gap between search and dates
        mainPanel.add(dateContainer);
        mainPanel.add(Box.createHorizontalGlue());

        add(mainPanel, BorderLayout.CENTER);
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireSearchEvent() {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bruh");
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    public void setAdminMode(boolean isAdmin) {
        if (this.isAdmin != isAdmin) {
            this.isAdmin = isAdmin;
            removeAll();
            setupUI();
            revalidate();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.dispose();
    }
}
