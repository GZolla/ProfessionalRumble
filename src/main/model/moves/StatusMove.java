package model.moves;

import model.Professional;
import model.data.Branch;
import model.effects.*;
import model.effects.StatModifier;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.Volatile.*;
import static model.data.Stat.*;
import static ui.Main.PLAYER_1;
import static ui.Main.PLAYER_2;


//Moves that do not cause damage but have added effects
public enum StatusMove implements Move {
    //STAT MODIFIER
    OPTIMIZATION("Optimization",NUMBER, new StatModifier(false,SPS,2,-1),0),
    ACCELERATE("Accelerate",TRANSP,new StatModifier(false, SPE,1,-1), 0),
    WORKOUT("Workout",SPORT,new StatModifier(false,STR,2,-1), 0),
    SURVEILL("Surveill",GOVERN,new StatModifier(true,SPR,-2,-1), 0),
    STARVE("Starve",FOOD, new StatModifier(true,SPR,-2,-1), 0),

    //CRIT MODIFIERS
    HYPEREXAMINE("Hyper-examine",DESIGN,new CritModifier(false,8),0),
    SHAME("Shame",ENVIRO,new CritModifier(true,-8),0),

    //STATUS INFLICTORS
    NIHILISTIC_POEM("Nihilistic poem",LETTER,new StatusInflictor(DEMOR,-1),0),
    BAD_INFLUENCE("Bad influence",ENTERT,new StatusInflictor(DEPRE,-1),0),
    CONFUSING_PROBLEM("Confusing problem",NUMBER,new StatusInflictor(NAUSEA,-1),0);

    private String name;
    private Branch branch;
    private Effect effect;
    private int priority;


    StatusMove(String name, Branch branch, Effect effect, int priority) {
        this.name = name;
        this.branch = branch;
        this.effect = effect;
        this.priority = priority;
    }

    @Override
    //EFFECT: apply the added effect
    public void use(boolean usedByPlayer1) {
        effect.apply(usedByPlayer1);
    }
}
