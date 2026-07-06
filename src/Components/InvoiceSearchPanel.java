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
        setPreferredSize(new Dimension(800, 90));
        setMinimumSize(new Dimension(600, 90));

        // Main container with flow layout
        JPanel mainPanel = new JPanel();
        mainPanel.setOpaque(false);
        mainPanel.setBackground(ColorPalette.BG_MAIN);
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        userIdSearch = new SimpleSearchField("Search by User ID");
        userIdSearch.addActionListener(e -> fireSearchEvent());
        mainPanel.add(userIdSearch);

        invoiceIdSearch = new SimpleSearchField("Search by Invoice ID");
        invoiceIdSearch.addActionListener(e -> fireSearchEvent());
        mainPanel.add(invoiceIdSearch);

        // User ID Search (only for admin)
        userIdSearch.setVisible(isAdmin);
        userIdSearch.setEnabled(isAdmin);

        // datePicker = new MiniDatePicker();
        // datePicker.addActionListener(e -> fireSearchEvent());
        // mainPanel.add(datePicker);

        dateFromInputPanel = new RoundedInputText("YYYY/MM/DD", 4);
        dateFromInputPanel.setPreferredSize(new Dimension(50 * 4, 50));
        dateFromInputPanel.setMaximumSize(new Dimension(50 * 4, 50));
        dateFromPanel = new FormTextFiledPanel("From", dateFromInputPanel, null);
        dateFromPanel.setPreferredSize(new Dimension(50 * 4, 75));
        dateFromInputPanel.addActionListener(e -> {
            var result = listener.onValidation(e.getActionCommand());
            if (!result.isValid())
                dateFromPanel.setError(result.getErrorMessage());
            else
                dateFromPanel.clearError();
        });

        mainPanel.add(dateFromInputPanel);

        dateToInputPanel = new RoundedInputText("YYYY/MM/DD", 4);
        dateToPanel = new FormTextFiledPanel("To", dateToInputPanel, null);
        dateToInputPanel.setPreferredSize(new Dimension(50 * 4, 50));
        dateToInputPanel.setMaximumSize(new Dimension(50 * 4, 50));
        dateToPanel.setPreferredSize(new Dimension(50 * 4, 75));
        dateToInputPanel.addActionListener(e -> {
            var result = listener.onValidation(e.getActionCommand());
            if (!result.isValid())
                dateToPanel.setError(result.getErrorMessage());
            else
                dateToPanel.clearError();
        });

        mainPanel.add(dateToInputPanel);

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
