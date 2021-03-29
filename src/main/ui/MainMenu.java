package ui;

import model.Player;
import ui.gui.BaseFrame;
import ui.gui.Menu;
import ui.gui.Style;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class MainMenu extends BaseFrame {
    private final JLabel welcome;


    public MainMenu(Player player, BaseFrame prev) {
        super("Main Menu", prev);
        this.player = player;
        setLayout(new GridBagLayout());

        welcome = new JLabel("Welcome " + player.getName());
        welcome.setFont(new Font("sans serif",Font.BOLD,128));
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.weighty = 2;
        add(welcome,labelConstraints);





        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 0;
        menuConstraints.gridy = 1;
        menuConstraints.weighty = 3;
        menuConstraints.anchor = GridBagConstraints.NORTH;
        add(createMenu(),menuConstraints);



    }

    //MODIFIES: this
    //EFFECTS: Creates the main menu, allowing users to enter a battle, edit teams or edit profile
    public ui.gui.Menu createMenu() {
        Font buttonFont = new Font("sans serif", Font.BOLD,64);
        Border border = BorderFactory.createCompoundBorder(Style.ETCHED,Style.PADDING);
        ui.gui.Menu menu = new Menu(new Style(buttonFont, Color.WHITE,Color.BLACK,border),50,true);

        menu.addButton("ENTER BATTLE",e -> new EnterBattle(player,this),0);

        menu.addButton("EDIT TEAMS",e -> new TeamManager(this),1);

        menu.addButton("PROFILE",e -> new Profile(this),2);

        menu.addButton("PROFPEDIA",e -> new Pedia(this),3);



        return menu;
    }

    public void updateWelcome() {
        welcome.setText("Welcome " + player.getName());
    }
}
