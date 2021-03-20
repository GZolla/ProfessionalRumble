package ui.gui;

import model.Player;

import javax.swing.*;
import java.awt.*;

import static java.awt.GridBagConstraints.REMAINDER;
import static ui.gui.CustomBagConstraints.customConstraint;

public class BaseFrame extends JFrame {
    protected Player player;
    protected BaseFrame prev;

    public BaseFrame(String title, Color bgColor, BaseFrame prev) {
        if (prev != null) {
            prev.setVisible(false);
            this.player = prev.player;
            this.prev = prev;
        }
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setTitle("Professional Rumble | " + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,900);
        setVisible(true);
        setResizable(false);

        getContentPane().setBackground(bgColor);
    }


    public JLabel buildHeaderAndReturn(String header) {
        JButton back = new JButton("<");
        back.setFont(new Font("sans serif",Font.BOLD,128));
        back.addActionListener(e -> goBack());
        add(back,customConstraint(0,0));

        JLabel title = new JLabel(player.getName() + " | " + header);
        title.setFont(new Font("sans serif",Font.BOLD,128));
        add(title, customConstraint(1,0,REMAINDER,1));

        return title;
    }

    //EFFECTS: sets prev baseframe to visible and eliminates current.
    public void goBack() {
        setVisible(false);
        dispose();
        prev.setVisible(true);
    }

    public Player getPlayer() {
        return player;
    }
}
