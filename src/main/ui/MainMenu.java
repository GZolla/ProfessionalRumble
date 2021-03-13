package ui;

import model.Battle;
import model.Player;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static ui.UiManager.chooseOptions;

public class MainMenu extends BaseFrame {
    private Player user;

    public MainMenu(Player user) {
        super("Main Menu", Color.WHITE);
        setLayout(new GridBagLayout());
        this.user = user;

        JLabel label = new JLabel("Welcome " + user.getName());
        label.setFont(new Font("sans serif",Font.BOLD,128));
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.weighty = 2;
        add(label,labelConstraints);





        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 0;
        menuConstraints.gridy = 1;
        menuConstraints.weighty = 3;
        menuConstraints.anchor = GridBagConstraints.NORTH;
        add(createMenu(),menuConstraints);



    }

    public Menu createMenu() {
        Font buttonFont = new Font("sans serif", Font.BOLD,64);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createEtchedBorder(new Color(80,80,80),new Color(120,120,120)),
                Style.padding
        );
        Menu menu = new Menu(new Style(buttonFont, Color.WHITE,Color.BLACK,border),50,true);



        JButton startBattle = new JButton("NEW BATTLE");
        menu.addButton(startBattle,0);

        JButton loadBattle = new JButton("LOAD BATTLE");
        loadBattle.setEnabled(false);
        menu.addButton(loadBattle,1);

        JButton editTeams = new JButton("EDIT TEAMS");
        editTeams.addActionListener(e -> new TeamManager(user));
        menu.addButton(editTeams,2);

        JButton profile = new JButton("PROFILE");
        menu.addButton(profile,3);



        return menu;
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
}
