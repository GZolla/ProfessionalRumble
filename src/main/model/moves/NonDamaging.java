package model.moves;

import model.Round;
import model.data.Branch;
import model.effects.*;
import model.effects.StatModifier;
import ui.TableAble;

import static model.data.Branch.*;
import static model.data.NonVolatile.*;
import static model.data.Volatile.*;
import static model.data.Stat.*;
import static model.effects.CriticalModifier.MAXCRITS;


//Moves that do not cause damage but have added effects
public enum NonDamaging implements Move {
    //CriticalModifier
    HYPEREXAMINE("Hyper-examine",DESIGN,new CriticalModifier(MAXCRITS)),
    SHAME("Shame",ENVIRO,new CriticalModifier(-MAXCRITS)),
    //DefeatCondition
    //FailCondition
    //Priority
    PROTECT("Protect",SECURI,new Priority(6)),
    //StatModifier
    OPTIMIZATION("Optimization",NUMBER,new StatModifier(false,SPS,2,0)),
    ACCELERATE("Accelerate",TRANSP,new StatModifier(false,SPE,1,0)),
    WORKOUT("Workout",SPORT,new StatModifier(false,STR,2,0)),
    SURVEILL("Surveill",GOVERN,new StatModifier(true,SPR,-2,0)),
    STARVE("Starve",FOOD,new StatModifier(true,SPR,-2,0)),
    //StatusInflictor
    NIHILISTIC_POEM("Nihilistic poem",LETTER,new StatusInflictor(DEMOR,0)),
    BAD_INFLUENCE("Bad influence",ENTERT,new StatusInflictor(DEPRE,0)),
    CONFUSING_PROBLEM("Confusing problem",NUMBER,new StatusInflictor(NAUSEA,0));


    private String name;
    private Branch branch;
    private Effect effect;

    NonDamaging(String name, Branch branch, Effect effect) {
        this.name = name;
        this.branch = branch;
        this.effect = effect;
    }

    @Override
    //EFFECT: apply the added effect
    public void use(Round round, boolean movedFirst) {
        if (!effect.fails(round,movedFirst)) {
            effect.apply(round, movedFirst);
        }
    }



    @Override
    public Effect getEffect() {
        return effect;
    }



//--- TABLE ABLE METHODS -----------------------------------------------------------------------------------------------
    @Override
    public int getIndex() {
        return ordinal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    //EFFECTS: Return name, branch, and description of effect in a String array
    public String[] toRow() {
        return new String[]{getIndex() + "", name, branch.getName(),effect.getDescription()};
    }

    @Override
    //EFFECT: Return the headers for the table displaying values of Status
    public String[] getHeaders() {
        return new String[]{"ID","Name","Branch","Added Effect"};
    }

    @Override
    public TableAble[] getValues() {
        return NonDamaging.values();
    }



}
