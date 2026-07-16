package Components;

import Controller.UserProfileController;
import Util.ColorPalette;
import Util.UIUtils;

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

    private UserProfileController controller;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void setController(UserProfileController controller) {
        this.controller = controller;
    }

    public ChargeWalletPanel() {
        setupUI();

        ColorPalette.getInstance().addPropertyChangeListener(e -> {
            removeAll();
            setupUI();
            revalidate();
            repaint();
        });
    }

    private void setupUI() {
        removeAll();
        setOpaque(false);
        setLayout(new GridBagLayout());

        RoundedPanel container = new RoundedPanel(30, ColorPalette.getInstance().getBgMain(),
                ColorPalette.getInstance().getBorder());
        container.setBorder(new EmptyBorder(40, 30, 40, 30));
        container.setOpaque(false);
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Charge Wallet");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorPalette.getInstance().getTextPrimary());
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        container.add(titleLabel);
        container.add(Box.createVerticalStrut(30));

        balanceInputText = new RoundedInputText("", 5);
        balanceInputText.setEnabled(false);
        balanceInputText.setForeground(ColorPalette.getInstance().getTextMuted());
        balanceInputFiled = new FormTextFiledPanel("Balance", balanceInputText, BALANCE_PROP);
        balanceInputFiled.setAlignmentX(CENTER_ALIGNMENT);

        amountInputText = new RoundedInputText("Enter amount", 5);
        amountInputFiled = new FormTextFiledPanel("Amount", amountInputText, AMOUNT_PROP);
        amountInputFiled.setAlignmentX(CENTER_ALIGNMENT);
        amountInputText.addActionListener(e -> {
            var result = controller.validateDouble(amountInputText.getText());
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
            var result = controller.validateDouble(amountInputText.getText());
            if (result.isValid())
                amountInputFiled.clearError();
            else
                amountInputFiled.setError(result.getErrorMessage());

            if (result.isValid())
                support.firePropertyChange(CHARGE_PROP, null, Double.valueOf(amountInputText.getText()));
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
        chargeBtn.setBackground(ColorPalette.getInstance().getAccentConfirm());
        chargeBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        cancelBtn = new RoundedButton("Cancel", 15);
        cancelBtn.setPreferredSize(new Dimension(120, 40));
        cancelBtn.setBackground(ColorPalette.getInstance().getAccentDanger());
        cancelBtn.setHoverColor(ColorPalette.getInstance().getAccentDanger());
        cancelBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        buttonPanel.add(chargeBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

        return buttonPanel;
    }

    public void loadUserData(String balance) {
        balanceInputText.setText(balance);
        amountInputText.setActivePlaceHolder(true);
        amountInputFiled.clearError();
    }
}
