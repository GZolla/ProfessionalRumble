package ui;

import model.Player;
import model.Professional;
import model.Team;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import model.moves.Status;
import persistence.SaveAble;

import java.util.Scanner;

import static ui.UiManager.*;

public class AccountManager {
    
    private final Player player;
    private Team team;

    public AccountManager(Player player) {
        this.player = player;
        System.out.println("Hi," + player.getName() + ".");
        chooseAction();

    }


    //MODIFIES: this
    //EFFECTS: choose to either change name, edit existing teams or create a new one
    private void chooseAction() {
        String[] options = new String[]{"Change Name","Edit Teams","Create New Team"};
        boolean end = false;
        while (!end) {
            int nextTab = chooseOptions("What do you want to do?",options,true);
            switch (nextTab) {
                case 0:
                    chooseNewName();
                    break;
                case 1:
                    chooseTeam();
                    break;
                case 2:
                    createNewTeam();
                    break;
                case 3:
                    end = true;
                    break;
            }
        }
    }

    public void chooseNewName() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("Insert new name, or -1 to return: ");
            String newName = sc.nextLine();
            if (newName.equals("-1")) {
                break;
            } else {
                if (new UserManager().findUser(newName) != null) {
                    System.out.println("Name is already taken!");
                } else {
                    player.setName(newName);
                    break;
                }
            }
        }
    }


    public void createNewTeam() {

        System.out.println("Insert a name, or -1 to return:");
        Scanner sc = new Scanner(System.in);
        String newName = sc.nextLine();
        if (!newName.equals("-1")) {
            team = new Team(player,newName);
            saveNewTeam();
            chooseTeam();
        }

    }

    public void chooseTeam() {
        while (true) {
            int teamIndex = player.selectTeam();
            SaveAble[] teams = player.loadTeams();
            if (teamIndex != teams.length) {
                team = (Team) teams[teamIndex];
                editTeam(teamIndex);
            } else {
                break;
            }
        }
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
        editTeam(newIndex);
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
