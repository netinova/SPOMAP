package Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

import Model.Invoice;
import Model.InvoiceStatus;
import Util.ColorPalette;

public class InvoiceCard extends JPanel {

    private static final int FIXED_HEIGHT = 110;

    private Invoice invoice;
    private boolean showUserId;

    private EventListenerList listenerList = new EventListenerList();

    public InvoiceCard(Invoice invoice, boolean showUserId) {
        this.invoice = invoice;
        this.showUserId = showUserId;
        setupUI();
        attachEvents();
    }

    public Invoice getInvoice() {
        return invoice;
    }

    private void setupUI() {
        this.setBackground(ColorPalette.BG_SECONDARY);
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ColorPalette.BORDER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));

        this.setPreferredSize(new Dimension(0, FIXED_HEIGHT));
        this.setMinimumSize(new Dimension(0, FIXED_HEIGHT));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, FIXED_HEIGHT));

        this.setLayout(new BorderLayout());

        // Left section: invoice ID + date
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel idLabel = new JLabel(invoice.getInvoiceId());
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        idLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        idLabel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(idLabel);

        leftPanel.add(Box.createVerticalStrut(4));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String dateStr = invoice.getInvoiceDate() != null
                ? invoice.getInvoiceDate().format(dtf)
                : "—";
        JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateLabel.setForeground(ColorPalette.TEXT_MUTED);
        dateLabel.setAlignmentX(LEFT_ALIGNMENT);
        leftPanel.add(dateLabel);

        if (showUserId) {
            leftPanel.add(Box.createVerticalStrut(3));
            JLabel userIdLabel = new JLabel("User: " + invoice.getUserId());
            userIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            userIdLabel.setForeground(ColorPalette.TEXT_PLACEHOLDER);
            userIdLabel.setAlignmentX(LEFT_ALIGNMENT);
            leftPanel.add(userIdLabel);
        }

        add(leftPanel, BorderLayout.WEST);

        // Right section: status + items count + total price
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.insets = new Insets(0, 20, 2, 0);

        // Status badge
        String statusText = invoice.getStatus() == InvoiceStatus.Paid ? "Paid" : "Refunded";
        JLabel statusLabel = new JLabel(statusText);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(invoice.getStatus() == InvoiceStatus.Paid
                ? ColorPalette.ACCENT_SUCCESS
                : ColorPalette.ACCENT_WARNING);
        statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        rightPanel.add(statusLabel, gbc);

        // Items count
        gbc.gridy = 1;
        int itemCount = invoice.getItems() != null ? invoice.getItems().size() : 0;
        JLabel itemsLabel = new JLabel(itemCount + (itemCount == 1 ? " item" : " items"));
        itemsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        itemsLabel.setForeground(ColorPalette.TEXT_MUTED);
        itemsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        rightPanel.add(itemsLabel, gbc);

        // Total price
        gbc.gridy = 2;
        gbc.insets = new Insets(6, 20, 0, 0);
        JLabel priceLabel = new JLabel(String.format("$%.2f", invoice.getFinalPrice()));
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        priceLabel.setForeground(ColorPalette.ACCENT_PRIMARY);
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        rightPanel.add(priceLabel, gbc);

        add(rightPanel, BorderLayout.EAST);
    }

    private void attachEvents() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fireActionEvent("cardClick");
            }
        });
    }

    public void addActionListener(ActionListener listener) {
        listenerList.add(ActionListener.class, listener);
    }

    private void fireActionEvent(String command) {
        ActionListener[] listeners = listenerList.getListeners(ActionListener.class);
        if (listeners.length > 0) {
            ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
            for (ActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }
}
