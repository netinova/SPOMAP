package Components;

import Util.ColorPalette;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;

public class FormTextFiledPanel extends JPanel {

    private JLabel label;
    private JLabel errorLabel;
    private JComponent component;
    private PropertyChangeSupport support;
    private String propertyName;

    public FormTextFiledPanel(String title, JComponent component, String propertyName) {

        this.component = component;
        this.propertyName = propertyName;
        this.support = new PropertyChangeSupport(this);
        setOpaque(false);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(300, 90));

        // Label
        label = new JLabel(title);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(ColorPalette.TEXT_MUTED);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        component.setAlignmentX(Component.LEFT_ALIGNMENT);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        errorLabel.setForeground(ColorPalette.ACCENT_WARNING);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        this.add(Box.createVerticalStrut(5));
        this.add(label);
        this.add(Box.createVerticalStrut(5));
        // Inputs
        this.add(component);
        this.add(Box.createVerticalStrut(5));
        this.add(errorLabel);

        addInputListener();
    }

    private void addInputListener() {
        if (component instanceof RoundedInputText) {
            RoundedInputText textField = (RoundedInputText) component;
            textField.addActionListener(e -> {
                String oldValue = textField.getText();
                String newValue = textField.getText();
                support.firePropertyChange(propertyName, oldValue, newValue);
            });

            textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    fireChange();
                }

                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    fireChange();
                }

                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    fireChange();
                }

                private void fireChange() {
                    String newValue = textField.getText();
                    support.firePropertyChange(propertyName, null, newValue);
                }
            });

        } else if (component instanceof RoundedInputPassword) {
            RoundedInputPassword passwordField = (RoundedInputPassword) component;
            passwordField.addActionListener(e -> {
                String newValue = Arrays.toString(passwordField.getPassword());
                support.firePropertyChange(propertyName, null, newValue);
            });
        } else if (component instanceof RoundedComboBox) {
            RoundedComboBox<?> comboBox = (RoundedComboBox<?>) component;
            comboBox.addActionListener(e -> {
                String newValue = comboBox.getSelectedItem() != null ? comboBox.getSelectedItem().toString() : "";
                support.firePropertyChange(propertyName, null, newValue);
            });
        }
    }

    public void setError(String errorMessage) {
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorLabel.setText(" ");
        } else {
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
        }
        revalidate();
        repaint();
    }

    public void clearError() {
        setError(null);
    }

    public JComponent getComponent() {
        return component;
    }

    public String getValue() {
        if (component instanceof RoundedInputText) {
            return ((RoundedInputText) component).getText();
        } else if (component instanceof RoundedInputPassword) {
            return Arrays.toString(((RoundedInputPassword) component).getPassword());
        } else if (component instanceof RoundedComboBox) {
            Object selected = ((RoundedComboBox<?>) component).getSelectedItem();
            return selected != null ? selected.toString() : "";
        }
        return "";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
