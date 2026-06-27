package Components;

import Controller.UserProfileController;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MangeUserProfilePanel extends JPanel {
    private RoundedInputText firstNameField;
    private RoundedInputText lastNameField;
    private RoundedInputText phoneNumberField;
    private RoundedInputText userIdField;
    private RoundedInputText userTypeField;
    private RoundedInputText registerField;
    private RoundedInputText memberShipIdField;
    private RoundedInputText creditField;
    private RoundedInputText debitField;
    private RoundedInputText searchUserField;

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final JPanel searchUserPanel;
    private final JPanel informationPanel;
    private JPanel btnPanelSearch;

    private FormTextFiledPanel memberShipIdPanel;
    private FormTextFiledPanel searchUserFiledPanel;
    private FormTextFiledPanel creditFiledPanel;
    private FormTextFiledPanel debitFiledPanel;

    private RoundedButton searchButton;
    private RoundedButton cancelButton;
    private RoundedButton convertButton;
    private RoundedButton kickButton;
    private RoundedButton cancelMangeButton;

    private UserProfileController controller;

    private String phoneNumber;

    public static final String SEARCH_PROP = "search";
    public static final String SEARCH_FILED_PROP = "searchFiled";
    public static final String CANCEL_PROP = "cancel";
    public static final String CANCEL_MANAGE_PROP = "cancelMange";
    public static final String KICK_PROP = "kick";
    public static final String CONVERT_TO_PRIME_PROP = "toPrime";


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

    public void setController(UserProfileController controller) {
        this.controller = controller;
    }

    public MangeUserProfilePanel() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20,20,20,20));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        searchUserPanel = createPanelSearch();
        informationPanel = crateInfoUserPanel();
        searchUserPanel.setLayout(new GridBagLayout());

        cardPanel.add(searchUserPanel,"SEARCH_USER");
        cardPanel.add(informationPanel,"INFORMATION_USER");
        cardLayout.show(cardPanel,"SEARCH_USER");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(cardPanel, gbc);

        attachEvents();
    }

    private JPanel createPanelSearch() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        RoundedPanel container = new RoundedPanel(30, ColorPalette.BG_MAIN, ColorPalette.BORDER);
        container.setBorder(new EmptyBorder(40, 30, 40, 30));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(350,300));
        container.setMaximumSize(new Dimension(350,300));

        JLabel titleLabel = new JLabel("Search User");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);
        container.add(titleLabel);
        container.add(Box.createVerticalStrut(30));

        searchUserField = new RoundedInputText("Username / Phone number", 5);
        searchUserFiledPanel = new FormTextFiledPanel("Username", searchUserField, SEARCH_PROP);
        searchUserFiledPanel.setAlignmentX(CENTER_ALIGNMENT);

        container.add(searchUserFiledPanel);
        btnPanelSearch = crateBtn();
        btnPanelSearch.setAlignmentX(CENTER_ALIGNMENT);
        container.add(Box.createVerticalStrut(15));
        container.add(btnPanelSearch);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(container, gbc);


        return panel;
    }

    private JPanel crateBtn() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        searchButton = new RoundedButton("Search", 15);
        searchButton.setPreferredSize(new Dimension(120, 40));
        searchButton.setBackground(new Color(75, 173, 79));
        searchButton.setForeground(Color.WHITE);

        cancelButton = new RoundedButton("Cancel", 15);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setBackground(new Color(0xde3c2f));
        cancelButton.setHoverColor(new Color(0xAD3225));
        cancelButton.setForeground(ColorPalette.TEXT_PRIMARY);

        buttonPanel.add(searchButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setAlignmentX(LEFT_ALIGNMENT);

        return buttonPanel;
    }

    private JPanel crateInfoUserPanel(){
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());

        RoundedPanel container = new RoundedPanel(30, ColorPalette.BG_MAIN, ColorPalette.BORDER);
        container.setBorder(new EmptyBorder(40, 30, 40, 30));
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setPreferredSize(new Dimension(1400,450));
        container.setMaximumSize(new Dimension(1400,450));
        container.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 20, 20, 20);


        JLabel titleLabel = new JLabel("User Status");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(ColorPalette.TEXT_PRIMARY);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        container.add(titleLabel,gbc);
        container.add(Box.createVerticalStrut(30));
        gbc.gridwidth = 1;

        //row 2(01)
        gbc.gridy = 1;

        firstNameField = new RoundedInputText("", 5);
        firstNameField.setEnabled(false);
        firstNameField.setForeground(ColorPalette.TEXT_PRIMARY);
        firstNameField.setMinimumSize(new Dimension(250, 40));
        FormTextFiledPanel firstNamePanel = new FormTextFiledPanel("First Name", firstNameField, "firstName");

        container.add(firstNamePanel, gbc);

        gbc.gridx = 1;

        lastNameField = new RoundedInputText("", 5);
        lastNameField.setEnabled(false);
        lastNameField.setForeground(ColorPalette.TEXT_PRIMARY);
        lastNameField.setMinimumSize(new Dimension(250, 40));
        FormTextFiledPanel lastNamePanel = new FormTextFiledPanel("Last Name", lastNameField, "lastName");
        container.add(lastNamePanel, gbc);

        gbc.gridx = 2;
        phoneNumberField = new RoundedInputText("", 5);
        phoneNumberField.setEnabled(false);
        phoneNumberField.setForeground(ColorPalette.TEXT_PRIMARY);
        phoneNumberField.setMinimumSize(new Dimension(250, 40));
        FormTextFiledPanel phonePanel = new FormTextFiledPanel("Phone Number", phoneNumberField, "phone");
        container.add(phonePanel, gbc);

        gbc.gridy=2;
        gbc.gridx=0;

        userIdField = new RoundedInputText("", 5);
        userIdField.setEnabled(false);
        userIdField.setForeground(ColorPalette.TEXT_PRIMARY);
        userIdField.setMinimumSize(new Dimension(250, 40));
        FormTextFiledPanel userIdPanel = new FormTextFiledPanel("User ID", userIdField, "userId");
        container.add(userIdPanel, gbc);

        gbc.gridx=1;
        userTypeField = new RoundedInputText("", 5);
        userTypeField.setEnabled(false);
        userTypeField.setForeground(ColorPalette.TEXT_PRIMARY);
        userTypeField.setMinimumSize(new Dimension(250, 40));
        FormTextFiledPanel userTypePanel = new FormTextFiledPanel("User Type", userTypeField, "userType");
        container.add(userTypePanel, gbc);

        gbc.gridx=2;
        registerField = new RoundedInputText("", 5);
        registerField.setEnabled(false);
        registerField.setForeground(ColorPalette.TEXT_PRIMARY);
        registerField.setMinimumSize(new Dimension(250, 40));
        FormTextFiledPanel registerDatePanel = new FormTextFiledPanel("Registered", registerField, "registerDate");
        container.add(registerDatePanel, gbc);

        //Prime user filed
        gbc.gridy=3;
        gbc.gridx=0;

        memberShipIdField = new RoundedInputText("", 5);
        memberShipIdField.setEnabled(false);
        memberShipIdField.setForeground(ColorPalette.TEXT_PRIMARY);
        memberShipIdField.setMinimumSize(new Dimension(250, 40));
        memberShipIdPanel = new FormTextFiledPanel("Membership Code", memberShipIdField, "membershipCode");
        container.add(memberShipIdPanel, gbc);

        gbc.gridx=1;
        creditField = new RoundedInputText("", 5);
        creditField.setEnabled(false);
        creditField.setForeground(ColorPalette.TEXT_PRIMARY);
        creditField.setMinimumSize(new Dimension(250, 40));
        creditFiledPanel = new FormTextFiledPanel("Total Credit", creditField, "credit");
        container.add(creditFiledPanel, gbc);

        gbc.gridx = 2;
        debitField = new RoundedInputText("", 5);
        debitField.setEnabled(false);
        debitField.setForeground(ColorPalette.TEXT_PRIMARY);
        debitField.setMinimumSize(new Dimension(250, 40));
        debitFiledPanel = new FormTextFiledPanel("Total Debit", debitField, "debit");
        container.add(debitFiledPanel, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 20, 0, 20);
        gbc.fill = GridBagConstraints.BOTH;
        JPanel btnPanel = crateBtnMange();
        container.add(btnPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(container, gbc);

        return panel;
    }

    private JPanel crateBtnMange(){
        JPanel btnPanel = new JPanel();
        btnPanel.setOpaque(false);
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setMinimumSize(new Dimension(750, 60));
        btnPanel.setMaximumSize(new Dimension(750, 60));

        // Button 1: Convert to Prime
        convertButton = new RoundedButton("Convert to Prime", 20);
        convertButton.setPreferredSize(new Dimension(230, 45));
        convertButton.setBackground(ColorPalette.SELECTION_BG   );
        convertButton.setForeground(ColorPalette.TEXT_PRIMARY);
        convertButton.setFont(new Font("Arial", Font.BOLD, 13));

        // Button 2: Kick User
        kickButton = new RoundedButton("Kick User", 20);
        kickButton.setPreferredSize(new Dimension(230, 45));
        kickButton.setBackground(ColorPalette.ACCENT_WARNING);
        kickButton.setHoverColor(new Color(0xB3F2994A, true));
        kickButton.setForeground(ColorPalette.TEXT_PRIMARY);
        kickButton.setFont(new Font("Arial", Font.BOLD, 13));

        // Button 3: Cancel
        cancelMangeButton = new RoundedButton("Cancel", 20);
        cancelMangeButton.setPreferredSize(new Dimension(230, 45));
        cancelMangeButton.setBackground(new Color(0xde3c2f));
        cancelMangeButton.setHoverColor(new Color(0xAD3225));
        cancelMangeButton.setForeground(ColorPalette.TEXT_PRIMARY);
        cancelMangeButton.setFont(new Font("Arial", Font.BOLD, 13));

        // Add buttons to panel
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(convertButton);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(kickButton);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(cancelMangeButton);
        btnPanel.add(Box.createHorizontalGlue());

        return btnPanel;
    }

    public void showSearchView(){
        loadView();
        cardLayout.show(cardPanel,"SEARCH_USER");
    }
    public void showInformationUser(){
        cardLayout.show(cardPanel,"INFORMATION_USER");
    }

    public String getPhoneNumber(){
        return phoneNumberField.getText();
    }

    public void loadView() {
        searchUserField.setActivePlaceHolder(true);
    }

    public void loadData(String firstName, String lastName, String phoneNumber, String userId, String userType, String registerDate, String memberShipCode,
                         double creditAmount, double debitAmount) {
        if (memberShipCode==null){
            memberShipIdPanel.setVisible(false);
            creditFiledPanel.setVisible(false);
            debitFiledPanel.setVisible(false);
            convertButton.setVisible(true);
        }
        else {
            memberShipIdField.setText(memberShipCode);
            creditField.setText(String.format("%.2f",creditAmount));
            debitField.setText(String.format("%.2f",debitAmount));
            convertButton.setVisible(false);
        }
        if (userType.equals("Administrator")){
            convertButton.setVisible(false);
            kickButton.setVisible(false);
        }
        kickButton.setVisible(true);
        convertButton.setVisible(true);
        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        phoneNumberField.setText(phoneNumber);
        userIdField.setText(userId);
        userTypeField.setText(userType);
        registerField.setText(registerDate);
        showInformationUser();
    }

    private void attachEvents() {
        cancelMangeButton.addActionListener(e -> {fireActionEvent(CANCEL_MANAGE_PROP);});
        kickButton.addActionListener(e ->{
            controller.handleKickUser(searchUserField.getText());
            showSearchView();
            controller.showMainPage();
            fireActionEvent(KICK_PROP);});
        convertButton.addActionListener(e ->{
            controller.handleUpgradeToPrime(searchUserField.getText());
            showSearchView();
            controller.showMainPage();
            fireActionEvent(CONVERT_TO_PRIME_PROP);});
        cancelButton.addActionListener(e ->{
            controller.showMainPage();
            fireActionEvent(CANCEL_PROP);});
        searchButton.addActionListener(e ->{
            var result = controller.validatePhoneNumber(searchUserField.getText());
            if (result.isValid())
                searchUserFiledPanel.clearError();
            else
                searchUserFiledPanel.setError(result.getErrorMessage());
            if (controller.statusSearchPhoneNumber(searchUserField.getText()))
                controller.handelSearchUser(searchUserField.getText());
            else searchUserFiledPanel.setError("User with this number not found!");

            phoneNumber = searchUserField.getText();

            fireActionEvent(SEARCH_PROP);});
        searchUserField.addActionListener(e -> {
            var result = controller.validatePhoneNumber(searchUserField.getText());
            if (result.isValid())
                searchUserFiledPanel.clearError();
            else
                searchUserFiledPanel.setError(result.getErrorMessage());

            fireActionEvent(SEARCH_FILED_PROP);
        });
    }
}
