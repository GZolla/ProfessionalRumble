package ui;

import model.Player;
import model.Professional;
import model.data.NonVolatile;
import model.data.ProfessionalBase;
import model.data.Stat;
import model.moves.Move;

import static model.data.NonVolatile.FAINT;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;
import static ui.UiManager.*;

public class Battle {

    //EFFECT: loops through rounds until all of a team is fainted
    public static void main(String[] args) {
        System.out.println("A battle started between " + PLAYER_1.getName() + " and " + PLAYER_2.getName() + ".");
        while (true) {
            int[] action1 = chooseAction(PLAYER_1);
            int[] action2 = chooseAction(PLAYER_2);
            boolean p1GoesFirst = determineOrder(action1,action2);


            Player first = p1GoesFirst ? PLAYER_1 : PLAYER_2;
            Player second = p1GoesFirst ? PLAYER_2 : PLAYER_1;;

            handleEffect(first, p1GoesFirst ? action1 : action2);
            if (second.getSelectedProfessional().getNonVolatileStatus() == FAINT) {
                if (handleFainted(second)) {
                    System.out.println(second.getName() + " was defeated. ");
                    break;
                }
            } else {
                handleEffect(second, p1GoesFirst ? action2 : action1);
                if (first.getSelectedProfessional().getNonVolatileStatus() == FAINT) {
                    if (handleFainted(first)) {
                        System.out.println(first.getName() + " was defeated. ");
                        break;
                    }
                }
            }
        }





    }

    public static int[] chooseAction(Player p) {
        System.out.println(p.getName() + ", choose what to do.");
        while (true) {
            int action = chooseOptions("What do you want to do?",new String[]{"Move","Switch"},false);
            int index;
            if (action == 0) {
                index = chooseMove(p);
            } else {
                index = selectProfessional(true,p);
            }
            if (index != -1) {
                return new int[]{action,index};
            }
        }
    }

    public static int chooseMove(Player p) {
        int moveIndex = chooseOptions("Choose a move",p.getSelectedProfessional().getMoveNames(),true);
        if (p.canUseCritical()) {
            if (chooseOptions("Use critical?", new String[]{"Yes","No"},false) == 0) {
                p.useCritical();
            }
        }
        return moveIndex == 4 ? -1 : moveIndex;
    }

    public static int selectProfessional(boolean enableReturn, Player p) {
        while (true) {
            Professional[] team = p.getTeam();
            printTable(p.getTeamTable(),new String[]{"ID","Name","Life","Status"});
            int profIndex = largeOptions("Choose Professional",6,true);
            if (profIndex == -1) {
                return -1;
            }

            if (team[profIndex].getNonVolatileStatus() == FAINT) {
                System.out.println("Cannot select a fainted professional");
            } else if (profIndex == p.getSelectedIndex()) {
                System.out.println("Professional already selected");
            } else {
                return profIndex;
            }

        }

    }

    public static boolean determineOrder(int[] p1, int[] p2) {
        if (p1[0] + p2[0] >= 1) {
            //Either of them switched
            return p1[0] == 1;
            //Since order is irrelevant if both switch, P1 switches first even if P2 is switching as well
        }

        //None switched
        Professional prof1 = PLAYER_1.getSelectedProfessional();
        Professional prof2 = PLAYER_2.getSelectedProfessional();
        int prio1 = prof1.getMoves()[p1[1]].getPriority();
        int prio2 = prof2.getMoves()[p2[1]].getPriority();
        if (prio1 == prio2) { //Check priority tie
            double speed1 = prof1.getRealStat(Stat.SPE,false);
            double speed2 = prof2.getRealStat(Stat.SPE,false);
            if (speed1 == speed2) { //check speed tie
                ProfessionalBase b1 = prof1.getBase();
                ProfessionalBase b2 = prof2.getBase();
                if (b1.getSpeed() == b2.getSpeed()) { //check base speed tie
                    return b1.getSpeedDistinct() > b2.getSpeedDistinct();
                }
                return b1.getSpeed() > b2.getSpeed();
            }
            return speed1 > speed2;
        }
        return prio1 > prio2;
    }

    public static void handleEffect(Player leader,int[] action) {
        Professional user = leader.getSelectedProfessional();
        Player opponent = user.isLedByPlayer1() ? PLAYER_2 : PLAYER_1;
        if (action[0] == 1) {
            System.out.println(user.getFullName() + " tapped out.");
            Professional newUser = leader.getTeam()[action[1]];
            System.out.println(newUser.getFullName() + " tapped in.");
            leader.setSelectedProfessional(action[1]);
        } else {
            user.useMove(action[1]);
        }
        System.out.println("\n");
    }

    public static boolean handleFainted(Player leader) {
        //If selected professional faints, a new professional must tap in
        Professional[] foeTeam = leader.getTeam();
        for (Professional p : foeTeam) {
            //Check for any professional in foe's team that is not fainted, allow him to choose next if found
            if (p.getNonVolatileStatus() != FAINT) {
                int newFoeInd = selectProfessional(false, leader);
                System.out.println(foeTeam[newFoeInd].getFullName() + " tapped in.");
                leader.setSelectedProfessional(newFoeInd);
                return false;
            }
        }
        return true;
        //If loop goes through all professional have fainted, hence opponent was defeated
    }
}
