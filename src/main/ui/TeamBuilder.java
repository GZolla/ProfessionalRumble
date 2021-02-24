package ui;

import model.Player;
import model.Professional;
import model.data.ProfessionalBase;
import model.moves.Damaging;
import model.moves.Move;
import model.moves.Status;

import java.util.Scanner;

import static ui.Main.*;
import static ui.UiManager.*;

public class TeamBuilder {

    public static void main(String[] args) {
        System.out.println("Welcome to the team builder and player editor.");

        while (true) {
            String[] names = new String[]{PLAYER_1.getName(),PLAYER_2.getName()};
            int playerInd = chooseOptions("Choose player to edit",names, true);
            if (playerInd < 2) {
                chooseAction(playerInd == 0 ? PLAYER_1 : PLAYER_2);
            } else {
                break;
            }
        }

    }

    public static void chooseAction(Player leader) {
        while (true) {
            String prompt = "Hi," + leader.getName() + ". What do you want to do?";
            int nextTab = chooseOptions(prompt,new String[]{"Change Name","Edit Team"},true);

            switch (nextTab) {
                case 0:
                    System.out.print("Insert new name: ");
                    Scanner sc = new Scanner(System.in);
                    String newName = sc.nextLine();
                    leader.setName(newName);
                    chooseAction(leader);
                    break;
                case 1:
                    chooseProfessional(leader);
                    break;
            }
            if (nextTab == 2) {
                break;
            }
        }

    }

    public static void chooseProfessional(Player leader) {
        while (true) {
            int profIndex = chooseOptions("What Professional do you wish to edit?",leader.getTeamNames(),true);
            if (profIndex == leader.getTeam().length) {
                break;
            } else {
                editProfessional(leader,profIndex);
            }

        }
    }

    public static void editProfessional(Player leader, int profInd) {


        while (true) {
            Professional p1 = leader.getTeam()[profInd];
            String prompt = "What to do with " + p1.getName() + "?";
            int action = chooseOptions(prompt,new String[]{"Change base","Change moveset"},true);
            if (action == 2) {
                break;
            } else if (action == 1) {
                editMoveSet(p1);
            } else {
                chooseNewProfessional(leader, profInd);
            }
        }
    }

    public static void editMoveSet(Professional p1) {
        while (true) {
            int moveInd = chooseOptions("Which move do you want to change?",p1.getMoveNames(), true);
            if (moveInd == p1.getMoves().length) {
                break;
            } else {
                chooseMove(p1,moveInd);
            }
        }


    }

    public static void chooseNewProfessional(Player leader, int profInd) {
        printTable(ProfessionalBase.toTable(),ProfessionalBase.getHeaders());
        int proId = largeOptions("Select new Professional",ProfessionalBase.values().length,true);
        if (proId != -1) {
            Professional[] team = leader.getTeam();
            boolean isPlayer1 = team[profInd].isLedByPlayer1();
            ProfessionalBase base = ProfessionalBase.getByIndex(proId);
            Professional newP = new Professional(isPlayer1,base,team[profInd].getMoves());
            team[profInd] = newP;
            leader.setTeam(team);
        }

    }

    public static void chooseMove(Professional p1, int moveInd) {
        while (true) {
            int type = chooseOptions("What type of move?",new String[]{"Status","Damaging"},true);

            if (type == -1) {
                break;
            } else {
                String[][] values = type == 0 ? Status.toTable() : Damaging.toTable();
                String[] headers = type == 0 ? Status.getHeaders() : Damaging.getHeaders();

                printTable(values,headers);
                int newMoveInd = largeOptions("Select new move.",values.length,true);

                if (newMoveInd != -1) {
                    Move move = (type == 0 ? Status.values() : Damaging.values())[newMoveInd];
                    if (p1.hasMove(move)) {
                        System.out.println("Move already assigned to Professional");
                    } else {
                        String change = " from " + p1.getMoves()[moveInd].getName() + " to " + move.getName();
                        p1.setMove(moveInd,move);

                        System.out.println("Changed move " + (moveInd + 1) + change);
                        break;
                    }
                }

            }

        }
    }
}
