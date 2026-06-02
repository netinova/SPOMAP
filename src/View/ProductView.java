package View;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Model.Product;
import Model.ProductCatalog;
import Util.ColorPalette;

public class ProductView extends JPanel implements PropertyChangeListener {
    public ProductView(ProductCatalog model) {

        model.addListener(this);

        this.add(new JLabel("product view"));
        this.setBackground(ColorPalette.BG_MAIN);
        this.setForeground(ColorPalette.TEXT_PRIMARY);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ProductCatalog.PROP_SELECTED)) {

            Product product = (Product) evt.getNewValue();
            System.out.println("Product selected: " + product.getName() + " - Discount: " + product.getDiscount());
        }
    }
}
