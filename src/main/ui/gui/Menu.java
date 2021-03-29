package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.LinkedList;

public class Menu extends JPanel {
    private Style style;


    public Menu(Style style, int gap, boolean vertical) {
        this.style = style;

        setLayout(new GridLayout(vertical ? 0 : 1, vertical ? 1 : 0,0,gap));
        setBackground(Color.WHITE);

    }

    public void addButton(String text, ActionListener l, int i) {
        JButton b = new JButton(text);
        if (l != null) {
            b.addActionListener(l);
        }
        if (style != null) {
            style.applyToComponent(b);
        }
        b.setFocusable(false);
        add(b,i);
    }

    public void changeEnable(boolean able) {
        for (Component b : getComponents()) {
            b.setEnabled(able);
        }
    }

}
