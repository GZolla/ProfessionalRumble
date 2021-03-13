package ui;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class Menu extends JPanel {
    private Style style;
    private LinkedList<JButton> buttons;


    public Menu(Style style, int gap, boolean vertical) {
        this.style = style;
        this.buttons = new LinkedList<>();

        setLayout(new GridLayout(vertical ? 0 : 1,vertical ? 1 : 0,0,gap));
        for (JButton b : buttons) {
            addButton(b,-1);
        }

    }

    public void addButton(JButton b, int i) {
        if (style != null) {
            style.applyToComponent(b);
        }
        b.setFocusable(false);
        add(b,i);
        buttons.add(i,b);
    }

    public void removeButton(int i) {
        JButton b = buttons.get(i);
        remove(b);
        buttons.remove(i);
    }

    public LinkedList<JButton> getButtons() {
        return buttons;
    }
}
