package Components;

import Controller.UserProfileController;
import Model.ProductColor;
import Util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import Util.UIUtils;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.copy;

public class AddProductPanel extends JPanel {
    private RoundedInputText nameField;
    private RoundedInputText priceField;
    private RoundedInputText discountField;
    private RoundedInputText manufacturerField;
    private RoundedInputText descriptionField;

    private FormTextFiledPanel namePanel;
    private FormTextFiledPanel pricePanel;
    private FormTextFiledPanel discountPanel;

    private File selectedFile;

    // private ColorMultiSelect colorMultiSelect;
    private ColorSelectorPanel colorMultiSelect;

    // specs
    private final Map<String, String> technicalSpecs = new LinkedHashMap<>();
    private JPanel specsListPanel;

    private RoundedInputText specKeyField;
    private RoundedInputText specValueField;
    private FormTextFiledPanel specKeyPanel;
    private FormTextFiledPanel specValuePanel;

    // image
    private final List<String> productImages = new ArrayList<>();
    private JPanel imagesListPanel;

    private RoundedInputText imagePathField;
    private FormTextFiledPanel imagePathPanel;

    private RoundedButton saveBtn;
    private RoundedButton cancelBtn;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private UserProfileController controller;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public static final String SAVE_PROP = "save";
    public static final String CANCEL_PROP = "cancel";
    public static final String NAME_PROP = "name";
    public static final String PRICE_PROP = "price";
    public static final String DISCOUNT_PROP = "discount";
    public static final String MANUFACTURER_PROP = "manufacturer";
    public static final String DESCRIPTION_PROP = "description";

    public void setController(UserProfileController controller) {
        this.controller = controller;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public AddProductPanel() {
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

    private void setupUI() {
        setOpaque(false);
        setLayout(new BorderLayout());
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        // cardPanel.setOpaque(false);
        cardPanel.setBackground(ColorPalette.getInstance().getBgMain());
        cardPanel.add(buildFormPage(), "FORM");
        cardPanel.add(buildSpecPage(), "ADD_SPEC");
        cardPanel.add(buildImagePage(), "ADD_IMAGE");
        cardLayout.show(cardPanel, "FORM");
        add(cardPanel, BorderLayout.CENTER);
    }

    private JPanel buildFormPage() {
        JPanel borderPanel = new JPanel();
        borderPanel.setBackground(ColorPalette.getInstance().getBgMain());
        borderPanel.setLayout(new BorderLayout());
        borderPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(ColorPalette.getInstance().getBorder())));

        JPanel content = new JPanel(new GridBagLayout());
        // content.setOpaque(false);
        content.setBackground(ColorPalette.getInstance().getBgSecondary());
        content.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(4, 6, 4, 6);

        JLabel title = new JLabel("Add New Product");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(ColorPalette.getInstance().getTextPrimary());
        title.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 6, 20, 6);
        content.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        gbc.insets = new Insets(4, 6, 4, 6);

        // row 1
        gbc.gridy = 1;
        nameField = new RoundedInputText("Product name", 5);
        nameField.setColorBG(ColorPalette.getInstance().getBgMain());
        nameField.repaint();
        namePanel = new FormTextFiledPanel("Name", nameField, NAME_PROP);
        gbc.gridx = 0;
        content.add(namePanel, gbc);

        priceField = new RoundedInputText("e.g. 10.99", 5);
        priceField.setColorBG(ColorPalette.getInstance().getBgMain());
        priceField.repaint();
        pricePanel = new FormTextFiledPanel("Price", priceField, PRICE_PROP);
        gbc.gridx = 1;
        content.add(pricePanel, gbc);

        gbc.gridy = 2;
        discountField = new RoundedInputText("0 - 100", 5);
        discountField.setColorBG(ColorPalette.getInstance().getBgMain());
        discountField.repaint();
        discountPanel = new FormTextFiledPanel("Discount %", discountField, DISCOUNT_PROP);
        gbc.gridx = 0;
        content.add(discountPanel, gbc);

        manufacturerField = new RoundedInputText("Manufacturer", 5);
        manufacturerField.setColorBG(ColorPalette.getInstance().getBgMain());
        manufacturerField.repaint();
        FormTextFiledPanel manufacturerPanel = new FormTextFiledPanel("Manufacturer", manufacturerField,
                "manufacturer");
        gbc.gridx = 1;
        content.add(manufacturerPanel, gbc);

        gbc.gridy = 3;
        descriptionField = new RoundedInputText("Explain about product", 5);
        descriptionField.setColorBG(ColorPalette.getInstance().getBgMain());
        descriptionField.repaint();
        FormTextFiledPanel descriptionPanel = new FormTextFiledPanel("Description", descriptionField, "thumbnail");
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1;
        content.add(descriptionPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(14, 6, 2, 6);
        content.add(createLabel("Colors"), gbc);

        colorMultiSelect = new ColorSelectorPanel();
        colorMultiSelect.setSelectionMode(ColorSelectorPanel.SelectionMode.MULTI);
        colorMultiSelect.setColors(ProductColor.values());

        gbc.gridy = 5;
        gbc.insets = new Insets(2, 6, 4, 6);
        content.add(colorMultiSelect, gbc);

        // Spec
        gbc.gridy = 6;
        gbc.insets = new Insets(14, 6, 2, 6);
        content.add(createLabel("Technical Specs"), gbc);

        specsListPanel = new JPanel(new GridBagLayout());
        specsListPanel.setOpaque(false);
        gbc.gridy = 7;
        gbc.insets = new Insets(2, 6, 0, 6);
        content.add(specsListPanel, gbc);

        RoundedButton addSpecBtn = new RoundedButton("+ Add Spec", 12);
        addSpecBtn.setPreferredSize(new Dimension(130, 34));
        addSpecBtn.setBackground(ColorPalette.getInstance().getBgTertiary());
        addSpecBtn.setForeground(ColorPalette.getInstance().getTextPrimary());
        addSpecBtn.addActionListener(e -> cardLayout.show(cardPanel, "ADD_SPEC"));

        JPanel specBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        specBtnPanel.setOpaque(false);
        specBtnPanel.add(addSpecBtn);
        gbc.gridy = 8;
        gbc.insets = new Insets(6, 6, 4, 6);
        content.add(specBtnPanel, gbc);

        gbc.gridy = 9;
        gbc.insets = new Insets(14, 6, 2, 6);
        content.add(createLabel("Product Images"), gbc);

        imagesListPanel = new JPanel(new GridBagLayout());
        imagesListPanel.setOpaque(false);
        gbc.gridy = 10;
        gbc.insets = new Insets(2, 6, 0, 6);
        content.add(imagesListPanel, gbc);

        RoundedButton addImageBtn = new RoundedButton("+ Add Image", 12);
        addImageBtn.setPreferredSize(new Dimension(130, 34));
        addImageBtn.setBackground(ColorPalette.getInstance().getBgTertiary());
        addImageBtn.setForeground(ColorPalette.getInstance().getTextPrimary());
        addImageBtn.addActionListener(e -> cardLayout.show(cardPanel, "ADD_IMAGE"));

        JPanel imageBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        imageBtnPanel.setOpaque(false);
        imageBtnPanel.add(addImageBtn);
        gbc.gridy = 11;
        gbc.insets = new Insets(6, 6, 4, 6);
        content.add(imageBtnPanel, gbc);

        // save/cancel
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnRow.setOpaque(false);

        saveBtn = new RoundedButton("Add Product", 15);
        saveBtn.setPreferredSize(new Dimension(140, 40));
        saveBtn.setBackground(ColorPalette.getInstance().getAccentConfirm());
        saveBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        cancelBtn = new RoundedButton("Cancel", 15);
        cancelBtn.setPreferredSize(new Dimension(120, 40));
        cancelBtn.setBackground(ColorPalette.getInstance().getAccentDanger());
        cancelBtn.setHoverColor(ColorPalette.getInstance().getAccentDanger());
        cancelBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        btnRow.add(saveBtn);
        btnRow.add(cancelBtn);

        gbc.gridy = 12;
        gbc.insets = new Insets(20, 6, 10, 6);
        content.add(btnRow, gbc);

        borderPanel.add(content, BorderLayout.CENTER);

        // scroll
        JScrollPane scrollPane = new JScrollPane(borderPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(ColorPalette.getInstance().getBgMain());
        UIUtils.styleScrollBar(scrollPane.getVerticalScrollBar());

        JPanel page = new JPanel(new BorderLayout());
        page.setOpaque(false);
        page.add(scrollPane, BorderLayout.CENTER);
        return page;
    }

    private JPanel buildSpecPage() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setOpaque(false);

        RoundedPanel container = new RoundedPanel(25, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getBorder());
        container.setLayout(new GridBagLayout());
        container.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel title = new JLabel("Add Technical Spec");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ColorPalette.getInstance().getTextPrimary());
        title.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(title, gbc);

        specKeyField = new RoundedInputText("e.g. Mass", 5);
        specKeyField.setColorBG(ColorPalette.getInstance().getBgMain());
        specKeyField.repaint();
        specKeyPanel = new FormTextFiledPanel("Key", specKeyField, "specKey");
        specKeyField.addActionListener(e -> {
            var result = controller.validationQuery(specKeyField.getText(), "e.g. Mass");
            if (!result.isValid())
                specKeyPanel.setError(result.getErrorMessage());
            else
                specKeyPanel.clearError();
        });
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 4, 0);
        container.add(specKeyPanel, gbc);

        specValueField = new RoundedInputText("e.g. 250g", 5);
        specValueField.setColorBG(ColorPalette.getInstance().getBgMain());
        specValueField.repaint();
        specValuePanel = new FormTextFiledPanel("Value", specValueField, "specValue");
        specValueField.addActionListener(e -> {
            var result = controller.validationQuery(specValueField.getText(), "e.g. Mass");
            if (!result.isValid())
                specValuePanel.setError(result.getErrorMessage());
            else
                specValuePanel.clearError();
        });
        gbc.gridy = 2;
        container.add(specValuePanel, gbc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnRow.setOpaque(false);

        RoundedButton confirmSpecBtn = new RoundedButton("Add", 15);
        confirmSpecBtn.setPreferredSize(new Dimension(110, 38));
        confirmSpecBtn.setBackground(ColorPalette.getInstance().getAccentConfirm());
        confirmSpecBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        RoundedButton backSpecBtn = new RoundedButton("Back", 15);
        backSpecBtn.setPreferredSize(new Dimension(110, 38));
        backSpecBtn.setBackground(ColorPalette.getInstance().getBgTertiary());
        backSpecBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        btnRow.add(confirmSpecBtn);
        btnRow.add(backSpecBtn);

        gbc.gridy = 3;
        gbc.insets = new Insets(20, 0, 0, 0);
        container.add(btnRow, gbc);

        confirmSpecBtn.addActionListener(e -> {
            String key = specKeyField.getText().trim();
            String val = specValueField.getText().trim();
            int temp = 0;
            var result = controller.validationName(key);
            if (!result.isValid()) {
                specKeyPanel.setError(result.getErrorMessage());
                temp++;
            } else
                specKeyPanel.clearError();

            result = controller.validationQuery(val, "e.g. 250g");
            if (!result.isValid()) {
                specValuePanel.setError(result.getErrorMessage());
                temp++;
            } else
                specValuePanel.clearError();

            if (temp == 0) {
                technicalSpecs.put(key, val);
                refreshSpecs();
                specKeyField.setActivePlaceHolder(true);
                specValueField.setActivePlaceHolder(true);
                cardLayout.show(cardPanel, "FORM");
            }
        });

        backSpecBtn.addActionListener(e -> {
            specKeyPanel.clearError();
            specValuePanel.clearError();
            specKeyField.setActivePlaceHolder(true);
            specValueField.setActivePlaceHolder(true);
            cardLayout.show(cardPanel, "FORM");
        });

        GridBagConstraints gbcDuplicate = new GridBagConstraints();
        gbcDuplicate.gridx = 0;
        gbcDuplicate.gridy = 0;
        gbcDuplicate.weightx = 1;
        gbcDuplicate.weighty = 1;
        gbcDuplicate.anchor = GridBagConstraints.CENTER;
        page.add(container, gbcDuplicate);
        return page;
    }

    private JPanel buildImagePage() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setOpaque(false);

        RoundedPanel container = new RoundedPanel(25, ColorPalette.getInstance().getBgSecondary(),
                ColorPalette.getInstance().getBorder());
        container.setLayout(new GridBagLayout());
        container.setBorder(new EmptyBorder(40, 50, 40, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(4, 0, 4, 0);

        JLabel title = new JLabel("Add Product Image");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(ColorPalette.getInstance().getTextPrimary());
        title.setHorizontalAlignment(JLabel.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        container.add(title, gbc);

        // ---- Image path with Browse button ----
        gbc.gridy = 1;
        gbc.insets = new Insets(4, 0, 4, 0);

        JPanel pathPanel = new JPanel(new BorderLayout(8, 0));
        pathPanel.setOpaque(false);

        imagePathField = new RoundedInputText("database/pictures/...", 5);
        imagePathField.setColorBG(ColorPalette.getInstance().getBgMain());
        imagePathField.repaint();
        imagePathField.setPreferredSize(new Dimension(0, 40));
        imagePathField.setEnabled(false);
        pathPanel.add(imagePathField, BorderLayout.CENTER);

        RoundedButton browseBtn = new RoundedButton("Browse", 25);
        browseBtn.setPreferredSize(new Dimension(90, 40));
        browseBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            // Filter for images
            FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
                    "Images (png, jpg, jpeg)", "png", "jpg", "jpeg");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                String relativePath = "database/pictures/" + selectedFile.getName();
                imagePathField.setText(relativePath);
            }
        });
        pathPanel.add(browseBtn, BorderLayout.EAST);

        imagePathPanel = new FormTextFiledPanel("Image Path", pathPanel, "imagePath");
        container.add(imagePathPanel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnRow.setOpaque(false);

        RoundedButton confirmImageBtn = new RoundedButton("Add", 15);
        confirmImageBtn.setPreferredSize(new Dimension(110, 38));
        confirmImageBtn.setBackground(ColorPalette.getInstance().getAccentConfirm());
        confirmImageBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        RoundedButton backImageBtn = new RoundedButton("Back", 15);
        backImageBtn.setPreferredSize(new Dimension(110, 38));
        backImageBtn.setBackground(ColorPalette.getInstance().getBgTertiary());
        backImageBtn.setForeground(ColorPalette.getInstance().getTextPrimary());

        btnRow.add(confirmImageBtn);
        btnRow.add(backImageBtn);
        container.add(btnRow, gbc);

        confirmImageBtn.addActionListener(e -> {
            String path = imagePathField.getText().trim();
            boolean tempStatus = true;
            var result = controller.validationFileAddress(path);
            if (!result.isValid()) {
                imagePathPanel.setError(result.getErrorMessage());
                tempStatus = false;
            } else
                imagePathPanel.clearError();

            if (tempStatus) {
                try {
                    File copyFile = new File("database/pictures");
                    if (!copyFile.exists())
                        copyFile.mkdirs();

                    File destFile = new File(copyFile, selectedFile.getName());

                    // copy file
                    copy(selectedFile.toPath(), destFile.toPath(),
                            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    System.out.println("Error copying file: " + ex.getMessage());
                }

                productImages.add(path);
                refreshImages();
                imagePathField.setActivePlaceHolder(true);
                cardLayout.show(cardPanel, "FORM");
            }
        });

        backImageBtn.addActionListener(e -> {
            imagePathPanel.clearError();
            imagePathField.setActivePlaceHolder(true);
            cardLayout.show(cardPanel, "FORM");
        });

        GridBagConstraints gbcDuplicate = new GridBagConstraints();
        gbcDuplicate.gridx = 0;
        gbcDuplicate.gridy = 0;
        gbcDuplicate.weightx = 1;
        gbcDuplicate.weighty = 1;
        gbcDuplicate.anchor = GridBagConstraints.CENTER;
        page.add(container, gbcDuplicate);
        return page;
    }

    private void refreshSpecs() {
        specsListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 0, 2, 0);

        int row = 0;
        for (Map.Entry<String, String> entry : technicalSpecs.entrySet()) {
            JPanel specRow = new JPanel(new BorderLayout(8, 0));
            specRow.setOpaque(false);

            JLabel label = new JLabel(entry.getKey() + ": " + entry.getValue());
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            label.setForeground(ColorPalette.getInstance().getTextPrimary());

            RoundedButton removeBtn = new RoundedButton("×", 25);
            removeBtn.setPreferredSize(new Dimension(25, 25));
            removeBtn.setBackground(ColorPalette.getInstance().getAccentDanger());
            removeBtn.setHoverColor(ColorPalette.getInstance().getAccentDanger());
            removeBtn.setForeground(ColorPalette.getInstance().getTextPrimary());
            removeBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
            removeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            removeBtn.setHasBorder(false);
            String key = entry.getKey();
            removeBtn.addActionListener(e -> {
                technicalSpecs.remove(key);
                refreshSpecs();
            });

            specRow.add(label, BorderLayout.CENTER);
            specRow.add(removeBtn, BorderLayout.EAST);

            gbc.gridy = row++;
            specsListPanel.add(specRow, gbc);
        }

        specsListPanel.revalidate();
        specsListPanel.repaint();
    }

    private void refreshImages() {
        imagesListPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.gridx = 0;
        gbc.insets = new Insets(2, 0, 2, 0);

        for (int i = 0; i < productImages.size(); i++) {
            String path = productImages.get(i);
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);

            JLabel label = new JLabel(path);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            label.setForeground(ColorPalette.getInstance().getTextPrimary());

            RoundedButton removeBtn = new RoundedButton("×", 25);
            removeBtn.setPreferredSize(new Dimension(25, 25));
            removeBtn.setBackground(ColorPalette.getInstance().getAccentDanger());
            removeBtn.setHoverColor(ColorPalette.getInstance().getAccentDanger());
            removeBtn.setForeground(ColorPalette.getInstance().getTextPrimary());
            removeBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
            removeBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            removeBtn.setHasBorder(false);
            final int idx = i;
            removeBtn.addActionListener(e -> {
                productImages.remove(idx);
                refreshImages();
            });

            row.add(label, BorderLayout.CENTER);
            row.add(removeBtn, BorderLayout.EAST);

            gbc.gridy = i;
            imagesListPanel.add(row, gbc);
        }

        imagesListPanel.revalidate();
        imagesListPanel.repaint();
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(ColorPalette.getInstance().getTextMuted());
        return lbl;
    }

    public String getProductName() {
        return nameField.getText();
    }

    public String getPriceText() {
        return priceField.getText();
    }

    public String getDiscountText() {
        return discountField.getText();
    }

    public String getManufacturer() {
        return manufacturerField.getText();
    }

    public String getThumbnail() {
        return descriptionField.getText();
    }

    public Model.ProductColor[] getSelectedColors() {
        return colorMultiSelect.getSelectedColors();
    }

    public Map<String, String> getTechnicalSpecs() {
        return new LinkedHashMap<>(technicalSpecs);
    }

    public String[] getProductImages() {
        return productImages.toArray(new String[0]);
    }

    public String getDescription() {
        return descriptionField.getText();
    }

    public void showNameError(String msg) {
        namePanel.setError(msg);
    }

    public void showPriceError(String msg) {
        pricePanel.setError(msg);
    }

    public void showDiscountError(String msg) {
        discountPanel.setError(msg);
    }

    public void resetForm() {
        nameField.setActivePlaceHolder(true);
        priceField.setActivePlaceHolder(true);
        discountField.setActivePlaceHolder(true);
        manufacturerField.setActivePlaceHolder(true);
        descriptionField.setActivePlaceHolder(true);
        technicalSpecs.clear();
        refreshSpecs();
        namePanel.clearError();
        pricePanel.clearError();
        discountPanel.clearError();
        cardLayout.show(cardPanel, "FORM");
    }

    private void attachEvents() {
        nameField.addActionListener(e -> {
            var result = controller.validationName(nameField.getText());
            if (!result.isValid())
                namePanel.setError(result.getErrorMessage());
            else
                namePanel.clearError();

            support.firePropertyChange(NAME_PROP, null, nameField.getText());
        });
        priceField.addActionListener(e -> {
            var result = controller.validateDouble(priceField.getText());
            if (!result.isValid())
                pricePanel.setError(result.getErrorMessage());
            else
                pricePanel.clearError();

            support.firePropertyChange(PRICE_PROP, null, priceField.getText());
        });
        discountField.addActionListener(e -> {
            var result = controller.validateDouble(discountField.getText());
            if (!result.isValid())
                discountPanel.setError(result.getErrorMessage());
            else if (Double.parseDouble(discountField.getText()) > 100
                    || Double.parseDouble(discountField.getText()) < 0)
                discountPanel.setError("Must be between 0 and 100");
            else
                discountPanel.clearError();

            support.firePropertyChange(DISCOUNT_PROP, null, discountField.getText());
        });
        manufacturerField.addActionListener(e -> {
            support.firePropertyChange(MANUFACTURER_PROP, null, manufacturerField.getText());
        });
        descriptionField.addActionListener(e -> {
            support.firePropertyChange(DESCRIPTION_PROP, null, descriptionField.getText());
        });
        saveBtn.addActionListener(e -> {
            support.firePropertyChange(SAVE_PROP, null, null);
            controller.handleAddProduct(this);
        });
        cancelBtn.addActionListener(e -> {
            support.firePropertyChange(CANCEL_PROP, null, null);
            cardLayout.show(cardPanel, "FORM");
        });
    }
}
