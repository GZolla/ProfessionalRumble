package ui;

import model.Player;
import model.Professional;
import model.Team;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import model.moves.Status;
import persistence.SaveAble;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Scanner;

import static java.awt.GridBagConstraints.*;
import static javax.swing.JOptionPane.showInputDialog;
import static ui.UiManager.*;
import static ui.UiManager.largeOptions;

public class TeamManager extends BaseFrame {

    private final Player player;
    private Team team;
    private Menu teamMenu;

    public TeamManager(Player player) {
        super("Team Builder", Color.WHITE);
        setLayout(new GridBagLayout());

        this.player = player;

        JLabel title = new JLabel(player.getName() + " | Team Builder");
        title.setFont(new Font("sans serif",Font.BOLD,128));
        add(title,DefaultBagConstraints.HEADER_BANNER.getConstraint());

        setTeamMenu();
        displayTeamEditor(null);

    }



    public void setTeamMenu() {
        SaveAble[] teams = player.loadTeams();
        JButton[] buttons = new JButton[teams.length + 1];

        for (int i = 0; i < teams.length; i++) {
            JButton newButton = new JButton(teams[i].getName());
            newButton.addActionListener(e -> displayTeamEditor(team));
            buttons[i] = newButton;
        }
        JButton addTeam = new JButton("+ Create a New Team");
        addTeam.addActionListener(e -> createNewTeam());
        buttons[teams.length] = addTeam;

        Font buttonFont = new Font("sans serif", Font.BOLD,64);

        if (teamMenu != null) {
            this.remove(teamMenu);
        }
        teamMenu = new Menu(buttons,new Style(buttonFont,Color.BLACK,new Color(0,64,128)),0, true);

        GridBagConstraints menuConstraints = new GridBagConstraints();
        menuConstraints.gridx = 0;
        menuConstraints.gridy = 1;
        menuConstraints.weighty = 1;
        menuConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(teamMenu,menuConstraints);
        this.revalidate();
        this.repaint();
    }



    public void createNewTeam() {
        String newName = showInputDialog("Insert new team name.");
        if (newName != null) {
            team = new Team(player,newName);
            teamMenu.addButton(new JButton(team.getName()),player.loadTeams().length);
            saveNewTeam();
            this.revalidate();
            this.repaint();
        }

    }

    private void displayTeamEditor(Team team) {
        JPanel container = new JPanel();

        JLabel name = new JLabel(team != null ? team.getName() : "Select a team from the menu on the right");
        if (team == null) {
            container.add(name);
        } else {
            container.setLayout(new GridBagLayout());
            Menu members = createMemberMenu(team);
            container.add(name,new GridBagConstraints(0, 0,REMAINDER,1,1,0,NORTHEAST,BOTH,new Insets(0, 0, 0, 0), 0,0));
        }

        add(container,new GridBagConstraints(1, 1,REMAINDER,1,1,0,NORTHEAST,BOTH,new Insets(0, 0, 0, 0), 0,0));
        revalidate();
        repaint();
    }

    private Menu createMemberMenu(Team team) {
        Professional[] members = team.getMembers();
        
    }


    public void editTeam(int currentIndex) {
        String[] options = new String[]{"Change Name","Edit Members","Remove","Duplicate","Save and Return"};
        while (true) {
            switch (chooseOptions("What to do with '" + team.getName() + "'?",options,false)) {
                case 0:
                    team.setName(UiManager.prompt("Insert new name: "));
                    break;
                case 1:
                    chooseProfessional();
                    break;
                case 2:
                    player.removeTeam(currentIndex);
                    return;
                case 3:
                    duplicateTeam(currentIndex);
                    return;
                case 4:
                    return;
            }
            team.save(currentIndex);
        }
    }

    public void saveNewTeam() {
        int newIndex = player.loadTeams().length;
        team.save(newIndex);
    }

    public void duplicateTeam(int currentIndex) {

        team.save(currentIndex);
        System.out.println(player.loadTeams()[0].getName());
        String name = team.getName();
        team.setName("Copy of " + name);
        saveNewTeam();
        team.setName(name);
    }

    public void chooseProfessional() {
        Professional[] members = team.getMembers();
        while (true) {
            int profIndex = chooseOptions("What Professional do you wish to edit?",getNames(members),true);
            if (profIndex == members.length) {
                break;
            } else {
                editProfessional(members[profIndex]);
            }

        }
    }

    public void editProfessional(Professional professional) {
        while (true) {
            String prompt = "What to do with " + professional.getName() + "?";
            int action = chooseOptions(prompt,new String[]{"Change base","Change moveset"},true);
            if (action == 2) {
                break;
            } else if (action == 1) {
                editMoveSet(professional);
            } else {
                chooseNewBase(professional);
            }
        }
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
                Move[] values = type == 0 ? Status.values() : Damaging.values();
                printTable(values[0]);

                int newMoveInd = largeOptions("Select new move.",values.length,true);

                if (newMoveInd != -1) {
                    Move move = (type == 0 ? Status.values() : Damaging.values())[newMoveInd];
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
