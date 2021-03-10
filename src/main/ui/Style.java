package ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Style {

    public static final Border padding = BorderFactory.createEmptyBorder(10,50,10,50);

    private Font font;
    private Color fontColor;
    private Color bgColor;
    private Border border;

    public Style(Font font, Color fontColor, Color bgColor, Border border) {
        this.font = font;
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.border = border;
    }
    public Style(Font font, Color fontColor, Color bgColor) {
        this.font = font;
        this.fontColor = fontColor;
        this.bgColor = bgColor;
        this.border = padding;
    }

    public void applyToComponent(JComponent c) {
        c.setBackground(bgColor);
        c.setFont(font);
        c.setForeground(fontColor);
        c.setBorder(border);
    }
}
