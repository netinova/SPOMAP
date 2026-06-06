package Model;

import java.awt.Color;

public enum ProductColor {
    Black(new Color(0x222222)), // #222222
    White(new Color(0xffffff)), // #ffffff
    Gray(new Color(0x6b6b6b)), // #6b6b6b
    Black_Band(new Color(0x222222)),// #222222
    Blue_Band(new Color(0x194171)),
    Orange_Band(new Color(0xf65d24)),
    Olive_Loop(new Color(0x79705f)),
    Blue_Loop(new Color(0x194171)),
    Purple_Doppler(new Color(0x742451)),
    Blue_Doppler(new Color(0x8276FB)),
    Red_Doppler(new Color(0x8c1826)),
    Dark_Purple_Doppler(new Color(0x2c0a16));


    private final Color productColor;

    private ProductColor(Color productColor) {
        this.productColor = productColor;
    }

    public Color getProductColor() {
        return this.productColor;
    }
}
