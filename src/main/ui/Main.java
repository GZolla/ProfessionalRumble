package ui;

import model.Battle;
import model.Player;
import persistence.SaveAble;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;


public class Main {
    public final static BattleManager BATTLEMGR = new BattleManager();

    //EFFECTS: changes the default font sizes for OptionPane objects(including TextField), opens program
    public static void main(String[] args) {
        //CITATION:https://stackoverflow.com/questions/26913923/how-do-you-change-the-size-and-font-of-a-joptionpane
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 32));
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 32));
        UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 50));
        UIManager.put("ComboBox.font", new Font("Arial", Font.PLAIN, 50));

        new UserManager();
    }



}
