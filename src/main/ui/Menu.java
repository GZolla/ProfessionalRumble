package ui;

import javax.swing.*;
import java.awt.*;

public class Menu extends JPanel {
    private Style style;


    public Menu(JButton[] buttons, Style style, int gap, boolean vertical) {
        this.style = style;

        setLayout(new GridLayout(vertical ? 0 : 1,vertical ? 1 : 0,0,gap));

        for (JButton b : buttons) {
            addButton(b,-1);
        }

    }

    public void addButton(JButton b, int i) {
        style.applyToComponent(b);
        b.setFocusable(false);
        add(b,i);
    }
}
