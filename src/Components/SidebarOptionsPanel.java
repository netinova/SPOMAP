package Components;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

public class SidebarOptionsPanel extends JPanel {

    public interface SidebarButtonListener{
        void onButtonFactorsClick();
        void onSettingsClick();
    }

    private SidebarButtonListener listener;
    public int rounded = 45;

    public void setListener(SidebarButtonListener listener) {
        this.listener = listener;
    }

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
        RoundedButton factorButton = new RoundedButton("",rounded);
        factorButton.setPreferredSize(new Dimension(45, 45));

        gbc.gridx=0;
        gbc.gridy=0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        factorButton.setIcon(scaledFactorIcon);
        this.add(factorButton , gbc);

        factorButton.addActionListener(e->{
            if (listener!=null){
                listener.onButtonFactorsClick();
            }
        });

        // add button ======== Prime User
        ImageIcon settingsIcon = new ImageIcon("icons/settings.png");
        Image settingsImage = settingsIcon.getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH);
        ImageIcon scaledSettingsIcon = new ImageIcon(settingsImage);
        RoundedButton settingsButton = new RoundedButton("",rounded);
        settingsButton.setPreferredSize(new Dimension(45, 45));
        settingsButton.setIcon(scaledSettingsIcon);

        gbc.gridy=1;
        this.add(settingsButton , gbc);

        settingsButton.addActionListener(e->{
            if (listener!=null){
                listener.onSettingsClick();
            }
        });
    }
}
