package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import static ui.gui.CustomBagConstraints.customConstraint;

public class CustomForm extends JPanel {
    public enum Row {
        NAME,CURRENT,PASSWORD,CONFIRM
    }

    private static Font FONT = new Font("sans serif",Font.PLAIN,48);

    public CustomForm(Row[] rows, String buttonText, ActionListener listener) {
        super(new GridBagLayout());

        Insets insets = new Insets(20,20,20,20);
        GridBagConstraints labelConstraint = customConstraint(0,0,insets);
        GridBagConstraints fieldConstraint = customConstraint(1,0,insets);
        fieldConstraint.weighty = 1;


        for (int i = 0; i < rows.length; i++) {
            labelConstraint.gridy = i;
            fieldConstraint.gridy = i;
            add(buildLabel(rows[i]),labelConstraint);
            add(buildField(rows[i]),fieldConstraint);
        }

        JButton button = new JButton(buttonText);
        button.addActionListener(listener);
        button.setFont(FONT);
        add(button,customConstraint(0,rows.length,2,1));
    }

    private JLabel buildLabel(Row row) {
        String labelText = "";
        switch (row) {
            case NAME:
                labelText = "Name";
                break;
            case CURRENT:
                labelText = "Current Password";
                break;
            case PASSWORD:
                labelText = "Password";
                break;
            case CONFIRM:
                labelText = "Confirm Password";
                break;
        }
        JLabel label = new JLabel(labelText + ":");
        label.setFont(FONT);
        return label;
    }

    private JTextField buildField(Row row) {
        JTextField field = row == Row.NAME ? new JTextField(16) : new JPasswordField(16);
        field.setFont(FONT);
        return field;
    }

    public void changeRowVisible(int i, boolean visible) {
        getComponent(2 * i).setVisible(visible);
        getComponent(2 * i + 1).setVisible(visible);
    }

    public JTextField getField(int i) {
        return (JTextField) getComponent(2 * i + 1);
    }

    public JButton getActionButton() {
        return (JButton) getComponent(getComponents().length - 1);
    }

}
