package ui;

import model.Player;
import model.Professional;
import model.Round;
import model.Team;
import model.data.NonVolatile;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import persistence.SaveAble;
import ui.gui.BaseFrame;
import ui.gui.Menu;
import ui.gui.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import static java.awt.GridBagConstraints.BOTH;
import static javax.swing.JOptionPane.*;
import static model.Round.parseIndex;
import static model.data.NonVolatile.FAINT;
import static ui.Main.BATTLEMGR;
import static ui.UiManager.showMessage;
import static ui.UiManager.showOnSecondScreen;
import static ui.gui.CustomBagConstraints.customConstraint;


//A BaseFrame that displays the battle for one of the players in it
public class BattleDisplay extends BaseFrame {

    private Menu teamMenu;

    private Menu moveMenu;
    private Menu professionalMenu;
    private Menu actionMenu;

    private JLabel log;
    private JLayeredPane main;

    private final JComponent[] playerItems;
    private final JComponent[] opponentItems;

    private static final int ICON_SIZE = 8;


    public BattleDisplay(Player player) {
        super(BATTLEMGR.getTitle(), null);
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        if (!BATTLEMGR.isPlayer1(player)) {
            showOnSecondScreen(this);
        }



        this.player = player;
        setExitAction();

        playerItems = getDetailComponents();
        opponentItems = getDetailComponents();


        if (player.getTeam() != null) {
            buildDisplays();
        } else {
            buildTeamMenu();
        }
    }

    //MODIFIES: this
    //EFFECTS: sets the exiting action for the BaseFrame
    public void setExitAction() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {

                if (player.getTeam() != null && player.getTeam() != null) {
                    int close = showConfirmDialog(
                                    BATTLEMGR.getDisplay(player,false),
                                    "Save & Exit?","Exiting",JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.PLAIN_MESSAGE,null
                                );
                    if (close != OK_OPTION) {
                        return;
                    }
                }
                showMessage(BATTLEMGR.getDisplay(player,true),"Opponent Exited Battle, battle saved","Exiting");
                BATTLEMGR.endBattle();
            }
        });
    }

//--- SELECT TEAM ------------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: Builds and displays a menu of existing teams for users to select one
    public void buildTeamMenu() {
        teamMenu = new Menu(Style.getButtonStyle(64),50,true);
        SaveAble[] teams = (player.getId() == -1 ? BATTLEMGR.getOpponent(player) : player).loadTeams();
        for (int i = 0; i < teams.length; i++) {
            Team t = (Team) teams[i];
            teamMenu.addButton(t.getName(),e -> setTeam(t),i);
        }

        add(teamMenu,customConstraint(0,0));
    }

    //MODIFIES: this
    //EFFECTS: sets the battle properties for player with given team
    public void setTeam(Team t) {
        teamMenu.setVisible(false);
        player.setBattleProperties(t);

        buildDisplays();

        BATTLEMGR.updateOpponentIcons();
    }

//--- BUILD GUI --------------------------------------------------------------------------------------------------------

    //EFFECTS: builds an array of non-formatted JComponents made of three JLabels and a JProgressBar in that order
    public JComponent[] getDetailComponents() {
        JComponent[] array = new JComponent[4];
        for (int i = 0; i < 3; i++) {
            array[i] = new JLabel();
        }
        array[3] = new JProgressBar(0,1);

        return array;
    }

    //MODIFIES: this
    //EFFECTS: builds menus and main display for the player
    public void buildDisplays() {
        actionMenu = new Menu(new Style(new Font("sans serif",Font.PLAIN,60),Color.WHITE,Style.PADDING),0,false);
        actionMenu.addButton("MOVE",e -> toggleActionDisplay(true),0);
        actionMenu.addButton("SWITCH",e ->  toggleActionDisplay(false),1);
        add(actionMenu,customConstraint(1,0));

        buildMenus();
        buildLog();
        buildBattlefield();

        changeEnableAll(false);
        log.setText("Waiting for opponent");

        revalidate();
        repaint();
    }



    //MODIFIES: this
    //EFFECTS: Builds moveMenu and professionalMenu, displays moveMenu
    public void buildMenus() {
        moveMenu = new Menu(Style.getButtonStyle(50),0,true);
        Move[] moves = player.getSelectedProfessional().getMoves();
        for (int i = 0; i < 4; i++) {
            final int index = i;
            moveMenu.addButton(moves[i].getName(),e -> setAction(new int[]{0,index}),i);
        }

        professionalMenu = new Menu(Style.getButtonStyle(50),0,true);
        Professional[] members = player.getTeamMembers();
        for (int i = 0; i < 6; i++) {
            final int index = i;
            professionalMenu.addButton(members[i].getName(),e -> setProfessional(index),i);
        }

        GridBagConstraints constraints = customConstraint(1,1,1,1);
        constraints.fill = BOTH;
        add(professionalMenu,constraints);
        add(moveMenu,constraints);

        toggleActionDisplay(true);
    }


    //EFFECTS: Builds the main display of the battle
    private void buildBattlefield() {
        main = new JLayeredPane();
        GridBagConstraints constraints = customConstraint(0,0,1.0,1.0);
        main.setBackground(new Color(60,150,230));
        main.setOpaque(true);
        constraints.gridheight = 2;
        constraints.fill = BOTH;
        add(main,constraints);

        repaint();
        revalidate();

        JLabel ground = new JLabel();
        ground.setOpaque(true);
        Dimension dim = main.getSize();
        ground.setBounds(0,dim.height / 4,dim.width,dim.height - dim.height / 4);
        ground.setBackground(new Color(30,130,80));
        main.add(ground,new Integer(1));

        buildItems(true);



        updateProfessional(true);

        repaint();
        revalidate();
    }

    //MODIFIES: this
    //EFFECTS: builds the log bar
    public void buildLog() {
        log = new JLabel();
        log.setFont(new Font("sans serif",Font.PLAIN,60));
        log.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK,10),
                BorderFactory.createEmptyBorder(50,100,50,100)
        ));
        GridBagConstraints constraints = customConstraint(0,2,2,1);
        constraints.fill = BOTH;
        add(log,constraints);
    }

    //MODIFIES: this
    //EFFECTS: builds the icon, name, non volatile status and health bar in correct positions
    public void buildItems(boolean isPlayer) {
        JComponent[] items = (isPlayer ? playerItems : opponentItems);
        Dimension mainDim = main.getSize();
        int padding = mainDim.height / 6;

        int barWidth = mainDim.width / 3;
        int barHeight = 50;

        int labelHeight = 150;
        int nvsWidth = 300;

        int rightMargin = mainDim.width - padding;
        int detailHeight = barHeight + labelHeight;

        int iconSize = ((ICON_SIZE - (isPlayer ? 0 : 2)) * mainDim.height) / 16;
        int iconYPosition = (isPlayer ? mainDim.height - padding : mainDim.height / 2) - iconSize;
        items[0].setBounds(isPlayer ? padding : rightMargin - iconSize,iconYPosition,iconSize,iconSize);

        int detailsXPosition = isPlayer ? padding + iconSize : rightMargin - iconSize - barWidth;
        int detailsYPosition = iconYPosition + (isPlayer ? iconSize  - detailHeight : 0);
        items[1].setBounds(detailsXPosition,detailsYPosition,barWidth, labelHeight);
        items[2].setBounds(detailsXPosition + barWidth - nvsWidth,detailsYPosition, nvsWidth,labelHeight);
        items[3].setBounds(detailsXPosition,detailsYPosition + labelHeight,barWidth,barHeight);

        ((JProgressBar) items[3]).setStringPainted(true);

        int depth = isPlayer ? 9 : 5;
        for (int i = 0; i < 4; i++) {
            items[i].setFont(new Font("sans serif",Font.PLAIN,80 - 16 * i));
            main.add(items[i],new Integer(depth - i));
        }

    }

//--- UPDATE GUI -------------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: Fill moveMenu with moves of given professional
    public void updateMoveMenu() {
        Move[] moves = player.getSelectedProfessional().getMoves();
        for (int i = 0; i < 4; i++) {
            JButton button = (JButton) moveMenu.getComponent(i);

            button.setText(moves[i].getName());
        }
    }

    //MODIFIES: this
    //EFFECTS: updates professional icon of player to selected professional
    public void updateProfessional(boolean isPlayer) {
        Professional pr = (isPlayer ? player : BATTLEMGR.getOpponent(player)).getSelectedProfessional();
        updateProfessional(isPlayer,pr.getBase(),pr.getLife(),pr.getNonVolatileStatus());
    }


    //MODIFIES: this
    //EFFECTS: updates professional icon of player(isPlayer) or opponent to given ProfessionalBase and life
    public void updateProfessional(boolean isPlayer, ProfessionalBase base, int life, NonVolatile status) {
        String src = "./data/icons/" + base.getIndex() + ".png";
        int scale = ((ICON_SIZE - (isPlayer ?  0 : 2)) * main.getSize().height) / 16;
        Image image = new ImageIcon(src).getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        JComponent[] items = isPlayer ? playerItems : opponentItems;
        ((JLabel) items[0]).setIcon(new ImageIcon(image));
        ((JLabel) items[1]).setText(base.getName());
        JLabel statusLabel = ((JLabel) items[2]);
        statusLabel.setText(status != null ? status.getName() : "");
        statusLabel.setBackground(status != null ? status.getColor() : Color.WHITE);
        statusLabel.setOpaque(status != null);


        updateLife(isPlayer,life,base.getLife());

        if (isPlayer) {
            updateMoveMenu();
        }
    }


    //MODIFIES: this
    //EFFECTS: updates the player's(if isPlayer) or the opponent's health bar to given value and max
    public void updateLife(boolean isPlayer,int value,int max) {
        JProgressBar bar = (JProgressBar) (isPlayer ? playerItems : opponentItems)[3];
        bar.setMaximum(max);
        bar.setValue(value);
        bar.setString(value + " / " + max);
        bar.repaint();
    }

    //MODIFIES: this
    //EFFECTS: updates log to given text
    public void setLogText(String message) {
        log.setText(message);
        log.repaint();
    }

//--- CHANGE ENABLED INPUTS --------------------------------------------------------------------------------------------

    //EFFECTS: disable all buttons for user
    public void changeEnableAll(boolean enable) {
        actionMenu.changeEnable(enable);
        moveMenu.changeEnable(enable);
        professionalMenu.changeEnable(enable);
    }

    //EFFECTS: enables all buttons and display a prompt in log
    public void chooseAction() {
        changeEnableAll(true);
        log.setText("Choose what to do...");
    }

    //EFFECTS: only allows the user to choose a professional
    public void chooseProfessional() {
        changeEnableAll(false);
        toggleActionDisplay(false);
        professionalMenu.changeEnable(true);
    }



//---- HANDLE INPUT ----------------------------------------------------------------------------------------------------

    //MODIFIES: this
    //EFFECTS: Displays moveMenu(if move is true) or professionalMenu
    public void toggleActionDisplay(boolean move) {
        moveMenu.setVisible(move);
        professionalMenu.setVisible(!move);

        actionMenu.getComponent(move ? 1 : 0).setBackground(Color.GRAY);
        actionMenu.getComponent(move ? 0 : 1).setBackground(Color.WHITE);

    }

    //MODIFIES: this
    //EFFECTS: sets the action to given, if both actions are set, resume battle
    public void setAction(int[] action) {
        changeEnableAll(false);
        boolean usedDMG = action[0] == 0 && player.getSelectedProfessional().getMoves()[action[1]] instanceof Damaging;
        if (usedDMG && player.canUseCritical()) {
            int res = showConfirmDialog(this,"Use a critical hit?","Critical",YES_NO_OPTION,PLAIN_MESSAGE);
            if (res == YES_OPTION) {
                player.useCritical();
            }
        }

        log.setText("Waiting for opponent...");
        BATTLEMGR.setAction(player,action);
    }

    //MODIFIES: this
    //EFFECTS: if player's selected professional is fainted !!!
    //         otherwise set action to switching to professional in given index
    public void setProfessional(int index) {
        if (player.getTeam().getMembers()[index].getNonVolatileStatus() == FAINT) {
            showMessage(this,"Cannot select a fainted professional","Invalid Selection");
        } else if (index == player.getSelectedProfessionalIndex()) {
            showMessage(this,"Professional already selected","Invalid Selection");
        } else {
            if (player.getSelectedProfessional().getNonVolatileStatus() == NonVolatile.FAINT) {
                ArrayList<Round> rounds = BATTLEMGR.getBattle().getRounds();
                Round round = rounds.get(rounds.size() - 1);
                boolean first = round.wentFirst(player);

                int[] action = round.getAction(first);
                action[1] = parseIndex(action[1],true) + 4 * (index + 2);
                if (first) {
                    round.setActionFirst(action);
                } else {
                    round.setActionSecond(action);
                }
                player.getSelectedProfessional().tapOut(index);

                updateProfessional(true);
                BATTLEMGR.nextAction();

            } else {
                setAction(new int[]{1,index});
            }
        }

    }
}
