package ui;

import model.Professional;
import model.Team;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import model.moves.NonDamaging;
import persistence.SaveAble;
import ui.gui.BaseFrame;
import ui.gui.Menu;
import ui.gui.Style;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

import static java.awt.GridBagConstraints.BOTH;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.JOptionPane.showMessageDialog;
import static ui.gui.CustomBagConstraints.customConstraint;

public class TeamManager extends BaseFrame {

    private Team team;
    private int teamIndex;
    private Professional professional;

    private ui.gui.Menu teamMenu;

    private JLabel teamName;
    private ui.gui.Menu teamOptions;
    private ui.gui.Menu membersMenu;

    private JPanel memberEditor;
    private JLabel profIcon;
    private JComboBox<String> memberBase;
    private JComboBox<String>[] moves;


    //EFFECTS: Creates JFrame with title "Team Builder", sets layout to GridBagLayout and displays the player's name,
    //         the list of all teams created by the player, a button to add a new team and empty team editor
    public TeamManager(BaseFrame prev) {
        super("Team Builder", Color.WHITE,prev);
        setLayout(new GridBagLayout());


        buildHeaderAndReturn("Team Builder");
        buildTeamMenu();
        buildTeamEditor();
        revalidate();
        repaint();
    }

    //MODIFIES: this
    //EFFECTS: builds a menu with all teams of the user
    private void buildTeamMenu() {
        if (teamMenu != null) {
            this.remove(teamMenu);
        }
        Font buttonFont = new Font("sans serif", Font.BOLD,64);
        teamMenu = new ui.gui.Menu(new Style(buttonFont,Color.WHITE,new Color(0,64,128)),0, true);

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

        GridBagConstraints menuConstraints = customConstraint(0,1,2,3);
        menuConstraints.weighty = 1;
        menuConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(teamMenu,menuConstraints);
    }

    //MODIFIES: this
    //EFFECTS: builds a JLabel for team names, the team options, the member menu and the member editor
    private void buildTeamEditor() {
        teamName = new JLabel("Select a team from the menu on the right");
        teamName.setFont(new Font("sans serif",Font.ITALIC,64));
        add(teamName,customConstraint(1,1,1.0,0));

        Style style = new Style(new Font("sans serif",Font.PLAIN,56),Color.BLACK,Color.WHITE);
        teamOptions = new ui.gui.Menu(style,0,true);
        teamOptions.setVisible(false);

        JButton delete = new JButton("Delete");
        delete.addActionListener(e -> removeTeam());
        teamOptions.addButton(delete,0);
        JButton duplicate = new JButton("Duplicate");
        duplicate.addActionListener(e -> duplicateTeam());
        teamOptions.addButton(duplicate,0);
        add(teamOptions,customConstraint(2,1,1,2));

        buildMemberMenu();
        buildMemberEditor();
    }

    //MODIFIES: this
    //EFFECTS: Creates a horizontal menu to select members of a team(initially is not visible and without buttons)
    private void buildMemberMenu() {
        membersMenu = new Menu(null,0,false);
        add(membersMenu,customConstraint(1,2,1,1));
        membersMenu.setVisible(false);

        for (int i = 0; i < 6; i++) {
            membersMenu.addButton(new JButton(),i);
        }
    }

    //MODIFIES: this
    //EFFECTS: build the JPanel for the member editor, adding JComboBoxes for the base and moves of a professional
    private void buildMemberEditor() {
        memberEditor = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = customConstraint(1,3,1.0,1.0);
        constraints.fill = BOTH;
        constraints.gridwidth = 2;
        add(memberEditor,constraints);
        memberEditor.setVisible(false);


        profIcon = new JLabel("",null,SwingConstants.CENTER);
        profIcon.setVerticalAlignment(SwingConstants.CENTER);
        constraints = customConstraint(0,0,3.0,0);
        constraints.gridheight = 8;
        memberEditor.add(profIcon,constraints);

        //Display base of member
        memberBase = new JComboBox<>(ProfessionalBase.listAllBases());
        memberBase.addActionListener(e -> setNewBase());
        memberBase.setFont(new Font("sans serif", Font.PLAIN,32));
        memberEditor.add(memberBase,customConstraint(0,8,1,4));


        buildMoves();
    }

    //MODIFIES: this
    //EFFECTS: builds and sets the JComBox for moves
    public void buildMoves() {
        String[] moveList = Move.listAllMoves();
        moves = new JComboBox[4];
        for (int i = 0; i < 4; i++) {
            final int index = i;
            moves[i] = new JComboBox<>(moveList);
            moves[i].addActionListener(e -> setMove(index));
            moves[i].setFont(new Font("sans serif", Font.PLAIN,32));
            GridBagConstraints constraints = customConstraint(1,3 * i,1.0,1.0);
            constraints.gridheight = 3;
            memberEditor.add(moves[i],constraints);
        }
    }

    //MODIFIES: this
    //EFFECTS: Sets team and teamIndex, displays name and updates member's menu, displays memberEditor for first member
    private void displayTeamEditor(Team team, int teamIndex) {
        this.team = team;
        this.teamIndex = teamIndex;


        teamName.setText(team.getName());
        displayMembers();

        teamOptions.setVisible(true);

        displayMemberEditor(team.getMembers()[0]);

        revalidate();
        repaint();
    }

    public void displayMembers() {

        Professional[] members = team.getMembers();
        Component[] buttons = membersMenu.getComponents();
        Font buttonFont = new Font("sans serif", Font.BOLD,48);

        for (int i = 0; i < members.length; i++) {
            Professional p = members[i];
            Style buttonStyle =  new Style(buttonFont,p.getBase().getBranch1().getColor());
            JButton b = (JButton) buttons[i];
            b.setText(p.getName());
            buttonStyle.applyToComponent(b);

            final int index = i;
            b.addActionListener(e -> displayMemberEditor(team.getMembers()[index]));
        }

        membersMenu.setVisible(true);
    }

    //MODIFIES: this
    //EFFECTS: sets professional, display the icon for its base, set corresponding index for memberBase and all moves
    public void displayMemberEditor(Professional professional) {
        this.professional = professional;

        setProfIcon();

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

    public void setProfIcon() {
        String src = "./data/icons/" + professional.getBase().getIndex() + ".png";
        int scale = 800;
        Image image = new ImageIcon(src).getImage().getScaledInstance(scale, scale, Image.SCALE_DEFAULT);
        profIcon.setIcon(new ImageIcon(image));
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

    public void duplicateTeam() {
        Team newTeam = new Team(player,team.toJson());
        newTeam.setName("Copy of " + team.getName());

        int index = player.loadTeams().length;
        newTeam.save(index);
        JButton newButton = new JButton(newTeam.getName());
        newButton.addActionListener(e -> displayTeamEditor(newTeam,index));
        teamMenu.addButton(newButton,index);
        displayTeamEditor(newTeam,index);

    }

    public void removeTeam() {
        player.removeTeam(teamIndex);
        teamName.setText("Select a team from the menu on the right");
        buildTeamMenu();
        membersMenu.setVisible(false);
        teamOptions.setVisible(false);
        memberEditor.setVisible(false);
    }





    public void setNewBase() {
        professional.setBase(ProfessionalBase.values()[memberBase.getSelectedIndex()]);
        team.save(teamIndex);

        displayMembers();
        setProfIcon();
    }



    public void setMove(int moveIndex) {
        Damaging[] dmg = Damaging.values();
        NonDamaging[] nonDMG = NonDamaging.values();



        int newMoveIndex = moves[moveIndex].getSelectedIndex();
        Move newMove = newMoveIndex < dmg.length ? dmg[newMoveIndex] : nonDMG[newMoveIndex - dmg.length];

        if (professional.hasMove(newMove)) {
            Move prevMove = professional.getMoves()[moveIndex];
            if (newMove != prevMove) {
                int prevMoveIndex = prevMove.getIndex() - (prevMove instanceof NonDamaging ? dmg.length : 0);
                showMessageDialog(null,"Move already assigned to Professional");
                moves[moveIndex].setSelectedIndex(prevMoveIndex);
            }
        } else {
            professional.setMove(moveIndex,newMove);
        }
    }
}
