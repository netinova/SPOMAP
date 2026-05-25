package Components;

import javax.swing.*;
import java.awt.*;

public class SidebarOptionsPanel extends JPanel {

    private int rounded = 10;
    public SidebarOptionsPanel() {

        setupUI();
        crateComponents();
    }

    private void setupUI() {
        this.setOpaque(false);
        this.setLayout(new GridBagLayout());
    }

    private void crateComponents() {
        GridBagConstraints gbc = new GridBagConstraints();

        // add button ========factors
        ImageIcon factorIcon = new ImageIcon("icons/factor.png");
        Image factorImage = factorIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        ImageIcon scaledFactorIcon = new ImageIcon(factorImage);
        RoundedButton factorButton = new RoundedButton("Factors",rounded);
        factorButton.setPreferredSize(new Dimension(200, 40));

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 10, 5, 10);

        factorButton.setIcon(scaledFactorIcon);
        this.add(factorButton , gbc);


        // add button ======== Prime User
        ImageIcon primeUserIcon = new ImageIcon("icons/Prime_user.png");
        Image primeUserImage = primeUserIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        ImageIcon scaledPrimeUserIcon = new ImageIcon(primeUserImage);
        RoundedButton primeUserButton = new RoundedButton("Prime User",rounded);
        primeUserButton.setPreferredSize(new Dimension(200, 40));

        primeUserButton.setIcon(scaledPrimeUserIcon);

        gbc.gridx=0;
        gbc.gridy=1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        this.add(primeUserButton , gbc);

        // add button ======== Prime User
        ImageIcon settingsIcon = new ImageIcon("icons/settings.png");
        Image settingsImage = settingsIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        ImageIcon scaledSettingsIcon = new ImageIcon(settingsImage);
        RoundedButton settingsButton = new RoundedButton("Settings",rounded);
        settingsButton.setPreferredSize(new Dimension(200, 40));

        settingsButton.setIcon(scaledSettingsIcon);

        gbc.gridx=0;
        gbc.gridy=2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 10, 5, 10);
        this.add(settingsButton , gbc);


        // stick to top
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.VERTICAL;
        this.add(Box.createVerticalGlue(), gbc);

    }
}
