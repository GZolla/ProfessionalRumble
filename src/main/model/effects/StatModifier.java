package model.effects;

import model.Professional;
import model.data.Stat;

import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;

//Effect that changes the stages of target professional, or set a counter do apply the effect later
public class StatModifier extends Counter {

    private Stat[] stats;
    private int stages;
    private boolean targetFoe;


    //EFFECT: Create stat modifier when given an array of Stats, set windowTurns to 1
    public StatModifier(boolean targetFoe, Stat[] stats, int stages, int waitTurns) {
        this.stats = stats;
        this.stages = stages;
        this.targetFoe = targetFoe;
        this.waitTurns = waitTurns;
        windowTurns = 1;
    }

    //EFFECT: Create stat modifier when given a single Stat, set windowTurns to 1
    public StatModifier(boolean targetFoe, Stat stat, int stages, int waitTurns) {
        this.stats = new Stat[]{stat};
        this.stages = stages;
        this.targetFoe = targetFoe;
        this.waitTurns = waitTurns;
        windowTurns = 1;
    }

    @Override
    //EFFECT: changes the stages of Stats in stats array,
    //        target PLAYER_1 if either usedByPlayer1 or targetFoe(but not both), otherwise target PLAYER_2
    public void finalApply(boolean usedByPlayer1) {
        Professional target = (targetFoe ^ usedByPlayer1 ? PLAYER_1 : PLAYER_2).getSelectedProfessional();
        for (Stat stat : stats) {
            int oldStage = target.getStage(stat,false);
            int newStage = Math.max(Math.min(oldStage + stages,6),-6);
            target.setStage(stat,newStage);
        }
    }

    @Override
    //EFFECT: Return exact copy of this, so as to set a counter independent to this
    public Counter getCopy() {
        return new StatModifier(targetFoe,stats,stages,waitTurns);
    }
}
