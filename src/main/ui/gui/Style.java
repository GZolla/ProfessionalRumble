package ui.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Style {

    public static final Border PADDING = BorderFactory.createEmptyBorder(10,50,10,50);
    public static final Border ETCHED = BorderFactory.createEtchedBorder();

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
        this.border = PADDING;
    }


    //CITATION: https://www.accessibility-developer-guide.com/knowledge/colours-and-contrast/how-to-calculate/
    //EFFECTS: Sets fontColor based on bgColor, trying to maximise contrast
    //         According to the formula above to calculate contrast ((l1 + 0.05) / (l2 + 0.05)):
    //             - if bgColor is lighter than fontColor:
    //                 - fontColor is l2, meaning that contrast is better if it is black(divisor is the smallest)
    //                 - otherwise, fontColor is l1, making contrast better if it is white(largest numerator)
    public Style(Font font, Color bgColor) {
        this.font = font;
        this.bgColor = bgColor;
        this.fontColor = relativeLuminance(bgColor) < Math.sqrt(1.05 * 0.5) ? Color.WHITE : Color.BLACK;
        this.border = PADDING;
    }


    public void applyToComponent(JComponent c) {
        c.setBackground(bgColor);
        c.setFont(font);
        c.setForeground(fontColor);
        c.setBorder(border);
    }

    //CITATION: https://www.w3.org/WAI/GL/wiki/Relative_luminance (for methods below)



    //EFFECTS: Calculates relative luminance of a colour as indicated in page above
    public double relativeLuminance(Color color) {
        double red = calculateComponent(color.getRed());
        double green = calculateComponent(color.getGreen());
        double blue = calculateComponent(color.getBlue());
        return 0.2126 * red + 0.7152 * green + 0.0722 * blue;
    }

    //EFFECTS: processes one of the components of a colour as described by the formula cited above
    public double calculateComponent(int value) {
        double proportion = value / 255.0;
        if (proportion <= 0.03928) {
            return proportion / 12.92;
        } else {
            return Math.pow((proportion + 0.055) / 1.055, 2.4);
        }
    }

}
