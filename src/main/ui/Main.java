package ui;

import model.Battle;
import model.Player;
import persistence.SaveAble;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

import static ui.UiManager.chooseOptions;
import static ui.UiManager.getNames;

public class Main {
    private static final UserManager userManager = new UserManager();

    //EFFECT
    public static void main(String[] args) {
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 12));
        System.out.println("Welcome to Professional's Rumble!");

        new MainMenu(new Player(0,"Test1"));





    }

    //EFFECT: outputs user's name and the available actions for the user, returns true if exiting mainMenu
    public static void mainMenu(Player user) {

        while (true) {
            System.out.println("Welcome " + user.getName());
            String[] options = new String[]{"New Battle","Load Battle","Manage Account","Change Account","Exit"};
            switch (chooseOptions("Select an action.",options,false)) {
                case 0:
                    startBattle(user);
                    break;
                case 1:
                    chooseBattle(user);
                    break;
                case 2:
                    new AccountManager(user);
                    break;
                case 3:
                    return;
                case 4:
                    if (chooseOptions("Do you want to exit the program", new String[]{"Yes","No"},false) == 0) {
                        System.exit(0);
                    }
                    break;
            }
        }
    }


    public static void startBattle(Player user) {
        Player player2 = chooseOpponent();
        if (player2 != null) {
            if (user.readyUp()) {
                if (new UserManager().findName(player2.getId()) != null) {
                    player2.readyUp();
                } else {
                    player2.readyUp(user);
                }

            }
        }
        if (player2 != null && user.isReady() && player2.isReady()) {
            Battle battle = new Battle(user,player2);
            new BattleManager(battle).resume();
        }


        user.unready();
    }

    public static Player chooseOpponent() {
        while (true) {
            int selection = chooseOptions("Log in or play as guest.",new String[]{"Login","Play as guest"},true);

            switch (selection) {
                case 0:
                    return new UserManager().login();
                case 1:
                    return new Player(-1,"Guest");
                default:
                    return null;
            }
        }
    }

    public static void chooseBattle(Player user) {
        while (true) {
            SaveAble[] battles =  user.loadBattles();
            int battleIndex = chooseOptions("Choose a Battle to Load:", getNames(battles),true);
            if (battleIndex == battles.length) {
                break;
            } else {
                Battle battle = (Battle) battles[battleIndex];
                String oppName = userManager.findName(battle.getPlayer2().getId());
                if (oppName == null || (oppName != null && userManager.checkPassword(oppName))) {
                    battle.logRounds();
                    new BattleManager(battle).resume();
                    user.unready();
                }

            }
        }
    }


}
