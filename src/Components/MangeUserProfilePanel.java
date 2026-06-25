package Components;

import Controller.UserProfileController;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class MangeUserProfilePanel extends JPanel {
    private RoundedInputText firstNameField;
    private RoundedInputText lastNameField;
    private RoundedInputText phoneNumberField;
    private RoundedInputText searchUserFiled;

    private CardLayout cardLayout;
    private JPanel searchUserPanel;
    private JPanel cardPanel;
    private JPanel btnPanel;

    private FormTextFiledPanel firstNamePanel;
    private FormTextFiledPanel lastNamePanel;
    private FormTextFiledPanel phoneNumberPanel;
    private FormTextFiledPanel searchUserFiledPanel;

    private RoundedButton searchButton;
    private RoundedButton cancelButton;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    private UserProfileController controller;

    public static final String SEARCH_PROP = "search";
    public static final String SEARCH_FILED_PROP = "searchFiled";
    public static final String CANCEL_PROP = "cancel";

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
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
        searchUserPanel.setLayout(new GridBagLayout());

        cardPanel.add(searchUserPanel,"SEARCH_USER");
        cardLayout.show(cardPanel,"SEARCH_USER");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(cardPanel, gbc);
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

        searchUserFiled = new RoundedInputText("Username / Phone number", 5);
        searchUserFiledPanel = new FormTextFiledPanel("Username", searchUserFiled, SEARCH_PROP);
        searchUserFiledPanel.setAlignmentX(CENTER_ALIGNMENT);
        searchUserFiled.addActionListener(e -> {
            var result = controller.validatePhoneNumber(searchUserFiled.getText());
            if (result.isValid())
                searchUserFiledPanel.clearError();
            else
                searchUserFiledPanel.setError(result.getErrorMessage());

            support.firePropertyChange(SEARCH_FILED_PROP, null, searchUserFiled.getText());
        });

        container.add(searchUserFiledPanel);
        btnPanel = crateBtn();
        btnPanel.setAlignmentX(CENTER_ALIGNMENT);
        searchButton.addActionListener(e -> {
            var result = controller.validatePhoneNumber(searchUserFiled.getText());
            if (result.isValid())
                searchUserFiledPanel.clearError();
            else
                searchUserFiledPanel.setError(result.getErrorMessage());

            support.firePropertyChange(SEARCH_PROP, null, searchUserFiled.getText());
        });
        cancelButton.addActionListener(e -> {
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

    public void loadView() {
        searchUserFiled.setActivePlaceHolder(true);
    }
}
