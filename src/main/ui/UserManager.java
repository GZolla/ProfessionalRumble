package ui;

import model.exceptions.EssentialFileFailed;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static ui.UiManager.chooseOptions;

public class UserManager {
    private Scanner sc;
    private JSONArray users;

    public UserManager() {
        sc = new Scanner(System.in);
        try {
            users = new JsonReader("users.json").arrayRead();
        } catch (IOException e) {
            System.out.println("users.json");
            throw new EssentialFileFailed();
        }

    }

    //EFFECT: saves users to file ./data/user.json
    public void saveUsers() throws FileNotFoundException {
        JsonWriter writer = new JsonWriter("users.json");
        writer.open();
        writer.writeArray(users);
        writer.close();
    }

    public Player access() {
        String[] options = new String[]{"Log In", "Sign Up"};

        Player res;
        while (true) {
            int action = chooseOptions("Access your account, or create one", options,true);
            switch (action) {
                case 0:
                    res = login();
                    break;
                case 1:
                    res = signUp();
                    break;
                case 2:
                    return null;
                default:
                    throw new RuntimeException();
            }
            if (res != null) {
                return res;
            }
        }
    }

    public Player login() {
        while (true) {
            System.out.println("Login with credentials or insert -1 to return");
            System.out.print("Username:");
            String user = sc.nextLine();
            if (user.equals("-1")) {
                return null;
            } else {
                JSONObject userData = findUser(user);
                if (userData != null) {
                    if (checkPassword(user)) {
                        return new Player(userData.getInt("id"),user);
                    } else {
                        System.out.println("Wrong password.");
                    }
                } else {
                    System.out.println("Username not found.");
                }
            }
        }
    }

    //EFFECTS: Prompts user to enter a password insisting until:
    //             A -1 is inserted, returning false
    //             Or a password matching the one stored in users.json, returning true
    public boolean checkPassword(String user) {
        JSONObject userData = findUser(user);
        while (true) {
            String password = userData.getString("password");
            System.out.println("Insert password for " + user + " or insert -1 to return");
            System.out.print("Password:");
            String pass = sc.nextLine();

            if (pass.equals("-1")) {
                return false;
            }

            if (hash256(pass).equals(password)) {
                return true;
            } else {
                System.out.println("Password is incorrect.");
            }
        }
    }


    public JSONObject findUser(String user) {
        for (Object ud : users) {
            JSONObject userData = (JSONObject) ud;
            String username = userData.getString("name");
            if (username.equals(user)) {
                return userData;
            }
        }
        return null;
    }

    public String findName(int id) {
        for (Object ud : users) {
            JSONObject userData = (JSONObject) ud;
            int currentId = userData.getInt("id");
            if (id == currentId) {
                return userData.getString("name");
            }
        }
        return null;
    }


    public Player signUp() {
        while (true) {
            System.out.println("Insert a username, or -1 to return:");
            String user = sc.nextLine();
            if (user.equals("-1")) {
                return null;
            }

            if (findUser(user) == null) {
                String hash = getHashedPassword();
                if (hash != null) {
                    try {
                        return createNewUser(user, hash);
                    } catch (IOException e) {
                        throw new EssentialFileFailed();
                    }
                }
            } else {
                System.out.println("Username already taken");
            }

        }
    }

    //EFFECT: Asks for a password, returns null if input is -1,
    //        It keeps asking until a password >=8 long is inserted, and re-entered correctly, returns that password
    public String getHashedPassword() {
        while (true) {
            System.out.println("Insert a password, or -1 to return:");
            String password = sc.nextLine();
            if (password.equals("-1")) {
                return null;
            } else if (password.length() < 8) {
                System.out.println("Password must be at least 8 characters long.");
            } else {
                System.out.println("Confirm password:");
                String confirmPass = sc.nextLine();
                if (confirmPass.equals(password)) {
                    return  hash256(password);
                } else {
                    System.out.println("Passwords did not match.");
                }
            }
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

    //MODIFIES: this
    //EFFECT: Adds the user data to users.json and creates a corresponding file in teams and battles
    private Player createNewUser(String username, String password) throws IOException {
        int id = getHighestId(users) + 1;

        Player p = new Player(id,username);
        JSONObject userData = p.toJson();
        userData.put("password",password);
        users.put(userData);
        saveUsers();

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

    //EFFECT: hashes given string to sha256
    private static String hash256(String input) {
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(input);
    }
}
