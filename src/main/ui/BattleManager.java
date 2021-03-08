package ui;

import model.Battle;
import model.Player;
import model.Professional;
import model.Round;

import static model.data.NonVolatile.FAINT;
import static ui.UiManager.*;

public class BattleManager {
    private final Battle battle;


    public BattleManager(Battle battle) {
        this.battle = battle;
    }

    public void resume() {
        while (true) {
            int[] action1 = chooseAction(battle.getPlayer1());
            if (action1[0] == 2) {
                battle.save();
                return;
            }

            int[] action2 = chooseAction(battle.getPlayer2());
            Round newRound = new Round(battle,action1,action2);

            battle.addRound(newRound);

            if (newRound.handleAll() > -1) {
                System.out.println(battle.getResultString());
                battle.save();
                break;
            }
        }
    }


    //EFFECTS: Makes player choose between switching(calls selectProfessional) and using a move(calls chooseMove)
    //        Returns a 2-long int array:
    //            - first position is 0 if using a move or 1 if switching
    //            - second position is either index of move selected or index of professional tapping in
    public int[] chooseAction(Player p) {
        System.out.println(p.getName() + ", choose what to do.");
        while (true) {
            int action = chooseOptions("What do you want to do?",new String[]{"Move","Switch"},false);
            int index;
            if (action == 0) {
                index = chooseMove(p);
            } else {
                index = selectProfessional(p);
            }
            if (index != -1) {
                return new int[]{action,index};
            }
        }
    }

    public int chooseMove(Player p) {
        int moveIndex = chooseOptions("Choose a move",p.getSelectedProfessional().getMoveNames(),true);
        if (p.canUseCritical()) {
            if (chooseOptions("Use critical?", new String[]{"Yes","No"},false) == 0) {
                p.useCritical();
            }
        }
        return moveIndex == 4 ? -1 : moveIndex;
    }

    public static int selectProfessional(Player p) {
        if (!hasAbleReplacements(p)) {
            System.out.println("No able professionals can tap in!");
            return -1;
        }
        boolean selectedCanContinue = p.getSelectedProfessional().getNonVolatileStatus() != FAINT;
        while (true) {
            printTable(p.getTeamTable(),new String[]{"ID","Name","Life","Status"});
            int profIndex = largeOptions("Choose Professional",6,selectedCanContinue);
            if (profIndex == -1) {
                return -1;
            }

            if (p.getTeam().getMembers()[profIndex].getNonVolatileStatus() == FAINT) {
                System.out.println("Cannot select a fainted professional");
            } else if (profIndex == p.getSelectedProfessionalIndex()) {
                System.out.println("Professional already selected");
            } else {
                return profIndex;
            }

        }

    }

    //EFFECTS: Checks if there are non-fainted professionals in team other than the selected professional
    private static boolean hasAbleReplacements(Player p) {
        Professional[] members = p.getTeam().getMembers();
        for (int i = 0; i < members.length; i++) {
            if (members[i].getNonVolatileStatus() != FAINT && i != p.getSelectedProfessionalIndex()) {
                return true;
            }
        }
        return false;
    }

    public static int handleWage(Player p) {
        String promptConclusion = " critical points. How many do you wish to wager?";
        int critPoints1 = p.getCritCounter();
        if (critPoints1 == 0) {
            return 0;
        }
        return largeOptions("You have " + critPoints1 + promptConclusion,critPoints1 + 1,false);
    }
}
