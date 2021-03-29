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

import static java.awt.GridBagConstraints.BOTH;
import static javax.swing.JOptionPane.showInputDialog;
import static ui.UiManager.showMessage;
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
    private JComboBox<ProfessionalBase> memberBase;
    private JComboBox<Move>[] moves;


    //EFFECTS: Creates JFrame with title "Team Builder", sets layout to GridBagLayout and displays the player's name,
    //         the list of all teams created by the player, a button to add a new team and empty team editor
    public TeamManager(BaseFrame prev) {
        super("Team Builder",prev);
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
        teamMenu = new ui.gui.Menu(Style.getButtonStyle(64),0, true);

        SaveAble[] teams = player.loadTeams();


        for (int i = 0; i < teams.length; i++) {
            final int index = i;
            teamMenu.addButton(teams[i].getName(),e -> displayTeamEditor((Team) teams[index],index),i);
        }
        teamMenu.addButton("+ Create a New Team",e -> createNewTeam(),teams.length);

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
        add(teamName,customConstraint(2,1,1.0,0));

        Style style = new Style(new Font("sans serif",Font.PLAIN,56),Color.BLACK,Color.WHITE);
        teamOptions = new ui.gui.Menu(style,0,true);
        teamOptions.setVisible(false);

        teamOptions.addButton("Delete",e -> removeTeam(),0);

        teamOptions.addButton("Duplicate",e -> duplicateTeam(),0);

        add(teamOptions,customConstraint(3,1,1,2));

        buildMemberMenu();
        buildMemberEditor();
    }

    //MODIFIES: this
    //EFFECTS: Creates a horizontal menu to select members of a team(initially is not visible and without buttons)
    private void buildMemberMenu() {
        membersMenu = new Menu(null,0,false);
        add(membersMenu,customConstraint(2,2,1,1));
        membersMenu.setVisible(false);

        for (int i = 0; i < 6; i++) {
            membersMenu.addButton("",null,i);
        }
    }

    //MODIFIES: this
    //EFFECTS: build the JPanel for the member editor, adding JComboBoxes for the base and moves of a professional
    private void buildMemberEditor() {
        memberEditor = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = customConstraint(2,3,1.0,1.0);
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
        memberBase = new JComboBox<>(ProfessionalBase.values());
        memberBase.addActionListener(e -> setNewBase());
        memberBase.setFont(new Font("sans serif", Font.PLAIN,32));
        memberEditor.add(memberBase,customConstraint(0,8,1,4));


        buildMoves();
    }

    //MODIFIES: this
    //EFFECTS: builds and sets the JComBox for moves
    public void buildMoves() {
        Move[] moveList = Move.listAllMoves();
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
            Style buttonStyle =  new Style(buttonFont,p.getBase().getBranch1().getColor(),Style.PADDING);
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
            moves[i].setSelectedIndex(index);
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
        String newName = showInputDialog(null,"Insert new team name.","New Team");
        if (newName != null) {

            final int index = player.loadTeams().length;
            Team newTeam = new Team(player,newName);
            newTeam.save(index);

            SaveAble[] teams = player.loadTeams();


            teamMenu.addButton(newName,e -> displayTeamEditor((Team) teams[index],index),index);

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

        teamMenu.addButton(newTeam.getName(),e -> displayTeamEditor(newTeam,index),index);
        displayTeamEditor(newTeam,index);

    }

    //EFFECTS: Removes the selected team, resets team menu and hides team editor
    public void removeTeam() {
        player.removeTeam(teamIndex);
        teamName.setText("Select a team from the menu on the right");
        buildTeamMenu();
        membersMenu.setVisible(false);
        teamOptions.setVisible(false);
        memberEditor.setVisible(false);
    }





    public void setNewBase() {
        professional.setBase((ProfessionalBase) memberBase.getSelectedItem());
        team.save(teamIndex);

        displayMembers();
        setProfIcon();
    }



    public void setMove(int moveIndex) {



        Move newMove = (Move) moves[moveIndex].getSelectedItem();

        if (professional.hasMove(newMove)) {
            Move prevMove = professional.getMoves()[moveIndex];
            if (newMove != prevMove) {
                showMessage(this,"Move already assigned to Professional","Invalid Selection");
                moves[moveIndex].setSelectedItem(prevMove);
            }
        } else {
            professional.setMove(moveIndex,newMove);
            team.save(teamIndex);
        }
    }
}
