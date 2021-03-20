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

    //EFFECTS: changes the default font sizes for OptionPane objects(including TextField)
    public static void main(String[] args) {
        //CITATION:https://stackoverflow.com/questions/26913923/how-do-you-change-the-size-and-font-of-a-joptionpane
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 32));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 32));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 50));

        new UserManager();
    }




//    public static void startBattle(Player user) {
//        Player player2 = chooseOpponent();
//        if (player2 != null) {
//            if (user.readyUp()) {
//                if (new UserManager().findName(player2.getId()) != null) {
//                    player2.readyUp();
//                } else {
//                    player2.readyUp(user);
//                }
//
//            }
//        }
//        if (player2 != null && user.isReady() && player2.isReady()) {
//            Battle battle = new Battle(user,player2);
//            new BattleManager(battle).resume();
//        }
//
//
//        user.unready();
//    }
//
//    public static Player chooseOpponent() {
//        while (true) {
//            int selection = chooseOptions("Log in or play as guest.",new String[]{"Login","Play as guest"},true);
//
//            switch (selection) {
//                case 0:
//                    return new UserManager().login();
//                case 1:
//                    return new Player(-1,"Guest");
//                default:
//                    return null;
//            }
//        }
//    }

//    public static void chooseBattle(Player user) {
//        while (true) {
//            SaveAble[] battles =  user.loadBattles();
//            int battleIndex = chooseOptions("Choose a Battle to Load:", getNames(battles),true);
//            if (battleIndex == battles.length) {
//                break;
//            } else {
//                Battle battle = (Battle) battles[battleIndex];
//                String oppName = userManager.findName(battle.getPlayer2().getId());
//                if (oppName == null || (oppName != null && userManager.checkPassword(oppName))) {
//                    battle.logRounds();
//                    new BattleManager(battle).resume();
//                    user.unready();
//                }
//
//            }
//        }
//    }



}
