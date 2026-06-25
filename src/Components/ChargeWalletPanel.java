package Components;

import Controller.ProfileController;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ChargeWalletPanel extends JPanel {


    private RoundedInputText amountInputText;
    private RoundedInputText balanceInputText;

    private FormTextFiledPanel amountInputFiled;
    private FormTextFiledPanel balanceInputFiled;

    private JPanel btnPanel;

    private RoundedButton chargeBtn;
    private RoundedButton cancelBtn;

    public static final String AMOUNT_PROP = "amount";
    public static final String BALANCE_PROP = "balance";
    public static final String CHARGE_PROP = "charge";
    public static final String CANCEL_PROP = "cancel";

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private ProfileController controller;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setController(ProfileController controller) {
        this.controller = controller;
    }

    public ChargeWalletPanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());

        createPanel();
    }

    private void createPanel() {
        RoundedPanel container = new RoundedPanel(30, ColorPalette.BG_MAIN, ColorPalette.BORDER);
        container.setBorder(new EmptyBorder(40, 30, 40, 30));
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Charge Wallet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        container.add(titleLabel);
        container.add(Box.createVerticalStrut(30));

        balanceInputText = new RoundedInputText("", 5);
        balanceInputText.setEnabled(false);
        balanceInputText.setForeground(ColorPalette.TEXT_MUTED);
        balanceInputFiled = new FormTextFiledPanel("Balance", balanceInputText, BALANCE_PROP);
        balanceInputFiled.setAlignmentX(CENTER_ALIGNMENT);

        amountInputText = new RoundedInputText("Enter amount", 5);
        amountInputFiled = new FormTextFiledPanel("Amount", amountInputText, AMOUNT_PROP);
        amountInputFiled.setAlignmentX(CENTER_ALIGNMENT);
        amountInputText.addActionListener(e -> {
            var result = controller.validateAmount(amountInputText.getText());
            if (result.isValid())
                amountInputFiled.clearError();
            else
                amountInputFiled.setError(result.getErrorMessage());

            support.firePropertyChange(AMOUNT_PROP, null, amountInputText.getText());
        });

        container.add(balanceInputFiled);
        container.add(amountInputFiled);
        btnPanel = crateBtn();
        btnPanel.setAlignmentX(CENTER_ALIGNMENT);
        chargeBtn.addActionListener(e -> {
            var result = controller.validateAmount(amountInputText.getText());
            if (result.isValid())
                amountInputFiled.clearError();
            else
                amountInputFiled.setError(result.getErrorMessage());

            support.firePropertyChange(CHARGE_PROP, null, amountInputText.getText());
        });
        cancelBtn.addActionListener(e -> {
            controller.showMainPage();
            support.firePropertyChange(CANCEL_PROP, null, null);
        });
        container.add(Box.createVerticalStrut(15));
        container.add(btnPanel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(container, gbc);

    }

    private JPanel crateBtn() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        chargeBtn = new RoundedButton("Charge", 15);
        chargeBtn.setPreferredSize(new Dimension(120, 40));
        chargeBtn.setBackground(new Color(75, 173, 79));
        chargeBtn.setForeground(Color.WHITE);

        cancelBtn = new RoundedButton("Cancel", 15);
        cancelBtn.setPreferredSize(new Dimension(120, 40));
        cancelBtn.setBackground(new Color(0xde3c2f));
        cancelBtn.setHoverColor(new Color(0xAD3225));
        cancelBtn.setForeground(ColorPalette.TEXT_PRIMARY);

        buttonPanel.add(chargeBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

        return buttonPanel;
    }

    public void loadUserData(String balance) {
        balanceInputText.setText(balance);
        amountInputText.setActivePlaceHolder(true);
    }
}
