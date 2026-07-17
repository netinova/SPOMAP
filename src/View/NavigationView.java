package View;

import Components.IconNavbarLabel;
import Components.SearchFiled;
import Controller.NavigationController;
import Util.ColorPalette;
import Util.UIUtils;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class NavigationView extends JPanel {

    private SearchFiled searchFiled;
    private IconNavbarLabel iconNavbarLabel;
    private NavigationController controller;

    public NavigationView(NavigationController controller) {
        this.controller = controller;

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

    private void attachEvents() {

        this.iconNavbarLabel.setOnIconClickListenerListener(new IconNavbarLabel.OnIconClickListener() {

            @Override
            public void onUserIconClick() {
                controller.onUserIconClick();
            }

            @Override
            public void onShoppingCartClick() {
                controller.onShoppingCartIconClick();
            }

        });

        // Use standard ActionListener for search field
        this.searchFiled.addActionListener(e -> {
            String searchText = e.getActionCommand();
            String searchType = searchFiled.getSelectedSearchType();
            controller.searchProducts(searchText, searchType);
        });
    }

    private void setupUI() {
        this.setLayout(new GridBagLayout());
        this.setBackground(ColorPalette.getInstance().getBgSecondary());
        this.setPreferredSize(new Dimension(0, 60));
        this.setMinimumSize(new Dimension(0, 60));

        Border bottomSeparator = BorderFactory.createMatteBorder(0, 0, 1, 0,
                ColorPalette.getInstance().getBorder());
        this.setBorder(BorderFactory.createCompoundBorder(bottomSeparator, new EmptyBorder(5, 10, 5, 10)));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        this.add(Box.createHorizontalGlue(), gbc);

        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        this.searchFiled = new SearchFiled();
        this.add(searchFiled, gbc);

        gbc.weightx = 1.0;
        this.add(Box.createHorizontalGlue(), gbc);

        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        this.iconNavbarLabel = new IconNavbarLabel();
        this.add(iconNavbarLabel, gbc);
    }
}
