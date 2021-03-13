package ui;

import model.Player;
import model.Professional;
import model.Team;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import model.moves.NonDamaging;
import persistence.SaveAble;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

import static javax.swing.JOptionPane.showInputDialog;
import static ui.CustomBagConstraints.customConstraint;
import static ui.UiManager.*;
import static ui.UiManager.largeOptions;

public class TeamManager extends BaseFrame {

    private final Player player;
    private Team team;
    private int teamIndex;

    private Menu teamMenu;

    private JLabel teamName;
    private Menu membersMenu;

    private JPanel memberEditor;
    private JLabel profIcon;
    private JComboBox<ProfessionalBase> memberBase;
    private JComboBox<String>[] moves;


    //EFFECTS: Creates JFrame with title "Team Builder", sets layout to GridBagLayout and displays the player's name,
    //         the list of all teams created by the player, a button to add a new team and empty team editor
    public TeamManager(Player player) {
        super("Team Builder", Color.WHITE);
        setLayout(new GridBagLayout());

        this.player = player;

        JLabel title = new JLabel(player.getName() + " | Team Builder");
        title.setFont(new Font("sans serif",Font.BOLD,128));
        add(title, CustomBagConstraints.HEADER_BANNER.getConstraint());

        buildTeamMenu();
        buildTeamEditor();

    }

    private void buildTeamMenu() {
        if (teamMenu != null) {
            this.remove(teamMenu);
        }
        Font buttonFont = new Font("sans serif", Font.BOLD,64);
        teamMenu = new Menu(new Style(buttonFont,Color.WHITE,new Color(0,64,128)),0, true);

        SaveAble[] teams = player.loadTeams();


        for (int i = 0; i < teams.length; i++) {
            final int index = i;
            JButton newButton = new JButton(teams[i].getName());

            newButton.addActionListener(e -> displayTeamEditor((Team) teams[index],index));
            teamMenu.addButton(newButton,i);
        }
        JButton addTeam = new JButton("+ Create a New Team");
        addTeam.addActionListener(e -> createNewTeam());
        teamMenu.addButton(addTeam,teams.length);

        GridBagConstraints menuConstraints = customConstraint(0,1,1,2);
        menuConstraints.anchor = GridBagConstraints.NORTHWEST;
        menuConstraints.weighty = 1;
        add(teamMenu,menuConstraints);
        revalidate();
        repaint();
    }

    private void buildTeamEditor() {
        JPanel container = new JPanel(new GridBagLayout());

        teamName = new JLabel("Select a team from the menu on the right");
        teamName.setFont(new Font("sans serif",Font.ITALIC,64));
        container.add(teamName,customConstraint(0,0,1.0,0));

        add(container,customConstraint(1,1,1.0,0));


        buildMemberMenu();
        buildMemberEditor();

        container.add(membersMenu,customConstraint(0,1,2,1));
        GridBagConstraints constraints = customConstraint(0,2,2,1);
        constraints.weighty = 1.0;
        container.add(memberEditor,constraints);


    }

    //EFFECTS: Creates a horizontal menu to select members of a team(buttons are initially empty)
    private void buildMemberMenu() {
        membersMenu = new Menu(null,0,false);
        membersMenu.setVisible(false);

        for (int i = 0; i < 6; i++) {
            membersMenu.addButton(new JButton(),i);
        }
    }

    private void buildMemberEditor() {
        memberEditor = new JPanel(new GridBagLayout());
        memberEditor.setVisible(false);

        profIcon = new JLabel();
        memberEditor.add(profIcon,customConstraint(0,0,1,8));

        //Display base of member
        memberBase = new JComboBox<>(ProfessionalBase.values());
        memberEditor.add(memberBase,customConstraint(0,1,1,4));

        String[] moveList = Move.listAllMoves();
        moves = new JComboBox[4];
        for (int i = 0; i < 4; i++) {
            moves[i] = new JComboBox<>(moveList);
            moves[i].setFont(new Font("sans serif",));
            memberEditor.add(moves[i],customConstraint(1,3 * i,1,3));
        }

    }

    private void displayTeamEditor(Team team, int teamIndex) {
        this.team = team;
        this.teamIndex = teamIndex;


        teamName.setText(team.getName());
        displayMembers();

        displayMemberEditor(team.getMembers()[0]);

        revalidate();
        repaint();
    }

    public void displayMembers() {

        Professional[] members = team.getMembers();
        LinkedList<JButton> buttons = membersMenu.getButtons();
        Font buttonFont = new Font("sans serif", Font.BOLD,48);

        for (int i = 0; i < members.length; i++) {
            Professional p = members[i];
            Style buttonStyle =  new Style(buttonFont,p.getBase().getBranch1().getColor());
            JButton b = buttons.get(i);
            b.setText(p.getName());
            buttonStyle.applyToComponent(b);

            final int index = i;
            b.addActionListener(e -> displayMemberEditor(team.getMembers()[index]));
        }

        membersMenu.setVisible(true);
    }

    public void displayMemberEditor(Professional professional) {
        //ProfIcon

        memberBase.setSelectedIndex(professional.getBase().getIndex());
        int damagingLength = Damaging.values().length;
        Move[] profMoves = professional.getMoves();
        for (int i = 0; i < 4; i++) {
            int index = profMoves[i].getIndex() + (profMoves[i] instanceof NonDamaging ? damagingLength : 0);
            try {
                moves[i].setSelectedIndex(index);
            } catch (IllegalArgumentException e) {
                System.out.println(profMoves[i].getName());
            }
        }

        memberEditor.setVisible(true);

    }

    public void createNewTeam() {
        String newName = showInputDialog(null,"Insert new team name.","New Team",JOptionPane.PLAIN_MESSAGE);
        if (newName != null) {
            SaveAble[] teams = player.loadTeams();

            Team newTeam = new Team(player,newName);
            final int index = teams.length;
            JButton newButton = new JButton(newName);

            newButton.addActionListener(e -> displayTeamEditor((Team) teams[index],index));

            teamMenu.addButton(newButton,index);
            newTeam.save(index);

            displayTeamEditor(newTeam,index);
            revalidate();
            repaint();
        }

    }

    public void duplicateTeam(int currentIndex) {

        team.save(currentIndex);
        System.out.println(player.loadTeams()[0].getName());
        String name = team.getName();
        team.setName("Copy of " + name);
        team.save(player.loadTeams().length);
        team.setName(name);
    }





    public void chooseNewBase(Professional professional) {
        ProfessionalBase[] values = ProfessionalBase.values();
        printTable(values[0]);
        int proId = largeOptions("Select new Professional",values.length,true);
        if (proId != -1) {
            professional.setBase(values[proId]);
        }

    }

    public static void editMoveSet(Professional professional) {
        while (true) {
            int moveInd = chooseOptions("Which move do you want to change?",professional.getMoveNames(), true);
            if (moveInd == professional.getMoves().length) {
                break;
            } else {
                chooseMove(professional,moveInd);
            }
        }


    }

    public static void chooseMove(Professional professional, int moveInd) {
        while (true) {
            int type = chooseOptions("What type of move?",new String[]{"Status","Damaging"},true);

            if (type == -1) {
                break;
            } else {
                Move[] values = type == 0 ? NonDamaging.values() : Damaging.values();
                printTable(values[0]);

                int newMoveInd = largeOptions("Select new move.",values.length,true);

                if (newMoveInd != -1) {
                    Move move = (type == 0 ? NonDamaging.values() : Damaging.values())[newMoveInd];
                    if (professional.hasMove(move)) {
                        System.out.println("Move already assigned to Professional");
                    } else {
                        String change = " from " + professional.getMoves()[moveInd].getName() + " to " + move.getName();
                        professional.setMove(moveInd,move);

                        System.out.println("Changed move " + (moveInd + 1) + change);
                        break;
                    }
                }

            }

        }
    }
}
