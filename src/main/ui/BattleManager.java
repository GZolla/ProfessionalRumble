package ui;

import model.Battle;
import model.Player;
import model.Professional;
import model.Round;
import model.data.NonVolatile;
import model.data.ProfessionalBase;
import ui.gui.BaseFrame;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.util.LinkedList;

import static javax.swing.JOptionPane.showInputDialog;
import static ui.Main.BATTLEMGR;

public class BattleManager {
    private boolean isDelaying;

    private Battle battle;
    private BaseFrame mainMenu;
    
    private int[] action1;
    private int[] action2;

    private BattleDisplay display1;
    private BattleDisplay display2;

    private LinkedList<ActionListener> actions;
    public static final int DELAY = 3000;
    private Timer timer;

    public BattleManager() {

        isDelaying = false;
        actions = new LinkedList<>();
    }

    public void loadBattle(Battle battle, BaseFrame mainMenu) {
        this.battle = battle;
        this.mainMenu = mainMenu;

        battle.logRounds();
        isDelaying = true;

        display1 = new BattleDisplay(battle.getPlayer1());
        display2 = new BattleDisplay(battle.getPlayer2());

        BATTLEMGR.updateOpponentIcons();


    }

    //MODIFIES: this
    //EFFECTS: if neither action is null, creates a new round with those actions, adds it to battle and runs it
    //         checks if battle has concluded and ends battle if it has, logging the result
    public void nextRound() {
        if (action1 != null && action2 != null) {
            Round newRound = new Round(battle,action1,action2);
            action1 = null;
            action2 = null;

            battle.addRound(newRound);
            newRound.handleAll();

            if (battle.getResult() != Battle.Result.ONGOING) {
                log(battle.getResultString());
                addToActions(e -> endBattle());
            }

            timer = new Timer(0,actions.pollFirst());
            timer.start();
        }
    }

    //MODIFIES: this
    //EFFECTS: stops timer if it exists, saves battle and disposes displays, opens mainMenu and resets all fields
    public void endBattle() {
        if (timer != null) {
            timer.stop();
        }
        if (battle.getPlayer1().getTeam() != null && battle.getPlayer2().getTeam() != null) {
            battle.save();
        }
        mainMenu.setVisible(true);
        display1.dispose();
        display2.dispose();

        battle = null;
        display1 = null;
        display2 = null;
        action1 = null;
        action2 = null;
        isDelaying = false;
    }

//--- EXPECT INPUT -----------------------------------------------------------------------------------------------------

    //EFFECTS: prompts user to wage a number of its critical points
    public int handleWage(Player p) {
        int critPoints1 = p.getCritCounter();
        if (critPoints1 == 0) {
            return 0;
        }
        Integer[] options = new Integer[critPoints1 + 1];
        for (int i = 0; i <= critPoints1; i++) {
            options[i] = i;
        }

        String prompt = "You have " + critPoints1 + " critical points. How many do you wish to wager?";
        return (int) showInputDialog(getDisplay(p,false),prompt,"Wager", JOptionPane.PLAIN_MESSAGE,null,options,0);
    }

    //EFFECTS: prompts user to choose a professional
    public void chooseProfessional(Player p) {
        addToActions(e -> getDisplay(p,false).chooseProfessional());

    }

    //EFFECTS: prompt both players for actions
    public void getActions() {
        if (timer != null) {
            timer.stop();
        }
        display1.repaint();
        display2.repaint();
        display1.chooseAction();
        display2.chooseAction();
    }


//--- UPDATE DISPLAYS --------------------------------------------------------------------------------------------------


    //MODIFIES: this
    //EFFECTS: updates the professionals opponent professionals on both displays
    public void updateOpponentIcons() {
        if (battle.getPlayer1().getTeam() != null && battle.getPlayer2().getTeam() != null) {
            display1.buildItems(false);
            display1.updateProfessional(false);
            display2.buildItems(false);
            display2.updateProfessional(false);

            BATTLEMGR.getActions();
        }
    }




    //MODIFIES: this
    //EFFECTS: Adds an action to actions to update the details of the given professional in both displays
    public void updateProfessional(String message, Professional pr) {
        final boolean isP1 = isPlayer1(pr.getLeader());
        final int life = pr.getLife();
        final ProfessionalBase base = pr.getBase();
        final NonVolatile status = pr.getNonVolatileStatus();

        addToActions(e -> {
            display1.setLogText(message);
            display2.setLogText(message);
            display1.updateProfessional(isP1,base,life,status);
            display2.updateProfessional(!isP1,base,life,status);
            nextAction();
        });
    }

    //MODIFIES: this
    //EFFECTS: adds action updating the player's selected prof. on both displays, updates move menu on player's display
    //         logs "tapped out" and "tapped in" messages
    public void updateTapOut(Player player, Professional old) {
        Professional pr = player.getSelectedProfessional();
        updateProfessional(old.getFullName() + " tapped out.",pr);
        log(pr.getFullName() + " tapped in.");
    }

    //MODIFIES: this
    //EFFECTS: sets an action listener that updates the life of given player in both displays
    public void updateLife(Player player, int dmg) {
        Professional sel = player.getSelectedProfessional();
        final int max = sel.getBase().getLife();
        final int value = sel.getLife();
        final String msg = sel.getFullName() + (dmg > 0 ? "lost" : "gained") + " " + Math.abs(dmg) + " health points.";
        final boolean isP1 = isPlayer1(player);
        addToActions(e -> {
            display1.setLogText(msg);
            display2.setLogText(msg);
            display1.updateLife(isP1,value,max);
            display2.updateLife(!isP1,value,max);
            nextAction();
        });
    }

    //EFFECTS: sets an action listener that logs a message in both screens
    public void log(String message) {
        addToActions(e -> {
            display1.setLogText(message);
            display2.setLogText(message);

            nextAction();
        });
    }

//--- HANDLE TIMER -----------------------------------------------------------------------------------------------------

    //EFFECTS: adds given actionListener to the tail of actions if isDelaying
    public void addToActions(ActionListener l) {
        if (isDelaying) {
            actions.offer(l);
        }
    }

    //EFFECTS: updates displays sets a timer for next action in actions
    public void nextAction() {
        display1.repaint();
        display2.repaint();
        timer.stop();
        if (actions.size() == 0) {
            addToActions(e -> getActions());
        }
        timer = new Timer(DELAY,actions.pollFirst());
        timer.start();
    }



//--- ACCESSORS AND MUTATORS -------------------------------------------------------------------------------------------

    //EFFECTS: returns true if given player's id matches player1's id
    public boolean isPlayer1(Player p) {
        return battle.getPlayer1().getId() == p.getId();
    }

    //EFFECTS: Return the title for a BattleDisplay
    public String getTitle() {
        return battle.getPlayer1().getName() + " vs " + battle.getPlayer2().getName();
    }

    //MODIFIES: this
    //EFFECTS: if player matches battle's first player, set action 1 to given action otherwise set action 2
    //         if both actions has been set, run next round
    public void setAction(Player player,int[] action) {
        if (isPlayer1(player)) {
            this.action1 = action;
        } else {
            this.action2 = action;
        }
        nextRound();
    }

    public Player getOpponent(Player p) {
        if (isPlayer1(p)) {
            return battle.getPlayer2();
        } else {
            return battle.getPlayer1();
        }
    }

    public BattleDisplay getDisplay(Player p, boolean opponent) {
        return isPlayer1(p) ^ opponent ? display1 : display2;
    }

    public Battle getBattle() {
        return battle;
    }

    public void setBattle(Battle battle) {
        this.battle = battle;
    }

    public void setDelaying(boolean b) {
        this.isDelaying = b;
    }
}
