package model.effects;

import model.Professional;
import model.Round;
import model.data.Stat;
import ui.Main;


//Effect that changes the stages of target professional, or set a counter do apply the effect later
public class StatModifier extends CounterSetter {

    private Stat[] stats;
    private int stages;
    private boolean targetFoe;


    //EFFECT: Create stat modifier when given an array of Stats, set windowTurns to 1
    public StatModifier(boolean targetFoe, Stat[] stats, int stages, int chargeTurns) {
        this.stats = stats;
        this.stages = stages;
        this.targetFoe = targetFoe;
        this.chargeTurns = chargeTurns;
        windowTurns = 1;
    }

    //EFFECT: Create stat modifier when given a single Stat, set windowTurns to 1
    public StatModifier(boolean targetFoe, Stat stat, int stages, int chargeTurns) {
        this.stats = new Stat[]{stat};
        this.stages = stages;
        this.targetFoe = targetFoe;
        this.chargeTurns = chargeTurns;
        windowTurns = 1;
    }

    @Override
    //EFFECT: changes the stages of Stats in stats array,
    //        target PLAYER_1 if either usedByPlayer1 or targetFoe(but not both), otherwise target PLAYER_2
    public void finalApply(Round round, boolean movedFirst) {
        Professional target = round.getUser(targetFoe ^ movedFirst);

        //Professional target = (targetFoe ^ usedByPlayer1 ? PLAYER_1 : PLAYER_2).getSelectedProfessional();
        for (Stat stat : stats) {
            int oldStage = target.getStage(stat,false);
            int newStage = Math.max(Math.min(oldStage + stages,6),-6);

            if (newStage != oldStage) {
                String prompt = target.getFullName() + "'s " + stat.getName() + (newStage > oldStage ? " in" : " de");
                Main.BATTLEMGR.log(prompt + "creased" + (stages > 1 ? " drastically." : "."));
            }
            target.setStage(stat,newStage);
        }
    }


    @Override
    public String getDescription() {
        String statsMod;
        if (stats.length > 1) {
            statsMod = "";
            for (int i = 0; i < stats.length - 1; i++) {
                statsMod += stats[i].getName() + ", ";
            }
            statsMod = statsMod.substring(0,statsMod.length() - 2) + "&" + stats[stats.length - 1].name();
        } else {
            statsMod = stats[0].getName();
        }

        String action;
        if (chargeTurns == -1) {
            String change = stages < 0 ? "Reduces " : "Increases ";
            String target = targetFoe ? "opponent" : "user";
            action = change + target;
        } else {
            action =  "If used immediately after charging for " + chargeTurns  + " uses, it reduces opponent";
        }
        return action + "'s " + statsMod + "by " + Math.abs(stages) + " stages";

    }
}
