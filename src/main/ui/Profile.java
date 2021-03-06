package ui;

import ui.gui.BaseFrame;
import ui.gui.CustomForm;

import javax.swing.*;
import java.awt.*;

import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static ui.UiManager.showMessage;
import static ui.UserManager.*;
import static ui.gui.CustomBagConstraints.customConstraint;

import static ui.gui.CustomForm.Row;

public class Profile extends BaseFrame {

    private JLabel name;

    private JTextField newName;
    private CustomForm passwordForm;


    public Profile(MainMenu prev) {
        super("Profile",prev);
        setLayout(new GridBagLayout());

        name = buildHeaderAndReturn("Profile Manager");

        buildNewName();
        buildNewPassword();

        repaint();
        revalidate();
    }

    //MODIFIES: this
    //EFFECTS: Builds custom form for a new name, sets newName field and sets its text to the player's name
    public void buildNewName() {
        CustomForm container = new CustomForm(new Row[]{Row.NAME},"Save New Name",e -> saveNewName());

        newName = container.getField(0);
        newName.setText(player.getName());

        GridBagConstraints constraints = customConstraint(0,1,1.0,1.0);
        constraints.gridwidth = 2;
        add(container,constraints);
    }

    //MODIFIES: this
    //EFFECTS: Builds custom form for current password, new password, sets newPassword field
    public void buildNewPassword() {
        Row[] rows = new Row[]{Row.CURRENT,Row.PASSWORD,Row.CONFIRM};
        passwordForm = new CustomForm(rows,"Change Password",e -> saveNewPassword());

        add(passwordForm,customConstraint(2,1,1.0,1.0));
    }


    //MODIFIES: this
    //EFFECTS: checks that new name is available and changes th
    public void saveNewName() {
        String newName = this.newName.getText();
        if (UserManager.findUser(newName) != null) {
            showMessage(this,"Name is already taken!","Login Failed");
        } else {
            player.setName(newName);
            player.saveToUsers(null,null);
            name.setText(newName + " | Profile Manager");
        }
    }

    private void saveNewPassword() {
        if (checkCredentials(player.getName(),passwordForm.getField(0)) != -1) {
            String[] hash = getHashedPassword(passwordForm);
            if (hash != null) {
                player.saveToUsers(hash[0],hash[1]);
                showMessage(this,"Password Changed","Success");
            }
        } else {
            showMessage(this,"Current Password is incorrect","Login Failed");
        }
    }

    @Override
    public void goBack() {
        ((MainMenu) prev).updateWelcome();
        super.goBack();
    }
}
