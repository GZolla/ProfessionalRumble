package ui;

import model.Battle;
import model.Player;
import persistence.SaveAble;
import ui.gui.BaseFrame;
import ui.gui.CustomForm;
import ui.gui.Menu;
import ui.gui.Style;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.NORTH;
import static javax.swing.JOptionPane.*;
import static model.Battle.Result.ONGOING;
import static ui.UiManager.showMessage;
import static ui.UserManager.checkCredentials;
import static ui.gui.CustomBagConstraints.customConstraint;
import static ui.gui.CustomForm.Row;
import static ui.gui.Style.getButtonStyle;

public class EnterBattle extends BaseFrame {

    private final CustomForm form;
    private Menu menu;
    private final Battle[] battles;

    public EnterBattle(Player player, BaseFrame prev) {
        super("New Battle", prev);
        setLayout(new GridBagLayout());

        battles = filterBattles();

        player.unready();
        this.player = player;
        buildHeaderAndReturn("Start Battle");

        GridBagConstraints constraints = customConstraint(0,1,1.0,1.0);
        constraints.gridwidth = 2;

        form = new CustomForm(new Row[]{Row.NAME,Row.PASSWORD},"Login",e -> startPlayerBattle());
        add(form,constraints);

        buildMenu();

    }

    //MODIFIES: this
    //EFFECTS: builds the buttons to start battles
    public void buildMenu() {
        Style buttonStyle = getButtonStyle(48);
        buttonStyle.applyToComponent(form.getActionButton());

        menu = new Menu(buttonStyle,50,true);

        menu.addButton("PLAY WITH GUEST",e -> startBattle(new Player(-1,"Guest")),0);

        menu.addButton("LOAD BATTLE",e -> loadBattle(),1);

        GridBagConstraints constraints = customConstraint(0,2,1.0,1.0);
        constraints.gridwidth = 2;
        constraints.anchor = NORTH;
        add(menu,constraints);

        checkConditions();
    }

    //MODIFIES: this
    //EFFECTS: disables new battles if no teams have been created, and loading if no battles have been saved
    //         if no teams have been created, alert the user to that fact
    //         Note that this means that battles(that store teams) can still be loaded even without created teams
    public void checkConditions() {

        if (battles.length == 0) {
            menu.getComponent(1).setEnabled(false);
        }
        if (player.loadTeams().length == 0) {
            form.getActionButton().setEnabled(false);
            menu.getComponent(0).setEnabled(false);
            showMessage(null, "You need to create a team to start new battles!","No Teams Created");
        }
    }

    //MODIFIES: this
    //EFFECTS: start new battle with given player
    private void startBattle(Player player2) {
        setVisible(false);
        Main.BATTLEMGR.loadBattle(new Battle(player,player2),prev);
    }

    //MODIFIES: this
    //EFFECTS: starts given battle
    private void startBattle(Battle battle) {
        setVisible(false);
        Main.BATTLEMGR.loadBattle(battle,prev);
    }

    //EFFECTS: if entered credentials match a user that is not this user, start a new battle with that user
    public void startPlayerBattle() {
        String name = form.getField(0).getText();
        int id = checkCredentials(name,form.getField(1));
        if (id == -1) {
            showMessage(this,"Username or Password is incorrect","Login Failed");
        } else if (id == player.getId()) {
            showMessage(this,"Cannot start a battle with yourself","Login Failed");
        } else {
            startBattle(new Player(id,name));
        }
    }


    //EFFECTS: user selects a battle, loads it if its against a guest or if password for opponent is entered correctly
    public void loadBattle() {
        Battle battle = (Battle) showInputDialog(
                null,"Choose a Battle to Load:","Load Battle",PLAIN_MESSAGE,null,battles,battles[0]
        );
        if (battle != null) {
            Player p2 = battle.getPlayer2();
            if (p2.getId() == -1 || checkPlayerPassword(p2)) {
                player = battle.getPlayer1();
                startBattle(battle);
            }
        }

    }

    //EFFECTS: filters user's battles that are still ongoing
    public Battle[] filterBattles() {
        SaveAble[] all = player.loadBattles();
        ArrayList<Battle> filter = new ArrayList<>();

        for (SaveAble saveAble : all) {
            Battle current = (Battle) saveAble;
            if (current.getResult() == ONGOING) {
                filter.add(current);
            }
        }

        Battle[] arrayFilter = new Battle[filter.size()];
        return filter.toArray(arrayFilter);
    }

    //EFFECTS: prompts user for a password, returns true if selected ok and the password matches the given player's
    //CITATION:https://stackoverflow.com/questions/8881213/joptionpane-to-get-password
    public boolean checkPlayerPassword(Player p) {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Enter "  + p.getName() + "'s password:");
        JPasswordField pass = new JPasswordField(30);
        Font font = new Font("sans serif",Font.PLAIN,30);
        pass.setFont(font);
        label.setFont(font);
        panel.add(label);
        panel.add(pass);
        int option = JOptionPane.showConfirmDialog(
                null, panel, "Login",OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,null
        );
        boolean success = checkCredentials(p.getName(),pass) != -1;
        if (option != OK_OPTION || !success) {
            if (!success) {
                showMessage(null,"Wrong Password","Login Failed");
            }
            return false;
        }
        return true;
    }
}
