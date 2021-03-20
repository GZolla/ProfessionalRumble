package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Menu extends JPanel {
    private Style style;


    public Menu(Style style, int gap, boolean vertical) {
        this.style = style;

        setLayout(new GridLayout(vertical ? 0 : 1,vertical ? 1 : 0,0,gap));

    }

    public void addButton(JButton b, int i) {
        if (style != null) {
            style.applyToComponent(b);
        }
        b.setFocusable(false);
        add(b,i);
    }

    public void removeButton(int i) {
        remove(i);
    }
}
