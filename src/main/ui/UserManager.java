package ui;

import model.exceptions.EssentialFileFailed;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import ui.gui.BaseFrame;
import ui.gui.CustomForm;
import ui.gui.Menu;
import ui.gui.Style;


import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.LinkedList;

import static javax.swing.JOptionPane.showMessageDialog;
import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static ui.gui.CustomBagConstraints.customConstraint;
import static ui.gui.CustomForm.Row;

//A frame that allows users to login or sign up
public class UserManager extends BaseFrame {

    private Menu displayButtons;

    private CustomForm form;
    private boolean login;


    //EFFECTS: create display buttons and call buildAccessJPanel()
    public UserManager() {
        super("Professional Rumble", Color.WHITE,null);
        setLayout(new GridBagLayout());

        displayButtons = new Menu(new Style(new Font("sans serif",Font.PLAIN,48),Color.WHITE),0,false);
        add(displayButtons,customConstraint(0,0));

        JButton loginDisplay = new JButton("Access Account");
        loginDisplay.addActionListener(e -> changeDisplay(true));
        displayButtons.addButton(loginDisplay,0);

        JButton signupDisplay = new JButton("Create New Account");
        signupDisplay.addActionListener(e -> changeDisplay(false));
        displayButtons.addButton(signupDisplay,1);

        buildAccessJPanel();

    }



    //MODIFIES: this
    //EFFECTS: builds name, password and confirm adding them to a JPanel container, calls displayLogin
    public void buildAccessJPanel() {
        form = new CustomForm(new Row[]{Row.NAME,Row.PASSWORD,Row.CONFIRM},"Save New Name", e -> handleButton());

        add(form,customConstraint(0,1,0,1.0));
        changeDisplay(true);
    }


    //MODIFIES: this
    //EFFECTS: if login is true, hide confirm and set login display button to white, signup display button to grey
    //         otherwise, do the exact opposite
    private void changeDisplay(boolean login) {
        this.login = login;
        Component[] buttons = displayButtons.getComponents();
        buttons[login ? 0 : 1].setBackground(Color.WHITE);
        buttons[login ? 1 : 0].setBackground(Color.GRAY);

        form.changeRowVisible(2,!login);
        form.getActionButton().setText(login ? "Log In" : "Sign Up");

        revalidate();
        repaint();
    }

    //MODIFIES: this
    //EFFECTS: Attempts to login with given credential if login field is true, attempts a signup otherwise
    private void handleButton() {
        if (login) {
            login();
        } else {
            signup();
        }
    }


    //MODIFIES: this
    //EFFECTS: Checks if credentials match a stored user, if they do enter main menu, otherwise display error message
    public void login() {
        String name = form.getField(0).getText();
        JSONObject userData = findUser(name);
        JTextField pass = form.getField(1);
        if (userData != null && checkPassword(userData,pass)) {
            new MainMenu(new Player(userData.getInt("id"),name),this);
        } else {
            pass.setText("");
            showMessageDialog(null,"Username or Password is incorrect","Login Failed",JOptionPane.PLAIN_MESSAGE);
        }
    }

    //MODIFIES: this
    //EFFECTS: Checks that the name is not taken already and that password is at least 8 char long and re-entered
    //         If conditions are met, create new user and open main menu, otherwise display error message
    public void signup() {
        String user = form.getField(0).getText();

        if (findUser(user) == null) {
            String[] hash = getHashedPassword(form);
            if (hash != null) {
                try {
                    new MainMenu(createNewUser(user, hash),this);
                } catch (IOException e) {
                    throw new EssentialFileFailed();
                }
            }
        } else {
            showMessageDialog(null,"Username already taken","Sign Up Failed",JOptionPane.PLAIN_MESSAGE);
        }
    }

    //MODIFIES: this
    //EFFECT: Adds the user data to users.json and creates a corresponding file in teams and battles
    private Player createNewUser(String username, String[] hash) throws IOException {
        JSONArray users = new JsonReader("users.json").arrayRead();
        int id = users.length();

        Player p = new Player(id,username);
        p.saveToUsers(hash[0], hash[1]);

        //Create Essential files for user
        File team = new File("./data/teams/" + id + ".json");
        File battle = new File("./data/battles/" + id + ".json");

        team.createNewFile();
        battle.createNewFile();

        //Writes empty array in each of the files
        JsonWriter writer = new JsonWriter("teams/" + id + ".json");
        writer.open();
        writer.writeArray(new JSONArray());
        writer.close();

        writer = new JsonWriter("battles/" + id + ".json");
        writer.open();
        writer.writeArray(new JSONArray());
        writer.close();

        return p;
    }

    //EFFECTS: Finds a stored user with given name, it exists return its data(including id) otherwise return null
    public static JSONObject findUser(String user) {
        try {
            JSONArray users = new JsonReader("users.json").arrayRead();
            for (int i = 0; i < users.length(); i++) {
                JSONObject userData = users.getJSONObject(i);
                String username = userData.getString("name");
                if (username.equals(user)) {
                    userData.put("id",i);
                    return userData;
                }
            }
            return null;
        } catch (IOException e) {
            throw new EssentialFileFailed();
        }
    }

    //EFFECTS: If user with given id exists, return the name of the user at given id position, otherwise return null
    public static String findName(int id) {
        try {
            JSONArray users = new JsonReader("users.json").arrayRead();
            if (id < users.length()) {
                return users.getJSONObject(id).getString("name");
            }
            return null;
        } catch (IOException e) {
            throw new EssentialFileFailed();
        }

    }

    //EFFECTS: finds highest id in array, returns -1 if users is empty
    public static int getHighestId(JSONArray array) {
        int maxID = -1;
        for (Object o : array) {
            JSONObject data = (JSONObject) o;
            int currentID = data.getInt("id");
            if (currentID > maxID) {
                maxID = currentID;
            }
        }
        return maxID;
    }

    //EFFECT: If password is at least 8 char long and re-entered correctly returns it hashed, or returns null otherwise
    public static String[] getHashedPassword(CustomForm form) {
        JPasswordField pass = (JPasswordField) form.getField(1);
        JPasswordField confirmPass = (JPasswordField) form.getField(2);
        char[] password = pass.getPassword();
        char[] confirmPassword = confirmPass.getPassword();

        pass.setText("");
        confirmPass.setText("");

        if (password.length < 8) {
            String msg = "Password must be at least 8 characters long.";
            showMessageDialog(null,msg,"Sign Up Failed",JOptionPane.PLAIN_MESSAGE);

        } else {


            if (Arrays.equals(confirmPassword, password)) {

                //CITATION: https://stackoverflow.com/questions/18142745/how-do-i-generate-a-salt-in-java-for-salted-hash
                byte[] saltByte = new byte[16];
                new SecureRandom().nextBytes(saltByte);
                String salt = new String(saltByte);
                return  new String[]{hash256(password,salt),salt};

            } else {

                showMessageDialog(null,"Passwords did not match.","Sign Up Failed",JOptionPane.PLAIN_MESSAGE);

            }
        }
        return null;
    }

    public static boolean checkPassword(JSONObject userData,JTextField passwordField) {
        char[] password = ((JPasswordField) passwordField).getPassword();
        String hash = hash256(password,userData.getString("salt"));

        passwordField.setText("");
        return hash.equals(userData.getString("password"));
    }

    //EFFECT: hashes given string to sha256 adding given salt
    private static String hash256(char[] input, String salt) {
        return sha256Hex(Arrays.hashCode(input) + salt);
    }


}
