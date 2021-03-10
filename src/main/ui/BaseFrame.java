package ui;

import javax.swing.*;
import java.awt.*;

public class BaseFrame extends JFrame {

    public BaseFrame(String title, Color bgcolor) {
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setTitle("Professional Rumble | " + title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,900);
        setVisible(true);
        setResizable(false);

        getContentPane().setBackground(bgcolor);
    }
}
