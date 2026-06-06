package Model;

import java.awt.Color;

public enum ProductColor {
    Black(new Color(0x222222)), // #222222
    White(new Color(0xffffff)), // #ffffff
    Gray(new Color(0x6b6b6b)); // #6b6b6b

    private final Color productColor;

    private ProductColor(Color productColor) {
        this.productColor = productColor;
    }

    public Color getProductColor() {
        return this.productColor;
    }
}
